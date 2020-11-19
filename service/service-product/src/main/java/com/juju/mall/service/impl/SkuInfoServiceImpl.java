package com.juju.mall.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.juju.mall.common.cache.GmallCache;
import com.juju.mall.constant.RedisConst;
import com.juju.mall.entity.*;
import com.juju.mall.list.Goods;
import com.juju.mall.list.SearchAttr;
import com.juju.mall.mapper.SkuInfoMapper;
import com.juju.mall.service.*;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoMapper, SkuInfo> implements SkuInfoService {

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Autowired
    private SkuImageService skuImageService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private BaseTrademarkService baseTrademarkService;

    @Autowired
    private BaseCategoryViewService baseCategoryViewService;

    @Autowired
    private BaseAttrInfoService baseAttrInfoService;

    @Override
    public void saveSkuInfo(SkuInfo skuInfo) {
        baseMapper.insert(skuInfo);
        List<SkuAttrValue> skuAttrValueList = skuInfo.getSkuAttrValueList();
        for (SkuAttrValue skuAttrValue : skuAttrValueList) {
            skuAttrValue.setSkuId(skuInfo.getId());
            skuAttrValueService.save(skuAttrValue);
        }
        List<SkuImage> skuImageList = skuInfo.getSkuImageList();
        for (SkuImage skuImage : skuImageList) {
            skuImage.setSkuId(skuInfo.getId());
        }
        skuImageService.saveBatch(skuImageList);

        List<SkuSaleAttrValue> skuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();
        for (SkuSaleAttrValue skuSaleAttrValue : skuSaleAttrValueList) {
            skuSaleAttrValue.setSkuId(skuInfo.getId());
            skuSaleAttrValue.setSpuId(skuInfo.getSpuId());
        }
        skuSaleAttrValueService.saveBatch(skuSaleAttrValueList);

    }

    @GmallCache(prefix = RedisConst.SKUKEY_PREFIX)
    @Override
    public SkuInfo getSkuById(String skuId) {
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        return skuInfo;
    }

    /**
     * redisson 实现分布式锁
     * @param skuId
     * @return
     */
    public SkuInfo getSkuById2(String skuId) {
        SkuInfo skuInfo = null;
        try {
            String skuKey = RedisConst.SKUKEY_PREFIX+skuId+RedisConst.SKUKEY_SUFFIX;
            skuInfo = (SkuInfo) redisTemplate.opsForValue().get(skuKey);
            if (skuInfo == null){
                //直接获取数据库中的数据，可能会造成缓存击穿。所以在这个位置，应该添加锁
                String lockKey = RedisConst.SKUKEY_PREFIX+skuId+RedisConst.SKUKEY_SUFFIX;
                RLock lock = redissonClient.getLock(lockKey);
                //加锁
                boolean res = lock.tryLock(RedisConst.SKULOCK_EXPIRE_PX1, RedisConst.SKULOCK_EXPIRE_PX2, TimeUnit.SECONDS);
                if (res){
                    try {
                        //真正获取数据库中的数据 判断数据库中有没有这个数据，防止缓存穿透
                        skuInfo = baseMapper.selectById(skuId);
                        if (skuInfo == null){
                            //为了避免缓存穿透 应该给空的对象放入缓存
                            SkuInfo skuInfo1 = new SkuInfo();
                            redisTemplate.opsForValue().set(skuKey,skuInfo1,RedisConst.SKUKEY_TEMPORARY_TIMEOUT,TimeUnit.SECONDS);
                            return skuInfo1;
                        }
                        //查询数据库的时候有值
                        redisTemplate.opsForValue().set(skuKey,skuInfo,RedisConst.SKUKEY_TIMEOUT,TimeUnit.SECONDS);
                        return skuInfo;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }else {
                    return getSkuById(skuId);
                }
            }else {
                //如果用户查询的数据在数据库中根本不存在的时候第一次会将一个空对象直接放入缓存。
                //那么第二次查询的时候，缓存中有一个空对象 防止缓存穿透
                if (null == skuInfo.getId()){
                    return null;
                }
                //缓存数据不为空
                return skuInfo;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //为了防止缓存宕机，从数据库中获取数据
        return baseMapper.selectById(skuId);
    }

    @Override
    public BigDecimal getSkuPrice(String skuId) {
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        return skuInfo.getPrice();
    }

    @GmallCache(prefix = "image")
    @Override
    public List<SkuImage> getSkuImages(String skuId) {
        QueryWrapper<SkuImage> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id",skuId);
        List<SkuImage> list = skuImageService.list(wrapper);
        return list;
    }

    @GmallCache(prefix = "saleAttrValue")
    @Override
    public List<Map<String, Object>> getSaleAttrValueBySpuId(Long spuId) {
        List<Map<String, Object>> maps = skuSaleAttrValueService.selectSaleAttrValueBySpuId(spuId);
        return maps;
    }

    @Override
    public Goods getGoodsBySkuId(String skuId) {
        Goods goods = new Goods();
        SkuInfo skuInfo = baseMapper.selectById(skuId);
        BaseTrademark baseTrademark = baseTrademarkService.getById(skuInfo.getTmId());
        QueryWrapper<BaseCategoryView> wrapper = new QueryWrapper<>();
        wrapper.eq("category3_id",skuInfo.getCategory3Id());
        BaseCategoryView categoryView = baseCategoryViewService.getOne(wrapper);

        goods.setId(Long.valueOf(skuId));
        goods.setDefaultImg(skuInfo.getSkuDefaultImg());
        goods.setTitle(skuInfo.getSkuName());
        goods.setPrice(skuInfo.getPrice().doubleValue());
        goods.setCreateTime(new Date());
        goods.setTmId(skuInfo.getTmId());
        goods.setTmName(baseTrademark.getTmName());
        goods.setTmLogoUrl(baseTrademark.getLogoUrl());
        goods.setCategory1Id(categoryView.getCategory1Id());
        goods.setCategory1Name(categoryView.getCategory1Name());
        goods.setCategory2Id(categoryView.getCategory2Id());
        goods.setCategory2Name(categoryView.getCategory2Name());
        goods.setCategory3Id(categoryView.getCategory3Id());
        goods.setCategory3Name(categoryView.getCategory3Name());

        List<SearchAttr> searchAttrs = baseAttrInfoService.selectBaseAttrInfoListBySkuId(skuId);
        goods.setAttrs(searchAttrs);
        return goods;
    }
}