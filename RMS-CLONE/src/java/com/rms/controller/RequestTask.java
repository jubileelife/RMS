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
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.TabChangeEvent;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "request_task")
@SessionScoped
public class RequestTask implements Serializable {

    private DbConnection dbc;
    private DbConnection dbc2;
    private Statement st;
    private Statement stt;
    private ResultSet rs;
    private ResultSet rss;
    private String sessionUserId;
    private String sessionUsername;
    private String sessionReportTo;
    private HttpSession session = null;
    private String errorMesg = "";

    private String taskCategory;
    private String taskTitle;
    private String taskDescription;
    private Date taskStartDate;
    private Date taskEndDate;
    private String taskAttachment;
    private String taskAssignedTo;
    public int taskTabIndex = 0;
    private String destination = "D:\\rms_upload\\";
    private Date assignmentStartDate;
    private Date assignmentEndDate;

    private List<DdValues> reqTaskCategory = new ArrayList<DdValues>();

    private List<reqTaskDtList> filtered;
    private List<reqTaskDtList> reqTaskDataTable = new ArrayList<reqTaskDtList>();
    private reqTaskDtList clickedItem;

    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public RequestTask() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");

        try {
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            String sql = "select id,category_name from SSR_REQUEST_CATEGORY ";
            rs = st.executeQuery(sql);
            reqTaskCategory.add(new DdValues("", "Select One"));
            while (rs.next()) {
                reqTaskCategory.add(new DdValues(rs.getString("id"), rs.getString("category_name")));
            }
        } catch (Exception e) {
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }

