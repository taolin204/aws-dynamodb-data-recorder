/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwi.aws.dynamodb;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.awt.geom.Point2D;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author Gurushanth
 */
@ManagedBean(name = "applicationBean", eager = true)
@ApplicationScoped
public class ApplicationBean {
	/**
	 * MAX value for event ID to reset.
	 */
	public static int EVENT_ID_MAX_VALUE = 65535;

	/** Webclient user session bean Map. */
	private Map<String, SessionBean> userSessionMap;
	/** Google GSON object for JSON conversion. */
	private Gson gson = new Gson();
	/**
	 * Properties file.
	 */
	public Properties properties;

	@PostConstruct
	public void init() {
		// Do stuff during webapp's startup.
		userSessionMap = new HashMap<>();
		
		// Load properties file
		loadProperites();
		// initialize JMS connection
		// init all data from datamanger
		initAllData();
	}

	@PreDestroy
	public void destroy() {
		// Do stuff during webapp's shutdown.

	}

	public void loadProperites() {
		try {
			InputStream fis = null;
//			// initialize properties
//			properties = new Properties();
//			// Read properties file path
//			String propertiesFile = System.getProperty("propertiesFile", AppConstants.APP_PROPERTIES_FILE);
//			fis = new FileInputStream(propertiesFile);
//			// load a properties file
//			properties.load(fis);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	/**
	 * Initial all sample data.
	 */
	private void initAllData() {

	}

	/**
	 * @return the userSessionMap
	 */
	public Map<String, SessionBean> getUserSessionMap() {
		return userSessionMap;
	}

	/**
	 * @param userSessionMap
	 *            the userSessionMap to set
	 */
	public void setUserSessionMap(Map<String, SessionBean> userSessionMap) {
		this.userSessionMap = userSessionMap;
	}

}
