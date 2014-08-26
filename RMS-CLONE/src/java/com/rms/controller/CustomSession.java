/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "custom_session")
@SessionScoped
public class CustomSession implements Serializable {

    private HttpSession session = null;
    private String val;

    public String getSessionName() {
        return getSessionValues();
    }

    public void setSessionName(String val) {
        this.val = val;
    }

    public String getSessionValues() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        String sessionvalue = (String) session.getAttribute("userid");
        String session_role = (String) session.getAttribute("username");

        return "Welcome, " + session_role + " ";

    }

}
