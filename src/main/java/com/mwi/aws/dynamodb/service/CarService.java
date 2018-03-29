package com.mwi.aws.dynamodb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fluttercode.datafactory.impl.DataFactory;

import com.mwi.aws.dynamodb.model.Car;
import com.mwi.aws.dynamodb.model.ModelPrice;

public class CarService {

private List<Object> carList;
	
	
	public List<Object> getCars() {
		
		carList = new ArrayList<>();
		DataFactory dataFactory = new DataFactory();
		
		for (int i = 1; i < 3; i++) {
			Car car = new Car();
			//car.setKey(dataFactory.getRandomWord());
			car.setCarNum(dataFactory.getRandomWord());
			car.setCarOwner(dataFactory.getName());
			
			Map priceMap = new HashMap();
			ModelPrice modelPrice = new ModelPrice();
			modelPrice.setManuYear("2011");
			modelPrice.setModel("BMW");
			modelPrice.setPrice(100L);
			priceMap.put("1S", modelPrice);
			
			modelPrice = new ModelPrice();
			modelPrice.setManuYear("2012");
			modelPrice.setModel("AUDI");
			modelPrice.setPrice(200L);
			priceMap.put("2S", modelPrice);
			
			modelPrice = new ModelPrice();
			modelPrice.setManuYear("2013");
			modelPrice.setModel("BENZ");
			modelPrice.setPrice(300L);
			priceMap.put("3S", modelPrice);
			
			car.setPriceMap(priceMap);

			carList.add(car);
		}
		return carList;
	}
	
}
