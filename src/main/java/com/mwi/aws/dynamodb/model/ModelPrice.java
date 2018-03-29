package com.mwi.aws.dynamodb.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ModelPrice {

	private String model;
	private String manuYear;
	private Long price;
	
	public Object getKey() {
		return model;
	}
	
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getManuYear() {
		return manuYear;
	}
	public void setManuYear(String manuYear) {
		this.manuYear = manuYear;
	}
	public Long getPrice() {
		return price;
	}
	public void setPrice(Long price) {
		this.price = price;
	}
	
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
}
