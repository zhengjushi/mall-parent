package com.juju.mall.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.mapper.BaseCategoryViewMapper;
import com.juju.mall.service.BaseCategoryViewService;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 三级分类表 服务实现类
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
@Service
public class BaseCategoryViewServiceImpl extends ServiceImpl<BaseCategoryViewMapper, BaseCategoryView> implements BaseCategoryViewService {

    @Override
    public BaseCategoryView getCategoryView(Long category3Id) {
        QueryWrapper<BaseCategoryView> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",category3Id);
        BaseCategoryView baseCategoryView = baseMapper.selectOne(wrapper);
        return baseCategoryView;
    }

    @Override
    public List<JSONObject> categoryList() {
        List<BaseCategoryView> baseCategoryViews = baseMapper.selectList(null);

        List<JSONObject> categoryList = new ArrayList<>();
        Map<Long,List<BaseCategoryView>> category1Map = baseCategoryViews.stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory1Id));
        for (Map.Entry<Long,List<BaseCategoryView>> category1group: category1Map.entrySet() ){
            //1级分类的id和名称
            Long category1Id = category1group.getKey();
            String category1Name = category1group.getValue().get(0).getCategory1Name();

            //封装1级分类
            JSONObject category1 = new JSONObject();
            category1.put("categoryId",category1Id);
            category1.put("categoryName",category1Name);

            //封装2级分类
            List<JSONObject> category2List = new ArrayList<>();
            Map<Long,List<BaseCategoryView>> category2Map = category1group.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory2Id));
            for (Map.Entry<Long,List<BaseCategoryView>> category2group : category2Map.entrySet()){
                //二级分类的id和名称
                Long category2Id = category2group.getKey();
                String category2Name = category2group.getValue().get(0).getCategory2Name();

                //封装二级分类
                JSONObject category2 = new JSONObject();
                category2.put("categoryId",category2Id);
                category2.put("categoryName",category2Name);


                //封装3级分类
                List<JSONObject> category3List = new ArrayList<>();
                Map<Long, List<BaseCategoryView>> category3Map = category2group.getValue().stream().collect(Collectors.groupingBy(BaseCategoryView::getCategory3Id));
                for (Map.Entry<Long,List<BaseCategoryView>> category3group : category3Map.entrySet()){

                    //三级分类的id和name
                    Long category3Id = category3group.getKey();
                    String category3Name = category3group.getValue().get(0).getCategory3Name();

                    //封装三级分类
                    JSONObject category3 = new JSONObject();
                    category3.put("categoryId",category3Id);
                    category3.put("categoryName",category3Name);

                    category3List.add(category3);
                }

                category2.put("categoryChild",category3List);
                category2List.add(category2);
            }
            category1.put("categoryChild",category2List);
            categoryList.add(category1);
        }
        return categoryList;
    }
}
