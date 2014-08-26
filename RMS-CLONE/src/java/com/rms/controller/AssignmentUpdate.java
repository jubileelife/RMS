/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import com.rms.dbconnection.Adder;
import com.rms.dbconnection.DbConnection;
import com.rms.dbconnection.FetchResc;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
@ManagedBean(name = "assignment_update")
@SessionScoped
public class AssignmentUpdate implements Serializable {

    private DbConnection dbc;
    private DbConnection dbc2;
    private Statement st;
    private Statement stt;
    private ResultSet rs;
    private String sessionUserId;
    private String sessionUsername;
    private String sessionReportTo;
    private String role;
    private HttpSession session = null;

    private String assignUpdatereqId;
    private String assignUpdateAssignId;
    private String assignUpdateAssignCatergory;
    private Date assignUpdateDate;
    private String assignUpdateDesc;
    private String assignUpdateStatus;

    FetchResc fthDdRecord = new FetchResc();
    String resultSet[][] = new String[2][11];

    private List<assignmentUpdateDdList> filtered;
    private List<assignmentUpdateDdList> tableData = new ArrayList<assignmentUpdateDdList>();
    private assignmentUpdateDdList clickedItem;

    private String destination = "D:\\rms_upload\\";
    private boolean dl = true;
    private boolean dlapp = true;
    private String errorMesg = "";

    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());
    SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    public String getAssignUpdateAssignCatergory() {
        return assignUpdateAssignCatergory;
    }

    public void setAssignUpdateAssignCatergory(String assignUpdateAssignCatergory) {
        this.assignUpdateAssignCatergory = assignUpdateAssignCatergory;
    }
    
    public List<assignmentUpdateDdList> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<assignmentUpdateDdList> filtered) {
        this.filtered = filtered;
    }

    public List<assignmentUpdateDdList> getTableData() {
        return tableData;
    }

    public void setTableData(List<assignmentUpdateDdList> tableData) {
        this.tableData = tableData;
    }

    public assignmentUpdateDdList getClickedItem() {
        return clickedItem;
    }

    public void setClickedItem(assignmentUpdateDdList clickedItem) {
        this.clickedItem = clickedItem;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public String getAssignUpdateStatus() {
        return assignUpdateStatus;
    }

    public void setAssignUpdateStatus(String assignUpdateStatus) {
        this.assignUpdateStatus = assignUpdateStatus;
    }

    public String getAssignUpdatereqId() {
        return assignUpdatereqId;
    }

    public void setAssignUpdatereqId(String assignUpdatereqId) {
        this.assignUpdatereqId = assignUpdatereqId;
    }

    public String getAssignUpdateAssignId() {
        return assignUpdateAssignId;
    }

    public void setAssignUpdateAssignId(String assignUpdateAssignId) {
        this.assignUpdateAssignId = assignUpdateAssignId;
    }

    public Date getAssignUpdateDate() {
        return assignUpdateDate;
    }

    public void setAssignUpdateDate(Date assignUpdateDate) {
        this.assignUpdateDate = assignUpdateDate;
    }

    public String getAssignUpdateDesc() {
        return assignUpdateDesc;
    }

    public void setAssignUpdateDesc(String assignUpdateDesc) {
        this.assignUpdateDesc = assignUpdateDesc;
    }

    public AssignmentUpdate() {
        session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
        sessionUserId = (String) session.getAttribute("userid");
        sessionUsername = (String) session.getAttribute("username");
        sessionReportTo = (String) session.getAttribute("report_to");
        role = (String) session.getAttribute("role");
    }

    public String onUpdateAssignment(String reqid, String assignid, String assignCat) {
        updateAssignUpdateDataTable();
        setAssignUpdatereqId(reqid);
        setAssignUpdateAssignId(assignid);
        setAssignUpdateAssignCatergory(assignCat);
        return "assignmentUpdate.xhtml?faces-redirect=true";

    }

    public String updateAssignment() {
        try {
            if ("".equals(assignUpdateDesc) || "".equals(assignUpdateStatus)) {
                errorMesg = "Please Enter Status and Description.";
            } else {

                dbc = new DbConnection();
                st = dbc.getConnection(false).createStatement();
                HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
                String reqDate = request.getParameter("j_idt43:taskid");

                String maxValue = null;
                String LastID = "select nvl(max(ASSIGN_HISTORY_ID),0)+1 as maxID from SSR_ASSIGNMENT_HISTORY";
                rs = st.executeQuery(LastID);
                while (rs.next()) {
                    maxValue = rs.getString("maxID");
                }

                if (sessionUserId != null) {
                    String sql = "Insert Into SSR_ASSIGNMENT_HISTORY(ASSIGN_HISTORY_ID,request_id,ASSIGNMENT_ID,STATUS,DESCCRIPTION,UPDATE_BY,UPDATE_DATE)\n"
                            + "values(" + maxValue + "," + assignUpdatereqId + "," + assignUpdateAssignId + ",'" + assignUpdateStatus + "','" + assignUpdateDesc + "','" + sessionUsername + "','" + timeStamp + "')";
                    System.out.println(sql);
                    Adder ad = new Adder();
                    String sts = ad.ins(sql);
                    if (sts.equals("Y")) {
                        errorMesg=null;
                        assignUpdatereqId = null;
                        assignUpdateAssignId = null;
                        assignUpdateStatus = null;
                        assignUpdateDesc = null;
                        RequestContext context = RequestContext.getCurrentInstance();
                        context.execute("PF('myDialogReqDetAssign').show();");
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

    public List<assignmentUpdateDdList> updateAssignUpdateDataTable() {
        int i = 0;
        try {
            tableData = new ArrayList<assignmentUpdateDdList>();
            dbc = new DbConnection();
            dbc2 = new DbConnection();
            st = null;
            st = dbc.getConnection(false).createStatement();
            
            stt = null;
            stt = dbc2.getConnection(false).createStatement();
            String sql = null;

            int maxValue = 0;
            String LastID = "select count(*) as maxID from SSR_ASSIGNMENT_HISTORY where request_id = " + assignUpdatereqId + " and ASSIGNMENT_ID = " + assignUpdateAssignId + " ";
            rs = st.executeQuery(LastID);
            while (rs.next()) {
                maxValue = Integer.parseInt(rs.getString("maxID"));
            }

            sql = "select REQUEST_ID,ASSIGNMENT_ID,(select status from SSR_REQUEST_STATUS b where b.id = h.STATUS ),DESCCRIPTION,UPDATE_BY,UPDATE_DATE from SSR_ASSIGNMENT_HISTORY h where request_id = " + assignUpdatereqId + " and ASSIGNMENT_ID = " + assignUpdateAssignId + " order by ASSIGN_HISTORY_ID desc ";
            ResultSet rs = null;
            rs = stt.executeQuery(sql);
            while (rs.next()) {
                tableData.add(i, new assignmentUpdateDdList(maxValue, rs.getString(1), rs.getString(2), rs.getString(6), rs.getString(5), rs.getString(3), rs.getString(4)));
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

    public class assignmentUpdateDdList {

        private int serial;
        private String assignreqId;
        private String assginupdatereqassignId;
        private String assignreqAssignUpdateDate;
        private String assingUpdateBy;
        private String assignStatus;
        private String assignDesc;

        public assignmentUpdateDdList(int serial, String assignreqId, String assginupdatereqassignId, String assignreqAssignUpdateDate, String assingUpdateBy, String assignStatus, String assignDesc) {
            this.serial = serial;
            this.assignreqId = assignreqId;
            this.assginupdatereqassignId = assginupdatereqassignId;
            this.assignreqAssignUpdateDate = assignreqAssignUpdateDate;
            this.assingUpdateBy = assingUpdateBy;
            this.assignStatus = assignStatus;
            this.assignDesc = assignDesc;

        }

        public int getSerial() {
            return serial;
        }

        public void setSerial(int serial) {
            this.serial = serial;
        }

        public String getAssignreqId() {
            return assignreqId;
        }

        public void setAssignreqId(String assignreqId) {
            this.assignreqId = assignreqId;
        }

        public String getAssginupdatereqassignId() {
            return assginupdatereqassignId;
        }

        public void setAssginupdatereqassignId(String assginupdatereqassignId) {
            this.assginupdatereqassignId = assginupdatereqassignId;
        }

        public String getAssignreqAssignUpdateDate() {
            return assignreqAssignUpdateDate;
        }

        public void setAssignreqAssignUpdateDate(String assignreqAssignUpdateDate) {
            this.assignreqAssignUpdateDate = assignreqAssignUpdateDate;
        }

        public String getAssingUpdateBy() {
            return assingUpdateBy;
        }

        public void setAssingUpdateBy(String assingUpdateBy) {
            this.assingUpdateBy = assingUpdateBy;
        }

        public String getAssignStatus() {
            return assignStatus;
        }

        public void setAssignStatus(String assignStatus) {
            this.assignStatus = assignStatus;
        }

        public String getAssignDesc() {
            return assignDesc;
        }

        public void setAssignDesc(String assignDesc) {
            this.assignDesc = assignDesc;
        }

    }

}
