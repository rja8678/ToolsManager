package cs.rit.edu.GUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import ObjectClasses.Tool;
import ObjectClasses.User;
import cs.rit.edu.DBConn;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolInterface extends Application{

    static User appUser = null ;
    static DBConn conn = null ;
    
    VBox collectionToolName, collectionPurchaseDate, ownedToolName, ownedPurchaseDate, ownedLendable;
	
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
                //TODO change this
//                String passwordInput = "fukee8lohx3ahga2Sahx";
//
//                if(usernameInput.isBlank() || passwordInput.isBlank()){
//                    System.out.println("Please enter valid credentials!");
//                }else{
//                    DBConn newConnection = new DBConn(usernameInput, passwordInput);
//                    if(newConnection.connected()){
//                        loginBtn.setText("Login Successful!");
//                        loginBtn.setDisable(true);
//
//                    }
//                }

                System.out.println(username.getText());

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
        
        
        HBox collectionList = new HBox();
        HBox ownedList = new HBox();
        
        //Creating columns for the collection
        collectionToolName = new VBox();
        collectionPurchaseDate = new VBox();
        
        collectionList.getChildren().add(collectionToolName);
        collectionList.getChildren().add(collectionPurchaseDate);
        
        //Creating columns for the owned tools section
        ownedToolName = new VBox();
        ownedPurchaseDate = new VBox();
        ownedLendable = new VBox();
        
        ownedList.getChildren().add(ownedToolName);
        ownedList.getChildren().add(ownedPurchaseDate);
        ownedList.getChildren().add(ownedLendable);
        
        toolLists.getChildren().add(collectionList);
        toolLists.getChildren().add(ownedList);
        
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
        
        //Tool Types Checkboxes
        HBox listOfTypes = new HBox();
        VBox toolTypeName = new VBox();
        VBox checkBoxes = new VBox();
        
        listOfTypes.getChildren().add(toolTypeName);
        listOfTypes.getChildren().add(checkBoxes);
        
        List<String> toolTypes = conn.fetchAllToolTypes();
        List<CheckBox> selectedToolTypes = new ArrayList<>();
        
        for(String toolType: toolTypes) {
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
                Iterator<String> toolTypesI = toolTypes.iterator();
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
        toolCreation.getChildren().add(createTool);
        
        
        BorderPane pane = new BorderPane();
        pane.setTop(login);
        pane.setCenter(toolLists);
        pane.setLeft(toolCreation);
        

        Scene scene = new Scene(pane, 300, 250);

        stage.setTitle("Tool Interface");
        stage.setScene(scene);
        stage.show();
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
