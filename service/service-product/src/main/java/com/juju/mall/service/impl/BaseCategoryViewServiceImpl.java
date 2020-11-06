package com.juju.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.BaseCategory3;
import com.juju.mall.entity.BaseCategoryView;
import com.juju.mall.mapper.BaseCategory3Mapper;
import com.juju.mall.mapper.BaseCategoryViewMapper;
import com.juju.mall.service.BaseCategory3Service;
import com.juju.mall.service.BaseCategoryViewService;
import org.springframework.stereotype.Service;

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
}
