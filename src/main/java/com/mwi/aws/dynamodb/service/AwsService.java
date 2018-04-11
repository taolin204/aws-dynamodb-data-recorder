/**
 * Copyright (c) 2017 MWI Pte. Ltd. All Rights Reserved.
 *
 * Revision 1.0 09/10/2017 Sun Yong
 * - Creation
 */
package com.mwi.aws.dynamodb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mfg.Contact;
import com.mfg.OwnerConfig;
import com.mfg.SensorConfig;
import com.mfg.SensorState;
import com.mfg.UserConfig;
import com.mfg.UserPermissions;
import com.mwi.aws.dynamodb.util.DebugUtil;
import com.mwi.aws.dynamodb.util.SystemConfig;




/**
 * This class provides the AWS services.
 * Use "-Dregion=ap-southeast-1" system property for local testing.
 */
public class AwsService {

	
	 /** logger. */
    private static Logger logger = LoggerFactory.getLogger(AwsService.class);
    
	private static AwsService instance;
	public static AwsService getInstance() {
		if(instance == null) {
			instance = new AwsService();
		} 
		return instance;
	}
    
    //========== PMS change start ==========
    // message constants
//    private static final String STR_SENSOR_OFFLINE = "OFFLINE: no data received";
//    private static final String STR_SENSOR_ONLINE = "ONLINE: data received";
//    private static final String STR_SYSTEM_OFFLINE = "ERROR: IoT system is down";
//    private static final String STR_SYSTEM_ONLINE = "NORMINAL: IoT system is OK";
//    private static final String STR_SENSOR_MAINT = "due for maintenance on ";
    //========== PMS change end ==========
    
    //========== PMS change start ==========
    /** string format. */
    private FormattingControl formattingControl;
    /** system name. */
    private String systemName = "";
    //========== PMS change end ==========
    /** AWS region. */
    private Regions region;
    /** AmazonDynamoDBClient. */
    private AmazonDynamoDBClient dynamoDbClient;
    
    private AmazonDynamoDBClient dynamoDBLocalClient;
    /** DynamoDBMapper. */
    private DynamoDBMapper mapper;

    private DynamoDB db;
    
    private Table userConfigTable;
    
    private OwnerConfigService ownerConfigService;
    
    private SensorConfigService sensorConfigService;
   
    private String serverConfiguration = "local";

    private UserConfig loggedInUser;
    
    /**
     * Constructor.
     */
    public AwsService() {
        //========== PMS change start ==========
    	formattingControl = new FormattingControl("", null);
    	
    	String prop = System.getProperty("systemName");
        if (prop != null) {
            systemName = prop;
        }
        //========== PMS change end ==========

    	initAwsRegion();
        initDynamoDbClient();
        
        ownerConfigService = new OwnerConfigService(mapper);
        sensorConfigService = new SensorConfigService(mapper);
    }

    /**
     * Init AWS region.
     */
    private void initAwsRegion() {
        String regionName;

//        String prop = System.getProperty("region");
//        if (prop != null) {
//            // region for local testing purpose
//            regionName = prop;
//        } else{
//            // get region name from EC2 environment
//            regionName = Regions.getCurrentRegion().getName();
//        }
        
        regionName = "ap-southeast-1";
        logger.debug("AWS region " + regionName);
        regionName = regionName.replaceAll("-", "_").toUpperCase();
        region = Regions.valueOf(regionName);
    }
    
