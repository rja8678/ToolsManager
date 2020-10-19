package DBContollerPackage;

import ObjectClasses.User;
import cs.rit.edu.DBConn;

import java.sql.ResultSet;
import java.sql.Statement;

public class DBUser {

    //
    private DBConn conn;


    public User createUserObject(int id){
        Statement stmt = null;
        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getC().createStatement();
            //todo
            ResultSet rs = stmt.executeQuery( "SELECT * FROM USER WHERE iduser = ?");
            while ( rs.next() ) {

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
