package com.tonghang.web.user.repository;

import java.util.List;

import com.tonghang.web.user.pojo.UserPo;

public interface IUserDao {
	
	public void saveOrUpdate(UserPo user);
	public UserPo findUserById(String client_id);
	
	public UserPo findUserByEmail(String email);
	public UserPo findUserByNickName(String nickname);
	public List<UserPo> findUserByUsername(String username,int page);

	public List<UserPo> findUserByLabel(String label_name,int nowpage);
	
	public void addFriend(UserPo my,UserPo friend);
	public void deleteFriend(UserPo my,UserPo friend);
	public void addBlocker(UserPo me,UserPo blocker);
	public void deleteBlock(UserPo me,UserPo blcoker);
	
}