    /**
     * init DynamoDB client.
     */
    private void initDynamoDbClient() {
    	
    	serverConfiguration = SystemConfig.getProperty("aws.dynamodb.server.configuration", "local");
    	DebugUtil.log("serverConfiguration " + serverConfiguration);
    	if(serverConfiguration.equals("local")) {
    		// The secret key doesn't need to be valid, DynamoDB Local doesn't care.
        	AWSCredentials credentials = new BasicAWSCredentials(
        			SystemConfig.getProperty("aws.dynamodb.local.credentials.accesskey", "AKIAJRRNA6EUMTW6YFGQ"), 
        			SystemConfig.getProperty("aws.dynamodb.local.credentials.secretkey", "bogus"));
        	
        	
        	DebugUtil.log("accesskey " + SystemConfig.getProperty("aws.dynamodb.local.credentials.accesskey", "AKIAJRRNA6EUMTW6YFGQ"));
        	DebugUtil.log("secretKey " + SystemConfig.getProperty("aws.dynamodb.local.credentials.secretkey", "bogus"));
        	DebugUtil.log("endpoint " + SystemConfig.getProperty("aws.dynamodb.local.endpoint", "http://localhost:8111"));
        	DebugUtil.log("region " + SystemConfig.getProperty("aws.dynamodb.local.region", "local"));
        	
        	this.dynamoDBLocalClient = new AmazonDynamoDBClient(credentials);
        	// Make sure you use the same port as you configured DynamoDB Local to bind to.
        	dynamoDBLocalClient.setEndpoint(SystemConfig.getProperty("aws.dynamodb.local.endpoint", "http://localhost:8111"));
        	// Sign requests for the "local" region to read data written by the toolkit.
        	dynamoDBLocalClient.setSignerRegionOverride(SystemConfig.getProperty("aws.dynamodb.local.region", "local"));
        	
        	mapper = new DynamoDBMapper(dynamoDBLocalClient); 
        	db = new DynamoDB(dynamoDBLocalClient);
        	
    	} else {
	        ClientConfiguration clientConfig = new ClientConfiguration()
	                .withClientExecutionTimeout(15000) //set 5000ms timeout
	                .withRequestTimeout(15000)
	                .withConnectionTimeout(15000)
	                .withSocketTimeout(15000)
	                .withMaxErrorRetry(0);
	
	        	
	        dynamoDbClient = new AmazonDynamoDBClient(clientConfig).withRegion(region);
	
	        mapper = new DynamoDBMapper(dynamoDbClient); 
	        db = new DynamoDB(dynamoDbClient);
        
    	}
    	
    	userConfigTable = db.getTable(UserConfig.DB_TABLE_NAME);
    }
    
    /**
     * load all SensorConfig.
     * @return list of SensorConfig
     */
    public List<SensorConfig> loadAllSensorConfig() {
        List<SensorConfig> sensorConfigList = null;
        try {
            sensorConfigList = mapper.scan(
                    SensorConfig.class, new DynamoDBScanExpression());
        } catch (final Exception e) {
            logger.error("Load SensorConfig exception: " + e.getMessage());
        }
        logger.debug("no of SensorConfig loaded: " + sensorConfigList.size());
        return sensorConfigList;
    }
    
    /**
     * load all SensorState.
     * @return list of SensorState
     */
    public List<SensorState> loadAllSensorState() {
        List<SensorState> sensorStateList = null;
        try {
            sensorStateList = mapper.scan(
                    SensorState.class, new DynamoDBScanExpression());
        } catch (final Exception e) {
            logger.error("Load SensorState exception: " + e.getMessage());
        }
        logger.debug("no of SensorState loaded: " + sensorStateList.size());
        return sensorStateList;
    }

    /**
     * Load SensorConfig by ID.
     * @param sensorId sensorId
     * @return SensorConfig
     */
    public SensorConfig loadSensorConfig(String sensorId) {
        SensorConfig sensorConfig = null;
        try {
            sensorConfig = mapper.load(SensorConfig.class, sensorId);
        } catch (final Exception e) {
            logger.error("Load SensorConfig for " + sensorId + " exception: " + e.getMessage());
        }
        return sensorConfig;
    }

    /**
     * Load SensorState by ID.
     * @param sensorId sensorId
     * @return SensorState
     */
    public SensorState loadSensorState(String sensorId) {
        SensorState sensorState = null;
        try {
            sensorState = mapper.load(SensorState.class, sensorId);
        } catch (final Exception e) {
            logger.error("Load SensorState for " + sensorId + " exception: " + e.getMessage());
        }
        return sensorState;
    }
    
