package br.com.sindsbarra.views;

import br.com.sindsbarra.Main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TelaPrincipal extends Application {
	
	private Parent root;

	@Override
	public void start(Stage stage) {
		try {
			root = FXMLLoader.load(new Main().TELA_PRINCIPAL);
		
			stage.setTitle("SINDSBARRA - Sindicato dos Servidores Públicos de São José da Barra");
			stage.setResizable(false);
			Scene scene = new Scene(root);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.setOnCloseRequest((we) -> Platform.exit());
			stage.show();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Application .launch(TelaPrincipal.class, args);
	}

}
