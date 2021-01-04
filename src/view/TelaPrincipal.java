package view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TelaPrincipal extends Application {

	private String resource = "/fxml/TelaPrincipal.fxml";
	private Parent root;

	@Override
	public void start(Stage stage) {
		try {
			root = FXMLLoader.load(getClass().getResource(resource));
			stage.setTitle("SINDSBARRA");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.setOnCloseRequest((we) -> Platform.exit());
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		args = "--module-path /home/luis/Programas/javafx-sdk-13.0.2/lib --add-modules javafx.controls,javafx.fxml".split(" ");
		launch(TelaPrincipal.class, args);
	}

}
