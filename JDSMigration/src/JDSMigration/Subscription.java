/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import com.mysql.jdbc.Statement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

public class Subscription extends MigrationBase {

    private static final Logger logger = Logger.getLogger(Subscription.class.getName());
    int totalRows = 0;
    int insertedRows = 0;
    int insertedRowsSubDtls = 0;
    int subscriberID;
    String subscriberNumber;
    Map<String, String> invalidSubRates = new HashMap<>();
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
    private String sql_insert_subscription = "insert into subscription(subscriberID,inwardID,remarks,legacy,legacy_amount,subscriptiondate,legacy_balance)"
            + "values(?,?,?,?,?,?,?)";
//--------------------------------------------------------------------------------------------
    //Insert Statement for Subscription Details
    private String sql_insert_subscriptiondetails = "insert into subscriptiondetails(subscriptionID, "
            + "journalGroupID, copies, startYear, startMonth, endYear, journalPriceGroupID)values(?,?,?,?,?,?,?)";
//--------------------------------------------------------------------------------------------    
    //Prepared Statements
    PreparedStatement pst_insert_subscription = null;
    PreparedStatement pst_select_inward = null;
    PreparedStatement pst_select_subscriber = null;
    PreparedStatement pst_insert_subscription_dtls = null;

//--------------------------------------------------------------------------------------------
    //Constructor
    public Subscription() throws SQLException {
        this.dataFile = this.dataFolder + "\\jnls.xls";
        this.conn = this.db.getConnection();

    }

