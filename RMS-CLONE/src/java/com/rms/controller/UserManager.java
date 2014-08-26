/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.controller;

import com.rms.dbconnection.Adder;
import com.rms.dbconnection.DbConnection;
import com.rms.util.AESencrp;
import com.rms.util.EmailUtility;
import java.io.IOException;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.context.RequestContext;

/**
 *
 * @author Hassan Ali
 */
@ManagedBean(name = "user")
@SessionScoped
public class UserManager implements Serializable {

    private String username;
    private String password;
    private String current;
    private String errorMesg = "";
    private String errorMesgReg = "";
    private DbConnection dbc;
    private Statement st;
    private ResultSet rs;
    private HttpSession session = null;
    private String logoutValue = null;

    private String empCode = null;
    private String empEmail = null;
    private String empPassword = null;

    private String forgotEmpId = null;
    private String forgotEmpEmail = null;
    private String forgotEmpPass = null;

    private String menuAssignmentCheck;
    private String menuRequestListCheck;

    private static final String CHAR_LIST = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final int RANDOM_STRING_LENGTH = 10;

    public static int a;

    public static SimpleDateFormat dateFormate = new SimpleDateFormat("dd-MMM-yyyy");

    public String generateRandomString() {

        StringBuffer randStr = new StringBuffer();
        for (int i = 0; i < RANDOM_STRING_LENGTH; i++) {
            int number = getRandomNumber();
            char ch = CHAR_LIST.charAt(number);
            randStr.append(ch);
        }
        return randStr.toString();
    }

    private int getRandomNumber() {
        int randomInt = 0;
        Random randomGenerator = new Random();
        randomInt = randomGenerator.nextInt(CHAR_LIST.length());
        if (randomInt - 1 == -1) {
            return randomInt;
        } else {
            return randomInt - 1;
        }
    }

    public String getForgotEmpPass() {
        return forgotEmpPass;
    }

    public void setForgotEmpPass(String forgotEmpPass) {
        this.forgotEmpPass = forgotEmpPass;
    }

    public String getForgotEmpId() {
        return forgotEmpId;
    }

    public void setForgotEmpId(String forgotEmpId) {
        this.forgotEmpId = forgotEmpId;
    }

    public String getForgotEmpEmail() {
        return forgotEmpEmail;
    }

    public void setForgotEmpEmail(String forgotEmpEmail) {
        this.forgotEmpEmail = forgotEmpEmail;
    }

    public String getMenuAssignmentCheck() {
        return menuAssignmentCheck;
    }

    public void setMenuAssignmentCheck(String menuAssignmentCheck) {
        this.menuAssignmentCheck = menuAssignmentCheck;
    }

    public String getMenuRequestListCheck() {
        return menuRequestListCheck;
    }

    public void setMenuRequestListCheck(String menuRequestListCheck) {
        this.menuRequestListCheck = menuRequestListCheck;
    }

    public String getErrorMesgReg() {
        return errorMesgReg;
    }

    public void setErrorMesgReg(String errorMesgReg) {
        this.errorMesgReg = errorMesgReg;
    }

    public String getLogoutValue() {
        return logoutText();
    }

    public void setLogoutValue(String logoutValue) {
        this.username = logoutValue;
    }

    public String logoutText() {
        String str = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRequestURI();
        if (str.equals("/RMS/faces/login.xhtml")) {
            return "display: none;text-decoration: none;color: white;font-weight: bold;";
        } else {
            return "font-family:Verdana, Geneva, sans-serif; font-size: 12px;display: block;text-decoration: none;color: white;font-weight: bold;margin-top: -7px;";
        }
    }

