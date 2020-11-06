package com.juju.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.entity.BaseCategory3;
import com.juju.mall.entity.BaseCategoryView;

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

}
