package cs.rit.edu.CommandLineBackup;

/**
 * Exception used to signify that the user has entered some form of erroneous information
 *
 * @author asn3371
 */
public class UserInputException extends Exception{

    /**
     * constructor for a UserInputException
     * @param message message for what caused the exception
     */
    public UserInputException(String message){
        super(message);
    }
}
