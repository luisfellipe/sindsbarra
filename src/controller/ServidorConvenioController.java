package controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Servidor;
import model.ServidorConvenio;
import view.TelaCadastroSC;

public class ServidorConvenioController implements Initializable {
	private Servidor servidor = null;

	@FXML
	private TableView<ServidorConvenio> tabela;
	@FXML
	private TableColumn<ServidorConvenio, String> nomeColuna;
	@FXML
	private TableColumn<ServidorConvenio, Double> valorColuna;
	@FXML
	private Label lbTotal;
	@FXML
	private Button btnFechar;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, String>("nome"));
		valorColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, Double>("valor"));
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
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
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
