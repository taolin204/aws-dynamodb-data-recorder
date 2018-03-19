package com.logicbig.example;

import java.util.ArrayList;
import java.util.List;

import org.fluttercode.datafactory.impl.DataFactory;

public class UserService {

	private List<Object> userList;
	
	
	public List<Object> getUsers() {
		
		userList = new ArrayList<>();
		DataFactory dataFactory = new DataFactory();
		
		for (int i = 1; i < 20; i++) {
			User user = new User();
			user.setUsername(dataFactory.getName());
			user.setPassword(dataFactory.getName());
			userList.add(user);
		}
		return userList;
	}
}
