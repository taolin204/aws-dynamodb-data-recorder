package com.mwi.aws.dynamodb.model;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Car implements Serializable {

	private String carNum;
	private String carOwner;
	private Map<String, ModelPrice> priceMap;
	
	public Object getKey() {
		return carNum;
	}
	
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
	public void setPriceMap(Map<String, ModelPrice> price) {
		this.priceMap = price;
	}
	
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
	
}
