package com.tonghang.web.user.service;

import javax.annotation.Resource;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.tonghang.web.user.pojo.UserPo;

//@Transactional
@Service
public class UserService_r {  

	@Resource
    private RedisTemplate<String, UserPo> redisTemplate;  
  
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