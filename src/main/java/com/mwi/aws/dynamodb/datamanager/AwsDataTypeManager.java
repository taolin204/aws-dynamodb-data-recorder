package com.mwi.aws.dynamodb.datamanager;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mwi.aws.dynamodb.AppConstants;
import com.mwi.aws.dynamodb.model.AwsDataType;
import com.mwi.aws.dynamodb.model.AwsDataTypeStore;
import com.mwi.aws.dynamodb.ui.ColumnModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

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
		
//		AwsDataType awsDataType = new AwsDataType();
//		awsDataType.setDataTypeId(1L);
//		awsDataType.setDataClass("com.mwi.aws.dynamodb.model.OwnerConfig");
//		
//		List columns = new ArrayList<ColumnModel>();		
//        ColumnModel columnDescriptor = new ColumnModel();
//        
//        columnDescriptor.setKey("");
//        columnDescriptor.setValue("owner");         
//        columnDescriptor.setHeader("Owner");        
//        columnDescriptor.setType("String");        
//        columns.add(columnDescriptor);
//        
//        columnDescriptor = new ColumnModel();
//        columnDescriptor.setKey("");
//        columnDescriptor.setValue("address");         
//        columnDescriptor.setHeader("Address");        
//        columnDescriptor.setType("String");        
//        columns.add(columnDescriptor);
//		
//
//        columnDescriptor = new ColumnModel();
//        columnDescriptor.setKey("owner");
//        columnDescriptor.setValue("contacts");         
//        columnDescriptor.setHeader("contacts");        
//        columnDescriptor.setType("Map");
//        columnDescriptor.setMapDataTypeId(2L);
//        columns.add(columnDescriptor);
//        
//        awsDataType.setColumnModels(columns);
//        awsDataMap.put(awsDataType.getDataTypeId(), awsDataType);
//        
//        //code to generate contact class xml definition.
//        awsDataType = new AwsDataType();
//		awsDataType.setDataTypeId(2L);
//		awsDataType.setDataClass("com.mwi.aws.dynamodb.model.Contact");
//		
//		columns = new ArrayList<ColumnModel>();
//		
//		columnDescriptor = new ColumnModel();	
//		columnDescriptor.setKey("");
//		columnDescriptor.setValue("contactName");         
//        columnDescriptor.setHeader("ContactName");        
//        columnDescriptor.setType("String");        
//        columns.add(columnDescriptor);
//        
//        columnDescriptor = new ColumnModel();
//        columnDescriptor.setKey("");
//        columnDescriptor.setValue("email");         
//        columnDescriptor.setHeader("Email");        
//        columnDescriptor.setType("String");        
//        columns.add(columnDescriptor);
//		
//        columnDescriptor = new ColumnModel();
//        columnDescriptor.setKey("");
//        columnDescriptor.setValue("phoneNumber");         
//        columnDescriptor.setHeader("PhoneNumber");        
//        columnDescriptor.setType("String");        
//        columns.add(columnDescriptor);
//        
//        awsDataType.setColumnModels(columns);
		
		try {
			
			XStream xstream = new XStream(new DomDriver("utf8"));
	        Class<?>[] classes = new Class[] { AwsDataType.class, ArrayList.class, ColumnModel.class, AwsDataTypeStore.class};
	        XStream.setupDefaultSecurity(xstream);
	        //XStream.setupDefaultSecurity(xstream);
	        xstream.allowTypes(classes);
	        xstream.alias("AwsDataTypeStore", AwsDataTypeStore.class);
			xstream.alias("AwsData", AwsDataType.class);
			xstream.alias("Column", ColumnModel.class);
			
			//Object obj = Class.forName("com.mwi.aws.dynamodb.model.AwsDataType").newInstance();
			AwsDataTypeStore data = new AwsDataTypeStore(); 
			InputStream in = this.getClass().getResourceAsStream("/" + AppConstants.DATA_TYPE_FILE);

			xstream.fromXML(in, data);
			System.out.println("===================================");
			for(int i=0; i<data.getAwsDataTypes().size(); i++) {
				AwsDataType data1= data.getAwsDataTypes().get(i);
				System.out.println(data1.toString());
				awsDataMap.put(data1.getDataTypeId(), data1);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("exception creating datatypes from xml file");
			e.printStackTrace();
		}
		
        
	}

	public Map<Long, AwsDataType> getAwsDataMap() {
		return awsDataMap;
	}
	
	
	
}
