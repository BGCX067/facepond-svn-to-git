package com.soa.facepond;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("authDao")
public class AuthDao {
	private ConnectionFactory connectionFactory;
	
	private static final String QUERY_GET_AUTH_PROPERTY = "select name, value from auth_properties where name=?";
	public String getAuthProperty(String name) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_GET_AUTH_PROPERTY);
			ps.setString(1, name);
			
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getString("value");
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			con.close();
		}
		return null;
	}
	
	private static final String QUERY_LOAD_AUTH_PROPERTIES = "select name, value from auth_properties";
	public Map<String, String> loadAuthProperties() throws Exception {
		Map<String, String> authProperties = new HashMap<String, String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_LOAD_AUTH_PROPERTIES);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				authProperties.put(rs.getString("name"), rs.getString("value"));
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			con.close();
		}
		return authProperties;
		
	}

	@Autowired
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}
