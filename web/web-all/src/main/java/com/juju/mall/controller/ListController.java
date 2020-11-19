package com.juju.mall.controller;

import com.alibaba.fastjson.JSONObject;
import com.juju.mall.list.SearchAttr;
import com.juju.mall.list.SearchParam;
import com.juju.mall.list.SearchResponseVo;
import com.juju.mall.list.client.ListFeignClient;
import com.juju.mall.product.client.ProductFeignClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ListController {

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private ListFeignClient listFeignClient;

    @RequestMapping("index.html")
    public String index(Model model) {
        List<JSONObject> categoryList = productFeignClient.categoryList();
        model.addAttribute("list", categoryList);
        return "index/index";
    }

    @GetMapping({"list.html", "search.html"})
    public String list(Model model, SearchParam searchParam) {
        SearchResponseVo searchResponseVo = listFeignClient.list(searchParam);
        model.addAttribute("goodsList", searchResponseVo.getGoodsList());
        model.addAttribute("attrsList", searchResponseVo.getAttrsList());
        model.addAttribute("trademarkList", searchResponseVo.getTrademarkList());
        model.addAttribute("urlParam", getUrlParam(searchParam));

        if (null!=searchParam.getProps()&&searchParam.getProps().length>0){
            List<SearchAttr> searchAttrs = new ArrayList<>();
            for (String prop : searchParam.getProps()){
                SearchAttr searchAttr = new SearchAttr();
                String[] split = prop.split(":");

                Long attrId = Long.parseLong(split[0]);
                String attrValueName = split[1];
                String attrName = split[2];
                searchAttr.setAttrId(attrId);
                searchAttr.setAttrName(attrName);
                searchAttr.setAttrValue(attrValueName);
                searchAttrs.add(searchAttr);
            }
            model.addAttribute("propsParamList",searchAttrs);
        }

        if (StringUtils.isNotBlank(searchParam.getTrademark())){
            model.addAttribute("trademarkParam",searchParam.getTrademark().split(":")[1]);
        }

        //排序
        if (StringUtils.isNotBlank(searchParam.getOrder())){
            String order = searchParam.getOrder();
            String[] split = order.split(":");
            String type = split[0];
            String sort = split[1];
            Map<String,String> orderMap = new HashMap<>();
            orderMap.put("type",type);
            orderMap.put("sort",sort);
            model.addAttribute("orderMap",orderMap);
        }

        return "list/index";
    }

    private String getUrlParam(SearchParam searchParam) {
        StringBuffer urlParam = new StringBuffer("list.html?");

        Long category3Id = searchParam.getCategory3Id();
        String trademark = searchParam.getTrademark();
        String keyword = searchParam.getKeyword();
        String[] props = searchParam.getProps();

        if (category3Id != null && category3Id > 0) {
            urlParam.append("category3Id=" + category3Id);
        }

        if (StringUtils.isNotBlank(keyword)) {
            urlParam.append("keyword=" + keyword);
        }

        if (StringUtils.isNotBlank(trademark)) {
            urlParam.append("&trademark=" + trademark);
        }

        if (props != null && props.length > 0) {
            for (String prop : props) {
                urlParam.append("&props="+prop);
            }
        }
        return urlParam.toString();
    }


}
