package cs.rit.edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConn {
    Connection c = null ;
    public DBConn(String user, String pass) {
        this.createConn(user, pass) ;
    }

    public boolean connected() {
        return c != null;
    }

    public Connection getConn() {
        return c;
    }

    public void closeConn() {
        try {
            c.close();
        } catch(Exception e) {
            System.out.println("Failed to close database connection.");
            System.out.println(e.getStackTrace());
        }
    }

    public Connection createConn(String user, String pass) {
        if (!connected()) {
            try {
                Class.forName("org.postgresql.Driver");
                c = DriverManager
                        .getConnection("jdbc:postgresql://reddwarf.cs.rit.edu:5432/p320_31",
                                user, pass);
                c.setAutoCommit(false);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(e.getClass().getName() + ": " + e.getMessage());
                System.exit(0);
            }
            System.out.println("Opened database successfully");
            return c;
        }
        else {
            System.out.println("Database is already connected.");
            return null;
        }
    }
}