package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TelaPrincipal extends Application {
	
	private String resource = "/fxml/TelaPrincipal.fxml";
	private Parent root;
	@Override
	public void start(Stage stage){
		try {
			root = FXMLLoader.load(getClass().getResource(resource));
			stage.setTitle("SINDSBARRA");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				
				@Override
				public void handle(WindowEvent arg0) {
					Platform.exit();
				}
			});
			stage.show();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void main(String[] args) {
		launch(TelaPrincipal.class, args);
	}

}
