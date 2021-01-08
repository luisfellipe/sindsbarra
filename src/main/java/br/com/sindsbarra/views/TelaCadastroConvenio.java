package br.com.sindsbarra.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import br.com.sindsbarra.Main;
import br.com.sindsbarra.controller.CadastroConvenioController;
import br.com.sindsbarra.models.Convenio;

public class TelaCadastroConvenio extends Application {
	private Convenio convenio = null;
	private Parent rootLayout;


	@Override
	public void start(Stage stage) {
		try {
			FXMLLoader loader = new FXMLLoader(new Main().TELA_CADASTRO_CONVENIO);
			rootLayout = (Parent) loader.load();
			if (convenio != null) {
				CadastroConvenioController controller = (CadastroConvenioController) loader.getController();
				controller.setConvenio(convenio);
			}
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.centerOnScreen();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
}