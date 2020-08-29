package view;

import java.util.Set;

import controller.ConvenioController;
import dao.ConvenioServidor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TelaConvenio extends Application {
	private Parent rootLayout;
	FXMLLoader loader = null;
	private String resource = "/fxml/VisualizarConvenios.fxml";

	Set<ConvenioServidor> setConvenios = null;
 
	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			rootLayout = (Parent) loader.load();
			if (setConvenios != null) {
				ConvenioController controller = (ConvenioController) loader.getController();
				controller.visualizar(setConvenios);
			}
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(TelaConvenio.class, args);
	}

	public void setConveniosServidor(Set<ConvenioServidor> convenios) {
		this.setConvenios = convenios;
	}
 
}
