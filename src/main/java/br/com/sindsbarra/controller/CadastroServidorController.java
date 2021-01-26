package br.com.sindsbarra.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import br.com.sindsbarra.constantes.EstadoCivil;
import br.com.sindsbarra.constantes.Sexo;
import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Endereco;
import br.com.sindsbarra.models.Ficha;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.views.TelaServidorConvenio;

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
	private Servidor servidor = null;
	private boolean update = false;// se ha um funcionario que vai ser atualizado no banco de dados

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {

		btnServidorConvenios.setDisable(true);

		String[] opt1 = { EstadoCivil.SOLT.getType(), EstadoCivil.CASAD.getType(), EstadoCivil.DIV.getType(),
				EstadoCivil.VIUV.getType(), EstadoCivil.NENHUM.getType() };
		cbEstadoCivil.getItems().addAll(opt1);
		String[] opt2 = { Sexo.FEMININO.getType(), Sexo.MASCULINO.getType(), Sexo.NENHUM.getType() };
		cbSexo.getItems().addAll(opt2);

		Tooltip ttCpf = new Tooltip("CPF não pode ser repetido!");
		ttCpf.setFont(new Font(14));
		tfCpf.setTooltip(ttCpf);
		Tooltip ttsc = new Tooltip("Adicione e remova convenios do servidor!");
		ttsc.setFont(new Font(14));
		btnServidorConvenios.setTooltip(ttsc);

	}

	/**
	 * Adiciona servidor no banco de dados
	 */
	@FXML
	private void adicionarServidor() {

		Ficha ficha = new Ficha();
		Endereco endereco = new Endereco();
		Servidor servidor = new Servidor();
		
		Alert alertError = new Alert(AlertType.ERROR);
		alertError.setHeaderText("Falha ao adcionar Servidor");
		
		if (servidor.isCpf(tfCpf.getText()))
			servidor.setCpf(tfCpf.getText());
		else {
			alertError.setContentText("Confira se o campo CPF esta correto!");
			alertError.show();
			return;
		}
		if (!new Data().isNumeric(tfDependentes.getText())) {
			alertError.setContentText("Confira se o campo Dependentes esta correto!");
			alertError.show();
			return;
		} else {
			servidor.setQtdDependentes(Integer.parseInt(tfDependentes.getText()));
		}
		String estadoCivil = cbEstadoCivil.getSelectionModel().getSelectedItem();
		if(estadoCivil == null) {
			alertError.setContentText("Confira se o campo Estado Civil esta correto!");
			alertError.show();
			return;
		}else ficha.setEstadoCivil(estadoCivil);
		String sexo = cbSexo.getSelectionModel().getSelectedItem();
		
		if(sexo == null) {
			alertError.setContentText("Confira se o campo Sexo esta correto!");
			alertError.show();
			return;
		}else ficha.setSexo(sexo);
		

		servidor.setNome(tfNome.getText());
		servidor.setFuncao(tfFuncao.getText());
		servidor.setRG(tfRG.getText());
		servidor.setMatricula(tfMatricula.getText());
		servidor.setDataNasc(dataPickerNasc.getValue());
		servidor.setDataAdmissao(dataPickerAdmissao.getValue());

		ficha.setTelefone(tfTelefone.getText());
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
		
		Alert alertInfo = new Alert(AlertType.INFORMATION);
		if (update) {
			System.out.println("Atualizando Informações do Servidor . . .");
			// atualizar servidor no banco de dados
			if(new ServidorDB().update(servidor)) {
				alertInfo.setHeaderText("Servidor Atualizado");
				alertInfo.show();
				System.out.println("Informações do Servidor Atualizadas !!!");
			}else System.out.println("Erro ao atualizar Informações do Servidor . . .");

		} else {
			System.out.println("Salvando Informações do Servidor . . .");
			if(new ServidorDB().save(servidor)) {// salva servidor no banco de dados
				alertInfo.setHeaderText("Servidor Salvo no Banco de Dados!");
				alertInfo.show();
				System.out.println("Informações do Servidor Salvas . . .");
			}else System.out.println("Erro ao Salvar Informações do Servidor . . .");
		}
		btnServidorConvenios.setDisable(false);
		update = true;
		this.servidor = servidor;
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
		btnServidorConvenios.setDisable(false);
		btnSalvar.setDisable(false);

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
				cbEstadoCivil.getSelectionModel()
						.select(EstadoCivil.NENHUM.getType(fichaServidor.getEstadoCivil()).getType());

				cbSexo.getSelectionModel().select(Sexo.NENHUM.getType(fichaServidor.getSexo()).getType());

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
		Tooltip ttbs = new Tooltip("Atualizar dados do servidor!");
		ttbs.setFont(new Font(14));
		btnSalvar.setTooltip(ttbs);

		// ativa button para visualizar e adicionar convenios
		this.update = true;
	}

	/**
	 * Fecha tela
	 */
	@FXML
	private void onActionFechar() {
		Stage stage = (Stage) btnFechar.getScene().getWindow();
		stage.close();
	}
}
