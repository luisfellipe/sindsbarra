package br.com.sindsbarra.controller;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.dao.ModelCSV;
import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.pdf.DeclaracaoFile;
import br.com.sindsbarra.pdf.ServidorFile;

import br.com.sindsbarra.views.TelaCadastroConvenio;
import br.com.sindsbarra.views.TelaCadastroServidor;
import br.com.sindsbarra.views.TelaConvenio;
import br.com.sindsbarra.views.TelaServidorConvenio;

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
	@FXML
	private Label lbQtdServidores, lbQtdConvenios;

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

		atualizarTabela();

		// adiciona atalhos
		miNovoUsuario.setAccelerator(KeyCombination.keyCombination("Shortcut + N"));
		miExibirServidor.setAccelerator(KeyCombination.keyCombination("Shortcut + E"));
		miExibirServConv.setAccelerator(KeyCombination.keyCombination("Shortcut + C"));
		miRemoverServ.setAccelerator(KeyCombination.keyCombination("Shortcut + R"));
		miGerarFicha.setAccelerator(KeyCombination.keyCombination("Shortcut + G"));
		miExibirConv.setAccelerator(KeyCombination.keyCombination("alt + C"));
		miNovoConvenio.setAccelerator(KeyCombination.keyCombination("alt + N"));
		miImprimirRelacao.setAccelerator(KeyCombination.keyCombination("Shortcut + I"));
		miUpdateTable.setAccelerator(KeyCombination.keyCombination("Shortcut + U"));
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
			tcs.start(stage);
		}
	}

	@FXML
	private void exibirServidorConvenios() {
		Servidor servidor = tabela.getSelectionModel().getSelectedItem();
		TelaServidorConvenio tsc = new TelaServidorConvenio();
		tsc.setServidor(servidor);
		Stage stage = new Stage();
		tsc.start(stage);
	}

	@FXML
	private void atualizarTabela() {
		System.out.println("atualizando tabela . . .");
		tabela.getItems().clear();
		List<Servidor> servidores = new ServidorDB().selectAll();
		servidores.sort(Comparator.comparing(Servidor::getNome));
		tabela.getItems().addAll(servidores);
		tabela.refresh();
		lbQtdConvenios.setText("" + new ConvenioDB().getAll().size());
		lbQtdServidores.setText("" + servidores.size());
		System.out.println("pronto!!!");
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
		tcc.start(stage);
	}

	@FXML
	private void gerarFichaServidor() {
		
		System.out.println("gerando ficha do servidor . . .");
		String cpf = tabela.getSelectionModel().getSelectedItem().getCpf();

		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = dirChooser.showDialog(stage);

		Servidor servidor = new ServidorDB().select(cpf);
		String RESULT = file.getAbsolutePath() + "/Ficha de Filiação-" + cpf + ".pdf";
		file.mkdirs();
		DeclaracaoFile dcf = new DeclaracaoFile(servidor, RESULT);
		dcf.declaracao();
		System.out.println("pronto!!!");
	}

	@FXML
	private void imprimirRelacao() {
		System.out.println("imprimindo relação de servidores . . .");
		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = dirChooser.showDialog(stage);

		List<Servidor> servidores = new ServidorDB().selectAll();
		String RESULT = file.getAbsolutePath() + "/Relação de Servidores Atualizados-" + LocalDate.now().getDayOfMonth()
				+ LocalDate.now().getMonthValue() + LocalDate.now().getYear() + ".pdf";
		file.mkdirs();
		ServidorFile sf = new ServidorFile(RESULT);
		sf.addServidor(servidores);
		System.out.println("pronto !!!");

	}

	@FXML
	private void exportarConveniosCSV() {
		System.out.println("exportando convênios  para arquivo csv. . . ");
		new ModelCSV().exportConvenios(new ConvenioDB().getAll());
		System.out.println("Convenios exportados !!! ");
		
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados exportados!");
		a.setContentText("Convenios exportados para arquivo csv!");
		a.show();
	}

	@FXML
	private void importarConveniosCSV() {
		System.out.println("importando convênios  de arquivo csv. . . ");
		new ConvenioDB().saveAll(new ModelCSV().importConvenios());
		System.out.println("convênios importados!!! ");
		
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados importados!");
		a.setContentText("Convenios importados do arquivo csv!");
		a.show();
	}

	@FXML
	private void exportarServidoresCSV() {
		System.out.println("exportando servidores para arquivo csv . . . ");
		new ModelCSV().exportServidores(new ServidorDB().selectAll());
		System.out.println("servidores exportados !!!");
	
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados exportados!");
		a.setContentText("Servidores exportados para arquivo csv!");
		a.show();
	}

	@FXML
	private void importarServidoresCSV() {
		System.out.println("importando servidores de arquivo csv . . . ");
		new ServidorDB().saveAll((new ModelCSV().importServidores()));
		System.out.println("servidores importados !!!");
	
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados importados!");
		a.setContentText("Servidores importados do arquivo csv!");
		a.show();
	}
	@FXML
	private void exportarServidorConvenioCSV() {
		System.out.println("exportando convenio dos servidores para arquivo csv . . . ");
		new ModelCSV().exportServidorConvenio(new ServidorDB().getAllServidorConvenio());
		System.out.println("convenios exportados para arquivo csv . . . ");
	
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados exportados!");
		a.setContentText("Convenios existentes dos Servidores foram exportados para arquivo csv!");
		a.show();
	}
	@FXML
	private void importarServidorConvenioCSV() {
		System.out.println("importando convenio dos servidores de arquivo csv . . . ");
		new ServidorDB().saveAllServidorConvenio((new ModelCSV().importservidorConvenio()));
		System.out.println("convenios importados !!!");
		
		Alert a = new Alert(AlertType.INFORMATION);
		a.setHeaderText("Dados importados!");
		a.setContentText("Convenios existentes dos Servidores foram importados do arquivo csv!");
		a.show();
	}
	

	@FXML
	private void fecharAplicacao() {
		System.out.println("Encerrando aplicação . . . ");
		for (int i = 0; i < 10000; i++);
		System.out.println("Até logo . . . ");
		Platform.exit();
	}
}
