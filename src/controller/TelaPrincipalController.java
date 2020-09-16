package controller;

import java.net.URL;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import dao.ConvenioDB;
import dao.ServidorDB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Servidor;
import model.Convenio;
import view.TelaCadastroConvenio;
import view.TelaCadastroServidor;
import view.TelaConvenioServidor;
import view.TelaVizualizarServidor;

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
		updateConveniosOptions();

	}

	private void createTableView() {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("nome"));
		cpfColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("cpf"));
		admissaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataAdmissao"));
		matriculaColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("matricula"));
		funcaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("funcao"));
		nascColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataNasc"));

		tabela.getItems().addAll(new ServidorDB().getAllServidores());
		dadosDaTabela = tabela.getItems();
		// ordena por nome
		dadosDaTabela.sort(Comparator.comparing(Servidor::getNome));
	}

	private void updateConveniosOptions() {
		List<Convenio> convenios = new ConvenioDB().getAll();
		if (convenios != null) {
			ObservableList<MenuItem> itens = mbConvenios.getItems();
			MenuItem mi = null;
			for (Convenio c : convenios) {
				mi = new MenuItem();
				mi.setText(c.getNome());
				itens.add(mi);
			}
		}
	}

	/**
	 * cria um novo servidor
	 */
	@FXML
	private void novoServidor() {
		Stage stage = new Stage();
		TelaCadastroServidor tcs = new TelaCadastroServidor();
		tcs.addObservableList(dadosDaTabela);
		tcs.start(stage);
		refreshTable();

	}

	/**
	 * remove servidor selecionado na tabela
	 */
	@FXML
	private void removerServidor() {
		Servidor selectedItem = tabela.getSelectionModel().getSelectedItem();

		int rowsDeleted = new ServidorDB().deleteServidor(selectedItem);
		if (rowsDeleted > 0) {
			Alert dialogo = new Alert(AlertType.CONFIRMATION);
			dialogo.setTitle("Removido");
			dialogo.setHeaderText("Servidor Removido!");
			dialogo.show();
			dadosDaTabela.remove(selectedItem);
			refreshTable();
		} else {
			return;
		}
	}

	// atualiza tabela
	private void refreshTable() {
		tabela.refresh();
	}

	/**
	 * exibi servidor selecionado na tabela
	 */
	@FXML
	private void exibirServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		Stage stage = new Stage();
		TelaVizualizarServidor tvs = new TelaVizualizarServidor();
		tvs.setServidor(servidor);
		tvs.start(stage);
	}

	@FXML
	private void atualizarTabela() {
		tabela.getItems().clear();
		tabela.getItems().addAll(new ServidorDB().getAllServidores());
		dadosDaTabela = tabela.getItems();
		// ordena por nome
		dadosDaTabela.sort(Comparator.comparing(Servidor::getNome));
	}

	@FXML
	private void pesquisar() {

		String pesquisa = tfPesquisar.getText();
		pesquisa = pesquisa.replace(',', ' ');
		String columns[] = pesquisa.split(" ");

		Servidor servidor = null;
		int x = 0;
		for (String column : columns) {
			servidor = new ServidorDB().selectServidor(column);
			if (!dadosDaTabela.contains(servidor)) {
				dadosDaTabela.add(x, servidor);
				refreshTable();
			}
			servidor = null;
		}
	}

	@FXML
	private void editarServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		if (servidor != null) {
			TelaCadastroServidor tcs = new TelaCadastroServidor();
			tcs.setServidor(dadosDaTabela, servidor);
			Stage stage = new Stage();
			tcs.start(stage);
		}
		refreshTable();
	}

	/**
	 * mostra todos os convenios associados ao servidor selecionado na tabela
	 */
	@FXML
	private void exibirConveniosServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		List<Convenio> convenios = new ServidorDB().getAllConvenios(servidor);
		TelaConvenioServidor tcs = new TelaConvenioServidor();
		tcs.setConvenios(convenios);
		Stage stage = new Stage();
		tcs.start(stage);
	}

	/**
	 * Exibi todos os convenios cadastrados
	 */
	@FXML
	private void exibirConvenios() {
		List<Convenio> convenios = new ConvenioDB().getAll();

		for (Convenio c : convenios) {
			String label = c.getNome();
			mbConvenios.getItems().add(new MenuItem(label));
		}
	}

	/**
	 * adiciona um novo convenio
	 */
	@FXML
	private void adicionarConvenio() {
		TelaCadastroConvenio tcc = new TelaCadastroConvenio();
		Stage stageConvenio = new Stage();
		tcc.start(stageConvenio);
	}

	/**
	 * exibe os convenios existentes para o servidor
	 */
	@FXML
	private void conveniosServidor() {
		Servidor s = tabela.getSelectionModel().getSelectedItem();
		List<Convenio> convenios = new ServidorDB().getAllConvenios(s);
		if (convenios != null) {
			TelaConvenioServidor tcs = new TelaConvenioServidor();
			tcs.setConvenios(convenios);
			Stage stage = new Stage();
			tcs.start(stage);
		}
	}

	/**
	 * adiciona um convenio existente à um servidor
	 */
	@FXML
	private void adicionarConvenioServidor() {
		Servidor selectedItem = tabela.getSelectionModel().getSelectedItem();
		TextInputDialog tid = new TextInputDialog();
		tid.setTitle("Adicionar Convenio a Servidor");
	}

	@FXML
	private void exportarDadosCSV() {
		List<Servidor> servidores = new ServidorDB().getAllServidores();
	}

	@FXML
	private void gerarFichaServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();

	}

}
