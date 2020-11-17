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
                    "ORDER BY lend_count DESC " +
                    "LIMIT 1");
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
}
