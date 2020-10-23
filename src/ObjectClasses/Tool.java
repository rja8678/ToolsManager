package ObjectClasses;

import java.sql.Date;
import java.util.ArrayList;

import DBContollerPackage.DBTool;
import cs.rit.edu.DBConn;

public class Tool {

    private int toolID;

    private String toolName;

    private boolean lendable;

    private Date purchaseDate;

    private ArrayList<String> toolTypes;


    /**
     * constructor for a tool object
     * @param toolID the unique id of the tool
     * @param toolName the name of the tool (not unique)
     * @param purchaseDate the date the tool was purchased
     * @param lendable whether or not the tool is flaggd and being lendable
     * @param toolTypes a fully populated list
     */
    public Tool(int toolID, String toolName, Date purchaseDate, boolean lendable, ArrayList<String> toolTypes){
        this.toolID = toolID;
        this.toolName = toolName;
        this.purchaseDate = purchaseDate;
        this.lendable = lendable;
        this.toolTypes = toolTypes;
    }

    public Tool (int toolID, DBConn conn) {
        DBTool dbt = new DBTool(conn) ;

        Tool t = dbt.fetchTool(toolID);

        this.toolID = t.toolID;
        this.toolName = t.toolName;
        this.purchaseDate = t.purchaseDate;
        this.lendable = t.lendable;
        this.toolTypes = t.toolTypes;
    }

    public Tool (String toolName, Date purchaseDate, boolean lendable, ArrayList<Integer> toolTypes, DBConn conn) {
        DBTool dbt = new DBTool(conn);

        int newToolId = dbt.insertNewTool(toolName, purchaseDate, lendable, toolTypes);
        Tool t = new Tool(newToolId, conn);

        this.toolID = t.toolID;
        this.toolName = t.toolName;
        this.purchaseDate = t.purchaseDate;
        this.lendable = t.lendable;
        this.toolTypes = t.toolTypes;
    }

    public int getToolID(){
        return this.toolID;
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

    /**
     * returns String in tuple format representing this tool
     * @return tuple of this tool in string format
     */
    @Override
    public String toString() {
        return "{id: " + this.toolID
                + ", name: " + this.toolName
                + ", purchase_date: " + this.purchaseDate
                + ", types: " + this.toolTypes.toString()
                + ", lendable " + this.lendable + "}";
    }
}
