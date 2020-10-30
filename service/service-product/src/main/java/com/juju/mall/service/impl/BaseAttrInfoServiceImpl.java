package com.juju.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.entity.BaseAttrInfo;
import com.juju.mall.mapper.BaseAttrInfoMapper;
import com.juju.mall.service.BaseAttrInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 属性表 服务实现类
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo> implements BaseAttrInfoService {

    @Override
    public List<BaseAttrInfo> attrInfoList(String category1Id, String category2Id, String category3Id) {
        String categoryId = category1Id;
        int categoryLevel = 1;
        if (StringUtils.equals("0",category3Id)){
            categoryLevel = 2;
            categoryId = category2Id;
        }else {
            categoryLevel = 3;
            categoryId = category3Id;
        }
        QueryWrapper<BaseAttrInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category_id",categoryId);
        wrapper.eq("category_level",categoryLevel);
        return baseMapper.selectList(wrapper);
    }
}
