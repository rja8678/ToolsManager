package cs.rit.edu.CommandLineBackup;

import DBContollerPackage.DBTool;
import ObjectClasses.LendingLog;
import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Backup user interface that uses a command line interface for the user to interact with the database
 */
public class TextInterface {
    
    private DBConn conn;
    private User user;
    
    private final String COMMAND_PROMPT = ">";

    public TextInterface(DBConn conn, int userID){
        this.conn = conn;
        //TODO Make sure that the database actually contains a user with this ID. No invalid IDs
        user = new User(userID, conn);
    }

    /**
     * Formats a nice help box detailing each user command and its details and returns it
     * @return help box with user command info
     */
    public String getHelp(){
        String helpBox = "";
        helpBox += "primary name | description | args \n";
        for(UserCommand comm:UserCommand.values()){
            helpBox += comm.getNames().get(0) + " | " +
                    comm.getMessage() + " | " +
                    comm.getArguments() + "\n";
        }
        return helpBox;
    }

    /**
     * inserts a new Tool into the database
     * Sets the purchase date to the current date
     * @param name the name of the tool
     * @param lendable whether or not the tool can be lent out
     * @param types the tool types of the tool
     */
    public void makeTool(String name, boolean lendable, ArrayList<Integer> types){
        DBTool dbTool = new DBTool(conn);
        dbTool.insertNewTool(name, new Date(System.currentTimeMillis()), lendable, types);
    }

    /**
     * Lends out a tool with a given ID to a given User
     * @param toolID the ID of the tool we are lending out
     * @param toUserID the ID of the User we are lending to
     * @param returnDate the date the tool should be returned by
     */
    public void lendTool(int toolID, int toUserID, Date returnDate){
        //TODO make sure that both this tool and this user actually exist
        //TODO also make sure that we properly reflect this lending change inside the lend tool function
        this.user.lendTool(this.user.getToolFromOwned(toolID), new User(toUserID, conn), returnDate);
    }

    /**
     * returns a tool back to its owner
     * @param toolID the ID of the tool to be returned
     */
    public void returnTool(int toolID){
        //todo make sure that there are guards here against lending to non existent
        this.user.returnTool(this.user.getToolFromCollection(toolID));
    }

    /**
     * displays all the users in the database
     */
    public void displayUsers(){
        System.out.println("Here are the users in the database: ");
        HashMap<Integer, String> allUsers= conn.fetchAllUsers();
        for(int id:allUsers.keySet()){
            System.out.println("ID: " + id + ", Name: " + allUsers.get(id));
        }
    }

    /**
     * Displays the collection of the signed in user
     */
    public void displayCollection(){
        System.out.println("Here is your Collection of tools: ");
        LinkedList<Tool> collection = user.getToolCollection();
        for(Tool tool:collection){
            System.out.println(tool);
        }
    }

    /**
     * displays the list of all the tools that the signed in user owns
     */
    public void displayOwned(){
        System.out.println("Here are the tools you Own: ");
        LinkedList<Tool> owned = user.getOwnedTools();
        for(Tool tool:owned){
            System.out.println(tool);
        }
    }

    /**
     * Displays all of the logs involving the user
     */
    public void displayLogs(){
        System.out.println("Here are the logs involving you: ");
        ArrayList<LendingLog> logs = user.getLendingLogs();
        for(LendingLog log:logs){
            System.out.println(log.toString());
        }
    }

    /**
     * displays all the tool types currently stored within the database
     */
    public void displayToolTypes(){
        System.out.println("Here are all the tool types that are available: ");
        HashMap<Integer, String> toolTypes = conn.fetchAllToolTypes();
        for(int id:toolTypes.keySet()){
            System.out.println("ID: " + id + ", Name: " + toolTypes.get(id));
        }
    }



    /**
     * Method of the Text Interface that handles getting the user input one line at a time
     */
    public void consoleInput() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            //once input has been set up, initialize output:
            System.out.println("Welcome, " + user.getFirstName() + " " + user.getLastName() + ", UID: " + user.getUserID());
            System.out.println("Type 'help' to see available commands");
            System.out.println("Begin Input:");

