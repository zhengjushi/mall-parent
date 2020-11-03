package com.juju.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.entity.SkuImage;
import com.juju.mall.mapper.SkuImageMapper;
import com.juju.mall.service.SkuImageService;
import org.springframework.stereotype.Service;

@Service
public class SkuImageServiceImpl extends ServiceImpl<SkuImageMapper, SkuImage> implements SkuImageService {
}
