package cs.rit.edu.GUI;

import cs.rit.edu.DBConn;
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
	VBox collectionList;
	
	@Override
	public void start(Stage stage) throws Exception {
		TextField username = new TextField();
		username.setPromptText("Username");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");
		Button loginBtn = new Button();
        loginBtn.setText("Login");
        loginBtn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                String usernameInput = username.getText();
                String passwordInput = password.getText();

                if(usernameInput.isBlank() || passwordInput.isBlank()){
                    System.out.println("Please enter valid credentials!");
                }else{
                    DBConn newConnection = new DBConn(usernameInput, passwordInput);
                    if(newConnection.connected()){
                        loginBtn.setText("Login Successful!");
                        loginBtn.setDisable(true);

                    }
                }

                System.out.println(username.getText());

            }
        });

        //Login at top of the screen
        VBox usernameAndPassword = new VBox();
        Label usernameLab = new Label("Username: ");
        Label passwordLab = new Label("Password: ");
        usernameAndPassword.getChildren().add(usernameLab);
        usernameAndPassword.getChildren().add(username);
        usernameAndPassword.getChildren().add(passwordLab);
        usernameAndPassword.getChildren().add(password);

        HBox login = new HBox();
        login.setAlignment(Pos.CENTER_LEFT);
        login.getChildren().add(usernameAndPassword);
        login.getChildren().add(loginBtn);

        //Collection in the center
        collectionList = new VBox();
        
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
        
        toolCreation.getChildren().add(toolName);
        toolCreation.getChildren().add(toolPurchase);
        
        
        BorderPane pane = new BorderPane();
        pane.setTop(login);
        pane.setCenter(collectionList);
        pane.setLeft(toolCreation);
        

        Scene scene = new Scene(pane, 300, 250);

        stage.setTitle("Tool Interface");
        stage.setScene(scene);
        stage.show();
    }
	
	//TODO Run this once user is logged in or the collection for the user has changed
	public void refreshToolCollection() {
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
