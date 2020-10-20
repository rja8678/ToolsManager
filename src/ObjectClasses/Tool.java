package ObjectClasses;


import java.sql.Date;
import java.util.ArrayList;

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
     * returns String in tuple format representing this tool
     * @return
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
