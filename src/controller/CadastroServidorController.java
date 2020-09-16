package controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import dao.ServidorDB;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Servidor;
import view.TelaConvenioServidor;
import view.TelaServidorConvenio;
import model.Convenio;
import model.Endereco;
import model.Ficha;

public class CadastroServidorController implements Initializable {

	@FXML
	private AnchorPane rootLayout;
	@FXML
	private Label lbSexo, lbDataAdmissao, lbNome, lbFuncao, lbTelefone, lbCpf, lbRG, lbMatricula, lbEstadoCivil,
			lbDataNasc, lbDependentes, lbNomeMae, lbNomePai, lbCidadeNatal, lbCidadeAtual, lbBairro, lbRua, lbCep,
			lbNumeroRua, lbEstado;
	@FXML
	private TextField tfNome, tfCpf, tfRG, tfMatricula, tfDependentes, tfFuncao, tfTelefone, tfNomeMae, tfNomePai,
			tfCidadeNatal, tfCidadeAtual, tfBairro, tfRua, tfCep, tfNumero, tfEstado;
	@FXML
	private DatePicker dataPickerAdmissao, dataPickerNasc;
	@FXML
	private ChoiceBox<String> cbEstadoCivil, cbSexo;
	@FXML
	private Button btnFechar;
	@FXML
	private Button btnSalvar;
	private ObservableList<Servidor> dadosDaTabela = null;
	private Servidor servidor = null;
	private boolean update = false;// se ha um funcionario que vai ser atualizado no banco de dados

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cbEstadoCivil.getItems().add("Solteiro(a)");
		cbEstadoCivil.getItems().add( "Casado(a)");
		cbEstadoCivil.getItems().add( "Divorciado(a)");
		cbEstadoCivil.getItems().add( "Viuvo(a)");
		
		cbSexo.getItems().add("Masculino");
		cbSexo.getItems().add("Feminino");
	}

	/*
	 * Adiciona servidor no banco de dados
	 */
	@SuppressWarnings("unchecked")
	@FXML
	private void adicionarServidor() {
		
		Ficha ficha = null;
		Endereco endereco = null;
		if (servidor == null) {
			servidor = new Servidor();
			ficha = new Ficha();
			endereco = new Endereco();
		} else {
			ficha = servidor.getFicha();
			if(ficha != null) {
				endereco = ficha.getEndereco();
			}
		}

		servidor.setNome(tfNome.getText());
		servidor.setFuncao(tfFuncao.getText());
		servidor.setCpf(tfCpf.getText());
		servidor.setRG(tfRG.getText());
		servidor.setMatricula(tfMatricula.getText());
		servidor.setQtdDependentes(Integer.parseInt("0" + tfDependentes.getText()));
		servidor.setDataNasc(dataPickerNasc.getValue());
		servidor.setDataAdmissao(dataPickerAdmissao.getValue());

		ficha.setTelefone(tfTelefone.getText());
		ficha.setEstadoCivil(cbEstadoCivil.getSelectionModel().getSelectedItem());
		ficha.setSexo(cbSexo.getSelectionModel().getSelectedItem());
		ficha.setNomePai(tfNomePai.getText());
		ficha.setNomeMae(tfNomeMae.getText());

		endereco.setCidadeNatal(tfCidadeNatal.getText());
		endereco.setRua(tfRua.getText());
		endereco.setNumero(Integer.parseInt("0" + tfNumero.getText()));
		endereco.setBairro(tfBairro.getText());
		endereco.setCep(tfCep.getText());
		endereco.setCidadeAtual(tfCidadeAtual.getText());
		endereco.setEstado(tfEstado.getText());

		ficha.setEndereco(endereco);
		servidor.setFicha(ficha);

		/*
		 * Atualiza ou adiciona um novo servidor
		 */
		if (update) {
			new ServidorDB().updateServidor(servidor);
		} else {
			new ServidorDB().saveServidor(servidor);// salva servidor no banco de dados
		}
		if (dadosDaTabela != null) {
			dadosDaTabela.add(servidor);
		}
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setContentText("Servidor adicionado!!");

	}

	@FXML
	private void adicionarConvenio() {
		TelaServidorConvenio tsc = new TelaServidorConvenio();
	}

	@FXML
	private void exibirConvenios() {
		if (servidor != null) {
			List<Convenio> convenios = new ServidorDB().getAllConvenios(servidor);
			TelaConvenioServidor tcs = new TelaConvenioServidor();
			tcs.setConvenios(convenios);
			Stage stage = new Stage();
			tcs.start(stage);
		}
	}

	/**
	 * Servidor selecionado na tabela principal
	 * 
	 * @param servidor
	 */
	public void setServidor(ObservableList<Servidor> dadosDaTabela, Servidor servidor) {
		this.servidor = servidor;
		this.dadosDaTabela = dadosDaTabela;
		dadosDaTabela.remove(servidor);

		{
			//if (servidor.getNome() != null)
				tfNome.setText(servidor.getNome());
			//if (servidor.getCpf() != null)
				tfCpf.setText(servidor.getCpf());
			//if (servidor.getFuncao() != null)
				tfFuncao.setText(servidor.getFuncao());
			//if (servidor.getMatricula() != null)
				tfMatricula.setText(servidor.getMatricula());
			//if (servidor.getQtdDependentes() != null)
				tfDependentes.setText(servidor.getQtdDependentes() + "");
			//if (servidor.getRg() != null)
				tfRG.setText(servidor.getRg());
			//if (servidor.getDataAdmissao() != null)
				dataPickerAdmissao.setValue(servidor.getDataAdmissao());
			//if (servidor.getDataNasc() != null)
				dataPickerNasc.setValue(servidor.getDataNasc());

			Ficha fichaServidor = servidor.getFicha();

			if (fichaServidor != null) {
				//if (fichaServidor.getNomeMae() != null)
					tfNomeMae.setText(fichaServidor.getNomeMae());
				//if (fichaServidor.getNomePai() != null)
					tfNomePai.setText(fichaServidor.getNomePai());
			//	if (fichaServidor.getEstadoCivil() != null)
					cbEstadoCivil.getItems().add(fichaServidor.getEstadoCivil());
			//	if (fichaServidor.getSexo() != null)
					cbSexo.getItems().add(fichaServidor.getSexo());

				Endereco enderecoServidor = servidor.getFicha().getEndereco();

				if (enderecoServidor != null) {
				//	if (enderecoServidor.getCidadeNatal() != null)
						tfCidadeNatal.setText(enderecoServidor.getCidadeNatal());
					//if (enderecoServidor.getCep() != null)
						tfCep.setText(enderecoServidor.getCep());
					//if (enderecoServidor.getCidadeAtual() != null)
						tfCidadeAtual.setText(enderecoServidor.getCidadeAtual());
					//if (enderecoServidor.getBairro() != null)
						tfBairro.setText(enderecoServidor.getBairro());
					//if (enderecoServidor.getEstado() != null)
						tfEstado.setText(enderecoServidor.getEstado());
					//if (enderecoServidor.getRua() != null)
						tfRua.setText(enderecoServidor.getRua());
					//if (enderecoServidor.getNumero() != null)
						tfNumero.setText(enderecoServidor.getNumero() + "");
				}
			}

		}
		btnSalvar.setText("Atualizar");// altera titulo do button Salvar -> Atualizar
		this.update = true;
		// System.out.println(servidor.toString());
	}

	public void onActionFechar() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();

	}

	public void addObservableList(ObservableList<Servidor> dadosDaTabela) {
		this.dadosDaTabela = dadosDaTabela;
	}

}
