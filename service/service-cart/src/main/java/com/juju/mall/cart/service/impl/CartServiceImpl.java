package com.juju.mall.cart.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.juju.mall.cart.CartInfo;
import com.juju.mall.cart.mapper.CartMapper;
import com.juju.mall.cart.service.CartService;
import com.juju.mall.entity.SkuInfo;
import com.juju.mall.product.client.ProductFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    @Transactional
    public void addCart(CartInfo cartInfo) {
        //判断数据库中是否有当前 userId添加过的skuId
        QueryWrapper<CartInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",cartInfo.getUserId());
        wrapper.eq("sku_id",cartInfo.getSkuId());

        CartInfo cartInfoExist = cartMapper.selectOne(wrapper);
        SkuInfo skuInfo = productFeignClient.getSkuById(String.valueOf(cartInfo.getSkuId()));
        CartInfo cartInfoCache = new CartInfo();

        if (cartInfoExist!=null){
            //修改购物车数量
            Integer skuNumAdd = cartInfo.getSkuNum();
            Integer skuNumExist = cartInfoExist.getSkuNum();

            cartInfoExist.setSkuNum((new BigDecimal(skuNumAdd).add(new BigDecimal(skuNumExist))).intValue());
            cartInfoExist.setCartPrice(skuInfo.getPrice().multiply(new BigDecimal(cartInfoExist.getSkuNum())));
            cartMapper.update(cartInfoExist,wrapper);

            cartInfoCache = cartInfoExist;

        }else {
            //添加新购物车

            cartInfo.setCartPrice(skuInfo.getPrice().multiply(new BigDecimal(cartInfo.getSkuNum())));
            cartInfo.setIsChecked(1);
            cartInfo.setImgUrl(skuInfo.getSkuDefaultImg());
            cartInfo.setSkuName(skuInfo.getSkuName());
            cartMapper.insert(cartInfo);
            cartInfoCache = cartInfo;
        }

        //同步缓存
        redisTemplate.opsForHash().put("user:"+cartInfo.getUserId()+":cart",cartInfo.getSkuId()+"",cartInfoCache);
    }

    //@GmallCache
    @Override
    public List<CartInfo> cartList(String userId) {

        QueryWrapper<CartInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<CartInfo> cartInfoList = cartMapper.selectList(wrapper);
        for (CartInfo cartInfo : cartInfoList) {
            SkuInfo skuInfo = productFeignClient.getSkuById(String.valueOf(cartInfo.getSkuId()));
            cartInfo.setSkuPrice(skuInfo.getPrice());
        }
        return cartInfoList;
    }

    @Override
    public List<CartInfo> getCartCheckedList(String userId) {
        List<CartInfo> cartCheckedList = new ArrayList<>();
        QueryWrapper<CartInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        List<CartInfo> cartInfoList = cartMapper.selectList(wrapper);
        for (CartInfo cartInfo : cartInfoList) {
            if (cartInfo.getIsChecked() == 1){
                cartCheckedList.add(cartInfo);
            }
        }
        return cartCheckedList;
    }
}
