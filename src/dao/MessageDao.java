package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import db.ConnectionFactory;
import model.Message;

public class MessageDao {
	
	public List<Message> getAllChatroomMesages(){
		Connection connection = ConnectionFactory.getConnection();
		List<Message> allMessages = new ArrayList();
	    try {
	        PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages WHERE receiver IS NULL");
	        ResultSet rs = ps.executeQuery();
	        
	        while(rs.next()) {
	        	Message message = new Message();
	        	message.setFrom(rs.getString("sender"));
	        	message.setSentAt(rs.getString("sentAt"));
	        	message.setContent(rs.getString("content"));
	        	allMessages.add(message);
	        }
	        rs.close();
	        ps.close();
	        connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }

	    Collections.reverse(allMessages);
	    return allMessages;
	}
	
	public List<Message> getAllConverstationMessages(String sender, String receiver){
		Connection connection = ConnectionFactory.getConnection();
		List<Message> allMessages = new ArrayList();
	    try {
	    	PreparedStatement ps = connection.prepareStatement("SELECT * FROM messages WHERE (sender=? AND receiver=?) OR (sender=? AND receiver=?)");
	        ps.setString(1, sender);
	        ps.setString(2, receiver);
	        ps.setString(3, receiver);
	        ps.setString(4, sender);
	    	ResultSet rs = ps.executeQuery();
	        
	        while(rs.next()) {
	        	Message message = new Message();
	        	message.setFrom(rs.getString("sender"));
	        	message.setTo(rs.getString("receiver"));
	        	message.setContent(rs.getString("content"));
	        	message.setSentAt(rs.getString("sentat"));
	        	allMessages.add(message);
	        }
	        rs.close();
	        ps.close();
	        connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    Collections.reverse(allMessages);
	    return allMessages;
	}
	
	public void insertMessage(Message message) {
		Connection connection = ConnectionFactory.getConnection();
	    try {
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
	        LocalDateTime dateTime = LocalDateTime.now();
	        String date = dateTime.format(formatter);
	        PreparedStatement ps = connection.prepareStatement("INSERT INTO messages (content, sender, receiver, sentat) VALUES (?, ?, ?, ?)");
	        ps.setString(1, message.getContent());
	        ps.setString(2, message.getFrom());
	        ps.setString(3, message.getTo());
	        ps.setString(4, date);
	        ps.executeUpdate();
	    	ps.close();
		    connection.close();
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}
}
