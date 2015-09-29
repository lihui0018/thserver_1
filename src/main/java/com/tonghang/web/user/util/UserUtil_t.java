package com.tonghang.web.user.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.mysql.jdbc.StringUtils;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.SortUtil;
import com.tonghang.web.friend.service.FriendService;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.pojo.UserPo;
import com.tonghang.web.user.service.UserService_t;

@Component
public class UserUtil_t {
	
	@Resource
	private UserService_t userService_t;
	
	@Resource
	private FriendService friendService;

	public static Map<String,Object> userToMapConvertor(UserPo user){
		Map<String,Object> userMap = new HashMap<String, Object>();
		Map<String,Object> msg = new HashMap<String, Object>();
		if(!StringUtils.isEmptyOrWhitespaceOnly(user.getTags())){
			String[] labels = user.getTags().split(",");
			msg.put("labels", labels);			
		}else{
			msg.put("labels", null);
		}
		String city = "";
		if(!StringUtils.isEmptyOrWhitespaceOnly(user.getProvince()))
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
	
	/**
	 * 该方法是usersToMapSortedConvertor(List<User> users,String client_id)方法的改良版，
	 * 核心作用相同，除了有按行业排序，该方法多了一层按距离排序。现根据行业排序，然后根据距离排序
	 * @param users
	 * @return
	 * notice:在推荐的同行中如果有人没有location记录，则为其设置一个默认坐标
	 */
	public Map<String,Object> usersToMapConvertor(List<UserPo> users, UserPo me){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
//		List<String> label_names = new ArrayList<String>();
//		List<Boolean> is_same = new ArrayList<Boolean>();
//		Location my_local = locationService.findLocationByUser(me);
//		for(Label l:me.getLabellist()){
//			label_names.add(l.getLabel_name());
//		}
		for(Object uo:users){
			UserPo u = (UserPo)uo;
			List<String> labels = new ArrayList<String>();
//			Location his_local = locationService.findLocationByUser(u);
//			double distance = locationService.getDistanceByLocation(my_local, his_local);
			Map<String,Object> msg = new HashMap<String, Object>();
			//比较当前用户哪些标签是根据使用者的标签被推出来的
			labels.addAll(markLabel(u, me.getTags()));
			boolean is_friend = userService_t.isFriend(me.getId(), u.getId());
			//我的标签，送给前台和推荐的用户比对，相同的就标记高亮
			String city = "";
			if(StringUtils.isEmptyOrWhitespaceOnly(u.getProvince()))
				city = "未知";
			else city = StringUtils.isEmptyOrWhitespaceOnly(u.getCity())?u.getProvince():u.getProvince()+"-"+u.getCity();
			msg.put("distance", u.getDistance()==0?null:u.getDistance());
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("client_id", u.getId());
			msg.put("image", Constant.IMAGE_PATH+u.getId()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			msg.put("city", city);
			msg.put("type", true);
			boolean has_invited = friendService.isInvited(me.getId(), u.getId());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			usersmsg.add(msg);
		}
		//排序操作，详细请看 SortUtil 类
//		usersmsg = SortUtil.sortByLabelName(usersmsg, me.getTags());
//		usersmsg = SortUtil.sortByDistance(usersmsg);
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
	/**
	 * 将相同的label重点标识
	 * @param u 用户
	 * @param label_names 需要标识的label名
	 * @return 标识后的label名称集合
	 */
	private static List<String> markLabel(UserPo u, String label_names) {
		List<String> ls = new ArrayList<>();
		if(StringUtils.isEmptyOrWhitespaceOnly(u.getTags())){
			return ls;
		}
		if(StringUtils.isEmptyOrWhitespaceOnly(label_names)){
			return Arrays.asList(u.getTags().split(","));
		}
		String[] uLabels = u.getTags().split(",");
		String[] mLabels = label_names.split(",");
		for(String l : uLabels){
			for(String ml : mLabels){
				if(l.indexOf(ml)>=0){
					ls.add(l+"\t\t");
				}else{
					ls.add(l);
				}
			}
		}
		return ls;
	}

	/**
	 * 该方法是usersToMapConvertor(List<User> users,String client_id)方法的改良版，
	 * 核心作用相同，该方法多了一层按标签排序
	 * @param users
	 * @return
	 */
	public Map<String,Object> usersToMapSortedConvertor(List<UserPo> users,UserPo me){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		List<String> label_names = new ArrayList<String>();
//		for(Label l:me.getLabellist()){
//			label_names.add(l.getLabel_name());
//		}
		for(UserPo u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			//比较当前用户哪些标签是根据使用者的标签被推出来的
			labels.addAll(markLabel(u, me.getTags()));
			boolean is_friend = userService_t.isFriend(me.getId(), u.getId());
			//我的标签，送给前台和推荐的用户比对，相同的就标记高亮
			String city = "";
			if(u.getProvince()==null||"".equals(u.getProvince()))
				city = "未知";
			else city = u.getCity()==null||"".equals(u.getCity())?u.getProvince():u.getProvince()+"-"+u.getCity();
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("client_id", u.getId());
			msg.put("image", Constant.IMAGE_PATH+u.getId()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", is_friend);
			msg.put("city", city);
			msg.put("type", true);
			boolean has_invited = friendService.isInvited(me.getId(), u.getId());
			if(!is_friend)
				msg.put("has_invitation", has_invited);
			else msg.put("has_invitation",!is_friend);
			System.out.println("usersToMapConvertor: "+has_invited);
			usersmsg.add(msg);
		}
		//排序操作，详细请看 SortUtil 类
		usersmsg = SortUtil.sortByLabelName(usersmsg, label_names);
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
}
