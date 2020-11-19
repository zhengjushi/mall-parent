package com.juju.mall.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.SpuImage;
import com.juju.mall.entity.SpuInfo;
import com.juju.mall.entity.SpuSaleAttr;
import com.juju.mall.entity.SpuSaleAttrValue;
import com.juju.mall.mapper.SpuInfoMapper;
import com.juju.mall.service.SpuImageService;
import com.juju.mall.service.SpuInfoService;
import com.juju.mall.service.SpuSaleAttrService;
import com.juju.mall.service.SpuSaleAttrValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoMapper, SpuInfo> implements SpuInfoService {

    @Autowired
    private SpuSaleAttrService spuSaleAttrService;

    @Autowired
    private SpuSaleAttrValueService spuSaleAttrValueService;

    @Autowired
    private SpuImageService spuImageService;

    @Override
    public IPage<SpuInfo> spuList(IPage<SpuInfo> iPage, String category3Id) {

        QueryWrapper<SpuInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",category3Id);
        IPage<SpuInfo> pageList = baseMapper.selectPage(iPage, wrapper);
        return pageList;
    }

    @Transactional
    @Override
    public void saveSpuInfo(SpuInfo spuInfo) {
        //保存spuInfo
        baseMapper.insert(spuInfo);
        //保存SpuSaleAttr
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr : spuSaleAttrList) {
            spuSaleAttr.setSpuId(spuInfo.getId());
            //保存SpuSaleAttrValue
            List<SpuSaleAttrValue> spuSaleAttrValueList = spuSaleAttr.getSpuSaleAttrValueList();
            for (SpuSaleAttrValue spuSaleAttrValue : spuSaleAttrValueList) {
                spuSaleAttrValue.setSpuId(spuInfo.getId());
                spuSaleAttrValue.setSaleAttrName(spuSaleAttr.getSaleAttrName());
            }
            spuSaleAttrValueService.saveBatch(spuSaleAttrValueList);
        }
        spuSaleAttrService.saveBatch(spuSaleAttrList);
        //保存spuImageList
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage : spuImageList) {
            spuImage.setSpuId(spuInfo.getId());
        }
        spuImageService.saveBatch(spuImageList);
    }



    @Override
    public List<SpuSaleAttr> getSpuSaleAttrs(Long spuId, Long skuId) {
        List<SpuSaleAttr> spuSaleAttrs =  spuSaleAttrService.selectSpuSaleAttrListCheckBySku(spuId,skuId);
        return spuSaleAttrs;
    }
}