    /**
     * load all SensorConfig.
     * @return list of SensorConfig
     */
    public List<OwnerConfig> loadAllOwnerConfig() {
        List<OwnerConfig> ownerConfigList = null;
        try {
        	ownerConfigList = mapper.scan(
        			OwnerConfig.class, new DynamoDBScanExpression());
        } catch (final Exception e) {
            logger.error("Load OwnerConfig exception: " + e.getMessage());
        }
        logger.debug("no of OwnerConfig loaded: " + ownerConfigList.size());
        return ownerConfigList;
    }
    
    
    public OwnerConfigService getOwnerConfigService() {
		return ownerConfigService;
	}

	public void setOwnerConfigService(OwnerConfigService ownerConfigService) {
		this.ownerConfigService = ownerConfigService;
	}
	
	

	public SensorConfigService getSensorConfigService() {
		return sensorConfigService;
	}

	public void setSensorConfigService(SensorConfigService sensorConfigService) {
		this.sensorConfigService = sensorConfigService;
	}

	public Table getUserConfigTable() {
		return userConfigTable;
	}

	public void setUserConfigTable(Table userConfigTable) {
		this.userConfigTable = userConfigTable;
	}
	
	public boolean validateUserLogin(String userName) {
		boolean result = true;
		if(this.serverConfiguration.equals("test")) {
	    	
	    } else {
			UserConfig  userConfig = mapper.load(UserConfig.class, userName); 
			if(userConfig != null) {
				DebugUtil.log("userId=" + userConfig.getIdentityId()
		    		+ ", name=" + userConfig.getUserName());
				this.setLoggedInUser(userConfig);
			} else {
				result = false;
			}
	    }
		
		return result;
	}
	
	public UserConfig getLoggedInUser() {
		if(this.serverConfiguration.equals("test")) {
			loggedInUser = new UserConfig();
			loggedInUser.setIdentityId("user2");
			loggedInUser.setUserName("user name 2");
	
	    	List<String> managedOwners = new ArrayList();
	    	managedOwners.add("Company AAA");
	    	managedOwners.add("Company BBB");
	    	loggedInUser.setManagedOwners(managedOwners);
	
	    	UserPermissions userPermissions = new UserPermissions();
	    	userPermissions.setCanActuateSensor(Boolean.TRUE);
	    	userPermissions.setCanConfigureOwner(Boolean.TRUE);
	    	userPermissions.setCanConfigureSensor(Boolean.TRUE);
	    	userPermissions.setCanConfigureUser(Boolean.TRUE);
	    	userPermissions.setCanReadSensor(Boolean.TRUE);
	    	loggedInUser.setUserPermissions(userPermissions);
		}
		return loggedInUser;
	}

	public void setLoggedInUser(UserConfig loggedInUser) {
		this.loggedInUser = loggedInUser;
	}

	public List<String> getUserOwnerStrngs(UserConfig userConfig) {
		
		List<String> ownerList = new ArrayList<String>();
		if(serverConfiguration.equals("test")) {
			ownerList.add("Company AAA");
			ownerList.add("Company BBB");
		} else {
			//RequestCommon requestCommon = new RequestCommon();
			ownerList = userConfig.getManagedOwners();
		}
		
		return ownerList;
	}

	public List<Object> getUserOwnerLists(UserConfig userConfig) {
		
		List<Object> ownerList = new ArrayList();
		List<String> owners = getUserOwnerStrngs(userConfig);
		
		for(String owner : owners) {
			OwnerConfig ownerConfig = loadOwnerConfig(owner, mapper);
			if(ownerConfig != null) {
				ownerList.add(ownerConfig);
			} else DebugUtil.error("getUserOwnerLists ownerConfig is null ");
		}
		
		return ownerList;
	}
	
