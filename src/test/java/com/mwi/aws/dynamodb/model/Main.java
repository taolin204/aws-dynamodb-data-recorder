package com.mwi.aws.dynamodb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.mfg.Contact;
import com.mfg.OwnerConfig;
import com.mfg.SensorConfig;
import com.mfg.UserConfig;
import com.mfg.UserPermissions;
import com.mwi.aws.dynamodb.service.AwsService;

public class Main {

	private static boolean USE_LOCAL_DB = true;
	
	private static AmazonDynamoDBClient client;
	private static DynamoDBMapper mapper;
	private static DynamoDB dynamoDB;
	
    // test
    public static void main(String[] args) {
    	initDb();

//    	testAddOwner();
//    	testAddUser();
    	testAddSensor();
//    	
//    	testReadOwner();
//    	testReadUser();
//    	testReadSensor();
//    	testGetSensorByOwner();
    }
    
    public static void initDb() {
    	// local DynamoDB
    	if (USE_LOCAL_DB) {
	    	// The secret key doesn't need to be valid, DynamoDB Local doesn't care.
	    	AWSCredentials credentials = new BasicAWSCredentials("AKIAJRRNA6EUMTW6YFGQ", "bogus");
	    	client = new AmazonDynamoDBClient(credentials);
	    	// Make sure you use the same port as you configured DynamoDB Local to bind to.
	    	client.setEndpoint("http://localhost:8111");
	    	// Sign requests for the "local" region to read data written by the toolkit.
	    	client.setSignerRegionOverride("local");
    	}
    	// AWS DynamoDB
    	else {
            ClientConfiguration clientConfig = new ClientConfiguration()
                    .withClientExecutionTimeout(15000) //set 15000ms timeout
                    .withRequestTimeout(15000)
                    .withConnectionTimeout(15000)
                    .withSocketTimeout(15000)
                    .withMaxErrorRetry(0); 
            client = new AmazonDynamoDBClient(clientConfig)
            		.withRegion(Regions.AP_SOUTHEAST_1);
    	}
    	
    	dynamoDB = new DynamoDB(client);
        mapper = new DynamoDBMapper(client);
    }
    
    public static void testAddOwner() {
		OwnerConfig ownerConfig = new OwnerConfig();
		ownerConfig.setOwner("Company AAA");
		ownerConfig.setAddress("BLK 123, #11-11, Singapore 345688");

		Map contacts = new HashMap();
		Contact contact = new Contact();
		contact.setContactName("Mr. Black");
		contact.setEmail("user2@abc.com.sg");
		contact.setPhoneNumber("93334567");
		contacts.put(contact.getContactName(), contact);

		contact = new Contact();
		contact.setContactName("Mr. Smith");
		contact.setEmail("user1@abc.com.sg");
		contact.setPhoneNumber("91234567");
		contacts.put(contact.getContactName(), contact);

		ownerConfig.setContacts(contacts);

		mapper.save(ownerConfig);
	   }
    
    public static void testReadOwner() {
	    OwnerConfig  ownerConfig = mapper.load(OwnerConfig.class, "Company AAA"); 
	    System.out.println("owner=" + ownerConfig.getOwner()
	    		+ ", addr=" + ownerConfig.getAddress());
    }
    
    public static void testAddUser() {
		UserConfig userConfig = new UserConfig();
		userConfig.setIdentityId("user2");
		userConfig.setPassword("password");
		userConfig.setUserName("user name 2");

		List<String> managedOwners = new ArrayList();
		managedOwners.add("Company AAA");
		managedOwners.add("Company BBB");
		userConfig.setManagedOwners(managedOwners);

		UserPermissions userPermissions = new UserPermissions();
		userPermissions.setCanActuateSensor(true);
		userPermissions.setCanConfigureOwner(true);
		userPermissions.setCanConfigureSensor(true);
		userPermissions.setCanConfigureUser(true);
		userPermissions.setCanReadSensor(true);
		userConfig.setUserPermissions(userPermissions);
		mapper.save(userConfig);
    }
    
