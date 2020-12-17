package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.ConvenioDB;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.Convenio;
import model.Servidor;
import model.ServidorConvenio;

public class CadastroSCController implements Initializable {

	@FXML
	ChoiceBox<String> cbConvenios;

	@FXML
	private Button btnOk;
	private Servidor servidor = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		new ConvenioDB().getAll().forEach(c -> {
			cbConvenios.getItems().add(c.getNome());
		});
		cbConvenios.getItems().add("                                                              ");

	}

	@FXML
	private void onActionOk() {
		String nome = cbConvenios.getSelectionModel().getSelectedItem();
		Convenio convenio = new ConvenioDB().select(nome.trim());
		ServidorConvenio sc = new ServidorConvenio(servidor, convenio);
		new ServidorDB().saveServidorConvenio(sc);

		// fecha tela
		Stage stage = (Stage) btnOk.getScene().getWindow();
		stage.close();
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
}
