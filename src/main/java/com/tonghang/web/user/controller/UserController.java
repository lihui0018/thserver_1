package com.tonghang.web.user.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.danga.MemCached.MemCachedClient;
import com.tonghang.web.user.pojo.UserPo;
import com.tonghang.web.user.repository.UserDao;

@Controller
@RequestMapping("user")
public class UserController {

	@Resource
	private UserDao userDao;
	
	@Resource(name="memCachedClient")
	private MemCachedClient memCachedClient;
	
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
	
	@RequestMapping("cache")
	public ResponseEntity<Map<String,Object>> testCache(){
		memCachedClient.set("aaa", "bbb1");
		return new ResponseEntity<Map<String,Object>>(userDao.insertUser1(),HttpStatus.OK);
	}
	
	@RequestMapping("list")
	public @ResponseBody ResponseEntity<Map<String,Object>> queryAmount(){
		Map<String, Object> r = userDao.queryAmount();
		List<UserPo> l = (List<UserPo>) r.get("result");
		System.out.println(l.size());
		Map<String, Object> nr = new HashMap<>();
		nr.put("list", l.toString());
		return new ResponseEntity<Map<String,Object>>(nr,HttpStatus.OK);
	}
	
	@RequestMapping("clear")
	public ResponseEntity<Map<String,Object>> clearQueryAmount(){
		return new ResponseEntity<Map<String,Object>>(userDao.clearQueryAmount(),HttpStatus.OK);
	}
	
	@RequestMapping("index")
	public String index(){
		return "index";
	}
	
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public String uploadFile(@RequestParam("name") String name, @RequestParam("file") MultipartFile file){
		if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }
}
