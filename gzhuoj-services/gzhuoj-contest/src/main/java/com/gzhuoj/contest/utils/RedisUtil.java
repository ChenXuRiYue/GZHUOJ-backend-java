package com.gzhuoj.contest.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 存储List到Redis哈希表
    public <T> void saveListToHash(String key, String hashKey, List<T> list, long timeout, TimeUnit timeUnit) {
        String jsonStr = JSONUtil.toJsonStr(list);
        stringRedisTemplate.opsForHash().put(key, hashKey, jsonStr);
        stringRedisTemplate.expire(hashKey, timeout, timeUnit);
    }

    // 从Redis哈希表中读取List
    public <T> List<T> getListFromHash(Object jsonStr, String hashKey, Class<T> clazz) {
        return JSONUtil.parseArray(jsonStr).toList(clazz);
    }
}
