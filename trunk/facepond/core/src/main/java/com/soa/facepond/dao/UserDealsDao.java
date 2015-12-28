package com.soa.facepond.dao;

import java.util.List;

import com.soa.facepond.model.User;
import com.soa.facepond.model.UserDeals;

public interface UserDealsDao extends GenericDao<UserDeals, Long> {

	public List<UserDeals> findDealsForUser(String id);
}
