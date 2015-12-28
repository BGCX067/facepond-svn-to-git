package com.soa.facepond.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.soa.facepond.UserService;
import com.soa.facepond.model.User;
import com.soa.facepond.model.UserDeals;
import com.soa.facepond.service.FacebookAuthenticator;
import com.soa.facepond.service.FacebookService;
import com.soa.facepond.service.FacepondService;
import com.soa.facepond.service.GrouponService;
import com.soa.facepond.service.RemoteService;
import com.soa.facepond.service.UserDealsManager;
import com.soa.facepond.service.UserLikesManager;
import com.soa.facepond.service.UserManager;

@Component("facepondService")
public class FacepondServiceImpl implements FacepondService {

	@Autowired
	private UserManager userManager;
	
	@Autowired
	@Qualifier("userServiceNew")
	private UserService userService;
	
	@Autowired
	private UserDealsManager userDealsManager;
	
	@Autowired
	private UserLikesManager userLikesManager;
	
	@Autowired
	private RemoteService remoteService;
	
	@Autowired
	private GrouponService grouponService;
	
	@Autowired
	private FacebookService facebookService;
	
	@Autowired
	private FacebookAuthenticator facebookAuthenticator;
	
	public void updateUserDeals(Long userId) throws Exception {
		
		//List<User> users = userManager.getAll();
		
		//System.out.println("users:" + users.size());
		
		User user = null;
		try {
			user = userService.getUserById(userId);
			if (user.getDivisionId() == null) {
				throw new Exception("Division not supported in facepond/groupon.");
			}
			userService.removeDealsByUser(userId);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			throw e1;
		}
		
		//for(User user : users){
			List<JsonNode> userDeals = new ArrayList<JsonNode>();
			try {
				JsonNode deals = grouponService.getDeals(user.getDivisionId());
				System.out.println(deals.toString());
				if(deals == null){
					return;
				}
				
				String likes = facebookAuthenticator.getFBDealsForUser(user.getId());

				String[] likesToken = likes.split(",");

				for(JsonNode field : deals){
					boolean foundDeal = false;
					for(String like : likesToken){
						JsonNode node = field.get("tags");
						List<String> values = node.findValuesAsText("name");
						for(String value : values){
							String[] likeSplit = like.toLowerCase().split(" ");
							for (String splitLike : likeSplit) {
								if(value.toLowerCase().contains(splitLike)){
									userDeals.add(field);
									foundDeal = true;
									break;
								}
							}
							if(foundDeal){
								break;
							}
						}
						if(foundDeal){
							break;
						}
					}
				}
				
				for(JsonNode deal : userDeals){
					UserDeals uDeals = new UserDeals();
					uDeals.setUserId(user.getId());
					uDeals.setDeals(deal.toString());
					userDealsManager.save(uDeals);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
		//}

	}

}
