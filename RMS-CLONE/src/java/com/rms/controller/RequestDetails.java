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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;

/**
 *
 * @author Ali
 */
@ManagedBean(name = "request_details")
@SessionScoped
public class RequestDetails implements Serializable {

    private DbConnection dbc;
    private DbConnection dbc2;
    private Statement st;
    private Statement stt;
    private ResultSet rs;
    private String sessionUserId;
    private String sessionUsername;
    private String sessionReportTo;
    private HttpSession session = null;

    private DefaultStreamedContent download;
    private String reqNo;
    private String reqDate;
    private String reqStatus;
    private String reqRemarks = "Please Enter Your Remarks.";
    private String reqFileAttached;
    private String destination = "D:\\rms_upload\\";
    private String errorMesg = "";
    private boolean dl = true;
    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    private List<DdValues> requestStatusDd = new ArrayList<DdValues>();

    private String ddReqID;
    private String ddReqDate;
    private String ddReqUpdateBy;
    private String ddReqRemarks;

    private String searchReqId = "Search By Request No";
    private String searchReqTitle = "Search By Request Title";

    private int tabSelection = 0;
    private List<reqDetailDtList> filtered;
    private List<reqDetailDtList> reqDetailTableData = new ArrayList<reqDetailDtList>();
    private reqDetailDtList clickedItem;

    public List<DdValues> getRequestStatusDd() {
        return requestStatusDd;
    }

    public void setRequestStatusDd(List<DdValues> requestStatusDd) {
        this.requestStatusDd = requestStatusDd;
    }

    public String getDdReqID() {
        return ddReqID;
    }

    public String getSearchReqId() {
        return searchReqId;
    }

    public void setSearchReqId(String searchReqId) {
        this.searchReqId = searchReqId;
    }

    public String getSearchReqTitle() {
        return searchReqTitle;
    }

    public void setSearchReqTitle(String searchReqTitle) {
        this.searchReqTitle = searchReqTitle;
    }

    public void setDdReqID(String ddReqID) {
        this.ddReqID = ddReqID;
    }

    public String getDdReqDate() {
        return ddReqDate;
    }

    public void setDdReqDate(String ddReqDate) {
        this.ddReqDate = ddReqDate;
    }

    public String getDdReqUpdateBy() {
        return ddReqUpdateBy;
    }

    public void setDdReqUpdateBy(String ddReqUpdateBy) {
        this.ddReqUpdateBy = ddReqUpdateBy;
    }

    public String getDdReqRemarks() {
        return ddReqRemarks;
    }

    public void setDdReqRemarks(String ddReqRemarks) {
        this.ddReqRemarks = ddReqRemarks;
    }

    public int getTabSelection() {
        return tabSelection;
    }

    public void setTabSelection(int tabSelection) {
        this.tabSelection = tabSelection;
    }

    public boolean isDl() {
        return dl;
    }

    public void setDl(boolean dl) {
        this.dl = dl;
    }

