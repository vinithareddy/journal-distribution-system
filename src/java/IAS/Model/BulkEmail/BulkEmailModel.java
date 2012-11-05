/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package IAS.Model.BulkEmail;

import IAS.Class.JDSLogger;
import IAS.Class.Queries;
import IAS.Class.ServletContextInfo;
import IAS.Class.msgsend;
import IAS.Class.util;
import IAS.Model.JDSModel;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.log4j.Logger;

/**
 *
 * @author aloko
 */
public class BulkEmailModel extends JDSModel{

    private static final Logger logger = JDSLogger.getJDSLogger("IAS.Model.BulkEmailModel");

    public BulkEmailModel(HttpServletRequest request) throws SQLException{
        super(request);
    }

    public String validateEmail() throws ParserConfigurationException, IOException, TransformerConfigurationException, TransformerException{

        String to = request.getParameter("to");
        String message = "";
        boolean success = true;
        try {
            //new InternetAddress(to).validate();
            Address[] toUser = InternetAddress.parse(to, false);
            for(Address s: toUser){
                new InternetAddress(s.toString()).validate();
            }
        } catch (AddressException ex) {
            message = ex.getMessage();
            success = false;
        }

        HashMap<String, String> xmlResponse = new HashMap<>();
        String successValue = (success == true) ? "1" : "0";
        xmlResponse.put("success", successValue);
        xmlResponse.put("message", message);
        String xml = util.createXMLResponse(xmlResponse);

        //String xml = util.convertStringToXML(successValue, "success");

        return xml;
    }

    public String sendEmail() throws SQLException, ParserConfigurationException, TransformerException, IOException, AddressException{

        String subject  = request.getParameter("subject");
        //String msg      = request.getParameter("content");
        String encodedMessage = request.getParameter("content");
        String msg = util.decodeURIComponent(encodedMessage);
        String selall   = request.getParameter("selectFromDb");
        String personal   = request.getParameter("personal");
        String isc   = request.getParameter("isc");
        String ii   = request.getParameter("ii");
        String ic   = request.getParameter("ic");
        String free   = request.getParameter("free");
        String fp   = request.getParameter("fp");
        String fi   = request.getParameter("fp");

        String emailIDs = "";
        String sql = null;
        sql = Queries.getQuery("get_email_ids");
        int  first = 0;
        if (personal.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtypecode = 'IP'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtypecode = 'IP'";
            }
        }
        if (isc.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtypecode = 'IC'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtypecode = 'IC'";
            }
        }
        if (ii.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtypecode = 'II'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtypecode = 'II'";
            }
        }
        if (ic.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtypecode = 'IN'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtypecode = 'IN'";
            }
        }
        if (free.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtype = 'free'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtype = 'free'";
            }
        }
        if (fp.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtypecode = 'FP'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtypecode = 'FP'";
            }
        }
        if (fi.equals("1")){
            if (first == 0){
                sql += " and (subscriber_type.subtypecode = 'FI'";
                first = 1;
            }
            else{
                sql += " or subscriber_type.subtypecode = 'FI'";
            }
        }

        if (first == 1){
            sql += ")";
            PreparedStatement st = conn.prepareStatement(sql, com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = this.db.executeQueryPreparedStatement(st);
            while(rs.next()){
                // Append all the emailIDs separated by a space
                emailIDs = emailIDs + " " + rs.getString(1);
            }
            // For now for testing over-write all the emailIDs
            //emailIDs = "";
        }

        emailIDs = emailIDs + " " + request.getParameter("to");

        boolean success = true;
        Address[] toUser = InternetAddress.parse(emailIDs, false);
        String message = "Failed to send email to the following address:";

        ServletContext context          = ServletContextInfo.getServletContext();
        String emailPropertiesFile      = context.getRealPath("/WEB-INF/classes/jds_email.properties");
        Properties properties = new Properties();
        properties.load(new FileInputStream(emailPropertiesFile));

        msgsend sendMsg = new msgsend("text/html");
        for(Address s: toUser){
            //boolean success = sendMsg.sendMail("", "", emailIDs + " jds.ias.mails@gmail.com", subject, msg, "", "", null);
            logger.debug("Starting to send emails");
            String email = s.toString();
            // If message sending passed
            if(!sendMsg.sendMail(email, "", "jds.ias.mails@gmail.com", subject, msg, "", "", null)){
                success = false;
                message = message + " " + email;
            }
            logger.debug("Done sending emails");
        }

        String xml = "";
        String successValue = (success == true) ? "1" : "0";
        if(successValue.equals("1")){
            xml = util.convertStringToXML(successValue, "success");
        }else {
            HashMap<String, String> xmlResponse = new HashMap<>();
            xmlResponse.put("success", successValue);
            xmlResponse.put("message", message);
            xml = util.createXMLResponse(xmlResponse);
        }

        return xml;

    }
}
