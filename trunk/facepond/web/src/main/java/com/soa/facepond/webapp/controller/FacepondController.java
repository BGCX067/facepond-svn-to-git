package com.soa.facepond.webapp.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.soa.facepond.UserService;
import com.soa.facepond.model.User;
import com.soa.facepond.model.UserDeals;
import com.soa.facepond.service.FacebookAuthenticator;
import com.soa.facepond.service.FacepondService;
import com.soa.facepond.service.UserDealsManager;
import com.soa.facepond.service.GrouponService;

@Controller
@RequestMapping("/facepond")
public class FacepondController {

	protected final transient Log log = LogFactory.getLog(FacepondController.class);
	
	private FacebookAuthenticator facebookAuthenticator;
	private UserService userService;
	private FacepondService facepondService;
	private UserDealsManager userDealsManager;
	private GrouponService grouponService;

    @RequestMapping(value= "", method = {RequestMethod.GET})
    public ModelAndView facepondLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
        return new ModelAndView("welcome");
    }
    
    @RequestMapping(value= "/updateUserDeals", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody String updateDeals(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	request.setAttribute("userId", request.getParameter("userId"));
    	try {
    		facepondService.updateUserDeals(Long.parseLong(request.getParameter("userId")));
    	} catch (Exception e) {
    		return "failed";
    	}
    	return "deals_done";
    }
    
    @RequestMapping(value= "/getUserDeals", method = {RequestMethod.GET, RequestMethod.POST})
    public @ResponseBody Map<String, List<UserDeals>> getDeals(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	List<UserDeals> deals = userDealsManager.findDealsForUser(request.getParameter("userId"));
    	Map<String, List<UserDeals>> userDeals = new HashMap<String, List<UserDeals>>();
    	userDeals.put("deals", deals);
        return userDeals;
    }    
    
    @RequestMapping(value= "/dashboard", method = {RequestMethod.GET})
    public @ResponseBody String facepondDashboard(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String json = "{\"deals\":[{\"id\": \"metropark\"}]}";
    	
        return json;
    }
 
    @RequestMapping(value= "/initiateLogin", method = {RequestMethod.GET})
	public void initiateSSOLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String redirectUri = facebookAuthenticator.login();
		response.sendRedirect(redirectUri);
	}
	
    @RequestMapping(value= "/loginWithSSO", method = {RequestMethod.GET})
    public ModelAndView loginWithSSO(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	User user = null;
    	try {
	    	Enumeration<String> params = request.getParameterNames();
	    	Map<String, String> props = new HashMap<String, String>();
	    	while (params.hasMoreElements()) {
	    		String param = params.nextElement();
	    		props.put(param, request.getParameter(param));
	    		System.out.println("Param: "+param);
	    		System.out.println("Value=="+request.getParameter(param));
	    	}
	    	  	
	    	user = facebookAuthenticator.verify(props);
	    	HttpSession session = request.getSession();
	    	session.setAttribute("UserName", user.getFirstName() + " " + user.getLastName());
	    	session.setAttribute("AuthID", user.getAuthId());
	    	session.setAttribute("UserID", user.getId().toString());
	    	
    	} catch(Exception e) {
    		log.error(""+e);
    		//response.sendRedirect("/facepondpostlogin.html?status=failure&msg=error occurred during login");
    		Map<String, String> output = new HashMap<String, String>();
        	output.put("status", "failure");
    		output.put("userState", "failure");
    		return new ModelAndView("facepondpostlogin", "result", output);
    	}
    	boolean isNewUser = userService.isNewUser(user);
    	Map<String, String> output = new HashMap<String, String>();
    	output.put("status", "success");
		output.put("userState", "existing");
		output.put("userId", user.getId().toString());
		output.put("userName", user.getFirstName() +" "+user.getLastName());
    	if (isNewUser) {
    		userService.addUser(user);    		
    		
    		output.put("userState", "new");
    		return new ModelAndView("facepondpostlogin", "result", output);
    		//response.sendRedirect("/facepondpostlogin.html?status=success&userState=new");
    	} else {
    		//userService.updateUser(user);
    		
    		output.put("userState", "existing");
    		return new ModelAndView("facepondpostlogin", "result", output);
    		//response.sendRedirect("/facepondpostlogin.html?status=success&userState=");
    	}
    	
    }

    @RequestMapping(value= "/getUser", method = {RequestMethod.GET})
	public @ResponseBody User getUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	System.out.println(request.getSession().getAttribute("AuthID"));
		HttpSession session = request.getSession();
		String authId = (String)session.getAttribute("AuthID");
		if (authId == null) {
			throw new Exception("user not logged in. cannot retrive details");
		}
		User user = userService.getUserByAuthId(authId);
		if (user == null) {
			throw new Exception("user does not exist, can not retieve details.");
		}
		return user;
	}
    
    @RequestMapping(value= "/getUserLikes", method = {RequestMethod.GET})
	public @ResponseBody String getUserLikes(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	System.out.println(request.getSession().getAttribute("UserID"));
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("UserID");
		if (userId == null) {
			throw new Exception("user not logged in. cannot retrive details");
		}
		String likes = facebookAuthenticator.getFBDealsForUser(new Long(userId));
		System.out.println(likes);
		return likes;
	}
    
    
    
    @RequestMapping(value= "/getDealsFromGroupon", method = {RequestMethod.GET})
	public @ResponseBody String getDealsFromGroupon(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	System.out.println(request.getSession().getAttribute("UserID"));
		HttpSession session = request.getSession();
		String userId = (String)session.getAttribute("UserID");
		if (userId == null) {
			throw new Exception("user not logged in. cannot retrive details");
		}
		JsonNode node = grouponService.getDeals("los-angeles");
		
		return node.getTextValue();
	}
    
	public static String addQueryParameter(String url, String name,
			String value) {
		String retUrl = url;
		if (url.indexOf("?") == -1)
			retUrl += "?";
		else 
			retUrl += "&";
		
		try {
			retUrl += name + "=" + URLEncoder.encode(value, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		return retUrl;
	}
	
	@Autowired
	public void setFacebookAuthenticator(
			FacebookAuthenticator facebookAuthenticator) {
		this.facebookAuthenticator = facebookAuthenticator;
	}
	
	@Autowired
	@Qualifier("userServiceNew")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	@Autowired
	public void setFacepondService(FacepondService facepondService) {
		this.facepondService = facepondService;
	}
	@Autowired
	public void setUserDealsManager(UserDealsManager userDealsManager) {
		this.userDealsManager = userDealsManager;
	}
	
	@Autowired
	public void setGrouponService(GrouponService grouponService) {
		this.grouponService = grouponService;
	}
}