    //Migration
    @Override
    public void Migrate() throws FileNotFoundException, IOException, ParseException, SQLException, BiffException {
        Corr _corr = new Corr();
        this.conn.setAutoCommit(false);
        this.openExcel(dataFile);
        int commitCounter = 0;

        String[] datacolumns;
        String[] corrdatacolumns;

        pst_select_inward = this.conn.prepareStatement(sql_select_inward);
        pst_select_subscriber = this.conn.prepareStatement(sql_select_subscriber);
        pst_insert_subscription = this.conn.prepareStatement(sql_insert_subscription, Statement.RETURN_GENERATED_KEYS);



        // truncate the old data
        this.truncateTable("subscription");
        this.truncateTable("subscriptiondetails");

        while (true) {
            datacolumns = this.getNextRow();
            corrdatacolumns = _corr.getCorrNextRow();
            String corr_subscriber = null;
            String corr_sub_date = null;
            Float corr_balance;
            if (datacolumns == null) {
                break;
            }
            if (corrdatacolumns != null) {
                corr_subscriber = corrdatacolumns[3];
                corr_sub_date = corrdatacolumns[14];
            }

            totalRows++;

            //migrate only indian subscribers
            //if(datacolumns[2].equalsIgnoreCase("FI") || datacolumns[2].equalsIgnoreCase("FP"))
            //continue;

//------------------------------------------------------------------------------------------------------------------------------
            //Insert Subscription
//------------------------------------------------------------------------------------------------------------------------------
            // check if end year is < 2012, else do not migrate the records
            int startYear = Integer.parseInt(datacolumns[31]);
            logger.debug("End year field has: " + datacolumns[32]);
            int endYear = Integer.parseInt(datacolumns[32]) > 0 ? Integer.parseInt(datacolumns[32]) : startYear;

            if (endYear < 2012) {
                logger.debug("Skipping record for subscriber " + datacolumns[0] + " subsription end year is " + endYear);
                continue;
            } //else if (endYear - startYear + 1 > 5) {
            //logger.debug("Skipping record since subscription period > 5 years for subscriber " + datacolumns[0]);
            //continue;
            //}
            logger.debug("Start Year:" + datacolumns[31]);
            logger.debug("End Year:" + endYear);


            //Subscriber Number from excel
            this.subscriberNumber = datacolumns[0];
            int subscriberId = 0;
            pst_select_subscriber.setString(1, datacolumns[0]);
            ResultSet rs_subscriber = this.db.executeQueryPreparedStatement(pst_select_subscriber);
            logger.debug("Migrating subscription for subscriber Number: " + datacolumns[0]);
            try {
                rs_subscriber.first();
                subscriberId = rs_subscriber.getInt(1);
                this.subscriberID = subscriberId;
            } catch (SQLException e) {
                logger.fatal("No Subscriber ID found for Subscriber Number " + datacolumns[0] + " in Subscriber DB Table");
                continue;
            }



            //Inward Number from excel
            int inwardId = 0;
            pst_select_inward.setString(1, datacolumns[1]);
            ResultSet rs_inward = this.db.executeQueryPreparedStatement(pst_select_inward);
            if (rs_inward.first()) {
                inwardId = rs_inward.getInt(1);
            }

            float amount = Float.parseFloat(datacolumns[29]);
            //Current Date, Amount and Paid Flag is mapped to Remarks currently
            String remarks = ""; //"DATE_CURR = " + datacolumns[28] + "\n" + "AMOUNT = " + datacolumns[29] + "\n" + "PAID_FLAG = " + datacolumns[30];

            Date subdate = util.dateStringToSqlDate(null);
            // handle invalid subscriber ids
            try {
                if (!corr_subscriber.isEmpty() && Integer.parseInt(corr_subscriber) == Integer.parseInt(this.subscriberNumber)) {
                    subdate = util.dateStringToSqlDate(corr_sub_date);
                    corr_balance = corrdatacolumns[15].isEmpty() == false ? Float.parseFloat(corrdatacolumns[15]) : (float) 0;
                } else {
                    corr_balance = (float) 0;
                }
            }
            catch(NumberFormatException | ParseException | NullPointerException e){
                logger.fatal(e.toString());
                logger.fatal("cannot update subscription date and balance for subscriber: " + this.subscriberNumber);
                logger.fatal("subscriber in corr:" + corr_subscriber);
                
                corr_balance = (float) 0;
            }



            int paramIndex = 0;
            pst_insert_subscription.setInt(++paramIndex, subscriberId);
            pst_insert_subscription.setInt(++paramIndex, inwardId);
            pst_insert_subscription.setString(++paramIndex, remarks);
            pst_insert_subscription.setBoolean(++paramIndex, true);
            pst_insert_subscription.setFloat(++paramIndex, amount);
            pst_insert_subscription.setDate(++paramIndex, subdate);
            pst_insert_subscription.setFloat(++paramIndex, corr_balance);


            //Inserting the record in Subscription Table
            int ret = this.db.executeUpdatePreparedStatement(pst_insert_subscription);

            //Logging the inserting row
            if (ret == 1) {
                insertedRows++;
                commitCounter++;
            } else {
                logger.fatal("Failed to insert subscription");
                break;
            }


            //Getting back the subsciption Id
            ResultSet rs_sub = pst_insert_subscription.getGeneratedKeys();
            rs_sub.first();
            int subscriptionID = rs_sub.getInt(1);

            if (inwardId != 0) {
                logger.error("No Inward Number found for Subscriber:" + datacolumns[0] + " and Subscription:" + subscriptionID);
            }
//------------------------------------------------------------------------------------------------------------------------------
            //Insert Subscription Details
//------------------------------------------------------------------------------------------------------------------------------

            //Start year 
            // if datacoloumn[31] = 0
            int startYr = Integer.parseInt(datacolumns[31]);
            //int _tempStrtYr = Integer.parseInt(datacolumns[31]);

            /*
             * if (_tempStrtYr > 100) { // start year is rightly filled with 4
             * digit year
             *
             * if (_tempStrtYr > 100 && _tempStrtYr < 1000) { //Many records
             * have 3 digit year logger.error("Start year for subscription " +
             * subscriptionID + " is 3 digit " + datacolumns[31] + " but it is
             * updated in the table"); } startYr = _tempStrtYr; } else {
             *
             * if (_tempStrtYr > 50 && _tempStrtYr < 100) { // start year is
             * filled with 2 digit such as 79, which means it is 1979 startYr =
             * 1900 + _tempStrtYr; } else if (_tempStrtYr <= 50 && _tempStrtYr >
             * 0) { startYr = 2000 + _tempStrtYr; } }
             */


            //End year 
            int _tempEndYr = Integer.parseInt(datacolumns[32]);
            int endYr = _tempEndYr;

            if (_tempEndYr == 0 && startYr == 2012) { // in case of only one year subscription, only start year is filled
                _tempEndYr = startYr;
                endYr = _tempEndYr;
            }

            /*
             * else { if (_tempEndYr > 100) { // end year is rightly filled with
             * 4 digit year if (_tempStrtYr > 100 && _tempStrtYr < 1000) {
             * //Many records have 3 digit year logger.error("End year for
             * subscription " + subscriptionID + " is 3 digit " +
             * datacolumns[32] + " but it is updated in the table"); } endYr =
             * _tempEndYr; } else { if (_tempEndYr > 50 && _tempEndYr < 100) {
             * // end year is filled with 2 digit such as 79, which means it is
             * 1979 endYr = 1900 + _tempEndYr; } else if (_tempEndYr <= 50 &&
             * _tempEndYr > 0) { endYr = 2000 + _tempEndYr; } } }
             */

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
            //  ALL      - Column -> No  GrpID -> 12
            //  Except MS  ------        GrpID -> 13
//---------------------------------------------------------

            int[] jrnlArr = {4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 27}; //Data Columns frm excel
            //int[] jrnlArr = {4}; //Data Columns frm excel
            int[] jrnlGrpIDArr = {1, 2, 3, 4, 6, 5, 7, 8, 9, 10, 11, 12, 13}; //Journal Group IDs 
            int allGrpFlag = 0;
            int noCopies = 0;
            if (!datacolumns[4].isEmpty()) {
                noCopies = Integer.parseInt(datacolumns[4]); //For ALL and ALL but CS - Journal Grp the no. of copies should be same for all the jrnls
            } else {
                noCopies = 0;
            }

            /*
             * If all journals have the same copy count and is not null or empty
             * then we consider that he has subscribed to all journals
             */
            for (int j = 0; j < jrnlArr.length; j++) {
                if (!datacolumns[jrnlArr[j]].equalsIgnoreCase("0") && !datacolumns[jrnlArr[j]].isEmpty()) {
                    if (noCopies == Integer.parseInt(datacolumns[jrnlArr[j]])) {
                        ++allGrpFlag;
                    }
                }
            }
            if (allGrpFlag == 12) {
                // Journal Group for all the journals required by a subscriber
                insertedRowsSubDtls = migrateSubDtls(subscriptionID, jrnlGrpIDArr[11], noCopies, 1, startYr, endYr);
            } else {
                if (allGrpFlag == 11 && (datacolumns[8].equalsIgnoreCase("0") || datacolumns[14].isEmpty())) {
                    // Journal Group for all the journals except CS
                    insertedRowsSubDtls = migrateSubDtls(subscriptionID, jrnlGrpIDArr[12], Integer.parseInt(datacolumns[jrnlArr[1]]), 1, startYr, endYr);
                } else {
                    String dateRes = datacolumns[23];
                    String dateCurr = datacolumns[28];
                    String dateStr = null;
                    int startMonth = 1;

                    // if CURR_DATE or RES_DATE is set, it means it could start from middle of the year
                    if ((!datacolumns[22].isEmpty() && !datacolumns[22].equals("0"))
                            || (!datacolumns[27].isEmpty() && !datacolumns[27].equals("0"))) {
                        if (!dateRes.isEmpty()) {
                            dateStr = dateRes;
                        } else if (!dateCurr.isEmpty()) {
                            dateStr = dateCurr;
                        } else {
                            dateStr = "1/1/2012";
                        }
                        String[] splitDate = dateStr.split("/");
                        startMonth = Integer.parseInt(splitDate[0]);
                    }


                    for (int j = 0; j < jrnlArr.length; j++) {
                        // Journal Group for individual journals
                        if (!datacolumns[jrnlArr[j]].equalsIgnoreCase("0") && !datacolumns[jrnlArr[j]].isEmpty()) {
                            insertedRowsSubDtls = migrateSubDtls(subscriptionID, jrnlGrpIDArr[j], Integer.parseInt(datacolumns[jrnlArr[j]]), startMonth, startYr, endYr);
                        }
                    }
                }
            }
            // commit in bulk, its faster
            if (commitCounter == COMMIT_SIZE) {
                conn.commit();
                commitCounter = 0;
            }
        }
        conn.commit();
        conn.setAutoCommit(true);
        logger.debug("Rows Inserted in Subscription: " + insertedRows);
        logger.error("Invalid subscription rates");
        for (String key : invalidSubRates.keySet()) {
            String[] _keys = key.split(":");
            String _fs = "%s,%s,%s,%s";
            System.out.println(String.format(_fs, _keys[0], _keys[1], _keys[2], _keys[3]));
//            logger.error("JouralGrpID=" + _keys[0]);
//            logger.error("SubtypeID=" + _keys[1]);
//            logger.error("year=" + _keys[2]);
//            logger.error("Period: " + _keys[3]);
//            logger.error("-------------------------------");
        }
        //logger.debug("Rows Inserted in SubscriptionDtls: " + insertedRowsSubDtls);


    }

