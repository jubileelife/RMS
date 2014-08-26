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
import java.util.Date;
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
@ManagedBean(name = "Req_Approval")
@SessionScoped
public class RequestApproval implements Serializable {

    private DbConnection dbc;
    private DbConnection dbc2;
    private Statement st;
    private Statement stt;
    private ResultSet rs;
    private String sessionUserId;
    private String sessionUsername;
    private String sessionReportTo;
    private HttpSession session = null;

    private List<ApprovalDdList> filtered;
    private List<ApprovalDdList> tableData = new ArrayList<ApprovalDdList>();
    private ApprovalDdList clickedItem;

    private String approvalScreenFileUrl;
    private String approvalScreenDate;
    private String approvalScreenStatus;
    private String approvalScreenType;
    private String approvalScreenSystem;
    private String approvalScreenSystemMod;
    private String approvalScreenReqPriority;
    private String approvalScreenReqTitle;
    private String approvalScreenReqDesc;
    private String approvalScreenRequester;
    private String approvalScreenReqId;
    private Date expectedDate;
    private String approvalScreenApproveComment;
    private DefaultStreamedContent download;
    private String destination = "D:\\rms_upload\\";
    private boolean dl = true;
    private String errorMesg = "";
    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public RequestApproval() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");

    }

    public void redirectRequestApproval() {
        updateDataTable();
        expectedDate = null;
        approvalScreenApproveComment = null;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
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

    public String getApprovalScreenApproveComment() {
        return approvalScreenApproveComment;
    }

    public void setApprovalScreenApproveComment(String approvalScreenApproveComment) {
        this.approvalScreenApproveComment = approvalScreenApproveComment;
    }

    public Date getExpectedDate() {
        return expectedDate;
    }

    public void setExpectedDate(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    public String getApprovalScreenReqId() {
        return approvalScreenReqId;
    }

    public void setApprovalScreenReqId(String approvalScreenReqId) {
        this.approvalScreenReqId = approvalScreenReqId;
    }

    public String getApprovalScreenFileUrl() {
        return approvalScreenFileUrl;
    }

    public void setApprovalScreenFileUrl(String approvalScreenFileUrl) {
        this.approvalScreenFileUrl = approvalScreenFileUrl;
    }

    public String getApprovalScreenRequester() {
        return approvalScreenRequester;
    }

    public void setApprovalScreenRequester(String approvalScreenRequester) {
        this.approvalScreenRequester = approvalScreenRequester;
    }

    public String getApprovalScreenDate() {
        return approvalScreenDate;
    }

    public void setApprovalScreenDate(String approvalScreenDate) {
        this.approvalScreenDate = approvalScreenDate;
    }

    public String getApprovalScreenStatus() {
        return approvalScreenStatus;
    }

    public void setApprovalScreenStatus(String approvalScreenStatus) {
        this.approvalScreenStatus = approvalScreenStatus;
    }

    public String getApprovalScreenType() {
        return approvalScreenType;
    }

    public void setApprovalScreenType(String approvalScreenType) {
        this.approvalScreenType = approvalScreenType;
    }

    public String getApprovalScreenSystem() {
        return approvalScreenSystem;
    }

    public void setApprovalScreenSystem(String approvalScreenSystem) {
        this.approvalScreenSystem = approvalScreenSystem;
    }

    public String getApprovalScreenSystemMod() {
        return approvalScreenSystemMod;
    }

    public void setApprovalScreenSystemMod(String approvalScreenSystemMod) {
        this.approvalScreenSystemMod = approvalScreenSystemMod;
    }

    public String getApprovalScreenReqPriority() {
        return approvalScreenReqPriority;
    }

    public void setApprovalScreenReqPriority(String approvalScreenReqPriority) {
        this.approvalScreenReqPriority = approvalScreenReqPriority;
    }

    public String getApprovalScreenReqTitle() {
        return approvalScreenReqTitle;
    }

    public void setApprovalScreenReqTitle(String approvalScreenReqTitle) {
        this.approvalScreenReqTitle = approvalScreenReqTitle;
    }

    public String getApprovalScreenReqDesc() {
        return approvalScreenReqDesc;
    }

    public void setApprovalScreenReqDesc(String approvalScreenReqDesc) {
        this.approvalScreenReqDesc = approvalScreenReqDesc;
    }

    public ApprovalDdList getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(ApprovalDdList clickedItem) {
        this.clickedItem = clickedItem;
    }

    public List<ApprovalDdList> getTableData() {
        return tableData;
    }

    public void setTableData(List<ApprovalDdList> tableData) {
        this.tableData = tableData;
    }

    public List<ApprovalDdList> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<ApprovalDdList> filtered) {
        this.filtered = filtered;
    }

    public void redirectUrlApprove() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("requestApprovalList.xhtml?faces-redirect=true");
    }

    public List<ApprovalDdList> updateDataTable() {
        int i = 0;
        try {
            tableData = new ArrayList<ApprovalDdList>();
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();
            String sql = null;
            sql = "select REQUEST_ID,b.REQUEST_TYPE,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.user_name,REQUEST_TITLE from SSR_REQUEST_INITIATE a, SSR_REQUEST_TYPE b, SSR_LOGIN c where REPORTING_TO = " + sessionUserId + " and APPROVAL_REQUIRED = 1 "
                    + " and a.requester = c.user_id and a.request_type = b.id   ";
            //System.out.println("Sql: " + sql);

            ResultSet rs = null;
            rs = st.executeQuery(sql);
            while (rs.next()) {
                tableData.add(i, new ApprovalDdList(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)));
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

    public String onClickEvent() {
        try {
        FetchResc fetchrcrd = new FetchResc();
        String resultSet[][] = new String[2][11];
        String sql = null;
        sql = "select b.user_name,to_char(a.REQUEST_DATE,'dd-mm-yyyy HH12:mi:ss am'),c.REQUEST_TYPE,d.name,e.module_name,a.REQUEST_TITLE,a.REQUEST_DESCRIPTION,a.REQUEST_PRIORITY,a.REQUEST_FILE_ATTACHED \n"
                + " from SSR_REQUEST_INITIATE a, SSR_LOGIN b, SSR_REQUEST_TYPE c, SSR_SYSTEMS d,SSR_SYSTEM_MODULE e  \n"
                + "where a.request_id = " + clickedItem.getReqId() + " and a.REQUESTER = b.user_id and a.REQUEST_TYPE = c.id and a.system_id = d.id and e.id = a.SYSTEM_MODULE";

        errorMesg = "";
        resultSet = fetchrcrd.sel(sql);
        approvalScreenReqId = clickedItem.getReqId();
        approvalScreenRequester = resultSet[1][0];
        approvalScreenDate = resultSet[1][1];
        approvalScreenType = resultSet[1][2];
        approvalScreenSystem = resultSet[1][3];
        approvalScreenSystemMod = resultSet[1][4];
        approvalScreenReqTitle = resultSet[1][5];
        approvalScreenReqDesc = resultSet[1][6];
        approvalScreenReqPriority = resultSet[1][7];
        approvalScreenFileUrl = resultSet[1][8];

        if ("null".equals(approvalScreenFileUrl) || "-".equals(approvalScreenFileUrl) || "".equals(approvalScreenFileUrl)) {
            setDl(true);
        } else {
            setDl(false);
        }
        } catch (Exception e) {
        }
        return "requestapproval?faces-redirect=true";
    }

    public String approveSubmit() {
        try {
            if ("".equals(approvalScreenApproveComment)) {
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or Password.",""));
                errorMesg = "Pleas enter comments.";
            } else {
                String sql = "Update SSR_REQUEST_INITIATE set APPROVED_BY = '" + sessionUsername + "' , APPROVED_DATE = '" + timeStamp + "' , APPROVAL_COMMENT = '" + approvalScreenApproveComment + "' , APPROVAL_REQUIRED = 2,  "
                        + " APPROVAL_FILE_ATTACH = '" + (String) session.getAttribute("fileurlA") + "' where request_id = " + approvalScreenReqId + " ";
                //System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    updateDataTable();
                    session.setAttribute("fileurlA", "");
                    errorMesg=null;
                    expectedDate = null;
                    approvalScreenApproveComment = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogApp').show();");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in Insertion: " + e);
        }
        return null;
    }

    public String approveReject() {
        try {
            if ("".equals(approvalScreenApproveComment)) {
                //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or Password.",""));
                errorMesg = "Please Enter Comments to Reject.";
            } else {
                String sql = "Update SSR_REQUEST_INITIATE set rejected_by = '" + sessionUsername + "' , rejected_date = '" + timeStamp + "' , APPROVAL_COMMENT = '" + approvalScreenApproveComment + "', rejected = 1,APPROVAL_REQUIRED = 3, "
                        + " APPROVAL_FILE_ATTACH = '" + (String) session.getAttribute("fileurlA") + "' where request_id = " + approvalScreenReqId + " ";
                //System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    updateDataTable();
                    session.setAttribute("fileurlA", "");
                    errorMesg=null;
                    expectedDate = null;
                    approvalScreenApproveComment = null;
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogRej').show();");
                }
            }
        } catch (Exception e) {
            System.out.println("Exception in Insertion: " + e);
        }
        return null;
    }

    public void preDownload() {
        try {
            setDl(false);
            File file = new File(approvalScreenFileUrl);
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
            session.setAttribute("fileurlA", url);
            System.out.println("New file created!" + url);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public class ApprovalDdList {

        private String reqId;
        private String reqType;
        private String reqDate;
        private String requesterName;
        private String reqTitle;

        public ApprovalDdList(String reqId, String reqType, String reqDate, String requesterName, String reqTitle) {
            this.reqId = reqId;
            this.reqType = reqType;
            this.reqDate = reqDate;
            this.requesterName = requesterName;
            this.reqTitle = reqTitle;
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

    }

}