	public OwnerConfig loadOwnerConfig(String owner, DynamoDBMapper mapper) {
		
		OwnerConfig ownerConfig = null;
		if(serverConfiguration.equals("test")) {
			
			if(owner.equals("Company BBB")) {
				
				ownerConfig = new OwnerConfig();
		    	ownerConfig.setOwner("Company BBB");
		    	ownerConfig.setAddress("BLK 234, #11-11, Singapore 345677");
		    	
		    	Map<String, Contact> contacts = new HashMap<String, Contact>();
		    	Contact contact = new Contact();
		    	contact.setContactName("Mr. Black");
		    	contact.setEmail("user2@abc.com.sg");
		    	contact.setPhoneNumber("93334567");
		    	contacts.put("Mr. Black", contact);
		    	
		    	contact = new Contact();
		    	contact.setContactName("Mr. Smith");
		    	contact.setEmail("user1@abc.com.sg");
		    	contact.setPhoneNumber("91234567");
		    	contacts.put("Mr. Smith", contact);
		    	
		    	ownerConfig.setContacts(contacts);
		    	
			} else if(owner.equals("Company AAA")) {
				
				ownerConfig = new OwnerConfig();
		    	ownerConfig.setOwner("Company AAA");
		    	ownerConfig.setAddress("BLK 123, #11-11, Singapore 345677");
		    	
		    	Map<String, Contact> contacts = new HashMap<String, Contact>();
		    	Contact contact = new Contact();
		    	contact.setContactName("Mr. Smith");
		    	contact.setEmail("user1@abc.com.sg");
		    	contact.setPhoneNumber("91234567");
		    	contacts.put("Mr. Smith", contact);
		    	
		    	ownerConfig.setContacts(contacts);
		    	
			}
			
		} else {
			//ownerConfig = RequestCommon.loadOwnerConfig(owner, mapper);
			
			ownerConfig = mapper.load(OwnerConfig.class, owner); 
		}
		
		return ownerConfig;
	}
	
	public void saveOwnerConfig(OwnerConfig ownerConfig) {
		if(serverConfiguration.equals("test")) {
			System.out.println("save owner config dummy data");
		} else {
			mapper.save(ownerConfig);
		}
	}
	
	public List<Object> getSensorListsByOwner(String owner) {
		List sensorConfigList = new ArrayList();
		Table sensorConfigTable = db.getTable(SensorConfig.DB_TABLE_NAME);
		
		sensorConfigList = getSensorsByOwner(sensorConfigTable, owner, mapper);
				
		return sensorConfigList;
	}
	
	
	public List<SensorConfig> getSensorsByOwner(Table sensorConfigTable, String owner,
            DynamoDBMapper mapper) {
		
		List<SensorConfig> sensorConfigList = new ArrayList();
		if(serverConfiguration.equals("test")) {
			
			if(owner.equals("Company AAA")) {
				
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
		    	sensorConfig.setType("Printer");
		    	
		    	List<String> contactNames = new ArrayList();
		    	contactNames.add("Mr Smith");
		    	sensorConfig.setContactNames(contactNames);
		    	sensorConfigList.add(sensorConfig);
				
			} else if(owner.equals("Company BBB")) {
				
				SensorConfig sensorConfig = new SensorConfig();
		    	sensorConfig.setAddress("787807");
		    	sensorConfig.setAlias("Test Printer 3");
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
		    	sensorConfig.setOwner("Company BBB");
		    	sensorConfig.setPollInterval(900001L);
		    	sensorConfig.setSensorId("PRNTST001010117");
		    	sensorConfig.setSerialNumber("SN0000003");
		    	sensorConfig.setSoftwareInstalled("PESONA Card");
		    	sensorConfig.setSoftwareLicense("LIC0000003");
		    	sensorConfig.setType("Printer");
		    	
		    	List<String> contactNames = new ArrayList();
		    	contactNames.add("Mr Smith");
		    	sensorConfig.setContactNames(contactNames);
		    	sensorConfigList.add(sensorConfig);
		    	
			}
			
		} else {
//			sensorConfigList = RequestCommon.getSensorsByOwner(sensorConfigTable, owner, mapper);
//			DebugUtil.log("sensorConfigList " + sensorConfigList);
			
			Map<String, String> nameMap = new HashMap<String, String>();
	        nameMap.put("#owner", "owner");

	        Map<String, AttributeValue> valueMap = new HashMap<String, AttributeValue>();
	        valueMap.put(":o", new AttributeValue().withS(owner));

	        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
	                .withFilterExpression("#owner = :o")
	                .withExpressionAttributeNames(nameMap)
	                .withExpressionAttributeValues(valueMap);
	    	
	    	sensorConfigList = mapper.scan(SensorConfig.class, scanExpression);
		}
		
		return sensorConfigList;
		
	}
	
