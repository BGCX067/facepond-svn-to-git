package com.soa.facepond;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service("connectionFactory")
public class ConnectionFactory {
	public Connection getConnection() throws Exception {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/facepond", "root", "password");
		} catch(SQLException sqle) {
			throw sqle;
		}
        return conn;
	}
}
