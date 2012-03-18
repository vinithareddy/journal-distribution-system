/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import org.apache.log4j.Logger;

/**
 *
 * @author Shailendra Mahapatra
 */
public class JDSMigrate {

    private static final Logger logger = Logger.getLogger(JDSMigrate.class);
    private boolean MIGRATE_INWARD = false;
    private boolean MIGRATE_SUBSCRIBER = true;

    public static void main(String[] args) {

        JDSMigrate _jdsmigrate = new JDSMigrate();
        Object obj = null;
        try {

            if(_jdsmigrate.MIGRATE_INWARD){
                Inward _inward = new Inward();
                _inward.Migrate();
            }else if(_jdsmigrate.MIGRATE_SUBSCRIBER){
                Subscriber _subscriber = new Subscriber();
                _subscriber.Migrate();
            }

        } catch (IOException | ParseException |  SQLException e) {

            logger.fatal(e.getMessage());
            /*StackTraceElement elements[] = e.getStackTrace();
            for (int i = 0, n = elements.length; i < n; i++) {
                logger.fatal("--------------- start --------------------");
                logger.fatal("Class: " + elements[i].getClassName());
                logger.fatal("Method Name: " + elements[i].getMethodName());
                logger.fatal("Line Number: " + elements[i].getLineNumber());
                logger.fatal("File Name: " + elements[i].getFileName());
                logger.fatal("--------------- end --------------------");
            }*/
        } catch (Exception e) {
            logger.fatal(e.getMessage());
            /*StackTraceElement elements[] = e.getStackTrace();
            for (int i = 0, n = elements.length; i < n; i++) {
                logger.fatal("--------------- start --------------------");
                logger.fatal("Class: " + elements[i].getClassName());
                logger.fatal("Method Name: " + elements[i].getMethodName());
                logger.fatal("Line Number: " + elements[i].getLineNumber());
                logger.fatal("File Name: " + elements[i].getFileName());
                logger.fatal("--------------- end --------------------");
            }*/
        }
    }
}
