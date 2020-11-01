package com.juju.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.entity.BaseAttrInfo;
import com.juju.mall.entity.BaseAttrValue;
import com.juju.mall.mapper.BaseAttrInfoMapper;
import com.juju.mall.service.BaseAttrInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.service.BaseAttrValueService;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private BaseAttrValueService baseAttrValueService;

    @Override
    public List<BaseAttrInfo> attrInfoList(String category1Id, String category2Id, String category3Id) {
        String categoryId = category1Id;
        int categoryLevel = 1;
        if ("0".equals(category3Id)){
            categoryLevel = 2;
            categoryId = category2Id;
        }else {
            categoryLevel = 3;
            categoryId = category3Id;
        }
        List<BaseAttrInfo> list = baseMapper.selectAttrS(categoryId);
        System.out.println(list);
        return list;
    }

    @Override
    @Transactional
    public void saveAttrInfo(BaseAttrInfo baseAttrInfo) {
        //判断是否有Id，有Id是修改，没有id是添加
        Long id ;
        if (StringUtils.isEmpty(baseAttrInfo.getId())){
            //添加
            //先保存属性
            baseMapper.insert(baseAttrInfo);
            id = baseAttrInfo.getId();

        }else {
            //跟新属性
            baseMapper.updateById(baseAttrInfo);
            //删除原本的属性值
            QueryWrapper<BaseAttrValue> wrapper = new QueryWrapper<>();
            id = baseAttrInfo.getId();
            wrapper.eq("attr_id",id);
            baseAttrValueService.remove(wrapper);
        }
        //在保存属性值
        List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();
        for (BaseAttrValue baseAttrValue : attrValueList) {
            baseAttrValue.setAttrId(id);
        }
        baseAttrValueService.saveBatch(attrValueList);
    }
}
