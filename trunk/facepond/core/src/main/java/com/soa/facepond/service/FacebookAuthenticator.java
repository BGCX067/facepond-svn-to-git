package com.soa.facepond.service;

import java.util.Map;

import com.soa.facepond.model.User;

public interface FacebookAuthenticator {
	public String login() throws Exception;
	public User verify(Map<String, String> properties) throws Exception;
	public String getFBDealsForUser(long userId) throws Exception;
}
