package com.soa.facepond.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.facepond.dao.UserLikesDao;
import com.soa.facepond.model.User;
import com.soa.facepond.model.UserLikes;
import com.soa.facepond.service.UserLikesManager;

@Service("userLikesManager")
public class UserLikesManagerImpl  extends GenericManagerImpl<UserLikes, Long> implements UserLikesManager {
    UserLikesDao userLikesDao;

    @Autowired
    public UserLikesManagerImpl(UserLikesDao userLikesDao) {
        super(userLikesDao);
        this.userLikesDao = userLikesDao;
    }

    public UserLikes findLikesForUser(User user){
    	return this.userLikesDao.findLikesForUser(user);
    }
}
