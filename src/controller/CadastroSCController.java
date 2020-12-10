package controller;

import java.net.URL;
import java.util.ResourceBundle;

import dao.ConvenioDB;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import model.Convenio;
import model.Servidor;
import model.ServidorConvenio;

public class CadastroSCController implements Initializable {

	@FXML
	ChoiceBox<String> cbConvenios;
	private Servidor servidor = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		new ConvenioDB().getAll().forEach(c -> {
			cbConvenios.getItems().add(c.getNome());
		});
	}

	@FXML
	private void onActionOk() {
		String nome = cbConvenios.getSelectionModel().getSelectedItem();
		Convenio convenio = new ConvenioDB().select(nome);
		ServidorConvenio sc = new ServidorConvenio(servidor, convenio);
		new ServidorDB().saveServidorConvenio(sc);

	}

	public void setServidor(Servidor s) {
		this.servidor = s;
	}
}
