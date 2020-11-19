package com.juju.mall.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.BaseCategory3;
import com.juju.mall.entity.BaseCategoryView;

import java.util.List;

/**
 * <p>
 * 三级分类表 服务类
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
public interface BaseCategoryViewService extends IService<BaseCategoryView> {

    BaseCategoryView getCategoryView(Long category3Id);

    List<JSONObject> categoryList();

}
