package ObjectClasses;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

public class User {

    private int userID;

    private String firstName;

    private String lastName;

    private HashMap<Integer, Tool> toolCollection;

    private HashMap<Integer, Tool> ownedTools;

    /**
     * temp constructor for user object. just for testing purposes before database implementation
     */
    public User(int userID, String firstName, String lastName,
                HashMap<Integer, Tool> toolCollection, HashMap<Integer, Tool> ownedTools){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;

        this.toolCollection = toolCollection;
        this.ownedTools = ownedTools;
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
        this.toolCollection.put(tool.getToolID(), tool);
    }

    public Tool getToolFromCollection(int toolID){
        return this.toolCollection.get(toolID);
    }

    public void removeFromCollection(int toolID){
        this.toolCollection.remove(toolID);
    }

    public int getNumToolsInCollection(){
        return this.toolCollection.size();
    }

    public int getNumOwnedTools(){
        return this.ownedTools.size();
    }

    public LinkedList<Tool> getToolCollection() {
    	return new LinkedList<Tool>(toolCollection.values());
    }
    
    public LinkedList<Tool> getOwnedTools() {
    	return new LinkedList<Tool>(ownedTools.values());
    }

    public void addToOwned(Tool tool){
        this.ownedTools.put(tool.getToolID(), tool);
    }

    public Tool getToolFromOwned(int toolID){
        return this.ownedTools.get(toolID);
    }

    public void removeFromOwned(int toolID){
        this.ownedTools.remove(toolID);
    }



    /**
     * returns a String in Tuple form represetning this user
     * @return String representing this User
     */
    @Override
    public String toString() {
        return "{userID: " + userID + ", firstName: " + this.firstName + ", lastName: " + this.lastName + ", OwnedTools: " + this.ownedTools.toString() +", Collection: "+ this.toolCollection.toString() +"}";
    }


    /**
     * Handles lending a tool out to another user. You must own the tool, have it in your
     * collection, and it must be flagged as lendable to be lent out
     *
     * this method only affects the user object. All DB is
     *
     * @param toolID the unique key to find a tool
     * @param user the user to give the tool to
     */
    public void lendTool(int toolID, User user){
        //check if tool is owned by you and is currently in your possession
        if(this.ownedTools.containsKey(toolID)) {
            if (this.toolCollection.containsKey(toolID)) {
                Tool lentTool = this.getToolFromOwned(toolID);
                //then check if it is marked as lendable
                if (lentTool.isLendable()) {
                    this.removeFromCollection(toolID);
                    user.addToCollection(lentTool);
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
