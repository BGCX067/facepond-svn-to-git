package com.soa.facepond.dao.hibernate;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Table;

import org.hibernate.Query;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.soa.facepond.dao.UserDao;
import com.soa.facepond.model.Frequency;
import com.soa.facepond.model.User;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *   Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with 
 *   the new BaseDaoHibernate implementation that uses generics.
*/
@Repository("userDaoOld")
public class UserDaoHibernate extends GenericDaoHibernate<User, Long> implements UserDao, UserDetailsService {

    /**
     * Constructor that sets the entity to User.class.
     */
    public UserDaoHibernate() {
        super(User.class);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return getHibernateTemplate().find("from User u order by upper(u.username)");
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        if (log.isDebugEnabled()) {
            log.debug("user's id: " + user.getId());
        }
        getHibernateTemplate().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
        getHibernateTemplate().flush();
        return user;
    }

    /**
     * Overridden simply to call the saveUser method. This is happenening 
     * because saveUser flushes the session and saveObject of BaseDaoHibernate 
     * does not.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    @Override
    public User save(User user) {
        return this.saveUser(user);
    }

    /** 
     * {@inheritDoc}
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List users = getHibernateTemplate().find("from User where username=?", username);
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }

    /** 
     * {@inheritDoc}
    */
    public String getUserPassword(String username) {
        SimpleJdbcTemplate jdbcTemplate =
                new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from " + table.name() + " where username=?", String.class, username);

    }
    
	public List<User> findUsersWithYearlyFrequency(){
		Calendar date = Calendar.getInstance();  
	    date.setTime(new Date());  
	    Format f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	    date.add(Calendar.YEAR, -1);  

	    String hql = "from User where frequencyType = :frequencyType and lastUpdated <= :previousYear";
	    Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
	    q.setParameter("frequencyType", Frequency.YEARLY);
	    q.setParameter("previousYear", date.getTime());
	    
	    return q.list();
	}

	public List<User> findUsersWithMonthlyFrequency(){
		Calendar date = Calendar.getInstance();  
	    date.setTime(new Date());  
	    date.add(Calendar.MONTH, -1);  

	    String hql = "from User where frequencyType = :frequencyType and lastUpdated <= :previousMonth";
	    Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
	    q.setParameter("frequencyType", Frequency.MONTHLY);
	    q.setParameter("previousMonth", date.getTime());
	    
	    return q.list();
	}

	public List<User> findUsersWithDailyFrequency(){
		Calendar date = Calendar.getInstance();  
	    date.setTime(new Date());  
	    date.add(Calendar.DAY_OF_MONTH, -1);  

	    String hql = "from User where frequencyType = :frequencyType and lastUpdated <= :previousDay";
	    Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
	    q.setParameter("frequencyType", Frequency.DAILY);
	    q.setParameter("previousDay", date.getTime());
	    
	    return q.list();
	}

	public List<User> findUsersWithHourlyFrequency(){
		Calendar date = Calendar.getInstance();  
	    date.setTime(new Date());  
	    date.add(Calendar.HOUR, -1);  

	    String hql = "from User where frequencyType = :frequencyType and lastUpdated <= :previousHour";
	    Query q = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery(hql);
	    q.setParameter("frequencyType", Frequency.HOURLY);
	    q.setParameter("previousHour", date.getTime());
	    
	    return q.list();
	}

}
