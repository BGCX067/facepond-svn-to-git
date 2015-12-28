package com.soa.facepond.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.facepond.dao.UserDealsDao;
import com.soa.facepond.model.User;
import com.soa.facepond.model.UserDeals;
import com.soa.facepond.service.UserDealsManager;

@Service("userDealseManager")
public class UserDealsManagerImpl extends GenericManagerImpl<UserDeals, Long> implements UserDealsManager {
    UserDealsDao userDealsDao;

    @Autowired
    public UserDealsManagerImpl(UserDealsDao userDealsDao) {
        super(userDealsDao);
        this.userDealsDao = userDealsDao;
    }
    
	public List<UserDeals> findDealsForUser(String userId) {
		return this.userDealsDao.findDealsForUser(userId);
	}

}
