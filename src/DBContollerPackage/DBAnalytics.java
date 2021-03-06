package DBContollerPackage;

import cs.rit.edu.DBConn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBAnalytics {
    private DBConn dbconn;

    public DBAnalytics(DBConn conn) {
        this.dbconn = conn;
    }

    public List<List<String>> mostLentType() {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("" +
                    "SELECT t2.type_name, count(t.idtool_type) as lend_count " +
                    "FROM log_relation" +
                    "        INNER JOIN tool_tooltype t on log_relation.idtool = t.idtool " +
                    "        INNER JOIN tooltype t2 on t2.idtool_type = t.idtool_type " +
                    "WHERE log_relation.idlog IN" +
                    "   (SELECT idlog FROM lendinglog WHERE action = 0) " +
                    "GROUP BY t2.type_name " +
                    "ORDER BY lend_count DESC");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("type_name"));
                inner.add(Integer.toString(rs.getInt("lend_count")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }

    public List<List<String>> overdueTools() {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("" +
                    "SELECT t.idtool, t.name, return_date, concat(u1.fname, ' ', u1.lname) AS owner, concat(u2.fname, ' ', u2.lname) AS receiver " +
                    "FROM lendinglog inner join log_relation lr on lendinglog.idlog = lr.idlog " +
                    "    INNER JOIN tool t on t.idtool = lr.idtool " +
                    "    INNER JOIN \"user\" u1 on u1.iduser = lr.from_iduser " +
                    "    INNER JOIN \"user\" u2 on u2.iduser = lr.to_iduser " +
                    "WHERE lendinglog.idlog IN ( " +
                    "    SELECT DISTINCT ON (idtool) " +
                    "        lendinglog.idlog " +
                    "    FROM lendinglog inner join log_relation lr on lendinglog.idlog = lr.idlog " +
                    "    ORDER BY idtool ASC, " +
                    "             lendinglog.idlog DESC " +
                    ") AND action = 0 AND return_date < now()");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(Integer.toString(rs.getInt("idtool")));
                inner.add(rs.getString("name"));
                inner.add(rs.getDate("return_date").toString());
                inner.add(rs.getString("owner"));
                inner.add(rs.getString("receiver"));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }

    public List<List<String>> mostLentTool() {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("" +
                    "SELECT t.idtool, t.name, count(log_relation.idtool) as usage " +
                    "FROM log_relation " +
                    "         INNER JOIN tool t on t.idtool = log_relation.idtool " +
                    "WHERE log_relation.idlog IN " +
                    "      (SELECT idlog from lendinglog WHERE action = 0) " +
                    "GROUP BY t.name, t.idtool " +
                    "ORDER BY usage DESC");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(Integer.toString(rs.getInt("idtool")));
                inner.add(rs.getString("name"));
                inner.add(Integer.toString(rs.getInt("usage")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> mostRecentLending(int userID) {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT t.name as tool_name, " + 
            		"       t.idtool, " + 
            		"       action, " + 
            		"      log_date " + 
            		"FROM lendinglog inner join log_relation lr on lendinglog.idlog = lr.idlog " + 
            		"inner join tool t on lr.idtool = t.idtool " + 
            		"WHERE lr.from_iduser = ? " + 
            		"ORDER BY  log_date DESC " + 
            		"LIMIT 10;");

            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("tool_name"));
                inner.add(Integer.toString(rs.getInt("idtool")));
                inner.add(Integer.toString(rs.getInt("action")));
                inner.add(rs.getDate("log_date").toString());

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> toolsIHaveLent(int userID) {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT name " + 
            		"FROM tool " + 
            		"WHERE idtool IN ( " + 
            		"   SELECT idtool FROM collection WHERE iduser = ? " + 
            		"   EXCEPT " + 
            		"   SELECT idtool FROM user_owns_tool WHERE iduser = ?);");

            st.setInt(1, userID);
            st.setInt(2, userID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("name"));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> toolsIAmLendihgOut(int userID) {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT name " + 
            		"FROM tool " + 
            		"WHERE idtool IN ( " + 
            		"SELECT idtool FROM user_owns_tool WHERE iduser = ? " + 
            		"EXCEPT " + 
            		"SELECT idtool FROM collection WHERE iduser = ?);");

            st.setInt(1, userID);
            st.setInt(2, userID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("name"));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> userWithMostToolsOwned() {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT concat(u.fname,' ', u.lname) as username, u.iduser, count(idtool) tools_owned " + 
            		"FROM user_owns_tool " + 
            		"   INNER JOIN \"user\" u on u.iduser = user_owns_tool.iduser " + 
            		"GROUP BY username, u.iduser " + 
            		"ORDER BY tools_owned DESC;");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("username"));
                inner.add(Integer.toString(rs.getInt("tools_owned")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> userWithMostToolsInCollection() {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT concat(u.fname,' ', u.lname) as username, u.iduser, count(idtool) collection_size " + 
            		"FROM collection " + 
            		"   INNER JOIN \"user\" u on u.iduser = collection.iduser " + 
            		"GROUP BY username, u.iduser " + 
            		"ORDER BY collection_size DESC;");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("username"));
                inner.add(Integer.toString(rs.getInt("collection_size")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> userWithMostLends() {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT from_iduser, concat(u.fname, ' ', u.lname) as username, count(idlog) lent_count " + 
            		"FROM log_relation " + 
            		"   INNER JOIN \"user\" u on u.iduser = log_relation.from_iduser " + 
            		"WHERE idlog IN " + 
            		"     (SELECT idlog FROM lendinglog WHERE action = 0) " + 
            		"GROUP BY from_iduser, username " + 
            		"ORDER BY lent_count DESC;");

            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("username"));
                inner.add(Integer.toString(rs.getInt("lent_count")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> userOwnedToolTypes(int userID) {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT type_name, count(tt.idtool) as usage " + 
            		"FROM tooltype INNER JOIN tool_tooltype tt on tooltype.idtool_type = tt.idtool_type " + 
            		"WHERE tt.idtool IN (SELECT idtool FROM user_owns_tool WHERE iduser = ?) " + 
            		"GROUP BY type_name ORDER BY usage DESC;");

            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("type_name"));
                inner.add(Integer.toString(rs.getInt("usage")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
    
    public List<List<String>> userCollectionToolTypes(int userID) {
        List<List<String>> rtnSet = new ArrayList<>() ;

        if(!this.dbconn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbconn.getConn().prepareStatement("SELECT type_name, count(tt.idtool) as usage " + 
            		"FROM tooltype INNER JOIN tool_tooltype tt on tooltype.idtool_type = tt.idtool_type " + 
            		"WHERE tt.idtool IN (SELECT idtool FROM collection WHERE iduser = ?) " + 
            		"GROUP BY type_name ORDER BY usage DESC;");

            st.setInt(1, userID);
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                List<String> inner = new ArrayList<String>();
                inner.add(rs.getString("type_name"));
                inner.add(Integer.toString(rs.getInt("usage")));

                rtnSet.add(inner);
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnSet;
    }
}
