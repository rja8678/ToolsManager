package ObjectClasses;

import java.sql.Date;

public class LendingLogClass {
    private int logID;
    private Date logDate;
    private int action;
    private Date returnDate;

    public LendingLogClass(int logID, Date logDate, int action, Date returnDate){
        this.logID = logID;
        this.logDate = logDate;
        this.returnDate = returnDate;
    }

    public int getLogID() {
        return logID;
    }

    public Date getLogDate() {
        return logDate;
    }

    public int getAction() {
        return action;
    }

    public Date getReturnDate() {
        return returnDate;
    }
}
