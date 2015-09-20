package com.tonghang.web.user.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.pojo.UserPo;

@Repository
@Transactional
public class UserDao_t implements IUserDao{
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Resource
	private SessionFactory sessionFactory_1;

	@Cacheable(value="message",key="'user'")
	public Map<String, Object> insertUser() {
		UserPo u = new UserPo();
		u.setUsername("aaa");
		u.setEmail("a@a.a");
		u.setPassword("123");
			u.setTags("java,aaa,java测试");
		sessionFactory.getCurrentSession().saveOrUpdate(u);
		Map<String, Object> m = new HashMap<>();
		m.put("result", true);
		return m;
	}
	
	@Cacheable(value="message",key="'user1'")
	public Map<String, Object> insertUser1() {
		UserPo u = new UserPo();
		u.setUsername("bbb");
		u.setEmail("b@b.b");
		u.setPassword("456");
		u.setTags("j,bbb,java实施");
		sessionFactory.getCurrentSession().saveOrUpdate(u);
		Map<String, Object> m = new HashMap<>();
		m.put("result", true);
		return m;
	}
	
	@Transactional()
	public Map<String, Object> insertAmount() {
		for(int i=0;i<10000;i++){
			UserPo u = new UserPo();
			u.setUsername("bbb"+i);
			u.setEmail("b@b.b"+i);
			u.setPassword("456"+i);
			u.setTags("j,bbb,java实施,"+i);
			sessionFactory.getCurrentSession().saveOrUpdate(u);
		}
		Map<String, Object> m = new HashMap<>();
		m.put("result", true);
		return m;
	}
	
	@SuppressWarnings("unchecked")
	@Cacheable(value="message",key="'amount'")
	public Map<String, Object> queryAmount() {
		Query q = sessionFactory.getCurrentSession().createQuery("from UserPo");
		q.setFirstResult(0);
		q.setMaxResults(99);
		List<UserPo> s = q.list();
		Map<String, Object> m = new HashMap<>();
		m.put("result", s);
		return m;
	}
	
	@CacheEvict(value="message",key="'amount'")
	public Map<String, Object> clearQueryAmount() {
		Map<String, Object> m = new HashMap<>();
		m.put("result", true);
		return m;
	}

	@Override
	public void addFriend(UserPo my, UserPo friend) {
		my.getFriends().add(friend);
		sessionFactory.getCurrentSession().update(my);
	}

	@Override
	public void addBlocker(UserPo me, UserPo blocker) {
		me.getBlacklist().add(blocker);
		sessionFactory.getCurrentSession().update(me);
	}

	@Override
	public UserPo findUserByEmail(String email) {
		UserPo user = (UserPo) sessionFactory.getCurrentSession().createQuery("from UserPo as user where user.email = :email")
				.setParameter("email", email).uniqueResult();
		return user;
	}

	@Override
	public UserPo findUserByNickName(String nickname) {
		UserPo user = (UserPo) sessionFactory.getCurrentSession().createQuery("from UserPo as user where user.username = :nickname")
				.setParameter("nickname", nickname).uniqueResult();
		return user;
	}

	@Override
	public UserPo findUserById(String client_id) {
		return (UserPo) sessionFactory.getCurrentSession().get(User.class, client_id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserPo> findUserByUsername(String username, int page) {
		List<UserPo> users = sessionFactory.getCurrentSession().createQuery("select distinct user from User as user where lower(username) like concat('%',lower(:username),'%') and (user.birth is not null and user.birth != '') and(user.sex is not null and user.sex != '') order by user.created_at").
				setParameter("username", username).setFirstResult(Constant.PAGESIZE*(page-1)).setMaxResults(Constant.PAGESIZE).list();
		return users;
	}

	@Override
	public void saveOrUpdate(UserPo user) {
		sessionFactory.getCurrentSession().saveOrUpdate(user);
	}

	@Override
	public void deleteFriend(UserPo my, UserPo friend) {
		my.getFriends().remove(friend);
		sessionFactory.getCurrentSession().update(my);
	}

	@Override
	public void deleteBlock(UserPo me, UserPo blcoker) {
		me.getBlacklist().remove(blcoker);
		sessionFactory.getCurrentSession().update(me);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserPo> findUserByLabel(String label_name, int nowpage) {
		Query query = sessionFactory.getCurrentSession().createQuery("select distinct user from User as user left join user.labellist as " +
				"label where lower(label.label_name) like concat('%',lower(:label_name),'%') " +
				"and (user.birth is not null and user.birth != '') and (user.sex is not null and user.sex != '') order by user.created_at");
		List<UserPo> user = (List<UserPo>) query.setParameter("label_name", label_name)
				.setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
		return user;
	}
	
}
