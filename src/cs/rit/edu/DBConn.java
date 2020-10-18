package cs.rit.edu;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBConn {
    Connection c = null ;
    public DBConn(String user, String pass) {
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
        }
        else {
            System.out.println("Database is already connected.");
        }
    }
    public boolean connected() {
        return c != null;
    }

    public String getTestVal() {
        Statement stmt = null;
        if(!connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery( "SELECT * FROM test;" );
            while ( rs.next() ) {
                int id = rs.getInt("id");
                String val = rs.getString("val");

                System.out.println( "ID = " + id );
                System.out.println( "Val = " + val );

                System.out.println();
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return null;
    }
}