    public static void testReadUser() {
	    UserConfig  userConfig = mapper.load(UserConfig.class, "user2"); 
	    System.out.println(userConfig.toString());
	    
	    //UserConfig  userConfig = mapper.load(UserConfig.class, "user2");  
    	//List ownerList = RequestCommon.getUserOwners(userConfig);
    	//System.out.println("ownerList " + ownerList);
    }
    
    public static void testAddSensor() {
    	SensorConfig sensorConfig = new SensorConfig();
    	sensorConfig.setAddress("787805");
    	sensorConfig.setAlias("Test Printer 1");
    	sensorConfig.setAwsThing("EC2GW-NodeRed");
    	sensorConfig.setEnablePoll(false);
    	sensorConfig.setInstallationDate("01/01/2017");
    	sensorConfig.setInstalledBy("Mr Smith");
    	sensorConfig.setLocation("office");
    	sensorConfig.setMaintContract("monthly");
    	sensorConfig.setMaintStartDate("01/01/2017");
    	sensorConfig.setMaintEndDate("01/01/2019");
    	sensorConfig.setManufacturer("Polaroid");
    	sensorConfig.setModel("Polaroid 420T");
    	sensorConfig.setOwner("Company AAA");
    	sensorConfig.setPollInterval(900001L);
    	sensorConfig.setSensorId("PRNTST001010117");
    	sensorConfig.setSerialNumber("SN0000001");
    	sensorConfig.setSoftwareInstalled("PESONA Card");
    	sensorConfig.setSoftwareLicense("LIC0000001");
    	sensorConfig.setWarrantyStartDate("01/01/2017");
    	sensorConfig.setWarrantyEndDate("01/01/2017");
    	sensorConfig.setType("Printer");
    	
    	List contactNames = new ArrayList();
    	contactNames.add("Mr. Smith");
    	sensorConfig.setContactNames(contactNames);
    	mapper.save(sensorConfig);        
    }
    
    public static void testReadSensor() {
	    SensorConfig  sensorConfig = mapper.load(SensorConfig.class, "PRNTST001010117"); 
	    System.out.println("sensorId=" + sensorConfig.getSensorId()
	    		+ ", owner=" + sensorConfig.getOwner());
    }
    
    public static void testGetSensorByOwner() {
        Map<String, String> nameMap = new HashMap<String, String>();
        nameMap.put("#owner", "owner");

        Map<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
        valueMap.put(":o", new AttributeValue().withS("Company AAA"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("#owner = :o")
                .withExpressionAttributeNames(nameMap)
                .withExpressionAttributeValues(valueMap);
    	
    	List<SensorConfig> sensorConfigList = mapper.scan(SensorConfig.class, scanExpression);

    	System.out.println("no of SensorConfig loaded: " + sensorConfigList.size());
		for (SensorConfig sensorConfig : sensorConfigList) {
			System.out.format("Id=" + sensorConfig.getSensorId()
				+ ", owner=" + sensorConfig.getOwner()
				+ ", location=" + sensorConfig.getLocation()
				+ ", alias=" + sensorConfig.getAlias()
				+ ", address=" + sensorConfig.getAddress()
				);
		}
		List<SensorConfig> result = new ArrayList();
		result.addAll(sensorConfigList);
		
		result.add(
				AwsService.getInstance().getSensorConfigService().createSensorConfigDummy("Company AAA"));
    }

//    public static void testGetSensorByOwner() {
//    	Table sensorConfigTable = dynamoDB.getTable(SensorConfig.DB_TABLE_NAME);
//        List<SensorConfig> sensorConfigList = RequestCommon.getSensorsByOwner(
//        		sensorConfigTable, "Company AAA", mapper);
//		System.out.println("no of SensorConfig loaded: " + sensorConfigList.size());
//		for (SensorConfig sensorConfig : sensorConfigList) {
//			System.out.format("Id=" + sensorConfig.getSensorId() + ", owner=" + sensorConfig.getOwner()
//			+ " location " + sensorConfig.getLocation());
//		}
//    }
}
