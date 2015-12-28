package com.soa.facepond;

import java.util.Map;

import com.soa.facepond.model.User;

public interface IUserDao {
	public boolean isNewUser(String authId) throws Exception;
	public void addUser(User user) throws Exception;
	public void updateUser(User user) throws Exception;
	public Map<String, String> loadCityDivisions() throws Exception;
	public User getUserById(long id) throws Exception;
	public User getUserByAuthId(String authId) throws Exception;
	
	public void removeDealsByUser(long userId) throws Exception;
}
