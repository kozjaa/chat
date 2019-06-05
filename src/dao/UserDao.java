package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import db.ConnectionFactory;
import model.User;

public class UserDao {
	
	public Set<User> getAllActiveUsers() {
	    Connection connection = ConnectionFactory.getConnection();
	    Set<User> users = new HashSet();
	    try {
	    	PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE isactive=true");
	        ResultSet rs = ps.executeQuery();
	        
	        while(rs.next()) {
	        	User user = new User();
	        	user.setIsActive(rs.getBoolean("isActive"));
	        	user.setPassword(rs.getString("password"));
	            user.setUsername(rs.getString("username"));
	            users.add(user);
	        }
	        rs.close();
	        ps.close();
	        connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }	    
	    return users;
	}

	public User getUserByUsername(String username) {
		Connection connection = ConnectionFactory.getConnection();
		User user = new User();
        try {
        	PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE username=?");
        	ps.setString(1, username);
        	ResultSet rs = ps.executeQuery();
        		
        	if(rs.next()) {
        		user.setUsername( rs.getString("username") );
        		user.setPassword( rs.getString("password") );
        		user.setIsActive( rs.getBoolean("isactive") );
        		rs.close();
       			ps.close();
       	        connection.close();
       		}
       	} catch (SQLException ex) {
       		ex.printStackTrace();
       	}
       	return user;
    }
	
	public void insertUser(User user) {
	    Connection connection = ConnectionFactory.getConnection();
	    try {
	        PreparedStatement ps = connection.prepareStatement("INSERT INTO users (username, password, isactive) VALUES (?, ?, ?)");
	        ps.setString(1, user.getUsername());
	        ps.setString(2, user.getPassword());
	        ps.setBoolean(3, user.getIsActive());
	        ps.executeUpdate();
	    	ps.close();
		    connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	public void setActiveUserStatus(String username, Boolean isActive) {
	    Connection connection = ConnectionFactory.getConnection();
	    try {
	        PreparedStatement ps = connection.prepareStatement("UPDATE users SET isactive=? WHERE username=?");
	        ps.setBoolean(1, isActive);
	        ps.setString(2, username);
	        ps.executeUpdate();
	    	ps.close();
		    connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
	
	public User getUserByUserNameAndPassword(String username, String password) {
	    Connection connection = ConnectionFactory.getConnection();
	    User user = new User();
	    try {
	        PreparedStatement ps = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?");
	        ps.setString(1, username);
	        ps.setString(2, password);
	        ResultSet rs = ps.executeQuery();
	        if(rs.next()) {
	        	user.setUsername(rs.getString("username"));
	        	user.setPassword(rs.getString("password"));
	        	ps.close();
	        	rs.close();
		        connection.close();	        	
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return user;
	}

}
