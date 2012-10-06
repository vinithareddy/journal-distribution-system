/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import com.mysql.jdbc.Statement;
import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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
    private static final Logger invalidcitylog = Logger.getLogger("JDSMigration.MigrationBase.getCityID");
    public HashMap<String, String> stateMap = new HashMap<>();
    public HashMap<String, String> cityMap = new HashMap<>();
    public HashMap<String, String> countryMap = new HashMap<>();
    public String sql_city = "select id from cities where city = ?";
    private String insert_city = "insert into cities(city)values(?)";
    public String sql_distrcit = "select id from districts where district = ?";
    public String sql_state = "select id from states where state = ?";
    public String sql_country = "select id from countries where country = ?";
    public String dataFile = null;
    private ExcelReader excelReader = null;
    public static final int COMMIT_SIZE = 1000;
    private PreparedStatement pst_pgid;
//--------------------------------------------------------------------------------------------
    //Select Statement for Inward Id
    public String sql_select_inward = "Select id from inward where inwardNumber = ?";
//--------------------------------------------------------------------------------------------
    //Select Statement for Subscriber Id
    public String sql_select_subscriber = "Select id from subscriber where subscriberNumber = ?";
//--------------------------------------------------------------------------------------------
    //Select Statement for Journal Group
    public String sql_select_journalGrp = "Select id from journal_groups where journalGroupName = ?";
//--------------------------------------------------------------------------------------------
    //Insert Statement for Subscription
    public String sql_insert_subscription = "insert into subscription(subscriberID,inwardID,legacy,legacy_amount,subscriptiondate,legacy_balance)"
            + "values(?,?,?,?,?,?)";
    public String sql_insert_subscription_no_dt = "insert into subscription(subscriberID,inwardID,legacy,legacy_amount,legacy_balance)"
            + "values(?,?,?,?,?)";
    public String sql_insert_subscription_free_subs = "insert into subscription(subscriberID,inwardID,legacy) values(?,?,?)";
//--------------------------------------------------------------------------------------------
    //Insert Statement for Subscription Details
    public String sql_insert_subscriptiondetails = "insert into subscriptiondetails(subscriptionID, "
            + "journalGroupID, copies, startYear, startMonth, endYear, endMonth, journalPriceGroupID)values(?,?,?,?,?,?,?,?)";
