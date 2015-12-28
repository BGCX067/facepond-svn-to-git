package com.soa.facepond.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;

import com.soa.facepond.service.RemoteService;

@Component("remoteService")
public class RemoteServiceImpl implements RemoteService {
	private static final Log LOG = LogFactory.getLog(RemoteServiceImpl.class);
	private HttpClient httpClient;
	private String endpoint = null;

	protected static final String CONTENT_TYPE_JSON = "application/json";
	
	public RemoteServiceImpl(){}

	public RemoteServiceImpl(String endpoint) {
		super();

		HttpConnectionManager mgr = new MultiThreadedHttpConnectionManager();

		httpClient = new HttpClient(mgr);
		this.endpoint = endpoint;
	}
	
	public Map<String, String> postMethod(String method,
			Map<String, String> params, Header header) throws Exception {
		StringBuffer buf = new StringBuffer(getUrl(method));
		
		//System.out.println(buf.toString());

		PostMethod m = new PostMethod(buf.toString());
		if(header != null){
			m.setRequestHeader(header);
		}

		for (Map.Entry<String, String> param : params.entrySet()) {
			if (param.getValue() == null) {
				System.out.println("value is null! for :" + param.getKey());
				continue;
			}

			m.addParameter(param.getKey(), param.getValue());
		}

		m.addRequestHeader("Accept", CONTENT_TYPE_JSON);

		try {
			Map<String, String> result = new HashMap<String, String>();
			int returnCode = httpClient.executeMethod(m);

			String responseBody = new String(m.getResponseBody());
			//LOG.error("DEBUG response is \n" + responseBody);
			if (returnCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				String message = m.getStatusText();
				throw new Exception(message);
			}
			result.put("returnCode", returnCode + "");
			result.put("responseBody", responseBody);
			result.put("message", m.getStatusText());

			return result;

		} catch (HttpException e) {
			throw new DataRetrievalFailureException(
					"Connecting to remote service failed", e);
		} catch (IOException e) {
			throw new DataRetrievalFailureException(
					"Connecting to remote service failed", e);
		}
	}

	public Map<String, String> getMethod(String method,
			Map<String, String> params, Header header) throws Exception {
		StringBuffer buf = new StringBuffer(getUrl(method));

		for (Map.Entry<String, String> param : params.entrySet()) {
			buf.append(param.getKey()).append("=").append(param.getValue());
			buf.append("&");
		}
		
		//System.out.println(buf.toString());

		GetMethod m = new GetMethod(buf.toString());
		if(header != null){
			m.setRequestHeader(header);
		}
		m.addRequestHeader("Accept", CONTENT_TYPE_JSON);
		m.setRequestHeader("Authorization", "Atmosphere atmosphere_app_id=atmosphere-52vGCyzZCXu5Lqhyw1ofd39o");
		try {
			Map<String, String> result = new HashMap<String, String>();
			int returnCode = httpClient.executeMethod(m);

			String responseBody = new String(m.getResponseBody());
			//LOG.error("DEBUG response is \n" + responseBody);

			if (returnCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				String message = m.getStatusText();
				throw new Exception(message);
			}

			result.put("returnCode", returnCode + "");
			result.put("responseBody", responseBody);
			result.put("message", m.getStatusText());
			return result;
		} catch (HttpException e) {
			throw new DataRetrievalFailureException(
					"Connecting to remote service failed", e);
		} catch (IOException e) {
			throw new DataRetrievalFailureException(
					"Connecting to remote service failed", e);
		}
	}

	private String getUrl(String method) {
		return this.endpoint + method;
	}

}
