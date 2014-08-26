/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.rms.util;

import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author mahtab
 */
public class EmailUtility {
    public static void emailSending(List<String> to,List<String> cc,String from,String subject, String text )
    {
        try{
        // Get system properties
            Properties properties = System.getProperties();
            String host = "portal.akpbsp.org";
          // Setup mail server
           properties.setProperty("smtp.akpbsp.org", host);

          // Get the default Session object.
          Session session = Session.getDefaultInstance(properties);

            // Create a default MimeMessage object.
          MimeMessage message = new MimeMessage(session);

          // Set the RFC 822 "From" header field using the
          // value of the InternetAddress.getLocalAddress method.
          message.setFrom(new InternetAddress(from));
            
            int totalElements = to.size();

            for(int index=0;index<totalElements;index++)
            {
            // Add the given addresses to the specified recipient type.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to.get(index)));
             System.out.println(to.get(index));

            }
            int cctotalElements=cc.size();
            for(int ccindex=0;ccindex<cctotalElements;ccindex++)
            {
            message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc.get(ccindex)));
            }                       
            // Set the "Subject" header field.
            message.setSubject(subject);
            
            // Sets the given String as this part's content,
            // with a MIME type of "text/plain".
            message.setText(text);

            // Send message
            Transport.send(message);

             System.out.println("Message Send.....");
        }
        catch (AddressException e) {
                                    e.printStackTrace();
                            } catch (MessagingException e) {
                                    e.printStackTrace();
                            }
    }
    
    
    
     public static void emailSendingwocc(String to,String from,String subject, String text )
    {
        try{
        // Get system properties
            Properties properties = System.getProperties();
            String host = "portal.akpbsp.org";
          // Setup mail server
           properties.setProperty("smtp.akpbsp.org", host);

          // Get the default Session object.
          Session session = Session.getDefaultInstance(properties);

            // Create a default MimeMessage object.
          MimeMessage message = new MimeMessage(session);

          // Set the RFC 822 "From" header field using the
          // value of the InternetAddress.getLocalAddress method.
          message.setFrom(new InternetAddress(from));
            
          
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
          
          
                          
            // Set the "Subject" header field.
            message.setSubject(subject);
            
            // Sets the given String as this part's content,
            // with a MIME type of "text/plain".
            message.setText(text);

            // Send message
            Transport.send(message);

             System.out.println("Message Send.....");
        }
        catch (AddressException e) {
                                    e.printStackTrace();
                            } catch (MessagingException e) {
                                    e.printStackTrace();
                            }
    }
    
    
    
}
