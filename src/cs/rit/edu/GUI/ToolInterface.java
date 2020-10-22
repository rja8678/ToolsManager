package cs.rit.edu.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolInterface extends Application{

    static User appUser = null ;
    static DBConn conn = null ;
    
    VBox collectionToolName, collectionPurchaseDate, ownedToolName, ownedPurchaseDate, ownedLendable, lendPane;
    HBox collectionList, ownedList;
    
    @Override
    public void stop() {
    	//Closing the connection to the database formally
    	conn.closeConn();
    }
    
	@Override
	public void start(Stage stage) throws Exception {
		TextField username = new TextField();
		username.setPromptText("Username");

		Button loginBtn = new Button();
        loginBtn.setText("Login");
        //Called when the user tries to login
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                String usernameInput = username.getText();
                
                System.out.println(Integer.parseInt(usernameInput));

                appUser = new User(Integer.parseInt(usernameInput), conn);
                
                //Call setup that requires the appUser here
                refreshToolCollection(appUser.getToolCollection());
                refreshToolsOwned(appUser.getOwnedTools());
                lendingPaneInit();
            }
        });

        //Login at top of the screen
        VBox usernameGroup = new VBox();
        Label usernameLab = new Label("Username: ");
        usernameGroup.getChildren().add(usernameLab);
        usernameGroup.getChildren().add(username);

        HBox login = new HBox();
        login.setAlignment(Pos.CENTER_LEFT);
        login.getChildren().add(usernameGroup);
        login.getChildren().add(loginBtn);

        
        //Collection in the center, holds the collection of tools and owned tools
        HBox toolLists = createCollections();
        
        //Creates the tool creation menu that allows a user to make a new tool
        VBox toolCreation = toolCreationMenu();
        
        lendPane = new VBox();
        
        //Main pane that holds all the sub panes
        BorderPane pane = new BorderPane();
        pane.setTop(login);
        pane.setCenter(toolLists);
        pane.setLeft(toolCreation);
        pane.setRight(lendPane);
        
        Scene scene = new Scene(pane, 300, 250);

        stage.setTitle("Tool Interface");
        stage.setScene(scene);
        stage.show();
    }
	
	/**
	 * Creates the "tables" for displaying both the tool collection and owned tools
	 * Called once on initialization
	 * @return The HBox holding those tables to be added to the BorderPane
	 */
	public HBox createCollections() {
		//Creating columns for the collection
		HBox toolLists = new HBox();
        
        collectionList = new HBox();
        ownedList = new HBox();
		
        collectionToolName = new VBox();
        collectionPurchaseDate = new VBox();
        
        //Creating columns for the owned tools section
        ownedToolName = new VBox();
        ownedPurchaseDate = new VBox();
        ownedLendable = new VBox();
        
        collectionList.getChildren().add(collectionToolName);
        collectionList.getChildren().add(new Separator());
        collectionList.getChildren().add(collectionPurchaseDate);
        
        ownedList.getChildren().add(ownedToolName);
        ownedList.getChildren().add(new Separator());
        ownedList.getChildren().add(ownedPurchaseDate);
        ownedList.getChildren().add(new Separator());
        ownedList.getChildren().add(ownedLendable);
        

        toolLists.getChildren().add(collectionList);
        toolLists.getChildren().add(new Separator());
        toolLists.getChildren().add(ownedList);
        
        return toolLists;
	}
	
	/**
	 * Displays a list of tools for the user representing their collection
	 * @param tools List of tools to display
	 */
	public void refreshToolCollection(List<Tool> tools) {
		collectionToolName.getChildren().clear();
		collectionPurchaseDate.getChildren().clear();

        collectionToolName.getChildren().add(new Label("Tool Names"));
        collectionPurchaseDate.getChildren().add(new Label("Purchase Dates"));
		
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
			
			collectionToolName.getChildren().add(toolName);
			collectionPurchaseDate.getChildren().add(purchaseDate);
		}
	}
	
	/**
	 * Function to set up the pane allowing the user to lend a tool to someone
	 * Created once a user logs in
	 */
	public void lendingPaneInit() {
		lendPane.getChildren().clear();
		
        Label lendPaneLabel = new Label("Lending");
        lendPane.getChildren().add(lendPaneLabel);
		
		LinkedList<Tool> ownedTools = appUser.getOwnedTools();
        ArrayList<String> toolNames = new ArrayList<>();
        for (Tool tool: ownedTools) {
        	toolNames.add(tool.getToolName());
        }
        
        HashMap<Integer, String> allUsers = conn.fetchAllUsers();
        
        ArrayList<String> usersNames = new ArrayList<>();
        for (Integer userID: allUsers.keySet()) {
        	//TODO Remove the appUsers name from the list
        	usersNames.add(allUsers.get(userID)+" "+userID);
        }
        
        ComboBox<String> toolsDrop = new ComboBox<String>(FXCollections.observableArrayList(toolNames));
        ComboBox<String> usersDrop = new ComboBox<String>(FXCollections.observableArrayList(usersNames));
        
        //TODO Find what values are selected on the ComboBoxes then actually send the request to the database
        
        lendPane.getChildren().add(toolsDrop);
        lendPane.getChildren().add(usersDrop);
	}
	
	/**
	 * Function to make the tool creation sidebar, added to the main BorderPane
	 * Called once on initialization
	 * @return A VBox containing everything necessary to make a tool
	 */
	public VBox toolCreationMenu() {
		//Making the input fields
		VBox toolCreation = new VBox();
        Label toolNameLabel = new Label("Tool Name: ");
        TextField toolNameEntry = new TextField();
        
        HBox toolName = new HBox();
        toolName.getChildren().add(toolNameLabel);
        toolName.getChildren().add(toolNameEntry);
        
        Label toolPurchaseLabel = new Label("Date Purchased: ");
        DatePicker toolPurchaseDate = new DatePicker();

        HBox toolPurchase = new HBox();
        toolPurchase.getChildren().add(toolPurchaseLabel);
        toolPurchase.getChildren().add(toolPurchaseDate);
        
        Label lendableToolLabel = new Label("Is Tool Lendable: ");
        CheckBox lendableToolCheck = new CheckBox();
        
        HBox lendableTool = new HBox();
        lendableTool.getChildren().add(lendableToolLabel);
        lendableTool.getChildren().add(lendableToolCheck);
        
        
        //Tool Types Checkboxes to select what types this tool is
        HBox listOfTypes = new HBox();
        VBox toolTypeName = new VBox();
        VBox checkBoxes = new VBox();
        
        listOfTypes.getChildren().add(toolTypeName);
        listOfTypes.getChildren().add(checkBoxes);
        
        //Getting the tool types from the database
        HashMap<Integer, String> toolTypes = conn.fetchAllToolTypes();
        //Storing the checkboxes to figure out which were selected on the button press
        List<CheckBox> selectedToolTypes = new ArrayList<>();
        
        //Creating the checkboxes with labels
        for(String toolType: toolTypes.values()) {
        	Label toolTypeLabel = new Label(toolType);
        	toolTypeName.getChildren().add(toolTypeLabel);
        	
        	CheckBox checkBox = new CheckBox();
        	checkBoxes.getChildren().add(checkBox);
        	selectedToolTypes.add(checkBox);
        }
        
        Button createTool = new Button();
        createTool.setText("Create Tool");
        //Runs when the user clicks to create a new tool
        createTool.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                //TODO Needs to take the list of checkboxes and tool types names and convert it to a list of tool type id's selected
                Iterator<String> toolTypesI = toolTypes.values().iterator();
                Iterator<CheckBox> selectedToolTypesI = selectedToolTypes.iterator();
                LinkedList<String> output = new LinkedList<>();
                while(toolTypesI.hasNext()) {
                	//Checks if the checkbox was selected
                	if (selectedToolTypesI.next().isSelected()) {
                		//If it was find the id associated with the tool type name that was selected
                		//Tool type name is actually toolTypesI.next(), don't call multiple times otherwise it breaks
                		output.add(toolTypesI.next());
                	}
                }
                
                //TODO Create the tool on the database using the data from the fields above
                //Ex: toolPurchaseDate.getValue()
            }
        });
        
        toolCreation.getChildren().add(toolName);
        toolCreation.getChildren().add(toolPurchase);
        toolCreation.getChildren().add(listOfTypes);
        toolCreation.getChildren().add(lendableTool);
        toolCreation.getChildren().add(createTool);
        
        return toolCreation;
	}
	
	/**
	 * Displays a list of tools for the user representing their owned tools
	 * @param tools List of tools to display
	 */
	public void refreshToolsOwned(List<Tool> tools) {
		ownedToolName.getChildren().clear();
		ownedPurchaseDate.getChildren().clear();
		ownedLendable.getChildren().clear();
		
        ownedToolName.getChildren().add(new Label("Tool Names"));
        ownedPurchaseDate.getChildren().add(new Label("Purchase Dates"));
        ownedLendable.getChildren().add(new Label("Lendability"));
		
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
			Label lendable = new Label((tool.isLendable() ? "True": "False"));

			ownedToolName.getChildren().add(toolName);
			ownedPurchaseDate.getChildren().add(purchaseDate);
			ownedLendable.getChildren().add(lendable);
		}
	}
	
	/**
	 * Connects to the database and starts the GUI interface
	 * @param args Takes the username to the database and then the password as arguments with just a space in between them
	 */
	public static void main(String[] args) {
		if(args.length > 1) {
			String username = args[0];
			String password = args[1];

            conn = new DBConn(username, password);

			launch(args);
		}else {
			System.out.println("Please give the database username and password as commandline arguments");
		}
	}
}
