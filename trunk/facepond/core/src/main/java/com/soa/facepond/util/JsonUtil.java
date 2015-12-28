package com.soa.facepond.util;

/**
 *  SOA Software, Inc. Copyright (C) 2000-2011, All rights reserved
 *
 *  This  software is the confidential and proprietary information of SOA Software, Inc.
 *  and is subject to copyright protection under laws of the United States of America and
 *  other countries. The  use of this software should be in accordance with the license
 *  agreement terms you entered into with SOA Software, Inc.
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class JsonUtil {

    // ----------- Private static members --------
	private static final Log log = LogFactory.getLog(JsonUtil.class);

    private static JsonFactory jf = new JsonFactory();                 // JSON factory

    // ------------ Public static methods ----------------------


    /**
     * Convert JackSon string to Map<String, String>[].
     *
     * @param str - Jackson string
     * @return Map<String, String>[]
     */
    @SuppressWarnings("unchecked")
	public static List<Map<String, String>> getListFromJsonArray(String str) {
        try {
            if (str != null && str.length() > 0) {
                ArrayList<Map<String, String>> arrList = (ArrayList<Map<String, String>>) new ObjectMapper()
                        .readValue(jf.createJsonParser(new StringReader(str)), List.class);
                return arrList;
            } else {
                log.warn("JacksonUtil.getListsFromJsonArray error| ErrMsg: input string is null ");
                return null;
            }
        } catch (Exception e) {
            log.error("JacksonUtil.getListsFromJsonArray error| ErrMsg: " + e.getMessage());
            return null;
        }

    }

	private static List<String> getStrListFromJsonArray(String str) {
        try {
            if (str != null && str.length() > 0) {
                List<String> arrList = (ArrayList<String>) new ObjectMapper()
                        .readValue(jf.createJsonParser(new StringReader(str)), List.class);
                return arrList;
            } else {
                log.warn("AuthTokenManager.getListsFromJsonArray error| ErrMsg: input string is null ");
                return null;
            }
        } catch (Exception e) {
            log.error("AuthTokenManager.getListsFromJsonArray error| ErrMsg: " + e.getMessage());
            return null;
        }

    }

    /**
     * Convert JackSon string to Map<String, String>
     *
     * @param str - jackson string
     * @return Map<String, String>
     */
    @SuppressWarnings("unchecked")
	public static Map<String, String> getMapFromJsonString(String str) {
        try {
            if (str != null && str.length() > 0) {
                Map<String, String> map = (Map<String, String>) new ObjectMapper()
                        .readValue(jf.createJsonParser(new StringReader(str)), Map.class);
                return map;
            } else {
                log.warn("ErrMsg: input string is null ");
                return null;
            }
        } catch (Exception e) {
            log.error("ErrMsg: " + e.getMessage());
            return null;
        }
    }

    public static Object getObjectFromJson(String str, Class<?> clazz) {
        try {
            if (str != null && str.length() > 0) {
                Object obj =  new ObjectMapper()
                        .readValue(jf.createJsonParser(new StringReader(str)), clazz);
                return obj;
            } else {
                log.warn("JacksonUtil.getObjectFromJson error| ErrMsg: input string is null ");
                return null;
            }
        } catch (Exception e) {
            log.error("JacksonUtil.getObjectFromJson error| ErrMsg: " + e.getMessage());
            return null;
        }

    }

    /**
     * Convert Map<String, String>[] to JackSon string
     *
     * @param list Array of Map<String,String>
     * @return jackson string
     */
    public static String getJsonStringFromList(List<Map<String, String>> list) {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator gen = jf.createJsonGenerator(sw);
            new ObjectMapper().writeValue(gen, list);
            gen.flush();
            return sw.toString();
        } catch (Exception e) {
            log.error("JacksonUtil.getJsonStringFromMap error| ErrMsg: " + e.getMessage());
            return null;
        }
    }

    /**
     * Convert Map<String, String> to JackSon string
     *
     * @param aMap Map
     * @return Map<String, String>
     */
    public static String getJsonStringFromMap(Map<String, String> aMap) {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator gen = jf.createJsonGenerator(sw);
            new ObjectMapper().writeValue(gen, aMap);
            gen.flush();
            return sw.toString();
        } catch (Exception e) {
            log.error("ErrMsg: " + e.getMessage());
            return null;
        }
    }

    public static String getJsonStringFromObject(Object obj) {
        try {
            StringWriter sw = new StringWriter();
            JsonGenerator gen = jf.createJsonGenerator(sw);
            new ObjectMapper().writeValue(gen, obj);
            gen.flush();
            return sw.toString();
        } catch (Exception e) {
            log.error("JacksonUtil.getJsonStringFromMap error| ErrMsg: " + e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
    	List<String> list = JsonUtil.getStrListFromJsonArray("[\"app101.serve\", \"app102.serve\"]");
    	System.out.println(list.toString());
    }
}
