/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.dbconnection;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ali
 */
public class FetchResc {

    private DbConnection conobj;
    private Statement stat;
    private ResultSet res;

    public FetchResc() {
        conobj = new DbConnection();
        stat = null;
        res = null;
    }

    private void ClsCon() {
        try {
            //System.out.println("Closing ResultSet,Statement & Connection..");
            res.close();
            stat.close();
            conobj.freeConnection();
            //System.out.println("Closed");
        } catch (SQLException ex) {
            System.out.println("Error Occured in 'sel' while closing Connection :" + ex);
        }
    }

    public String[][] sel(String sql) {
        String recs[][] = (String[][]) null;
        try {
            //System.out.println("QUERY: " + sql);
            
            stat = conobj.getConnection(false).createStatement();
            res = stat.executeQuery(sql);
            ResultSetMetaData rsmd = res.getMetaData();
            int col = rsmd.getColumnCount();
            int ti = 0;
            int ti2 = 1;
            recs = new String[10000][col + 1];
            for (ti = 0; ti < col; ti++) {
                recs[0][ti] = rsmd.getColumnName(ti + 1);
            }

            while (res.next()) {
                for (ti = 0; ti < col; ti++) {
                    recs[ti2][ti] = res.getString(ti + 1);
                }

                ti2++;
            }
            ClsCon();
        } catch (SQLException ex) {
            System.out.println("Error Occured in 'sel' : " + ex);
            ClsCon();
        } catch (Exception x) {
            ClsCon();
            System.out.println("Error Occured in 'sel' : " + x);
        }
        return recs;
    }

    public String[][] selCustom(String sql) {
        String recs[][] = (String[][]) null;
        try {
            //System.out.println("QUERY: " + sql);
            
            stat = conobj.getConnection(false).createStatement();
            res = stat.executeQuery(sql);
            ResultSetMetaData rsmd = res.getMetaData();
            int col = rsmd.getColumnCount();
            int ti = 0;
            int ti2 = 1;
            recs = new String[10000][col + 1];
            for (ti = 0; ti < col; ti++) {
                recs[0][ti] = rsmd.getColumnName(ti + 1);
            }

            while (res.next()) {
                for (ti = 0; ti < col; ti++) {
                    recs[ti2][ti] = res.getString(ti + 1);
                }

                ti2++;
            }
            ClsCon();
        } catch (SQLException ex) {
            System.out.println("Error Occured in 'sel' : " + ex);
            ClsCon();
        } catch (Exception x) {
            ClsCon();
            System.out.println("Error Occured in 'sel' : " + x);
        }
        return recs;
    }
    
    
}
