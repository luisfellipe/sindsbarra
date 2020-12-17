package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, String>("nome"));
		valorColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, Double>("valor"));
		update();
	}

	@FXML
	private void onActionRemover() {
		ServidorConvenio c = tabela.getSelectionModel().getSelectedItem();
		new ServidorDB().deleteConvenio(c.getNome());
		update();

	}

	@FXML
	private void onActionAdicionar() {
		TelaCadastroSC tccs = new TelaCadastroSC();
		tccs.setServidor(servidor);
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		tccs.start(stage);
		update();
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
		update();
	}

	private void update() {
		tabela.getItems().clear();
		double valorTotal = 0.0;
		if (servidor != null) {
			List<ServidorConvenio> lista = new ServidorDB().getAllServidorConvenio(servidor);
			tabela.getItems().addAll(lista);
			for (ServidorConvenio sc : lista) {
				valorTotal += sc.getValor();
			}
			lbTotal.setText("Total: R$ " + valorTotal);
		}
		tabela.refresh();
	}
}
