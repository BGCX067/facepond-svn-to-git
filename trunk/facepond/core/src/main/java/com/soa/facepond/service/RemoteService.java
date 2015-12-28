package com.soa.facepond.service;

import java.util.Map;

import org.apache.commons.httpclient.Header;

public interface RemoteService {

	public Map<String, String> postMethod(String method,
			Map<String, String> params, Header header) throws Exception;

	public Map<String, String> getMethod(String method,
			Map<String, String> params, Header header) throws Exception;
}
