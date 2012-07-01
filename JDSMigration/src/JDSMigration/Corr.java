/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

/**
 *
 * @author Newton
 */
public class Corr extends MigrationBase {

    private static final Logger logger = Logger.getLogger(Corr.class.getName());

    public Corr() throws SQLException {
        this.dataFile = this.dataFolder + "\\corr.xls";
        this.conn.setAutoCommit(false);
    }

    @Override
    public void Migrate() throws FileNotFoundException, IOException, BiffException, SQLException {
        this.openExcel(this.dataFile);
        int updatedRecords = 0;
        int commitSize = 0;
        String sql = "update subscriber set agent=? where subscriberNumber=?";
        PreparedStatement pst = this.conn.prepareStatement(sql);
        while (true) {
            String[] data = this.getNextRow();
            if (data == null) {
                break;
            }

            String subscriberNumber = data[3];
            String refNo = data[11];
            //logger.debug("REF_NO:" + refNo);
            if (!refNo.isEmpty() && refNo.toUpperCase().indexOf("KVPY") > -1 ) {
                int agentID = this.getAgentID("KVPY");                
                pst.setInt(1, agentID);
                pst.setString(2, subscriberNumber);
                if (pst.executeUpdate() == 1) {
                    updatedRecords++;
                    commitSize++;
                    logger.debug("Updated subscriber " + subscriberNumber + " as KVPY");
                }

            }
            else if (!refNo.isEmpty() && 
                    (   refNo.toUpperCase().indexOf("KUMARI") > -1  || 
                        refNo.toUpperCase().indexOf("MEERA TRUST") > -1 ||
                        refNo.toUpperCase().indexOf("KALM") > -1)
                    ) {
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

        }
        logger.debug("updated " + updatedRecords + " records as KVPY");
        conn.commit();
        conn.setAutoCommit(true);
    }
}
