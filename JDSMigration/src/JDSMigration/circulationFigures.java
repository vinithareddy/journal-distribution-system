/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import jxl.read.biff.BiffException;

/**
 *
 * @author aloko
 */
public class circulationFigures extends MigrationBase{

    public String dataFile1 = this.dataFolder + "\\INDTEMP.xls";
    public String dataFile2 = this.dataFolder + "\\TEMP.xls";

    public circulationFigures() throws SQLException{
        this.conn = this.db.getConnection();
        conn.setAutoCommit(false);
    }

    void getCount() throws FileNotFoundException, IOException, BiffException
    {
        this.openExcel(dataFile1);
        System.out.println("able to open file" + dataFile1.toString());
        String[] datacolumns = null;

        int totalRows = 0;
        while (true) {
            datacolumns = this.getNextRow();
            if (datacolumns == null) {
                break;
            }
            totalRows++;

            int startYear = Integer.parseInt(datacolumns[31]);
            int endYear = Integer.parseInt(datacolumns[32]) > 0 ? Integer.parseInt(datacolumns[32]) : startYear;
        }
        //this.CloseFile();

        this.openExcel(dataFile2);
        System.out.println("able to open file" + dataFile2.toString());
        //this.CloseFile();
    }

}
