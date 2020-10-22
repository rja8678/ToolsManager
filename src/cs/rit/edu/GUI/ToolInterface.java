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
    	conn.closeConn();
    }
    
	@Override
	public void start(Stage stage) throws Exception {
		TextField username = new TextField();
		username.setPromptText("Username");

		Button loginBtn = new Button();
        loginBtn.setText("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                String usernameInput = username.getText();
                
                System.out.println(Integer.parseInt(usernameInput));

                appUser = new User(Integer.parseInt(usernameInput), conn);

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

        //Collection in the center
        HBox toolLists = new HBox();
        
        collectionList = new HBox();
        ownedList = new HBox();
        
        createCollections();
        
        toolLists.getChildren().add(collectionList);
        toolLists.getChildren().add(new Separator());
        toolLists.getChildren().add(ownedList);
        
        VBox toolCreation = toolCreationMenu();
        
        lendPane = new VBox();
        Label lendPaneLabel = new Label("Lending");
        lendPane.getChildren().add(lendPaneLabel);
        
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
	
	public void createCollections() {
        
		//Creating columns for the collection
        collectionToolName = new VBox();
        collectionToolName.getChildren().add(new Label("Tool Names"));
        collectionPurchaseDate = new VBox();
        collectionPurchaseDate.getChildren().add(new Label("Purchase Dates"));
        
        //Creating columns for the owned tools section
        ownedToolName = new VBox();
        ownedToolName.getChildren().add(new Label("Tool Names"));
        ownedPurchaseDate = new VBox();
        ownedPurchaseDate.getChildren().add(new Label("Purchase Dates"));
        ownedLendable = new VBox();
        ownedLendable.getChildren().add(new Label("Lendability"));
        
        collectionList.getChildren().add(collectionToolName);
        collectionList.getChildren().add(new Separator());
        collectionList.getChildren().add(collectionPurchaseDate);
        
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
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
			
			collectionToolName.getChildren().add(toolName);
			collectionPurchaseDate.getChildren().add(purchaseDate);
		}
	}
	
	public void lendingPaneInit() {
		LinkedList<Tool> ownedTools = appUser.getOwnedTools();
        ArrayList<String> toolNames = new ArrayList<>();
        for (Tool tool: ownedTools) {
        	toolNames.add(tool.getToolName());
        }
        
        HashMap<Integer, String> allUsers = conn.fetchAllUsers();
        
        ArrayList<String> usersNames = new ArrayList<>();
        for (Integer userID: allUsers.keySet()) {
        	usersNames.add(allUsers.get(userID)+" "+userID);
        }
        
        ComboBox<String> toolsDrop = new ComboBox<String>(FXCollections.observableArrayList(toolNames));
        ComboBox<String> usersDrop = new ComboBox<String>(FXCollections.observableArrayList(usersNames));
        
        lendPane.getChildren().add(toolsDrop);
        lendPane.getChildren().add(usersDrop);
	}
	
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
        
        
        //Tool Types Checkboxes
        HBox listOfTypes = new HBox();
        VBox toolTypeName = new VBox();
        VBox checkBoxes = new VBox();
        
        listOfTypes.getChildren().add(toolTypeName);
        listOfTypes.getChildren().add(checkBoxes);
        
        HashMap<Integer, String> toolTypes = conn.fetchAllToolTypes();
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
        createTool.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Create tool");
                //TODO Change
                Iterator<String> toolTypesI = toolTypes.values().iterator();
                Iterator<CheckBox> selectedToolTypesI = selectedToolTypes.iterator();
                LinkedList<String> output = new LinkedList<>();
                while(toolTypesI.hasNext()) {
                	if (selectedToolTypesI.next().isSelected()) {
                		output.add(toolTypesI.next());
                	}
                }
                
                //TODO Create the tool on the database
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
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
			Label lendable = new Label((tool.isLendable() ? "True": "False"));

			ownedToolName.getChildren().add(toolName);
			ownedPurchaseDate.getChildren().add(purchaseDate);
			ownedLendable.getChildren().add(lendable);
		}
	}
	
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
