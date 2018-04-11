/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwi.aws.dynamodb;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mwi.aws.dynamodb.service.AwsService;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.push.EventBus;
import org.primefaces.push.EventBusFactory;

/**
 *
 * @author Gurushanth
 */
@ManagedBean(name = "sessionBean", eager = true)
@SessionScoped
public class SessionBean {

    /**
     * Username.
     */
    private String userName;

    /**
     * Password.
     */
    private boolean isLoggedIn;


    @ManagedProperty(value = "#{applicationBean}")
    private ApplicationBean applicationBean;

     private Gson gson = new Gson();
 /**
     * Primeface event bus publish.
     */
//    EventBus eventBus;

    @PostConstruct
    public void init(){
         //init event bus
//        eventBus = EventBusFactory.getDefault().eventBus();
    }

    /**
     * @return the isLoggedIn
     */
    public boolean isIsLoggedIn() {
        return isLoggedIn;
    }

    /**
     * @param isLoggedIn the isLoggedIn to set
     */
    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    /**
     * @return the applicationBean
     */
    public ApplicationBean getApplicationBean() {
        return applicationBean;
    }

    /**
     * @param applicationBean the applicationBean to set
     */
    public void setApplicationBean(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
    }

   public boolean isLoggedIn() {
         return isLoggedIn;
    }
    public boolean isAdminMenu() {
        return isLoggedIn;
    }
    /**
     * Redirect request to url
     * @param url
     */
    public void redirect(String url) {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (Exception e) {
            //tomcat log
            e.printStackTrace();
        }

    }
    
    /**
     * Verify user login.
     * @param userName
     * @param password
     * @return
     */
    public boolean login(String userName, String password) {
        boolean success = AwsService.getInstance().validateUserLogin(userName);
        if(success) {
	        this.userName = userName;
	        isLoggedIn = true;
	        //reload menu page
	        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
	//                        session.setAttribute("menuReload", "Y");
	        session.setAttribute("validSession", true);
	        applicationBean.getUserSessionMap().put(session.getId(), this);
        }
        return success;
    }

    /** user logout action
     * @return
     */
    public boolean logout(){
         boolean success = true;
        isLoggedIn = false;
        //remove all the sessionbeans from the session
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
            Enumeration enumSessions = session.getAttributeNames();
            while (enumSessions.hasMoreElements()) {
                String name = (String) enumSessions.nextElement();
                if (name.indexOf("sessionBean1") != -1
                        && name.contains("sessionBean")) {
                    session.removeAttribute(name);
                }
            }
//            session.invalidate();
            //reload menu page
            session.setAttribute("menuReload", "Y");
            session.setAttribute("validSession", false);
            applicationBean.getUserSessionMap().remove(session.getId());
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, AppConstants.ALERT_SUCCESS, "Logout Successfuly"));
            redirect(((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getContextPath() + AppConstants.URL_LOGIN);
        return success;
    }


}
