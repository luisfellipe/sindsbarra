package br.com.sindsbarra.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.ServidorConvenio;
import br.com.sindsbarra.pdf.ConvenioFile;
import br.com.sindsbarra.views.TelaCadastroConvenio;

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
	private MenuItem miNovoConvenio, miExibirConvenio, miRemoverConvenio, miUpdateTabela, miImprimirRelacao;
	@FXML
	private Button btnFechar;
	
	
	private List<Convenio> convenios = null;

	@Override
	public void initialize(URL location, ResourceBundle resource) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Convenio, String>("nome"));
		valorColuna.setCellValueFactory(new PropertyValueFactory<Convenio, Float>("valor"));
		dataAdesaoColuna.setCellValueFactory(new PropertyValueFactory<Convenio, LocalDate>("dataAdesao"));
		
		miNovoConvenio.setAccelerator(KeyCombination.keyCombination("Shortcut + N"));
		miExibirConvenio.setAccelerator(KeyCombination.keyCombination("Shortcut + E"));
		miRemoverConvenio.setAccelerator(KeyCombination.keyCombination("Shortcut + R"));
		miUpdateTabela.setAccelerator(KeyCombination.keyCombination("Shortcut + U"));
		miImprimirRelacao.setAccelerator(KeyCombination.keyCombination("Shortcut + I"));

		convenios = new ConvenioDB().getAll();
		tabela.getItems().addAll(convenios);
	}

	@FXML
	private void fecharTela() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}

	@FXML
	private void imprimirLista() {
		Convenio c = tabela.getSelectionModel().getSelectedItem();
		List<ServidorConvenio> sclista = new ServidorDB().getAllServidorConvenio(c);

		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = dirChooser.showDialog(stage);

		String RESULT = file.getAbsolutePath() + "/[" + c.getNome() + "]" + LocalDate.now().getDayOfMonth()
				+ LocalDate.now().getMonthValue() + LocalDate.now().getYear() + ".pdf";
		file.mkdirs();

		ConvenioFile cf = new ConvenioFile(RESULT);
		cf.addConvenioServidor(sclista);
	}

	@FXML
	private void exibirConvenio() {
		Convenio c = tabela.getSelectionModel().getSelectedItem();
		TelaCadastroConvenio tcc = new TelaCadastroConvenio();
		tcc.setConvenio(c);
		Stage stage = new Stage();
		tcc.start(stage);
	}

	@FXML
	private void novoConvenio() {
		TelaCadastroConvenio tcc = new TelaCadastroConvenio();
		Stage stage = new Stage();
		tcc.start(stage);
		update();
	}

	@FXML
	private void removerConvenio() {
		Convenio convenio = tabela.getSelectionModel().getSelectedItem();
		if (convenio != null) {
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("Remover Convênio?");
			Optional<ButtonType> result = a.showAndWait();
			if (result.get() == ButtonType.OK) {
				new ConvenioDB().delete(convenio);
				update();
			}
		}

	}

	@FXML
	private void update() {
		tabela.getItems().clear();
		convenios.clear();
		convenios.addAll(new ConvenioDB().getAll());
		tabela.getItems().addAll(convenios);
		tabela.refresh();
	}
}