	public void saveSensorConfig(SensorConfig sensorConfig) {
		if(serverConfiguration.equals("test")) {
			System.out.println("save sensor config dummy data");
		} else {
			mapper.save(sensorConfig);
		}
	}
	
	public void saveAwsData(Object data) {
		mapper.save(data);
	}
	
	public void removeAwsData(Object data) {
		mapper.delete(data);
	}
	
	
	public String[] getContactStringsForOwner(String owner) {
		String[] contacts =  null;
		if(serverConfiguration.equals("test")) {
			contacts = new String[3];
			contacts[0] = "Mr Smith";
			contacts[1] = "Mr Black";
			contacts[2] = "Mrs Smith";
		} else {
			OwnerConfig ownerConfig = this.loadOwnerConfig(owner, mapper);
			DebugUtil.log("ownerConfig " + ownerConfig);
			contacts = new String[ownerConfig.getContacts().size()];
			//for(int i=0; i<ownerConfig.getContacts().size(); i++) {
			//	contacts[i] = ownerConfig.getContacts().keySet().
			//}
			int i = 0;
			Iterator itr = ownerConfig.getContacts().keySet().iterator();
			while(itr.hasNext()) {
				String obj = (String) itr.next();
				contacts[i] = obj;
				i++;
			}
			
			DebugUtil.log(contacts.toString());
			//contacts = (String[]) ownerConfig.getContacts().keySet().toArray();
		}
		
		return contacts;
	}
	
	
	
	public static void main(String[] args) {
		
//    	AwsService service = new AwsService();
//    	
//    	List<SensorConfig> sensorList = service.loadAllSensorConfig();
//    	for(int i=0; i<sensorList.size(); i++) {
//    		SensorConfig obj = (SensorConfig) sensorList.get(i);
//    		System.out.println(obj.toString());
//    	}
    	
    	
//    	List<OwnerConfig> OwnerList = service.loadAllOwnerConfig();
//    	for(int i=0; i<OwnerList.size(); i++) {
//    		OwnerConfig obj = (OwnerConfig) OwnerList.get(i);
//    		System.out.println(obj.toString());
//    	}
    	
    	
//    	System.out.println(SystemConfig.getProperty("aws.dynamodb.local.credentials.accesskey"));
//    	System.out.println(SystemConfig.getProperty("aws.dynamodb.server.configuration"));
//    	
//    	Map<String, Object> inputParams = new HashMap();
//    	UserConfigFactory userConfigFactory = new UserConfigFactory(service.getUserConfigTable());
//    	inputParams.put(RequestCommon.JSON_KEY_REQUESTER_IDENTITY_ID, "user1");
//    	inputParams.put(RequestCommon.JSON_KEY_TARGET_IDENTITY_ID, "user1");
//    	JSONObject res = userConfigFactory.getUserConfig(inputParams);
//    	System.out.println(res.toString());
    	
    	
    	// The secret key doesn't need to be valid, DynamoDB Local doesn't care.
    	AWSCredentials credentials = new BasicAWSCredentials("AKIAJRRNA6EUMTW6YFGQ", "bogus");
    	AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials);
    	// Make sure you use the same port as you configured DynamoDB Local to bind to.
    	client.setEndpoint("http://localhost:8111");
    	// Sign requests for the "local" region to read data written by the toolkit.
    	client.setSignerRegionOverride("local");

