package com.tonghang.web.location.dao;

import com.tonghang.web.location.pojo.Location;
import com.tonghang.web.location.pojo.LocationPo;

public interface LocationDao_t {

	public void saveOrUpdateLocation(LocationPo location);
	
	public double getDistanceByLocation(Location me,Location him);
	
	public Location findLocationByUser(String userId);
}
