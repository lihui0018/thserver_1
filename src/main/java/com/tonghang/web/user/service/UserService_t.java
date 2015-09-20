package com.tonghang.web.user.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.exception.BaseException;
import com.tonghang.web.common.exception.EmailExistException;
import com.tonghang.web.common.exception.LoginException;
import com.tonghang.web.common.util.CommonConvertor;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.EmailUtil_t;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.friend.service.FriendService;
import com.tonghang.web.user.pojo.UserPo;
import com.tonghang.web.user.repository.IUserDao;
import com.tonghang.web.user.util.UserUtil_t;

@Transactional
@Service
public class UserService_t {

	@Resource(name="userDao_t")
	private IUserDao userDao_t;
	
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
//调修改密码方法
			EmailUtil_t.sendEmail(user);
			user.setPassword(SecurityUtil.getMD5(user.getPassword()));
			HuanXinUtil.changePassword(SecurityUtil.getMD5(user.getPassword()), user.getId());
			userDao_t.saveOrUpdate(user);
			result.put("success", CommonConvertor.messageToMapConvertor(200, "密码重置请求成功!"));
		}
		return result;
	}
//	private boolean isFriend(String my,String friend){
//		return friendDao.isFriend(my, friend);
//	}
	
}
