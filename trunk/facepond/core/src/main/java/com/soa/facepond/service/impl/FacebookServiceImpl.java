package com.soa.facepond.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.soa.facepond.service.FacebookService;
import com.soa.facepond.service.RemoteService;

@Component("facebookService")
public class FacebookServiceImpl implements FacebookService {

	private final static String endpoint = "";
	
	@Autowired
	private RemoteService remoteService;
	
	public void getLatestLikes(){
		
	}
}
