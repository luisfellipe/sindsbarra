package br.com.sindsbarra.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.models.ServidorConvenio;

public class CadastroSCController implements Initializable {

	@FXML
	ChoiceBox<String> cbConvenios;
	@FXML
	private DatePicker dpDataAdesao;
	@FXML
	private Button btnOk;
	private Servidor servidor = null;
	private List<Convenio> convenios = null;
	private Map<String, Long> mapaConveios = null;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		btnOk.setDisable(true);
		/*
		 * Preenche o CooiceBox com os convenios disponiveis
		 */
		convenios = new ConvenioDB().getAll();
		convenios.sort(Comparator.comparing(Convenio::getNome));

		mapaConveios = new HashMap<String, Long>(convenios.size());
		convenios.forEach(c -> {
			cbConvenios.getItems().add(c.getNome());
			mapaConveios.put(c.getNome(), c.getCodigo());
		});

		dpDataAdesao.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter formatter = new Data().getDateTimeFormat();

			@Override
			public String toString(LocalDate date) {
				return (date != null) ? formatter.format(date) : "";
			}

			@Override
			public LocalDate fromString(String string) {
				return (string != null && !string.isEmpty()) ? LocalDate.parse(string, formatter) : null;
			}
		});
		/*
		 * Desativa o button de salvar btnOk ate que uma data seja selecionada
		 */
		dpDataAdesao.setOnAction(event -> {
			if (dpDataAdesao.getValue() != null) {
				btnOk.setDisable(false);
			} else
				btnOk.setDisable(true);
		});
	}

	@FXML
	private void onActionOk() {
		Alert a = new Alert(AlertType.INFORMATION);
		String nome = cbConvenios.getSelectionModel().getSelectedItem();
		if (nome != null) {
			if (!nome.isBlank()) {
				Convenio convenio = new ConvenioDB().select(mapaConveios.get(nome.trim()));
				ServidorConvenio sc = new ServidorConvenio(convenio.getNome(), convenio.getCodigo(), servidor.getCpf(),
						dpDataAdesao.getValue(), null);
				if (new ServidorDB().saveServidorConvenio(sc)) {
					a.setHeaderText("Convenio Salvo!!");
					a.show();
				}
			}
		} else {
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
