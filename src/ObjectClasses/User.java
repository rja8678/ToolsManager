package ObjectClasses;

import DBContollerPackage.DBUser;
import cs.rit.edu.DBConn;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Object that stores User data and methods, including functionality for lending and returning tools
 *
 * @author asn3371
 */
public class User {

    private DBUser dbu = null;

    private int userID;

    private String firstName;

    private String lastName;

    private HashMap<Integer, Tool> toolCollection;

    private HashMap<Integer, Tool> ownedTools;

    /**
     * Constructor used by the DBUser to create a user object. This should not be filled with any data except
     */
    public User(int userID, String firstName, String lastName,
                HashMap<Integer, Tool> toolCollection, HashMap<Integer, Tool> ownedTools){
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;

        this.toolCollection = toolCollection;
        this.ownedTools = ownedTools;
    }

    /**
     * constructor for retrieving a User object from the database based on ID
     * @param id the unique id identifying the user
     * @param conn the connection to the database
     */
    public User (int id, DBConn conn) {
        DBUser dbu = new DBUser(conn) ;

        User t = dbu.createUserObject(id);

        this.dbu = dbu;
        this.userID = t.userID;
        this.firstName = t.firstName;
        this.lastName = t.lastName;
        this.toolCollection = t.toolCollection;
        this.ownedTools = t.ownedTools;
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

    public boolean addToCollection(Tool tool){
        int toolID = tool.getToolID();

        try {
            if(dbu.addToCollection(this.userID,toolID)) {
                this.toolCollection.put(tool.getToolID(), tool);
                return true;
            }
            else {
                System.out.println("Failed to add tool "+ toolID +" to collection: " +this.userID);
                return false;
            }

        } catch (Exception e) {
            System.out.println("DB-FAILURE: Failed to add tool "+ toolID +" to collection: " +this.userID);
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    public Tool getToolFromCollection(int toolID){
        return this.toolCollection.get(toolID);
    }

    public boolean removeFromCollection(Tool tool){
        int toolID = tool.getToolID();
        try {
            if(dbu.removeFromCollection(this.userID,toolID)) {
                this.toolCollection.remove(toolID);
                return true;
            }
            else {
                System.out.println("Failed to remove tool "+ toolID +" from collection: " +this.userID);
                return false;
            }

        } catch (Exception e) {
            System.out.println("DB-FAILURE: Failed to remove tool "+ toolID +" from collection: " +this.userID);
            System.out.println(e.getStackTrace());
            return false;
        }
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

    public boolean addToOwned(Tool tool){
        int toolID = tool.getToolID();

        try {
            if(dbu.addToOwned(this.userID,toolID)) {
                this.ownedTools.put(tool.getToolID(), tool);
                return true;
            }
            else {
                System.out.println("Failed to add tool "+ toolID +" to owner: " +this.userID);
                return false;
            }

        } catch (Exception e) {
            System.out.println("DB-FAILURE: Failed to add tool "+ toolID +" to owner: " +this.userID);
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    public Tool getToolFromOwned(int toolID){
        return this.ownedTools.get(toolID);
    }

    public boolean removeFromOwned(Tool tool){
        int toolID = tool.getToolID();

        try {
            if(dbu.addToOwned(this.userID,toolID)) {
                this.ownedTools.remove(toolID);
                return true;
            }
            else {
                System.out.println("Failed to remove tool "+ toolID +" from owner: " +this.userID);
                return false;
            }

        } catch (Exception e) {
            System.out.println("DB-FAILURE: Failed to remove tool "+ toolID +" from owner: " +this.userID);
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    public ArrayList<LendingLog> getLendingLogs() {
        return this.dbu.fetchUserLogs(this.userID) ;
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
     * Alternative toString that presents the User in a nicer format
     */
    public String betterToString(){
        return "" + firstName + " " + lastName;
    }


    /**
     * Handles lending a tool out to another user. You must own the tool, have it in your
     * collection, and it must be flagged as lendable to be lent out
     *
     * this method only affects the user object. All DB is
     *
     * @param tool the tool object
     * @param user_to the user to give the tool to
     * @return boolean returns true if operation completed successfully.
     */
    public boolean lendTool(Tool tool, User user_to, Date returnDate){
        //check if tool is owned by you and is currently in your possession
        if(this.ownedTools.containsKey(tool.getToolID())) {
            if (this.toolCollection.containsKey(tool.getToolID())) {
                //then check if it is marked as lendable
                if (tool.isLendable()) {
                    if(this.removeFromCollection(tool) && user_to.addToCollection(tool)) {
                        //todo make a log object and store this transaction in db, also prob need 'return date' as a param
                            new LendingLog(dbu.getConn(), new java.sql.Date(System.currentTimeMillis()),
                                ActionType.Lend, returnDate, tool.getToolID(), user_to.getUserID(), this.userID);
                        return true ;
                    }
                    else {
                        System.out.println("Database failure.  Data might be broken.");
                    }
                } else {
                    System.out.println("Tool you own and have with id " + tool.getToolID() + " is not marked as lendable");
                }
            } else {
                System.out.println("Tool you own with id: " + tool.getToolID() + " is not currently in your collection, and cannot be lent");
            }
        } else {
            System.out.println("Tool with id: " + tool.getToolID() + " is not owned by you, and cannot be lent");
        }
        return false ;
    }


    /**
     * Function that returns a given tool from this User's collection back to the tool's owner
     * If you do not have tool in your collection, function will not return tool and will print error statement
     * @param tool the tool to be returned
     * @return true if the tool was successfully given back; false otherwise
     */
    public boolean returnTool(Tool tool){
        if(tool.getOwnerID() == this.userID){
            System.out.println("Can't return tool back to yourself");
            return false;
        } else {
            //check if the user_to owns this tool
            User userTo = dbu.createUserObject(tool.getOwnerID());
            //check if you have tool in your collection
            if (this.getToolCollection().contains(tool)) {
                this.removeFromCollection(tool);
                userTo.addToCollection(tool);
                //todo make sure there is a corresponding USERDB function to properly manipulate database to manipulate this

                //todo use this log to make proper database update
                new LendingLog(dbu.getConn(), new java.sql.Date(System.currentTimeMillis()),
                        ActionType.Return, null, tool.getToolID(), userTo.getUserID(), this.getUserID());

                System.out.println("Tool " + tool.toString() + " was successfully returned to User "
                        + userTo.toString() + "by User " + this.toString());
                return true;
            } else {
                System.out.println("Unable to return Tool " + tool.toString() + " because it is not in your collection");
                return false;
            }
        }
    }


}
