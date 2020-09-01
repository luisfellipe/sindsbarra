package controller;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Servidor;
import model.Convenio;
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
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("nome"));
		cpfColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("cpf"));
		admissaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataAdmissao"));
		matriculaColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("matricula"));
		funcaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("funcao"));
		nascColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataNasc"));
		tabela.getItems().addAll(new ServidorDB().getAllServidores());
		dadosDaTabela = tabela.getItems();

	}

	@FXML
	private void novoServidor() {
		Stage stage = new Stage();
		TelaCadastroServidor tcs = new TelaCadastroServidor();
		tcs.addObservableList(dadosDaTabela);
		tcs.start(stage);

	}

	@FXML
	private void removerServidor() {
		Servidor selectedItem = tabela.getSelectionModel().getSelectedItem();

		int rowsDeleted = new ServidorDB().deleteServidor(selectedItem);
		if (rowsDeleted > 0) {
			Alert dialogo = new Alert(AlertType.CONFIRMATION);
			dialogo.setTitle("Removido");
			dialogo.setHeaderText("Servidor Removido!");
			dialogo.show();
		} else {
			return;
		}
		dadosDaTabela.remove(selectedItem);

	}

	@FXML
	private void exibirServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		Stage stage = new Stage();
		TelaVizualizarServidor tvs = new TelaVizualizarServidor();
		tvs.setServidor(servidor);
		tvs.start(stage);

	}

	@FXML
	private void pesquisar() {
		String texto[] = tfPesquisar.getText().split(" ");
	}

	@FXML
	private void editarServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		if (servidor != null) {
			TelaVizualizarServidor tcs = new TelaVizualizarServidor();
			tcs.setServidor(servidor);
			Stage stage = new Stage();
			tcs.start(stage);
		}
	}
/*
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

	/*
	 * Exibi todos os convenios cadastrados
	 */
	@FXML
	private void exibirConvenio() {
		List<Convenio> convenios = new ConvenioDB().getAll();
		Convenio convenio;
		for (Iterator iterator = convenios.iterator(); iterator.hasNext();) {
			convenio = (Convenio) iterator.next();
			String label = convenio.getNome();
			mbConvenios.getItems().add(new MenuItem(label));
		}
	}

	@FXML
	private void adicionarConvenio() {

	}

	@FXML
	private void removerConvenio() {

	}

	@FXML
	private void adicionarConvenioServidor() {

	}

	@FXML
	private void exportarDadosServidores() {
		List<Servidor> servidores = new ServidorDB().getAllServidores();
	}

	@FXML
	private void gerarFichaServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();

	}

}
