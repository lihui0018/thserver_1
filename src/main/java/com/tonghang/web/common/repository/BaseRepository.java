package com.tonghang.web.common.repository;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class BaseRepository<T> {
	
	@Resource
	private SessionFactory sessionFactory;
	
	public T save(T t){
		sessionFactory.getCurrentSession().saveOrUpdate(t);
		return t;
	}
	
}
