package com.mwi.aws.dynamodb.datamanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mwi.aws.dynamodb.model.AwsDataType;
import com.mwi.aws.dynamodb.ui.ColumnModel;

public class AwsDataTypeManager {

	
	private static AwsDataTypeManager instance;
	private Map<Long, AwsDataType> awsDataMap = new HashMap();
	
	public static AwsDataTypeManager getInstance() {
		if(instance == null) {
			instance = new AwsDataTypeManager();
		}
		return instance;
	}
	
	public AwsDataTypeManager() {
		createDataTypeMapFromXML();
	}
	
	
	public void createDataTypeMapFromXML() {
		AwsDataType awsDataType = new AwsDataType();
		awsDataType.setDataTypeId(1L);
		awsDataType.setDataClass("com.mwi.aws.dynamodb.model.OwnerConfig");
		
		List columns = new ArrayList<ColumnModel>();		
        ColumnModel columnDescriptor = new ColumnModel();
        
        columnDescriptor.setKey("");
        columnDescriptor.setValue("owner");         
        columnDescriptor.setHeader("Owner");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();
        columnDescriptor.setKey("");
        columnDescriptor.setValue("address");         
        columnDescriptor.setHeader("Address");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
		

        columnDescriptor = new ColumnModel();
        columnDescriptor.setKey("owner");
        columnDescriptor.setValue("contacts");         
        columnDescriptor.setHeader("contacts");        
        columnDescriptor.setType("Map");
        columnDescriptor.setMapDataTypeId(2L);
        columns.add(columnDescriptor);
        
        awsDataType.setColumnModels(columns);
        awsDataMap.put(awsDataType.getDataTypeId(), awsDataType);
        
        //code to generate contact class xml definition.
        awsDataType = new AwsDataType();
		awsDataType.setDataTypeId(2L);
		awsDataType.setDataClass("com.mwi.aws.dynamodb.model.Contact");
		
		columns = new ArrayList<ColumnModel>();
		
		columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("contactName");         
        columnDescriptor.setHeader("ContactName");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();
        columnDescriptor.setKey("");
        columnDescriptor.setValue("email");         
        columnDescriptor.setHeader("Email");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
		
        columnDescriptor = new ColumnModel();
        columnDescriptor.setKey("");
        columnDescriptor.setValue("phoneNumber");         
        columnDescriptor.setHeader("PhoneNumber");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        awsDataType.setColumnModels(columns);
        awsDataMap.put(awsDataType.getDataTypeId(), awsDataType);
	}

	public Map<Long, AwsDataType> getAwsDataMap() {
		return awsDataMap;
	}
	
	
	
}
