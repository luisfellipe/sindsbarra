package controller;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import dao.ConvenioDB;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import model.Convenio;
import model.Servidor;

public class ServidorConvenioController implements Initializable {

	private Servidor servidor = null;
	private List<String> listaConvenios = null;
	@FXML
	private ChoiceBox<String> cbConvenios;
	@FXML
	private Label lbCpf, lbNome;

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		listaConvenios = new ConvenioDB().getAll().stream().map(Convenio::getNome).collect(Collectors.toList());
		Collections.sort(listaConvenios);
		cbConvenios.getItems().addAll(listaConvenios);
	}

	@FXML
	private void adicionarConvenio() {
		Convenio c = new ConvenioDB().select(cbConvenios.getValue());
		new ServidorDB().saveConvenio(c, servidor);
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("Convenio adicionado ao servidor!");
		a.show();

	}

}
