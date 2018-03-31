package com.mwi.aws.dynamodb.ui;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ColumnModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String key;
	private String value;
	private String header;
	private String type;
	private Long mapDataTypeId;
	
	public ColumnModel() {}
	

	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Long getMapDataTypeId() {
		return mapDataTypeId;
	}


	public void setMapDataTypeId(Long mapDataTypeId) {
		this.mapDataTypeId = mapDataTypeId;
	}


	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
}