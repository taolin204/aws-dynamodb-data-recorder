package com.mwi.aws.dynamodb.service;

import java.util.ArrayList;
import java.util.List;

public class OwnerConfigService {

	private List ownerList;
	
	
	private static OwnerConfigService instance;
	public static OwnerConfigService getInstance() {
		if(instance == null) {
			instance = new OwnerConfigService();
		}
		return instance;
	}
	
	public OwnerConfigService() {
		ownerList = new ArrayList();
		//ownerList.add(OwnerConfigTest.getInstance().getOwner());
	}
	
	public List getOwnList() {
		return ownerList;
	}
}
