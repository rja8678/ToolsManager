package cs.rit.edu;

import ObjectClasses.ActionType;
import ObjectClasses.LendingLog;

import java.sql.*;
import java.util.HashMap;

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

    public HashMap<Integer, String> fetchAllToolTypes() {
        HashMap<Integer, String> types = new HashMap<>() ;

        if(!connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = this.getConn().prepareStatement("SELECT idtool_type, type_name FROM tooltype");
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                types.put(rs.getInt("idtool_type"),rs.getString("type_name"));
            }
            rs.close();
            st.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("All tool types pulled from database.");
        return types;
    }

    public HashMap<Integer, String> fetchAllUsers() {
        HashMap<Integer, String> users = new HashMap<>() ;

        if(!this.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = this.getConn().prepareStatement("SELECT * from \"user\"");
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                users.put(rs.getInt("iduser"), rs.getString("fname") + " " +rs.getString("lname"));
            }
            rs.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        System.out.println("Users where fetched from DB successfully");
        return users;
    }

    public LendingLog fetchLendingLog(int logid) {
        LendingLog log = null;
        Date logDate = null;
        int action = -1;
        Date returnDate = null;

        int toUser = -1;
        int fromUser = -1;
        int idtool = -1 ;

        try {
            PreparedStatement st = this.getConn().prepareStatement("" +
                    "SELECT ll.*, lr.* FROM lendinglog ll " +
                    "    INNER JOIN log_relation lr on ll.idlog = lr.idlog " +
                    "WHERE ll.idlog = ?  ");
            st.setInt(1, logid);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                logDate = rs.getDate("log_date");
                action = rs.getInt("action");
                returnDate = rs.getDate("return_date");

                toUser = rs.getInt("to_iduser") ;
                fromUser = rs.getInt("from_iduser") ;
                idtool = rs.getInt("idtool");
            }

            rs.close();
            st.close();

            ActionType at;

            if(action == 0) {
                at = ActionType.Lend;
            }
            else {
                at = ActionType.Return;
            }

            log = new LendingLog(logid, logDate, at, returnDate, idtool, toUser, fromUser);

        } catch (Exception e) {
            System.out.println("Failed to fetch log: " + logid);
            e.printStackTrace();
        }
        return log;
    }
}