/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import com.rms.dbconnection.Adder;
import com.rms.dbconnection.DbConnection;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "req_init")
@SessionScoped
public class RequestInitiate implements Serializable {

    private List<DdValues> reqTypeValues = new ArrayList<DdValues>();
    private List<DdValues> reqSystemValues = new ArrayList<DdValues>();
    private List<DdValues> reqSystemModuleValues = new ArrayList<DdValues>();
    private String reqTypeSelectValues;
    private String reqSystemSelectValues;
    private String reqSystemModuleSelectValues;
    private DbConnection dbc;
    private DbConnection dbc2;
    private Statement st;
    private Statement stt;
    private ResultSet rs;
    private String destination = "D:\\rms_upload\\";
    private String reqPriority;
    private String reqTitle;
    private String reqDescription;
    private HttpSession session = null;
    private String sessionUserId;
    private String sessionUsername;
    private String sessionReportTo;
    private boolean check = false;
    private boolean selectEditable = false;

    private String newRequestId;

    RequestContext contextLoading = RequestContext.getCurrentInstance();

    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public RequestInitiate() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");
        updateSelectFields();
    }

    public String getNewRequestId() {
        return newRequestId;
    }

    public void setNewRequestId(String newRequestId) {
        this.newRequestId = newRequestId;
    }

    public boolean isSelectEditable() {
        return selectEditable;
    }

    public void setSelectEditable(boolean selectEditable) {
        this.selectEditable = selectEditable;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getReqPriority() {
        return reqPriority;
    }

    public void setReqPriority(String reqPriority) {
        this.reqPriority = reqPriority;
    }

    public String getReqTitle() {
        return reqTitle;
    }

    public void setReqTitle(String reqTitle) {
        this.reqTitle = reqTitle;
    }

    public String getReqDescription() {
        return reqDescription;
    }

    public void setReqDescription(String reqDescription) {
        this.reqDescription = reqDescription;
    }

    public List<DdValues> getReqSystemModuleValues() {
        return reqSystemModuleValues;
    }

    public void setReqSystemModuleValues(List<DdValues> reqSystemModuleValues) {
        this.reqSystemModuleValues = reqSystemModuleValues;
    }

    public String getReqSystemModuleSelectValues() {
        return reqSystemModuleSelectValues;
    }

    public void setReqSystemModuleSelectValues(String reqSystemModuleSelectValues) {
        this.reqSystemModuleSelectValues = reqSystemModuleSelectValues;
    }

    public void reqSystemModuleChangeEvent(ValueChangeEvent valueCE) {
        //FacesContext.getCurrentInstance().renderResponse();
        reqSystemModuleSelectValues = valueCE.getNewValue().toString();
    }

    public String getReqSystemSelectValues() {
        return reqSystemSelectValues;
    }

    public void setReqSystemSelectValues(String reqSystemSelectValues) {
        this.reqSystemSelectValues = reqSystemSelectValues;
    }

    public List<DdValues> getReqSystemValues() {
        return reqSystemValues;
    }

    public void setReqSystemValues(List<DdValues> reqSystemValues) {
        this.reqSystemValues = reqSystemValues;
    }

    public void reqSystemChangeEvent(ValueChangeEvent valueCE) {
        reqSystemSelectValues = valueCE.getNewValue().toString();
        if ("3".equals(valueCE.getNewValue().toString()) || "4".equals(valueCE.getNewValue().toString())) {
            selectEditable = true;
        } else {
            selectEditable = false;
        }

        moduleSelectFields(valueCE.getNewValue().toString());

    }

    public List<DdValues> getReqTypeValues() {
        return reqTypeValues;
    }

    public void setReqTypeValues(List<DdValues> reqTypeValues) {
        this.reqTypeValues = reqTypeValues;
    }

    public String getReqTypeSelectValues() {
        return reqTypeSelectValues;
    }

    public void setReqTypeSelectValues(String reqTypeSelectValues) {
        this.reqTypeSelectValues = reqTypeSelectValues;
    }

    public void reqTypeChangeEvent(ValueChangeEvent valueCE) {
        reqTypeSelectValues = valueCE.getNewValue().toString();
    }

    public String creatRequest() {
        try {

            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String reqDate = request.getParameter("reqform:reqdate");

            if ("3".equals(reqSystemSelectValues) || "4".equals(reqSystemSelectValues)) {

                String moduleMax = null;
                String LastIDD = "select nvl(max(id),0)+1 as maxID from SSR_SYSTEM_MODULE";
                rs = st.executeQuery(LastIDD);
                while (rs.next()) {
                    moduleMax = rs.getString("maxID");
                }

                String sql = "Insert Into SSR_SYSTEM_MODULE(id,MODULE_NAME,MAIN_SYSTEM_ID)\n"
                        + "values(" + moduleMax + ",'" + reqSystemModuleSelectValues + "'," + reqSystemSelectValues + ")";
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                reqSystemModuleSelectValues = moduleMax;
            }

            String email;
            String subject = "Complaint System-User Password";
            String text = "Dear Concern, Your User has been Created. Your User id and Password are given below \n\n\n\n\n"
                    + "User Name:   " + sessionUserId + "\n\n\n"
                    + "Password:    " + sessionUsername + "\n\n\n";

            String maxValue = null;
            String LastID = "select nvl(max(request_id),0)+1 as maxID from SSR_REQUEST_INITIATE";
            rs = st.executeQuery(LastID);
            while (rs.next()) {
                maxValue = rs.getString("maxID");
            }

            int Approved;
            if ("2".equals(reqTypeSelectValues) || "3".equals(reqTypeSelectValues)) {
                Approved = 1;
            } else {
                Approved = 0;
            }

            if (sessionUserId != null) {
                String url = (String) session.getAttribute("fileurl");
                if (url == "") {
                    url = "null";
                } else {
                }
                String sql = "Insert Into SSR_REQUEST_INITIATE(REQUEST_ID,REQUESTER,REQUEST_DATE,REQUEST_TYPE,SYSTEM_ID,SYSTEM_MODULE,REQUEST_TITLE,REQUEST_DESCRIPTION,REQUEST_PRIORITY,REQUEST_FILE_ATTACHED,reporting_to,APPROVAL_REQUIRED,REQUEST_STATUS)\n"
                        + "values(" + maxValue + ",'" + sessionUserId + "','" + timeStamp + "'," + reqTypeSelectValues + ",'" + reqSystemSelectValues + "','" + reqSystemModuleSelectValues + "','" + reqTitle + "','" + reqDescription + "','" + reqPriority + "','" + url + "'," + sessionReportTo + "," + Approved + ",'1')";
                //System.out.println(sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    newRequestId = maxValue;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogVar').show();");
                } else {
                    System.out.println("Error Occured");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured in user creation", ""));
                }
            } else {
                FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml?faces-redirect=true");
            }

        } catch (Exception e) {
            System.out.println("Exception in Insertion: " + e);
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }
        return null;
    }

    public void redirectUrl() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("search_request.xhtml?faces-redirect=true");
        reqTypeSelectValues = null;
        reqSystemSelectValues = null;
        reqSystemModuleSelectValues = null;
        reqTitle = null;
        reqDescription = null;
        reqPriority = null;
    }

    public void updateSelectFields() {
        try {

            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            String sql = "select id,request_type from SSR_REQUEST_TYPE";
            rs = st.executeQuery(sql);
            reqTypeValues.add(new DdValues("", "Select One"));
            while (rs.next()) {
                reqTypeValues.add(new DdValues(rs.getString("id"), rs.getString("request_type")));
            }

            sql = "select id,name from SSR_SYSTEMS";
            rs = st.executeQuery(sql);
            reqSystemValues.add(new DdValues("", "Select One"));
            while (rs.next()) {
                reqSystemValues.add(new DdValues(rs.getString("id"), rs.getString("name")));
            }
        } catch (Exception sqe) {
            System.out.println("---------------" + sqe.getMessage());
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }

    }

    public void moduleSelectFields(String id) {
        try {
            int i = 0;
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            reqSystemModuleValues.clear();
            String sql = "select id,module_name from SSR_SYSTEM_MODULE where main_system_id = " + id + " ";
            rs = st.executeQuery(sql);
            if ("3".equals(id) || "4".equals(id)) {
                reqSystemModuleValues.add(i, new DdValues("", "Please enter your module here. "));
            } else {
                reqSystemModuleValues.add(i, new DdValues("", "Select One "));
            }
            i++;
            while (rs.next()) {
                reqSystemModuleValues.add(i, new DdValues(rs.getString("id"), rs.getString("module_name")));
                i++;
            }

        } catch (Exception sqe) {
            System.out.println("---------------a" + sqe.getMessage());
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }
    }

    public void upload(FileUploadEvent event) {
        // Do what you want with the file        
        try {
            //System.out.print("-------------"+event.getFile().getFileName());
            copyFile(event.getFile().getFileName(), event.getFile().getInputstream());
            FacesMessage msg = new FacesMessage("Success! ", event.getFile().getFileName() + " is uploaded.");
            FacesContext.getCurrentInstance().addMessage(null, msg);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void copyFile(String fileName, InputStream in) {
        try {
            //fileName.substring(fileName.lastIndexOf("\\"))
            // write the inputStream to a FileOutputStream
            try {
                fileName = fileName.substring(fileName.lastIndexOf("\\"));
            } catch (Exception e) {
            }

            String extension = "";

            int i = fileName.lastIndexOf('.');
            if (i >= 0) {
                extension = fileName.substring(i + 1);
            }

            String randomfile = new SimpleDateFormat("dd-MMM-yyyy-hh-mm-ss").format(Calendar.getInstance().getTime());
            OutputStream out = new FileOutputStream(new File(destination + randomfile+"."+extension));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            String url = destination + randomfile+"."+extension;
            session.setAttribute("fileurl", url);
            System.out.println("New file created!" + url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
