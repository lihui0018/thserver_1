package com.tonghang.web.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.exception.BaseException;
import com.tonghang.web.common.exception.EmailExistException;
import com.tonghang.web.common.exception.LoginException;
import com.tonghang.web.common.exception.NickNameExistException;
import com.tonghang.web.common.exception.SearchNoResultException;
import com.tonghang.web.common.util.CommonConvertor;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.EmailUtil_t;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.friend.service.FriendService;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.location.service.LocationService_t;
import com.tonghang.web.user.pojo.UserPo;
import com.tonghang.web.user.repository.IUserDao;
import com.tonghang.web.user.util.UserUtil_t;

@Transactional
@Service
public class UserService_t {

	@Resource(name="userDao_t")
	private IUserDao userDao_t;
	
	@Resource
	private LocationService_t locationService_t;
	
	@Resource
	private FriendService friendService;
	
	public Map<String, Object> login(String email, String password) {
		Map<String,Object> result = new HashMap<String, Object>();
		System.out.println("登录时的密码MD5加密后："+SecurityUtil.getMD5(password));
		UserPo user = userDao_t.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，该邮箱不存在！", 510));
			return result;
		}else{
			if(user.getStatus().equals("0")){
				result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户被封号！", 510));
				return result;
			}else{
				//新需求需要密码MD5加密，此处用来兼容老用户
				if(user.getPassword().equals(SecurityUtil.getMD5(password))){
					Map<String,Object> usermap = UserUtil_t.userToMapConvertor(user);
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					userDao_t.saveOrUpdate(user);
				}else if(user.getPassword().equals(password)){
					Map<String,Object> usermap = UserUtil_t.userToMapConvertor(user);
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					user.setPassword(SecurityUtil.getMD5(password));
//					HuanXinUtil.changePassword(SecurityUtil.getMD5(password), user.getId());
					userDao_t.saveOrUpdate(user);
				}else{
					result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户名或密码错误！", 510));
					return result;
				}
			}
		}
		return result;
	}

	/**
	 * 业务功能:旧版本的APP登录通道
	 * @param email
	 * @param password
	 * @return
	 * @throws BaseException
	 */
	public Map<String,Object> oldLogin(String email,String password) throws BaseException{
		Map<String,Object> result = new HashMap<String, Object>();
		UserPo user = userDao_t.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，该邮箱不存在！", 510));
			return result;
		}else{
			if(user.getStatus().equals(0)){
				result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户被封号！", 510));
				return result;
			}else{
				if(user.getPassword().equals(password)){
					Map<String,Object> usermap = UserUtil_t.userToMapConvertor(user);
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
					user.setIsonline("1");
					userDao_t.saveOrUpdate(user);
				}else{
					result.put("success", CommonMapUtil.baseMsgToMapConvertor("登录失败，用户名或密码错误！", 510));
					return result;
				}
			}
		}
		return result;
	}
	
	/**
	 * 找回密码
	 * @param 
	 * @return
	 * @throws EmailExistException 
	 * @throws LoginException 
	 * 
	 * notice: 2015-08-28 忘记密码的随机密码进行了MD5加密
	 */
	public Map<String,Object> forgetPassword(String email) throws LoginException{
		Map<String,Object> result = new HashMap<String, Object>();
		UserPo user = userDao_t.findUserByEmail(email);
		if(user==null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("发送失败，该邮箱不存在！", 510));
			return result;
		}else{
			user.setPassword(StringUtil.randomCode(8));
			EmailUtil_t.sendEmail(user);
			user.setPassword(SecurityUtil.getMD5(user.getPassword()));
			HuanXinUtil.changePassword(SecurityUtil.getMD5(user.getPassword()), user.getId());
			userDao_t.saveOrUpdate(user);
			result.put("success", CommonConvertor.messageToMapConvertor(200, "密码重置请求成功!"));
		}
		return result;
	}
	
	/**
	 * 业务功能：用户注册第一步
	 * @param user User对象(新注册的user对象)
	 * @return
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 * @throws EmailExistException 
	 * 
	 * notice：第一步注册去掉了添加标签
	 * @throws NickNameExistException 
	 */
	public Map<String,Object> registUser(UserPo user) throws EmailExistException, NickNameExistException{
		Map<String,Object> result = new HashMap<String, Object>();
		if(userDao_t.findUserByEmail(user.getEmail())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该邮箱已被注册", 511));
			return result;
		}else if(userDao_t.findUserByNickName(user.getUsername())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该昵称已经被注册", 512));
			return result;
		}else{
			userDao_t.saveOrUpdate(user);
//			HuanXinUtil.registUser(user);
			Map<String,Object> usermap = UserUtil_t.userToMapConvertor(user);
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}
		return result;
	}
	
	/**
	 *  旧的注册接口，因为注册业务换成三步注册，为了兼容0.8app留下该接口
	 * @param user
	 * @return
	 * @throws EmailExistException
	 * @throws NickNameExistException
	 */
	public Map<String,Object> oldRegistUser(UserPo user) throws EmailExistException, NickNameExistException{
		Map<String,Object> result = new HashMap<String, Object>();
		if(userDao_t.findUserByEmail(user.getEmail())!=null){
			result.put("success", CommonMapUtil.baseMsgToMapConvertor("注册失败！该邮箱已被注册", 511));
			return result;
		}else{
			userDao_t.saveOrUpdate(user);
//			HuanXinUtil.registUser(user);
			Map<String,Object> usermap = UserUtil_t.userToMapConvertor(user);
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}
		return result;
	}
	
	/**
	 * 首页推荐
	 * @param 
	 * @return
	 * 使用Set<User> userss先进行包装是为了去掉多余的用户，比如java能推出A用户，android也能推出A用户。
	 * 此时需要用Set去除重复项。
	 * @throws SearchNoResultException 
	 * 
	 * 2015-8-11日新加入排序功能，详情请见SortUtil
	 * 2015-8-27日新加入 在标签排序基础上，按照距离排序功能
	 */
	public Map<String, Object> recommend(String client_id,boolean byDistance, int page){
//		List<Map<String,Object>> sortlist = new ArrayList<Map<String,Object>>();
//		Map<String,Object> result = new HashMap<String, Object>();
//		List<String> label_names = new ArrayList<String>();
		UserPo user = userDao_t.findUserById(client_id);
		Location location = locationService_t.findLocationByUser(user.getId());
		List<UserPo> users = userDao_t.findUsersByLabel(user.getTags(), byDistance, location);
//		for(Label label : labels){
//			List<User> us = userDao.findUserByLabel(label.getLabel_name(), page);
//			if(us.contains(user)){
//				us.remove(user);
//			}
//			userss.addAll(us);
//			//存放目标用户的标签，用来排序
//			label_names.add(label.getLabel_name());
//		}
//		users.addAll(userss);
//		if(users.size()==0||userss.size()==0&&page==1){
////			throw new SearchNoResultException("首页推荐没有结果");
//			result.put("success", CommonMapUtil.baseMsgToMapConvertor("首页推荐没有结果", 520));
//			return result;
//		}else if(userss.size()==0&&page>1){
//			result.put("success", CommonMapUtil.baseMsgToMapConvertor("搜索不到更多了", 520));
//			return result;
//		}
//			throw new SearchNoResultException("搜索不到更多了");
		return UserUtil_t.usersToMapConvertor(users, user);
	}
//	private boolean isFriend(String my,String friend){
//		return friendDao.isFriend(my, friend);
//	}

	public boolean isFriend(String id, String id2) {
		return false;
	}
	
}
