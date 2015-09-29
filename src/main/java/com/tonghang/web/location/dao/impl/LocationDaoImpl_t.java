package com.tonghang.web.location.dao.impl;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.location.dao.LocationDao_t;
import com.tonghang.web.location.pojo.Location;

@Repository("locationDao_t")
public class LocationDaoImpl_t implements LocationDao_t{

	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;
//	public void setSessionFactory(SessionFactory sessionFactory) {
//		this.sessionFactory = sessionFactory;
//	}
	
	@Override
	public double getDistanceByLocation(Location me, Location him) {
		// TODO Auto-generated method stub
//		Session session = sessionFactory.openSession();
//		if(!session.getTransaction().isActive()){
//			session.getTransaction().begin();
//		}
		double distance = Double.MAX_VALUE;
		System.out.println("double 最大值："+distance);
		if(him!=null){
			distance = (Double) sessionFactory.getCurrentSession().createSQLQuery("select round(6378.138*2*asin(sqrt(pow(sin( (:x1 *pi()/180-:x2 *pi()/180)/2),2)+cos(:x1*pi()/180)*cos(:x2*pi()/180)" +
					"* pow(sin( (:y1*pi()/180-:y2*pi()/180)/2),2)))*1000) as distance").setParameter("x1", me.getX_point()).setParameter("x2", him.getX_point())
					.setParameter("y1", me.getY_point()).setParameter("y2", him.getY_point()).uniqueResult();
		}
//		session.close();
		return distance;
	}

	@Override
	public Location findLocationByUser(String userId) {
		// TODO Auto-generated method stub
//		Session session = sessionFactory.openSession();
//		if(!session.getTransaction().isActive()){
//			session.getTransaction().begin();
//		}
		Location local = (Location) sessionFactory.getCurrentSession().get(Location.class, userId);
//		session.getTransaction().commit();
//		session.close();
		return local;
	}

	@Override
	public void saveOrUpdateLocation(Location location) {
		// TODO Auto-generated method stub
//		Session session = sessionFactory.openSession();
//		if(!session.getTransaction().isActive()){
//			session.getTransaction().begin();
//		}
		sessionFactory.getCurrentSession().saveOrUpdate(location);
//		session.getTransaction().commit();
//		session.close();
	}

}
