package controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import dao.ConvenioDB;
import dao.ModelCSV;
import dao.ServidorDB;
import model.Servidor;
import pdf.DeclaracaoFile;
import pdf.ServidorFile;
import view.TelaCadastroConvenio;
import view.TelaCadastroServidor;
import view.TelaConvenio;
import view.TelaServidorConvenio;

public class TelaPrincipalController implements Initializable {
	@FXML
	private AnchorPane rootLayout;
	@FXML
	private TableView<Servidor> tabela;

	@FXML
	private TableColumn<Servidor, String> nomeColuna, cpfColuna, nascColuna, admissaoColuna, matriculaColuna,
			funcaoColuna;
	@FXML
	private MenuItem miNovoUsuario, miExibirServidor, miExibirServConv, miRemoverServ, miGerarFicha, miExibirConv,
			miNovoConvenio, miUpdateTable, miImprimirRelacao;

	private ObservableList<Servidor> dadosDaTabela = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		createTableView();
	}

	private void createTableView() {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("nome"));
		cpfColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("cpf"));
		admissaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataAdmissao"));
		matriculaColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("matricula"));
		funcaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("funcao"));
		nascColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataNasc"));

		// adiciona os servidores do banco de dados
		tabela.getItems().addAll(new ServidorDB().selectAll());
		dadosDaTabela = tabela.getItems();
		// ordena por nome
		dadosDaTabela.sort(Comparator.comparing(Servidor::getNome));
		Scene scene = rootLayout.getScene();
		miNovoUsuario.setAccelerator(KeyCombination.keyCombination("Shortcut + N"));
		miExibirServidor.setAccelerator(KeyCombination.keyCombination("Shortcut + V"));
		miExibirServConv.setAccelerator(KeyCombination.keyCombination("Shortcut + C"));
		miRemoverServ.setAccelerator(KeyCombination.keyCombination("Shortcut + R"));
		miGerarFicha.setAccelerator(KeyCombination.keyCombination("Shortcut + G"));
		miExibirConv.setAccelerator(KeyCombination.keyCombination("alt + C"));
		miNovoConvenio.setAccelerator(KeyCombination.keyCombination("alt + N"));
		miUpdateTable.setAccelerator(KeyCombination.keyCombination("shift + U"));
		miImprimirRelacao.setAccelerator(KeyCombination.keyCombination("Shortcut + I"));
	}

	/**
	 * cria um novo servidor
	 */
	@FXML
	private void novoServidor() {
		Stage stage = new Stage();
		TelaCadastroServidor tcs = new TelaCadastroServidor();
		tcs.start(stage);
	}

	/**
	 * remove servidor selecionado na tabela
	 */
	@FXML
	private void removerServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		if (servidor != null) {
			Alert a = new Alert(AlertType.CONFIRMATION);
			a.setHeaderText("Remover Servidor?");
			Optional<ButtonType> result = a.showAndWait();
			if (result.get() == ButtonType.OK) {
				new ServidorDB().delete(servidor.getCpf());
				atualizarTabela();
			}
		}
	}

	@FXML
	private void exibirServidor() {
		String cpf = tabela.getSelectionModel().getSelectedItem().getCpf();
		Servidor servidor = new ServidorDB().select(cpf);
		if (servidor != null) {
			TelaCadastroServidor tcs = new TelaCadastroServidor();
			tcs.setServidor(servidor);
			Stage stage = new Stage();
			stage.centerOnScreen();
			stage.initModality(Modality.APPLICATION_MODAL);
			tcs.start(stage);
		}
	}

	@FXML
	private void exibirServidorConvenios() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		TelaServidorConvenio tsc = new TelaServidorConvenio();
		tsc.setServidor(servidor);
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		tsc.start(stage);
	}

	@FXML
	private void atualizarTabela() {
		tabela.getItems().clear();
		List<Servidor> servidores = new ServidorDB().selectAll();
		servidores.sort(Comparator.comparing(Servidor::getNome));
		tabela.getItems().addAll(servidores);
		tabela.refresh();
	}

	/**
	 * Exibi todos os convenios cadastrados
	 */
	@FXML
	private void exibirConvenios() {
		TelaConvenio tv = new TelaConvenio();
		Stage stage = new Stage();
		tv.start(stage);
	}

	/**
	 * adiciona um novo convenio
	 */
	@FXML
	private void adicionarConvenio() {
		TelaCadastroConvenio tcc = new TelaCadastroConvenio();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		tcc.start(stage);
	}

	@FXML
	private void gerarFichaServidor() {
		String cpf = tabela.getSelectionModel().getSelectedItem().getCpf();

		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = dirChooser.showDialog(stage);

		Servidor servidor = new ServidorDB().select(cpf);
		String RESULT = file.getAbsolutePath() + "/Ficha de Filiação-" + cpf + ".pdf";
		file.mkdirs();
		DeclaracaoFile dcf = new DeclaracaoFile(servidor, RESULT);
		dcf.declaracao();
	}

	@FXML
	private void imprimirRelacao() {
		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = dirChooser.showDialog(stage);

		List<Servidor> servidores = new ServidorDB().selectAll();
		String RESULT = file.getAbsolutePath() + "/Relação de Servidores Atualizados-" + LocalDate.now().getDayOfMonth()
				+ LocalDate.now().getMonthValue() + LocalDate.now().getYear() + ".pdf";
		file.mkdirs();
		ServidorFile sf = new ServidorFile(RESULT);
		sf.addServidor(servidores);

	}

	@FXML
	private void exportarConveniosCSV() {
		new ModelCSV().exportConvenios(new ConvenioDB().getAll());
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados exportados!");
		a.setContentText("Convenios exportados para arquivo csv!");
		a.show();
	}

	@FXML
	private void exportarServidoresCSV() {
		new ModelCSV().exportServidores(new ServidorDB().selectAll());
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados exportados!");
		a.setContentText("Servidores exportados para arquivo csv!");
		a.show();
	}
	@FXML
	private void onActionFechar() {
		Stage stage = (Stage) tabela.getScene().getWindow();
		stage.close();
	}
}
