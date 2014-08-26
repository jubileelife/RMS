/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import com.rms.dbconnection.Adder;
import com.rms.dbconnection.DbConnection;
import com.rms.dbconnection.FetchResc;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "req_Assignment")
@SessionScoped
public class requestAssignment implements Serializable {

    private DbConnection dbc;
    private DbConnection dbc2;
    private Statement st;
    private Statement stt;
    private ResultSet rs;
    private ResultSet rss;
    private String sessionUserId;
    private String sessionUsername;
    private String sessionReportTo;
    private String role;
    private HttpSession session = null;

    FetchResc fthDdRecord = new FetchResc();
    String resultSet[][] = new String[2][11];

    private List<DdValues> reqAssignSelect = new ArrayList<DdValues>();

    private List<assignmentDdList> filtered;
    private List<assignmentDdList> tableData = new ArrayList<assignmentDdList>();
    private assignmentDdList clickedItem;

    private List<solutionDtList> solutionFilter;
    private List<solutionDtList> solutionDataTable = new ArrayList<solutionDtList>();
    private solutionDtList solutionClickedItem;

    public String assignmentScreenReqId;
    public String assignmentScreenReqStatus;
    public String assignmentScreenReqStatusID;
    private String assignmentScreenReqDate;
    private String assignmentScreenReqApproveDate;
    private String assignmentScreenReqType;
    private String assignmentScreenReqSystem;
    private String assignmentScreenReqSystemMod;
    private String assignmentScreenRequester;
    private String assignmentScreenReqExpectedDate;
    private String assignmentScreenReqPriority;
    private String assignmentScreenReqTitle;
    private String assignmentScreenReqDesc;
    private String assignmentScreenReqApprovDesc;
    private String assignmentScreenRequesterFileUrl;
    private String assignmentScreenApprovalFileUrl;
    private String assignmentScreenApproveBy;
    public String assignmentAssignId;
    public String assignmentAssignName;
    public String assignmentreqtargetdate;

    private String solutionTxt = "Please enter the Solution.";

    private DefaultStreamedContent download;
    private DefaultStreamedContent downloadApprov;

    private String dlCheck = "display:none;";
    private String dlCheckAprove = "display:none;";

    private Date reqTargetDateCalendarValidate;

    private String assignmentCategory;
    private String assignmentAssignTo;
    private Date assignmentTargetDate;
    private Date assignmentCompletionDate;
    private String assignmentComments;
    private String readCheck = "false";

    private String destination = "D:\\rms_upload\\";
    private boolean dl = true;
    private boolean dlapp = true;
    private String errorMesg = "";
    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public requestAssignment() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");
        role = (String) session.getAttribute("role");

