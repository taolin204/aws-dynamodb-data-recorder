package com.mwi.aws.dynamodb.model;

public class User {

	private String username;
	private String password;
	
	public Object getKey() {
		return username;
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
	
	
}
