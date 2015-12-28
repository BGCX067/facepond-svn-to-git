package com.soa.facepond.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.soa.facepond.AuthDao;
import com.soa.facepond.UserService;
import com.soa.facepond.model.Frequency;
import com.soa.facepond.model.User;
import com.soa.facepond.service.FacebookAuthenticator;
import com.soa.facepond.util.JsonUtil;

@Service("facebookAuthenticator")
public class FacebookAuthenticatorImpl implements FacebookAuthenticator {
	
	protected final transient Log log = LogFactory.getLog(FacebookAuthenticatorImpl.class);
	
	public static final String RETURN_URL_PROPERTY = "returnURL";  // The servlet URL associated with Facebook login (used by Facebook for redirects/callbacks)
	public static final String APP_ID_PROPERTY = "app.id";          // The application ID of our application registered in Facebook
	public static final String APP_SECRET_PROPERTY = "app.secret";  // The secret code of our application registered in Facebook

	// URL query parameters returned in Facebook responses/redirects
	private static final String ACCESS_CODE_PARAM = "code";             // Access code used to retrieve an access token
	private static final String ACCESS_TOKEN_PARAM = "access_token";    // Access token sent to graph API to retrieve user info
	private static final String CLIENT_ID_PARAM = "client_id";          // Set to the application ID of our Facebook app
	private static final String RETURN_URL_PARAM = "return.url";
	private static final String CLIENT_SECRET_PARAM = "client_secret";  // Application's secret ID
	private static final String SCOPE_PARAM = "scope";  // Application's secret ID
	private static final String REDIRECT_URI_PARAM = "redirect_uri"; 
	private static final String APP_ID = "446309825388322";
	private static final String APP_SECRET = "f8a03eb79d45b4436519fc266ffb31d8";
	private static final String returnToUrl = "http://facepond.couponpond.com:8080/facepond/loginWithSSO/";
	// JSON keys used in responses from Facebook
	private static final String USER_ID_KEY = "id";
	private static final String USER_FIRST_NAME_KEY = "first_name";
	private static final String USER_LAST_NAME_KEY = "last_name";
	private static final String USER_EMAIL_KEY = "email";

	// Scope parameter value
	private static final String SCOPE_VALUE = "user_likes,email,user_location";
	
	// Authentication ID format substitution parameters
	private static final String AUTH_ID_FORMAT_USER_PARAMETER = "{user.id}";
	
	private AuthDao authDao;
	private UserService userService;
	Map<String, String> authProperties = null;
	
	public String login() throws Exception {
		
		//==================//
		//      STEP 1      //
		//==================//
		// Request Facebook authorization, will return an access code
		String authRequestString = null;
		String oauthUrl = "https://www.facebook.com/dialog/oauth";
		if (authProperties == null) {
			authProperties = authDao.loadAuthProperties();
		}
		String returnUrl = authProperties.get(RETURN_URL_PARAM);
		String appId = authProperties.get(APP_ID_PROPERTY);
		String appSecret = authProperties.get(APP_SECRET_PROPERTY);
		if (appId == null || appSecret == null || returnUrl == null) {
			authProperties = authDao.loadAuthProperties();
			returnUrl = authProperties.get(RETURN_URL_PARAM);
			appId = authProperties.get(APP_ID_PROPERTY);
			appSecret = authProperties.get(APP_SECRET_PROPERTY);
		}
		
		authRequestString = oauthUrl
				+ "?" + CLIENT_ID_PARAM + "=" + appId
				+ "&" + REDIRECT_URI_PARAM + "=" + URLEncoder.encode(returnUrl, "UTF-8")
				+ "&" + SCOPE_PARAM + "=" + SCOPE_VALUE
				;
			
		
		return authRequestString;
		
	}

