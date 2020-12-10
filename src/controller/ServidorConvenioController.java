package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Convenio;
import model.Servidor;
import model.ServidorConvenio;

public class ServidorConvenioController implements Initializable {
	private Servidor servidor = null;
	private TableView<Convenio> tabela;
	@FXML
	private TableColumn<Convenio, String> nomeColuna;
	@FXML
	private TableColumn<ServidorConvenio, Float> valorColuna;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Convenio, String>("nome"));
		valorColuna.setCellValueFactory(new PropertyValueFactory<ServidorConvenio, Float>("valor"));

		if (servidor != null) {
			List<ServidorConvenio> lista = new ServidorDB().getAllConvenios(servidor);
			lista.forEach(sc -> {
				tabela.getItems().add(sc.getConvenio());
			});
		}
	}

	public void setServidor(Servidor servidor) {
		this.servidor = servidor;

	}

	@FXML
	private void onActionRemover() {
		Convenio c = tabela.getSelectionModel().getSelectedItem();
		tabela.getItems().remove(c);
		new ServidorDB().deleteConvenio(c.getNome());
		tabela.refresh();
	}
	@FXML
	private void onActionNovo() {
		
		
	}
}