        updateAssignSelectFields();

    }
    
    public String getAssignmentScreenReqStatusID() {
        return assignmentScreenReqStatusID;
    }

    public String getAssignmentAssignName() {
        return assignmentAssignName;
    }

    public void setAssignmentAssignName(String assignmentAssignName) {
        this.assignmentAssignName = assignmentAssignName;
    }

    public void setAssignmentScreenReqStatusID(String assignmentScreenReqStatusID) {
        this.assignmentScreenReqStatusID = assignmentScreenReqStatusID;
    }

    public String getAssignmentScreenReqStatus() {
        return assignmentScreenReqStatus;
    }

    public void setAssignmentScreenReqStatus(String assignmentScreenReqStatus) {
        this.assignmentScreenReqStatus = assignmentScreenReqStatus;
    }

    public List<DdValues> getReqAssignSelect() {
        return reqAssignSelect;
    }

    public void setReqAssignSelect(List<DdValues> reqAssignSelect) {
        this.reqAssignSelect = reqAssignSelect;
    }

    public void reqAssignChangeEvent(ValueChangeEvent valueCE) {
        assignmentAssignTo = valueCE.getNewValue().toString();
    }

    public Date getReqTargetDateCalendarValidate() {
        try {
            return dateFormate.parse(dateFormate.format(reqTargetDateCalendarValidate));
        } catch (Exception e) {
        }
        return reqTargetDateCalendarValidate;
    }

    public void setReqTargetDateCalendarValidate(Date reqTargetDateCalendarValidate) {
        this.reqTargetDateCalendarValidate = reqTargetDateCalendarValidate;
    }

    public String getReadCheck() {
        return readCheck;
    }

    public void setReadCheck(String readCheck) {
        this.readCheck = readCheck;
    }

    public String getAssignmentreqtargetdate() {
        return assignmentreqtargetdate;
    }

    public void setAssignmentreqtargetdate(String assignmentreqtargetdate) {
        this.assignmentreqtargetdate = assignmentreqtargetdate;
    }

    public String getAssignmentAssignId() {
        return assignmentAssignId;
    }

    public void setAssignmentAssignId(String assignmentAssignId) {
        this.assignmentAssignId = assignmentAssignId;
    }

    public List<solutionDtList> getSolutionFilter() {
        return solutionFilter;
    }

    public void setSolutionFilter(List<solutionDtList> solutionFilter) {
        this.solutionFilter = solutionFilter;
    }

    public List<solutionDtList> getSolutionDataTable() {
        return solutionDataTable;
    }

    public void setSolutionDataTable(List<solutionDtList> solutionDataTable) {
        this.solutionDataTable = solutionDataTable;
    }

    public solutionDtList getSolutionClickedItem() {
        return solutionClickedItem;
    }

    public void setSolutionClickedItem(solutionDtList solutionClickedItem) {
        this.solutionClickedItem = solutionClickedItem;
    }

    public String getSolutionTxt() {
        return solutionTxt;
    }

    public void setSolutionTxt(String solutionTxt) {
        this.solutionTxt = solutionTxt;
    }

    public String getAssignmentComments() {
        return assignmentComments;
    }

    public void setAssignmentComments(String assignmentComments) {
        this.assignmentComments = assignmentComments;
    }

    public String getAssignmentCategory() {
        return assignmentCategory;
    }

    public void setAssignmentCategory(String assignmentCategory) {
        this.assignmentCategory = assignmentCategory;
    }

    public String getAssignmentAssignTo() {
        return assignmentAssignTo;
    }

    public void setAssignmentAssignTo(String assignmentAssignTo) {
        this.assignmentAssignTo = assignmentAssignTo;
    }

    public Date getAssignmentTargetDate() {
        return assignmentTargetDate;
    }

    public void setAssignmentTargetDate(Date assignmentTargetDate) {
        this.assignmentTargetDate = assignmentTargetDate;
    }

    public Date getAssignmentCompletionDate() {
        return assignmentCompletionDate;
    }

    public void setAssignmentCompletionDate(Date assignmentCompletionDate) {
        this.assignmentCompletionDate = assignmentCompletionDate;
    }

    public String getDlCheck() {
        return dlCheck;
    }

    public void setDlCheck(String dlCheck) {
        this.dlCheck = dlCheck;
    }

    public String getDlCheckAprove() {
        return dlCheckAprove;
    }

    public void setDlCheckAprove(String dlCheckAprove) {
        this.dlCheckAprove = dlCheckAprove;
    }

    public DefaultStreamedContent getDownload() {
        return download;
    }

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownloadApprov() {
        return downloadApprov;
    }

    public void setDownloadApprov(DefaultStreamedContent downloadApprov) {
        this.downloadApprov = downloadApprov;
    }

    public void redirectUrlAssignment() {
        updateDataTableReq();
        //updateDataTable();
    }

    public void redirectUrlAssignmentReq() {
        updateAssignDataTable();
    }

    public String getAssignmentScreenApproveBy() {
        return assignmentScreenApproveBy;
    }

    public void setAssignmentScreenApproveBy(String assignmentScreenApproveBy) {
        this.assignmentScreenApproveBy = assignmentScreenApproveBy;
    }

    public String getAssignmentScreenReqApproveDate() {
        return assignmentScreenReqApproveDate;
    }

    public void setAssignmentScreenReqApproveDate(String assignmentScreenReqApproveDate) {
        this.assignmentScreenReqApproveDate = assignmentScreenReqApproveDate;
    }

    public boolean isDlapp() {
        return dlapp;
    }

    public void setDlapp(boolean dlapp) {
        this.dlapp = dlapp;
    }

    public String getAssignmentScreenReqId() {
        return assignmentScreenReqId;
    }

    public void setAssignmentScreenReqId(String assignmentScreenReqId) {
        this.assignmentScreenReqId = assignmentScreenReqId;
    }

    public String getAssignmentScreenReqDate() {
        return assignmentScreenReqDate;
    }

    public void setAssignmentScreenReqDate(String assignmentScreenReqDate) {
        this.assignmentScreenReqDate = assignmentScreenReqDate;
    }

    public String getAssignmentScreenReqType() {
        return assignmentScreenReqType;
    }

    public void setAssignmentScreenReqType(String assignmentScreenReqType) {
        this.assignmentScreenReqType = assignmentScreenReqType;
    }

    public String getAssignmentScreenReqSystem() {
        return assignmentScreenReqSystem;
    }

    public void setAssignmentScreenReqSystem(String assignmentScreenReqSystem) {
        this.assignmentScreenReqSystem = assignmentScreenReqSystem;
    }

    public String getAssignmentScreenReqSystemMod() {
        return assignmentScreenReqSystemMod;
    }

    public void setAssignmentScreenReqSystemMod(String assignmentScreenReqSystemMod) {
        this.assignmentScreenReqSystemMod = assignmentScreenReqSystemMod;
    }

    public String getAssignmentScreenRequester() {
        return assignmentScreenRequester;
    }

    public void setAssignmentScreenRequester(String assignmentScreenRequester) {
        this.assignmentScreenRequester = assignmentScreenRequester;
    }

    public String getAssignmentScreenReqExpectedDate() {
        return assignmentScreenReqExpectedDate;
    }

    public void setAssignmentScreenReqExpectedDate(String assignmentScreenReqExpectedDate) {
        this.assignmentScreenReqExpectedDate = assignmentScreenReqExpectedDate;
    }

    public String getAssignmentScreenReqPriority() {
        return assignmentScreenReqPriority;
    }

    public void setAssignmentScreenReqPriority(String assignmentScreenReqPriority) {
        this.assignmentScreenReqPriority = assignmentScreenReqPriority;
    }

    public String getAssignmentScreenReqTitle() {
        return assignmentScreenReqTitle;
    }

    public void setAssignmentScreenReqTitle(String assignmentScreenReqTitle) {
        this.assignmentScreenReqTitle = assignmentScreenReqTitle;
    }

    public String getAssignmentScreenReqDesc() {
        return assignmentScreenReqDesc;
    }

    public void setAssignmentScreenReqDesc(String assignmentScreenReqDesc) {
        this.assignmentScreenReqDesc = assignmentScreenReqDesc;
    }

    public String getAssignmentScreenReqApprovDesc() {
        return assignmentScreenReqApprovDesc;
    }

    public void setAssignmentScreenReqApprovDesc(String assignmentScreenReqApprovDesc) {
        this.assignmentScreenReqApprovDesc = assignmentScreenReqApprovDesc;
    }

    public String getAssignmentScreenRequesterFileUrl() {
        return assignmentScreenRequesterFileUrl;
    }

    public void setAssignmentScreenRequesterFileUrl(String assignmentScreenRequesterFileUrl) {
        this.assignmentScreenRequesterFileUrl = assignmentScreenRequesterFileUrl;
    }

    public String getAssignmentScreenApprovalFileUrl() {
        return assignmentScreenApprovalFileUrl;
    }

    public void setAssignmentScreenApprovalFileUrl(String assignmentScreenApprovalFileUrl) {
        this.assignmentScreenApprovalFileUrl = assignmentScreenApprovalFileUrl;
    }

    public boolean isDl() {
        return dl;
    }

    public void setDl(boolean dl) {
        this.dl = dl;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public List<assignmentDdList> getFiltered() {
        return filtered;
    }

    public List<assignmentDdList> getTableData() {
        return tableData;
    }

    public void setTableData(List<assignmentDdList> tableData) {
        this.tableData = tableData;
    }

    public void setFiltered(List<assignmentDdList> filtered) {
        this.filtered = filtered;
    }

    public assignmentDdList getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(assignmentDdList clickedItem) {
        this.clickedItem = clickedItem;
    }

    public void updateAssignSelectFields() {
        try {
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            String sql = "select user_id,user_name from SSR_Login where isassign = '1' ";
            rs = st.executeQuery(sql);
            reqAssignSelect.add(new DdValues("", "Select One"));
            while (rs.next()) {
                reqAssignSelect.add(new DdValues(rs.getString("user_id"), rs.getString("user_name")));
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
    }

    public List<solutionDtList> updateSolutionDataTable(String reqID) {
        int i = 0;
        try {
            solutionFilter = null;
            solutionDataTable.clear();
            solutionDataTable = new ArrayList<solutionDtList>();
            FetchResc fetchrcrd = new FetchResc();
            String resultSet[][] = new String[2][11];
            String sqlRow = "select count(*) from SSR_SERVICE_SOLUTION where REQUEST_ID = " + reqID + " ";
            resultSet = fetchrcrd.sel(sqlRow);
            int counts = Integer.parseInt(resultSet[1][0]);

            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

            String sql = "select SOLUTION,USER_ID,to_char(ACCOUNT_DATE_1,'dd-mm-yyyy HH12:mi:ss am'),SSR_SOLUTION_ID from SSR_SERVICE_SOLUTION where request_id = " + reqID + "  ORDER BY SSR_SOLUTION_ID DESC ";
            ResultSet rs = null;
            rs = st.executeQuery(sql);

            while (rs.next()) {

                solutionDataTable.add(i, new solutionDtList(counts, rs.getString(1), rs.getString(2), rs.getString(3)));
                i++;
                counts--;
            }
        } catch (SQLException ex) {
            Logger.getLogger(requestAssignment.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }
        return solutionDataTable;
    }

    public List<assignmentDdList> updateDataTableReq() {
        int i = 0;
        String maxDeskId = "";
        String sqlMax;

        try {
            filtered = null;
            tableData.clear();
            tableData = new ArrayList<assignmentDdList>();
            dbc = new DbConnection();
            dbc2 = new DbConnection();

            st = null;
            st = dbc.getConnection(false).createStatement();

            stt = null;
            stt = dbc2.getConnection(false).createStatement();

            ResultSet rss = null;

            String sql = null;
            if ("2".equals((String) session.getAttribute("role"))) {
                sql = "select a.REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,a.REQUEST_TITLE,a.APPROVED_BY,d.SERVICE_DESK_ID from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c,SSR_SERVICE_ASSIGNMENT \n"
                        + " d where APPROVAL_REQUIRED IN ( 0,2 ) and a.request_id = d.request_id(+) and d.ASSIGNED_TO = '" + sessionUserId + "'  and a.requester = c.user_id  and a.request_type = b.id   order by REQUEST_ID desc  ";
            } else {
                sql = "select a.REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,a.REQUEST_TITLE,a.APPROVED_BY,d.SERVICE_DESK_ID from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c,SSR_SERVICE_ASSIGNMENT d where APPROVAL_REQUIRED IN ( 0,2 )  "
                        + " and a.requester = c.user_id and a.request_type = b.id and a.request_id = d.request_id(+)  order by REQUEST_ID desc  ";
            }
            //System.out.println("Sql: " + sql);

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            while (rs.next()) {

                if ("2".equals((String) session.getAttribute("role"))) {
                    sqlMax = "select Service_desk_id,CATEGORY_CODE, (select user_name from ssr_login c where c.user_id =  ASSIGNED_TO ) as m from SSR_SERVICE_ASSIGNMENT where SERVICE_DESK_ID = " + rs.getString(7) + " and ASSIGNED_TO = " + sessionUserId + " ";
                } else {
                    sqlMax = "select Service_desk_id,CATEGORY_CODE,(select user_name from ssr_login c where c.user_id = ASSIGNED_TO ) as m from SSR_SERVICE_ASSIGNMENT where SERVICE_DESK_ID = " + rs.getString(7) + "  ";
                }

                if (rs.getString(7) == null || "".equals(rs.getString(7))) {
                    tableData.add(i, new assignmentDdList(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), "", "", ""));
                    i++;
                } else {

                    rss = stt.executeQuery(sqlMax);
                    while (rss.next()) {
                        tableData.add(i, new assignmentDdList(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rss.getString(2), rss.getString(3), rss.getString(1)));
                        i++;
                    }

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

        return tableData;
    }

    public List<assignmentDdList> updateDataTable() {
        int i = 0;
        String maxDeskId = "";
        String sqlMax;

        try {
            filtered = null;
            tableData.clear();
            tableData = new ArrayList<assignmentDdList>();
            dbc = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();
            String sql = null;
            if ("2".equals((String) session.getAttribute("role"))) {
                sql = "select a.REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,a.REQUEST_TITLE,a.APPROVED_BY,d.SERVICE_DESK_ID from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c,SSR_SERVICE_ASSIGNMENT \n"
                        + " d where APPROVAL_REQUIRED IN ( 0,2 ) and a.request_id = d.request_id(+) and d.ASSIGNED_TO = '" + sessionUserId + "'  and a.requester = c.user_id and a.request_type = b.id   order by REQUEST_ID desc  ";
            } else {
                sql = "select REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,REQUEST_TITLE,APPROVED_BY,'' from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c where  APPROVAL_REQUIRED IN ( 0,2 )  "
                        + " and a.requester = c.user_id and a.request_type = b.id order by REQUEST_ID desc  ";
            }
            //System.out.println("Sql: " + sql);

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            while (rs.next()) {

                if ("2".equals((String) session.getAttribute("role"))) {
                    sqlMax = "select Service_desk_id,CATEGORY_CODE,(select user_name from ssr_login c where c.user_id =  ASSIGNED_TO ) as m from SSR_SERVICE_ASSIGNMENT where request_id = " + rs.getString(1) + " and ASSIGNED_TO = " + sessionUserId + " ";
                } else {
                    sqlMax = "select Service_desk_id,CATEGORY_CODE,(select user_name from ssr_login c where c.user_id =  ASSIGNED_TO ) as m from SSR_SERVICE_ASSIGNMENT where request_id = " + rs.getString(1) + "  ";
                }

                resultSet = fthDdRecord.selCustom(sqlMax);
                maxDeskId = resultSet[1][0];
                String maxCat = resultSet[1][1];
                String maxAssign = resultSet[1][2];
                String split[] = StringUtils.split(rs.getString(3));
                //if(rs.getString(7) == null || rs.getString(7).equals("")){ maxDeskId = maxDeskId; }else{  }
                tableData.add(i, new assignmentDdList(rs.getString(1), rs.getString(2), split[0], rs.getString(4), rs.getString(5), rs.getString(6), maxCat, maxAssign, maxDeskId));
                i++;
            }

        } catch (SQLException e) {
            System.out.println("error in record retrieving: " + e);
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }
        return tableData;
    }

    public List<assignmentDdList> updateDataTableFromSearch(String requestID) {
        int i = 0;
        String maxDeskId = "";
        String sqlMax;

        try {
            filtered = null;
            tableData.clear();
            tableData = new ArrayList<assignmentDdList>();
            dbc = new DbConnection();
            dbc2 = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();

            stt = null;
            stt = dbc2.getConnection(false).createStatement();

            ResultSet rss = null;

            String sql = null;
            if ("2".equals((String) session.getAttribute("role"))) {
                sql = "select a.REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,a.REQUEST_TITLE,a.APPROVED_BY,d.SERVICE_DESK_ID from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c,SSR_SERVICE_ASSIGNMENT \n"
                        + " d where APPROVAL_REQUIRED IN ( 0,2 ) and a.request_id = d.request_id(+) and d.ASSIGNED_TO = '" + sessionUserId + "'  and a.requester = c.user_id and a.request_id = " + requestID + " and a.request_type = b.id   order by REQUEST_ID desc  ";
            } else {
                sql = "select a.REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,a.REQUEST_TITLE,a.APPROVED_BY,d.SERVICE_DESK_ID from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c,SSR_SERVICE_ASSIGNMENT d where  APPROVAL_REQUIRED IN ( 0,2 )  "
                        + " and a.requester = c.user_id and a.request_type = b.id and a.request_id = d.request_id(+) and a.request_id = " + requestID + " order by REQUEST_ID desc  ";
            }
            //System.out.println("Sql: " + sql);

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            while (rs.next()) {

                if ("2".equals((String) session.getAttribute("role"))) {
                    sqlMax = "select Service_desk_id,CATEGORY_CODE,(select user_name from ssr_login c where c.user_id =  ASSIGNED_TO ) as m from SSR_SERVICE_ASSIGNMENT where SERVICE_DESK_ID = " + rs.getString(7) + " and ASSIGNED_TO = " + sessionUserId + " ";
                } else {
                    sqlMax = "select Service_desk_id,CATEGORY_CODE,(select user_name from ssr_login c where c.user_id =  ASSIGNED_TO ) as m from SSR_SERVICE_ASSIGNMENT where SERVICE_DESK_ID = " + rs.getString(7) + "  ";
                }
                //System.out.println("Sql: " + sqlMax);
                String split[] = StringUtils.split(rs.getString(3));

                rss = stt.executeQuery(sqlMax);
                while (rss.next()) {
                    tableData.add(i, new assignmentDdList(rs.getString(1), rs.getString(2), split[0], rs.getString(4), rs.getString(5), rs.getString(6), rss.getString(2), rss.getString(3), rss.getString(1)));
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
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("requestListSearch.xhtml?faces-redirect=true");
        } catch (Exception e) {
        }
        return tableData;
    }

    public List<assignmentDdList> updateAssignDataTable() {
        int i = 0;
        String maxDeskId = "";
        String sqlMax;
        String reqID = getAssignmentScreenReqId();
        if (reqID == null || reqID.equals("")) {
            reqID = "";
        } else {
            reqID = "and a.request_id = " + reqID + " ";
        }
        try {
            filtered = null;
            tableData.clear();
            tableData = new ArrayList<assignmentDdList>();
            dbc = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();
            String sql = null;

            sql = "select a.REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,a.REQUEST_TITLE,a.APPROVED_BY,d.SERVICE_DESK_ID,d.CATEGORY_CODE,(select user_name from ssr_login c where c.user_id =  d.ASSIGNED_TO ) from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c,SSR_SERVICE_ASSIGNMENT \n"
                    + " d where APPROVAL_REQUIRED IN ( 0,2 ) and a.request_id = d.request_id  and a.requester = c.user_id and a.request_type = b.id " + reqID + "  order by REQUEST_ID desc  ";

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            while (rs.next()) {

                String split[] = StringUtils.split(rs.getString(3));
                //if(rs.getString(7) == null || rs.getString(7).equals("")){ maxDeskId = maxDeskId; }else{  }
                tableData.add(i, new assignmentDdList(rs.getString(1), rs.getString(2), split[0], rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(8), rs.getString(9), rs.getString(7)));
                i++;
            }

        } catch (SQLException e) {
            System.out.println("error in record retrieving: " + e);
        } finally {
            try {
                dbc.freeConnection();
                st.close();
            } catch (SQLException ex) {
                System.out.println("Exception while closing connection in logout " + ex.getMessage());
            }
        }

        return tableData;
    }

    public String onClickAssign() {

        FetchResc fetchrcrd = new FetchResc();
        String resultSet[][] = new String[2][11];
        String sql = null;
        sql = "select b.user_name,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.REQUEST_TYPE,d.name,e.module_name,a.REQUEST_TITLE,a.REQUEST_DESCRIPTION,a.REQUEST_PRIORITY,a.REQUEST_FILE_ATTACHED,a.APPROVAL_COMMENT,a.APPROVAL_FILE_ATTACH,a.EXPECTED_DELIVERY_DATE,to_char(a.APPROVED_DATE,'dd-mm-yyyy HH12:mi:ss am'),a.APPROVED_BY,to_char(a.target_date,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS cc where cc.id = a.REQUEST_STATUS )  \n"
                + " from SSR_REQUEST_INITIATE a, SSR_LOGIN b, SSR_REQUEST_TYPE c, SSR_SYSTEMS d,SSR_SYSTEM_MODULE e  \n"
                + "where a.request_id = " + clickedItem.getReqId() + " and a.REQUESTER = b.user_id and a.REQUEST_TYPE = c.id and a.system_id = d.id and e.id = a.SYSTEM_MODULE";
        errorMesg = "";
        resultSet = fetchrcrd.sel(sql);
        assignmentScreenReqId = clickedItem.getReqId();
        assignmentScreenRequester = resultSet[1][0];
        assignmentScreenReqDate = resultSet[1][1];
        assignmentScreenReqType = resultSet[1][2];
        assignmentScreenReqSystem = resultSet[1][3];
        assignmentScreenReqSystemMod = resultSet[1][4];
        assignmentScreenReqTitle = resultSet[1][5];
        assignmentScreenReqDesc = resultSet[1][6];
        assignmentScreenReqPriority = resultSet[1][7];
        assignmentScreenRequesterFileUrl = resultSet[1][8];
        assignmentScreenReqApprovDesc = resultSet[1][9];
        assignmentScreenApprovalFileUrl = resultSet[1][10];
        assignmentScreenReqExpectedDate = resultSet[1][11];
        assignmentScreenReqApproveDate = resultSet[1][12];
        assignmentScreenApproveBy = resultSet[1][13];
        assignmentreqtargetdate = resultSet[1][14];
        assignmentScreenReqStatus = resultSet[1][15];
        
        if ("null".equals(assignmentreqtargetdate) || "-".equals(assignmentreqtargetdate) || "".equals(assignmentreqtargetdate) || assignmentreqtargetdate == null) {
            setReadCheck("false");
        } else {
            setReadCheck("true");
        }

        if ("null".equals(assignmentScreenRequesterFileUrl) || "-".equals(assignmentScreenRequesterFileUrl) || "".equals(assignmentScreenRequesterFileUrl)) {
            setDl(true);
            setDlCheck("display:none");
        } else {
            setDl(false);
            setDlCheck("display:block");
        }
        if ("null".equals(assignmentScreenApprovalFileUrl) || "-".equals(assignmentScreenApprovalFileUrl) || "".equals(assignmentScreenApprovalFileUrl) || assignmentScreenApprovalFileUrl == null) {
            setDlapp(true);
            setDlCheckAprove("display:none");
        } else {
            setDlapp(false);
            setDlCheckAprove("display:block");
        }

        return "requestAssignment.xhtml?faces-redirect=true";
    }

    public String onClickSolutionForUser(String check, String reqId) {

        FetchResc fetchrcrd = new FetchResc();
        String resultSet[][] = new String[2][11];
        String sql = null;
        sql = "select b.user_name,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.REQUEST_TYPE,d.name,e.module_name,a.REQUEST_TITLE,a.REQUEST_DESCRIPTION,a.REQUEST_PRIORITY,a.REQUEST_FILE_ATTACHED,a.APPROVAL_COMMENT,a.APPROVAL_FILE_ATTACH,a.EXPECTED_DELIVERY_DATE,to_char(a.APPROVED_DATE,'dd-mm-yyyy HH12:mi:ss am'),a.APPROVED_BY,to_char(a.target_date,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS cc where cc.id = a.REQUEST_STATUS )  \n"
                + " from SSR_REQUEST_INITIATE a, SSR_LOGIN b, SSR_REQUEST_TYPE c, SSR_SYSTEMS d,SSR_SYSTEM_MODULE e  \n"
                + "where a.request_id = " + reqId + " and a.REQUESTER = b.user_id and a.REQUEST_TYPE = c.id and a.system_id = d.id and e.id = a.SYSTEM_MODULE";

        errorMesg = "";
        resultSet = fetchrcrd.sel(sql);
        assignmentScreenReqId = reqId;
        assignmentScreenRequester = resultSet[1][0];
        assignmentScreenReqDate = resultSet[1][1];
        assignmentScreenReqType = resultSet[1][2];
        assignmentScreenReqSystem = resultSet[1][3];
        assignmentScreenReqSystemMod = resultSet[1][4];
        assignmentScreenReqTitle = resultSet[1][5];
        assignmentScreenReqDesc = resultSet[1][6];
        assignmentScreenReqPriority = resultSet[1][7];
        assignmentScreenRequesterFileUrl = resultSet[1][8];
        assignmentScreenReqApprovDesc = resultSet[1][9];
        assignmentScreenApprovalFileUrl = resultSet[1][10];
        assignmentScreenReqExpectedDate = resultSet[1][11];
        assignmentScreenReqApproveDate = resultSet[1][12];
        assignmentScreenApproveBy = resultSet[1][13];
        assignmentreqtargetdate = resultSet[1][14];
        assignmentScreenReqStatus = resultSet[1][15];
        
        if ("null".equals(assignmentreqtargetdate) || "-".equals(assignmentreqtargetdate) || "".equals(assignmentreqtargetdate) || assignmentreqtargetdate == null) {
            setReadCheck("false");
        } else {
            setReadCheck("true");
        }

        if ("null".equals(assignmentScreenRequesterFileUrl) || "-".equals(assignmentScreenRequesterFileUrl) || "".equals(assignmentScreenRequesterFileUrl)) {
            setDl(true);
            setDlCheck("display:none");
        } else {
            setDl(false);
            setDlCheck("display:block");
        }
        if ("null".equals(assignmentScreenApprovalFileUrl) || "-".equals(assignmentScreenApprovalFileUrl) || "".equals(assignmentScreenApprovalFileUrl) || assignmentScreenApprovalFileUrl == null) {
            setDlapp(true);
            setDlCheckAprove("display:none");
        } else {
            setDlapp(false);
            setDlCheckAprove("display:block");
        }

        updateSolutionDataTable(assignmentScreenReqId);
        return "";
    }

    public String onClickSolution(String check) {

        FetchResc fetchrcrd = new FetchResc();
        String resultSet[][] = new String[2][11];
        String sql = null;
        sql = "select b.user_name,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.REQUEST_TYPE,d.name,e.module_name,a.REQUEST_TITLE,a.REQUEST_DESCRIPTION,a.REQUEST_PRIORITY,a.REQUEST_FILE_ATTACHED,a.APPROVAL_COMMENT,a.APPROVAL_FILE_ATTACH,a.EXPECTED_DELIVERY_DATE,to_char(a.APPROVED_DATE,'dd-mm-yyyy HH12:mi:ss am'),a.APPROVED_BY,to_char(a.target_date,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS cc where cc.id = a.REQUEST_STATUS )  \n"
                + " from SSR_REQUEST_INITIATE a, SSR_LOGIN b, SSR_REQUEST_TYPE c, SSR_SYSTEMS d,SSR_SYSTEM_MODULE e  \n"
                + "where a.request_id = " + clickedItem.getReqId() + " and a.REQUESTER = b.user_id and a.REQUEST_TYPE = c.id and a.system_id = d.id and e.id = a.SYSTEM_MODULE";

        errorMesg = "";
        resultSet = fetchrcrd.sel(sql);
        assignmentScreenReqId = clickedItem.getReqId();
        assignmentScreenRequester = resultSet[1][0];
        assignmentScreenReqDate = resultSet[1][1];
        assignmentScreenReqType = resultSet[1][2];
        assignmentScreenReqSystem = resultSet[1][3];
        assignmentScreenReqSystemMod = resultSet[1][4];
        assignmentScreenReqTitle = resultSet[1][5];
        assignmentScreenReqDesc = resultSet[1][6];
        assignmentScreenReqPriority = resultSet[1][7];
        assignmentScreenRequesterFileUrl = resultSet[1][8];
        assignmentScreenReqApprovDesc = resultSet[1][9];
        assignmentScreenApprovalFileUrl = resultSet[1][10];
        assignmentScreenReqExpectedDate = resultSet[1][11];
        assignmentScreenReqApproveDate = resultSet[1][12];
        assignmentScreenApproveBy = resultSet[1][13];
        assignmentreqtargetdate = resultSet[1][14];
        assignmentScreenReqStatus = resultSet[1][15];
        
        if ("null".equals(assignmentreqtargetdate) || "-".equals(assignmentreqtargetdate) || "".equals(assignmentreqtargetdate) || assignmentreqtargetdate == null) {
            setReadCheck("false");
        } else {
            setReadCheck("true");
        }

        if ("null".equals(assignmentScreenRequesterFileUrl) || "-".equals(assignmentScreenRequesterFileUrl) || "".equals(assignmentScreenRequesterFileUrl)) {
            setDl(true);
            setDlCheck("display:none");
        } else {
            setDl(false);
            setDlCheck("display:block");
        }
        if ("null".equals(assignmentScreenApprovalFileUrl) || "-".equals(assignmentScreenApprovalFileUrl) || "".equals(assignmentScreenApprovalFileUrl) || assignmentScreenApprovalFileUrl == null) {
            setDlapp(true);
            setDlCheckAprove("display:none");
        } else {
            setDlapp(false);
            setDlCheckAprove("display:block");
        }

        updateSolutionDataTable(assignmentScreenReqId);

        if ("No".equals(check) || check == "No") {
            return "";
        } else {
            return "directSolution.xhtml?faces-redirect=true";
        }
    }

    public String onClickRequestAssign() {
        try {

//            if ("".equals(assignmentCompletionDate) || assignmentCompletionDate == null && "false".equals(readCheck)) {
//                errorMesg = "Please Enter Request Complettion Date First.";
//                return "";
//            } else {
            if (readCheck == "true" || "true".equals(readCheck)) {
            } else {
                if ("".equals(assignmentCompletionDate) || assignmentCompletionDate == null) {
                } else {
                    String sqlReq = "Update SSR_REQUEST_INITIATE set target_date = '" + dateFormate.format(assignmentCompletionDate) + "'  where request_id = " + getAssignmentScreenReqId() + " ";
                    Adder add = new Adder();
                    String stts = add.ins(sqlReq);
                    if (stts.equals("Y")) {
                        errorMesg = null;
                        updateDataTable();
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.execute("PF('myDialogDateAsigned').show();");
                        return "";
                    }
                }
            }
//            }

            if ("".equals(assignmentCategory) || "".equals(assignmentAssignTo) || "".equals(assignmentTargetDate) || "".equals(assignmentCompletionDate) || "".equals(assignmentComments)) {
                errorMesg = "Please Enter Assignment Details.";
            } else {
                dbc = new DbConnection();
                st = dbc.getConnection(false).createStatement();

                String maxValue = null;
                String LastID = "select nvl(max(SERVICE_DESK_ID),0)+1 as maxID from SSR_SERVICE_ASSIGNMENT";
                rs = st.executeQuery(LastID);
                while (rs.next()) {
                    maxValue = rs.getString("maxID");
                }

                String sql = " insert into SSR_SERVICE_ASSIGNMENT (SERVICE_DESK_ID,REQUEST_ID,CATEGORY_CODE,ASSIGNED_TO,START_DATE,END_DATE,ASSIGNED_BY,ASSIGNED_DATE,SERVICE_DESK_STATUS,SD_COMMENTS,ACCOUNT_DATE_1,USER_ID) "
                        + " Values( " + maxValue + ", " + assignmentScreenReqId + ",'" + assignmentCategory + "','" + assignmentAssignTo + "','" + timeStamp + "','" + dateFormate.format(assignmentTargetDate) + "','" + sessionUsername + "','" + timeStamp + "','Open','" + assignmentComments + "','" + timeStamp + "','" + sessionUsername + "'  )";
                System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    errorMesg = null;
                    assignmentCategory = null;
                    assignmentAssignTo = null;
                    assignmentTargetDate = null;
                    assignmentComments = null;
                    assignmentCompletionDate = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogAsigned').show();");
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

    public String addSolution() {
        try {
            if ("".equals(solutionTxt) || "Please enter the Solution.".equals(solutionTxt)) {
                errorMesg = "Please Enter Solution.";
            } else {
                dbc = new DbConnection();
                st = dbc.getConnection(false).createStatement();

                String maxValue = null;
                String LastID = "select nvl(max(SSR_SOLUTION_ID),0)+1 as maxID from SSR_SERVICE_SOLUTION";
                rs = st.executeQuery(LastID);
                while (rs.next()) {
                    maxValue = rs.getString("maxID");
                }

                String sql = " insert into SSR_SERVICE_SOLUTION (SSR_SOLUTION_ID,REQUEST_ID,SOLUTION,ACCOUNT_DATE_1,USER_ID) "
                        + " Values( " + maxValue + ", " + assignmentScreenReqId + ",'" + solutionTxt + "','" + timeStamp + "','" + sessionUsername + "' )";
                System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    errorMesg = null;
                    solutionTxt = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogSolution').show();");
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

    public String onClickDetail() {
        FetchResc fetchrcrd = new FetchResc();
        String resultSet[][] = new String[2][11];
        String sql = null;
        sql = "select b.user_name,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.REQUEST_TYPE,d.name,e.module_name,a.REQUEST_TITLE,a.REQUEST_DESCRIPTION,a.REQUEST_PRIORITY,a.REQUEST_FILE_ATTACHED,a.APPROVAL_COMMENT,a.APPROVAL_FILE_ATTACH,a.EXPECTED_DELIVERY_DATE,to_char(a.APPROVED_DATE,'dd-mm-yyyy HH12:mi:ss am'),a.APPROVED_BY,to_char(a.target_date,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS cc where cc.id = a.REQUEST_STATUS )  \n"
                + " from SSR_REQUEST_INITIATE a, SSR_LOGIN b, SSR_REQUEST_TYPE c, SSR_SYSTEMS d,SSR_SYSTEM_MODULE e  \n"
                + "where a.request_id = " + clickedItem.getReqId() + " and a.REQUESTER = b.user_id and a.REQUEST_TYPE = c.id and a.system_id = d.id and e.id = a.SYSTEM_MODULE";

        errorMesg = "";
        resultSet = fetchrcrd.sel(sql);
        assignmentScreenReqId = clickedItem.getReqId();
        assignmentAssignName = clickedItem.getReqMaxCategory();
        assignmentAssignId = clickedItem.getReqAssignmentId();
        assignmentScreenRequester = resultSet[1][0];
        assignmentScreenReqDate = resultSet[1][1];
        assignmentScreenReqType = resultSet[1][2];
        assignmentScreenReqSystem = resultSet[1][3];
        assignmentScreenReqSystemMod = resultSet[1][4];
        assignmentScreenReqTitle = resultSet[1][5];
        assignmentScreenReqDesc = resultSet[1][6];
        assignmentScreenReqPriority = resultSet[1][7];
        assignmentScreenRequesterFileUrl = resultSet[1][8];
        assignmentScreenReqApprovDesc = resultSet[1][9];
        assignmentScreenApprovalFileUrl = resultSet[1][10];
        assignmentScreenReqExpectedDate = resultSet[1][11];
        assignmentScreenReqApproveDate = resultSet[1][12];
        assignmentScreenApproveBy = resultSet[1][13];
        assignmentreqtargetdate = resultSet[1][14];
        assignmentScreenReqStatus = resultSet[1][15];
        if ("null".equals(assignmentreqtargetdate) || "-".equals(assignmentreqtargetdate) || "".equals(assignmentreqtargetdate) || assignmentreqtargetdate == null) {
            setReadCheck("false");
        } else {
            setReadCheck("true");
        }

        if ("null".equals(assignmentScreenRequesterFileUrl) || "-".equals(assignmentScreenRequesterFileUrl) || "".equals(assignmentScreenRequesterFileUrl)) {
            setDl(true);
            setDlCheck("display:none");
        } else {
            setDl(false);
            setDlCheck("display:block");
        }
        if ("null".equals(assignmentScreenApprovalFileUrl) || "-".equals(assignmentScreenApprovalFileUrl) || "".equals(assignmentScreenApprovalFileUrl) || assignmentScreenApprovalFileUrl == null) {
            setDlapp(true);
            setDlCheckAprove("display:none");
        } else {
            setDlapp(false);
            setDlCheckAprove("display:block");
        }

        onClickSolution("No");
        return "requestDetails.xhtml?faces-redirect=true";

    }

    public String onClickDetailForUser(String reqId) {
        FetchResc fetchrcrd = new FetchResc();
        String resultSet[][] = new String[2][11];
        String sql = null;
        sql = "select b.user_name,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.REQUEST_TYPE,d.name,e.module_name,a.REQUEST_TITLE,a.REQUEST_DESCRIPTION,a.REQUEST_PRIORITY,a.REQUEST_FILE_ATTACHED,a.APPROVAL_COMMENT,a.APPROVAL_FILE_ATTACH,a.EXPECTED_DELIVERY_DATE,to_char(a.APPROVED_DATE,'dd-mm-yyyy HH12:mi:ss am'),a.APPROVED_BY,to_char(a.target_date,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS cc where cc.id = a.REQUEST_STATUS ),a.REQUEST_STATUS  \n"
                + " from SSR_REQUEST_INITIATE a, SSR_LOGIN b, SSR_REQUEST_TYPE c, SSR_SYSTEMS d,SSR_SYSTEM_MODULE e  \n"
                + "where a.request_id = " + reqId + " and a.REQUESTER = b.user_id and a.REQUEST_TYPE = c.id and a.system_id = d.id and e.id = a.SYSTEM_MODULE";

        errorMesg = "";
        resultSet = fetchrcrd.sel(sql);
        assignmentScreenReqId = reqId;
        //assignmentAssignId = clickedItem.getReqAssignmentId();
        assignmentScreenRequester = resultSet[1][0];
        assignmentScreenReqDate = resultSet[1][1];
        assignmentScreenReqType = resultSet[1][2];
        assignmentScreenReqSystem = resultSet[1][3];
        assignmentScreenReqSystemMod = resultSet[1][4];
        assignmentScreenReqTitle = resultSet[1][5];
        assignmentScreenReqDesc = resultSet[1][6];
        assignmentScreenReqPriority = resultSet[1][7];
        assignmentScreenRequesterFileUrl = resultSet[1][8];
        assignmentScreenReqApprovDesc = resultSet[1][9];
        assignmentScreenApprovalFileUrl = resultSet[1][10];
        assignmentScreenReqExpectedDate = resultSet[1][11];
        assignmentScreenReqApproveDate = resultSet[1][12];
        assignmentScreenApproveBy = resultSet[1][13];
        assignmentreqtargetdate = resultSet[1][14];
        assignmentScreenReqStatus = resultSet[1][15];
        assignmentScreenReqStatusID = resultSet[1][16];
        if ("null".equals(assignmentreqtargetdate) || "-".equals(assignmentreqtargetdate) || "".equals(assignmentreqtargetdate) || assignmentreqtargetdate == null) {
            setReadCheck("false");
        } else {
            setReadCheck("true");
        }

        if ("null".equals(assignmentScreenRequesterFileUrl) || "-".equals(assignmentScreenRequesterFileUrl) || "".equals(assignmentScreenRequesterFileUrl)) {
            setDl(true);
            setDlCheck("display:none");
        } else {
            setDl(false);
            setDlCheck("display:block");
        }
        if ("null".equals(assignmentScreenApprovalFileUrl) || "-".equals(assignmentScreenApprovalFileUrl) || "".equals(assignmentScreenApprovalFileUrl) || assignmentScreenApprovalFileUrl == null) {
            setDlapp(true);
            setDlCheckAprove("display:none");
        } else {
            setDlapp(false);
            setDlCheckAprove("display:block");
        }

        onClickSolutionForUser("No", reqId);
        return "requestUserDetails.xhtml?faces-redirect=true";

    }

    public void redirectUrlVaiAssignment() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("assignmentListDetail.xhtml?faces-redirect=true");
    }

    public void redirectUrlAssigned(String check) throws IOException {
        if ("user".equals(check) || check == "user") {
            FacesContext.getCurrentInstance().getExternalContext().redirect("requestList.xhtml?faces-redirect=true");
        } else {
            FacesContext.getCurrentInstance().getExternalContext().redirect("assignmentList.xhtml?faces-redirect=true");
        }
    }

    public void preDownload() {
        try {
            setDl(false);
            File file = new File(assignmentScreenRequesterFileUrl);
            InputStream input;
            input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
            //System.out.println("PREP = " + download.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RequestApproval.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void preDownloadApprove() {
        try {
            setDl(false);
            File file = new File(assignmentScreenApprovalFileUrl);
            InputStream input;
            input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownloadApprov(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
            //System.out.println("PREP = " + download.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RequestApproval.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class solutionDtList {

        private int serialNo;
        private String solution;
        private String solutionUpdateBy;
        private String solutionUpdateDate;

        public solutionDtList(int serialNo, String solution, String solutionUpdateBy, String solutionUpdateDate) {
            this.serialNo = serialNo;
            this.solution = solution;
            this.solutionUpdateBy = solutionUpdateBy;
            this.solutionUpdateDate = solutionUpdateDate;
        }

        public int getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(int serialNo) {
            this.serialNo = serialNo;
        }

        public String getSolution() {
            return solution;
        }

        public void setSolution(String solution) {
            this.solution = solution;
        }

        public String getSolutionUpdateBy() {
            return solutionUpdateBy;
        }

        public void setSolutionUpdateBy(String solutionUpdateBy) {
            this.solutionUpdateBy = solutionUpdateBy;
        }

        public String getSolutionUpdateDate() {
            return solutionUpdateDate;
        }

        public void setSolutionUpdateDate(String solutionUpdateDate) {
            this.solutionUpdateDate = solutionUpdateDate;
        }

    }

    public class assignmentDdList {

        private String reqId;
        private String reqType;
        private String reqDate;
        private String requesterName;
        private String reqTitle;
        private String reqApproveBy;
        private String reqMaxCategory;
        private String reqMaxAssign;
        private String reqAssignmentId;

        public assignmentDdList(String reqId, String reqType, String reqDate, String requesterName, String reqTitle, String reqApproveBy, String reqMaxCategory, String reqMaxAssign, String reqAssignmentId) {
            this.reqId = reqId;
            this.reqType = reqType;
            this.reqDate = reqDate;
            this.requesterName = requesterName;
            this.reqTitle = reqTitle;
            this.reqApproveBy = reqApproveBy;
            this.reqMaxCategory = reqMaxCategory;
            this.reqMaxAssign = reqMaxAssign;
            this.reqAssignmentId = reqAssignmentId;
        }

        public String getReqAssignmentId() {
            return reqAssignmentId;
        }

        public void setReqAssignmentId(String reqAssignmentId) {
            this.reqAssignmentId = reqAssignmentId;
        }

        public String getReqMaxCategory() {
            return reqMaxCategory;
        }

        public void setReqMaxCategory(String reqMaxCategory) {
            this.reqMaxCategory = reqMaxCategory;
        }

        public String getReqMaxAssign() {
            return reqMaxAssign;
        }

        public void setReqMaxAssign(String reqMaxAssign) {
            this.reqMaxAssign = reqMaxAssign;
        }

        public String getReqId() {
            return reqId;
        }

        public void setReqId(String reqId) {
            this.reqId = reqId;
        }

        public String getReqType() {
            return reqType;
        }

        public void setReqType(String reqType) {
            this.reqType = reqType;
        }

        public String getReqDate() {
            return reqDate;
        }

        public void setReqDate(String reqDate) {
            this.reqDate = reqDate;
        }

        public String getRequesterName() {
            return requesterName;
        }

        public void setRequesterName(String requesterName) {
            this.requesterName = requesterName;
        }

        public String getReqTitle() {
            return reqTitle;
        }

        public void setReqTitle(String reqTitle) {
            this.reqTitle = reqTitle;
        }

        public String getReqApproveBy() {
            return reqApproveBy;
        }

        public void setReqApproveBy(String reqApproveBy) {
            this.reqApproveBy = reqApproveBy;
        }

    }
}
