package com.juju.mall.service;

import com.juju.mall.list.SearchParam;
import com.juju.mall.list.SearchResponseVo;

public interface ListService {
    void onSale(String skuId);

    void cancelSale(String skuId);

    void hotScore(String skuId);

    SearchResponseVo list(SearchParam searchParam);
}
