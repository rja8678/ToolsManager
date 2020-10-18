package cs.rit.edu;

import java.sql.Connection;
import java.sql.DriverManager;
public class DBConn {
    Connection c = null ;
    public DBConn(String user, String pass) {
        if (!connected()) {
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://reddwarf.cs.rit.edu:5432/p320_31",
                                user, pass);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully");
        }
        else {
            System.out.println("Database is already connected.");
        }
    }
    public boolean connected() {
        return c != null;
    }
}