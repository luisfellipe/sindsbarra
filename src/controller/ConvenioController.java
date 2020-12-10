package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import dao.ConvenioDB;
import dao.ServidorDB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Convenio;
import model.Servidor;
import view.TelaCadastroConvenio;

public class ConvenioController implements Initializable {

	@FXML
	private Parent rootLayout;
	@FXML
	private TableView<Convenio> tabela;
	@FXML
	private TableColumn<Convenio, String> nomeColuna;
	@FXML
	private TableColumn<Convenio, Float> valorColuna;
	@FXML
	private TableColumn<Convenio, LocalDate> dataAdesaoColuna;
	@FXML
	private MenuItem miAddConvenio;
	@FXML
	private MenuItem miRemoverConvenio;
	private List<Convenio> convenios = null;

	@Override
	public void initialize(URL location, ResourceBundle resource) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Convenio, String>("nome"));
		valorColuna.setCellValueFactory(new PropertyValueFactory<Convenio, Float>("valor"));
		dataAdesaoColuna.setCellValueFactory(new PropertyValueFactory<Convenio, LocalDate>("dataAdesao"));

		convenios = new ConvenioDB().getAll();
		tabela.getItems().addAll(convenios);
	}

	@FXML
	private void onActionNovo() {
		Stage stage = new Stage();
		new TelaCadastroConvenio().start(stage);
		tabela.refresh();
	}

	@FXML
	private void onActionRemover() {
		Convenio c = tabela.getSelectionModel().getSelectedItem();
		new ConvenioDB().delete(c);
	}
}
