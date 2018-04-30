/**
 * Copyright (c) 2017 MWI Pte. Ltd. All Rights Reserved.
 *
 * Revision 1.0 25/10/2017 Sun Yong
 * - Creation for PMS change
 */
package com.mwi.aws.dynamodb.model;

import java.util.Date;

import com.mwi.aws.dynamodb.util.DebugUtil;

/**
 * Representation of a contact.
 */
public class ContactTest {
	
	public static void main(String[] args) {
		
		Date warrrantyEndDate = new Date("01/01/2017");
		Date dateNow = new Date();
		//DebugUtil.log("*********  warrantyEndDate " + warrrantyEndDate + ", DateNow " + dateNow) ;
		if(dateNow.before(warrrantyEndDate)) {
			DebugUtil.log("before");
		} else {
			DebugUtil.log("after");
		}
		
		
		warrrantyEndDate = new Date("01/01/2019");
		dateNow = new Date();
		//DebugUtil.log("*********  warrantyEndDate " + warrrantyEndDate + ", DateNow " + dateNow) ;
		if(dateNow.before(warrrantyEndDate)) {
			DebugUtil.log("before");
		} else {
			DebugUtil.log("after");
		}
		
		
//		Contact contact = new Contact();
//		contact.setContactName("taolin");
//		contact.setEmail("taolin@qq.com");
//		contact.setPhoneNumber("12345");
//		
//		System.out.println(contact.toJSON());
	}
    /*/FIELD GETTERS AND SETTERS*/
}
