package ObjectClasses;

/**
 * Enumerator for representing the different types of actions represented in the Lending Log
 *
 * @author asn3371
 */
public enum ActionType {
    Lend (0, "Lent"),
    Return (1, "Returned");

    private final int databaseValue;
    private final String stringName;

    /**
     * Private constructor used to associate the integer values inside the database with teh different enumerator values
     * @param databaseValue the int in the database that represents each type of action
     * @param stringName clean string name of the action type
     */
    ActionType(int databaseValue, String stringName) {
        this.databaseValue = databaseValue;
        this.stringName = stringName;
    }

    /**
     * accessor for the database value of an enum value
     * @return this.databaseValue
     */
    public int getDatabaseValue(){return databaseValue;}

    /**
     * accessor for the clean string name of the type of action
     * @return this.stringName
     */
    public String getStringName() {
        return stringName;
    }
}