    	DynamoDB dynamoDB = new DynamoDB(client);
    	DynamoDB db = new DynamoDB(client);
    	DynamoDBMapper mapper = new DynamoDBMapper(client);
    	
//    	Table userConfigTable = db.getTable(UserConfig.DB_TABLE_NAME);
//    	
//    	
//    	// =================== write
//    	UserConfig userConfig = new UserConfig();
//    	userConfig.setIdentityId("user2");
//    	userConfig.setUserName("user name 2");
//
//    	List<String> managedOwners = new ArrayList();
//    	managedOwners.add("Company AAA");
//    	managedOwners.add("Company BBB");
//    	userConfig.setManagedOwners(managedOwners);
//
//    	UserPermissions userPermissions = new UserPermissions();
//    	userPermissions.setCanActuateSensor(Boolean.TRUE);
//    	userPermissions.setCanConfigureOwner(Boolean.TRUE);
//    	userPermissions.setCanConfigureSensor(Boolean.TRUE);
//    	userPermissions.setCanConfigureUser(Boolean.TRUE);
//    	userPermissions.setCanReadSensor(Boolean.TRUE);
//    	userConfig.setUserPermissions(userPermissions);
//    	mapper.save(userConfig);
//		
//		// =================== read
//		Map<String, Object> inputParams = new HashMap();
//		inputParams.put(RequestCommon.JSON_KEY_REQUESTER_IDENTITY_ID, "user2");
//		inputParams.put(RequestCommon.JSON_KEY_TARGET_IDENTITY_ID, "user2");
//		UserConfigFactory userConfigFactory = new UserConfigFactory(userConfigTable);
//		JSONObject res = userConfigFactory.getUserConfig(inputParams);
//		System.out.println(res.toString());
//
//		Gson gson = new GsonBuilder().create();
//		UserConfig user = gson.fromJson(res.toJSONString(), UserConfig.class);
//		System.out.println("output userConfig : " + user.toString());
		
		
		
    	Table ownerConfigTable = db.getTable(OwnerConfig.DB_TABLE_NAME);
//    	
//    	OwnerConfig ownerConfig = new OwnerConfig();
//    	ownerConfig.setOwner("Company BBB");
//    	ownerConfig.setAddress("BLK 123, #11-11, Singapore 345688");
//    	
//    	Map<String, Contact> contacts = new HashMap<String, Contact>();
//    	Contact contact = new Contact();
//    	contact.setContactName("Mr. Black");
//    	contact.setEmail("user2@abc.com.sg");
//    	contact.setPhoneNumber("93334567");
//    	contacts.put("Mr. Black", contact);
//    	
//    	contact = new Contact();
//    	contact.setContactName("Mr. Smith");
//    	contact.setEmail("user1@abc.com.sg");
//    	contact.setPhoneNumber("91234567");
//    	contacts.put("Mr. Smith", contact);
//    	
//    	ownerConfig.setContacts(contacts);
//    	mapper.save(ownerConfig);
		
