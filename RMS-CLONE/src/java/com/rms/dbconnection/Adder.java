/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rms.dbconnection;

import java.sql.*;
/**
 *
 * @author mahtab
 */
public class Adder {
    public Adder() {
        super();
        conobj=new DbConnection();
        st=null;
    }
    
    private void ClsCon()
        {
            try
            {
                st.close();
                conobj.freeConnection();
            }
            catch(SQLException ex)
            {
                System.out.println("Error Occured in 'ins' :");
                ex.printStackTrace();
            }
        }
    public String ins(String sql)
       {
           String sts = null;
           try
           {
               System.out.println(sql);
               st = conobj.getConnection(false).createStatement();
               st.executeUpdate(sql);
               conobj.ConCom();
               sts = "Y";
               ClsCon();
           }
           catch(SQLException ex)
           {
               sts = "N";
               conobj.ConRlb();
               System.out.println("Error Occured in 'ins' : " + ex);
               ex.printStackTrace();
               ClsCon();
           }
           return sts;
       }
private DbConnection conobj;
Statement st;
    
}
