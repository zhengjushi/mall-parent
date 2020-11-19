package com.juju.mall.order.service.impl;

import com.juju.mall.cart.CartInfo;
import com.juju.mall.client.cart.CartFeignClient;
import com.juju.mall.order.OrderDetail;
import com.juju.mall.order.OrderInfo;
import com.juju.mall.order.service.OrderService;
import com.juju.mall.product.client.ProductFeignClient;
import com.juju.mall.user.UserAddress;
import com.juju.mall.user.client.UserFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartFeignClient cartFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Override
    public Map<String, Object> trade(String userId) {

        //获取用户地址信息
        List<UserAddress> userAddressList = userFeignClient.findUserAddressListByUserId(userId);

        //获取选中的商品集合
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);

        //设置skuPrice
        for (CartInfo cartInfo : cartCheckedList) {
            BigDecimal skuPrice = productFeignClient.getSkuPrice(String.valueOf(cartInfo.getSkuId()));
            cartInfo.setSkuPrice(skuPrice);
        }

        //声明一个集合来存储订单明细
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CartInfo cartInfo : cartCheckedList) {
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setSkuId(cartInfo.getSkuId());
            orderDetail.setSkuName(cartInfo.getSkuName());
            orderDetail.setImgUrl(cartInfo.getImgUrl());
            orderDetail.setOrderPrice(cartInfo.getSkuPrice());
            orderDetail.setSkuNum(cartInfo.getSkuNum());

            orderDetailList.add(orderDetail);
        }

        //计算总金额
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderDetailList(orderDetailList);
        orderInfo.sumTotalAmount();

        Map<String, Object> map = new HashMap<>();
        map.put("userAddressList",userAddressList);
        map.put("detailArrayList",orderDetailList);
        map.put("totalNum",orderDetailList.size());
        map.put("totalAmount",orderInfo.getTotalAmount());

        return map;
    }
}
