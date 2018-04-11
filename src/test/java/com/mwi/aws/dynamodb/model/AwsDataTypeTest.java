package com.mwi.aws.dynamodb.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mwi.aws.dynamodb.AppConstants;
import com.mwi.aws.dynamodb.ui.ColumnModel;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AwsDataTypeTest {

	public static void main(String[] args) {
		AwsDataTypeStore awsDataTypes = new AwsDataTypeStore();
		
		AwsDataType awsDataType = new AwsDataType();
		awsDataType.setDataTypeId(1L);
		awsDataType.setDataClass("com.mfg.OwnerConfig");
		
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
        awsDataTypes.getAwsDataTypes().add(awsDataType);
        
        //code to generate contact class xml definition.
        awsDataType = new AwsDataType();
		awsDataType.setDataTypeId(2L);
		awsDataType.setDataClass("com.mfg.Contact");
		
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
        awsDataTypes.getAwsDataTypes().add(awsDataType);
        
        
        awsDataType = new AwsDataType();
		awsDataType.setDataTypeId(2L);
		awsDataType.setDataClass("com.mfg.SensorConfig");
        
		columns = new ArrayList<ColumnModel>();
		
		columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("address");         
        columnDescriptor.setHeader("Address");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("alias");         
        columnDescriptor.setHeader("Alias");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("alias");         
        columnDescriptor.setHeader("Alias");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("enablePoll");         
        columnDescriptor.setHeader("EnablePoll");        
        columnDescriptor.setType("Boolean");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("installationDate");         
        columnDescriptor.setHeader("InstallationDate");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("installedBy");         
        columnDescriptor.setHeader("InstalledBy");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("location");         
        columnDescriptor.setHeader("Location");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("maintContract");         
        columnDescriptor.setHeader("MaintContract");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("maintStartDate");         
        columnDescriptor.setHeader("MaintStartDate");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("maintEndDate");         
        columnDescriptor.setHeader("MaintEndDate");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("manufacturer");         
        columnDescriptor.setHeader("Manufacturer");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("model");         
        columnDescriptor.setHeader("Model");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("owner");         
        columnDescriptor.setHeader("Owner");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("pollInterval");         
        columnDescriptor.setHeader("PollInterval");        
        columnDescriptor.setType("Long");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("sensorId");         
        columnDescriptor.setHeader("SensorId");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("serialNumber");         
        columnDescriptor.setHeader("SerialNumber");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("softwareInstalled");         
        columnDescriptor.setHeader("SoftwareInstalled");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("softwareLicense");         
        columnDescriptor.setHeader("SoftwareLicense");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("type");         
        columnDescriptor.setHeader("Type");        
        columnDescriptor.setType("String");        
        columns.add(columnDescriptor);
        
        columnDescriptor = new ColumnModel();	
		columnDescriptor.setKey("");
		columnDescriptor.setValue("contactNames");         
        columnDescriptor.setHeader("ContactNames");        
        columnDescriptor.setType("ListString");        
        columns.add(columnDescriptor);
  
        awsDataType.setColumnModels(columns);
        awsDataTypes.getAwsDataTypes().add(awsDataType);
    	
        System.out.println("list size " + awsDataTypes.getAwsDataTypes().size());
        
        XStream xstream = new XStream(new DomDriver("utf8"));
        Class<?>[] classes = new Class[] { AwsDataType.class, ArrayList.class, ColumnModel.class, AwsDataTypeStore.class};
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        xstream.alias("AwsDataTypeStore", AwsDataTypeStore.class);
		xstream.alias("AwsData", AwsDataType.class);
		xstream.alias("Column", ColumnModel.class);
		
		String xml = xstream.toXML(awsDataTypes);
		System.out.println(xml);
		
		String xmlString = "<AwsDataType>\r\n" + 
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
				"</AwsDataType>";
		
		try {
			//Object obj = Class.forName("com.mwi.aws.dynamodb.model.AwsDataType").newInstance();
			AwsDataTypeStore data = new AwsDataTypeStore(); 
			File file = new File(AppConstants.DATA_TYPE_FILE);
			if(file.exists()) {
				//XStream.setupDefaultSecurity(xstream);
		        xstream.allowTypes(classes);
		        xstream.alias("AwsDataTypeStore", AwsDataTypeStore.class);
				xstream.alias("AwsData", AwsDataType.class);
				xstream.alias("Column", ColumnModel.class);
				xstream.fromXML(file, data);
				System.out.println("===================================");
				for(int i=0; i<data.getAwsDataTypes().size(); i++) {
					AwsDataType data1= data.getAwsDataTypes().get(i);
					System.out.println(data1.toString());
				}
			} else {
				System.out.println("file is not exists");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
