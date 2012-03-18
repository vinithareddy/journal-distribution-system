/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package JDSMigration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author Shailendra Mahapatra
 */
public class MigrationBase {

    private FileReader fr = null;
    private BufferedReader br = null;
    public String dataFolder = "data";
    public Database db = null;
    private Connection conn = null;
    private static final Logger logger = Logger.getLogger(MigrationBase.class);
    public HashMap<String, String> stateMap = new HashMap<>();
    public String sql_city = "select id from cities where city = ?";
    public String sql_distrcit = "select id from districts where district = ?";
    public String sql_state = "select id from states where state = ?";
    public String sql_country = "select id from countries where country = ?";

    public MigrationBase() {

        try {
            PropertyConfigurator.configure("log4j.properties");
            this.db = new Database();
            this.conn = this.db.getConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        stateMap.put("T.N.", "Tamil Nadu");
        stateMap.put("A.P.", "Andhra Pradesh");
        stateMap.put("M.S.", "Maharashtra");
        stateMap.put("U.P.", "Uttar Pradesh");
        stateMap.put("M.P.", "Madhya Pradesh");
        stateMap.put("H.P.", "Himachal Pradesh");
        stateMap.put("W.B.", "West Bengal");
        stateMap.put("Orissa", "Odisha");

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

    public int getCityID(String cityName) throws SQLException {

        PreparedStatement pst = this.conn.prepareStatement(sql_city);
        pst.setString(1, cityName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            return rs.getInt(1);
        }else{
            return 0;
        }


    }

    public int getCountryID(String countryName) throws SQLException {

        PreparedStatement pst = this.conn.prepareStatement(sql_country);
        pst.setString(1, countryName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            return rs.getInt(1);
        }else{
            return 0;
        }


    }

    public int getStateID(String stateName) throws SQLException {

        PreparedStatement pst = this.conn.prepareStatement(sql_state);
        pst.setString(1, stateName);
        ResultSet rs = db.executeQueryPreparedStatement(pst);
        if (rs.isBeforeFirst()) {
            rs.first();
            return rs.getInt(1);
        }else{
            return 0;
        }


    }

    public void truncateTable(String table) throws SQLException{

        String sql = "delete from " + table;
        this.db.executeUpdate(sql);
    }
}
