package cs.rit.edu.CommandLineBackup;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Enumerator representing a Java Class
 */
public enum UserCommand {
    HELP("Displays the available commands and information", "", "help"),
    MAKETOOL("Create a Tool and add to your collection", "name(string), lendabl(bool), type...(integers)", "make", "maketool"),
    LENDTOOL("Lend a Tool to another user", "toolID(int), userID(int), returnDate(\"yyyy-mm-dd\")", "lend", "lendtool"),
    RETURNTOOL("Return a borrowed tool", "toolID(int), userID(int)", "return", "returntool"),
    OWNED("Display all of your owned tools", "", "owned", "displayowned"),
    COLLECTION("Display all of the tools in your collection", "", "collection", "displaycollection"),
    LOGS("Display the logs involving you", "", "logs", "displaylogs"),
    TOOLTYPES("Displays the available tool types and their IDs", "", "tooltypes", "displaytooltypes"),
    GETUSERS("Displays the other users in the Database", "", "users", "getusers"),
    QUIT("Quit the program", "", "quit", "q"),
    ;

    private String message;
    private String arguments;
    private ArrayList<String> names;

    /**
     * Constructor for the enum values
     * @param message help message associated with the command
     * @param arguments arguments for teh command
     * @param names string names that can be used to execute this command
     */
    UserCommand(String message, String arguments, String... names){
        this.arguments = arguments;
        this.message = message;
        this.names = new ArrayList<>(Arrays.asList(names));
    }

    /**
     * accessor for the 'help message' corresponding to this command
     * @return message associated with an enum value
     */
    public String getMessage() {
        return message;
    }

    /**
     * gets all the string names associated with an command
     * @return the collection of names
     */
    public ArrayList<String> getNames(){
        return this.names;
    }

    /**
     * accessor for the arguments of a command
     * @return this.arguments
     */
    public String getArguments(){
        return this.arguments;
    }
}
