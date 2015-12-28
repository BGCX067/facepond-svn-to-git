package com.soa.facepond.service;

import java.util.List;

import com.soa.facepond.model.User;
import com.soa.facepond.model.UserLikes;

public interface UserLikesManager extends GenericManager<UserLikes, Long> {

	public UserLikes findLikesForUser(User user);
}
