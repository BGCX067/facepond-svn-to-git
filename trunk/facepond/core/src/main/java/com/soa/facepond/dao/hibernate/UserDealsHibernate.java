package com.soa.facepond.dao.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.soa.facepond.dao.UserDealsDao;
import com.soa.facepond.model.User;
import com.soa.facepond.model.UserDeals;

@Repository("userDealsDao")
public class UserDealsHibernate extends GenericDaoHibernate<UserDeals, Long> implements UserDealsDao {

	public UserDealsHibernate(){
		super(UserDeals.class);
	}

	public List<UserDeals> findDealsForUser(String id) {
	    String hql = "from UserDeals where userId = :userId";
	    Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
	    q.setParameter("userId", Long.parseLong(id));
	    
	    return q.list();
	}
	
	
}
