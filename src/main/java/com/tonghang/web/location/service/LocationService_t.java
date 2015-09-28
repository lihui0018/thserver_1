package com.tonghang.web.location.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.location.dao.LocationDao_t;
import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.user.pojo.User;

@Service
public class LocationService_t {

	@Resource
	private LocationDao_t locationDao_t;
	
	public void saveLocation(User user,double x_point,double y_point){
		Location location = new Location();
		location.setUser(user);
		location.setX_point(x_point);
		location.setY_point(y_point);
		locationDao_t.saveOrUpdateLocation(location);
	}
	/**
	 * 业务功能：计算两点之间的距离
	 * @param me
	 * @param him
	 * @return
	 */
	public double getDistanceByLocation(Location me,Location him){
		return locationDao_t.getDistanceByLocation(me, him);
	}
	/**
	 * 业务功能：查找某用户的地理位置（坐标）
	 * @param user
	 * @return
	 */
	public Location findLocationByUser(String userId){
		return locationDao_t.findLocationByUser(userId);
	}
}