    public DefaultStreamedContent getDownload() {
        return download;
    }

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public List<reqDetailDtList> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<reqDetailDtList> filtered) {
        this.filtered = filtered;
    }

    public List<reqDetailDtList> getReqDetailTableData() {
        return reqDetailTableData;
    }

    public void setReqDetailTableData(List<reqDetailDtList> reqDetailTableData) {
        this.reqDetailTableData = reqDetailTableData;
    }

    public reqDetailDtList getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(reqDetailDtList clickedItem) {
        this.clickedItem = clickedItem;
    }

    public String getReqNo() {
        return reqNo;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public void setReqNo(String reqNo) {
        this.reqNo = reqNo;
    }

    public String getReqDate() {
        return reqDate;
    }

    public void setReqDate(String reqDate) {
        this.reqDate = reqDate;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getReqRemarks() {
        return reqRemarks;
    }

    public void setReqRemarks(String reqRemarks) {
        this.reqRemarks = reqRemarks;
    }

    public String getReqFileAttached() {
        return reqFileAttached;
    }

    public void setReqFileAttached(String reqFileAttached) {
        this.reqFileAttached = reqFileAttached;
    }

    public RequestDetails() {

        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");
        try {

            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            String sql = "select id,status from SSR_REQUEST_STATUS order by id";
            rs = st.executeQuery(sql);
            requestStatusDd.add(new DdValues("", "Select One"));
            while (rs.next()) {
                requestStatusDd.add(new DdValues(rs.getString("id"), rs.getString("status")));
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

    public void redirectRequestDetail() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("requestDetails.xhtml?faces-redirect=true");
        //setTabSelection(1);
    }

    public void redirectRequestDetailTab(String reqID, String reqAssignId) {
        updateReqDataTable(reqID, "");
    }

    public List<reqDetailDtList> updateReqDataTable(String requestId, String reqTitle) {
        int i = 0;
        String searchQ = "";
        try {

            if (reqTitle == null || reqTitle.equals("")) {
                searchQ = "";
            } else {
                searchQ = "and request_title like '%" + reqTitle + "%' ";
            }
            filtered = null;
            reqDetailTableData.clear();
            reqDetailTableData = new ArrayList<reqDetailDtList>();
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

            FetchResc fetchrcrd = new FetchResc();
            String resultSet[][] = new String[2][11];
            String sqlRow = "select count(*) from SSR_REQUEST_HISTORY where REQUEST_ID = " + requestId + " order by REQUEST_HISTORY_ID desc ";
            resultSet = fetchrcrd.sel(sqlRow);
            int counts = Integer.parseInt(resultSet[1][0]);

            String sql = null;
            sql = "select a.REQUEST_ID,to_char(a.ACCOUNT_DATE_1,'dd-mm-yyyy HH12:mi:ss am'),b.STATUS,a.REMARKS,a.REQUEST_FILE_ATTACHED,a.USER_ID,a.REQUEST_HISTORY_ID from SSR_REQUEST_HISTORY a, SSR_REQUEST_STATUS b where REQUEST_ID = " + requestId + " " + searchQ + " and a.REQUEST_STATUS = b.id order by REQUEST_HISTORY_ID desc ";
            //System.out.println("Sql: " + sql);

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            int counter = counts;

            boolean check = true;
            String mesg = "View Attachment";
            while (rs.next()) {
                if ("null".equals(rs.getString(5)) || "-".equals(rs.getString(5)) || "".equals(rs.getString(5))) {
                    check = true;
                    mesg = "No File";
                } else {
                    check = false;
                    mesg = "View Attachment";
                }
                reqDetailTableData.add(i, new reqDetailDtList(counter, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), check, mesg));
                i++;
                counter--;
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

        return reqDetailTableData;
    }

    public List<reqDetailDtList> updateReqDataTableS() {
        int i = 0;
        String searchQ = "";
        String searchQQ = "";
        try {

//            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
//            String reqID = request.getParameter("j_idt47:txt_tktno");
//            String reqTitle = request.getParameter("j_idt47:txt_title");
//            
            String reqID = getSearchReqId();
            String reqTitle = getSearchReqTitle();

            if (reqID == null || reqID.equals("") || reqID.equals("Search By Request No")) {
                searchQQ = "";
            } else {
                searchQQ = " where request_id = " + reqID + " ";
            }

            if (reqTitle == null || reqTitle.equals("") || reqTitle.equals("Search By Request Title")) {
                searchQ = "";
            } else if (reqID == null || reqID.equals("") || reqID.equals("Search By Request No")) {
                searchQ = "Where request_title like '%" + reqTitle + "%' ";
            } else {
                searchQ = "and request_title like '%" + reqTitle + "%' ";
            }

            if (searchQQ == null && searchQ == null || "".equals(searchQQ) && "".equals(searchQ)) {
                searchQQ = "Where request_id = '0000' ";
            }

            filtered = null;
            reqDetailTableData.clear();
            reqDetailTableData = new ArrayList<reqDetailDtList>();
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

            String sql = null;
            sql = "select REQUEST_ID,to_char(REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS a where a.id = REQUEST_STATUS ),REQUEST_DESCRIPTION,REQUESTER,APPROVED_BY,APPROVED_DATE,(select user_name from SSR_LOGIN b where b.user_id=requester) from SSR_REQUEST_INITIATE " + searchQQ + "   " + searchQ + " ";
            //System.out.println("Query " + sql);
            ResultSet rs = null;
            rs = st.executeQuery(sql);
            int counter = 1;

            while (rs.next()) {

                reqDetailTableData.add(i, new reqDetailDtList(counter, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), true, rs.getString(8)));
                i++;
                counter++;
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
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("search_request_detail.xhtml?faces-redirect=true");
        } catch (Exception e) {
        }
        searchReqId = "Search By Request No";
        searchReqTitle = "Search By Request Title";
        return reqDetailTableData;
    }

    public List<reqDetailDtList> updateReqDataTableStaus(String filter) {
        int i = 0;
        try {
            reqDetailTableData = new ArrayList<reqDetailDtList>();
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

            String sql = null;
            sql = "select REQUEST_ID,to_char(REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),(select status from SSR_REQUEST_STATUS a where a.id = REQUEST_STATUS ),REQUEST_DESCRIPTION,REQUESTER,APPROVED_BY,APPROVED_DATE,(select user_name from SSR_LOGIN b where b.user_id=requester) from SSR_REQUEST_INITIATE where requester = '" + sessionUserId + "' and request_status = '" + filter + "' ";

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            int counter = 1;

            while (rs.next()) {

                reqDetailTableData.add(i, new reqDetailDtList(counter, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), true, rs.getString(8)));
                i++;
                counter++;
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
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("search_request_detail.xhtml?faces-redirect=true");
        } catch (Exception e) {
        }
        return reqDetailTableData;
    }

    public void onClickReqDetail(String id, String date, String user, String remarks) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('myDataDialog').show();");
        setDdReqID(id);
        setDdReqDate(date);
        setDdReqUpdateBy(user);
        setDdReqRemarks(remarks);
    }

    public void onClickReqSolDetail(String id, String date, String user, String remarks) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('mySolDataDialog').show();");
        setDdReqID(id);
        setDdReqDate(date);
        setDdReqUpdateBy(user);
        setDdReqRemarks(remarks);
    }

    public String onClickRequestUpdateSubmit(String requestId) {
        try {

            if ("".equals(reqStatus) || "Please Enter Your Remarks.".equals(reqRemarks) || "".equals(reqRemarks)) {
                errorMesg = "Please Enter Remarks.";
            } else {
                dbc = new DbConnection();
                st = dbc.getConnection(false).createStatement();

                String maxValue = null;
                String LastID = "select nvl(max(REQUEST_HISTORY_ID),0)+1 as maxID from SSR_REQUEST_HISTORY";
                rs = st.executeQuery(LastID);
                while (rs.next()) {
                    maxValue = rs.getString("maxID");
                }

                String sql = " insert into SSR_REQUEST_HISTORY (REQUEST_HISTORY_ID,REQUEST_ID,REQUEST_FILE_ATTACHED,REQUEST_STATUS,REMARKS,ACCOUNT_DATE_1,USER_ID) "
                        + " Values( " + maxValue + ", " + requestId + ",'" + reqFileAttached + "'," + reqStatus + ",'" + reqRemarks + "','" + timeStamp + "','" + sessionUsername + "' )";
                //System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {

                    String sqlReq = "Update SSR_REQUEST_INITIATE set REQUEST_STATUS = " + reqStatus + "  where request_id = " + requestId + " ";
                    Adder add = new Adder();
                    String stts = add.ins(sqlReq);
                    errorMesg=null;
                    reqStatus = null;
                    reqRemarks = null;
                    reqFileAttached = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogReqDet').show();");
                    updateReqDataTable(requestId, "");
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

    public String onClickRequestUpdateSubmitFromUser(String requestId, String statusid) {
        try {

            if ("Please Enter Your Remarks.".equals(reqRemarks) || "".equals(reqRemarks)) {
                errorMesg = "Please Enter Remarks.";
            } else {
                dbc = new DbConnection();
                st = dbc.getConnection(false).createStatement();

                String maxValue = null;
                String LastID = "select nvl(max(REQUEST_HISTORY_ID),0)+1 as maxID from SSR_REQUEST_HISTORY";
                rs = st.executeQuery(LastID);
                while (rs.next()) {
                    maxValue = rs.getString("maxID");
                }

                String sql = " insert into SSR_REQUEST_HISTORY (REQUEST_HISTORY_ID,REQUEST_ID,REQUEST_FILE_ATTACHED,REQUEST_STATUS,REMARKS,ACCOUNT_DATE_1,USER_ID) "
                        + " Values( " + maxValue + ", " + requestId + ",'" + reqFileAttached + "'," + statusid + ",'" + reqRemarks + "','" + timeStamp + "','" + sessionUsername + "' )";
                //System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    errorMesg=null;
                    reqStatus = null;
                    reqRemarks = null;
                    reqFileAttached = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogReqDet').show();");
                    updateReqDataTable(requestId, "");
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

    public void preDownloadF(String url) {
        try {
            File file = new File(url);
            InputStream input;
            input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
            //System.out.println("PREP = " + download.getName());
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RequestApproval.class.getName()).log(Level.SEVERE, null, ex);
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
            setReqFileAttached(url);
            //session.setAttribute("fileurl", url);
            System.out.println("New file created!" + url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public class reqDetailDtList {

        private int serialNo;
        private String reqId;
        private String reqDate;
        private String reqStatus;
        private String reqRemarks;
        private String reqFileAttach;
        private String updateUser;
        private boolean enable;
        private String showAttachMesg;

        public reqDetailDtList(int serialNo, String reqId, String reqDate, String reqStatus, String reqRemarks, String reqFileAttach, String updateUser, boolean enable, String showAttachMesg) {
            this.serialNo = serialNo;
            this.reqId = reqId;
            this.reqDate = reqDate;
            this.reqStatus = reqStatus;
            this.reqRemarks = reqRemarks;
            this.reqFileAttach = reqFileAttach;
            this.updateUser = updateUser;
            this.enable = enable;
            this.showAttachMesg = showAttachMesg;
        }

        public String getShowAttachMesg() {
            return showAttachMesg;
        }

        public void setShowAttachMesg(String showAttachMesg) {
            this.showAttachMesg = showAttachMesg;
        }

        public boolean isEnable() {
            return enable;
        }

        public void setEnable(boolean enable) {
            this.enable = enable;
        }

        public int getSerialNo() {
            return serialNo;
        }

        public void setSerialNo(int serialNo) {
            this.serialNo = serialNo;
        }

        public String getReqId() {
            return reqId;
        }

        public void setReqId(String reqId) {
            this.reqId = reqId;
        }

        public String getReqDate() {
            return reqDate;
        }

        public void setReqDate(String reqDate) {
            this.reqDate = reqDate;
        }

        public String getReqStatus() {
            return reqStatus;
        }

        public void setReqStatus(String reqStatus) {
            this.reqStatus = reqStatus;
        }

        public String getReqRemarks() {
            return reqRemarks;
        }

        public void setReqRemarks(String reqRemarks) {
            this.reqRemarks = reqRemarks;
        }

        public String getReqFileAttach() {
            return reqFileAttach;
        }

        public void setReqFileAttach(String reqFileAttach) {
            this.reqFileAttach = reqFileAttach;
        }

        public String getUpdateUser() {
            return updateUser;
        }

        public void setUpdateUser(String updateUser) {
            this.updateUser = updateUser;
        }

    }
}
