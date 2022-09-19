package pluto.test;


import java.sql.*;


/**
 *
 * @author sqlitetutorial.net
 */
public class Main {
    /**
     * Connect to a sample database
     */
    public static void connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:/Users/pluto/Downloads/sqlite/ATestDB.db";
            String jobSQL = "select * from ATEST_TABLE";

            // create a connection to the database
            conn = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");

            PreparedStatement stmt = conn.prepareStatement(jobSQL);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {

                String atest = rs.getString("ATEST");
                //String jobTitle = rs.getString("JOB_TITLE");

                System.out.println( " ATEST: " +atest);
            }


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        connect();
    }
}

