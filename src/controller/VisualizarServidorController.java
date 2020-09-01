package controller;

import java.net.URL;
import java.util.ResourceBundle;
import dao.ServidorDB;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.util.List;
import model.Convenio;
import model.Data;
import model.Endereco;
import model.Ficha;
import model.Servidor;

public class VisualizarServidorController implements Initializable {
	@FXML
	private AnchorPane rootLayout;
	@FXML
	private Label lbSexo, lbDataAdmissao, lbNome, lbFuncao, lbTelefone, lbCpf, lbRG, lbMatricula, lbEstadoCivil,
			lbDataNasc, lbDependentes, lbNomeMae, lbNomePai, lbCidadeNatal, lbEndereco;

	@FXML
	private Button btnFechar, btnConvenios;
	
	private Servidor servidor = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		// TODO Auto-generated method stub

	}

	public void exibirServidor(Servidor servidor) {
		this.servidor = servidor;
		{
			if (servidor.getNome() != null)
				lbNome.setText(servidor.getNome());
			if (servidor.getCpf() != null)
				lbCpf.setText(servidor.getCpf());
			if (servidor.getFuncao() != null)
				lbFuncao.setText(servidor.getFuncao());
			if (servidor.getMatricula() != null)
				lbMatricula.setText(servidor.getMatricula());
			if (servidor.getQtdDependentes() != null)
				lbDependentes.setText(servidor.getQtdDependentes() + "");
			if (servidor.getRg() != null)
				lbRG.setText(servidor.getRg());
			
			Data d = new Data();
			if (servidor.getDataAdmissao() != null)
				
				lbDataAdmissao.setText(d.getStringDate(servidor.getDataAdmissao()));
			if (servidor.getDataNasc() != null)
				lbDataNasc.setText(d.getStringDate(servidor.getDataNasc()));
				d = null;

			Ficha fichaServidor = servidor.getFicha();

			if (fichaServidor != null) {
				if (fichaServidor.getNomeMae() != null)
					lbNomeMae.setText(fichaServidor.getNomeMae());
				if (fichaServidor.getNomePai() != null)
					lbNomePai.setText(fichaServidor.getNomePai());
				if (fichaServidor.getEstadoCivil() != null)
					lbEstadoCivil.setText(fichaServidor.getEstadoCivil());
				if (fichaServidor.getSexo() != null)
					lbSexo.setText(fichaServidor.getSexo());

				Endereco enderecoServidor = servidor.getFicha().getEndereco();
				StringBuilder sb = null;
				if (enderecoServidor != null) {
					sb = new StringBuilder();
					if (enderecoServidor.getCidadeNatal() != null)
						lbCidadeNatal.setText(enderecoServidor.getCidadeNatal());
					
					if (enderecoServidor.getRua() != null)
						sb.append("Rua ").append(enderecoServidor.getRua()).append(" Nº ");
					if (enderecoServidor.getNumero() != null)
						sb.append(enderecoServidor.getNumero()).append(", ");
					if (enderecoServidor.getBairro() != null)
						sb.append("bairro ").append(enderecoServidor.getBairro()).append(", ");
					
					if (enderecoServidor.getCidadeAtual() != null)
						sb.append(enderecoServidor.getCidadeAtual()).append(" - ");
					if (enderecoServidor.getEstado() != null)
						sb.append(enderecoServidor.getEstado()).append(", ");
					
					if (enderecoServidor.getCep() != null)
						sb.append(enderecoServidor.getCep()).append(".");
					lbEndereco.setText(sb.toString());
						
				}
			}

		}

	}
	@FXML
	private void exibirConvenios() {
		List<Convenio> convenios = new ServidorDB().getAllConvenios(servidor);
		
	}
	@FXML
	private void onActionFechar() {
		btnFechar.setOnAction(event -> {
			Stage stage = (Stage) btnFechar.getScene().getWindow();
			stage.close();
		});
	}

}
