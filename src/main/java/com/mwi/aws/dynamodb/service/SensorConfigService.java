package com.mwi.aws.dynamodb.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fluttercode.datafactory.impl.DataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.mfg.Contact;
import com.mfg.OwnerConfig;
import com.mfg.SensorConfig;

public class SensorConfigService {

	/** logger. */
    private static Logger logger = LoggerFactory.getLogger(OwnerConfigService.class);
	/** DynamoDBMapper. */
    private DynamoDBMapper mapper;
    
    public SensorConfigService(DynamoDBMapper aMapper) {
    	mapper = aMapper;
    }
	
    public SensorConfig createSensorConfigDummy(String owner) {
    	
    	DataFactory dataFactory = new DataFactory();
    	
    	SensorConfig sensorConfig = new SensorConfig();
    	sensorConfig.setSensorId("SensorId");
    	sensorConfig.setAddress("");
    	sensorConfig.setAlias("");
    	sensorConfig.setAwsThing("EC2GW-NodeRed");
    	sensorConfig.setEnablePoll(false);
    	sensorConfig.setInstallationDate("01/01/2018");
    	sensorConfig.setInstalledBy(" ");
    	sensorConfig.setLocation("office");
    	sensorConfig.setMaintContract("monthly");
    	sensorConfig.setMaintStartDate("01/01/2018");
    	sensorConfig.setMaintEndDate("01/01/2019");
    	sensorConfig.setManufacturer("Polaroid");
    	sensorConfig.setModel("Polaroid 420T");
    	sensorConfig.setOwner(owner);
    	sensorConfig.setPollInterval(900001L);
    	sensorConfig.setSerialNumber("");
    	sensorConfig.setSoftwareInstalled("");
    	sensorConfig.setSoftwareLicense("");
    	sensorConfig.setType("Printer");
    	sensorConfig.setWarrantyStartDate("");
    	sensorConfig.setWarrantyEndDate("10/01/2017");
    	List<String> contactNames = new ArrayList();
    	sensorConfig.setContactNames(contactNames);
		
		return sensorConfig;
	}
	
}
