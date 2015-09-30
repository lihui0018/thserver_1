package com.tonghang.web;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tonghang.web.user.pojo.UserPo;
import com.tonghang.web.user.service.UserService_r;

public class Main {  
    public static void main( String[] args )  
    {  
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:/applicationContext.xml");
        UserService_r userService_r =  (UserService_r) applicationContext.getBean("userService_r");
              
        UserPo user1 = new UserPo();
        user1.setId("123");
        UserPo user2 = new UserPo();  
        user2.setId("321");
        
        System.out.println("==== getting objects from redis ====");
        System.out.println("User is not in redis yet: " + userService_r.get(user1));
        System.out.println("User is not in redis yet: " + userService_r.get(user2));
          
        System.out.println("==== putting objects into redis ====");  
        userService_r.put(user1);  
        userService_r.put(user2);  
          
        System.out.println("==== getting objects from redis ====");  
        System.out.println("User should be in redis yet: " + userService_r.get(user1));  
        System.out.println("User should be in redis yet: " + userService_r.get(user2));  
          
        System.out.println("==== deleting objects from redis ====");  
        userService_r.delete(user1);  
        userService_r.delete(user2);  
          
        System.out.println("==== getting objects from redis ====");  
        System.out.println("User is not in redis yet: " + userService_r.get(user1));  
        System.out.println("User is not in redis yet: " + userService_r.get(user2));  
  
    }  
} 