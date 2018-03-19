/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mwi.aws.dynamodb.ui;

//import com.mwi.cad.web.jms.CADServiceManager;
//import com.mwi.cad.client.util.MwiResourceMap;
//import com.mwi.cad.systemmgr.message.LoginOutput;
//import com.mwi.cad.systemmgr.message.SystemMgrConstant;
//import com.mwi.cad.web.config.ApplicationBeanConfig;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;

import com.mwi.aws.dynamodb.SessionBean;

/**
 *
 * @author Naveen S
 */

@ManagedBean(name = "loginView", eager = true)
@RequestScoped
public class Login {
    private String userName;
    private String passWord;
//    @ManagedProperty(value = "#{applicationBeanConfig}")
//    private ApplicationBeanConfig applicationBeanConfig;

    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;

    @PostConstruct
    public void init() {
        try {
            if (sessionBean.isIsLoggedIn()) {
                sessionBean.redirect("/datarecorder/faces/DataRecorder.xhtml");
            }
        } catch (Exception e) {
            //if error occurs due to session time out do nothing
        }
    }
    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the passWord
     */
    public String getPassWord() {
        return passWord;
    }

    /**
     * @param passWord the passWord to set
     */
    public void setPassWord(String passWord) {
        this.passWord = passWord;
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

    public String login() {
        String str = null;
        if(sessionBean.login(userName, passWord)){
            str = "DataRecorder";
        }else{
//        LoginOutput output = CADServiceManager.getInstance().getJmsSystemServiceProvider().login(userName, passWord, "Client1");
//        if (output == null) {
//            FacesContext.getCurrentInstance().addMessage("loginForm:messages1", new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", "Server Communication Error!"));
//        } else if (output.getLoginResult() == SystemMgrConstant.E_LOGIN_FAIL_INVALID_USER) {
//            FacesContext.getCurrentInstance().addMessage("loginForm:messages1", new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", MwiResourceMap.getResourceStr("error.login.invalid_user_pwd", "Invaild password")));
//        } else if (output.getLoginResult() == SystemMgrConstant.E_LOGIN_FAIL_INVALID_PWD) {
//            FacesContext.getCurrentInstance().addMessage("loginForm:messages1", new FacesMessage(FacesMessage.SEVERITY_WARN, "WARN", MwiResourceMap.getResourceStr("error.login.invalid_user_pwd", "Invaild password")));
//        } else if (output.getLoginResult() == SystemMgrConstant.E_LOGIN_SUCCEED) {
//            str = "radioDisp";
//        }
        }
        return str;
    }

//    /**
//     * @return the applicationBeanConfig
//     */
//    public ApplicationBeanConfig getApplicationBeanConfig() {
//        return applicationBeanConfig;
//    }
//
//    /**
//     * @param applicationBeanConfig the applicationBeanConfig to set
//     */
//    public void setApplicationBeanConfig(ApplicationBeanConfig applicationBeanConfig) {
//        this.applicationBeanConfig = applicationBeanConfig;
//    }
}
