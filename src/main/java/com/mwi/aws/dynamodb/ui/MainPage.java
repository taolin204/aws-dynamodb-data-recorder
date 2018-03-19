/**
 * Copyright (c) 2018 MWI Pte. Ltd. All Rights Reserved.
 *
 * Revision 1.0 29/01/2018  Gurushanth
 * - Creation
 *
 */

package com.mwi.aws.dynamodb.ui;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.facelets.FaceletContext;
import org.primefaces.context.RequestContext;

import com.mwi.aws.dynamodb.ApplicationBean;
import com.mwi.aws.dynamodb.SessionBean;

/**
 * TODO: Enter short description
 *
 */
@ManagedBean(name = "mainPageView", eager = true)
@RequestScoped
public class MainPage {

    private String eventType;

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    @ManagedProperty(value = "#{applicationBean}")
    private ApplicationBean applicationBean;

    /**
     * Initial components
     */
    @PostConstruct
    public void init(){
//        applicationBean.loadAllPicLocations();
    }


    public void openToolTip(ActionEvent event ) {
        Map<String,Object> options = new HashMap<String, Object>();
        options.put("modal", true);
//        PrimeFaces.current().dialog().openDynamic("level1", options, null);
    }

    public void refreshDetails(){
//        //update
//        if(eventType.equals("Event")){
//            RequestContext.getCurrentInstance().update("mainpageForm:eventTable");
//        }else if(eventType.equals("Workorder")){
//            RequestContext.getCurrentInstance().update("mainpageForm:workOrderTable");
//        }
    }

    /**
     * @return the eventType
     */
    public String getEventType() {
        return eventType;
    }

    /**
     * @param eventType the eventType to set
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * @param sessionBean the sessionBean to set
     */
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
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

}
