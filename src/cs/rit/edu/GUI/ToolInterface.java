package cs.rit.edu.GUI;

import cs.rit.edu.DBConn;
import java.util.List;

import DBContollerPackage.DBTool;
import DBContollerPackage.DBUser;
import ObjectClasses.Tool;
import ObjectClasses.User;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolInterface extends Application{
	VBox collectionList, ownedList;

    static User appUser = null ;
    static DBConn conn = null ;
	
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
        
        collectionList = new VBox();
        ownedList = new VBox();
        
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
        
        Button createTool = new Button();
        createTool.setText("Create Tool");
        createTool.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                System.out.println("Create tool");
                //TODO Create new tool here and update database
            }
        });


        HBox toolPurchase = new HBox();
        toolPurchase.getChildren().add(toolPurchaseLabel);
        toolPurchase.getChildren().add(toolPurchaseDate);
        
        toolCreation.getChildren().add(toolName);
        toolCreation.getChildren().add(toolPurchase);
        
        
        BorderPane pane = new BorderPane();
        pane.setTop(login);
        pane.setCenter(toolLists);
        pane.setLeft(toolCreation);
        

        Scene scene = new Scene(pane, 300, 250);

        stage.setTitle("Tool Interface");
        stage.setScene(scene);
        stage.show();
    }
	
	//TODO Run this once user is logged in or the collection for the user has changed
	/**
	 * Displays a list of tools for the user representing their collection
	 * @param tools List of tools to display
	 */
	public void refreshToolCollection(List<Tool> tools) {
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
//			Label lendable = new Label((tool.isLendable() ? "True": "False"));

			HBox toolEntry = new HBox();
			toolEntry.getChildren().add(toolName);
			toolEntry.getChildren().add(purchaseDate);
//			toolEntry.getChildren().add(lendable);

			collectionList.getChildren().add(toolEntry);
		}
	}
	
	//TODO Run this once user is logged in or the collection for the user has changed
	/**
	 * Displays a list of tools for the user representing their owned tools
	 * @param tools List of tools to display
	 */
	public void refreshToolsOwned(List<Tool> tools) {
		for (Tool tool: tools) {
			Label toolName = new Label(tool.getToolName());
			Label purchaseDate = new Label(tool.getPurchaseDate().toString());
			Label lendable = new Label((tool.isLendable() ? "True": "False"));

			HBox toolEntry = new HBox();
			toolEntry.getChildren().add(toolName);
			toolEntry.getChildren().add(purchaseDate);
			toolEntry.getChildren().add(lendable);

			ownedList.getChildren().add(toolEntry);
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
