package com.soa.facepond;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.soa.facepond.model.User;

@Service("userDaoNew")
public class UserDao implements IUserDao {
	private ConnectionFactory connectionFactory;

	public boolean isNewUser(String authId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement("select auth_id from user where auth_id=?");
			ps.setString(1, authId);

			rs = ps.executeQuery();
			if (rs.next()) {
				return false;
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
		return true;
	}

	private static final String QUERY_ADD_USER = "insert into user (id, auth_id, first_name, last_name, email, city, state, frequency, access_code, last_updated, division_id, frequency_type) "
			+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, current_timestamp, ?, ?)";

	public void addUser(User user) throws Exception {
		PreparedStatement ps = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_ADD_USER);
			ps.setLong(1, user.getId());
			ps.setString(2, user.getAuthId());
			ps.setString(3, user.getFirstName());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getEmail());
			ps.setString(6, user.getCity());
			ps.setString(7, user.getState());
			ps.setString(8, user.getFrequency());
			ps.setString(9, user.getAccessCode());
			// ps.setString(10, user.getLattitude());
			// ps.setString(11, user.getLongitude());
			// ps.setDate(12, new java.sql.Date(System.currentTimeMillis()));
			ps.setString(10, user.getDivisionId());
			ps.setInt(11, user.getFrequencyType().ordinal());

			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			con.close();
		}
	}

	private static final String QUERY_UPDATE_USER = "update user set frequency=?, city=?, division_id=? where id=?";

	public void updateUser(User user) throws Exception {
		PreparedStatement ps = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_ADD_USER);

			ps.setString(1, user.getFrequency());
			ps.setString(2, user.getCity());
			ps.setString(3, user.getDivisionId());
			ps.setLong(4, user.getId());

			ps.executeUpdate();
		} finally {
			if (ps != null) {
				ps.close();
			}
			con.close();
		}
	}

	private static final String QUERY_LOAD_CITY_DIVISIONS = " select city, division_id from city_division";

	public Map<String, String> loadCityDivisions() throws Exception {
		Map<String, String> cityDivisionMap = new HashMap<String, String>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_LOAD_CITY_DIVISIONS);

			rs = ps.executeQuery();
			while (rs.next()) {
				cityDivisionMap.put(rs.getString("city"),
						rs.getString("division_id"));
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
		return cityDivisionMap;
	}

	private static final String QUERY_GET_USER_BY_ID = "select id, auth_id, first_name, last_name, email, city, state, frequency, access_code, last_updated, division_id from user where id=?";

	public User getUserById(long id) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_GET_USER_BY_ID);
			ps.setLong(1, id);
			rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setAuthId(rs.getString("auth_id"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmail(rs.getString("email"));
				user.setCity(rs.getString("city"));
				user.setState(rs.getString("state"));
				user.setFrequency(rs.getString("frequency"));
				user.setAccessCode(rs.getString("access_code"));
				user.setDivisionId(rs.getString("division_id"));

				return user;
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

	private static final String QUERY_GET_USER_BY_AUTH_ID = "select id, auth_id, first_name, last_name, email, city, state, frequency, access_code, last_updated, division_id from user where auth_id=?";

	public User getUserByAuthId(String authId) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_GET_USER_BY_AUTH_ID);
			ps.setString(1, authId);
			rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setId(rs.getLong("id"));
				user.setAuthId(rs.getString("auth_id"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastName(rs.getString("last_name"));
				user.setEmail(rs.getString("email"));
				user.setCity(rs.getString("city"));
				user.setState(rs.getString("state"));
				user.setFrequency(rs.getString("frequency"));
				user.setAccessCode(rs.getString("access_code"));
				user.setDivisionId(rs.getString("division_id"));

				return user;
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

	private static final String QUERY_DELETE_DEALS_FOR_USER = "delete from user_deals where userId=?";
	public void removeDealsByUser(long userId) throws Exception {
		PreparedStatement ps = null;
		Connection con = connectionFactory.getConnection();
		try {
			ps = con.prepareStatement(QUERY_DELETE_DEALS_FOR_USER);
			ps.setLong(1, userId);
			
			ps.executeUpdate();
			
		} finally {
			if (ps != null) {
				ps.close();
			}
			con.close();
		}
	}
	@Autowired
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
}
