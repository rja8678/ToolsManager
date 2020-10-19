package DBContollerPackage;

import ObjectClasses.User;
import cs.rit.edu.DBConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBUser {

    //
    private DBConn conn;
    private Connection connectionActual;


    public User createUserObject(int id){
        Statement stmt = null;
        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = connectionActual.createStatement();
            int foovalue = 500;
            PreparedStatement st = connectionActual.prepareStatement("SELECT * FROM \"user\" WHERE columnfoo = ?");
            st.setInt(1, foovalue);
            ResultSet rs = st.executeQuery();
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
