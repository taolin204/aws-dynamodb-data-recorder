package com.mwi.aws.dynamodb.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SystemConfig {

	private static SystemConfig instance;
    private static Properties props ;
    
    public static SystemConfig getInstance() {
    	if(instance == null) {
    		instance = new SystemConfig();
    	}
    	return instance;
    }

    public SystemConfig(){

    	InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");     
    	props = new Properties();     
    	try {     
    		props.load(inputStream);     
    	} catch (IOException e1) {     
    		e1.printStackTrace();     
    	}     
    }


    
    public static String getPropertyString(String key){
    	props = SystemConfig.getInstance().getProperties();
    	return props == null ? null :  props.getProperty(key);

    }

    public static String getProperty(String key){
    	String result = getPropertyString(key);
    	return result;

    }
    
    
    public static String getProperty(String key,String defaultValue){
    	String result = getPropertyString(key);
    	if(result == null) {
    		result = defaultValue;
    	}
         return result;

    }

    
    public static Properties getProperties(){
        return props;
    }

}