    private int migrateSubDtls(int subscriptionID, int jrnlGrpId, int noCopies,
            int startMonth, int startYr, int endYr)
            throws FileNotFoundException, IOException, ParseException, SQLException {
        endYr = endYr > 0 ? endYr : startYr;
        int paramIndex1 = 0;
        int subtypeID = this.getSubscriberTyeID(this.subscriberID);
        if (subtypeID == 0) {
            logger.fatal("Subscriber type id = " + subtypeID + " for subscriber : " + this.subscriberNumber);
        }
        int priceGroupID = this.getJournalPriceGroupID(jrnlGrpId, subtypeID, startYr, endYr);
        logger.debug("Price group id is: " + priceGroupID);
        if (priceGroupID == 0) {

            String key = String.valueOf(jrnlGrpId)
                    + ":" + String.valueOf(subtypeID)
                    + ":" + String.valueOf(startYr)
                    + ":" + String.valueOf((endYr - startYr + 1));
            if (!invalidSubRates.containsKey(key)) {
                invalidSubRates.put(key, "0");
            }
            logger.debug("JouralGrpID=" + jrnlGrpId);
            logger.debug("SubtypeID=" + subtypeID);
            logger.debug("year=" + startYr);
            logger.debug("Period: " + (endYr - startYr + 1));
            logger.debug("Price Group ID is 0 for subscriber Number " + this.subscriberNumber + " at row:" + (totalRows + 1));
            //System.exit(1);

            // set it to 1 to take the first row from subscription_rates table, which indicates its old data
            priceGroupID = 1;
        }


        //Cases where start year and end year is zero but no. of copies are not zero - LOG IT
        if (startYr == 0) {
            logger.error("No start year for subscription " + subscriptionID + "but subscription details are updated in the table");
        }
        pst_insert_subscription_dtls = this.conn.prepareStatement(sql_insert_subscriptiondetails);
        pst_insert_subscription_dtls.setInt(++paramIndex1, subscriptionID);
        pst_insert_subscription_dtls.setInt(++paramIndex1, jrnlGrpId);
        pst_insert_subscription_dtls.setInt(++paramIndex1, noCopies);
        pst_insert_subscription_dtls.setInt(++paramIndex1, startYr);
        pst_insert_subscription_dtls.setInt(++paramIndex1, startMonth);
        pst_insert_subscription_dtls.setInt(++paramIndex1, endYr);
        pst_insert_subscription_dtls.setInt(++paramIndex1, priceGroupID);

        //Inserting the record in Subscription Table
        int retUpdStatus = this.db.executeUpdatePreparedStatement(pst_insert_subscription_dtls);

        //Logging the inserting row
        if (retUpdStatus == 1) {
            insertedRowsSubDtls++;
            logger.debug("Inserted " + insertedRowsSubDtls + " rows in sub_details table");
            //logger.debug("Rows Inserted for SubscriptionID: " + subscriptionID);
        }
        //conn.commit();
        return insertedRowsSubDtls;
    }
}
