/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import JDSMigration.Database;
/**
 *
 * @author Shailendra Mahapatra
 */
public class MigrationBase {


    private FileReader fr = null;
    private BufferedReader br = null;
    public String dataFolder = "data";
    public Database db = null;

    private static final Logger logger = Logger.getLogger(MigrationBase.class);

    public MigrationBase() {

        try {

            PropertyConfigurator.configure("log4j.properties");
            this.db = new Database();



        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void openFile(String fileName) throws java.io.FileNotFoundException {

        this.fr = new FileReader(fileName);
        this.br = new BufferedReader(this.fr);

    }

    public String getNextLine() throws java.io.IOException {

        String line = null;
        if (this.br.ready()) {

            line = this.br.readLine();
            if (line == null) {
                this.br.close();
            }

        } else {
            line = null;
        }

        return line;
    }

    private void CloseFile() throws java.io.IOException {
        this.br.close();
    }
}
