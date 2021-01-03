package view;

import controller.CadastroConvenioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Convenio;

public class TelaCadastroConvenio extends Application {
	private Convenio convenio = null;
	private Parent rootLayout;
	FXMLLoader loader = null;
	private String resource = "/fxml/CadastroConvenio.fxml";

	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			rootLayout = (Parent) loader.load();
			if (convenio != null) {
				CadastroConvenioController controller = (CadastroConvenioController) loader.getController();
				controller.setConvenio(convenio);
			}
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
}