            //start reading input
            boolean sentinel = true;
            while(sentinel){
                System.out.print(COMMAND_PROMPT);
                String nextInput = input.readLine();
                sentinel = parseCommand(nextInput);
            }
            //closes Buffered Reader - freeing up resources
            System.out.println("Goodbye...");
            input.close();
            //TODO ensure database resources are properly closed here
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses a single line of input and executes the corresponding command; or prints out a message indicating otherwise
     * @param nextInput a string input representing the command to be parsed
     * @return true if the program should continue accepting commands; false otherwise
     */
    private boolean parseCommand(String nextInput) {
        boolean sentinel = true;
        try {
            UserCommand command = translateCommand(nextInput);
            String[] splitCom = nextInput.split(" ");
            switch (command) {
                case MAKETOOL:
                    if(splitCom.length >= 3){
                        String name = splitCom[1];
                        boolean lendable = (splitCom[2].equals("true"));
                        ArrayList<Integer> types = new ArrayList<>();
                        for(int i = 3; i < splitCom.length; i++){
                            types.add(Integer.parseInt(splitCom[i]));
                        }
                        this.makeTool(name, lendable, types);
                    }
                    break;
                case LENDTOOL:
                    if(splitCom.length == 4) {
                        int toolID = Integer.parseInt(splitCom[1]);
                        int userID = Integer.parseInt(splitCom[2]);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                        try {
                            Date returnDate = new Date((sdf.parse(splitCom[3])).getTime());
                            this.lendTool(toolID, userID, returnDate);
                        } catch (ParseException pe) {
                            System.out.println("Invalid Date Format");
                        }
                    } else {
                        System.out.println("Invalid Lending Arguments");
                    }
                    break;
                case RETURNTOOL:
                    if(splitCom.length == 2) {
                        int toolID = Integer.parseInt(splitCom[1]);
                        this.returnTool(toolID);
                    } else {
                        System.out.println("Invalid Return Arguments");
                    }
                    break;
                case OWNED:
                    this.displayOwned();
                    break;
                case COLLECTION:
                    this.displayCollection();
                    break;
                case LOGS:
                    this.displayLogs();
                    break;
                case HELP:
                    //displays help box
                    System.out.println(getHelp());
                    break;
                case TOOLTYPES:
                    this.displayToolTypes();
                    break;
                case GETUSERS:
                    this.displayUsers();
                    break;
                case QUIT:
                    sentinel = false;
                    break;
            }
        } catch (UserInputException comException) {
            //unknown command received: print message declaring so and go back to start of loop
            System.out.println(comException.getMessage());
        } catch(NumberFormatException nfe){
            System.out.println("Improper usage: User did not input integer for correct arguments");
        }
        return sentinel;
    }


    /**
     * given a string of user input, translates it into a recognized command
     * @param command user input
     * @return UserCommand corresponding to that input
     * @throws UserInputException if there is no command corresponding to that input, throw this exception
     */
    public UserCommand translateCommand(String command) throws UserInputException {
        String[] split = command.split(" "); // get access to individual words in command
        for(UserCommand com:UserCommand.values()){
            if(com.getNames().contains(split[0])){ // check if first word is an actual command
                return com;
            }
        }
        throw new UserInputException("Unrecognized command: " + command);
    }




    /**
     * @param args username password used for connecting to database successfully
     *             followed by the userID of the user we are signing in as
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            //case: provide sign in details via command line
            try {
                //provide all details through console
                System.out.println("Please provide: Database Username, Database Password, userID: ");

                //get input from console
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String nextLine = input.readLine();
                String[] splitLine = nextLine.split(" ");

                //create DBConn, initial user, and begin interface
                String username = splitLine[0];
                String password = splitLine[1];
                int userID = Integer.parseInt(splitLine[3]);

                DBConn conn = new DBConn(username, password);
                TextInterface textInterface = new TextInterface(conn, userID);
                //start input
                textInterface.consoleInput();

            } catch (IOException io){
                io.printStackTrace();
            } catch (NumberFormatException e){
                System.out.println("Please insert an Integer for User ID");
            }

        } else if (args.length == 2){
            //case where you choose user ID as you log in
            try {
                System.out.println("Provide the User ID you are signing in as: ");
                BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
                String nextLine = input.readLine();
                int userID = Integer.parseInt(nextLine);

                String username = args[0];
                String password = args[1];
                DBConn conn = new DBConn(username, password);

                TextInterface textInterface = new TextInterface(conn, userID);
                textInterface.consoleInput();

            } catch (IOException io){
                io.printStackTrace();
            } catch (NumberFormatException ne){
                System.out.println("User ID was not an integer");
            }
        } else if(args.length == 3){
            //case: provided sign in details through arguments
            String username = args[0];
            String password = args[1];
            try {
                int userID = Integer.parseInt(args[2]);
                DBConn conn = new DBConn(username, password);
                TextInterface textInterface = new TextInterface(conn, userID);
                textInterface.consoleInput();
            } catch(NumberFormatException e){
                System.out.println("Usage: third userID parameter must be an integer");
            }
        } else {
            System.out.println("Usage: username password [userID]");
        }
    }
}
