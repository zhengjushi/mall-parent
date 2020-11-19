package com.juju.mall.cart.service;

import com.juju.mall.cart.CartInfo;

import java.util.List;

public interface CartService {
    void addCart(CartInfo cartInfo);

    List<CartInfo> cartList(String userId);

    List<CartInfo> getCartCheckedList(String userId);
}
