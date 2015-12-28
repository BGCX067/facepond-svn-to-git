package com.soa.facepond;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.soa.facepond.model.User;

@Service("userServiceNew")
public class UserServiceImpl implements UserService {

	IUserDao userDao;
	Map<String, String> cityDivisionMap = null;
	public boolean isNewUser(User user) throws Exception {
		if(userDao == null){
			System.out.println("userDao is null");
		}else{
			System.out.println("userDao is not null");
		}
		if(user == null){
			System.out.println("user is null");
		}else{
			System.out.println("user is not null");
		}
		return userDao.isNewUser(user.getAuthId());
	}

	public void updateUser(User user) throws Exception {
		// TODO Auto-generated method stub

	}

	public void addUser(User user) throws Exception {
		if (cityDivisionMap == null) {
			loadCityDivisions();
		}
		if (user.getCity() == null || user.getCity().trim().equalsIgnoreCase("Thirroul")) {
			user.setCity("Los Angeles");
        	user.setState("California");
		}
		
		user.setDivisionId(cityDivisionMap.get(user.getCity()));
		if (user.getDivisionId() == null) {
			String city = user.getCity().toLowerCase();
			city = city.replace(" / ", "-");
			city = city.replace("/", "-");
			city = city.replace(", ", "-");
			city = city.replace(",", "-");		
			city = city.replace("'", "");
			city = city.replace(" ", "-");			
			
			/*if (city.indexOf("/") != -1) {
				city = city.substring(0, city.indexOf("/"));
			}*/
			user.setDivisionId(city);
		} 
		
		userDao.addUser(user);
	}

	public User getUserById(long id) throws Exception {
		return userDao.getUserById(id);
	}

	public User getUserByAuthId(String authId) throws Exception {
		return userDao.getUserByAuthId(authId);
	}
	
	public void removeDealsByUser(long userId) throws Exception {
		userDao.removeDealsByUser(userId);
	}
	
	private void loadCityDivisions() throws Exception {
		this.cityDivisionMap = userDao.loadCityDivisions();
	}

	@Autowired
	@Qualifier("userDaoNew")
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}
}
