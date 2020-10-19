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

    public DBUser(DBConn conn) {
        this.conn = conn;
    }

    public User createUserObject(int id){
        Statement stmt = null;
        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getConn().createStatement();
            PreparedStatement st = conn.getConn().prepareStatement("SELECT * FROM \"user\" WHERE iduser = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            String fname = null;
            String lname = null;

            while ( rs.next() ) {
                fname = rs.getString("fname");
                lname = rs.getString("lname") ;
            }
            rs.close();
            stmt.close();

            return new User(id, fname, lname);

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Operation done successfully");
        return null;
    }


}
