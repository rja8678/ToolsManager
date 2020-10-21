package ObjectClasses;

import cs.rit.edu.DBConn;

import java.sql.Date;

/**
 * Class used to represent a member of the Lending Log, which
 * is sued to keep track of actions taken by users on the database
 *
 * @author asn3371
 */
public class LendingLog {
    //unique integer associated with this log
    private int logID;
    //the date the log was created
    private Date logDate;
    //the action that classifies this log
    private ActionType action;
    //the date that the tool must be returned by. Applicable for lending actions
    private Date returnDate;

    /**
     * constructor for a LendingLog
     * @param logID unique ID for this log
     * @param logDate the date this log was created
     * @param action the type of action this log records
     * @param returnDate the date that the tool associated with this log needs to be returned
     */
    public LendingLog(int logID, Date logDate, ActionType action, Date returnDate){
        this.logID = logID;
        this.logDate = logDate;
        this.action = action;
        this.returnDate = returnDate;
    }

    /*
    /**
     * Constructor for relieving a Lending Log from the database
     * TODO needs a DBLL to handle database connections before this will work
     *
     * @param id unique ID identifying this log
     * @param conn the connection to the ID
     *//*
    public LendingLog(int id, DBConn conn){
        DBLL dbll = new DBLL(conn);

        LendingLog log = dbll.createLendingLog(id);

        this.logID = log.getLogID();
        this.logDate = log.getReturnDate();
        this.action = log.getAction();
        this.returnDate = log.getReturnDate();
    }
    */


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


}