		// =================== read
//    	Map<String, Object> inputParams = new HashMap();
//    	inputParams.put(RequestCommon.JSON_KEY_REQUESTER_IDENTITY_ID, "user2");
//    	OwnerListFactory ownerListFactory = new OwnerListFactory(ownerConfigTable, mapper);
//    	JSONObject res = ownerListFactory.getOwnerList(inputParams);
//    	System.out.println(res.toString());
    	
//    	inputParams = new HashMap();
//		inputParams.put(RequestCommon.JSON_KEY_REQUESTER_IDENTITY_ID, "user2");
//		inputParams.put(RequestCommon.JSON_KEY_OWNER, "Company BBB");
//		OwnerConfigFactory ownerConfigFactory = new OwnerConfigFactory(mapper);
//		res = ownerConfigFactory.getOwnerConfig(inputParams);
//		System.out.println(res.toString());
//		
//		Gson gson = new GsonBuilder().create();
//		OwnerConfig user = gson.fromJson(res.get("ownerConfiguration").toString(), OwnerConfig.class);
//		System.out.println("output userConfig : " + user.toString());
    	
//    	Table sensorConfigTable = db.getTable(SensorConfig.DB_TABLE_NAME);
//    	
//    	SensorConfig sensorConfig = new SensorConfig();
//    	sensorConfig.setAddress("787805");
//    	sensorConfig.setAlias("Test Printer 1");
//    	sensorConfig.setAwsThing("EC2GW-NodeRed");
//    	sensorConfig.setEnablePoll(false);
//    	sensorConfig.setInstallationDate("01/01/2017");
//    	sensorConfig.setInstalledBy("Mr Smith");
//    	sensorConfig.setLocation("office");
//    	sensorConfig.setMaintContract("monthly");
//    	sensorConfig.setMaintStartDate("01/01/2017");
//    	sensorConfig.setMaintEndDate("01/01/2019");
//    	sensorConfig.setManufacturer("Polaroid");
//    	sensorConfig.setModel("Polaroid 420T");
//    	sensorConfig.setOwner("Company AAA");
//    	sensorConfig.setPollInterval(900001L);
//    	sensorConfig.setSensorId("PRNTST001010117");
//    	sensorConfig.setSerialNumber("SN0000001");
//    	sensorConfig.setSoftwareInstalled("PESONA Card");
//    	sensorConfig.setSoftwareLicense("LIC0000001");
//    	sensorConfig.setType("Printer");
//    	
//    	List<String> contactNames = new ArrayList();
//    	contactNames.add("Mr. Smith");
//    	sensorConfig.setContactNames(contactNames);
//    	
//    	
//    	mapper.save(sensorConfig);
// 
//    	Map<String, Object> inputParams = new HashMap();
//    	inputParams.put(RequestCommon.JSON_KEY_REQUESTER_IDENTITY_ID, "user2");
//    	inputParams.put(RequestCommon.JSON_KEY_TARGET_OWNER, "Company AAA");
//    	inputParams.put(RequestCommon.JSON_KEY_CHECKSUM, "");
//    	SensorListFactory sensorListFactory = new SensorListFactory(sensorConfigTable, mapper);
//    	JSONObject res = sensorListFactory.getSensorList(inputParams);
//    	System.out.println(res.toString());
    	
//    	 List<SensorConfig> sensorConfigList = RequestCommon.getSensorsByOwner(
//    	          sensorConfigTable, "Company AAA", mapper);
//    	System.out.println(sensorConfigList.toString());
    	
//    	Table sensorConfigTable = dynamoDB.getTable(SensorConfig.DB_TABLE_NAME);
//        List<SensorConfig> sensorConfigList = RequestCommon.getSensorsByOwner(
//        		sensorConfigTable, "Company AAA", mapper);
//		System.out.println("no of SensorConfig loaded: " + sensorConfigList.size());
//		for (SensorConfig sensorConfig : sensorConfigList) {
//			System.out.format("Id=" + sensorConfig.getSensorId() + ", owner=" + sensorConfig.getOwner());
//		}
		
    	
//    	Map<String, Object> inputParams = new HashMap();
//    	inputParams.put(RequestCommon.JSON_KEY_REQUESTER_IDENTITY_ID, "user2");
//    	inputParams.put(RequestCommon.JSON_KEY_SENSOR_ID, "PRNTST001010117");
//    	SensorConfigFactory sensorConfigFactory = new SensorConfigFactory(mapper);
//    	JSONObject res = sensorConfigFactory.getSensorConfig(inputParams);
    	
//    	UserConfig  userConfig = mapper.load(UserConfig.class, "user2");  
//    	List ownerList = RequestCommon.getUserOwners(userConfig);
//    	System.out.println("ownerList " + ownerList);
//    	
//    	OwnerConfig ownerConfig = AwsService.getInstance().loadOwnerConfig("Company AAA", mapper);
//		DebugUtil.log("ownerConfig " + ownerConfig);
//		//String[] contacts = (String[]) ownerConfig.getContacts().keySet().toArray();
//		int i = 0;
//		Iterator itr = ownerConfig.getContacts().keySet().iterator();
//		String[] contacts = new String[ownerConfig.getContacts().size()];
//		
//		while(itr.hasNext()) {
//			String obj = (String) itr.next();
//			contacts[i] = obj;
//			i++;
//		}
//		
//		DebugUtil.log(contacts.toString());
		//contactsList.to
//		System.out.println("contacts " + ownerConfig.getContacts().keySet().toArray());
//		for(Object obj : ownerConfig.getContacts().keySet()) {
//			System.out.println((String) obj);
//		}
		
		Table sensorConfigTable = db.getTable(SensorConfig.DB_TABLE_NAME);
//		List sensorConfigList = RequestCommon.getSensorsByOwner(sensorConfigTable, "Company AAA", mapper);
//		DebugUtil.log(sensorConfigList.toString());

	}
    
    
}
