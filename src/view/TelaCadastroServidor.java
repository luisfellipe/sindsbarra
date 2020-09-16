package view;

import controller.CadastroServidorController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Servidor;

//--module-path /home/Programas/javafx-sdk-13.0.2/lib --add-modules javafx.controls,javafx.fxml


public class TelaCadastroServidor extends Application {

	private String resource = "/fxml/CadastroServidor.fxml";
	private ObservableList<Servidor> dadosDaTabela = null; 
	private Servidor servidor = null;
	private Parent root;
	FXMLLoader loader;
	@Override
	public void start(Stage stage) {
		try {
			loader = new FXMLLoader(getClass().getResource(resource));
			 root = (Parent)loader.load();
			 
			 if(dadosDaTabela != null) {
				 CadastroServidorController controller = (CadastroServidorController) loader.getController();
				 if(servidor != null) {
					 controller.setServidor(dadosDaTabela, servidor);
				 }else {
					 controller.addObservableList(dadosDaTabela);
				 }
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
	public void addObservableList(ObservableList<Servidor> dadosDaTabela) {
		this.dadosDaTabela = dadosDaTabela;
	}
	public void setServidor(ObservableList<Servidor> dadosDaTabela, Servidor servidor) {
		this.dadosDaTabela = dadosDaTabela;
		this.servidor = servidor;
	}
}
