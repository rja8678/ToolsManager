package ObjectClasses;


import java.util.ArrayList;

public class Tool {

    private int toolID;

    private String toolName;

    private boolean lendable;

    //private  purchase_date; //todo might have to fix to use postgres stuff

    private ArrayList<ToolType> toolTypes;


    public int getToolID(){
        return this.toolID;
    }

    public boolean isLendable(){
        return lendable;
    }
}
