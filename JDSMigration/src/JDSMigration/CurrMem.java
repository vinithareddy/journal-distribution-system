/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import com.mysql.jdbc.Statement;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
/**
 *
 * @author Newton
 */
public class CurrMem extends MigrationBase{
    
    private static final Logger logger = Logger.getLogger(CurrMem.class.getName());
    private String insert_subscriber_sql;
    public CurrMem() throws SQLException, IOException, BiffException{
        this.dataFile = this.dataFolder + "\\CURRMEM.xls";
        this.openExcel(this.dataFile);
        insert_subscriber_sql = "insert into subscriber(subscriberNumber,subscriberName,"
                + "department,institution,shippingaddress,city,state,pincode,subtype)values("
                + "?,?,?,?,?,?,?,?,?)";
                
        
    }
    
    @Override
    public void Migrate() throws SQLException, IOException, BiffException, ParseException,
                                    InvocationTargetException, IllegalAccessException {
        //this.conn.setAutoCommit(false);
        //int commitCounter = 0;
        String[] datacolumns;
        int rownum = 0;
        
        while (true) {
            datacolumns = this.getNextRow();
            if (datacolumns == null) {
                break;
            }
            rownum++;
            String state = datacolumns[7];
            //skip if state contains STOP
            if(state.contains("STOP")){
                logger.debug("STOP flag found at row num:" + rownum);
                continue;
            }
            
            String name = datacolumns[0];
            String department = datacolumns[1];
            String institute = datacolumns[2];
            String address = datacolumns[3] + "\n" + datacolumns[4];
            String cityAndPin = datacolumns[5];
            String pin = datacolumns[6].replaceAll(" ", "");
            
            int cityid = 0;
            int stateid;
            
            // get the city name from city+pin columns
            Pattern p = Pattern.compile("^(.*?)\\s*(\\d+\\s*\\d+)$");
            Matcher m = p.matcher(cityAndPin);
            if (m.find()) {
                String city = m.group(1);
                cityid = this.getCityID(city);
            }
            
            // get the state id
            stateid = this.getStateID(state);
            if(stateid == 0){
                logger.error("Cannot find state id for state: " + state);
            }
            int paramindex = 0;
            PreparedStatement pst = conn.prepareStatement(insert_subscriber_sql, Statement.RETURN_GENERATED_KEYS);
            
            pst.setString(++paramindex, this.getNextSubscriberNumber());
            pst.setString(++paramindex, name);
            pst.setString(++paramindex, department);
            pst.setString(++paramindex, institute);
            pst.setString(++paramindex, address);
            pst.setInt(++paramindex, cityid);
            pst.setInt(++paramindex, stateid);
            pst.setInt(++paramindex, Integer.parseInt(pin));
            pst.setInt(++paramindex, 8);
            int upd_count = pst.executeUpdate();
            if(upd_count == 1){
                ResultSet rs = pst.getGeneratedKeys();
                rs.first();
                int subscriberId = rs.getInt(1);
            }
            
        }
    }
}
