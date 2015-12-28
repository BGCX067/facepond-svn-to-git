package com.soa.facepond;


import com.soa.facepond.model.User;

public interface UserService {

	public boolean isNewUser(User user) throws Exception;
	public void updateUser(User user) throws Exception;
	public void addUser(User user) throws Exception;
	public User getUserById(long id) throws Exception;
	public User getUserByAuthId(String authId) throws Exception;
	
	public void removeDealsByUser(long userId) throws Exception;
}
