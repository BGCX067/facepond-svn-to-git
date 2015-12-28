package com.soa.facepond.service;

import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import com.soa.facepond.model.User;
import com.soa.facepond.model.UserDeals;

@WebService
@Path("/")
@Produces({"application/json", "application/xml"})
public interface UserDealsManager extends GenericManager<UserDeals, Long> {
	
    @GET
    @Path("/deals/{id}")
    public List<UserDeals> findDealsForUser(@PathParam("id") String userId);
    
}
