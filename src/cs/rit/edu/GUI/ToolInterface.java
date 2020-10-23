package cs.rit.edu.GUI;


import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ObjectClasses.LendingLog;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Owner Riley Adams(rja8678)
 *
 */
public class ToolInterface extends Application{

    static User appUser = null ;
    static DBConn conn = null ;
    
    VBox collectionToolName, collectionReturnButtons, collectionPurchaseDate, ownedToolName, toolIDCol, ownedPurchaseDate, ownedLendable, lendPane, logDateCol, actionCol, returnDateCol, toUserIDCol, fromUserIDCol;
    HBox collectionList, ownedList;
    
    @Override
    public void stop() {
    	//Closing the connection to the database formally
    	System.out.println("Closing GUI");
    	conn.closeConn();
    }
    
	@Override
	public void start(Stage stage) throws Exception {
		TextField username = new TextField();
		username.setPromptText("User ID");

		Button loginBtn = new Button();
        loginBtn.setText("Login");
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
                refreshLogs(appUser.getLendingLogs(), conn.fetchAllUsers());
            }
        });

        //Login at top of the screen
        VBox usernameGroup = new VBox();
        Label usernameLab = new Label("User ID: ");
        usernameGroup.getChildren().add(usernameLab);
        usernameGroup.getChildren().add(username);

        HBox login = new HBox();
        login.setAlignment(Pos.CENTER_LEFT);
        login.getChildren().add(usernameGroup);
        login.getChildren().add(loginBtn);

        //Collection in the center, holds the collection of tools and owned tools
        HBox toolLists = new HBox();
        ScrollPane toolListScroll = new ScrollPane();
        toolListScroll.setContent(toolLists);
        
        VBox collectionTitle = new VBox();
        VBox ownedTitle = new VBox();
        
        collectionList = new HBox();
        ownedList = new HBox();
        
        collectionTitle.getChildren().add(new Label("Your collection"));
        collectionTitle.getChildren().add(collectionList);
        ownedTitle.getChildren().add(new Label("Your owned tools"));
        ownedTitle.getChildren().add(ownedList);
        
      //Creates the tool creation menu that allows a user to make a new tool
        createCollections();
        
        toolLists.getChildren().add(collectionTitle);
        toolLists.getChildren().add(new Separator());
        toolLists.getChildren().add(ownedTitle);
        
        VBox toolCreation = toolCreationMenu();
        ScrollPane toolCreationScroll = new ScrollPane();
        toolCreationScroll.setContent(toolCreation);
        
        lendPane = new VBox();
        
        HBox lendLogs = new HBox();
        ScrollPane lendLogScroll = new ScrollPane();
        lendLogScroll.setContent(lendLogs);
        lendLogScroll.resize(800, 200);

        lendLogScroll.setFitToHeight(true);
        lendLogScroll.setFitToWidth(true);
        
        logDateCol = new VBox();
        actionCol = new VBox();
        returnDateCol = new VBox();
        toUserIDCol = new VBox();
        fromUserIDCol = new VBox();
        toolIDCol = new VBox();
        
        lendLogs.getChildren().add(logDateCol);
        lendLogs.getChildren().add(new Separator());
        lendLogs.getChildren().add(actionCol);
        lendLogs.getChildren().add(new Separator());
        lendLogs.getChildren().add(returnDateCol);
        lendLogs.getChildren().add(new Separator());
        lendLogs.getChildren().add(toUserIDCol);
        lendLogs.getChildren().add(new Separator());
        lendLogs.getChildren().add(fromUserIDCol);
        lendLogs.getChildren().add(new Separator());
        lendLogs.getChildren().add(toolIDCol);
        
      //Main pane that holds all the sub panes
        BorderPane pane = new BorderPane();
        pane.setTop(login);
        pane.setCenter(toolListScroll);
        pane.setLeft(toolCreationScroll);
        pane.setRight(lendPane);
        pane.setBottom(lendLogScroll);
        
        Scene scene = new Scene(pane, 1000, 900);

        stage.setTitle("Tool Interface");
        stage.setScene(scene);
        stage.show();
    }
	
	/**
	 * Function to display the logs of a user on the GUI
	 * @param logs A list of logs for the current user logged in or for a specific tool
	 * @param users A HashMap of integers of user ids as keys and strings of users names as values
	 */
	public void refreshLogs(List<LendingLog> logs, HashMap<Integer, String> users) {
		logDateCol.getChildren().clear();
		actionCol.getChildren().clear();
		returnDateCol.getChildren().clear();
		toUserIDCol.getChildren().clear();
		fromUserIDCol.getChildren().clear();
		toolIDCol.getChildren().clear();
		
		logDateCol.getChildren().add(new Label("Log Date"));
		actionCol.getChildren().add(new Label("Action Type"));
		returnDateCol.getChildren().add(new Label("Return Date"));
		toUserIDCol.getChildren().add(new Label("Recieving User"));
		fromUserIDCol.getChildren().add(new Label("Sending User"));
		toolIDCol.getChildren().add(new Label("Tool ID"));
		
		for(LendingLog log: logs) {
			logDateCol.getChildren().add(new Label(log.getLogDate().toString()));
			actionCol.getChildren().add(new Label(log.getAction().getStringName()));
			if (log.getReturnDate() != null) {
				returnDateCol.getChildren().add(new Label(log.getReturnDate().toString()));
			}else {
				returnDateCol.getChildren().add(new Label(" "));
			}
			toUserIDCol.getChildren().add(new Label(users.get(log.getToUserID())));
			fromUserIDCol.getChildren().add(new Label(users.get(log.getFromUserID())));
			toolIDCol.getChildren().add(new Label(log.getToolID()+""));
		}
	}
	
	/**
	 * Creates the "tables" for displaying both the tool collection and owned tools
	 * Called once on initialization
	 * @return The HBox holding those tables to be added to the BorderPane
	 */
	public void createCollections() {
        
		//Creating columns for the collection
        collectionToolName = new VBox();
        collectionPurchaseDate = new VBox();
        collectionReturnButtons = new VBox();
        
        //Creating columns for the owned tools section
        ownedToolName = new VBox();
        ownedPurchaseDate = new VBox();
        ownedLendable = new VBox();
        
        collectionList.getChildren().add(collectionToolName);
        collectionList.getChildren().add(new Separator());
        collectionList.getChildren().add(collectionPurchaseDate);
        collectionList.getChildren().add(new Separator());
        collectionList.getChildren().add(collectionReturnButtons);
        
        
        ownedList.getChildren().add(ownedToolName);
        ownedList.getChildren().add(new Separator());
        ownedList.getChildren().add(ownedPurchaseDate);
        ownedList.getChildren().add(new Separator());
        ownedList.getChildren().add(ownedLendable);
        
	}
	
	/**
	 * Displays a list of tools for the user representing their collection
	 * @param tools List of tools to display
	 */
	public void refreshToolCollection(List<Tool> tools) {
		collectionToolName.getChildren().clear();
		collectionPurchaseDate.getChildren().clear();
		collectionReturnButtons.getChildren().clear();

		collectionToolName.setSpacing(8);
		collectionPurchaseDate.setSpacing(8);
		
        collectionToolName.getChildren().add(new Label("Tool Names"));
        collectionPurchaseDate.getChildren().add(new Label("Purchase Dates"));
        collectionReturnButtons.getChildren().add(new Label("Return back to owner"));
		
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
			Button returnButton = new Button();
			returnButton.setText("Return");
			returnButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					appUser.returnTool(tool);
					
					refreshToolCollection(appUser.getToolCollection());
					refreshToolCollection(appUser.getOwnedTools());
					refreshLogs(appUser.getLendingLogs(), conn.fetchAllUsers());
				}
			});
			
			collectionToolName.getChildren().add(toolName);
			collectionPurchaseDate.getChildren().add(purchaseDate);
			collectionReturnButtons.getChildren().add(returnButton);
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
		
		List<Tool> ownedTools = appUser.toolsICanLend();
        ArrayList<String> toolNames = new ArrayList<>();
        for (Tool tool: ownedTools) {
        	if (tool.isLendable()) {
            	toolNames.add(tool.getToolID()+ " : "+tool.getToolName());
        	}
        }
        
        HashMap<Integer, String> allUsers = conn.fetchAllUsers();
        
        ArrayList<String> usersNames = new ArrayList<>();
        for (Integer userID: allUsers.keySet()) {
        	if(userID != appUser.getUserID()) {
            	usersNames.add(userID+" : "+allUsers.get(userID));
        	}
        }
        
        ComboBox<String> toolsDrop = new ComboBox<String>(FXCollections.observableArrayList(toolNames));
        ComboBox<String> usersDrop = new ComboBox<String>(FXCollections.observableArrayList(usersNames));

        Label toolReturnLabel = new Label("Return Date: ");
        DatePicker toolReturnDate = new DatePicker();

        HBox toolReturn = new HBox();
        toolReturn.getChildren().add(toolReturnLabel);
        toolReturn.getChildren().add(toolReturnDate);
        
        Button submitLendRequest = new Button();
        submitLendRequest.setText("Submit");
        submitLendRequest.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
            	String[] selectedTool = toolsDrop.getValue().split(" ");
            	
            	Tool toolToLend = appUser.getToolFromOwned(Integer.parseInt(selectedTool[0]));
            	
            	String[] selectedUser = usersDrop.getValue().split(" ");
            	
            	User userToLendTo = new User(Integer.parseInt(selectedUser[0]), conn);
            	
            	LocalDate returnDate = toolReturnDate.getValue();
                Date date = Date.valueOf(returnDate);
                
            	appUser.lendTool(toolToLend, userToLendTo, date);
            	refreshToolCollection(appUser.getToolCollection());
            	refreshToolsOwned(appUser.getOwnedTools());
            	refreshLogs(appUser.getLendingLogs(), conn.fetchAllUsers());
            	
            	lendingPaneInit();
            }
        });
        
        lendPane.getChildren().add(toolsDrop);
        lendPane.getChildren().add(usersDrop);
        lendPane.getChildren().add(toolReturn);
        lendPane.getChildren().add(submitLendRequest);
	}
	/**
	 * Function to make the tool creation sidebar, added to the main BorderPane
	 * Called once on initialization
	 * @return A VBox containing everything necessary to make a tool
	 */
	public VBox toolCreationMenu() {
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
                Iterator<Integer> toolTypesI = toolTypes.keySet().iterator();
                Iterator<CheckBox> selectedToolTypesI = selectedToolTypes.iterator();
                ArrayList<Integer> output = new ArrayList<>();
                
                for(int i = 0; i < toolTypes.keySet().size(); i++) {
                	//Checks if the checkbox was selected
                	if (selectedToolTypesI.next().isSelected()) {
                		output.add(toolTypesI.next());
                	}
                }
                
                LocalDate purchDate = toolPurchaseDate.getValue();
                Date date = Date.valueOf(purchDate);
                String toolNameStr = toolNameEntry.getText();
                boolean lendability = lendableToolCheck.isSelected();
                
                Tool newTool = new Tool(toolNameStr, date, lendability, output, conn);
                appUser.addToCollection(newTool);
                appUser.addToOwned(newTool);
                refreshToolCollection(appUser.getToolCollection());
                refreshToolsOwned(appUser.getOwnedTools());
                lendingPaneInit();
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