	public User verify(Map<String, String> properties) throws Exception {
		HttpConnectionManager mgr = null;
   		try {
   			
			// Construct redirect_url so that it exactly matches the redirect_url param from Step 1
			String accessTokenUrl = "https://graph.facebook.com/oauth/access_token";
			if (authProperties == null) {
				authProperties = authDao.loadAuthProperties();
			}
			String returnUrl = authProperties.get(RETURN_URL_PARAM);
			String appId = authProperties.get(APP_ID_PROPERTY);
			String appSecret = authProperties.get(APP_SECRET_PROPERTY);
			if (appId == null || appSecret == null || returnUrl == null) {
				authProperties = authDao.loadAuthProperties();
				returnUrl = authProperties.get(RETURN_URL_PARAM);
				appId = authProperties.get(APP_ID_PROPERTY);
				appSecret = authProperties.get(APP_SECRET_PROPERTY);
			}
			String accessTokenRequest = accessTokenUrl 
					+ "?" + CLIENT_ID_PARAM + "=" + appId 
					+ "&" + REDIRECT_URI_PARAM + "=" + URLEncoder.encode(returnUrl, "UTF-8")
					+ "&" + CLIENT_SECRET_PARAM + "=" + appSecret
					+ "&" + ACCESS_CODE_PARAM + "=" + properties.get(ACCESS_CODE_PARAM);
			
			// Call the Facebook graph API URL using the URL utility class
			System.out.println(accessTokenRequest);
			GetMethod getMethod = new GetMethod(accessTokenRequest);
			mgr = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(mgr);
			int returnCode = httpClient.executeMethod(getMethod);

			String responseBody = new String(getMethod.getResponseBody());
			System.out.println("response is \n" + responseBody);

			if (returnCode == HttpStatus.SC_INTERNAL_SERVER_ERROR
					|| responseBody.indexOf("OAuthException") > 0) {
				String message = getMethod.getStatusText();
				throw new Exception(message);
			}
			
			log.debug("responseBody="+responseBody);
			String accessToken = responseBody.substring(responseBody.indexOf('=')+1, responseBody.indexOf('&'));
			log.debug("accessToken="+accessToken);
			User user = new User();
		 	if (accessToken != null) {
				String userInfoUrl = "https://graph.facebook.com/me";
				String userInfoRequest = userInfoUrl + "?" + ACCESS_TOKEN_PARAM + "=" + URLEncoder.encode(accessToken, "UTF-8");
				
				// Call the Facebook graph API URL using the URL utility class
				getMethod = new GetMethod(userInfoRequest);
				returnCode = httpClient.executeMethod(getMethod);
				responseBody = new String(getMethod.getResponseBody());
				System.out.println("response is \n" + responseBody);

				if (returnCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					String message = getMethod.getStatusText();
					throw new Exception(message);
				}
				
		        Map<String, String> userInfo = JsonUtil.getMapFromJsonString(responseBody);
		        user.setId(new Long(userInfo.get("id")));
		        String authIdFormat = "http://www.facebook.com/{user.id}";
		        String authId = authIdFormat.replace(AUTH_ID_FORMAT_USER_PARAMETER, userInfo.get(USER_ID_KEY));
		        user.setAuthId(authId);
		        user.setAccessCode(properties.get(ACCESS_CODE_PARAM));
		        Object location = userInfo.get("location");
		        if (location != null) {
			        try {
			        	LinkedHashMap<String, String> locationMap = (LinkedHashMap<String, String>)location;
			        	String userLocationTest = location.toString();
			        	String userLocation = locationMap.get("name");
			        	user.setCity(userLocation.substring(0, userLocation.indexOf(",")));
			        	user.setState(userLocation.substring(userLocation.indexOf(",")+1));
			        } catch (ClassCastException ccEx) {
			        	String userLocation = location.toString();
			        	userLocation = userLocation.substring(userLocation.indexOf("name=")+5, userLocation.lastIndexOf("}"));
			        	user.setCity(userLocation.substring(0, userLocation.indexOf(",")));
			        	user.setState(userLocation.substring(userLocation.indexOf(",")+1));
			        }
		        } else {
		        	user.setCity("Los Angeles");
		        	user.setState("California");
		        }
		        user.setFirstName(userInfo.get(USER_FIRST_NAME_KEY));
		        user.setLastName(userInfo.get(USER_LAST_NAME_KEY));
		        user.setEmail(userInfo.get(USER_EMAIL_KEY));
		        user.setFrequencyType(Frequency.YEARLY);
		        
			} else {
				throw new Exception("Access token can not be retrieved.");
			}
			return user;
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (mgr != null)
				mgr.closeIdleConnections(0);
			mgr = null;
		}

	}

