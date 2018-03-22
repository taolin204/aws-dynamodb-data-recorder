package com.mwi.aws.dynamodb.model;

import java.util.Map;

public class Car {

	private String carNum;
	private String carOwner;
	private Map priceMap;
	
	public String getCarNum() {
		return carNum;
	}
	public void setCarNum(String carNum) {
		this.carNum = carNum;
	}
	public String getCarOwner() {
		return carOwner;
	}
	public void setCarOwner(String carOwner) {
		this.carOwner = carOwner;
	}
	public Map getPriceMap() {
		return priceMap;
	}
	public void setPriceMap(Map price) {
		this.priceMap = price;
	}
	
	
}
