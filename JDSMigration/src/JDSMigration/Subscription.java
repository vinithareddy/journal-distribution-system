/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import com.mysql.jdbc.Statement;
import org.apache.log4j.Logger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.regex.Pattern;

public class Subscription extends MigrationBase {

    private String dataFile = null;
    private static final Logger logger = Logger.getLogger(Subscription.class.getName());
    int totalRows = 0;
    int insertedRows = 0;
    int insertedRowsSubDtls = 0;
    Connection conn = null;
//--------------------------------------------------------------------------------------------    
    //Select Statement for Inward Id
    private String sql_select_inward = "Select id from inward where inwardNumber = ?";
//--------------------------------------------------------------------------------------------    
    //Select Statement for Subscriber Id
    private String sql_select_subscriber = "Select id from subscriber where subscriberNumber = ?";
//--------------------------------------------------------------------------------------------    
    //Select Statement for Journal Group
    private String sql_select_journalGrp = "Select id from journal_groups where journalGroupName = ?";
//--------------------------------------------------------------------------------------------    
    //Insert Statement for Subscription
    private String sql_insert_subscription = "insert into subscription(subscriberID,inwardID,remarks)values(?,?,?)";
//--------------------------------------------------------------------------------------------
    //Insert Statement for Subscription Details
    private String sql_insert_subscriptiondetails = "insert into subscriptiondetails(subscriptionID, journalGroupID, copies, startYear, endYear)values(?,?,?,?,?)";
//--------------------------------------------------------------------------------------------    
    //Prepared Statements
    PreparedStatement pst_insert_subscription = null;
    PreparedStatement pst_select_inward = null;
    PreparedStatement pst_select_subscriber = null;
    PreparedStatement pst_insert_subscription_dtls = null;

//--------------------------------------------------------------------------------------------
    //Constructor
    public Subscription() {
        this.dataFile = this.dataFolder + "\\subscription.txt";
        this.conn = this.db.getConnection();
    }

