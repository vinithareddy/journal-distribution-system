/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

/**
 *
 * @author Shailendra Mahapatra
 */
public class IndTemp extends MigrationBase {

    private String dataFile = null;
    private int COMMIT_BATCH_SIZE = 1000;
    private static final Logger logger = Logger.getLogger(IndTemp.class.getName());
    int totalRows = 0;
    int insertedRows = 0;
    int duplicateRows = 0;
    Connection conn = null;
    private String sql_insert = "insert IGNORE into subscriber(subtype, subscriberNumber"
            + ",subscriberName, department"
            + ",institution, shippingAddress"
            + ",city, state, pincode, country, deactive, email)values"
            + "((select id from subscriber_type where subtypecode = ?),?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    PreparedStatement pst_insert = null;

    public IndTemp() throws SQLException{
        //super(); // call the base class constructor
        //this.dataFile = this.dataFolder + "\\subscriber\\temp.txt";
        this.dataFile = this.dataFolder + "\\subscriber\\indtemp.txt";
        this.conn = this.db.getConnection();
        conn.setAutoCommit(false);

    }

    @Override
    public void Migrate() throws FileNotFoundException, IOException, ParseException, SQLException {

        this.openFile(dataFile);
        String _line = null;
        int lineNum = 0;
        int numOfCols = 57;
        int recordCounter = 0;
        List DuplicateList = new ArrayList();

        String[] datacolumns = new String[numOfCols];
        pst_insert = this.conn.prepareStatement(sql_insert);

        //conn.setAutoCommit(false);

        // truncate the old data
        this.truncateTable("Subscriber");

        while (true) {
            _line = this.getNextLine();
            lineNum++;
            if(lineNum == 1){
                continue;
            }
            String[] columns = new String[numOfCols];
            if (_line == null) {
                break;
            }
            for (int i = 0; i < numOfCols; i++) {
                columns[i] = "";
                datacolumns[i] = "";
            }
            totalRows++;
            columns = _line.split(Pattern.quote("\t"));
            System.arraycopy(columns, 0, datacolumns, 0, columns.length);

            String agentCode = datacolumns[0];
            String subscribercode = datacolumns[1];
            String subscriberNumber = datacolumns[2];
            String subscriberName = datacolumns[7];
            String department = datacolumns[8];
            String institute = datacolumns[9];
            String address = datacolumns[10];
            //String address2 = datacolumns[6];
            String cityAndPin = datacolumns[11];
            String country = datacolumns[13];
            String email = datacolumns[55];

            //skip foreign suscribers
            /*if(subscribercode.equalsIgnoreCase("FP") || subscribercode.equalsIgnoreCase("FI")){
                logger.debug("Skipping foreign subscriber:" + subscriberNumber);
                continue;
            }*/

            if(subscriberNumber.isEmpty() || Integer.parseInt(subscriberNumber) == 0){
                logger.error("No Subscriber Number found for:" + subscriberName + " at line number:" + lineNum);
                continue;
            }

            int pin;
            pin = this.getPinCode(datacolumns[14].replaceAll(" ", ""));
            String remarks = "";//datacolumns[10];
            String city = "";
            String pin2 = "";

            //country field has ****, remove them
            country = country.replaceAll("\\*", "");

            //subscriberName,department, institution and address have "" quotes, remove them
            subscriberName = subscriberName.replaceAll("\"", "");
            department = department.replaceAll("\"", "");
            institute = institute.replaceAll("\"", "");
            address = address.replaceAll("\"", "");
            //address2 = address2.replaceAll("\"", "");

            int countryID = 0, stateID = 0, cityID = 0;
            //remove the spaces from city and then separate the pincode from the city
            Pattern p = Pattern.compile("^(.*?)\\s+(\\d+\\s+\\d+)$");
            Matcher m = p.matcher(cityAndPin);
            if (m.find()) {
                city = m.group(1);
                pin2 = m.group(2);
                // remove spaces between the pin code e.g. 123 456
                //pin = Integer.parseInt(m.group(2).replaceAll(" ", ""));
            }

            logger.debug("Inserting subscriber: " + subscriberNumber);

            // At times the city name in JDS db and city name mentioned may in the xls may not match due to say spelling mistake.
            // Try to correct that before checking if the city exist in the JDS db.
            if (this.cityMap.containsKey(city)) {
                city = this.cityMap.get(city);
            }
            // get the city id
            cityID = this.getCityID(city);
            if(cityID == 0){
                // if this is not a city, check if this is a state
                //String state = city;
                //if (this.stateMap.containsKey(state)) {
                    //state = this.stateMap.get(state);
                //}
                //stateID = this.getStateID(state);
                //if(stateID == 0) {
                    logger.warn("Found city/state " + city + " which does not have a entry in the database");
                    address = (cityID == 0) ? address : address + "\n" + city;
                //}

            } else {
                logger.debug("city " + city + " has the id : " + cityID);
            }

            // get the country id, check the state hash map first, there are many cases where the state is mentioned in the country field
            if(this.countryMap.containsKey(country)) {
                country = this.countryMap.get(country);
            }
            countryID = this.getCountryID(country);
            // If country is not found, check if this is a state
            if(countryID == 0) {
                String state = country;
                if (this.stateMap.containsKey(state)) {
                    state = this.stateMap.get(state);
                }
                stateID = this.getStateID(state);
                // If state is also not found means we either have a country or a state which is not present in the db
                if(stateID == 0) {
                    logger.warn("Found state/country " + country + " which does not have a entry in the database");
                    address = (stateID > 0) ? address : address + "\n" + country;
                }
                // If we found a valid state, means the country is India.
                else {
                    country = "India";
                    countryID = this.getCountryID(country);
                }
            }

            /*
            if (this.stateMap.containsKey(country)) {
                country = this.stateMap.get(country);
            }
            int countryID = this.getCountryID(country);
            int stateID = 0;
            if (countryID == 0) {
                stateID = this.getStateID(country);
                if(stateID == 0) {
                    logger.warn("Found state " + country + " which does not have a entry in the database");
                    address = (stateID > 0) ? address : address + "\n" + country;
                } else {
                    logger.debug("State " + country + " has the id : " + stateID);
                }
            }
            */

            // By default the pin is extracted from the pin column,
            // but in some cases the pin code is present in the citypin column
            // If the pin column is empty, then check if the cityPin column has the pin code
            if(pin == 0)
            {
                pin = this.getPinCode(pin2);
                if(pin == 0) {
                    logger.warn("Found pin to be 0 for subscriber number " + subscriberNumber);
                }
                else {
                    logger.debug("pin Code is : " + pin);
                }
            }


            int paramIndex = 0;
            pst_insert.setString(++paramIndex, subscribercode);
            pst_insert.setString(++paramIndex, subscriberNumber);
            //pst_insert.setDate(++paramIndex, util.dateStringToSqlDate("00/00/0000", "dd/MM/yyyy"));
            pst_insert.setString(++paramIndex, subscriberName);
            pst_insert.setString(++paramIndex, department);
            pst_insert.setString(++paramIndex, institute);
            pst_insert.setString(++paramIndex, address);
            pst_insert.setInt(++paramIndex, cityID);
            pst_insert.setInt(++paramIndex, stateID);
            pst_insert.setInt(++paramIndex, pin);
            pst_insert.setInt(++paramIndex, countryID);
            //pst_insert.setInt(++paramIndex, 0);
            pst_insert.setInt(++paramIndex, 0);
            pst_insert.setString(++paramIndex, email);
            pst_insert.addBatch();
            recordCounter++;



            int ret = this.db.executeUpdatePreparedStatement(pst_insert);
            if (ret == 0) {
                logger.error("Skipping Duplicate Subscriber : " + subscriberNumber + " Name: " + subscriberName);
                DuplicateList.add(subscriberNumber);
                duplicateRows++;
            } else {
                insertedRows++;
            }

            if(recordCounter == COMMIT_BATCH_SIZE){
                logger.debug("commiting database after " + String.valueOf(insertedRows) + " rows");
                conn.commit();
                recordCounter = 0;
            }


        }
        conn.commit();
        logger.debug("Total Rows: " + totalRows);
        logger.debug("Rows Inserted: " + insertedRows);
        logger.debug("Duplicate Rows: " + duplicateRows);
        ListIterator li = DuplicateList.listIterator();
        while(li.hasNext()){
            logger.error("Subscriber id Skipped(duplicate rows): " + li.next());
        }
    }
}