	public String getFBDealsForUser(long userId) throws Exception {
		User user = userService.getUserById(userId);
		HttpConnectionManager mgr = null;
   		try {
   			
			// Construct redirect_url so that it exactly matches the redirect_url param from Step 1
			String accessTokenUrl = "https://graph.facebook.com/oauth/access_token";
			if (authProperties == null) {
				authProperties = authDao.loadAuthProperties();
			}
			String returnUrl = authProperties.get(RETURN_URL_PARAM);
			String appId = authProperties.get(APP_ID_PROPERTY);
			String appSecret = authProperties.get(APP_SECRET_PROPERTY);
			if (appId == null || appSecret == null || returnUrl == null) {
				authProperties = authDao.loadAuthProperties();
				returnUrl = authProperties.get(RETURN_URL_PARAM);
				appId = authProperties.get(APP_ID_PROPERTY);
				appSecret = authProperties.get(APP_SECRET_PROPERTY);
			}
			String accessTokenRequest = accessTokenUrl 
					+ "?" + CLIENT_ID_PARAM + "=" + appId 
					+ "&" + REDIRECT_URI_PARAM + "=" + URLEncoder.encode(returnUrl, "UTF-8")
					+ "&" + CLIENT_SECRET_PARAM + "=" + appSecret
					+ "&" + ACCESS_CODE_PARAM + "=" + user.getAccessCode();
			
			// Call the Facebook graph API URL using the URL utility class
			System.out.println(accessTokenRequest);
			GetMethod getMethod = new GetMethod(accessTokenRequest);
			mgr = new MultiThreadedHttpConnectionManager();
			HttpClient httpClient = new HttpClient(mgr);
			int returnCode = httpClient.executeMethod(getMethod);

			String responseBody = new String(getMethod.getResponseBody());
			System.out.println("response is \n" + responseBody);

			if (returnCode == HttpStatus.SC_INTERNAL_SERVER_ERROR
					|| responseBody.indexOf("OAuthException") > 0) {
				String message = getMethod.getStatusText();
				throw new Exception(message);
			}
			
			log.debug("responseBody="+responseBody);
			String accessToken = responseBody.substring(responseBody.indexOf('=')+1, responseBody.indexOf('&'));
			System.out.println("accessToken= "+accessToken);
			if (accessToken != null) {
				String likesUrl = "http://facebook.broker.soa.com/facebook/"+userId+"/likes";
				//"https://graph.facebook.com/"+userId+"/likes";
				//String userInfoUrl = "https://graph.facebook.com/me";
				String userLikesRequest = likesUrl + "?" + ACCESS_TOKEN_PARAM + "=" + URLEncoder.encode(accessToken, "UTF-8");
				
				// Call the Facebook graph API URL using the URL utility class
				getMethod = new GetMethod(userLikesRequest);
				getMethod.setRequestHeader("Authorization", "Atmosphere atmosphere_app_id=atmosphere-52vGCyzZCXu5Lqhyw1ofd39o");
				returnCode = httpClient.executeMethod(getMethod);
				responseBody = new String(getMethod.getResponseBody());
				System.out.println("response is \n" + responseBody);

				if (returnCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					String message = getMethod.getStatusText();
					throw new Exception(message);
				}
				
				/*
				 * { "data" : [ { "category" : "Product/service",
        "created_time" : "2012-06-18T03:33:12+0000",
        "id" : "74250042688",
        "name" : "KYMCO USA"
      },
      { "category" : "Consulting/business services",
        "created_time" : "2012-06-18T03:33:09+0000",
        "id" : "184124915024995",
        "name" : "Foundry Solutions, LLC"
      }
    ],
  "paging" : { "next" : "https://graph.facebook.com/100002838142803/likes?access_token=AAAGV6pqaHyIBAIF2SeRxwUmhKtDZBphtUgt9hZAhV7R9Y1eyqZBSNVualsxAvnvOExMwFxneV0wN61ktyucAYtgvH9qRVZBJrVIsA0T0bAZDZD&limit=5000&offset=5000&__after_id=184124915024995" }
}
				 */
				
		        //Map<String, String> userInfo = JsonUtil.getMapFromJsonString(responseBody);
				
				ObjectMapper m = new ObjectMapper(); 
				JsonNode element = m.readTree(responseBody);
				JsonNode likes = element.path("data");
				
				StringBuilder returnValue = new StringBuilder();
				
				List<String> values = likes.findValuesAsText("name");
				for(String value : values){
					returnValue.append(value.toLowerCase());
					returnValue.append(",");
				}
				returnValue.append("Arts and Entertainment");
				returnValue.append(",");
				
				return returnValue.toString();
		        
			} else {
				return null;
			}
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			if (mgr != null)
				mgr.closeIdleConnections(0);
			mgr = null;
		}
	}
	@Autowired
	public void setAuthDao(AuthDao authDao) {
		this.authDao = authDao;
	}
	@Autowired
	@Qualifier("userServiceNew")
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
