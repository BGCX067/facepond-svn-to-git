package com.soa.facepond.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.soa.facepond.Constants;
import com.soa.facepond.model.User;
import com.soa.facepond.service.UserManager;

/*
 * Using the url with /open because that is not gated by the spring security
 */
@Controller
@RequestMapping("/open")
public class TestController {
	
	@Autowired
	private UserManager userManager;
	
    @RequestMapping(value="/say", method = RequestMethod.GET)
    public String handleRequest(@RequestParam(required = false, value = "q") String query) throws Exception {
        return "test/hello";
    }
    
    @RequestMapping(value= "/users", method = {RequestMethod.GET})
    public ModelAndView getUsers(@RequestParam(required = false, value = "q") String query) throws Exception {
        return new ModelAndView("admin/userList", Constants.USER_LIST, userManager.search("matt"));
    }

    @RequestMapping(value= "/users2", method = {RequestMethod.GET})
    public ModelAndView httpServletVersion(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String q = request.getParameter("q");
    	String username = request.getParameter("username");
    	User user = userManager.getUserByUsername(username);
    	
    	// you can do it this way and access user via the jsp admin/userList
    	request.setAttribute("user", user);
    	
        return new ModelAndView("admin/userList", Constants.USER_LIST, userManager.search("matt"));
    }
}
