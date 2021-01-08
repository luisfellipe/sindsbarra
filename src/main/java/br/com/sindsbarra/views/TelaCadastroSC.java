package br.com.sindsbarra.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import br.com.sindsbarra.Main;
import br.com.sindsbarra.controller.CadastroSCController;
import br.com.sindsbarra.models.Servidor;

public class TelaCadastroSC extends Application {
	private Parent rootLayout;
	FXMLLoader loader = null;
	private Servidor servidor = null;

	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(new Main().TELA_CADASTRO_SC);
			rootLayout = (Parent) loader.load();
			if (servidor != null) {
				CadastroSCController controller = (CadastroSCController) loader.getController();
				controller.setServidor(servidor);
			}
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);;
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
}
