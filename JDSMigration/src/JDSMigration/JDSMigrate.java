/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import jxl.read.biff.BiffException;
import org.apache.log4j.Logger;

/**
 *
 * @author Shailendra Mahapatra
 */
public class JDSMigrate {

    private static final Logger logger = Logger.getLogger(JDSMigrate.class);
    private boolean MIGRATE_INWARD = false;
    private boolean MIGRATE_SUBSCRIBER = false;
    private boolean MIGRATE_SUBSCRIPTION = true;
    private boolean MIGRATE_CORR = false;

    public static void main(String[] args) throws   IOException, FileNotFoundException, 
                                                    ParseException, SQLException, BiffException{

        JDSMigrate _jdsmigrate = new JDSMigrate();
        //IMigrate _migrate = null;
        //try {

            if (_jdsmigrate.MIGRATE_INWARD) {
                Inward _inward = new Inward();
                _inward.Migrate();
            }
            if (_jdsmigrate.MIGRATE_SUBSCRIBER) {
                Subscriber _subscriber = new Subscriber();
                _subscriber.Migrate();
            }
            if (_jdsmigrate.MIGRATE_SUBSCRIPTION) {
                Subscription _subscription = new Subscription();
                _subscription.Migrate();
            }
            if (_jdsmigrate.MIGRATE_CORR) {
                Corr _corr = new Corr();
                _corr.Migrate();
            }
//        } catch (IOException | ParseException | SQLException e) {
//
//            logger.fatal(e.getMessage());
//            StackTraceElement elements[] = e.getStackTrace();
//            for (int i = 0, n = elements.length; i < n; i++) {
//                logger.fatal("--------------- start --------------------");
//                logger.fatal("Message: " + elements[i].toString());
//                logger.fatal("Class: " + elements[i].getClassName());
//                logger.fatal("Method Name: " + elements[i].getMethodName());
//                logger.fatal("Line Number: " + elements[i].getLineNumber());
//                logger.fatal("File Name: " + elements[i].getFileName());
//                logger.fatal("--------------- end --------------------");
//            }
//        } catch (Exception e) {
//            logger.fatal(e.getMessage());
//            StackTraceElement elements[] = e.getStackTrace();
//            for (int i = 0, n = elements.length; i < n; i++) {
//                logger.fatal("--------------- start --------------------");
//                logger.fatal("Message: " + elements[i].toString());
//                logger.fatal("Class: " + elements[i].getClassName());
//                logger.fatal("Method Name: " + elements[i].getMethodName());
//                logger.fatal("Line Number: " + elements[i].getLineNumber());
//                logger.fatal("File Name: " + elements[i].getFileName());
//                logger.fatal("--------------- end --------------------");
//            }
//        }
    }
}