//--------------------------------------------------------------------------------------------
    public String sql_insert_subscriber = "insert IGNORE into subscriber(subtype, subscriberNumber"
            + ",subscriberName, department"
            + ",institution, shippingAddress, invoiceAddress"
            + ",city, state, pincode, country, deactive, email)values"
            + "((select id from subscriber_type where subtypecode = ?),?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public String sql_insert_subscriber_dt = "insert IGNORE into subscriber(subtype, subscriberNumber"
            + ",subscriberName, department"
            + ",institution, shippingAddress, invoiceAddress"
            + ",city, state, pincode, country, deactive, email,subscriberCreationDate)values"
            + "((select id from subscriber_type where subtypecode = ?),?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
    public String sql_getLastSubscriberId = "SELECT id from subscriber order by id desc LIMIT 1";
//--------------------------------------------------------------------------------------------
    private PreparedStatement pst_insert_subscription = null;
    private PreparedStatement pst_insert_subscription_no_dt = null;
    private PreparedStatement pst_insert_subscription_dtls = null;
    private PreparedStatement pst_insert_subscriber = null;
    private PreparedStatement pst_insert_city = null;

    public MigrationBase() throws SQLException {

        try {
            PropertyConfigurator.configure("log4j.properties");
            this.db = new Database();
            this.conn = this.db.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        String sql = "select id from subscription_rates t1 "
                + "where journalGroupID=? and t1.subtypeid=? "
                + "and year=? and period=?";
        pst_pgid = this.conn.prepareStatement(sql);

        //Added for fellows data migration
        cityMap.put("Bangalore", "Bengaluru");
        cityMap.put("Dehra Dun", "Dehradun");
        cityMap.put("Baroda", "Vadodara");
        cityMap.put("Krishangarh", "Kishangarh");
        cityMap.put("Calicut", "Kozhikode (Calicut)");
        cityMap.put("Pudicherry", "Puducherry");
        cityMap.put("Bangalore ", "Bengaluru");
        cityMap.put("Karaikudi ", "Karaikkudi");
        cityMap.put("Bangalroe ", "Bengaluru");
        cityMap.put("Vellroe ", "Vellore");
        cityMap.put("Thirvananthapuram ", "Thiruvananthapuram");
        cityMap.put("Pondicherry", "Puducherry");
        cityMap.put("Dehra Dun ", "Dehradun");
        cityMap.put("Baroda ", "Vadodara");
        cityMap.put("Pondicherry ", "Puducherry");
        cityMap.put("Pudicherry ", "Puducherry");
        cityMap.put("Calicut ", "Kozhikode (Calicut)");
        cityMap.put("Trivandrum ", "Thiruvananthapuram");
        cityMap.put("Ahmedabad ,", "Ahmedabad");
        cityMap.put("Bengalooru ", "Bengaluru");
        cityMap.put("Palkkad ", "Palakkad");
        cityMap.put("Kolkdata ", "Kolkata");
        cityMap.put("Pune - ", "Pune");
        cityMap.put("Goa ", "Goa");
        cityMap.put("Kotal", "Kotala");
        cityMap.put("Jammu-Tawi", "Jammu");
        cityMap.put("Alapuuzha", "Alappuzha");
        cityMap.put("Sivagangai", "Sivaganga");
        cityMap.put("Trivandrum", "Thiruvananthapuram");
        cityMap.put("Peramballur", "Perambalur");
        cityMap.put("Allapuzha", "Alappuzha");
        cityMap.put("Alleppay", "Alappuzha");
        cityMap.put("Alleppey", "Alappuzha");
        cityMap.put("Chikkmagalur", "Chikmagalur");
        cityMap.put("Virudhaunagar", "Viridhunagar");
        cityMap.put("Virudhinagar", "Viridhunagar");
        cityMap.put("Virudhunagar", "Viridhunagar");
        cityMap.put("Visakha", "Vishakhapatnam");
        cityMap.put("Visakhapatna", "Vishakhapatnam");
        cityMap.put("Vishakapatnam", "Vishakhapatnam");
        cityMap.put("Viskhapatnam", "Vishakhapatnam");
        cityMap.put("Thoothkudi", "Thoothukudi");
        cityMap.put("Thoothudkudi", "Thoothukudi");
        cityMap.put("Krishnapuran", "Krishnapuram");
        cityMap.put("Shimoga", "Shivamogga (Shimoga)");
        cityMap.put("Trichy", "Tiruchirapalli");
        cityMap.put("Kollam", "Kollam (Quilon)");
        cityMap.put("Chandigrah", "Chandigarh");
        cityMap.put("24 Paraganas", "24 Parganas North");
        cityMap.put("24 Paraganas N", "24 Parganas North");
        cityMap.put("24 Paraganas(N)", "24 Parganas North");
        cityMap.put("24 Pargana N", "24 Parganas North");
        cityMap.put("24 Parganas", "24 Parganas North");
        cityMap.put("24 Parganas (N)", "24 Parganas North");
        cityMap.put("24 Parganas N", "24 Parganas North");
        cityMap.put("24 Parganas(N)", "24 Parganas North");
        cityMap.put("24 Parganes N", "24 Parganas North");
        cityMap.put("24 Pragans", "24 Parganas North");
        cityMap.put("24 Parganas (S)", "24 Parganas South");
        cityMap.put("24 Parganas S", "24 Parganas South");
        cityMap.put("Bhubaneshwar", "Bhubaneswar");
        cityMap.put("Bhunbaneshwar", "Bhubaneswar");
        cityMap.put("Mumbai 400 703", "Mumbai");
        cityMap.put("Hyderabad 400", "Hyderabad");
        cityMap.put("Kolkata 700 032", "Kolkata");
        cityMap.put("Hyderabad 500046", "Hyderabad");
        cityMap.put("Pune 411 001", "Pune");
        cityMap.put("Hyderabad 500 001", "Hyderabad");
        cityMap.put("Thiruvananthapuram 695 004", "Thiruvananthapuram");
        cityMap.put("Hyderabad 500 046", "Hyderabad");
        cityMap.put("Bangalore 560 100", "Bengaluru");
        cityMap.put("New Delhi 110 054", "New Delhi");
        cityMap.put("Pune 410 513", "Pune");
        cityMap.put("Hyderabad 500 607", "Hyderabad");
        cityMap.put("Kottayam 686 560", "Kottayam");


        stateMap.put("Uttaranchal", "Uttarakhand");
        stateMap.put("Uttarkhand", "Uttarakhand");
        stateMap.put("Uttrakhand", "Uttarakhand");
        stateMap.put("Uttranchal", "Uttarakhand");
        stateMap.put("Uttarnchal", "Uttarakhand");
        stateMap.put("T.N.", "Tamil Nadu");
        stateMap.put("T,N.", "Tamil Nadu");
        stateMap.put("T.M.", "Tamil Nadu");
        stateMap.put("T.N", "Tamil Nadu");
        stateMap.put("\"T,N.\"", "Tamil Nadu");
        stateMap.put("A.P.", "Andhra Pradesh");
        stateMap.put("A.P", "Andhra Pradesh");
        stateMap.put("A.p.", "Andhra Pradesh");
        stateMap.put("M.S.", "Maharashtra");
        stateMap.put("M.S", "Maharashtra");
        stateMap.put("M.s.", "Maharashtra");
        stateMap.put("M.S..", "Maharashtra");
        stateMap.put("M.S.la", "Maharashtra");
        stateMap.put("U.P.", "Uttar Pradesh");
        stateMap.put("UP", "Uttar Pradesh");
        stateMap.put("U.P", "Uttar Pradesh");
        stateMap.put("U.P.`", "Uttar Pradesh");
        stateMap.put("U.p.", "Uttar Pradesh");
        stateMap.put("M.P.", "Madhya Pradesh");
        stateMap.put("H.P.", "Himachal Pradesh");
        stateMap.put("W.B.", "West Bengal");
        stateMap.put("w.B.", "West Bengal");
        stateMap.put("W.B", "West Bengal");
        stateMap.put("Orissa", "Odisha");
        stateMap.put("Oriss", "Odisha");
        stateMap.put("J&K", "Jammu & Kashmir");
        stateMap.put("J & K", "Jammu & Kashmir");
        stateMap.put("A&N Islands", "Andaman & Nicobar");
        stateMap.put("Delhi", "New Delhi");
        stateMap.put("Delhi`", "New Delhi");
        stateMap.put("Kashmir", "Jammu & Kashmir");
        stateMap.put("Panjab", "Punjab");
        stateMap.put("Pubjab", "Punjab");
        stateMap.put("Puducherry", "Pondicherry");
        stateMap.put("Pondicherr", "Pondicherry");
        stateMap.put("Gujrat", "Gujarat");
        stateMap.put("Guj.", "Gujarat");
        stateMap.put("Gujatat", "Gujarat");
        stateMap.put("Gujarat (D&D)", "Gujatat");
        stateMap.put("Harayana", "Haryana");
        stateMap.put("Haryanaka", "Haryana");
        stateMap.put("Haryrana", "Haryana");
        stateMap.put("Chattisgarh", "Chhattisgarh");
        stateMap.put("Chattisgarh", "Chhattisgarh");
        stateMap.put("Chhaattisgarh", "Chhattisgarh");
        stateMap.put("Chhatisgarh", "Chhattisgarh");
        stateMap.put("Chattisgarah", "Chhattisgarh");
        stateMap.put("C.G.", "Chhattisgarh");
        stateMap.put("T/N/", "Tamil Nadu");
        stateMap.put("T.N/", "Tamil Nadu");
        stateMap.put("T,N.", "Tamil Nadu");
        stateMap.put("'.N.", "Tamil Nadu");
        stateMap.put("Kar.", "Karnataka");
        stateMap.put("Kar", "Karnataka");
        stateMap.put("Karnataka'", "Karnataka");
        stateMap.put("Karnataaka", "Karnataka");
        stateMap.put("Karnataka ************************", "Karnataka");
        stateMap.put("Karnatakia", "Karnataka");
        stateMap.put("Karnatka", "Karnataka");
        stateMap.put("Karnakata", "Karnataka");
        stateMap.put("Raj", "Rajasthan");
        stateMap.put("Raj.", "Rajasthan");
        stateMap.put("Jharkahand", "Jharkhand");
        stateMap.put("Jhanrkhand", "Jharkhand");
        stateMap.put("Dlhi", "New Delhi");
        stateMap.put("KeralaM.S.", "Kerala");
        stateMap.put("Goa.", "Goa");
        stateMap.put("Goa              Y", "Goa");
        stateMap.put("Maghalaya", "Meghalaya");
        stateMap.put("Sikkam", "Sikkim");
        stateMap.put("A.S.", "Andhra Pradesh");
        stateMap.put("A& N Islands", "Andaman & Nicobar");
        stateMap.put("A&N Island", "Andaman & Nicobar");
        stateMap.put("A&N Islands", "Andaman & Nicobar");
        stateMap.put("Dadra & N Haveli", "Dadra and Nagar Haveli");
        stateMap.put("EPP", "Rajasthan");
        stateMap.put("Chandigrah", "Chandigarh");

        countryMap.put("U.S.A", "USA");
        countryMap.put("U.S.A.", "USA");
        countryMap.put("CANCELLED U.S.A.", "USA");
        countryMap.put("CANCELELED  U.S.A.", "USA");
        countryMap.put("U.K.", "UK");
        countryMap.put("England, U.K.", "UK");
        countryMap.put("London WC2E 8LH", "UK");
        countryMap.put("The Netherlands", "Netherlands");
        countryMap.put("Czechoslovakia", "Czech Republic");
        countryMap.put("Yugoslavia", "Macedonia");
        countryMap.put("Nepal*****************************", "Nepal");
        countryMap.put("Belgique", "Belgium");
        countryMap.put("THE NETHERLANDS", "Netherlands");
        countryMap.put("REPUBLIC OF MOLDAVA", "Moldava");
        countryMap.put("US", "USA");
        countryMap.put("U.S.A. (2 COPIES)", "USA");
        countryMap.put("S. Afarica", "South Africa");
        countryMap.put("S.Africa", "South Africa");
        countryMap.put("Frnace", "France");
        countryMap.put("Italy*****SURFACE", "Italy");
        countryMap.put("Italia", "Italy");
        countryMap.put("S.A.", "Colombia");
        countryMap.put("Brasil", "Brazil");
        countryMap.put("Colombia, S.A.", "Colombia");
        countryMap.put("Columbia, S.A.", "Colombia");
        countryMap.put("\"Colombia, S.A.\"", "Colombia");
        countryMap.put("Columbia", "Colombia");
        countryMap.put("Argentine", "Argentina");
        countryMap.put("Sultanate of Oman", "Oman");
        countryMap.put("Southern Africa", "Mozambique");
        countryMap.put("U.A.E", "United Arab Emirates");
        countryMap.put("U.A.E.", "United Arab Emirates");
        countryMap.put("England", "UK");
        countryMap.put("Great Britain", "UK");
        countryMap.put("Kuwait Surface Mail", "Kuwait");
        countryMap.put("Panama 6", "Panama");
        countryMap.put("Republica of Panama", "Panama");
        countryMap.put("Rep.of Korea", "South Korea");
        countryMap.put("Korea", "South Korea");
        countryMap.put("France (Cedex)", "France");
        countryMap.put("Phillippines", "Philippines");
        countryMap.put("U.S.S.R.", "Russia");
        countryMap.put("U.S.A.(DO NOT SEND)", "USA");
        countryMap.put("Philippines 1109", "Philippines");
        countryMap.put("Rep of Mauritius", "Mauritius");
        countryMap.put("Republic of China", "China");
        countryMap.put("China (PRC)", "China");
        countryMap.put("Italy  (Italia)", "Italy");
        countryMap.put("ItalySURFACE", "Italy");
        countryMap.put("Italy - Europe", "Italy");
        countryMap.put("CANCELLED ON 9.7.93", "Brazil");
        countryMap.put("France (Euro I-334)", "France");
        countryMap.put("France (cedex)", "France");
        countryMap.put("France (Cedex/France", "France");
        countryMap.put("CANCELLED ON 19.6.93", "Canada");
        countryMap.put("U.K.CANCELLED", "UK");
        countryMap.put("CANCELLED AFTER 1989", "Japan");
        countryMap.put("CANCELLED ON 8/7/91", "France");
        countryMap.put("CANCELLED  JAPAN", "Japan");
        countryMap.put("CANCELLED JAPAN", "Japan");
        countryMap.put("CANCELLED FRABCE", "France");
        countryMap.put("CANCELLED Mexico", "Mexico");
        countryMap.put("CANCELLED France", "France");
        countryMap.put("CANCELLED wef 1994", "UK");
        countryMap.put("CANCELLED Germany", "Germany");
        countryMap.put("SUR America", "Colombia");
        countryMap.put("SP Brazil", "Brazil");
        countryMap.put("France (Euro I-334)", "France");
        countryMap.put("France (Euro-I334)", "France");
        countryMap.put("France (EURO I-334)", "France");
        countryMap.put("Russia(SURFACEMAIL)", "Russia");
        countryMap.put("Rep. of Mauritius", "Mauritius");
        countryMap.put("Japan (Kin)", "Japan");
        countryMap.put("Japan (KIN)", "Japan");
        countryMap.put("Italy (Italia)", "Italy");
        countryMap.put("Japan 980", "Japan");
        countryMap.put("Japan (MZ)", "Japan");
        countryMap.put("Japan (Kin) 980", "Japan");
        countryMap.put("Japan 530", "Japan");
        countryMap.put(".", "India");
        countryMap.put("\"Taiwan, ROC\"", "Taiwan");
        countryMap.put("Taiwan (R.O.C.)", "Taiwan");
        countryMap.put("France (Euro I-334)", "France");
        countryMap.put("France (Euro I-894)", "France");
        countryMap.put("Eretrea", "Eritrea");
        countryMap.put("P.R.", "Puerto Rico");
        countryMap.put("Canada H9X 1CO", "Canada");
        countryMap.put("Canada K1A 0Y3", "Canada");
        countryMap.put("Italy - Europe", "Italy");
        countryMap.put("South America", "Chile");

        pst_insert_subscription = this.conn.prepareStatement(sql_insert_subscription, Statement.RETURN_GENERATED_KEYS);
        pst_insert_subscription_no_dt = this.conn.prepareStatement(sql_insert_subscription_no_dt, Statement.RETURN_GENERATED_KEYS);
        pst_insert_subscription_dtls = this.conn.prepareStatement(sql_insert_subscriptiondetails);
        pst_insert_subscriber = this.conn.prepareStatement(sql_insert_subscriber_dt, Statement.RETURN_GENERATED_KEYS);
        pst_insert_city = this.conn.prepareStatement(insert_city, Statement.RETURN_GENERATED_KEYS);

    }

    @Override
    public void Migrate() throws Exception {
        throw new NotImplementedException();
    }

    public void openFile(String fileName) throws java.io.FileNotFoundException {

        this.fr = new FileReader(fileName);
        this.br = new BufferedReader(this.fr);

    }

    public void openExcel(String fileName) throws java.io.FileNotFoundException {
        try {
            excelReader = new ExcelReader(fileName, 0);
        } catch (IOException | BiffException ex) {
            logger.fatal("Failed to open excel file: " + fileName);
            System.exit(1);
        }
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

    public void CloseFile() throws java.io.IOException {
        this.br.close();
        this.fr.close();
    }

    public int getCityID(String cityName) throws SQLException {
        if (cityName.isEmpty()) {
            return 0;
        }

        //trim of any spaces are the ends
        cityName = cityName.trim();

        int cityid = 0;
        if (this.cityMap.containsKey(cityName)) {
            cityName = this.cityMap.get(cityName);
        }
        PreparedStatement pst = this.conn.prepareStatement(sql_city);
        pst.setString(1, cityName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            cityid = rs.getInt(1);
        } else {
            // if the city is not present in the city table, check the district table
            //try {
            invalidcitylog.error(cityName);
            //Path path = Paths.get("logs\\invalidCities.log");
            //Files.write(path, citynames, StandardCharsets.UTF_8);
            pst_insert_city.setString(1, cityName);
            int rc = pst_insert_city.executeUpdate();
            if (rc == 1) {
                ResultSet _rs = pst_insert_city.getGeneratedKeys();
                if (_rs.first()) {
                    cityid = _rs.getInt(1);
                }
            }
            //}
        }
        return cityid;


    }

    public int getCountryID(String countryName) throws SQLException {
        if (this.countryMap.containsKey(countryName)) {
            countryName = this.countryMap.get(countryName);
        }
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

        if (this.stateMap.containsKey(stateName)) {
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

    public int getLastSubscriberId() throws SQLException {

        PreparedStatement pst = this.conn.prepareStatement(sql_getLastSubscriberId);
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
        } catch (SQLException e) {
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
            logger.fatal("Invalid subscriber id: " + subscriberNumber);
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

    public int getInteger(String _text) {
        int rc = 0;
        try {
            rc = Integer.parseInt(_text);
        } catch (NumberFormatException e) {
            logger.error("Invalid string to int: " + _text);
            rc = 0;
        } finally {
            return rc;
        }
    }

    public int getPinCode(String _pinAsText) {
        _pinAsText = _pinAsText.replaceAll(" ", "");
        int pincode = 0;
        if (_pinAsText.length() == 6) {
            try {
                pincode = Integer.parseInt(_pinAsText);
            } catch (NumberFormatException e) {
                //logger.fatal("Invalid pincode: " + _pinAsText);
                pincode = 0;
            }
        } else {
            //logger.fatal("Invalid pincode: " + _pinAsText);
        }
        return pincode;
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

    public int getEndYear() {
        int endYear;
        String sql = "SELECT year FROM `year` ORDER BY yearId DESC LIMIT 1";

        try {
            PreparedStatement pst = this.conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();
            rs.first();
            endYear = rs.getInt(1);
        } catch (SQLException e) {
            endYear = 0;
        }
        return endYear;

    }

    public int insertSubscriber(String subtypeCode, String SubscriberName, String department,
            String institution, String ShipAddress, String invAddress,
            int city, int state, int pincode, int country, String email) throws SQLException, ParseException,
            java.lang.reflect.InvocationTargetException, java.lang.IllegalAccessException {

        String nextSubscriberNumber = this.getNextSubscriberNumber();
        int paramindex = 0;

        pst_insert_subscriber.setString(++paramindex, subtypeCode);
        pst_insert_subscriber.setString(++paramindex, nextSubscriberNumber);
        pst_insert_subscriber.setString(++paramindex, SubscriberName);

        pst_insert_subscriber.setString(++paramindex, department);
        pst_insert_subscriber.setString(++paramindex, institution);
        pst_insert_subscriber.setString(++paramindex, ShipAddress);
        pst_insert_subscriber.setString(++paramindex, invAddress);
        pst_insert_subscriber.setInt(++paramindex, city);
        pst_insert_subscriber.setInt(++paramindex, state);
        pst_insert_subscriber.setInt(++paramindex, pincode);
        pst_insert_subscriber.setInt(++paramindex, country);
        pst_insert_subscriber.setBoolean(++paramindex, false);
        pst_insert_subscriber.setString(++paramindex, email);
        pst_insert_subscriber.setDate(++paramindex, util.dateStringToSqlDate(util.getDateString()));

        int subscriberid = 0;
        try {
            if (pst_insert_subscriber.executeUpdate() == 1) {
                try (ResultSet rs = pst_insert_subscriber.getGeneratedKeys();) {
                    if (rs.first()) {
                        subscriberid = rs.getInt(1);
                    }
                }
            }
        } finally {
            return subscriberid;
        }



    }

    public int insertSubscription(int subscriberId, int inwardId, float amount, Date subdate, float corr_balance) throws SQLException {
        int paramIndex = 0;
        pst_insert_subscription.setInt(++paramIndex, subscriberId);
        pst_insert_subscription.setInt(++paramIndex, inwardId);
        //pst_insert_subscription.setString(++paramIndex, remarks);
        pst_insert_subscription.setBoolean(++paramIndex, true);
        pst_insert_subscription.setFloat(++paramIndex, amount);
        pst_insert_subscription.setDate(++paramIndex, subdate);
        pst_insert_subscription.setFloat(++paramIndex, corr_balance);

        //Inserting the record in Subscription Table
        int ret = this.db.executeUpdatePreparedStatement(pst_insert_subscription);
        if (ret == 1) {
            //Getting back the subsciption Id
            ResultSet rs_sub = pst_insert_subscription.getGeneratedKeys();
            rs_sub.first();
            return rs_sub.getInt(1);  //return subscription id
        } else {
            throw (new SQLException("Failed to add subscription"));
        }

    }

    public int insertSubscription(int subscriberId) throws SQLException {
        int paramIndex = 0;
        pst_insert_subscription_no_dt.setInt(++paramIndex, subscriberId);
        pst_insert_subscription_no_dt.setInt(++paramIndex, 0);
        pst_insert_subscription_no_dt.setBoolean(++paramIndex, true);
        pst_insert_subscription_no_dt.setFloat(++paramIndex, 0);
        pst_insert_subscription_no_dt.setFloat(++paramIndex, 0);

        //Inserting the record in Subscription Table
        int ret = pst_insert_subscription_no_dt.executeUpdate();
        if (ret == 1) {
            //Getting back the subsciption Id
            ResultSet rs_sub = pst_insert_subscription_no_dt.getGeneratedKeys();
            rs_sub.first();
            return rs_sub.getInt(1);  //return subscription id
        } else {
            throw (new SQLException("Failed to add subscription"));
        }

    }

    public boolean insertSubscriptionDetails(int subscriptionID, int jrnlGrpId, int noCopies,
            int startYr, int startMonth, int endYr, int endMonth, int priceGroupID) throws SQLException {
        int paramIndex = 0;
        pst_insert_subscription_dtls = this.conn.prepareStatement(sql_insert_subscriptiondetails);
        pst_insert_subscription_dtls.setInt(++paramIndex, subscriptionID);
        pst_insert_subscription_dtls.setInt(++paramIndex, jrnlGrpId);
        pst_insert_subscription_dtls.setInt(++paramIndex, noCopies);
        pst_insert_subscription_dtls.setInt(++paramIndex, startYr);
        pst_insert_subscription_dtls.setInt(++paramIndex, startMonth);
        pst_insert_subscription_dtls.setInt(++paramIndex, endYr);
        pst_insert_subscription_dtls.setInt(++paramIndex, endMonth);
        pst_insert_subscription_dtls.setInt(++paramIndex, priceGroupID);

        //Inserting the record in Subscription Table
        int retUpdStatus = this.db.executeUpdatePreparedStatement(pst_insert_subscription_dtls);

        //Logging the inserting row
        if (retUpdStatus == 1) {
            return true;
        }
        throw (new SQLException("Failed to add subscription details"));
    }

    public void executeMasterDataScripts() throws IOException, SQLException {
        String files[] = new String[11];
        files[0] = "data" + "\\masterdata\\1.journals.sql";
        files[1] = "data" + "\\masterdata\\2.journal_groups.sql";
        files[2] = "data" + "\\masterdata\\3.journal_group_contents.sql";
        files[3] = "data" + "\\masterdata\\4.subscriber_types.sql";
        files[4] = "data" + "\\masterdata\\5.subscription_rates.sql";
        files[5] = "data" + "\\masterdata\\6.cities.sql";
        files[6] = "data" + "\\masterdata\\7.countries.sql";
        files[7] = "data" + "\\masterdata\\8.states.sql";
        files[8] = "data" + "\\masterdata\\9.year.sql";
        files[9] = "data" + "\\masterdata\\10.districts.sql";
        files[10] = "data" + "\\masterdata\\truncate_transaction_data.sql";

        for (int j = 0; j < files.length; j++) {
            String s;
            StringBuilder sb = new StringBuilder();

            FileReader _fr = new FileReader(new File(files[j].toString()));
            try (BufferedReader _br = new BufferedReader(_fr)) {
                while ((s = _br.readLine()) != null) {
                    sb.append(s);
                }
            }

            // here is our splitter ! We use ";" as a delimiter for each request
            // then we are sure to have well formed statements
            String[] inst = sb.toString().split(";");

            for (int i = 0; i < inst.length; i++) {
                // we ensure that there is no spaces before or after the request string
                // in order to not execute empty statements
                if (!inst[i].trim().equals("")) {
                    this.db.executeUpdate(inst[i]);
                    //System.out.println(">>"+inst[i]);
                }
            }
        }
    }
}
