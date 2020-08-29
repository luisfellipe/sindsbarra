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
		fi.setNomePai(tfNomePai.getText());
		fi.setNomeMae(tfNomeMae.getText());

		// Endereco
		Endereco endereco = new Endereco();
		endereco.setCidadeNatal(tfNaturalidade.getText());
		endereco.setRua(tfRua.getText());
		endereco.setNumero(Integer.parseInt(tfNumero.getText()));
		endereco.setBairro(tfBairro.getText());
		endereco.setCep(tfCep.getText());
		endereco.setCidadeAtual(tfCidade.getText());
		endereco.setEstado(tfEstado.getText());

		fi.setEndereco(endereco);
		servidor.setFicha(fi);

		// salva servidor no banco de dados
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
	 * @param servidor
	 */
	public void visualizar(Servidor servidor) {
		{
			if (servidor.getNome() != null)
				tfNome.setText(servidor.getNome());
			if (servidor.getCpf() != null)
				tfCpf.setText(servidor.getCpf());
			if (servidor.getFuncao() != null)
				tfFuncao.setText(servidor.getFuncao());
			if (servidor.getMatricula() != null)
				tfMatricula.setText(servidor.getMatricula());
			if (servidor.getQtdDependentes() != null)
				tfDependentes.setText(servidor.getQtdDependentes() + "");
			if (servidor.getRg() != null)
				tfRG.setText(servidor.getRg());
			if (servidor.getDataAdmissao() != null)
				dataPickerAdmissao.setValue(servidor.getDataAdmissao());
			if (servidor.getDataNasc() != null)
				dataPickerNasc.setValue(servidor.getDataNasc());

			Ficha fichaServidor = servidor.getFicha();

			if (fichaServidor != null) {
				if (fichaServidor.getNomeMae() != null)
					tfNomeMae.setText(fichaServidor.getNomeMae());
				if (fichaServidor.getNomePai() != null)
					tfNomePai.setText(fichaServidor.getNomePai());
				if (fichaServidor.getEstadoCivil() != null)
					cbEstadoCivil.getItems().add(fichaServidor.getEstadoCivil());
				if (fichaServidor.getSexo() != null)
					cbSexo.getItems().add(fichaServidor.getSexo());

				Endereco enderecoServidor = servidor.getFicha().getEndereco();

				if (enderecoServidor != null) {
					if (enderecoServidor.getCidadeNatal() != null)
						tfNaturalidade.setText(enderecoServidor.getCidadeNatal());
					if (enderecoServidor.getCep() != null)
						tfCep.setText(enderecoServidor.getCep());
					if (enderecoServidor.getCidadeAtual() != null)
						tfCidade.setText(enderecoServidor.getCidadeAtual());
					if (enderecoServidor.getBairro() != null)
						tfBairro.setText(enderecoServidor.getBairro());
					if (enderecoServidor.getEstado() != null)
						tfEstado.setText(enderecoServidor.getEstado());
					if (enderecoServidor.getRua() != null)
						tfRua.setText(enderecoServidor.getRua());
					if (enderecoServidor.getNumero() != null)
						tfNumero.setText(enderecoServidor.getNumero() + "");
				}
			}

		}
		// System.out.println(servidor.toString());
	}

	public void onActionFechar() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();

	}

}
