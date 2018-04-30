package com.mwi.aws.dynamodb.model;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.mwi.aws.dynamodb.ui.ColumnModel;

public class AwsDataType {

	private Long dataTypeId;
	private String dataClass;
	private Long panelSize;
	private List<ColumnModel> columnModels;
	
	public Long getDataTypeId() {
		return dataTypeId;
	}
	public void setDataTypeId(Long dataTypeId) {
		this.dataTypeId = dataTypeId;
	}
	public String getDataClass() {
		return dataClass;
	}
	public void setDataClass(String dataClass) {
		this.dataClass = dataClass;
	}
	public List<ColumnModel> getColumnModels() {
		return columnModels;
	}
	public void setColumnModels(List<ColumnModel> columnModels) {
		this.columnModels = columnModels;
	}
	
	public Long getPanelSize() {
		return panelSize;
	}
	public void setPanelSize(Long panelSize) {
		this.panelSize = panelSize;
	}
	@Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
	
	
}
