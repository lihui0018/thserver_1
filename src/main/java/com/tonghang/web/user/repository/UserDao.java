package com.tonghang.web.user.repository;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.user.pojo.UserPo;

@Repository
@Transactional
public class UserDao {
	
	@Resource
	private SessionFactory sessionFactory;
	
	@Resource
	private SessionFactory sessionFactory_1;

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
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> insertAmount() {
		for(int i=0;i<10000;i++){
			UserPo u = new UserPo();
			u.setUsername("bbb"+i);
			u.setEmail("b@b.b"+i);
			u.setPassword("456"+i);
			u.setTags("j,bbb,java实施,"+i);
			sessionFactory.getCurrentSession().saveOrUpdate(u);
		}
		return (Map<String, Object>) new HashMap<>().put("result", true);
	}

}
