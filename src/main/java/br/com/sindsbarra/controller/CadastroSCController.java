package br.com.sindsbarra.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.models.ServidorConvenio;

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
	}

	@FXML
	private void onActionOk() {
		String nome = cbConvenios.getSelectionModel().getSelectedItem();
		if(!nome.isBlank() && nome !=null) {
			Convenio convenio = new ConvenioDB().select(nome.trim());
			ServidorConvenio sc = new ServidorConvenio(servidor, convenio);
			new ServidorDB().saveServidorConvenio(sc);
		}else {
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Escolha um convenio!!");
			a.setContentText("Um convenio deve estar selecionado para completar a ação!");
			a.show();
		}

	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
	}
	
	@FXML
	private void fecharTela() {
		Stage stage = (Stage) btnOk.getScene().getWindow();
		stage.close();
	}
}
