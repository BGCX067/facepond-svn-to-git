package com.soa.facepond.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Component;

import com.soa.facepond.service.GrouponService;
import com.soa.facepond.service.RemoteService;

@Component("grouponService")
public class GrouponServiceImpl implements GrouponService {

	//private final static String endpoint = "http://api.groupon.com/v2/";
	private final static String endpoint = "http://15033.broker.soa.com/groupon/";
	
	public JsonNode getDeals(String location) throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("division_id", location);
		params.put("client_id", "8d8b68eb8a477bcbf4c5c64d264547b9164834dc");
		
		
		
		RemoteService remoteService = new RemoteServiceImpl(endpoint);

		Header header = new Header();
		header.setName("Authorization");
		header.setValue("Atmosphere atmosphere_app_id=atmosphere-52vGCyzZCXu5Lqhyw1ofd39o");
		Map<String, String> response = remoteService.getMethod("deals.json?", params, header);
		
		ObjectMapper m = new ObjectMapper(); 
		//System.out.println(response.get("responseBody"));
		JsonNode element = m.readTree(response.get("responseBody"));
		JsonNode deals = element.path("deals");

		return deals;
	}
}
