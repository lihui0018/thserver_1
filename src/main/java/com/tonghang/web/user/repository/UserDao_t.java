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

import com.tonghang.web.user.pojo.UserPo_t;

@Repository
@Transactional
public class UserDao_t {
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Resource
	private SessionFactory sessionFactory_1;

	@Cacheable(value="message",key="'user'")
	public Map<String, Object> insertUser() {
		UserPo_t u = new UserPo_t();
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
		UserPo_t u = new UserPo_t();
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
			UserPo_t u = new UserPo_t();
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
		List<UserPo_t> s = q.list();
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

}
