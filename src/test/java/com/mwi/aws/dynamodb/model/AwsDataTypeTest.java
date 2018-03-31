package com.mwi.aws.dynamodb.model;

import java.util.ArrayList;
import java.util.List;

import com.mwi.aws.dynamodb.ui.ColumnModel;
import com.thoughtworks.xstream.XStream;

public class AwsDataTypeTest {

	public static void main(String[] args) {
		List list = new ArrayList();
		
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
        columnDescriptor.setKey("contactName");
        columnDescriptor.setValue("contacts");         
        columnDescriptor.setHeader("contacts");        
        columnDescriptor.setType("Map");        
        columns.add(columnDescriptor);
        
        awsDataType.setColumnModels(columns);
        list.add(awsDataType);
        
        //code to generate contact class xml definition.
        awsDataType = new AwsDataType();
		awsDataType.setDataTypeId(2L);
		awsDataType.setDataClass("com.mwi.aws.dynamodb.model.Contact");
		
		columns = new ArrayList<ColumnModel>();
		
		columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("contactNumber");         
        columnDescriptor.setHeader("ContactNumber");        
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
        list.add(awsDataType);
        
        System.out.println("list size " + list.size());
        
        XStream xstream = new XStream();
        xstream.alias("AwsDataType", List.class);
		xstream.alias("AwsData", AwsDataType.class);
		xstream.alias("Column", ColumnModel.class);
		
		String xml = xstream.toXML(list);
		System.out.println(xml);
		
		String dataType = "list size 2\r\n" + 
				"<AwsDataType>\r\n" + 
				"  <AwsData>\r\n" + 
				"    <dataTypeId>1</dataTypeId>\r\n" + 
				"    <dataClass>com.mwi.aws.dynamodb.model.OwnerConfig</dataClass>\r\n" + 
				"    <columnModels>\r\n" + 
				"      <Column>\r\n" + 
				"        <key></key>\r\n" + 
				"        <value>owner</value>\r\n" + 
				"        <header>Owner</header>\r\n" + 
				"        <type>String</type>\r\n" + 
				"      </Column>\r\n" + 
				"      <Column>\r\n" + 
				"        <key></key>\r\n" + 
				"        <value>address</value>\r\n" + 
				"        <header>Address</header>\r\n" + 
				"        <type>String</type>\r\n" + 
				"      </Column>\r\n" + 
				"      <Column>\r\n" + 
				"        <key>contactName</key>\r\n" + 
				"        <value>contacts</value>\r\n" + 
				"        <header>contacts</header>\r\n" + 
				"        <type>Map</type>\r\n" + 
				"      </Column>\r\n" + 
				"    </columnModels>\r\n" + 
				"  </AwsData>\r\n" + 
				"  <AwsData>\r\n" + 
				"    <dataTypeId>2</dataTypeId>\r\n" + 
				"    <dataClass>com.mwi.aws.dynamodb.model.Contact</dataClass>\r\n" + 
				"    <columnModels>\r\n" + 
				"      <Column>\r\n" + 
				"        <key></key>\r\n" + 
				"        <value>contactNumber</value>\r\n" + 
				"        <header>ContactNumber</header>\r\n" + 
				"        <type>String</type>\r\n" + 
				"      </Column>\r\n" + 
				"      <Column>\r\n" + 
				"        <key></key>\r\n" + 
				"        <value>email</value>\r\n" + 
				"        <header>Email</header>\r\n" + 
				"        <type>String</type>\r\n" + 
				"      </Column>\r\n" + 
				"      <Column>\r\n" + 
				"        <key></key>\r\n" + 
				"        <value>phoneNumber</value>\r\n" + 
				"        <header>PhoneNumber</header>\r\n" + 
				"        <type>String</type>\r\n" + 
				"      </Column>\r\n" + 
				"    </columnModels>\r\n" + 
				"  </AwsData>\r\n" + 
				"</AwsDataType>\r\n" + 
				"";
		
		try {
			Object obj = Class.forName("com.mwi.aws.dynamodb.model.AwsDataType").newInstance();
			xstream.fromXML(dataType, obj);
			System.out.println(obj.toString());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
