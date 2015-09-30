package com.tonghang.web.user.service;

import org.springframework.data.redis.core.RedisTemplate;

import com.tonghang.web.user.pojo.UserPo;

public class UserService_r {  
  
    RedisTemplate<String, UserPo> redisTemplate;  
  
    public RedisTemplate<String, UserPo> getRedisTemplate() {  
        return redisTemplate;  
    }  
  
    public void setRedisTemplate(RedisTemplate<String, UserPo> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }  
      
    public void put(UserPo user) {  
        redisTemplate.opsForHash().put(user.getClass().getName(), user.getId(), user);  
    }  
  
    public void delete(UserPo key) {  
        redisTemplate.opsForHash().delete(key.getClass().getName(), key.getId());  
    }  
  
    public UserPo get(UserPo key) {  
        return (UserPo) redisTemplate.opsForHash().get(key.getClass().getName(), key.getId());  
    }  
} 