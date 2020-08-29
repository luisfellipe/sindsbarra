package teste;

import java.time.LocalDate;

import dao.ServidorDB;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.Data;
import model.Endereco;
import model.Ficha;
import model.Servidor;

public class ServidorTeste {

	public static void main(String[] args) {

		Servidor s = new Servidor();

		s.setCpf("117.682.136-90");
		s.setDataAdmissao(LocalDate.now());
		s.setDataNasc(LocalDate.now());
		s.setNome("luis felipe");
		s.setFuncao("Estudante");
		s.setMatricula("1235-5");
		s.setQtdDependentes(2);
		s.setRG("653.125-78");

		Endereco e = new Endereco();
		e.setBairro("Santa catarina");
		e.setCep("34978-555");
		e.setCidadeAtual("São Paulo");
		e.setEstado("SP");
		e.setNumero(45);
		e.setRua("Rua das maravilhas");
		Ficha f = new Ficha();
		f.setEndereco(e);
		f.setEstadoCivil("Casado(a)");
		f.setNaturalidade("Brasil");
		f.setTelefone("(68) 9 8856-8989");
		f.setSexo("Masculino");
		f.setNomeMae("Nely Alves Soares");
		f.setNomePai("José Aparecido");

		s.setFicha(f);

		System.out.println(new Data().getDate(s.getDataAdmissao()).getClass());

		System.out.println(s.toString());
		ServidorDB sdb = new ServidorDB();
		sdb.saveServidor(s);
		sdb.deleteServidor(s);

		Alert a = new Alert(AlertType.NONE);
		a.setTitle("Removido");
		a.setHeaderText("Servidor Removido!");
		a.setAlertType(AlertType.CONFIRMATION);
		a.show();
	}

}
