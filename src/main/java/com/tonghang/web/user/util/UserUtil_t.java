package com.tonghang.web.user.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mysql.jdbc.StringUtils;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.pojo.UserPo;

public class UserUtil_t {

	public static Map<String,Object> userToMapConvertor(UserPo user){
		Map<String,Object> userMap = new HashMap<String, Object>();
		Map<String,Object> msg = new HashMap<String, Object>();
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getTags())){
			String[] labels = user.getTags().split(",");
			msg.put("labels", labels);			
		}else{
			msg.put("labels", null);
		}
		String city = "";
		if(StringUtils.isEmptyOrWhitespaceOnly(user.getProvince()))
			city = "未知";
		else city = user.getCity()==null||"".equals(user.getCity())?user.getProvince():user.getProvince()+"-"+user.getCity();
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", city);
		msg.put("client_id", user.getId());
		msg.put("image", Constant.IMAGE_PATH+user.getId()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("birth", user.getBirth());
		msg.put("is_friend", false);
		msg.put("has_invitation", false);
		userMap.put("user", msg);
		return userMap;
	}
	
	public Map<String,Object> userToMapConvertor(User user, String client_id, boolean isFriend, boolean isInvited){
		List<String> labels = new ArrayList<String>();
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(user.getLabellist()!=null){
			for(Label l:user.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);			
		}else{
			msg.put("labels", null);	
		}
		String city = "";
		if(user.getProvince()==null||"".equals(user.getProvince()))
			city = "未知";
		else city = user.getCity()==null||"".equals(user.getCity())?user.getProvince():user.getProvince()+"-"+user.getCity();
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", city);
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("birth", user.getBirth());
		msg.put("is_friend", isFriend);
		if(!isFriend)
			msg.put("has_invitation", isInvited);
		else 
			msg.put("has_invitation",!isFriend);
		usermap.put("user", msg);
		return usermap;
	}
	
	/**
	 * 重载userToMapConvertor方法，ignore表示忽略好友关系（因为有可能已知对方肯定是或者不是好友关系）
	 * 新加了查看用户是不是给某用户发送了好友申请
	 * @param user
	 * @param ignore
	 * @return
	 */
	public Map<String,Object> userToMapConvertor(User user,boolean ignore,String client_id, boolean isInvited){
		List<String> labels = new ArrayList<String>();
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		if(user.getLabellist()!=null){
			for(Label l:user.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);			
		}else{
			msg.put("labels", null);	
		}
		String city = "";
		if(user.getProvince()==null||"".equals(user.getProvince()))
			city = "未知";
		else city = user.getCity()==null||"".equals(user.getCity())?user.getProvince():user.getProvince()+"-"+user.getCity();
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("city", city);
		msg.put("birth", user.getBirth());
		msg.put("is_friend", ignore);
		if(!ignore)
			msg.put("has_invitation",isInvited);
		else msg.put("has_invitation",!ignore);
		usermap.put("user", msg);
		return usermap;
	}
	
}
