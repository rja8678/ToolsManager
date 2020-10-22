package DBContollerPackage;

import ObjectClasses.ActionType;
import ObjectClasses.LendingLog;
import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;

import java.sql.*;
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
        ArrayList<Integer> toolids = new ArrayList<>() ;
        HashMap<Integer, Tool> collection = new HashMap<>() ;

        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = conn.getConn().prepareStatement("SELECT idtool from user_owns_tool where iduser = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                toolids.add(rs.getInt("idtool"));
            }
            rs.close();
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

    public boolean removeFromCollection(int uid, int tid) {
        try {
            PreparedStatement st = conn.getConn().prepareStatement("DELETE FROM collection WHERE iduser = ? AND idtool = ?");
            st.setInt(1, uid);
            st.setInt(2, tid);
            st.executeUpdate();

            conn.getConn().commit();
            st.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//            System.exit(0);
            return false;
        }
        System.out.println("Operation done successfully");
        return true;
    }

    public boolean addToCollection(int uid, int tid) {
        try {
            PreparedStatement st = conn.getConn().prepareStatement("INSERT INTO collection VALUES (?,?)");
            st.setInt(1, uid);
            st.setInt(2, tid);
            st.executeUpdate();

            conn.getConn().commit();
            st.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//            System.exit(0);
            return false;
        }
        System.out.println("Operation done successfully");
        return true;
    }

    public boolean addToOwned(int uid, int tid) {
        try {
            PreparedStatement st = conn.getConn().prepareStatement("INSERT INTO user_owns_tool VALUES (?,?)");
            st.setInt(1, uid);
            st.setInt(2, tid);
            st.executeUpdate();

            conn.getConn().commit();
            st.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//            System.exit(0);
            return false;
        }
        System.out.println("Tool " + tid+ " added to user "+ uid + " owned tools");
        return true;
    }

    public boolean removeFromOwned(int uid, int tid) {
        try {
            PreparedStatement st = conn.getConn().prepareStatement("DELETE FROM user_owns_tool WHERE iduser = ? AND idtool = ?");
            st.setInt(1, uid);
            st.setInt(2, tid);
            st.executeUpdate();

            conn.getConn().commit();
            st.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
//            System.exit(0);
            return false;
        }
        System.out.println("Operation done successfully");
        return true;
    }

    public int insertLendingLog(Date logDate, ActionType action, Date returnDate, int toolID, int toUserID, int fromUserID) {
        int logid = -1;
        try {
            PreparedStatement st = conn.getConn().prepareStatement("INSERT INTO lendinglog (log_date, action, return_date) VALUES (?,?,?) RETURNING idlog");

            st.setDate(1, logDate);
            st.setInt(2, action.getDatabaseValue());
            st.setDate(3, returnDate);

            ResultSet rs = st.executeQuery();
            rs.next();

            logid = rs.getInt(1);
            st.close();
            rs.close();

            PreparedStatement inner_st = conn.getConn().prepareStatement("INSERT INTO log_relation (to_iduser, from_iduser, idtool, idlog) VALUES (?,?,?,?)");
            inner_st.setInt(1, toUserID);
            inner_st.setInt(2, fromUserID);
            inner_st.setInt(3, toolID);
            inner_st.setInt(4, logid);
            inner_st.executeUpdate();
            inner_st.close();

            conn.getConn().commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to insert new log");
        }
        return logid;
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
            PreparedStatement st = conn.getConn().prepareStatement("SELECT * FROM lendinglog WHERE idlog = ?");
            st.setInt(1, logid);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                logDate = rs.getDate("log_date");
                action = rs.getInt("action");
                returnDate = rs.getDate("return_date");
            }
            rs.close();
            st.close();

            PreparedStatement inner_st = conn.getConn().prepareStatement("SELECT * FROM log_relation WHERE idlog = ?");
            inner_st.setInt(1, logid);
            ResultSet result = inner_st.executeQuery();

            while ( result.next() ) {
                toUser = result.getInt("to_iduser");
                fromUser = result.getInt("from_iduser");
                idtool = result.getInt("idtool");
            }
            result.close();
            inner_st.close();

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

    public ArrayList<LendingLog> fetchUserLogs(int uid) {
        ArrayList<LendingLog> logSet = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.getConn().prepareStatement("SELECT idlog FROM log_relation WHERE from_iduser = ? OR to_iduser = ?");
            stmt.setInt(1, uid);
            stmt.setInt(2, uid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                logSet.add(this.fetchLendingLog(rs.getInt(1)));
            }

            System.out.println("Successfully returned list of 'LendingLog' related to user: " + uid);

        } catch (Exception e) {
            System.out.println("Failed to pull user logs from DB.");
            e.printStackTrace();
        }

        return logSet ;
    }
}
