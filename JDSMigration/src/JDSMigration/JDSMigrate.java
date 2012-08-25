/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private boolean MIGRATE_SUBSCRIPTION = false;
    private boolean MIGRATE_CORR = false;
    private boolean MIGRATE_FELLOWS = false;
    private boolean MIGRATE_ASSOCIATES = false;
    private boolean MIGRATE_EBALL = false;
    private boolean MIGRATE_HONFEL = false;
    private boolean MIGRATE_JGRANT = false;
    private boolean MIGRATE_EXCHANGE = false;    
    private boolean CURRMEM = false;
    private boolean CURTWAS = false;
    private boolean MIGRATE_CURR = true;
    private boolean MIGRATE_RES = false;
    private boolean CIRCULATION_FIGURES = false;    


    public static void main(String[] args) throws IOException, FileNotFoundException,
            ParseException, SQLException, BiffException, InvocationTargetException,
            IllegalAccessException{

        JDSMigrate _jdsmigrate = new JDSMigrate();
        //IMigrate _migrate = null;
        //try {

        if (_jdsmigrate.MIGRATE_INWARD) {
            OldInward _oldinward = new OldInward("INW2009.txt");
            _oldinward.Migrate();
            String[] inwardFiles = {"INW2010.txt", "INW2011.txt", "NEWINW.txt"};
            Inward _inward;
            for (int i = 0; i < inwardFiles.length; i++) {
                _inward = new Inward(inwardFiles[i]);
                _inward.Migrate();
            }

        }
        if (_jdsmigrate.MIGRATE_SUBSCRIBER) {
            // the subscriber table will be truncated here
            IndTemp _subscriber = new IndTemp();
            _subscriber.Migrate();

            // the subscriber table will ***NOT** be truncated here
            Temp _subscriber2 = new Temp();
            _subscriber2.Migrate();
        }
        if (_jdsmigrate.MIGRATE_SUBSCRIPTION) {
            Subscription _subscription = new Subscription();
            _subscription.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CORR) {
            Corr _corr = new Corr();
            _corr.Migrate();
        }
        if (_jdsmigrate.MIGRATE_FELLOWS) {
            migrateFellows _fellows = new migrateFellows();
            _fellows.Migrate();
        }
        if (_jdsmigrate.MIGRATE_ASSOCIATES) {
            migrateAssociates _associates = new migrateAssociates();
            _associates.Migrate();
        }
        if (_jdsmigrate.MIGRATE_EBALL) {
            migrateEBALL _editorialBoardMembers = new migrateEBALL();
            _editorialBoardMembers.Migrate();
        }
        if (_jdsmigrate.MIGRATE_HONFEL) {
            migrateHONFEL _honfel = new migrateHONFEL();
            _honfel.Migrate();
        }
        if (_jdsmigrate.MIGRATE_JGRANT) {
            migrateJGRANT _jgrant = new migrateJGRANT();
            _jgrant.Migrate();
        }
        if (_jdsmigrate.MIGRATE_EXCHANGE) {
            migrateExchange _exchange = new migrateExchange();
            _exchange.Migrate();
        }
        if(_jdsmigrate.CURRMEM){
            CurrMem _currmem = new CurrMem();
            _currmem.Migrate();
        }
        if(_jdsmigrate.CURTWAS){
            CurtWas _curtwas = new CurtWas();
            _curtwas.Migrate();
        }
        if (_jdsmigrate.MIGRATE_CURR) {
            migrateCURR _migrateCURR = new migrateCURR();
            _migrateCURR.migrate();
        }
        if (_jdsmigrate.MIGRATE_RES) {
            migrateRES _migrateRES = new migrateRES();
            _migrateRES.migrate();
        }
        if (_jdsmigrate.CIRCULATION_FIGURES) {
            circulationFigures _circulationFigures = new circulationFigures();
            _circulationFigures.getCount();
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
