package common.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
public class RedisConfig {

    /**
     * 普通对象的redisTeamplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplateObject(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //设置value的序列化方式json
        redisTemplate.setValueSerializer(redisSerializer());
        //设置key序列化方式String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置hash key序列化方式String
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //设置hash value序列化json
        redisTemplate.setHashValueSerializer(redisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    public RedisSerializer<Object> redisSerializer() {
        //创建JSON序列化器
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        //必须设置，否则无法序列化实体类对象
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        return new GenericJackson2JsonRedisSerializer(objectMapper);
    }

    // 用于维护排名情况。
    @Bean
    public RedisTemplate<String, String> redisTemplateString(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericToStringSerializer<>(String.class));
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericToStringSerializer<>(String.class));
        return redisTemplate;
    }

    //  Z set 维护榜单
    @Bean
    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplateString) {
        return redisTemplateString.opsForZSet();
    }

    // Hash 维护选手的具体信息
    @Bean
    HashOperations<String , String, Object> hashOperations(RedisTemplate<String, Object> redisTemplateObject){
        return redisTemplateObject.opsForHash();
    }
}