    //Migration
    public void Migrate() throws FileNotFoundException, IOException, ParseException, SQLException {
        this.openFile(dataFile);
        String _line = null;
        int lineNum = 0;
        int numOfCols = 34;
        String[] datacolumns = new String[numOfCols];

        pst_select_inward = this.conn.prepareStatement(sql_select_inward);
        pst_select_subscriber = this.conn.prepareStatement(sql_select_subscriber);
        pst_insert_subscription = this.conn.prepareStatement(sql_insert_subscription, Statement.RETURN_GENERATED_KEYS);



        // truncate the old data
        this.truncateTable("subscription");
        this.truncateTable("subscriptiondetails");

        while (true) {
            _line = this.getNextLine();
            logger.debug("Row Number " + _line);
            lineNum++;
            //skip first line
            if (lineNum == 1) {
                continue;
            }

            //For testing
            /*
             * if (lineNum == 15) { break; }
             */

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

//------------------------------------------------------------------------------------------------------------------------------
            //Insert Subscription
//------------------------------------------------------------------------------------------------------------------------------

            //Subscriber Number from excel
            int subscriberId = 0;
            pst_select_subscriber.setString(1, datacolumns[0]);
            ResultSet rs_subscriber = this.db.executeQueryPreparedStatement(pst_select_subscriber);
            if (rs_subscriber.first()) {
                subscriberId = rs_subscriber.getInt(1);
            } else {
                logger.error("No Subscriber ID found for Subscriber Number " + datacolumns[0] + " in Subscriber DB Table");
                continue;
            }


            //Inward Number from excel
            int inwardId = 0;
            pst_select_inward.setString(1, datacolumns[1]);
            ResultSet rs_inward = this.db.executeQueryPreparedStatement(pst_select_inward);
            if (rs_inward.first()) {
                inwardId = rs_inward.getInt(1);
            }


            //Current Date, Amount and Paid Flag is mapped to Remarks currently
            String remarks = "DATE_CURR = " + datacolumns[28] + "\n" + "AMOUNT = " + datacolumns[29] + "\n" + "PAID_FLAG = " + datacolumns[30];


            int paramIndex = 0;
            pst_insert_subscription.setInt(++paramIndex, subscriberId);
            pst_insert_subscription.setInt(++paramIndex, inwardId);
            pst_insert_subscription.setString(++paramIndex, remarks);


            //Inserting the record in Subscription Table
            int ret = this.db.executeUpdatePreparedStatement(pst_insert_subscription);

            //Logging the inserting row
            if (ret == 1) {
                insertedRows++;
            }


            //Getting back the subsciption Id
            ResultSet rs_sub = pst_insert_subscription.getGeneratedKeys();
            rs_sub.first();
            int subscriptionID = rs_sub.getInt(1);

            if (inwardId != 0) {
                //logger.error("No Inward Number found for Subscriber:" + datacolumns[0] + " and Subscription:" + subscriptionID);
            }
//------------------------------------------------------------------------------------------------------------------------------
            //Insert Subscription Details
//------------------------------------------------------------------------------------------------------------------------------

            //Start year 
            // if datacoloumn[31] = 0
            int startYr = 0;
            int _tempStrtYr = Integer.parseInt(datacolumns[31]);

            if (_tempStrtYr > 100) { // start year is rightly filled with 4 digit year

                if (_tempStrtYr > 100 && _tempStrtYr < 1000) { //Many records have 3 digit year
                    logger.error("Start year for subscription " + subscriptionID + " is 3 digit " + datacolumns[31] + " but it is updated in the table");
                }
                startYr = _tempStrtYr;
            } else {

                if (_tempStrtYr > 50 && _tempStrtYr < 100) { // start year is filled with 2 digit such as 79, which means it is 1979
                    startYr = 1900 + _tempStrtYr;
                } else if (_tempStrtYr <= 50 && _tempStrtYr > 0) {
                    startYr = 2000 + _tempStrtYr;
                }
            }


            //End year 
            int endYr = 0;
            int _tempEndYr = Integer.parseInt(datacolumns[32]);
            if (_tempEndYr == 0 && startYr != 0) { // in case of only one year subscription, only start year is filled
                _tempEndYr = startYr;
                endYr = _tempEndYr;
            } else {
                if (_tempEndYr > 100) { // end year is rightly filled with 4 digit year
                    if (_tempStrtYr > 100 && _tempStrtYr < 1000) { //Many records have 3 digit year
                        logger.error("End year for subscription " + subscriptionID + " is 3 digit " + datacolumns[32] + " but it is updated in the table");
                    }
                    endYr = _tempEndYr;
                } else {
                    if (_tempEndYr > 50 && _tempEndYr < 100) { // end year is filled with 2 digit such as 79, which means it is 1979
                        endYr = 1900 + _tempEndYr;
                    } else if (_tempEndYr <= 50 && _tempEndYr > 0) {
                        endYr = 2000 + _tempEndYr;
                    }
                }
            }

//---------------------------------------------------------
            //Journal Group IDs
            //  Pramanna - Column -> 4   GrpID -> 3
            //  JAA      - Column -> 6   GrpID -> 3
            //  MS       - Column -> 8   GrpID -> 3
            //  EPS      - Column -> 10  GrpID -> 3
            //  BMS      - Column -> 12  GrpID -> 3
            //  CS       - Column -> 14  GrpID -> 3
            //  S        - Column -> 16  GrpID -> 3
            //  JB       - Column -> 18  GrpID -> 3
            //  JG       - Column -> 20  GrpID -> 3
            //  RES      - Column -> 22  GrpID -> 3
            //  AS       - Column -> 25  GrpID -> 3
            //  CURR     - Column -> 27  GrpID -> 3
            //  ALL      - Column -> No  GrpID -> 3
            //  Except CS  ------        GrpID -> 3
//---------------------------------------------------------

            int[] jrnlArr = {4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 27}; //Data Columns frm excel
            int[] jrnlGrpIDArr = {4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 25, 27, 100, 101}; //Journal Group IDs 
            int allGrpFlag = 0;
            int noCopies = 0;
            if (!datacolumns[4].isEmpty()) {
                noCopies = Integer.parseInt(datacolumns[4]); //For ALL and ALL but CS - Journal Grp the no. of copies should be same for all the jrnls
            } else {
                noCopies = 0;
            }
            for (int j = 0; j < jrnlArr.length; j++) {
                if (!datacolumns[jrnlArr[j]].equalsIgnoreCase("0") && !datacolumns[jrnlArr[j]].isEmpty()) {
                    if (noCopies == Integer.parseInt(datacolumns[jrnlArr[j]])) {
                        ++allGrpFlag;
                    }
                }
            }
                if (allGrpFlag == 12) {
                    // Journal Group for all the journals required by a subscriber
                    insertedRowsSubDtls = migrateSubDtls(subscriptionID, jrnlGrpIDArr[13], Integer.parseInt(datacolumns[jrnlArr[1]]), startYr, endYr);
                } else {
                    if (allGrpFlag == 11 && (datacolumns[14].equalsIgnoreCase("0") || datacolumns[14].isEmpty())) {
                        // Journal Group for all the journals except CS
                        insertedRowsSubDtls = migrateSubDtls(subscriptionID, jrnlGrpIDArr[14], Integer.parseInt(datacolumns[jrnlArr[1]]), startYr, endYr);
                    } else {
                        for (int j = 0; j < jrnlArr.length; j++) {
                            // Journal Group for individual journals
                            if (!datacolumns[jrnlArr[j]].equalsIgnoreCase("0") && !datacolumns[jrnlArr[j]].isEmpty()) {
                                insertedRowsSubDtls = migrateSubDtls(subscriptionID, jrnlGrpIDArr[j], Integer.parseInt(datacolumns[jrnlArr[j]]), startYr, endYr);
                            }
                        }
                    }
                }
            }
        
        logger.debug("Rows Inserted in Subscription: " + insertedRows);
        logger.debug("Rows Inserted in SubscriptionDtls: " + insertedRowsSubDtls);
    }

    public int migrateSubDtls(int subscriptionID, int jrnlGrpId, int noCopies, int startYr, int endYr)
            throws FileNotFoundException, IOException, ParseException, SQLException {
        int paramIndex1 = 0;

        //Cases where start year and end year is zero but no. of copies are not zero - LOG IT
        if (startYr == 0) {
            logger.error("No start year for subscription " + subscriptionID + "but subscription details are updated in the table");
        }
        pst_insert_subscription_dtls = this.conn.prepareStatement(sql_insert_subscriptiondetails);
        pst_insert_subscription_dtls.setInt(++paramIndex1, subscriptionID);
        pst_insert_subscription_dtls.setInt(++paramIndex1, jrnlGrpId);
        pst_insert_subscription_dtls.setInt(++paramIndex1, noCopies);
        pst_insert_subscription_dtls.setInt(++paramIndex1, startYr);
        pst_insert_subscription_dtls.setInt(++paramIndex1, endYr);

        //Inserting the record in Subscription Table
        int retUpdStatus = this.db.executeUpdatePreparedStatement(pst_insert_subscription_dtls);

        //Logging the inserting row
        if (retUpdStatus == 1) {
            insertedRowsSubDtls++;
            logger.debug("Rows Inserted for SubscriptionID: " + subscriptionID);
        }
        return insertedRowsSubDtls;
    }
}
