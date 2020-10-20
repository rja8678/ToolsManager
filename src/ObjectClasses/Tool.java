package ObjectClasses;


import java.sql.Date;
import java.util.ArrayList;

public class Tool {

    private int toolID;

    private String toolName;

    private boolean lendable;

    private Date purchaseDate;

    //todo
    private ArrayList<ToolType> toolTypes;


    /**
     * constructor for a tool object
     * @param toolID the unqiue id of the tool
     * @param toolName the name of the tool (not unique)
     * @param purchaseDate the date the tool was purchased
     * @param lendable whether or not the tool is flaggd and being lendable
     */
    public Tool(int toolID, String toolName, Date purchaseDate, boolean lendable){
        this.toolID = toolID;
        this.toolName = toolName;
        this.purchaseDate = purchaseDate;
        this.lendable = lendable;
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
        //todo call function in DBTool that will update database entry
    }

    public Date getPurchaseDate(){
        return this.purchaseDate;
    }


    @Override
    public String toString() {
        return "{id: " + this.toolID
                + ", name: " + this.toolName
                + ", purchase_date: " + this.purchaseDate
                + ", lendable " + this.lendable + "}";
    }
}
