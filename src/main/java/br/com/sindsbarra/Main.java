package br.com.sindsbarra;

import java.net.URL;

import br.com.sindsbarra.views.TelaPrincipal;

public class Main {
	 public final URL LOGO = getClass().getResource("assets/img/logox4.png");
	 public final URL TELA_PRINCIPAL = getClass().getResource("fxml/TelaPrincipal.fxml");
	 public final URL TELA_CADASTRO_SERVIDOR = getClass().getResource("fxml/CadastroServidor.fxml");
	 public final URL TELA_CADASTRO_CONVENIO = getClass().getResource("fxml/CadastroConvenio.fxml");
	 public final URL TELA_CADASTRO_SC = getClass().getResource("fxml/CadastroSC.fxml");
	 public final URL TELA_CONVENIOS = getClass().getResource("fxml/VisualizarConvenios.fxml");
	 public final URL TELA_SERVIDOR_CONVENIO = getClass().getResource("fxml/VisualizarServidorConvenios.fxml");

	public static void main(String[] args) {
		TelaPrincipal.main(args);
	}

}
