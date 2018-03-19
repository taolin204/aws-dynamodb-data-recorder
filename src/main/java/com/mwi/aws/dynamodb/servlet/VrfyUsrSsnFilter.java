/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mwi.aws.dynamodb.servlet;


import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mwi.aws.dynamodb.AppConstants;
import com.mwi.aws.dynamodb.ApplicationBean;
import com.mwi.aws.dynamodb.SessionBean;

/**
 *
 * @author Gurushanth Murthy
 */
public class VrfyUsrSsnFilter implements Filter {

    private static final String AJAX_REDIRECT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
            + "<partial-response><redirect url=\"%s\"></redirect></partial-response>";

    public VrfyUsrSsnFilter() {
    }

    private boolean isAJAXRequest(HttpServletRequest request) {
        boolean check = false;

//        String bAjxRequest = request.getHeader("com.sun.faces.avatar.Partial");
        String bAjxRequest = request.getHeader("Faces-Request");
        String facesAjxRequested = request.getHeader("X-Requested-With");
        if (bAjxRequest != null && bAjxRequest.equals("partial/ajax") && facesAjxRequested.equals("XMLHttpRequest")) {
            check = true;
        }
        return check;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        Throwable problem = null;
        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;
        String CONTEXT_PATH = httpReq.getContextPath();
        //else process the request
        ApplicationBean applicationBean = (ApplicationBean) request.getServletContext().getAttribute("applicationBean");
        try {
            HttpSession session = (HttpSession) httpReq.getSession();
            boolean isLggdIn = session == null ? false : (session.getAttribute("validSession") == null ? false : Boolean.parseBoolean(session.getAttribute("validSession").toString()));
            if (isLggdIn) {
                //check for access privilege
                SessionBean sb1 = (SessionBean) session.getAttribute("sessionBean1");
//                if (sb1.getAccess(httpReq.getRequestURI().replaceFirst(CONTEXT_PATH, ""), AppConstants.MODE_VIEW)) {
                //process the request
                chain.doFilter(request, response);
//                } else {
//                    //Redirect to No privilage page
//                    httpRes.sendRedirect(CONTEXT_PATH + "/faces/NoPrivilege.jsp");
//                }
            } else if (isAJAXRequest(httpReq)) {
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().printf(AJAX_REDIRECT_XML, CONTEXT_PATH + AppConstants.URL_LOGIN); // So, return special XML response instructing JSF ajax to send a redirect.
                applicationBean.getUserSessionMap().remove(session.getId());
            } else {
                //Redirect to Login Page
                httpRes.sendRedirect(CONTEXT_PATH + AppConstants.URL_LOGIN);
                applicationBean.getUserSessionMap().remove(session.getId());
            }
        } catch (Throwable t) {
            //redirect to error Page
            httpRes.sendRedirect(((HttpServletRequest) request).getContextPath() + "/faces/ErrorPage.xhtml");
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }

        }
    }

    /**
     * Destroy method for this filter
     */
    @Override
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
    }
}
