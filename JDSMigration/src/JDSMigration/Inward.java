/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Shailendra Mahapatra
 */
public class Inward extends MigrationBase {

    private String dataFile = null;
    static final Logger logger = Logger.getRootLogger();

    public Inward() {
        //super(); // call the base class constructor
        this.dataFile = this.dataFolder + "\\inward.txt";
    }

    public void Migrate() throws FileNotFoundException, IOException, ParseException, SQLException {

        int totalRows = 0;
        int insertedRows = 0;
        HashMap<String, String> cityMap = new HashMap<>();
        cityMap.put("Bang", "Bengaluru");
        cityMap.put("Banga", "Bengaluru");
        cityMap.put("Bagalpur", "Bhagalpur");
        cityMap.put("Trichy", "Tiruchirappalli");
        cityMap.put("Tircy", "Tiruchirappalli");
        cityMap.put("Nohali", "Mohali");
        cityMap.put("Hyd", "Hyderabad");
        cityMap.put("Trivandrum", "Thiruvananthapuram");



        int recordCount = 0;
        String pattern = "(\\d+.*\\d*)";

        Pattern r = Pattern.compile(pattern);
        Format dtformat = new SimpleDateFormat("dd/MM/yyyy");
        Connection conn = this.db.getConnection();
        String sql = "select id from cities where city = ?";
        String sql_distrcit = "select id from districts where district = ?";
        String sql_state = "select id from states where state = ?";
        String insert_sql = "insert into inward(inwardNumber,inward.from,state,district,city,email,inwardCreationDate,inwardPurpose,chqddNumber,currency,amount,remarks,completed) "
                + "values (?,?,(select id from states where state = ?),(select id from districts where district = ?),?,?,?,(select id from inward_purpose where purpose=?),?,(select id from currency where currency = ?),?,?,true)";
        PreparedStatement pst = conn.prepareStatement(sql);
        PreparedStatement pst_district = conn.prepareStatement(sql_distrcit);
        PreparedStatement pst_state = conn.prepareStatement(sql_state);
        PreparedStatement pst_insert = conn.prepareStatement(insert_sql);


        this.openFile(dataFile);
        String _line = null;
        while (true) {

            totalRows++;
            _line = this.getNextLine();
            if (_line == null) {
                break;
            }
            int cityId = 0;
            int districtId = 0;
            int countryId = 0;
            int paramIndex = 0;

            String[] columns = _line.split(Pattern.quote("\t"));

            java.sql.Date inwardDate = util.dateStringToSqlDate(columns[1]);

            String inwardNumber = columns[11] + "-" + String.format("%05d", Integer.parseInt(columns[0]));
            String From = columns[2];
            String City = columns[3];
            String Currency = "INR";

            // replace all non word characters
            City = City.replaceAll("'", "");
            City = City.replaceAll("\"", "");

            From = From.replaceAll("\"", "");

            logger.debug("Migrating inward: " + inwardNumber);
            logger.debug("City name is " + City);

            if (cityMap.containsKey(City)) {

                City = cityMap.get(City);
                logger.debug("Found City name in Hash map: " + City);
            }


            pst_insert.setString(++paramIndex, inwardNumber);
            pst_insert.setString(++paramIndex, From);

            // search if the city is there in the db
            pst.setString(1, City);

            ResultSet rs = this.db.executeQueryPreparedStatement(pst);
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    cityId = rs.getInt(1);
                    logger.debug(City + " has the city id: " + rs.getInt(1));
                    pst_insert.setString(++paramIndex, null);
                    pst_insert.setString(++paramIndex, null);
                    pst_insert.setInt(++paramIndex, cityId);

                }
            } else {
                logger.debug(City + " is not in the city table..look up district");
                pst_district.setString(1, City);
                rs = this.db.executeQueryPreparedStatement(pst_district);
                if (rs.isBeforeFirst()) {
                    // district found
                    while (rs.next()) {
                        logger.debug(City + " has the district id: " + rs.getInt(1));
                        pst_insert.setString(++paramIndex, null);
                        pst_insert.setString(++paramIndex, City);
                        pst_insert.setInt(++paramIndex, cityId);

                    }
                } else {
                    // district also not found
                    logger.debug(City + " is not in the district table..look up state");
                    pst_state.setString(1, City);
                    rs = this.db.executeQueryPreparedStatement(pst_state);
                    if (rs.isBeforeFirst()) {
                        // state found
                        while (rs.next()) {
                            logger.debug(City + " has the state id: " + rs.getInt(1));
                            pst_insert.setString(++paramIndex, City);
                            pst_insert.setString(++paramIndex, null);
                            pst_insert.setInt(++paramIndex, cityId);

                        }
                    } else {
                        logger.error("No state found. Cannot insert inward " + inwardNumber);
                        continue;
                    }


                }

            }
            pst_insert.setString(++paramIndex, columns[10]);
            pst_insert.setDate(++paramIndex, inwardDate);

            String inwardPurpose = columns[4].toLowerCase();

            if (inwardPurpose == null) {
                logger.error("No inward reason for: " + inwardNumber);
                continue;
            }


            if (inwardPurpose.contains("subscription") || inwardPurpose.contains("sub")) {
                inwardPurpose = "Renew Subscription";
            } else if (inwardPurpose.contains("advertisement")) {
                inwardPurpose = "Advertisement";
            } else if (inwardPurpose.contains("invoice")) {
                inwardPurpose = "Request For Invoice";
            } else if (inwardPurpose.contains("reprint")) {
                inwardPurpose = "Reprint";
            } else if (inwardPurpose.contains("manuscript")) {
                inwardPurpose = "Manuscript";
            } else {
                logger.error("Could not find inward reason " + inwardPurpose);
                continue;
            }
            pst_insert.setString(++paramIndex, inwardPurpose);

            // chq dd number
            int chqddNumber = 0;
            if(columns[5].matches("^\\d+")){
                chqddNumber = Integer.parseInt(columns[5]);
            }
            pst_insert.setInt(++paramIndex, chqddNumber);

            if (columns[7].equals("$")) {
                Currency = "USD";
            }
            pst_insert.setString(++paramIndex, Currency);
            float amount = 0;

            Matcher m = r.matcher(columns[8]);
            if (m.find()) {
                amount = Float.parseFloat(columns[8]);
            } else {
                amount = 0;
            }

            //float amount = (columns[8] != null || !columns[8].isEmpty()) ? Float.parseFloat(columns[8]) : 0;
            //float amount = Float.parseFloat(columns[8])  0.0;
            pst_insert.setFloat(++paramIndex, amount);
            pst_insert.setString(++paramIndex, columns[9]);

            db.executeUpdatePreparedStatement(pst_insert);
            insertedRows++;




        } // end of while

        logger.debug("Total Records: " + totalRows);
        logger.debug("Inserted Records: " + insertedRows);

    }   // migrate
} // class
