/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import com.rms.dbconnection.Adder;
import com.rms.dbconnection.DbConnection;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "update_task")
@SessionScoped
public class UpdateTask implements Serializable {

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

    private String taskReqId;
    private String taskReqAsignId;
    private String taskReqTaskId;
    private String taskReqTaskName;
    private String taskReqTaskStatus;
    private String taskReqTaskDesc;
    private String taskReqTaskUpdateBy;
    private String taskReqTaskUpdateDate;

    private List<assignmentTaskUpdateDdList> filtered;
    private List<assignmentTaskUpdateDdList> tableData = new ArrayList<assignmentTaskUpdateDdList>();
    private assignmentTaskUpdateDdList clickedItem;

    private List<requestCounterByStatus> filteredStatus;
    private List<requestCounterByStatus> tableDataStatus = new ArrayList<requestCounterByStatus>();
    private requestCounterByStatus clickedItemStatus;

    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public UpdateTask() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");

       //updateTaskStatusUpdateDataTable();
    }

    public String getTaskReqTaskName() {
        return taskReqTaskName;
    }

    public void setTaskReqTaskName(String taskReqTaskName) {
        this.taskReqTaskName = taskReqTaskName;
    }
    
    public List<requestCounterByStatus> getFilteredStatus() {
        return filteredStatus;
    }

    public void setFilteredStatus(List<requestCounterByStatus> filteredStatus) {
        this.filteredStatus = filteredStatus;
    }

    public List<requestCounterByStatus> getTableDataStatus() {
        return tableDataStatus;
    }

    public void setTableDataStatus(List<requestCounterByStatus> tableDataStatus) {
        this.tableDataStatus = tableDataStatus;
    }

    public requestCounterByStatus getClickedItemStatus() {
        return clickedItemStatus;
    }

    public void setClickedItemStatus(requestCounterByStatus clickedItemStatus) {
        this.clickedItemStatus = clickedItemStatus;
    }

    public List<assignmentTaskUpdateDdList> getTableData() {
        return tableData;
    }

    public void setTableData(List<assignmentTaskUpdateDdList> tableData) {
        this.tableData = tableData;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public String getTaskReqTaskStatus() {
        return taskReqTaskStatus;
    }

    public void setTaskReqTaskStatus(String taskReqTaskStatus) {
        this.taskReqTaskStatus = taskReqTaskStatus;
    }

    public String getTaskReqTaskDesc() {
        return taskReqTaskDesc;
    }

    public void setTaskReqTaskDesc(String taskReqTaskDesc) {
        this.taskReqTaskDesc = taskReqTaskDesc;
    }

    public String getTaskReqTaskUpdateBy() {
        return taskReqTaskUpdateBy;
    }

    public void setTaskReqTaskUpdateBy(String taskReqTaskUpdateBy) {
        this.taskReqTaskUpdateBy = taskReqTaskUpdateBy;
    }

    public String getTaskReqTaskUpdateDate() {
        return taskReqTaskUpdateDate;
    }

    public void setTaskReqTaskUpdateDate(String taskReqTaskUpdateDate) {
        this.taskReqTaskUpdateDate = taskReqTaskUpdateDate;
    }

    public List<assignmentTaskUpdateDdList> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<assignmentTaskUpdateDdList> filtered) {
        this.filtered = filtered;
    }

    public List<assignmentTaskUpdateDdList> getReqTaskDataTable() {
        return tableData;
    }

    public void setReqTaskDataTable(List<assignmentTaskUpdateDdList> tableData) {
        this.tableData = tableData;
    }

    public assignmentTaskUpdateDdList getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(assignmentTaskUpdateDdList clickedItem) {
        this.clickedItem = clickedItem;
    }

    public String getTaskReqId() {
        return taskReqId;
    }

    public void setTaskReqId(String taskReqId) {
        this.taskReqId = taskReqId;
    }

    public String getTaskReqAsignId() {
        return taskReqAsignId;
    }

    public void setTaskReqAsignId(String taskReqAsignId) {
        this.taskReqAsignId = taskReqAsignId;
    }

    public String getTaskReqTaskId() {
        return taskReqTaskId;
    }

    public void setTaskReqTaskId(String taskReqTaskId) {
        this.taskReqTaskId = taskReqTaskId;
    }

    public String onUpdateAssignmentTask(String reqid, String assignid, String taskid,String taskName) {

        setTaskReqId(reqid);
        setTaskReqAsignId(assignid);
        setTaskReqTaskId(taskid);
        setTaskReqTaskName(taskName);
        updateAssignTaskUpdateDataTable();

        return "taskUpdate.xhtml?faces-redirect=true";

    }

    public List<assignmentTaskUpdateDdList> updateAssignTaskUpdateDataTable() {
        int i = 0;
        try {
            tableData.clear();
            tableData = new ArrayList<assignmentTaskUpdateDdList>();
            dbc = new DbConnection();
            dbc2 = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();
            stt = null;
            stt = dbc2.getConnection(false).createStatement();
            String sql = null;

            int maxValue = 0;
            String LastID = "select count(*) as maxID from SSR_TASK_HISTORY where request_id = " + taskReqId + " and ASSIGNMENT_ID = " + taskReqAsignId + " and TASK_ID = " + taskReqTaskId + " ";
            rs = st.executeQuery(LastID);
            while (rs.next()) {
                maxValue = Integer.parseInt(rs.getString("maxID"));
            }

            sql = "select REQUEST_ID,ASSIGNMENT_ID,TASK_ID,(select status from SSR_REQUEST_STATUS a where a.id = v.STATUS ),DESCRIPTION,UPDATE_BY,UPDATE_DATE from SSR_TASK_HISTORY v where request_id = " + taskReqId + " and ASSIGNMENT_ID = " + taskReqAsignId + " and TASK_ID = " + taskReqTaskId + " order by TASK_HISTORY_ID desc ";
            //System.out.println(sql);
            ResultSet rs = null;
            rs = stt.executeQuery(sql);
            while (rs.next()) {
                tableData.add(i, new assignmentTaskUpdateDdList(maxValue, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)));
                i++;
                maxValue--;
            }

        } catch (SQLException e) {
            System.out.println("error in record retrieving: " + e);
        } finally {
            try {
                dbc.freeConnection();
                dbc2.freeConnection();
                st.close();
                stt.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }

        return tableData;
    }

    public List<requestCounterByStatus> updateTaskStatusUpdateDataTable() {
        int i = 0;
        try {
            tableDataStatus = new ArrayList<requestCounterByStatus>();
            dbc = new DbConnection();
            dbc2 = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();
            stt = null;
            stt = dbc2.getConnection(false).createStatement();

            ResultSet rss = null;

            String sql = null;
            int maxValue = 1;

            sql = "select ID,STATUS count from SSR_REQUEST_STATUS ";
            ResultSet rs = null;
            rss = st.executeQuery(sql);

            while (rss.next()) {

                String sqll = "select count(*) as count from SSR_REQUEST_INITIATE where REQUESTER = '" + sessionUserId + "' and request_status = " + rss.getString(1) + " ";
                rs = stt.executeQuery(sqll);
                while (rs.next()) {
                    tableDataStatus.add(i, new requestCounterByStatus(maxValue, rss.getString(1), rss.getString(2), Integer.parseInt(rs.getString(1))));
                    maxValue++;
                    i++;
                }

            }

        } catch (SQLException e) {
            System.out.println("error in record retrieving: " + e);
        } finally {
            try {
                dbc.freeConnection();
                dbc2.freeConnection();
                st.close();
                stt.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }

        return tableDataStatus;
    }

    public String updateAssignmentTask() {
        try {
            if ("".equals(taskReqTaskDesc) || "".equals(taskReqTaskStatus)) {
                errorMesg = "Please Enter Status and Description.";
            } else {

                dbc = new DbConnection();
                st = dbc.getConnection(false).createStatement();
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String reqDate = request.getParameter("j_idt43:taskid");

                String maxValue = null;
                String LastID = "select nvl(max(TASK_HISTORY_ID),0)+1 as maxID from SSR_TASK_HISTORY";
                rs = st.executeQuery(LastID);
                while (rs.next()) {
                    maxValue = rs.getString("maxID");
                }

                if (sessionUserId != null) {
                    String sql = "Insert Into SSR_TASK_HISTORY(TASK_HISTORY_ID,request_id,ASSIGNMENT_ID,TASK_ID,STATUS,DESCRIPTION,UPDATE_BY,UPDATE_DATE)\n"
                            + "values(" + maxValue + "," + taskReqId + "," + taskReqAsignId + "," + taskReqTaskId + ",'" + taskReqTaskStatus + "','" + taskReqTaskDesc + "','" + sessionUsername + "','" + timeStamp + "')";
                    System.out.println(sql);
                    Adder ad = new Adder();
                    String sts = ad.ins(sql);
                    if (sts.equals("Y")) {
                        errorMesg=null;
                        taskReqTaskStatus = null;
                        taskReqTaskDesc = null;
                        updateAssignTaskUpdateDataTable();
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.execute("PF('myDialogReqDetAssignTask').show();");
                    } else {
                        System.out.println("Error Occured");
                        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Assignment Updated.", ""));
                    }
                } else {
                    FacesContext.getCurrentInstance().getExternalContext().redirect("../login.xhtml?faces-redirect=true");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in Insertion: " + e);
        }finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }
        return null;
    }

    public class requestCounterByStatus {

        private int serial;
        private String reqId;
        private String Status;
        private int counts;

        public requestCounterByStatus(int serial, String reqId, String Status, int counts) {
            this.serial = serial;
            this.reqId = reqId;
            this.Status = Status;
            this.counts = counts;
        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }

        public String getReqId() {
            return reqId;
        }

        public void setReqId(String reqId) {
            this.reqId = reqId;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

    }

    public class assignmentTaskUpdateDdList {

        private int serial;
        private String taskreqId;
        private String taskasignId;
        private String taskId;
        private String taskupdateStatus;
        private String taskupdateDec;
        private String taskUpdateBy;
        private String taskupdatedate;

        public assignmentTaskUpdateDdList(int serial, String taskreqId, String taskasignId, String taskId, String taskupdateStatus, String taskupdateDec, String taskUpdateBy, String taskupdatedate) {
            this.serial = serial;
            this.taskreqId = taskreqId;
            this.taskasignId = taskasignId;
            this.taskId = taskId;
            this.taskupdateStatus = taskupdateStatus;
            this.taskupdateDec = taskupdateDec;
            this.taskUpdateBy = taskUpdateBy;
            this.taskupdatedate = taskupdatedate;
        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }

        public String getTaskreqId() {
            return taskreqId;
        }

        public void setTaskreqId(String taskreqId) {
            this.taskreqId = taskreqId;
        }

        public String getTaskasignId() {
            return taskasignId;
        }

        public void setTaskasignId(String taskasignId) {
            this.taskasignId = taskasignId;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getTaskupdateStatus() {
            return taskupdateStatus;
        }

        public void setTaskupdateStatus(String taskupdateStatus) {
            this.taskupdateStatus = taskupdateStatus;
        }

        public String getTaskupdateDec() {
            return taskupdateDec;
        }

        public void setTaskupdateDec(String taskupdateDec) {
            this.taskupdateDec = taskupdateDec;
        }

        public String getTaskUpdateBy() {
            return taskUpdateBy;
        }

        public void setTaskUpdateBy(String taskUpdateBy) {
            this.taskUpdateBy = taskUpdateBy;
        }

        public String getTaskupdatedate() {
            return taskupdatedate;
        }

        public void setTaskupdatedate(String taskupdatedate) {
            this.taskupdatedate = taskupdatedate;
        }

    }

}
