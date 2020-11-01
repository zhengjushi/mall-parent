package com.juju.mall.service;

import com.juju.mall.entity.BaseAttrInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.juju.mall.response.AttrInfoVO;

import java.util.List;

/**
 * <p>
 * 属性表 服务类
 * </p>
 *
 * @author juju
 * @since 2020-10-30
 */
public interface BaseAttrInfoService extends IService<BaseAttrInfo> {

    List<BaseAttrInfo> attrInfoList(String category1Id, String category2Id, String category3Id);

    void saveAttrInfo(BaseAttrInfo baseAttrInfo);
}
