package com.mwi.aws.dynamodb.model;

import java.util.ArrayList;
import java.util.List;

public class AwsDataTypeStore {
	private List<AwsDataType> awsDataTypes = new ArrayList();

	public List<AwsDataType> getAwsDataTypes() {
		return awsDataTypes;
	}

	public void setAwsDataTypes(List<AwsDataType> awsDataTypes) {
		this.awsDataTypes = awsDataTypes;
	}
	
}
