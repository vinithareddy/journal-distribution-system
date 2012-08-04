/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.sql.PreparedStatement;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import jxl.read.biff.BiffException;

/**
 *
 * @author aloko
 */
public class circulationFigures extends MigrationBase{

    private String select_journals = "SELECT id, journalCode, journalName from journals";
    private String select_subscriberTypes = "select id, subtypecode from subscriber_type";
    private String select_subscriber = "select id from subscriber where subtype = ? and deactive = False";
    //private String select_journalGroups = "select"
    private String select_subscription = "select id from subscription where subscriberID = ? and active = '1' and startYear = '2012' and endYear >= '2012'";
    private String select_copies = "select copies from subscriptiondetails where subscriptionID = ? and journalGroupID = ?";
    //select sum(copies) from subscriptiondetails where journalGroupID=3 and endYear>='2012' and active='1'

    public circulationFigures() throws SQLException{
        this.conn = this.db.getConnection();
        conn.setAutoCommit(false);
    }

    void getCount() throws FileNotFoundException, IOException, BiffException, SQLException
    {
        /*
        String[] datacolumns = null;


        PreparedStatement pst1 = this.conn.prepareStatement(select_journals);
        ResultSet rs1 = db.executeQueryPreparedStatement(pst1);
        while(rs1.next())
        {
            int journalId = rs1.getInt(1);
            String journalCode = rs1.getString(2);
            String journalName = rs1.getString(3);
            System.out.print(journalId);
            System.out.print(" " + journalCode);
            System.out.println(" " + journalName);
        }

        PreparedStatement pst2 = this.conn.prepareStatement(select_subscriberTypes);
        ResultSet rs2 = db.executeQueryPreparedStatement(pst2);
        while(rs2.next())
        {
            int subtypeId = rs2.getInt(1);
            String subtypecode = rs2.getString(2);
            System.out.print(subtypeId);
            System.out.println(" " + subtypecode);
        }
        */
    }
}
