package cs.rit.edu.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ToolInterface extends Application{
	VBox collectionList;
	
	@Override
	public void start(Stage stage) throws Exception {
		TextField textField = new TextField ();
		Button btn = new Button();
        btn.setText("Login");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println(textField.getText());
                //TODO Log in user
            }
        });
        //Login at top of the screen
        HBox hb = new HBox();
        hb.getChildren().add(textField);
        hb.getChildren().add(btn);

        //Collection in the center
        collectionList = new VBox();
        
        VBox toolCreation = new VBox();
//        toolCreation
        
        BorderPane pane = new BorderPane();
        pane.setTop(hb);
        pane.setCenter(collectionList);
        

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
