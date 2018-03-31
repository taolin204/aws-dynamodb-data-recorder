package com.mwi.aws.dynamodb.service;

import java.util.ArrayList;
import java.util.List;

import com.mwi.aws.dynamodb.model.OwnerConfig;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public class OwnerConfigService {

	
	private static OwnerConfigService instance;
	
	private List ownerList =  new ArrayList();
	
	
	public static OwnerConfigService getInstance() {
		if(instance == null) {
			instance = new OwnerConfigService();
		}
		return instance;
	}
	
	public OwnerConfigService() {
		//ownerList = new ArrayList();
		//ownerList.add(OwnerConfigTest.getInstance().getOwner());
		createOwnConfigList();
	}
	
	public List getOwnList() {
		return ownerList;
	}
	
	public void createOwnConfigList() {
		PodamFactory factory = new PodamFactoryImpl();
		OwnerConfig ownerConfig = factory.manufacturePojo(OwnerConfig.class);
		//factory.populatePojo(ownerConfig);
		ownerList.add(ownerConfig);
		
		for(int i=0; i<ownerList.size(); i++) {
			OwnerConfig owner = (OwnerConfig) ownerList.get(i);
			System.out.println(owner.toString());
		}
	}
	
	public static void main(String[] args) {
		OwnerConfigService.getInstance().getOwnList();
	}
}
