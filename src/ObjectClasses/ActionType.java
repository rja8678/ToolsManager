package ObjectClasses;

/**
 * Enumerator for representing the different types of actions represented in the Lending Log
 *
 * @author asn3371
 */
public enum ActionType {
    Lend (0),
    Return (1);

    private final int databaseValue;

    /**
     * Private constructor used to associate the integer values inside the database with teh different enumerator values
     * @param databaseValue the int in the database that represents each type of action
     */
    ActionType(int databaseValue) {
        this.databaseValue = databaseValue;
    }

    /**
     * accessor for the database value of an enum value
     * @return this.databaseValue
     */
    public int getDatabaseValue(){return databaseValue;}
}
