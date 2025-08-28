package org.example.pat.infrastructure;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setValueWithExpiry(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    public void setHashValue(String hash, String key, String value) {
        redisTemplate.opsForHash().put(hash, key, value);
    }

    public String getHashValue(String hash, String key) {
        return (String) redisTemplate.opsForHash().get(hash, key);
    }

    public boolean hashHasKey(String hash, String key) {
        return redisTemplate.opsForHash().hasKey(hash, key);
    }
}