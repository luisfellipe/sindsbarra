package br.com.sindsbarra.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.models.ServidorConvenio;
import br.com.sindsbarra.views.TelaCadastroSC;

public class ServidorConvenioController implements Initializable {
	private Servidor servidor = null;

	@FXML
	private TableView<ServidorConvenio> tabela;
	@FXML
	private TableColumn<ServidorConvenio, String> nomeColuna;
	@FXML
	private TableColumn<ServidorConvenio, Double> valorColuna;
	@FXML
	private TableColumn<ServidorConvenio, LocalDate> dataAdesaoColuna;
	@FXML
	private Label lbTotal;
	@FXML
	private Button btnFechar;
	@FXML
	private MenuItem miAddConvenio, miRemoverConvenio, miAtualizar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, String>("nome"));
		valorColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, Double>("valor"));
		dataAdesaoColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, LocalDate>("dataAdesao"));

		/*
		 * Modificando padrao de Data da coluna
		 */
		dataAdesaoColuna.setCellFactory(column -> new TableCell<ServidorConvenio, LocalDate>() {
			DateTimeFormatter formatter = new Data().getDateTimeFormat();

			@Override
			protected void updateItem(LocalDate date, boolean empty) {
				if (empty) {
					setText("");
				} else {
					setText(formatter.format(date));
				}
				super.updateItem(date, empty);
			}
		});
		{// Atalhos de Teclado 
			miAddConvenio.setAccelerator(KeyCombination.keyCombination("Shortcut + N"));
			miRemoverConvenio.setAccelerator(KeyCombination.keyCombination("Shortcut + R"));
			miAtualizar.setAccelerator(KeyCombination.keyCombination("Shortcut + U"));
		}
		update();
	}

	@FXML
	private void onActionRemover() {
		ServidorConvenio c = tabela.getSelectionModel().getSelectedItem();
		if (c != null) {
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("Remover Convenio do Servidor?");
			a.getButtonTypes();
			Optional<ButtonType> result = a.showAndWait();

			if (result.get() == ButtonType.OK) {
				new ServidorDB().deleteConvenio(c.getNome());
				update();
			}
		}
	}

	@FXML
	private void onActionAdicionar() {
		update();
		TelaCadastroSC tcsc = new TelaCadastroSC();
		tcsc.setServidor(servidor);
		Stage stage = new Stage();
		tcsc.start(stage);
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
		update();
	}

	@FXML
	private void update() {
		tabela.getItems().clear();
		double valorTotal = 0.0;
		if (servidor != null) {
			List<ServidorConvenio> lista = new ServidorDB().getAllServidorConvenio(servidor);
			tabela.getItems().addAll(lista);
			for (ServidorConvenio sc : lista) {
				valorTotal += sc.getValor();
			}
			DecimalFormat df = new DecimalFormat("0.00");
			lbTotal.setText("Total: R$ " + df.format(valorTotal));
		}
		tabela.refresh();
	}

	@FXML
	private void fecharTela() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}
}
