/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 *
 * @author Shailendra Mahapatra
 */
public class MigrationBase implements IMigrate {

    private FileReader fr = null;
    private BufferedReader br = null;
    private FileInputStream fis = null;
    public String dataFolder = "data";
    public Database db = null;
    public Connection conn = null;
    private static final Logger logger = Logger.getLogger(MigrationBase.class);
    public HashMap<String, String> stateMap = new HashMap<>();
    public String sql_city = "select id from cities where city = ?";
    public String sql_distrcit = "select id from districts where district = ?";
    public String sql_state = "select id from states where state = ?";
    public String sql_country = "select id from countries where country = ?";
    public String dataFile = null;
    private ExcelReader excelReader = null;
    public static final int COMMIT_SIZE = 1000;
    private PreparedStatement pst_pgid;

    public MigrationBase() throws SQLException{

        try {
            PropertyConfigurator.configure("log4j.properties");
            this.db = new Database();
            this.conn = this.db.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        stateMap.put("T.N.", "Tamil Nadu");
        stateMap.put("A.P.", "Andhra Pradesh");
        stateMap.put("M.S.", "Maharashtra");
        stateMap.put("U.P.", "Uttar Pradesh");
        stateMap.put("M.P.", "Madhya Pradesh");
        stateMap.put("H.P.", "Himachal Pradesh");
        stateMap.put("W.B.", "West Bengal");
        stateMap.put("Orissa", "Odisha");
        
        String sql = "select id from subscription_rates t1 "
                + "where journalGroupID=? and t1.subtypeid=? "
                + "and year=? and period=?";

        pst_pgid = this.conn.prepareStatement(sql);

    }

    @Override
    public void Migrate() throws Exception {
        throw new NotImplementedException();
    }

    public void openFile(String fileName) throws java.io.FileNotFoundException {

        this.fr = new FileReader(fileName);
        this.br = new BufferedReader(this.fr);

    }

    public void openExcel(String fileName) throws java.io.FileNotFoundException, IOException, BiffException {
        excelReader = new ExcelReader(fileName, 0);
    }

    public String[] getNextRow() throws IOException, EOFException, BiffException {
        return (this.excelReader.getNextRow());
    }

    public String getNextLine() throws java.io.IOException {

        String line = null;
        if (this.br.ready()) {

            line = this.br.readLine();
            if (line == null) {
                this.br.close();
            }

        } else {
            line = null;
        }

        return line;
    }

    private void CloseFile() throws java.io.IOException {
        this.br.close();
    }

    public int getCityID(String cityName) throws SQLException {

        PreparedStatement pst = this.conn.prepareStatement(sql_city);
        pst.setString(1, cityName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            return rs.getInt(1);
        } else {
            return 0;
        }


    }

    public int getCountryID(String countryName) throws SQLException {

        PreparedStatement pst = this.conn.prepareStatement(sql_country);
        pst.setString(1, countryName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            return rs.getInt(1);
        } else {
            return 0;
        }


    }

    public int getStateID(String stateName) throws SQLException {

        if(this.stateMap.containsKey(stateName)){
            stateName = this.stateMap.get(stateName);
        }
        PreparedStatement pst = this.conn.prepareStatement(sql_state);
        pst.setString(1, stateName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            return rs.getInt(1);
        } else {
            return 0;
        }


    }

    public void truncateTable(String table) throws SQLException {

        String sql = "delete from " + table;
        this.db.executeUpdate(sql);
    }

    public int getAgentID(String agentName) throws SQLException {
        String sql = "select id from agents where agentName=? LIMIT 1";
        int agentID;
        try {
            PreparedStatement pst = this.conn.prepareStatement(sql);
            pst.setString(1, agentName);
            ResultSet rs = pst.executeQuery();
            rs.first();
            agentID = rs.getInt(1);
        }
        catch(SQLException e){
            agentID = 0;
        }

        return agentID;

    }
    
    public int getSubscriberID(int subscriberNumber) {
        int subID;
        String sql = "select id from subscriber where subscribernumber=?";

        try {
            PreparedStatement pst = this.conn.prepareStatement(sql);
            pst.setInt(1, subscriberNumber);
            ResultSet rs = pst.executeQuery();
            rs.first();
            subID = rs.getInt(1);
        } catch (SQLException e) {
            subID = 0;
        }
        return subID;

    }

    public int getSubscriberTyeID(int subscriberID) {
        int subtypeID;
        String sql = "select subtype from subscriber where id=?";

        try {
            PreparedStatement pst = this.conn.prepareStatement(sql);
            pst.setInt(1, subscriberID);
            ResultSet rs = pst.executeQuery();
            rs.first();
            subtypeID = rs.getInt(1);
        } catch (SQLException e) {
            subtypeID = 0;
        }
        return subtypeID;

    }

    public int getJournalPriceGroupID(int journalGrpID, int subtypeID, int startYear, int endYear) throws SQLException {
        int priceGroupID;
        int period = endYear - startYear + 1;

        
        pst_pgid.setInt(1, journalGrpID);
        pst_pgid.setInt(2, subtypeID);
        pst_pgid.setInt(3, startYear);
        pst_pgid.setInt(4, period);
        try {
            ResultSet rs = pst_pgid.executeQuery();
            rs.first();
            priceGroupID = rs.getInt(1);
        } catch (SQLException e) {
            priceGroupID = 0;
        }
        return priceGroupID;

    }
    
    public String getNextSubscriberNumber() throws SQLException, ParseException,
            java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {

        String nextSubscriber;
        // Identify the subscriber type i.e.Free or Paid
        String subtype = "S";
        //get the last subscriber number from subscriber table
        String lastSubscriberSql = "SELECT subscriberNumber FROM subscriber where YEAR(subscriberCreationDate)=YEAR(CURDATE()) ORDER BY id DESC LIMIT 1";
        ResultSet rs = db.executeQuery(lastSubscriberSql);
        //java.sql.ResultSetMetaData metaData = rs.getMetaData();
        Calendar calendar = Calendar.getInstance();
        String lastSubscriber;

        //if true there exists a previous subscriber for the year, so just increment the subscriber number.
        if (rs.first()) {

            lastSubscriber = rs.getString(1);

            // get the last subscriber number after the split
            int subscriber = Integer.parseInt(lastSubscriber.substring(6));
            //increment
            ++subscriber;
            //apend the year, month character and new subscriber number.
            nextSubscriber = lastSubscriber.substring(0, 2) + getMonthToCharacterMap(calendar.get(Calendar.MONTH)) + "-" + subtype + "-" + String.format("%05d", subscriber);
        } else {
            // there is no previous record for the year, so start the numbering afresh
            String year = String.valueOf(calendar.get(Calendar.YEAR)).substring(2);
            nextSubscriber = year + getMonthToCharacterMap(calendar.get(Calendar.MONTH)) + "-" + subtype + "-" + String.format("%05d", 1);
        }
        return nextSubscriber;
    }
    
    public String getMonthToCharacterMap(int _month) {
        char[] alphabet = "abcdefghijkl".toCharArray();
        // the calendar objects month starts from 0
        String monthChar = Character.toString(alphabet[_month]);
        return monthChar.toUpperCase();
    }
}
