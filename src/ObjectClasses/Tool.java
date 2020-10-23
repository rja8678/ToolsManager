package ObjectClasses;

import java.sql.Date;
import java.util.ArrayList;

import DBContollerPackage.DBTool;
import cs.rit.edu.DBConn;

public class Tool {

    private int toolID;

    private int ownerID;

    private String toolName;

    private boolean lendable;

    private Date purchaseDate;

    private ArrayList<String> toolTypes;


    /**
     * constructor for a tool object
     * @param toolID the unique id of the tool
     * @param ownerID the user id of teh user that owns this tool
     * @param toolName the name of the tool (not unique)
     * @param purchaseDate the date the tool was purchased
     * @param lendable whether or not the tool is flaggd and being lendable
     * @param toolTypes a fully populated list
     */
    public Tool(int toolID, int ownerID, String toolName, Date purchaseDate, boolean lendable, ArrayList<String> toolTypes){
        this.toolID = toolID;
        this.ownerID = ownerID;
        this.toolName = toolName;
        this.purchaseDate = purchaseDate;
        this.lendable = lendable;
        this.toolTypes = toolTypes;
    }

    public Tool (int toolID, DBConn conn) {
        DBTool dbt = new DBTool(conn) ;

        Tool t = dbt.fetchTool(toolID);

        this.toolID = t.getToolID();
        this.ownerID = t.getOwnerID();
        this.toolName = t.getToolName();
        this.purchaseDate = t.getPurchaseDate();
        this.lendable = t.isLendable();
        this.toolTypes = t.getToolTypes();
    }

    public Tool (String toolName, Date purchaseDate, boolean lendable, ArrayList<Integer> toolTypes, DBConn conn) {
        DBTool dbt = new DBTool(conn);

        int newToolId = dbt.insertNewTool(toolName, purchaseDate, lendable, toolTypes);
        Tool t = new Tool(newToolId, conn);

        this.toolID = t.getToolID();
        this.ownerID = t.getOwnerID();
        this.toolName = t.getToolName();
        this.purchaseDate = t.getPurchaseDate();
        this.lendable = t.isLendable();
        this.toolTypes = t.getToolTypes();
    }

    public int getToolID(){
        return this.toolID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public String getToolName(){
        return this.toolName; }

    public boolean isLendable(){
        return lendable;
    }

    public void setLendable(boolean lendable){
        this.lendable = lendable;
    }

    public Date getPurchaseDate(){
        return this.purchaseDate;
    }

    public ArrayList<String> getToolTypes(){
        return this.toolTypes;
    }

    /**
     * Overridden .equals method to allow for deep comparison between tools
     * ToolID is a unique identifier for tools, so if two tools are the same they must have the
     * same toolID
     * @param obj object to compare
     * @return true if obj is the same as this Tool, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Tool){
            Tool toolObj = (Tool) obj;
            return toolObj.getToolID() == this.toolID;
        } else {
            return false;
        }
    }

    public ArrayList<LendingLog> getLendingLogs(DBConn conn) {
        return new DBTool(conn).fetchToolLogs(this.toolID) ;
    }

    /**
     * returns String in tuple format representing this tool
     * @return tuple of this tool in string format
     */
    @Override
    public String toString() {
        return "{id: " + this.toolID
                + ", name: " + this.toolName
                + ", ownerId: " + this.ownerID
                + ", purchase_date: " + this.purchaseDate
                + ", types: " + this.toolTypes.toString()
                + ", lendable " + this.lendable + "}";
    }
}
