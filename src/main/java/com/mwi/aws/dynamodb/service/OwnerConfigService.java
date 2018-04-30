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

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class OwnerConfigService {

	/** logger. */
    private static Logger logger = LoggerFactory.getLogger(OwnerConfigService.class);
	/** DynamoDBMapper. */
    private DynamoDBMapper mapper;
    
    public OwnerConfigService(DynamoDBMapper aMapper) {
    	mapper = aMapper;
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
    
    
//	private static OwnerConfigService instance;
//	
//	private List ownerList =  new ArrayList();
//	
//	
//	public static OwnerConfigService getInstance() {
//		if(instance == null) {
//			instance = new OwnerConfigService();
//		}
//		return instance;
//	}
//	
//	public OwnerConfigService() {
//		//ownerList = new ArrayList();
//		//ownerList.add(OwnerConfigTest.getInstance().getOwner());
//		createOwnConfigList();
//	}
	
//	public List getOwnList() {
//		return ownerList;
//	}
	
	public OwnerConfig createOwnerConfigDummy() {
	
		DataFactory dataFactory = new DataFactory();
		
		OwnerConfig owner = new OwnerConfig();
		owner.setAddress("");
		owner.setOwner("Owner");
		
		Map contacts = new HashMap();
//		Contact contact = new Contact();
//		contact.setContactName("Contact");
//		contact.setEmail("");
//		contact.setPhoneNumber("");
//		contacts.put(contact.getKey(), contact);
		owner.setContacts(contacts);
		
		return owner;
	}
	
	public Contact createContactDummy() {
		
		DataFactory dataFactory = new DataFactory();
		Contact contact = new Contact();
		//contact.setContactName("Mr " + dataFactory.getLastName());
		contact.setContactName("Contact");
		contact.setEmail("");
		contact.setPhoneNumber("");
		return contact;
	}
	
	
//	public void createOwnConfigList() {
//		PodamFactory factory = new PodamFactoryImpl();
//		OwnerConfig ownerConfig = factory.manufacturePojo(OwnerConfig.class);
//		//factory.populatePojo(ownerConfig);
//		ownerList.add(ownerConfig);
//		
//		for(int i=0; i<ownerList.size(); i++) {
//			OwnerConfig owner = (OwnerConfig) ownerList.get(i);
//			System.out.println(owner.toString());
//		}
//	}
	
	public static void main(String[] args) {
		//OwnerConfigService.getInstance().getOwnList();
	}
}
