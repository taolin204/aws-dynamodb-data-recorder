package com.logicbig.example;

import java.util.ArrayList;
import java.util.List;

import org.fluttercode.datafactory.impl.DataFactory;

public class CarService {

private List<Object> carList;
	
	
	public List<Object> getCars() {
		
		carList = new ArrayList<>();
		DataFactory dataFactory = new DataFactory();
		
		for (int i = 1; i < 20; i++) {
			Car car = new Car();
			car.setCarNum(dataFactory.getRandomWord());
			car.setCarOwner(dataFactory.getName());
			car.setPrice(1);
			carList.add(car);
		}
		return carList;
	}
	
}