    public String getEmpCode() {
        return empCode;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getEmpPassword() {
        return empPassword;
    }

    public void setEmpPassword(String empPassword) {
        this.empPassword = empPassword;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getErrorMesg() {
        return errorMesg;
    }

    public void setErrorMesg(String errorMesg) {
        this.errorMesg = errorMesg;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void checkAlreadyLogin() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);

        if ((String) session.getAttribute("userid") == null) {

        } else {
            try {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                context.redirect("/RMS/faces/content/search_request.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(UserManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public String login() throws Exception {

        try {
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement(); //AESencrp.decrypt(new String(password))
            String sql = "select user_id,user_name,email,report_to,roles from ssr_login where user_id = '" + username + "' and password = '" + AESencrp.encrypt(password) + "' ";

            rs = st.executeQuery(sql);
            String userId = null;
            String user_name = null;
            String email = null;
            String report_to = null;
            String role = null;

            while (rs.next()) {
                userId = rs.getString("user_id");
                user_name = rs.getString("user_name");
                email = rs.getString("email");
                report_to = rs.getString("report_to");
                role = rs.getString("roles");
                current = rs.getString("user_id");
                session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                session.setAttribute("userid", userId);
                session.setAttribute("username", user_name);
                session.setAttribute("email", email);
                session.setAttribute("report_to", report_to);
                session.setAttribute("role", role);

                if ("1".equals(role) || role == "1") {
                    menuAssignmentCheck = "";
                    menuRequestListCheck = "";
                } else if ("2".equals(role) || role == "2") {
                    menuAssignmentCheck = "display:none;";
                    menuRequestListCheck = "";
                } else {
                    menuAssignmentCheck = "display:none;";
                    menuRequestListCheck = "display:none;";
                }

            }
            if (userId == null && user_name == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Invalid Username or Password.", ""));
                errorMesg = "Invalid Username or Password.";
                return (username = password = null);
            } else {

                return "content/search_request?faces-redirect=true";
            }
        } catch (SQLException sqe) {
            ClsCon();
            System.out.println("Exception in login query: " + sqe.getMessage());
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

    public static String getFullURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }

    public void userForgotCheck() {

        try {
            
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String random = request.getParameter("abc");
            //System.out.println("------------- " + random);
            
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

            String resetPas = "";
            String resetUid = "";
            String LastIDD = "select password_reset,user_id from SSR_LOGIN where PASSWORD_RESET_STRING = '" + random + "' ";
            //System.out.println("------------- " + LastIDD);
            rs = st.executeQuery(LastIDD);
            while (rs.next()) {
                resetPas = rs.getString(1);
                resetUid = rs.getString(2);
            }

            if ("".equals(resetPas) || "".equals(resetPas)) {
            } else {
                String sql = "Update SSR_LOGIN set PASSWORD_RESET = '' ,PASSWORD = '" + resetPas + "',PASSWORD_RESET_STRING = ''  "
                        + "  where user_id = " + resetUid + " ";
                //System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {
                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('myDialogVarPasswordConfrm').show();");
                }
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

    public void userForgot() {

        try {
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

            String uName = "";
            String uId = "";
            String uEmail = "";
            String LastIDD = "select user_name,user_id,email from SSR_LOGIN where user_id = '" + forgotEmpId + "' and email = '" + forgotEmpEmail + "' ";
            rs = st.executeQuery(LastIDD);
            while (rs.next()) {
                uName = rs.getString(1);
                uId = rs.getString(2);
                uEmail = rs.getString(3);
            }

            if ("".equals(uName) || "".equals(uEmail)) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Provided information is incorrect.", ""));
            } else {
                String encrypt_pass = AESencrp.encrypt(forgotEmpPass);
                String rand = generateRandomString();
                String sql = "Update SSR_LOGIN set PASSWORD_RESET = '" + encrypt_pass + "',PASSWORD_RESET_STRING = '" + rand + "'  "
                        + "  where user_id = " + uId + " ";
                //System.out.println("------------- " + sql);
                Adder ad = new Adder();
                String sts = ad.ins(sql);
                if (sts.equals("Y")) {

                    forgotEmpEmail = "";
                    forgotEmpId = "";
                    forgotEmpPass = "";

                    RequestContext context = RequestContext.getCurrentInstance();
                    context.execute("PF('dlgforgot').hide();");
                    context.execute("PF('myDialogVarPassword').show();");

                    String subject = "Requirement Management System - Reset Password";
                    String text = "Dear " + uName + ",\n\nYour request for changing your account password is submitted. Please click on the mentioned url to change your password .\n\n      http://rnd:7001/RMS/faces/changePassword.xhtml?pattern=" + rand + " "
                            + "\n\nRegards,\nRMS-Admin"
                            + "\nTechnology, Project & Quality";
                    EmailUtility.emailSendingwocc(uEmail, "RMS-Admin@jubileelife.com", subject, text);
                    System.out.println("----" + text);

                    //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have already registered.", ""));
                }
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

    public String userRegister() {

        try {
            
            dbc = new DbConnection();
            st = dbc.getConnection(false).createStatement();

             if ("".equals(empEmail) || "".equals(empCode) ||  "".equals(empPassword) ) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("All fields are mandatory.", ""));
                return "";
            }
            
            
            String uName = "";
            String LastIDD = "select user_name from SSR_LOGIN where user_id = '" + empCode + "' ";
            rs = st.executeQuery(LastIDD);
            while (rs.next()) {
                uName = rs.getString(1);
            }

            if ("".equals(uName) || uName == null) {
                
            } else {
                errorMesgReg = "You have already registerd.";
                empPassword = null;
                empEmail = null;
                empCode = null;

                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("You have already registered.", ""));
                return "";
            }

            String empId = null;
            String fullName = null;
            String reportedTo = null;
            String email = null;
            String dept = null;
            //String LastID = "select EMPLOYEE_CODE,initcap(FIRST_NAME)||' '||initcap(MIDDLE_NAME)||' '||initcap(LAST_NAME),REPORTED_TO,lower(EMAIL_ADDRESS2),dept_code from hrms_employee where EMPLOYEE_CODE = '" + empCode + "'  ";
            //String LastID = "select EMPLOYEE_CODE,initcap(FIRST_NAME)||' '||initcap(MIDDLE_NAME)||' '||initcap(LAST_NAME),REPORTED_TO,lower(EMAIL_ADDRESS2),dept_code from hrms_employee where EMPLOYEE_CODE = '" + empCode + "' and lower(EMAIL_ADDRESS2) = '" + empEmail + "' or lower(EMAIL_ADDRESS1) = '" + empEmail + "' ";
            String LastID = "select  \n" +
                            "employee_code, \n" +
                            "first_name|| ' ' || Middle_name || ' '|| Last_name Employee_name, \n" +
                            "reported_to, \n" +
                            "NVL(decode(INSTR(EMAIL_ADDRESS1,'jubilee'),0,EMAIL_ADDRESS2,EMAIL_ADDRESS1),EMAIL_ADDRESS2) Email_Address,\n" +
                            "dept_code\n" +
                            "from hrms_employee\n" +
                            "where (instr(email_address2, 'jubilee') > 0  OR instr(email_address1, 'jubilee') > 0 )"
                             + " and EMPLOYEE_CODE = '" + empCode + "' and lower(EMAIL_ADDRESS2) = '" + empEmail + "' or lower(EMAIL_ADDRESS1) = '" + empEmail + "'";
            //System.out.println(LastID);
            rs = st.executeQuery(LastID);
            while (rs.next()) {
                empId = rs.getString(1);
                fullName = rs.getString(2).replaceAll("\\s+", " ").trim();
                reportedTo = rs.getString(3);
                email = rs.getString(4);
                dept = rs.getString(5);
            }

            
            if ("".equals(empId) || empId == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Provided Information is incorrect.", ""));
                errorMesgReg = "Please Enter Correct Information.";
                return "";
            }
            
            if ("".equals(email) || email == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your official email Address not updated in the system, Please contact HR.", ""));
                errorMesgReg = "Your official email address not updated in the system, Please contact HR.";
                return "";
            }
            
            if ("".equals(reportedTo) || reportedTo == null) {
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Your reporting manager id not updated in the system, Please contact HR.", ""));
                errorMesgReg = "Your reporting manager id not updated in the system, Please contact HR.";
                return "";
            }
            
            String userRoles = "";
            String userCheck = "";
            if ("08".equals(dept) || dept == "08") {
                userCheck = "1";
                userRoles = "2";
            } else {
                userCheck = "0";
                userRoles = "3";
            }

            String encrypt_pass = AESencrp.encrypt(empPassword);
            String sql = "Insert Into SSR_LOGIN(USER_ID,USER_NAME,PASSWORD,REPORT_TO,EMAIL,roles,isassign)\n"
                    + "values('" + empCode + "','" + fullName + "','" + encrypt_pass + "','" + reportedTo + "','" + email + "'," + userRoles + ",'" + userCheck + "')";
            //System.out.println(sql);
            Adder ad = new Adder();
            String sts = ad.ins(sql);
            if (sts.equals("Y")) {

                RequestContext context = RequestContext.getCurrentInstance();
                context.execute("PF('dlg').hide();");
                context.execute("PF('myDialogVarLogin').show();");

                String subject = "Requirement Management System - User Registration";
                String text = "Dear " + fullName + ",\n\n Your account is ready to go. Just Sign In. \nTo connect:\n     1. Go to http://rnd:7001/RMS \n     2.Use the following credentials to sign in:\n     EmployeeId: <b>" + empId + "</b>\n     Password: <b>" + empPassword + "</b> "
                        + "\n\nRegards,\nRMS-Admin"
                        + "\nTechnology, Project & Quality";
                EmailUtility.emailSendingwocc(email, "RMS-Admin@jubileelife.com", subject, text);
                
                empPassword = null;
                empEmail = null;
                empCode = null;
                
            } else {
                System.out.println("Error Occured");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error Occured in user creation", ""));
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

        return "";
    }

    public String logout() {
        ExternalContext external_cont = FacesContext.getCurrentInstance().getExternalContext();
        external_cont.invalidateSession();
        if (external_cont.getRequestCookieMap().get("opentoken") != null) {
            external_cont.addResponseCookie("opentoken", null, Collections.<String, Object>singletonMap("maxAge", 0));
        }
        return "../login.xhtml?faces-redirect=true";
    }

    public boolean isLoggedIn() {
        return current != null;
    }

    private void ClsCon() {
        try {
            //System.out.println("Closing ResultSet,Statement & Connection..");
            rs.close();
            st.close();
            dbc.freeConnection();
            //System.out.println("Closed");
        } catch (SQLException ex) {
            System.out.println("Error Occured in 'sel' while closing Connection :" + ex);
        }
    }

}
