package view;

import controller.CadastroServidorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Servidor;

public class TelaVizualizarServidor extends Application {

	private Servidor servidor = null;
	private String resource = "/fxml/CadastroServidor.fxml";
	private Parent root;
	FXMLLoader loader;
	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			 root = (Parent)loader.load();
			 if(servidor != null) {
	
				CadastroServidorController controller = ( CadastroServidorController)loader.getController();
				controller.visualizar(servidor);
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
	public static void main(String[] args) {
		launch(TelaVizualizarServidor.class, args);
	}
}
