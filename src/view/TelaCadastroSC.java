package view;

import controller.CadastroSCController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Servidor;

public class TelaCadastroSC extends Application {
	private Parent rootLayout;
	FXMLLoader loader = null;
	private String resource = "/fxml/CadastroSC.fxml";
	private Servidor servidor = null;

	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			rootLayout = (Parent) loader.load();
			if (servidor != null) {
				CadastroSCController controller = (CadastroSCController) loader.getController();
				controller.setServidor(servidor);
			}
			Scene scene = new Scene(rootLayout);
			stage.setScene(scene);
			stage.setResizable(false);;
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
	
	public static void main(String[] args) {
		launch(TelaCadastroSC.class, args);
	}
}
