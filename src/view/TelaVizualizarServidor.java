package view;

import controller.VisualizarServidorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Servidor;

public class TelaVizualizarServidor extends Application {

	private Servidor servidor = null;
	private String resource = "/fxml/VisualizarServidor.fxml";
	private Parent root;
	FXMLLoader loader;

	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			root = (Parent) loader.load();
			if (servidor != null) {

				VisualizarServidorController controller = (VisualizarServidorController) loader.getController();
				controller.exibirServidor(servidor);
			}

			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
}