        requestAssignment rq = new requestAssignment();
        rq.updateAssignSelectFields();

    }

    public List<DdValues> getReqTaskCategory() {
        return reqTaskCategory;
    }

    public void setReqTaskCategory(List<DdValues> reqTaskCategory) {
        this.reqTaskCategory = reqTaskCategory;
    }

    public Date getAssignmentStartDate() {
        return assignmentStartDate;
    }

    public void setAssignmentStartDate(Date assignmentStartDate) {
        this.assignmentStartDate = assignmentStartDate;
    }

    public Date getAssignmentEndDate() {
        return assignmentEndDate;
    }

    public void setAssignmentEndDate(Date assignmentEndDate) {
        this.assignmentEndDate = assignmentEndDate;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public List<reqTaskDtList> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<reqTaskDtList> filtered) {
        this.filtered = filtered;
    }

    public reqTaskDtList getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(reqTaskDtList clickedItem) {
        this.clickedItem = clickedItem;
    }

    public List<reqTaskDtList> getReqTaskDataTable() {
        return reqTaskDataTable;
    }

    public void setReqTaskDataTable(List<reqTaskDtList> reqTaskDataTable) {
        this.reqTaskDataTable = reqTaskDataTable;
    }

    public int getTaskTabIndex() {
        return taskTabIndex;
    }

    public void setTaskTabIndex(int taskTabIndex) {
        this.taskTabIndex = taskTabIndex;
    }

    public String getTaskAssignedTo() {
        return taskAssignedTo;
    }

    public void setTaskAssignedTo(String taskAssignedTo) {
        this.taskAssignedTo = taskAssignedTo;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskEndDate() {
        return taskEndDate;
    }

    public void setTaskEndDate(Date taskEndDate) {
        this.taskEndDate = taskEndDate;
    }

    public String getTaskAttachment() {
        return taskAttachment;
    }

    public void setTaskAttachment(String taskAttachment) {
        this.taskAttachment = taskAttachment;
    }

    public String createTask(String reqID, String reqAssignId) {
        try {
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            reqID = request.getParameter("reqDetails:j_idt44:requestno");
//            reqAssignId = request.getParameter("reqDetails:j_idt44:reqAssign8");
            String maxValue = null;
            String LastID = "select nvl(max(SSR_TASK_ID),0)+1 as maxID from SSR_SERVICE_TASK";
            rs = st.executeQuery(LastID);
            while (rs.next()) {
                maxValue = rs.getString("maxID");
            }

            String url = taskAttachment;
            if (url == "") {
                url = "null";
            } else {
            }
            if ("".equals(reqAssignId) || reqAssignId == null) {
                errorMesg = "Please Create Assignment First.";
                return "";
            }
            if ("".equals(taskCategory) || "".equals(taskAssignedTo) || "".equals(taskStartDate) || "".equals(taskEndDate) || "".equals(taskTitle) || "".equals(taskDescription)) {
                errorMesg = "All fields are require.";
            } else {
                String sql = "Insert Into SSR_SERVICE_TASK(SSR_TASK_ID,USER_ID,ACCOUNT_DATE_1,TASK_CATEGORY,TASK_TITLE,TASK_DESCRIPTION,ASSIGNED_TO,START_TIME,END_TIME,ASSIGNED_BY,ASSIGNED_DATE,TASK_STATUS,ST_FILE_ATTACHMENT,REQUEST_ID,SERVICE_DESK_ID)\n"
                        + "values(" + maxValue + ",'" + sessionUsername + "','" + timeStamp + "','" + taskCategory + "','" + taskTitle + "','" + taskDescription + "','" + taskAssignedTo + "','" + dateFormate.format(taskStartDate) + "','" + dateFormate.format(taskEndDate) + "','" + sessionUsername + "','" + timeStamp + "','New','" + url + "','" + reqID + "','" + reqAssignId + "')";
                //System.out.println(sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogVarTAsk').show();");
                    errorMesg = null;
                    taskCategory = null;
                    taskTitle = null;
                    taskDescription = null;
                    taskStartDate = null;
                    taskEndDate = null;
                    taskAttachment = null;
                    taskAssignedTo = null;

                } else {
                    System.out.println("Error Occured");
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured in user creation", ""));
                }
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

    public void redirectTaskUrl() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("requestList.xhtml?faces-redirect=true");
        //setTaskTabIndex(3);
    }

    public void updateTabRest() {
        setTaskTabIndex(0);
    }

    public void redirectRequestTaskDetail(String reqID, String reqAssignId,String reqAssignName) {
        setTaskTabIndex(0);
        if (reqAssignId == null || reqAssignId.equals("") || reqID == null || reqID.equals("")) {
        } else {
            updateTaskDataTable(reqID, reqAssignId,reqAssignName);
        }
    }

    public final void onTabChange(final TabChangeEvent event) {
//        TabView tv = (TabView) event.getComponent();
//        this.taskTabIndex = tv.getActiveIndex();
//        if (this.taskTabIndex == 3) {
//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            String reqID = request.getParameter("reqDetails:requestno");
//            String reqAssignID = request.getParameter("reqDetails:reqAssign8");
//            if (reqAssignID == null || reqAssignID.equals("")) {
//                reqAssignID = "0";
//            }
//            System.out.println("Selected Tab = " + taskTabIndex + "------" + reqID + " = " + reqAssignID + "");
//            updateTaskDataTable(reqID, reqAssignID);
//            RequestContext context = RequestContext.getCurrentInstance();
//            context.update("reqDetails:j_idt44:reqTaskDetailTable");
//            setTaskTabIndex(0);
//            } else if (this.taskTabIndex == 1) {
////        System.out.println("Selected Tab = "+taskTabIndex+"------");
////        RequestDetails dt = new RequestDetails();
////        dt.updateReqDataTable("8","");
////        RequestContext context = RequestContext.getCurrentInstance();context.update("reqDetails:j_idt44:reqDetailTable");
//        }
    }

    public List<reqTaskDtList> updateTaskDataTable(String reqID, String reqAssignId,String reqAssignName) {
        int i = 0;
        int counts = 0;
        try {
            reqTaskDataTable.clear();
            reqTaskDataTable = new ArrayList<reqTaskDtList>();
            dbc = new DbConnection();
            dbc2 = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();
            stt = null;
            stt = dbc2.getConnection(false).createStatement();
            ResultSet rss = null;
            String sqle="";
            if(sessionUsername.equals(reqAssignName) || sessionUsername == reqAssignName ){
            String sql = "select count(*) as count from SSR_SERVICE_TASK where REQUEST_ID = " + reqID + " and SERVICE_DESK_ID = " + reqAssignId + " ";
            rss = st.executeQuery(sql);
            while (rss.next()) {
                counts = Integer.parseInt(rss.getString(1));
            }

            sqle = "select SSR_TASK_ID,TASK_CATEGORY,TASK_TITLE,TASK_DESCRIPTION,ASSIGNED_TO,to_char(START_TIME, 'dd-mm-yyyy HH12:mi:ss am' ),to_char(END_TIME, 'dd-mm-yyyy HH12:mi:ss am' ),ASSIGNED_BY,to_char(ASSIGNED_DATE, 'dd-mm-yyyy HH12:mi:ss am' ),TASK_STATUS,ST_FILE_ATTACHMENT,SERVICE_DESK_ID from SSR_SERVICE_TASK where request_id = " + reqID + " and SERVICE_DESK_ID = " + reqAssignId + " ORDER BY SSR_TASK_ID DESC ";
            }else{
            String sql = "select count(*) as count from SSR_SERVICE_TASK where REQUEST_ID = " + reqID + " and SERVICE_DESK_ID = " + reqAssignId + " ";
            rss = st.executeQuery(sql);
            while (rss.next()) {
                counts = Integer.parseInt(rss.getString(1));
            }

            sqle = "select SSR_TASK_ID,TASK_CATEGORY,TASK_TITLE,TASK_DESCRIPTION,ASSIGNED_TO,to_char(START_TIME, 'dd-mm-yyyy HH12:mi:ss am' ),to_char(END_TIME, 'dd-mm-yyyy HH12:mi:ss am' ),ASSIGNED_BY,to_char(ASSIGNED_DATE, 'dd-mm-yyyy HH12:mi:ss am' ),TASK_STATUS,ST_FILE_ATTACHMENT,SERVICE_DESK_ID from SSR_SERVICE_TASK where request_id = " + reqID + " and SERVICE_DESK_ID = " + reqAssignId + " and ASSIGNED_TO like '%"+sessionUsername+"%' ORDER BY SSR_TASK_ID DESC ";
            
            }
            ResultSet rs = null;
            rs = stt.executeQuery(sqle);
            boolean check = true;
            String mesg = "View Attachment";
            while (rs.next()) {

                if ("null".equals(rs.getString(11)) || "-".equals(rs.getString(11)) || "".equals(rs.getString(11))) {
                    check = true;
                    mesg = "No File";
                } else {
                    check = false;
                    mesg = "View Attachment";
                }

                reqTaskDataTable.add(i, new reqTaskDtList(counts, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getString(10), rs.getString(11), check, mesg));
                i++;
                counts--;
            }

        } catch (SQLException ex) {
            Logger.getLogger(requestAssignment.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dbc.freeConnection();
                st.close();
                stt.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }

        return reqTaskDataTable;
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
            OutputStream out = new FileOutputStream(new File(destination + randomfile + "." + extension));
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            in.close();
            out.flush();
            out.close();
            String url = destination + randomfile + "." + extension;
            setTaskAttachment(url);
            //session.setAttribute("fileurl", url);
            System.out.println("New file created!" + url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public class reqTaskDtList {

        private int serialNo;
        private String reqTaskId;
        private String reqTaskCat;
        private String reqTaskTitle;
        private String reqTaskDesc;
        private String reqTaskAssignTo;
        private String reqTaskStartDate;
        private String reqTaskEndDate;
        private String reqTaskAssignBy;
        private String reqTaskAssignDate;
        private String reqTaskStatus;
        private String reqTaskAttachment;
        private boolean check;
        private String mesg;

        public reqTaskDtList(int serialNo, String reqTaskId, String reqTaskCat, String reqTaskTitle, String reqTaskDesc, String reqTaskAssignTo, String reqTaskStartDate, String reqTaskEndDate, String reqTaskAssignBy, String reqTaskAssignDate, String reqTaskStatus, String reqTaskAttachment, boolean check, String mesg) {
            this.serialNo = serialNo;
            this.reqTaskId = reqTaskId;
            this.reqTaskCat = reqTaskCat;
            this.reqTaskTitle = reqTaskTitle;
            this.reqTaskDesc = reqTaskDesc;
            this.reqTaskAssignTo = reqTaskAssignTo;
            this.reqTaskStartDate = reqTaskStartDate;
            this.reqTaskEndDate = reqTaskEndDate;
            this.reqTaskAssignBy = reqTaskAssignBy;
            this.reqTaskAssignDate = reqTaskAssignDate;
            this.reqTaskStatus = reqTaskStatus;
            this.reqTaskAttachment = reqTaskAttachment;
            this.check = check;
            this.mesg = mesg;
        }

        public boolean getCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }

        public String getMesg() {
            return mesg;
        }

        public void setMesg(String mesg) {
            this.mesg = mesg;
        }

        public int getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(int serialNo) {
            this.serialNo = serialNo;
        }

        public String getReqTaskId() {
            return reqTaskId;
        }

        public void setReqTaskId(String reqTaskId) {
            this.reqTaskId = reqTaskId;
        }

        public String getReqTaskCat() {
            return reqTaskCat;
        }

        public void setReqTaskCat(String reqTaskCat) {
            this.reqTaskCat = reqTaskCat;
        }

        public String getReqTaskTitle() {
            return reqTaskTitle;
        }

        public void setReqTaskTitle(String reqTaskTitle) {
            this.reqTaskTitle = reqTaskTitle;
        }

        public String getReqTaskDesc() {
            return reqTaskDesc;
        }

        public void setReqTaskDesc(String reqTaskDesc) {
            this.reqTaskDesc = reqTaskDesc;
        }

        public String getReqTaskAssignTo() {
            return reqTaskAssignTo;
        }

        public void setReqTaskAssignTo(String reqTaskAssignTo) {
            this.reqTaskAssignTo = reqTaskAssignTo;
        }

        public String getReqTaskStartDate() {
            return reqTaskStartDate;
        }

        public void setReqTaskStartDate(String reqTaskStartDate) {
            this.reqTaskStartDate = reqTaskStartDate;
        }

        public String getReqTaskEndDate() {
            return reqTaskEndDate;
        }

        public void setReqTaskEndDate(String reqTaskEndDate) {
            this.reqTaskEndDate = reqTaskEndDate;
        }

        public String getReqTaskAssignBy() {
            return reqTaskAssignBy;
        }

        public void setReqTaskAssignBy(String reqTaskAssignBy) {
            this.reqTaskAssignBy = reqTaskAssignBy;
        }

        public String getReqTaskAssignDate() {
            return reqTaskAssignDate;
        }

        public void setReqTaskAssignDate(String reqTaskAssignDate) {
            this.reqTaskAssignDate = reqTaskAssignDate;
        }

        public String getReqTaskStatus() {
            return reqTaskStatus;
        }

        public void setReqTaskStatus(String reqTaskStatus) {
            this.reqTaskStatus = reqTaskStatus;
        }

        public String getReqTaskAttachment() {
            return reqTaskAttachment;
        }

        public void setReqTaskAttachment(String reqTaskAttachment) {
            this.reqTaskAttachment = reqTaskAttachment;
        }

    }

}
