package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import dao.ConvenioDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Convenio;

public class CadastroConvenioController implements Initializable {

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
		new ConvenioDB().save(c);
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

}
