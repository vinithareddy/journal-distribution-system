/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

/**
 *
 * @author aloko
 */
public class migrateRES extends MigrationBase{

    private static final Logger logger = Logger.getLogger(migrateRES.class.getName());

    public migrateRES() throws SQLException {
        this.dataFile = this.dataFolder + "\\jnls.xls";
        this.conn = this.db.getConnection();
    }

public void migrate() throws FileNotFoundException, IOException, BiffException, SQLException {

        this.openExcel(dataFile);
        logger.debug("able to open file" + dataFile.toString());

        String[] datacolumns = null;
        int totalRows = 1, count=0;

        while (true) {
            datacolumns = this.getNextRow();
            if (datacolumns == null) {
                break;
            }
            totalRows++;

            // This is the first row, which has all the column names
            if(totalRows == 0) {
                continue;
            }

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                String RESYR = datacolumns[21];
                String DATE_RES = datacolumns[23];


                String CSY = "0"; Date csy = dateFormat.parse("01/01/1900");
                if(!(RESYR.isEmpty() || RESYR.equals("0") || DATE_RES.isEmpty())) {
                    CSY = DATE_RES;
                    csy = dateFormat.parse(CSY);
                }

                String CEY = "0"; Date cey = dateFormat.parse("01/01/1900");
                if(!(CSY.equals("0")))
                {
                    Calendar temp = Calendar.getInstance();

                    temp.setTime(dateFormat.parse(CSY));
                    if(!(RESYR.isEmpty() || RESYR.equals("0"))) {
                        temp.add(Calendar.DAY_OF_YEAR, Integer.parseInt(RESYR) * 365);
                    }
                    cey = temp.getTime();
                    CEY = dateFormat.format(cey);
                }

                // Start year < 1st-Jan-2013 and End year > 31-July-2012
                if(csy.compareTo(dateFormat.parse("01/01/2013"))< 0 && cey.compareTo(dateFormat.parse("29/07/2012")) > 0) {
                    count = count + Integer.parseInt(datacolumns[22]);

                    // Add logic to migrate the subscribers here
                }

                logger.debug("Row: "+ totalRows + " -> CSY: " + CSY + " -> CEY: " + CEY);
            }catch (ParseException e) {
                logger.fatal("Found incorrect date in row: " + totalRows + " skipping this row");
            }
        }
        logger.fatal("RES: " + count);
    }
}
