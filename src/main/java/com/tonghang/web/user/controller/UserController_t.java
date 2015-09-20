package com.tonghang.web.user.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.danga.MemCached.MemCachedClient;
import com.mchange.io.FileUtils;
import com.tonghang.web.user.pojo.UserPo;
import com.tonghang.web.user.repository.UserDao_t;

@Controller
@RequestMapping("user")
public class UserController_t {

	@Resource
	private UserDao_t userDao_t;
	
	@Resource(name="memCachedClient")
	private MemCachedClient memCachedClient;
	
	@RequestMapping("user")
	public ResponseEntity<Map<String,Object>> insertUser(){
		return new ResponseEntity<Map<String,Object>>(userDao_t.insertUser(),HttpStatus.OK);
	}
	
	@RequestMapping("user1")
	public ResponseEntity<Map<String,Object>> insertUser1(){
		return new ResponseEntity<Map<String,Object>>(userDao_t.insertUser1(),HttpStatus.OK);
	}
	
	@RequestMapping("user12")
	public ResponseEntity<Map<String,Object>> insertUser12(){
		userDao_t.insertUser();
		return new ResponseEntity<Map<String,Object>>(userDao_t.insertUser1(),HttpStatus.OK);
	}
	
	@RequestMapping("trans")
	public ResponseEntity<Map<String,Object>> transformData(){
		userDao_t.insertUser();
		return new ResponseEntity<Map<String,Object>>(userDao_t.insertAmount(),HttpStatus.OK);
	}
	
	@RequestMapping("cache")
	public ResponseEntity<Map<String,Object>> testCache(){
		memCachedClient.set("aaa", "bbb1");
		return new ResponseEntity<Map<String,Object>>(userDao_t.insertUser1(),HttpStatus.OK);
	}
	
	@RequestMapping("list")
	public @ResponseBody ResponseEntity<Map<String,Object>> queryAmount(){
		Map<String, Object> r = userDao_t.queryAmount();
		List<UserPo> l = (List<UserPo>) r.get("result");
		System.out.println(l.size());
		Map<String, Object> nr = new HashMap<>();
		nr.put("list", l.toString());
		return new ResponseEntity<Map<String,Object>>(nr,HttpStatus.OK);
	}
	
	@RequestMapping("clear")
	public ResponseEntity<Map<String,Object>> clearQueryAmount(){
		return new ResponseEntity<Map<String,Object>>(userDao_t.clearQueryAmount(),HttpStatus.OK);
	}
	
	@RequestMapping("index")
	public String index(){
		return "index";
	}
	
	@RequestMapping(value="upload")
	public ResponseEntity<Map<String,Object>> uploadFile(@RequestParam("name") String name, @RequestParam("file") MultipartFile file){
		String retur;
		if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File("d:"+File.separator+name + "-uploaded")));
                stream.write(bytes);
                stream.close();
                retur = "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                retur = "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            retur =  "You failed to upload " + name + " because the file was empty.";
        }
		Map<String, Object> result = new HashMap<>();
		result.put("result", retur);
		return new ResponseEntity<Map<String,Object>>(userDao_t.clearQueryAmount(),HttpStatus.OK);
    }
	
	@RequestMapping("download")
    public ResponseEntity<byte[]> download() throws IOException {
        String path="D:"+File.separator+"doc-uploaded";  
        File file=new File(path);  
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String("你好.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.getBytes(file), headers, HttpStatus.CREATED);
    }
    
}
