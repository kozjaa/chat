package model;

import java.util.List;

public class User {
	private String username;
	private String password;
	private Boolean isActive;
	private List<String> notifications;
	
	public User() {
	}

	public User(String username, String password, Boolean isActive) {
		this.username = username;
		this.password = password;
		this.isActive = isActive;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public List<String> getNotifications() {
		return notifications;
	}

	public void setNotifications(List<String> notifications) {
		this.notifications = notifications;
	}
	
	
}
