package br.com.sindsbarra.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import br.com.sindsbarra.Main;
import br.com.sindsbarra.controller.CadastroServidorController;
import br.com.sindsbarra.models.Servidor;



public class TelaCadastroServidor extends Application {

	private Servidor servidor = null;
	private Parent root;

	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(new Main().TELA_CADASTRO_SERVIDOR);
			root = (Parent) loader.load();

			if (servidor != null) {
				CadastroServidorController controller = (CadastroServidorController) loader.getController();
				controller.setServidor(servidor);

			}
			stage.setTitle("SINDISBARRA");
			stage.setScene(new Scene(root));
			stage.centerOnScreen();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
}
