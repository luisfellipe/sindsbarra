package view;

import java.util.List;

import controller.ConvenioController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Convenio;

public class TelaConvenioServidor extends Application {
	private Parent rootLayout;
	FXMLLoader loader = null;
	private String resource = "/fxml/VisualizarConvenios.fxml";

	List<Convenio> convenios = null;
 
	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			rootLayout = (Parent) loader.load();
			if (convenios != null) {
				ConvenioController controller = (ConvenioController) loader.getController();
				controller.visualizar(convenios);
			}
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(TelaConvenioServidor.class, args);
	}

	public void setConvenios(List<Convenio> convenios) {
		this.convenios = convenios;
	}
 
}
