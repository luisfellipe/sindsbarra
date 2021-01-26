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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Stage;

import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;

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
	@FXML
	private RadioButton rbIncluDpdt;

	private boolean update = false;

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		rbIncluDpdt.setSelected(true);
		Tooltip tip = new Tooltip("Inclui dependentes no calculo do valor total do convênio!");
		rbIncluDpdt.setTooltip(tip);
	}

	@FXML
	private void salvarConvenio() {
		if (convenio == null) {
			convenio = new Convenio(null);
		}
		convenio.setDataAdesao(dpDataAderido.getValue());
		convenio.setDescricao(taDescricao.getText());
		convenio.setNome(tfNome.getText());
		convenio.includeDependentes(rbIncluDpdt.isSelected());
		convenio.setValor(Double.parseDouble(tfValor.getText()));

		Alert a = new Alert(AlertType.INFORMATION);
		if (update) {
			if (new ConvenioDB().update(convenio)) {
				a.setHeaderText("Convenio Atualizado!");
				a.show();
			}
		} else {
			if (new ConvenioDB().save(convenio)) {
				a.setHeaderText("Convenio Salvo!");
				a.show();
			}
		}

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
		int len = taDescricao.getText().length() + 1;
		lbContaChar.setText(len + "/150");
		if (len >= 150) {
			lbContaChar.setStyle("-fx-text-fill: rgb(230.0, 60.0, 19.0);");
		} else {
			lbContaChar.setStyle("-fx-text-fill: rgb(0.0, 0.0, 0.0);");
		}
	}

	public void setConvenio(Convenio convenio) {
		update = true;
		this.convenio = convenio;
		dpDataAderido.setValue(LocalDate.now());
		taDescricao.setText(convenio.getDescricao());
		tfNome.setText(convenio.getNome());
		tfValor.setText(convenio.getValor() + "");
		rbIncluDpdt.setSelected(convenio.isDependentesInlude());
		btnSalvar.setText("Atualizar");
	}

	@FXML
	private void fecharTela() {
		Stage stage = (Stage) btnSalvar.getScene().getWindow();
		stage.close();
	}

}
