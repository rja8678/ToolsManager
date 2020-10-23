package ObjectClasses;

import DBContollerPackage.DBUser;
import cs.rit.edu.DBConn;

import javax.swing.*;
import java.sql.Date;

/**
 * Class used to represent a member of the Lending Log, which
 * is sued to keep track of actions taken by users on the database
 *
 * @author asn3371
 */
public class LendingLog {

//    private DBLL dbll = null;
    //unique integer associated with this log
    private int logID;
    //the date the log was created
    private Date logDate;
    //the action that classifies this log
    private ActionType action;
    //the date that the tool must be returned by. Applicable for lending actions
    private Date returnDate;

    private int toolID;
    private int toUserID;
    private int fromUserID;

    /**
     * constructor for a LendingLog
     * @param logID unique ID for this log
     * @param logDate the date this log was created
     * @param action the type of action this log records
     * @param returnDate the date that the tool associated with this log needs to be returned
     */
    public LendingLog(int logID, Date logDate, ActionType action, Date returnDate, int toolID, int toUserID, int fromUserID){
        this.logID = logID;
        this.logDate = logDate;
        this.action = action;
        this.returnDate = returnDate;

        this.toolID = toolID;
        this.toUserID = toUserID;
        this.fromUserID = fromUserID;
    }

    /**
     * alternative constructor for a Log
     * This constructor is used when the Application generates a new log and needs to file in inside the database
     * @param dbu the connection to the database
     * @param logDate the date the log was generated at
     * @param action the type of action this log is
     * @param returnDate the date the tool needs to be returned to
     * @param toolID the unique too id
     * @param toUserID the unique sending user id
     * @param fromUserID the unique receiving user id
     */
    public LendingLog(DBUser dbu, Date logDate, ActionType action, Date returnDate, int toolID, int toUserID, int fromUserID){
        //todo make proper DB connection and do stuff
        int newId = dbu.insertLendingLog(logDate, action, returnDate, toolID, toUserID, fromUserID);

        LendingLog temp = dbu.fetchLendingLog(newId);

        this.logDate = temp.getLogDate();
        this.action = temp.getAction();
        this.returnDate = temp.getReturnDate();
        this.toolID = temp.getToolID();
        this.toUserID = temp.getToUserID();
        this.fromUserID = temp.getFromUserID();
    }


    /**
     * Constructor for relieving a Lending Log from the database
     * TODO needs a DBLL to handle database connections before this will work
     *
     * @param id unique ID identifying this log
     * @param dbu the connection to the user db
     */
    public LendingLog(int id, DBUser dbu){

        LendingLog log = dbu.fetchLendingLog(id);

        this.logID = log.getLogID();
        this.logDate = log.getReturnDate();
        this.action = log.getAction();
        this.returnDate = log.getReturnDate();

        this.fromUserID = log.getFromUserID();
        this.toUserID = log.getToUserID();
        this.toolID = log.getToolID();
    }



    public int getLogID() {
        return logID;
    }

    public Date getLogDate() {
        return logDate;
    }

    public ActionType getAction() {
        return action;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public int getToolID() {
        return toolID;
    }

    public int getToUserID() {
        return toUserID;
    }

    public int getFromUserID() {
        return fromUserID;
    }

    /**
     * returns a tuple congaing the relevant data contained within this LendingLog
     * @return debug tuple containing the log info as a string
     */
    public String debugToString() {
        return "{logID : " + this.logID + "" +
                " userToID: " + this.toUserID +
                ", userFromID: " + this.fromUserID +
                ", toolID: " + this.toolID +
                ", logDate: " + this.logDate.toString() +
                ", returnDate: " + this.returnDate.toString() +
                ", actionType: " + this.action +
                "}";
    }

    /**
     * Cleaner, displayable version of this log
     * Format: User [FROMUSER] [Lent/Returned] Tool [TOOLID] to User [TOUSER] on the day [logDate]
     * @return lending log data in above format
     */
    @Override
    public String toString() {
        if(action == ActionType.Lend) {
            //Lending case
            return this.logID + ": User " + fromUserID + " " + action.getStringName() + " Tool " + toolID +
                    " to User " + toUserID + " on the day " + this.logDate.toString() +
                    " until the day " + this.returnDate.toString();
        } else {
            //no return dates for Returning actions
            return this.logID + ": User " + fromUserID + " " + action.getStringName() + " Tool " + toolID +
                    " to User " + toUserID + " on the day " + this.logDate.toString();
        }
    }
}
