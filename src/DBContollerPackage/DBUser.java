package DBContollerPackage;

import ObjectClasses.ActionType;
import ObjectClasses.LendingLog;
import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DBUser {

    //
    private DBConn conn;

    public DBUser(DBConn conn) {
        this.conn = conn;
    }

    public User createUserObject(int id){
        String fname = null;
        String lname = null;
        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = conn.getConn().prepareStatement("SELECT * FROM \"user\" WHERE iduser = ?");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                fname = rs.getString("fname");
                lname = rs.getString("lname") ;
            }
            rs.close();
            st.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("User with id: " + id + " fetched from DB successfully");
        return new User(id, fname, lname, fetchUserToolCollection(id), fetchUserOwnedTools(id), this);
    }

    public HashMap<Integer, Tool> fetchUserToolCollection(int id) {
        Statement stmt = null;
//        ArrayList<Integer> toolids = new ArrayList<>() ;
        HashMap<Integer, Tool> collection = new HashMap<>() ;

        String name = null;
        boolean lendable = false;
        Date purchaseDate = null;
        int ownerId = -1;
        ArrayList<String> tool_types = null;
        int toolId = -1;

        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getConn().createStatement();
            PreparedStatement st = conn.getConn().prepareStatement("" +
                    "SELECT t.*, u.iduser, string_agg(t2.type_name, ',') as types FROM tool t " +
                    "    INNER JOIN collection u ON t.idtool = u.idtool " +
                    "    INNER JOIN tool_tooltype tt on t.idtool = tt.idtool " +
                    "    INNER JOIN tooltype t2 on t2.idtool_type = tt.idtool_type " +
                    "WHERE u.idUser = ? " +
                    "GROUP BY t.idtool, u.iduser ");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                name= rs.getString("name");
                lendable = rs.getBoolean("lendable"); // todo this may break
                purchaseDate = rs.getDate("purchase_date");
                ownerId = rs.getInt("iduser") ;
                toolId = rs.getInt("idtool") ;
                tool_types = new ArrayList<String>(Arrays.asList(rs.getString("types").split(",")) );

                collection.put(toolId,new Tool(toolId, ownerId, name, purchaseDate, lendable, tool_types));
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

//        DBTool temptool_acc = new DBTool(conn);
//        for(int i = 0; i < toolids.size(); i++) {
//            collection.put(toolids.get(i), temptool_acc.fetchTool(toolids.get(i)));
//        }

        System.out.println("Collection for user: "+ id +" fetched from DB successfully");
        return collection;
    }

    public HashMap<Integer, Tool> fetchUserOwnedTools(int id) {
        Statement stmt = null;
//        ArrayList<Integer> toolids = new ArrayList<>() ;
        HashMap<Integer, Tool> collection = new HashMap<>() ;

        String name = null;
        boolean lendable = false;
        Date purchaseDate = null;
        int ownerId = -1;
        ArrayList<String> tool_types = null;
        int toolId = -1;

        if(!conn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = conn.getConn().createStatement();
            PreparedStatement st = conn.getConn().prepareStatement("" +
                    "SELECT t.*, u.iduser, string_agg(t2.type_name, ',') as types FROM tool t " +
                    "    INNER JOIN user_owns_tool u ON t.idtool = u.idtool " +
                    "    INNER JOIN tool_tooltype tt on t.idtool = tt.idtool " +
                    "    INNER JOIN tooltype t2 on t2.idtool_type = tt.idtool_type " +
                    "WHERE u.idUser = ? " +
                    "GROUP BY t.idtool, u.iduser ");
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                name= rs.getString("name");
                lendable = rs.getBoolean("lendable"); // todo this may break
                purchaseDate = rs.getDate("purchase_date");
                ownerId = rs.getInt("iduser") ;
                toolId = rs.getInt("idtool") ;
                tool_types = new ArrayList<String>(Arrays.asList(rs.getString("types").split(",")) );

                collection.put(toolId, new Tool(toolId, ownerId, name, purchaseDate, lendable, tool_types));
            }
            rs.close();
            stmt.close();
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }

        System.out.println("Owned Tools for user: "+ id +" fetched from DB successfully");
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

    public ArrayList<LendingLog> fetchUserLogs(int uid) {
        ArrayList<LendingLog> logSet = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.getConn().prepareStatement("SELECT idlog FROM log_relation WHERE from_iduser = ? OR to_iduser = ?");
            stmt.setInt(1, uid);
            stmt.setInt(2, uid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                logSet.add(conn.fetchLendingLog(rs.getInt(1)));
            }

            System.out.println("Successfully returned list of 'LendingLog' related to user: " + uid);

        } catch (Exception e) {
            System.out.println("Failed to pull user logs from DB.");
            e.printStackTrace();
        }

        return logSet ;
    }

    public DBConn getConn() {
        return conn;
    }
}
