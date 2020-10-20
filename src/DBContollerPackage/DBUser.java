package DBContollerPackage;

import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class DBUser {

    //
    private DBConn conn;

    public DBUser(DBConn conn) {
        this.conn = conn;
    }

    public User createUserObject(int id){
        Statement stmt = null;
        String fname = null;
        String lname = null;
        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getConn().createStatement();
            PreparedStatement st = conn.getConn().prepareStatement("SELECT * FROM \"user\" WHERE iduser = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                fname = rs.getString("fname");
                lname = rs.getString("lname") ;
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("User with id: " + id + " fetched from DB successfully");
        return new User(id, fname, lname, fetchUserToolCollection(id), fetchUserOwnedTools(id));
    }

    public HashMap<Integer, Tool> fetchUserToolCollection(int id) {
        Statement stmt = null;
        ArrayList<Integer> toolids = new ArrayList<>() ;
        HashMap<Integer, Tool> collection = new HashMap<>() ;

        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getConn().createStatement();
            PreparedStatement st = conn.getConn().prepareStatement("SELECT idtool from collection where iduser = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                toolids.add(rs.getInt("idtool"));
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        DBTool temptool_acc = new DBTool(conn);
        for(int i = 0; i < toolids.size(); i++) {
            collection.put(toolids.get(i), temptool_acc.fetchTool(toolids.get(i)));
        }

        System.out.println("Collection for user: "+ id +" fetched from DB successfully");
        return collection;
    }

    public HashMap<Integer, Tool> fetchUserOwnedTools(int id) {
        Statement stmt = null;
        ArrayList<Integer> toolids = new ArrayList<>() ;
        HashMap<Integer, Tool> collection = new HashMap<>() ;

        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getConn().createStatement();
            PreparedStatement st = conn.getConn().prepareStatement("SELECT idtool from user_owns_tool where iduser = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                toolids.add(rs.getInt("idtool"));
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        DBTool temptool_acc = new DBTool(conn);
        for(int i = 0; i < toolids.size(); i++) {
            collection.put(toolids.get(i), temptool_acc.fetchTool(toolids.get(i)));
        }

        System.out.println("Collection for user: "+ id +" fetched from DB successfully");
        return collection;
    }
}
