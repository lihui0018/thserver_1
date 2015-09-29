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

import com.mysql.jdbc.StringUtils;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.user.pojo.UserPo;

@Transactional
@Repository
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
		return (UserPo) sessionFactory.getCurrentSession().get(UserPo.class, client_id);
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

	@Override
	public List<UserPo> findUsersByLabel(String tags, boolean distance, Location location) {
		String sql = "";
		if(StringUtils.isEmptyOrWhitespaceOnly(tags)){
			if(distance){
				sql = "select u,round(6378.138*2*asin(sqrt(pow(sin( (l.x_point *pi()/180-"+location.getX_point()+" *pi()/180)/2),2)+cos(l.x_point*pi()/180)*cos("+location.getX_point()+"*pi()/180)*pow(sin( (l.y_point*pi()/180-"+location.getY_point()+"*pi()/180)/2),2)))*1000) as distance from UserPo u left join location l on u.id=l.client_id order by distance desc, created_at desc"; 
			}else{
				sql = "select u from UserPo u order by created_at desc";
			}
		}else{
			String[] tagArray = tags.split(",");
			String tagSql = "";
			for(String tag : tagArray){
				tagSql = tagSql + "(length(tags)-length(REPLACE(tags, '"+tag+"', '')))/length('"+tag+"')+";
			}
			tagSql = tagSql.substring(0,tagSql.length() - 1);
			if(distance){
				sql = "select u,round(6378.138*2*asin(sqrt(pow(sin( (l.x_point *pi()/180-"+location.getX_point()+" *pi()/180)/2),2)+cos(l.x_point*pi()/180)*cos("+location.getX_point()+"*pi()/180)*pow(sin( (l.y_point*pi()/180-"+location.getY_point()+"*pi()/180)/2),2)))*1000) as distance from (select UserPo," + tagSql + " as weight from UserPo) u left join location l on u.id=l.client_id order by weight desc,distance desc";
			}else{
				sql = "select u," + tagSql + " as weight from UserPo u order by weight desc";
			}
		}
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		List<UserPo> users = query.setFirstResult(0).setMaxResults(99).list();
		return users;
	}

	@Override
	public List<UserPo> findUsersByUsername(String username, boolean distance,
			Location location) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
