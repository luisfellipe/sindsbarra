package br.com.sindsbarra.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.models.Convenio;

public class CadastroConvenioController implements Initializable {
	private Convenio convenio = null;
	@FXML
	private TextArea taDescricao;
	@FXML
	private TextField tfNome;
	@FXML
	private TextField tfValor;
	@FXML
	private DatePicker dpDataAderido;
	@FXML
	private Label lbContaChar;
	@FXML
	private Button btnSalvar;

	@Override
	public void initialize(URL url, ResourceBundle resource) {

	}

	@FXML
	private void salvarConvenio() {
		Convenio c = new Convenio();
		c.setDataAdesao(dpDataAderido.getValue());
		c.setDescricao(taDescricao.getText());
		c.setNome(tfNome.getText());
		c.setValor(Double.parseDouble(tfValor.getText()));
		if(convenio == null) new ConvenioDB().save(c);
		else new ConvenioDB().update(c);
		
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setHeaderText("Convenio Salvo!");
		a.show();
	}

	@FXML
	private void limparCampos() {
		dpDataAderido.setValue(LocalDate.now());
		taDescricao.setText("");
		tfNome.setText("");
		tfValor.setText("");
	}

	@FXML
	private void contaLetras() {
		lbContaChar.setText((taDescricao.getLength() + 1) + "/150");
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
		dpDataAderido.setValue(LocalDate.now());
		taDescricao.setText(convenio.getDescricao());
		tfNome.setText(convenio.getNome());
		tfValor.setText(convenio.getValor() + "");
		btnSalvar.setText("Atualizar");
	}
	@FXML
	private void fecharTela() {
		Stage stage = (Stage) btnSalvar.getScene().getWindow();
		stage.close();
	}

}
