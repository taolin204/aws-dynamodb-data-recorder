package com.mwi.aws.dynamodb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fluttercode.datafactory.impl.DataFactory;

import com.mwi.aws.dynamodb.model.Car;

public class CarService {

private List<Object> carList;
	
	
	public List<Object> getCars() {
		
		carList = new ArrayList<>();
		DataFactory dataFactory = new DataFactory();
		
		for (int i = 1; i < 20; i++) {
			Car car = new Car();
			car.setCarNum(dataFactory.getRandomWord());
			car.setCarOwner(dataFactory.getName());
			
			Map priceMap = new HashMap();
			priceMap.put("1S", 100);
			priceMap.put("2S", 200);
			priceMap.put("3S", 300);
			car.setPriceMap(priceMap);

			carList.add(car);
		}
		return carList;
	}
	
}
