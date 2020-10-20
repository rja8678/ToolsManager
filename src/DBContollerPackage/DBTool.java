package DBContollerPackage;

import cs.rit.edu.*;
import ObjectClasses.*;

import java.sql.*;

public class DBTool {
    private DBConn dbConn;

    public DBTool(DBConn dbConn){
        this.dbConn = dbConn;
    }

    public Tool makeTool(int toolID){
        Statement stmt = null;
        String name = null;
        boolean lendable = false;
        Date purchaseDate = null;

        if(!dbConn.connected()) {
            System.out.println("System not connected.");
            return null;
        }
        try {
            stmt = dbConn.getConn().createStatement();
            PreparedStatement st = dbConn.getConn().prepareStatement("SELECT * FROM tool WHERE idtool = ?");
            st.setInt(1, toolID);
            ResultSet rs = st.executeQuery();

            while ( rs.next() ) {
                name= rs.getString("name");
                lendable = rs.getBoolean("lendable"); // todo this may break
                purchaseDate = rs.getDate("purchase_date");
            }
            rs.close();
            stmt.close();

        } catch ( Exception e ) {
            System.err.println( e.getClass().getName()+": "+ e.getMessage() );
            System.exit(0);
        }
        System.out.println("Tool with id " + toolID + " fetched from Database successfully");
        return new Tool(toolID, name, purchaseDate, lendable);//todo
    }
}
