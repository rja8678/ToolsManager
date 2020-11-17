package DBContollerPackage;

import cs.rit.edu.*;
import ObjectClasses.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DBTool {
    private DBConn dbConn;

    public DBTool(DBConn dbConn){
        this.dbConn = dbConn;
    }

    public Tool fetchTool(int toolID){
        String name = null;
        boolean lendable = false;
        Date purchaseDate = null;
        int ownerId = -1;
        ArrayList<String> tool_types = null;

        if(!dbConn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbConn.getConn().prepareStatement("" +
                    "SELECT t.*, u.iduser, string_agg(t2.type_name, ',') as types FROM tool t " +
                    "    INNER JOIN user_owns_tool u ON t.idtool = u.idtool " +
                    "    INNER JOIN tool_tooltype tt on t.idtool = tt.idtool " +
                    "    INNER JOIN tooltype t2 on t2.idtool_type = tt.idtool_type " +
                    "WHERE t.idtool = ? " +
                    "GROUP BY t.idtool, u.iduser");
            st.setInt(1, toolID);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                name= rs.getString("name");
                lendable = rs.getBoolean("lendable"); // todo this may break
                purchaseDate = rs.getDate("purchase_date");
                ownerId = rs.getInt("iduser") ;
                tool_types = new ArrayList<String>(Arrays.asList(rs.getString("types").split(",")) );

            }
            rs.close();
            st.close();
            
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
//        System.out.println("Tool with id " + toolID + " fetched from Database successfully");
        return new Tool(toolID, ownerId, name, purchaseDate, lendable, tool_types);
    }

    public ArrayList<String> fetchToolTypes(int toolid) {
        ArrayList<String> types = new ArrayList<>() ;

        if(!dbConn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            PreparedStatement st = dbConn.getConn().prepareStatement("SELECT type_name FROM tooltype JOIN tool_tooltype tt on tooltype.idtool_type = tt.idtool_type WHERE tt.idtool = ?");
            st.setInt(1, toolid);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                types.add(rs.getString("type_name"));
            }
            rs.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
//        System.out.println("Tool types for tool: "+ toolid +" pulled from database.");
        return types;
    }

    public int insertNewTool(String toolName, Date purchaseDate, boolean lendable, ArrayList<Integer> toolTypes) {
        int toolid = -1;
        try {
            PreparedStatement st = dbConn.getConn().prepareStatement("INSERT INTO tool (name, lendable, purchase_date) VALUES (?,?,?) RETURNING idtool");

            st.setString(1, toolName);
            if(lendable){
                st.setInt(2, 1);
            }
            else {
                st.setInt(2, 0);
            }
            st.setDate(3, purchaseDate);
            ResultSet rs = st.executeQuery();
            rs.next();

            toolid = rs.getInt(1);
            st.close();
            rs.close();


            for(int i = 0; i < toolTypes.size(); i++) {
                PreparedStatement inner_st = dbConn.getConn().prepareStatement("INSERT INTO tool_tooltype VALUES (?,?)");
                inner_st.setInt(1, toolid);
                inner_st.setInt(2, toolTypes.get(i));
                inner_st.executeUpdate();
                inner_st.close();
            }

            dbConn.getConn().commit();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to create new tool");
        }
        return toolid;
    }

    public ArrayList<LendingLog> fetchToolLogs(int tid) {
        ArrayList<LendingLog> logSet = new ArrayList<>();

        try {
            PreparedStatement stmt = dbConn.getConn().prepareStatement("SELECT idlog FROM log_relation WHERE idtool = ?");
            stmt.setInt(1, tid);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                logSet.add(dbConn.fetchLendingLog(rs.getInt(1)));
            }

            System.out.println("Successfully returned list of 'LendingLog' related to tool: " + tid);

        } catch (Exception e) {
            System.out.println("Failed to pull tool logs from DB.");
            e.printStackTrace();
        }

        return logSet ;
    }
}

