package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TelaPrincipal extends Application {
	
	private String resource = "/fxml/TelaPrincipal.fxml";
	private Parent root;
	@Override
	public void start(Stage stage){
		try {
			
			root = FXMLLoader.load(getClass().getResource(resource));
	
			stage.setTitle("SINDSBARRA");
			Scene scene = new Scene(root);

			stage.setScene(scene);
			stage.setMaximized(true);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace(); 
		}
	}

	public static void main(String[] args) {
		launch(TelaPrincipal.class, args);
	}

}
