package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

import dao.ConvenioServidor;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Servidor;
import view.TelaConvenio;
import model.Data;

public class VisualizarServidorController implements Initializable {
	
	private Servidor servidor = null;
	@FXML
	private AnchorPane rootLayout;

	@FXML
	private Label lbNome;
	@FXML
	private Label lbCpf;
	@FXML
	private Label lbMatricula;
	@FXML
	private Label lbDataAdmissao;
	@FXML
	private Label lbDataNasc;
	@FXML
	private Label lbValor;
	@FXML
	private Label lbFuncao;

	@FXML
	private TextField tfNome;
	@FXML
	private TextField tfCpf;
	@FXML
	private TextField tfMatricula;
	@FXML
	private TextField tfDependentes;
	@FXML
	private TextField tfFuncao;
	@FXML
	private DatePicker dataPickerAdmissao;
	@FXML
	private DatePicker dataPickerNasc;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnConvenios;
	

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// TODO Auto-generated method stub

	}
	
	@FXML 
	private void adicionarServidor() {
		Servidor servidor = new Servidor();
		servidor.setCpf(tfCpf.getText());
		servidor.setFuncao(tfFuncao.getText());		
		servidor.setDataAdmissao(dataPickerAdmissao.getValue());

		servidor.setDataNasc(dataPickerNasc.getValue());
		servidor.setMatricula(tfMatricula.getText());
		servidor.setNome(tfNome.getText());
		new ServidorDB().saveServidor(servidor);
		}
	
	@FXML 
	private void exibirConvenios() {
		if(servidor != null) {
			Set<ConvenioServidor> convenios = new ServidorDB().getAllConvenios(servidor);
			TelaConvenio tc = new TelaConvenio();
			tc.setConveniosServidor(convenios);
			Stage stage = new Stage();
			tc.start(stage);
		}
	}
	/**
	 * Servidor selecionado na tabela principal
	 * @param servidor
	 */
	public void visualizar(Servidor servidor) {
		tfNome.setText(servidor.getNome());
		tfCpf.setText(servidor.getCpf());
		tfFuncao.setText(servidor.getFuncao());
		tfMatricula.setText(servidor.getMatricula());
		Data data = new Data();
		System.out.println(servidor.toString());
		dataPickerAdmissao.setValue(servidor.getDataAdmissao());
		dataPickerNasc.setValue(servidor.getDataNasc());
	}
	
	/*
	 * gera ficha de cadastro
	 */
	@FXML
	private void gerarFicha(){
		
	}

}
