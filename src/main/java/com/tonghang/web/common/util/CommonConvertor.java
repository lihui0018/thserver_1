package com.tonghang.web.common.util;

import java.util.HashMap;
import java.util.Map;

public class CommonConvertor {

	public static Map<String,Object> messageToMapConvertor(int code,String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("message",message);
		return map;
	}
	
}
