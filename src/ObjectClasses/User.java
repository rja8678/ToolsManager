package ObjectClasses;

import DBContollerPackage.DBUser;
import cs.rit.edu.DBConn;

import java.util.*;


public class User {

    private DBUser dbUser;

    private int userID;

    private String firstName;

    private String lastName;

    private HashMap<Integer, Tool> tool_collection;

    private HashMap<Integer, Tool> owned_tools;

    /**
     * temp constructor for user object. just for testing purposes before database implementation
     */
    public User(int userID, String firstName, String lastName){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;

        this.tool_collection = new HashMap<>();

        this.owned_tools = new HashMap<>();
    }


    public int getUserID() {
        return userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void addToCollection(Tool tool){
        this.tool_collection.put(tool.getToolID(), tool);
    }

    public Tool getToolFromCollection(int toolID){
        return this.tool_collection.get(toolID);
    }

    public void removeFromCollection(int toolID){
        this.tool_collection.remove(toolID);
    }


    public void addToOwned(Tool tool){
        this.owned_tools.put(tool.getToolID(), tool);
    }

    public Tool getToolFromOwned(int toolID){
        return this.owned_tools.get(toolID);
    }

    public void removeFromOwned(int toolID){
        this.owned_tools.remove(toolID);
    }

    @Override
    public String toString() {
        return "" + userID + "{firstName: " + this.firstName + ", lastName: " + this.lastName + "}";
    }


    /**
     * Handles lending a tool out to another user. You must own the tool, have it in your
     * collection, and it must be flagged as lendable to be lent out
     * @param toolID the unique key to find a tool
     * @param user the user to give the tool to
     */
    public void lendTool(int toolID, User user){
        //check if tool is owned by you and is currently in your possession
        if(this.owned_tools.containsKey(toolID)) {
            if (this.tool_collection.containsKey(toolID)) {
                Tool lentTool = this.getToolFromOwned(toolID);
                //then check if it is marked as lendable
                if (lentTool.isLendable()) {
                    this.removeFromCollection(toolID);
                    user.addToCollection(lentTool);
                    //dbUser.dbLendTool(); // todo SCRATCH CODE - need to write code in DBUser
                    //todo do database update
                } else {
                    System.out.println("Tool you own and have with id " + toolID + " is not marked as lendable");
                }
            } else {
                System.out.println("Tool you own with id: " + toolID + " is not currently in your collection, and cannot be lent");
            }
        } else {
            System.out.println("Tool with id: " + toolID + " is not owned by you, and cannot be lent");
        }
    }



}
