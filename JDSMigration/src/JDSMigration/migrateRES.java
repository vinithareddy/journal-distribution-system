/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
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
public class migrateRES extends MigrationBase {

    private static final Logger logger = Logger.getLogger(migrateCURR.class.getName());

    public migrateRES() throws SQLException {
        this.dataFile = this.dataFolder + "\\jnls.xls";
        this.conn = this.db.getConnection();
        this.conn.setAutoCommit(false);
    }

    public void migrate() throws FileNotFoundException, IOException, BiffException, SQLException, ParseException {

        this.openExcel(dataFile);
        logger.debug("able to open file" + dataFile.toString());
        Corr _corr = new Corr();
        String[] corrdatacolumns;
        String corr_subscriber = null;
        String corr_sub_date = null;
        float corr_balance;
        int counter = 0;

        String[] datacolumns;
        int totalRows = 0, count = 0;

        while (true) {
            datacolumns = this.getNextRow();
            corrdatacolumns = _corr.getCorrNextRow();
            if (datacolumns == null) {
                break;
            }
            if (corrdatacolumns != null) {
                corr_subscriber = corrdatacolumns[3];
                corr_sub_date = corrdatacolumns[14];
            }
            totalRows++;
            counter++;
            if (counter >= 1000) {
                this.conn.commit();
                counter = 0;
            }

            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                String subno = datacolumns[0];
                String CURRYR = datacolumns[26];
                int copies = datacolumns[27].isEmpty() ? 0 : Integer.parseInt(datacolumns[27]);
                String DATE_CURR = datacolumns[28];
                float amount = Float.parseFloat(datacolumns[29]);

                java.sql.Date subdate = util.dateStringToSqlDate(null);
                try {
                    if (!corr_subscriber.isEmpty() && Integer.parseInt(corr_subscriber) == Integer.parseInt(subno)) {
                        subdate = util.dateStringToSqlDate(corr_sub_date);
                        corr_balance = corrdatacolumns[15].isEmpty() == false ? Float.parseFloat(corrdatacolumns[15]) : (float) 0;
                    } else {
                        corr_balance = (float) 0;
                    }
                } catch (NumberFormatException | ParseException | NullPointerException e) {
                    logger.fatal(e.toString());
                    logger.fatal("cannot update subscription date and balance for subscriber: " + subno);
                    logger.fatal("subscriber in corr:" + corr_subscriber);

                    corr_balance = (float) 0;
                }



                String CSY = "0";
                Date csy = dateFormat.parse("01/01/1900");
                if (!(CURRYR.isEmpty() || CURRYR.equals("0") || DATE_CURR.isEmpty())) {
                    CSY = DATE_CURR;
                    csy = dateFormat.parse(CSY);
                }

                String CEY = "0";
                Date cey = dateFormat.parse("01/01/1900");
                if (!(CSY.equals("0"))) {
                    Calendar temp = Calendar.getInstance();

                    temp.setTime(dateFormat.parse(CSY));
                    if (!(CURRYR.isEmpty() || CURRYR.equals("0"))) {
                        temp.add(Calendar.DAY_OF_YEAR, Integer.parseInt(CURRYR) * 365);
                    }
                    cey = temp.getTime();
                    CEY = dateFormat.format(cey);
                }

                // Start year < 1st-Jan-2013 and End year > 31-July-2012
                // csy.compareTo(dateFormat.parse("01/01/2013")) < 0 && cey.compareTo(dateFormat.parse("31/07/2012")) > 0
                if (cey.compareTo(dateFormat.parse("01/01/2012")) > 0) {
                    count = count + Integer.parseInt(datacolumns[27]);
                    //logger.info(subno);

                    // Add logic to migrate the subscribers here
                    int subscriber_id = this.getSubscriberID(Integer.parseInt(subno));
                    if(subscriber_id == 0){
                        logger.fatal("Unable to update subscription for subscriber " + subno + " No subscriber id found in DB");
                        continue;
                    }
                    int subscription_id = this.insertSubscription(subscriber_id, 0, amount, subdate, corr_balance);

                    logger.debug("Inserted Subscription with id: " + subscription_id);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(csy);
                    int startMonth = cal.get(Calendar.MONTH) + 1;
                    int startYear = cal.get(Calendar.YEAR);

                    cal.setTime(cey);
                    int endMonth = cal.get(Calendar.MONTH) + 1;
                    int endYear = cal.get(Calendar.YEAR);


                    this.insertSubscriptionDetails(subscription_id,
                            10,
                            copies,
                            startYear,
                            startMonth,
                            endYear,
                            endMonth,
                            1);
                    logger.debug("Inserted Subscription details for subscription id: " + subscription_id);

                }

                logger.debug("Rows: " + totalRows + " -> CSY: " + CSY + " -> CEY: " + CEY);
            } catch (ParseException e) {
                logger.fatal("Found incorrect date in row: " + totalRows + " skipping this row");
            }
        }
        logger.info("RES: " + count);
        this.conn.commit();
    }
}
