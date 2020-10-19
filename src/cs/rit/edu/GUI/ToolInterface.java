package cs.rit.edu.GUI;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ToolInterface extends Application{

	
	@Override
	public void start(Stage stage) throws Exception {
		TextField textField = new TextField ();
		Button btn = new Button();
        btn.setText("Login");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println(textField.getText());
            }
        });
        HBox hb = new HBox();
        hb.getChildren().add(textField);
        hb.getChildren().add(btn);

        Scene scene = new Scene(hb, 300, 250);

        stage.setTitle("Tool Interface");
        stage.setScene(scene);
        stage.show();
    }
	
	public static void main(String[] args) {
		launch(args);
	}
}
