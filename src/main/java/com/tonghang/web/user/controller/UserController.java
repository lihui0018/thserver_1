package com.tonghang.web.user.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tonghang.web.user.repository.UserDao;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	private UserDao userDao;
	
	@RequestMapping("user")
	public ResponseEntity<Map<String,Object>> insertUser(){
		return new ResponseEntity<Map<String,Object>>(userDao.insertUser(),HttpStatus.OK);
	}
	
	@RequestMapping("user1")
	public ResponseEntity<Map<String,Object>> insertUser1(){
		return new ResponseEntity<Map<String,Object>>(userDao.insertUser1(),HttpStatus.OK);
	}
	
	@RequestMapping("user12")
	public ResponseEntity<Map<String,Object>> insertUser12(){
		userDao.insertUser();
		return new ResponseEntity<Map<String,Object>>(userDao.insertUser1(),HttpStatus.OK);
	}
	
	@RequestMapping("trans")
	public ResponseEntity<Map<String,Object>> transformData(){
		userDao.insertUser();
		return new ResponseEntity<Map<String,Object>>(userDao.insertAmount(),HttpStatus.OK);
	}
}
