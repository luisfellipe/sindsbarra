package controller;

import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import dao.ConvenioDB;
import dao.ConvenioServidor;
import dao.ServidorDB;
import javafx.collections.FXCollections;
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
import view.TelaConvenio;
import view.TelaVizualizarServidor;

public class TelaPrincipalController implements Initializable { 
	@FXML
	private AnchorPane rootLayout;
	@FXML
	private TableView<Servidor> tabela;

	@FXML
	private TableColumn<Servidor, String> nomeColuna;
	@FXML
	private TableColumn<Servidor, String> cpfColuna;
	@FXML
	private TableColumn<Servidor, String> nascColuna;
	@FXML
	private TableColumn<Servidor, String> admissaoColuna;
	@FXML
	private TableColumn<Servidor, String> matriculaColuna;
	@FXML
	private TableColumn<Servidor, String> funcaoColuna;
	@FXML
	private Button btnPesquisar;
	@FXML
	private TextField tfPesquisar;
	@FXML
	private MenuButton mbConvenios;

	private ObservableList<Servidor> dadosDaTabela = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nomeColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("nome"));
		cpfColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("cpf"));
		admissaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataAdmissao"));
		matriculaColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("matricula"));
		funcaoColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("funcao"));
		nascColuna.setCellValueFactory(new PropertyValueFactory<Servidor, String>("dataNasc"));
		tabela.getItems().addAll(new ServidorDB().getAllServidores());

	}
	/*
	 * sincroniza possiveis alterações do banco de dados com a tabela
	 */
	@FXML
	private void onActionAtualizar() {

	}

	@FXML
	private void onActionProximo() {

	}

	/*
	 * 
	 * 
	 */
	@FXML
	private void onActionAnterior() {

	}

	@FXML
	private void novoServidor() {
		Stage stage = new Stage();
		new TelaCadastroServidor().start(stage); 
	}

	@FXML
	private void fecharTelaPrincipal() {
		System.exit(0);

	}

	@FXML
	private void removerServidor() {
		Servidor s = tabela.getSelectionModel().getSelectedItem();
		int rowsDeleted = new ServidorDB().deleteServidor(s);
		if (rowsDeleted > 0) {
			Alert dialogo = new Alert(AlertType.CONFIRMATION);
			dialogo.setTitle("Removido");
			dialogo.setHeaderText("Servidor Removido!");
			dialogo.show();
		}
		dadosDaTabela.remove(s);

	}

	@FXML
	private void exibirServidor() {
		editarServidor();

	}

	@FXML
	private void pesquisar() {
		String texto[] = tfPesquisar.getText().split(" ");
	}

	@FXML
	private void editarServidor() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		TelaVizualizarServidor tcs = new TelaVizualizarServidor();
		tcs.setServidor(servidor);
		Stage stage = new Stage();
		tcs.start(stage);
	}

	@FXML
	private void exibirConvenios() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		Set<ConvenioServidor> convenios = new ServidorDB().getAllConvenios(servidor);
		TelaConvenio tc = new TelaConvenio();
		tc.setConveniosServidor(convenios);
		Stage stage = new Stage();
		tc.start(stage);
	}

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
