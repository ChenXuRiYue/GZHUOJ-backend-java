package common.redis;

import cn.hutool.json.JSONUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static common.constant.RedisKey.REGULAR_CONTEST;

@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisUtil(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    // 生成相关的key
    // 1. 生成排行榜存储的key
    public String genKeyWhenProblemSet(Integer contestId, String role){
        return REGULAR_CONTEST + contestId.toString() + ":" + role;
    }

    // 存储List到Redis哈希表
    public <T> void saveListToHash(String key, String hashKey, List<T> list, long timeout, TimeUnit timeUnit) {
        String jsonStr = JSONUtil.toJsonStr(list);
        stringRedisTemplate.opsForHash().put(key, hashKey, jsonStr);
        stringRedisTemplate.expire(key, timeout, timeUnit);
    }

    // 从Redis哈希表中读取List
    public <T> List<T> getListFromHash(Object jsonStr, String hashKey, Class<T> clazz) {
        return JSONUtil.parseArray(jsonStr).toList(clazz);
    }

    /**
     * 将value放入list的左侧
     */
    public boolean lPush(String key, String value) {
        try {
            stringRedisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将value从list的右侧取出
     */
    public Object rPop(String key) {
        try {
            return stringRedisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取list集合大小
     */
    public long getListSize(String key) {
        try {
            return stringRedisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
