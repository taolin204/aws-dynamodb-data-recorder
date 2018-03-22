package com.mwi.aws.dynamodb.ui;

import java.io.Serializable;

public class ColumnModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String property;
	private String header;
	private String type;
	
	public ColumnModel() {}
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
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
}