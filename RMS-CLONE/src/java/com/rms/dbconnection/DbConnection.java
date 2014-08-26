/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rms.dbconnection;

import java.sql.Connection;
import java.sql.SQLException;
import oracle.jdbc.pool.OracleDataSource;

/**
 *
 * @author Hassan Ali
 */
public class DbConnection {

    private static Connection conn = null;

    public synchronized Connection getConnection(boolean flag)
            throws SQLException {
        
        try {

            //String jdbcUrl = "jdbc:oracle:thin:@clastst.jubileelife.com:1521:CLASLIVE";
            //String userid = "devgl";
            //String password = "devgl_123";
            String jdbcUrl = "jdbc:oracle:thin:@localhost:1521:xe";
            String userid = "rms";
            String password = "ali123";
            OracleDataSource datasource = new OracleDataSource();
            datasource.setURL(jdbcUrl);
            conn = datasource.getConnection(userid, password);
            //System.out.println("Connection Successfully ");
            //System.out.println("URL: " + jdbcUrl + " Userid " + userid + " Password " + password);
            conn.setAutoCommit(flag);

        } catch (Exception exception) {
            System.out.println("connection()Naming execption:" + exception);
            //exception.printStackTrace();
        }
        return conn;
    }

    public void release(Connection cn) {
        try {
            cn.close();
            //System.out.println("Connectin is released successfully");
        } catch (Exception localSQLException) {
            System.out.println("Close Time Exactly()" + localSQLException.getMessage());
        }
    }

     public synchronized void freeConnection()
        {
            try
            {
                //System.out.println("Closing Connection..");
                conn.close();
                //System.out.println("Connection Closed!");
            }
            catch(SQLException sqlexception)
            {
                System.out.println("Close Time Exactly()"+sqlexception.getMessage());
                sqlexception.printStackTrace();
            }
        }
    public void ConCom()
       {
           try
           {
               //System.out.println("Performing connection commit");
               conn.commit();
               //System.out.println("connection commited");
           }
           catch(SQLException ex)
           {
               System.out.println("Error occured in commiting connection");
               ex.printStackTrace();
           }
       }
    public void ConRlb()
        {
            try
            {
                //System.out.println("Performing connection rollback");
                conn.rollback();
                //System.out.println("connection rollback successfull");
            }
            catch(SQLException ex)
            {
                System.out.println("Error occured in connection rollback");
                ex.printStackTrace();
            }
        }
    
    
}
