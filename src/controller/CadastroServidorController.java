package controller;

import java.net.URL;
import java.util.ResourceBundle;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Servidor;
import view.TelaServidorConvenio;
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
	private Button btnFechar, btnSalvar, btnServidorConvenios;
	private Servidor servidor;
	private boolean update;// se ha um funcionario que vai ser atualizado no banco de dados

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		servidor = null;
		update = false;
		String[] opt1 = { "Solteiro(a)", "Casado(a)", "Divorciado(a)", "Viuvo(a)" };
		cbEstadoCivil.getItems().addAll(opt1);
		String[] opt2 = { "Masculino", "Feminino" };
		cbSexo.getItems().addAll(opt2);

	}

	/**
	 * Adiciona servidor no banco de dados
	 */
	@FXML
	private void adicionarServidor() {
		if (update) {
			System.out.println("Atualizando");
		}
		Ficha ficha = new Ficha();
		Endereco endereco = new Endereco();
		Servidor servidor = new Servidor();

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
			new ServidorDB().update(servidor);
		} else {
			new ServidorDB().save(servidor);// salva servidor no banco de dados
		}
	}

	@FXML
	private void visualizarServidorConvenios() {
		TelaServidorConvenio tsc = new TelaServidorConvenio();
		tsc.setServidor(servidor);
		Stage stage = new Stage();
		tsc.start(stage);
	}
	/**
	 * Servidor selecionado na tabela principal
	 * 
	 * @param servidor
	 */
	public void setServidor(Servidor servidor) {
		this.servidor = servidor;
		if (servidor != null) {
			tfNome.setText(servidor.getNome());
			tfCpf.setText(servidor.getCpf());
			tfFuncao.setText(servidor.getFuncao());
			tfMatricula.setText(servidor.getMatricula());
			tfDependentes.setText(servidor.getQtdDependentes() + "");
			tfRG.setText(servidor.getRg());
			dataPickerAdmissao.setValue(servidor.getDataAdmissao());
			dataPickerNasc.setValue(servidor.getDataNasc());

			Ficha fichaServidor = servidor.getFicha();

			if (fichaServidor != null) {
				tfNomeMae.setText(fichaServidor.getNomeMae());
				tfNomePai.setText(fichaServidor.getNomePai());
				
				if (fichaServidor.getEstadoCivil() != null) {
					for (String item : cbEstadoCivil.getItems()) {
						if (item.contains(fichaServidor.getEstadoCivil())) {
							cbEstadoCivil.getSelectionModel().select(item);
						}
					}
				}
			
				if (fichaServidor.getSexo() != null) {
					if (fichaServidor.getSexo().contains("Masculino"))
						cbSexo.getSelectionModel().select("Masculino");
					else
						cbSexo.getSelectionModel().select("Feminino");
				}

				Endereco enderecoServidor = servidor.getFicha().getEndereco();

				if (enderecoServidor != null) {
					tfCidadeNatal.setText(enderecoServidor.getCidadeNatal());
					tfCep.setText(enderecoServidor.getCep());
					tfCidadeAtual.setText(enderecoServidor.getCidadeAtual());
					tfBairro.setText(enderecoServidor.getBairro());
					tfEstado.setText(enderecoServidor.getEstado());
					tfRua.setText(enderecoServidor.getRua());
					tfNumero.setText(enderecoServidor.getNumero() + "");
				}
			}

		}
		btnSalvar.setText("Atualizar");// altera titulo do button Salvar -> Atualizar
		this.update = true;
	}

	public void onActionFechar() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}
}
