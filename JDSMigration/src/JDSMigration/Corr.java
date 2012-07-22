/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

/**
 *
 * @author Newton
 */
public class Corr extends MigrationBase {

    private static final Logger logger = Logger.getLogger(Corr.class.getName());

    public Corr() throws SQLException,FileNotFoundException,IOException,BiffException {
        this.dataFile = this.dataFolder + "\\corr.xls";
        this.conn.setAutoCommit(false);
        this.openExcel(this.dataFile);
    }
    
    public String[] getCorrNextRow() throws IOException, BiffException{
        return this.getNextRow();
    }

    @Override
    public void Migrate() throws FileNotFoundException, IOException, BiffException, SQLException {
        
        int updatedRecords = 0;
        int commitSize = 0;
        int rowCount = 0;
        String sql = "update subscriber set agent=? where subscriberNumber=?";
        String update_inward_with_sub_id = "update inward set subscriberid=(select id from subscriber where subscriberNumber=?) where inwardNumber=?";
        PreparedStatement pst = this.conn.prepareStatement(sql);
        PreparedStatement pst_update_inward = this.conn.prepareStatement(update_inward_with_sub_id);

        //pattern match for DATE_ACN to get the subscription Year.
        Pattern subYearPattern = Pattern.compile("\\d{1,2}/\\d{1,2}/(\\d+)");
        Pattern inwardNumberPattern = Pattern.compile("(\\w)-?(\\d+)?");
        Matcher subYearMatcher;
        Matcher inwardMatcher;
        while (true) {
            try {


                String[] data = this.getNextRow();
                rowCount++;
                if (data == null) {
                    break;
                } else if (data.length == 0) {
                    continue;
                }


                String subscriberNumber = data[3];
                String refNo = data[11];
                String SubscriptionDate = !data[5].isEmpty() ? data[5] : data[14];
                if(SubscriptionDate.isEmpty()){
                    logger.error("No reply date or subscription date to calculate inward number");
                    continue;
                }
                logger.debug("Subscription repl data is: " + SubscriptionDate);
                inwardMatcher = inwardNumberPattern.matcher(data[2]);
                subYearMatcher = subYearPattern.matcher(SubscriptionDate);
                subYearMatcher.find();

                String subscriptionYear = subYearMatcher.group(1);

                logger.debug("subscription year: " + subscriptionYear);
                if (Integer.parseInt(subscriptionYear) < 2009) {
                    logger.debug("Skipping record " + rowCount + "," + "year " + subscriptionYear + " less than 2009");
                    continue;
                }
                try {
                    inwardMatcher.find();
                    String inwardNo = subscriptionYear.substring(2) + inwardMatcher.group(1) + "-" + String.format("%05d", Integer.parseInt(inwardMatcher.group(2)));
                    logger.debug("Inward Number: " + inwardNo);

                    if (!inwardNo.isEmpty()) {
                        pst_update_inward.setString(1, subscriberNumber);
                        pst_update_inward.setString(2, inwardNo);
                        if (pst_update_inward.executeUpdate() == 1) {
                            logger.debug("Updated inward with " + subscriberNumber);
                        } else {
                            logger.debug("Failed to update inward: " + inwardNo + " with subscriber Number: " + subscriberNumber);
                        }

                    }
                }catch(Exception e){
                    logger.error("Invalid Inward Number: " + data[2]);
                    continue;
                }


                //logger.debug("REF_NO:" + refNo);
                if (!refNo.isEmpty() && refNo.toUpperCase().indexOf("KVPY") > -1) {
                    int agentID = this.getAgentID("KVPY");
                    pst.setInt(1, agentID);
                    pst.setString(2, subscriberNumber);
                    if (pst.executeUpdate() == 1) {
                        updatedRecords++;
                        commitSize++;
                        logger.debug("Updated subscriber " + subscriberNumber + " as KVPY");
                    }

                } else if (!refNo.isEmpty()
                        && (refNo.toUpperCase().indexOf("KUMARI") > -1
                        || refNo.toUpperCase().indexOf("MEERA TRUST") > -1
                        || refNo.toUpperCase().indexOf("KALM") > -1)) {
                    int agentID = this.getAgentID("Kumari Ali Mera Trust");
                    pst.setInt(1, agentID);
                    pst.setString(2, subscriberNumber);
                    if (pst.executeUpdate() == 1) {
                        updatedRecords++;
                        commitSize++;
                        logger.debug("Updated subscriber " + subscriberNumber + " as Kumari Ali Mera Trust");
                    }

                }


                if (commitSize == COMMIT_SIZE) {
                    conn.commit();
                    commitSize = 0;
                }
            } catch (Exception e) {
                logger.error("Exception at row: " + (rowCount + 1) + " " + String.valueOf(e));
            }

        }
        logger.debug("updated " + updatedRecords + " records as KVPY");
        conn.commit();
        conn.setAutoCommit(true);
    }
}
