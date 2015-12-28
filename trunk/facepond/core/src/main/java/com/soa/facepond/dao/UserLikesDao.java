package com.soa.facepond.dao;

import java.util.List;

import com.soa.facepond.model.User;
import com.soa.facepond.model.UserLikes;

public interface UserLikesDao extends GenericDao<UserLikes, Long> {
	public UserLikes findLikesForUser(User user);
}
