/**
 * Copyright (c) 2017 MWI Pte. Ltd. All Rights Reserved.
 *
 * Revision 1.0 25/10/2017 Sun Yong
 * - Creation for PMS change
 */
package com.mwi.aws.dynamodb.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.json.simple.JSONObject;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.mwi.aws.dynamodb.util.DynamoDBCustomUtils;

/**
 * Representation of a contact.
 */
@DynamoDBDocument
public class Contact {
    //DYNAMO DB ATTRIBUTE NAMES
    public static final String DB_ATTR_CONTACT_NAME = "contactName";
    public static final String DB_ATTR_EMAIL = "email"; 
    public static final String DB_ATTR_PHONE_NUMBER = "phoneNumber"; 
    
    /** The name of contact. */
    private String contactName;
    /** email of the contact. */
    private String email;
    /** phone number of the contact. */
    private String phoneNumber;
    
    /**
     * Convert object to JSON object.
     * 
     * @return JSON object
     */
    public JSONObject toJSON() {
        return DynamoDBCustomUtils.toJSON(this);
    }
    
    public Object getKey() {
    	return contactName;
    }
    
    /*FIELD GETTERS AND SETTERS*/
    @DynamoDBAttribute(attributeName = DB_ATTR_CONTACT_NAME)
    public String getContactName() {
        return contactName;
    }
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
    
    @DynamoDBAttribute(attributeName = DB_ATTR_EMAIL)
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = DB_ATTR_PHONE_NUMBER)
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    /*/FIELD GETTERS AND SETTERS*/
    @Override 
    public String toString() { 
            return ReflectionToStringBuilder.toString(this); 
    }
}
