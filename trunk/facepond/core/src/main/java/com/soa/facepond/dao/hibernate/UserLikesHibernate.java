package com.soa.facepond.dao.hibernate;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.soa.facepond.dao.UserLikesDao;
import com.soa.facepond.model.User;
import com.soa.facepond.model.UserLikes;

@Repository("userLikesDao")
public class UserLikesHibernate extends GenericDaoHibernate<UserLikes, Long> implements UserLikesDao {

	public UserLikesHibernate() {
		super(UserLikes.class);
	}

	public UserLikes findLikesForUser(User user){
	    String hql = "from UserLikes where user = :user";
	    Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
	    q.setParameter("user", user);
	    
	    return (UserLikes)q.uniqueResult();
	}
}
