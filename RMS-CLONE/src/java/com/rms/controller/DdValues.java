/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import javax.faces.bean.ManagedBean;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "DdValues")
public class DdValues {

    private String ddValues;
    private String ddString;
    private String ddStatus;

    public DdValues(String ddValues, String ddString) {
        this.ddValues = ddValues;
        this.ddString = ddString;
    }

    public String getDdValues() {
        return ddValues;
    }

    public void setDdValues(String ddValues) {
        this.ddValues = ddValues;
    }

    public String getDdString() {
        return ddString;
    }

    public void setDdString(String ddString) {
        this.ddString = ddString;
    }

    public String getDdStatus() {
        return ddStatus;
    }

    public void setDdStatus(String ddStatus) {
        this.ddStatus = ddStatus;
    }

}
