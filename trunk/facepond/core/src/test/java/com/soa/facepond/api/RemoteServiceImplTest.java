package com.soa.facepond.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.soa.facepond.model.UserDeals;
import com.soa.facepond.service.RemoteService;
import com.soa.facepond.service.impl.RemoteServiceImpl;


public class RemoteServiceImplTest extends TestCase {
	
	private final static String endpoint = "http://15033.broker.soa.com/groupon/deals";

	public void testGrouponDeals() throws Exception{
		Map<String, String> params = new HashMap<String, String>();
		params.put("division_id", "boston");
		params.put("client_id", "8d8b68eb8a477bcbf4c5c64d264547b9164834dc");
		   
		RemoteService remoteService = new RemoteServiceImpl("http://api.groupon.com/v2/");
		   
		Map<String, String> response = remoteService.getMethod("deals.json?", params, null);
		 
		ObjectMapper m = new ObjectMapper(); 
		JsonNode element = m.readTree(response.get("responseBody"));
		JsonNode deals = element.path("deals");
 
		List<JsonNode> userDeals = new ArrayList<JsonNode>();
		
		String[] likesToken = new String[]{"Arts and Entertainment", "heart"};

		for(JsonNode field : deals){
			boolean foundDeal = false;
			for(String like : likesToken){
				JsonNode node = field.get("tags");
				List<String> values = node.findValuesAsText("name");
				for(String value : values){
					if(like.toLowerCase().equals(value.toLowerCase())){
						System.out.println(value);
						userDeals.add(field);
						foundDeal = true;
						break;
					}
				}
				if(foundDeal == true){
					break;
				}
			}
		}
		
		for(JsonNode deal : userDeals){
			System.out.println(deal.toString());
		}		

		/*
		List<Deal> list = m.readValue(deals, new TypeReference<List<Deal>>(){});
		
		    
		 
		System.out.println("size:" + list.size());
		for(Deal deal : list){  
			System.out.println("mydeals:" + deal.getId());
		}  
		*/
		 
		//System.out.println("json:" + element.toString());

	}


}
