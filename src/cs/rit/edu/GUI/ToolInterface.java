package cs.rit.edu.GUI;

import java.util.List;

import ObjectClasses.Tool;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolInterface extends Application{
	VBox collectionList;
	
	@Override
	public void start(Stage stage) throws Exception {
		TextField username = new TextField();
		Button btn = new Button();
        btn.setText("Login");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println(username.getText());
                //TODO Log in user
            }
        });
        //Login at top of the screen
        HBox hb = new HBox();
        hb.getChildren().add(username);
        hb.getChildren().add(btn);

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
        pane.setTop(hb);
        pane.setCenter(collectionList);
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
			Label lendable = new Label((tool.isLendable() ? "True": "False"));
			
			HBox toolEntry = new HBox();
			toolEntry.getChildren().add(toolName);
			toolEntry.getChildren().add(purchaseDate);
			toolEntry.getChildren().add(lendable);
			
			collectionList.getChildren().add(toolEntry);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
