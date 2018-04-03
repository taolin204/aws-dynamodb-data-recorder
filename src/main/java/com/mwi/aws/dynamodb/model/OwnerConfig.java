/**
 * Copyright (c) 2017 MWI Pte. Ltd. All Rights Reserved.
 *
 * Revision 1.0 25/10/2017 Sun Yong
 * - Creation for PMS change
 */
package com.mwi.aws.dynamodb.model;

import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.simple.JSONObject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.mwi.aws.dynamodb.util.DynamoDBCustomUtils;

/**
 * Representation of an item from the Dynamo DB table userGroup
 */
@DynamoDBTable(tableName = OwnerConfig.DB_TABLE_NAME)
public class OwnerConfig implements AwsDataInterface{
    //DYNAMO DB TABLE NAME
    public static final String DB_TABLE_NAME = "ownerConfiguration";

    //DYNAMO DB ATTRIBUTE NAMES
    public static final String DB_ATTR_OWNER = "owner";
    public static final String DB_ATTR_CONTACTS = "contacts"; 
    public static final String DB_ATTR_ADDRESS = "address";
    
    /** The owner of sensor or user. */
    private String owner;
    /** Map of contacts of the owner. */
    private Map<String, Contact> contacts;
    /** Address of the owner. */
    private String address;
    
    @Override
    public Object getKey() {
    	return owner;
    }
    /**
     * Convert object to JSON object.
     * 
     * @return JSON object
     */
    public JSONObject toJSON() {
        return DynamoDBCustomUtils.toJSON(this);
    }
    
    /*FIELD GETTERS AND SETTERS*/
    @DynamoDBHashKey(attributeName = DB_ATTR_OWNER)
    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    
    @DynamoDBAttribute(attributeName = DB_ATTR_CONTACTS)
    public Map<String, Contact> getContacts() {
        return contacts;
    }
    public void setContacts(Map<String, Contact> contacts) {
        this.contacts = contacts;
    }

    @DynamoDBAttribute(attributeName = DB_ATTR_ADDRESS)
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    /*/FIELD GETTERS AND SETTERS*/
    
    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
}
