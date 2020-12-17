package controller;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import dao.ServidorDB;
import model.Servidor;
import pdf.DeclaracaoFile;
import view.TelaCadastroConvenio;
import view.TelaCadastroServidor;
import view.TelaConvenio;

public class TelaPrincipalController implements Initializable {
	@FXML
	private AnchorPane rootLayout;
	@FXML
	private TableView<Servidor> tabela;

	@FXML
	private TableColumn<Servidor, String> nomeColuna, cpfColuna, nascColuna, admissaoColuna, matriculaColuna,
			funcaoColuna;
	@FXML
	private Button btnPesquisar;
	@FXML
	private TextField tfPesquisar;
	@FXML
	private MenuButton mbConvenios;

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

		//adiciona os servidores do banco de dados
		tabela.getItems().addAll(new ServidorDB().selectAll());
		dadosDaTabela = tabela.getItems();
		// ordena por nome
		dadosDaTabela.sort(Comparator.comparing(Servidor::getNome));
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
		new ServidorDB().delete(tabela.getSelectionModel().getSelectedItem().getCpf());
		atualizarTabela();
	}

	@FXML
	private void visualizarServidor() {
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
	private void exportarDadosCSV() {
	}

	@FXML
	private void gerarFichaServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		String RESULT = "file.pdf";
		// TODO criar um filechooice que seleciona o local para salvar a declaração

		DeclaracaoFile dcf = new DeclaracaoFile(servidor, RESULT);
		dcf.declaracao();
	}

	@FXML
	private void imprimirRelacao() {

	}
}
