package view;

import controller.CadastroServidorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Servidor;

//--module-path /home/Programas/javafx-sdk-13.0.2/lib --add-modules javafx.controls,javafx.fxml

public class TelaCadastroServidor extends Application {

	private String resource = "/fxml/CadastroServidor.fxml";
	private Servidor servidor = null;
	private Parent root;
	FXMLLoader loader;

	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			root = (Parent) loader.load();

			if (servidor != null) {
				CadastroServidorController controller = (CadastroServidorController) loader.getController();
				controller.setServidor(servidor);

			}
			stage.setTitle("SINDISBARRA");
			stage.setScene(new Scene(root));
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(TelaCadastroServidor.class, args);
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
}
