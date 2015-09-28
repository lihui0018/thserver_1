package com.tonghang.web.location.dao;

import com.tonghang.web.location.pojo.Location;

public interface LocationDao_t {

	public void saveOrUpdateLocation(Location location);
	
	public double getDistanceByLocation(Location me,Location him);
	
	public Location findLocationByUser(String userId);
}
