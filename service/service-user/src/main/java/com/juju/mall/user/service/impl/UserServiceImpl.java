package com.juju.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.juju.mall.constant.RedisConst;
import com.juju.mall.user.UserInfo;
import com.juju.mall.user.mapper.UserInfoMapper;
import com.juju.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserInfoMapper userInfoMapper;


    @Override
    public Map<String, Object> login(UserInfo userInfo) {
        Map<String, Object> map = null;

        // 验证
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("login_name",userInfo.getLoginName());
        queryWrapper.eq("passwd", DigestUtils.md5DigestAsHex(userInfo.getPasswd().getBytes()));
        UserInfo userInfoResult = userInfoMapper.selectOne(queryWrapper);
        if (null!=userInfoResult){
            //token
            String token = UUID.randomUUID().toString();
            //存缓存
            redisTemplate.opsForValue().set("user:"+token,userInfoResult.getId());

            map = new HashMap<>();
            map.put("name",userInfoResult.getName());
            map.put("nickName",userInfoResult.getNickName());
            map.put("token",token);
        }
        return map;
    }

    @Override
    public UserInfo verify(String token) {
        String userKeyPrefix = RedisConst.USER_KEY_PREFIX;

        UserInfo userInfo = null;

        //查缓存
        Integer  userId = (Integer) redisTemplate.opsForValue().get(userKeyPrefix + token);

        if (null!=userId&&userId>0){
            userInfo = userInfoMapper.selectById(userId);
        }
        return userInfo;
    }

    @Override
    public UserInfo findUserByUserId(String userId) {
        return userInfoMapper.selectById(userId);
    }
}
