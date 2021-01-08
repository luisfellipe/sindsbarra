package br.com.sindsbarra.views;

import br.com.sindsbarra.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TelaConvenio extends Application {
	private Parent rootLayout;

	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(new Main().TELA_CONVENIOS);
			rootLayout = (Parent) loader.load();
			Scene scene = new Scene(rootLayout);
			stage.setResizable(false);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
