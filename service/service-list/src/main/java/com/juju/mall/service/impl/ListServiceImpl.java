package com.juju.mall.service.impl;

import com.alibaba.fastjson.JSON;
import com.juju.mall.list.*;
import com.juju.mall.product.client.ProductFeignClient;
import com.juju.mall.repository.GoodsRepository;
import com.juju.mall.result.Result;
import com.juju.mall.service.ListService;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListServiceImpl implements ListService {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public void onSale(String skuId) {
        Goods goods = productFeignClient.getGoodsBySkuId(skuId);
        createGoods();
        goodsRepository.save(goods);
    }

    @Override
    public void cancelSale(String skuId) {
        goodsRepository.deleteById(Long.valueOf(skuId));
    }

    /**
     * 统计热度值并同步到es中
     *
     * @param skuId
     */
    @Override
    public void hotScore(String skuId) {
        Long hotScore = redisTemplate.opsForValue().increment("sku:" + skuId + ":hotScore", 1l);

        if (hotScore % 10 == 0) {
            Optional<Goods> byId = goodsRepository.findById(Long.valueOf(skuId));
            Goods goods = byId.get();
            goods.setHotScore(hotScore);
            goodsRepository.save(goods);
        }
    }

    @Override
    public SearchResponseVo list(SearchParam searchParam) {
        SearchResponseVo searchResponseVo = null;
        //封装查询请求
        SearchRequest searchRequest = new SearchRequest();
        searchRequest = getSearchDSL(searchParam); //dsl语句

        try {
            SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            //解析返回结果
            searchResponseVo = parseSearchResponse(search); //返回结果
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResponseVo;
    }

    /**
     * 封装请求语句
     *
     * @param searchParam
     * @return
     */
    private SearchRequest getSearchDSL(SearchParam searchParam) {
        Long category3Id = searchParam.getCategory3Id();
        String keyword = searchParam.getKeyword();
        String[] props = searchParam.getProps();
        String trademark = searchParam.getTrademark();

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("goods");
        searchRequest.types("info");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //三级分类
        //判断category3Id是否为空
        if (category3Id != null && category3Id > 0) {
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("category3Id", category3Id);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //关键字
        //判断keyword是否为空
        if (StringUtils.isNotBlank(keyword)) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("title", keyword);
            boolQueryBuilder.must(matchQueryBuilder);

            //高亮设置
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("title");
            highlightBuilder.preTags("<span style='color:red;font-weight:bolder'>");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlighter(highlightBuilder);

        }

        //属性
        if (null != props && props.length > 0) {
            for (String prop : props) {
                String[] split = prop.split(":");

                Long attrId = Long.valueOf(split[0]);
                String attrValueName = split[1];
                String attrName = split[2];

                BoolQueryBuilder boolQueryBuilderForNested = new BoolQueryBuilder();
                //属性id
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("attrs.attrId", attrId);
                boolQueryBuilderForNested.filter(termQueryBuilder);
                //属性值名称
                MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("attrs.attrValue", attrValueName);
                boolQueryBuilderForNested.must(matchQueryBuilder);
                NestedQueryBuilder nestedQueryBuilder = new NestedQueryBuilder("attrs", boolQueryBuilderForNested, ScoreMode.None);

                boolQueryBuilder.filter(nestedQueryBuilder);
            }
        }

        //品牌
        if (StringUtils.isNotBlank(trademark)) {
            //取出商标参数中的tmId
            Long tmId = Long.parseLong(trademark.split(":")[0]);
            TermQueryBuilder termQueryBuilder = new TermQueryBuilder("tmId", tmId);
            boolQueryBuilder.filter(termQueryBuilder);
        }

        //排序
        if (StringUtils.isNotBlank(searchParam.getOrder())){
            String order = searchParam.getOrder();
            String[] split = order.split(":");
            String type = split[0];
            String sort = split[1];

            String name = "hotScore";
            if (type.equals("2")){
                name = "price";
            }
            searchSourceBuilder.sort(name,sort.equals("asc")? SortOrder.ASC:SortOrder.DESC);
        }

        //检索
        searchSourceBuilder.query(boolQueryBuilder);

        //聚合
        searchSourceBuilder.aggregation(AggregationBuilders.terms("tmIdAgg").field("tmId")
                .subAggregation(AggregationBuilders.terms("tmNameAgg").field("tmName"))
                .subAggregation(AggregationBuilders.terms("tmLogoUrlAgg").field("tmLogoUrl")));


        //属性聚合 AggregationBuilders.terms("tmIdAgg").field("tmId")
        searchSourceBuilder.aggregation(AggregationBuilders.nested("attrsAgg", "attrs")
                .subAggregation(AggregationBuilders.terms("attrIdAgg").field("attrs.attrId")
                        .subAggregation(AggregationBuilders.terms("attrNameAgg").field("attrs.attrName"))
                        .subAggregation(AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue"))));


        System.out.println(searchSourceBuilder); //打印dsl语句
        searchRequest.source(searchSourceBuilder); //检索条件

        return searchRequest;
    }

    /**
     * 解析返回结果
     *
     * @param search
     * @return
     */
    private SearchResponseVo parseSearchResponse(SearchResponse search) {

        SearchResponseVo searchResponseVo = new SearchResponseVo();
        SearchHit[] hits = search.getHits().getHits();
        if (search.getHits().totalHits > 0) {
            List<Goods> goodsList = new ArrayList<>();

            for (SearchHit documentFields : hits) {
                String sourceAsJSON = documentFields.getSourceAsString();
                Goods goods = JSON.parseObject(sourceAsJSON, Goods.class);
                //判断时否设置了高亮
                Map<String, HighlightField> highlightFields = documentFields.getHighlightFields();
                if (null!=highlightFields&&highlightFields.size()>0){
                    HighlightField highlightField = highlightFields.get("title");
                    Text[] fragments = highlightField.getFragments();
                    Text titleTextHighlight = fragments[0];
                    goods.setTitle(titleTextHighlight.toString());
                }
                goodsList.add(goods);
            }
            searchResponseVo.setGoodsList(goodsList);
            //解析商标聚合
            ParsedLongTerms tmIdParsedLongTerms = search.getAggregations().get("tmIdAgg");

            List<SearchResponseTmVo> searchResponseTmVos = tmIdParsedLongTerms.getBuckets().stream().map(tmIdBucket -> {

                SearchResponseTmVo searchResponseTmVo = new SearchResponseTmVo();
                //Id
                Long tmIdKey = (Long) tmIdBucket.getKey();
                searchResponseTmVo.setTmId(tmIdKey);

                //name
                ParsedStringTerms tmNameParsedStringTerms = tmIdBucket.getAggregations().get("tmNameAgg");
                List<String> tmNames = tmNameParsedStringTerms.getBuckets().stream().map(tmNameBucket -> {
                    return tmNameBucket.getKeyAsString();
                }).collect(Collectors.toList());
                searchResponseTmVo.setTmName(tmNames.get(0));

                //logo
                ParsedStringTerms tmLogoUrlParsedStringTerms = tmIdBucket.getAggregations().get("tmLogoAgg");
                List<String> tmLogoUrl = tmNameParsedStringTerms.getBuckets().stream().map(tmLogoBucket -> {
                    return tmLogoBucket.getKeyAsString();
                }).collect(Collectors.toList());
                searchResponseTmVo.setTmLogoUrl(tmLogoUrl.get(0));


                return searchResponseTmVo;
            }).collect(Collectors.toList());

            searchResponseVo.setTrademarkList(searchResponseTmVos);

            //解析属性聚合
            ParsedNested attrsAggParsedNested = (ParsedNested) search.getAggregations().get("attrsAgg");
            ParsedLongTerms attrIdParsedLongTerms = attrsAggParsedNested.getAggregations().get("attrIdAgg");

            List<SearchResponseAttrVo> searchResponseAttrVos = attrIdParsedLongTerms.getBuckets().stream().map(attrIdBucket -> {
                SearchResponseAttrVo searchResponseAttrVo = new SearchResponseAttrVo();

                //id
                Long attrIdKey = (Long) attrIdBucket.getKey();
                searchResponseAttrVo.setAttrId(attrIdKey);

                //attrName
                ParsedStringTerms attrNameParsedStringTerms = attrIdBucket.getAggregations().get("attrNameAgg");
                String attrName = attrNameParsedStringTerms.getBuckets().get(0).getKeyAsString();
                searchResponseAttrVo.setAttrName(attrName);

                // attrValue
                ParsedStringTerms attrValueParsedStringTerms = attrIdBucket.getAggregations().get("attrValueAgg");
                List<String> attrValueList = attrValueParsedStringTerms.getBuckets().stream().map(attrValueBucket -> {
                    return attrValueBucket.getKeyAsString();
                }).collect(Collectors.toList());

                searchResponseAttrVo.setAttrValueList(attrValueList);
                return searchResponseAttrVo;
            }).collect(Collectors.toList());

            searchResponseVo.setAttrsList(searchResponseAttrVos);
        }
        return searchResponseVo;
    }

    /**
     * 创建索引映射模型
     */
    public void createGoods() {
        elasticsearchRestTemplate.createIndex(Goods.class);
        elasticsearchRestTemplate.putMapping(Goods.class);
    }
}
