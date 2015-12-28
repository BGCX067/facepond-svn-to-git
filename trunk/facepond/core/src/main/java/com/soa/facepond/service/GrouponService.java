package com.soa.facepond.service;

import org.codehaus.jackson.JsonNode;

public interface GrouponService {
	public JsonNode getDeals(String location) throws Exception ;
}
