package controller;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;
import dao.ConvenioServidor;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;                       
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Servidor;
import view.TelaConvenio;
import model.Endereco;
import model.Ficha;

public class CadastroServidorController implements Initializable {

	private Servidor servidor = null;
	@FXML
	private AnchorPane rootLayout;

	
	@FXML
	private TextField tfNome;
	@FXML
	private TextField tfCpf;
	@FXML
	private TextField tfRG;
	@FXML
	private TextField tfMatricula;
	@FXML
	private TextField tfDependentes;
	@FXML
	private TextField tfFuncao;
	@FXML
	private TextField tfTelefone;
	@FXML
	private TextField tfNomeMae;
	@FXML
	private TextField tfNomePai;
	@FXML
	private TextField tfNaturalidade;
	@FXML
	private TextField tfCidade;
	@FXML
	private TextField tfBairro;
	@FXML
	private TextField tfRua;
	@FXML
	private TextField tfCep;
	@FXML
	private TextField tfNumero;
	@FXML
	private TextField tfEstado;
	
	@FXML
	private DatePicker dataPickerAdmissao;
	@FXML
	private DatePicker dataPickerNasc;
	@FXML
	private ChoiceBox<String> cbEstadoCivil;
	@FXML
	private ChoiceBox<String> cbSexo;
	@FXML
	private Button btnSalvar;
	@FXML
	private Button btnConvenios;
	@FXML
	private Button btnFechar;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cbEstadoCivil.getItems().addAll("Casado(a)", "Solteiro(a)", "Divorciado(a)");
		cbSexo.getItems().addAll("Masculino", "Feminino");

	}
	/*
	 * Adiciona servidor no banco de dados
	 */
	@FXML
	private void adicionarServidor() {
		Servidor servidor = new Servidor();
		Ficha fi = new Ficha();
		servidor.setNome(tfNome.getText());
		servidor.setFuncao(tfFuncao.getText());
		servidor.setCpf(tfCpf.getText());
		servidor.setRG(tfRG.getText());
		servidor.setMatricula(tfMatricula.getText());
		servidor.setQtdDependentes(Integer.parseInt(tfDependentes.getText()));
		servidor.setDataNasc(dataPickerNasc.getValue());
		servidor.setDataAdmissao(dataPickerAdmissao.getValue());
		
		fi.setTelefone(tfTelefone.getText());
		fi.setEstadoCivil(cbEstadoCivil.getSelectionModel().getSelectedItem());
		fi.setSexo(cbSexo.getSelectionModel().getSelectedItem());
		fi.setNaturalidade(tfNaturalidade.getText());
		fi.setNomePai(tfNomePai.getText());
		fi.setNomeMae(tfNomeMae.getText());
		
		//Endereco
		Endereco endereco = new Endereco();
		endereco.setRua(tfRua.getText());
		endereco.setNumero(Integer.parseInt(tfNumero.getText()));
		endereco.setBairro(tfBairro.getText());
		endereco.setCep(tfCep.getText());
		endereco.setCidade(tfCidade.getText());
		endereco.setEstado(tfEstado.getText());
		
		fi.setEndereco(endereco);
		servidor.setFicha(fi);

		//salva servidor no banco de dados
		new ServidorDB().saveServidor(servidor);
		
	}
	
	@FXML
	private void adicionarConvenio() {
		
	}

	@FXML
	private void exibirConvenios() {
		if (servidor != null) {
			Set<ConvenioServidor> convenios = new ServidorDB().getAllConvenios(servidor);
			TelaConvenio tc = new TelaConvenio();
			tc.setConveniosServidor(convenios);
			Stage stage = new Stage();
			tc.start(stage);
		}
	}

	/**
	 * Servidor selecionado na tabela principal
	 * 
	 * @param s
	 */
	public void visualizar(Servidor s) {
		tfNome.setText(s.getNome());
		tfCpf.setText(s.getCpf());
		tfFuncao.setText(s.getFuncao());
		tfMatricula.setText(s.getMatricula());
		tfDependentes.setText(s.getQtdDependentes()+"");
		tfRG.setText(s.getRg());
				
		Ficha f = s.getFicha();
		tfNaturalidade.setText(f.getNaturalidade());
		tfNomeMae.setText(f.getNomeMae());
		tfNomePai.setText(f.getNomePai());
		tfTelefone.setText(f.getTelefone());
		
		cbEstadoCivil.getItems().add(f.getEstadoCivil());
		cbSexo.getItems().add(f.getSexo());
		
		
		Endereco e = s.getFicha().getEndereco();
		tfCep.setText(e.getCep());
		tfCidade.setText(e.getCidade());
		tfBairro.setText(e.getBairro());
		tfEstado.setText(e.getEstado());
		tfRua.setText(e.getRua());
		tfNumero.setText(e.getNumero()+"");
		

		//System.out.println(servidor.toString());
		dataPickerAdmissao.setValue(s.getDataAdmissao());
		dataPickerNasc.setValue(s.getDataNasc());
	}
	public void onActionFechar() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
		
	}

}
