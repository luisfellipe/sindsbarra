package teste;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import dao.ConvenioDB;
import dao.ServidorDB;
import model.Convenio;
import model.Endereco;
import model.Ficha;
import model.Servidor;

public class ServidorTeste {
	public static List<Servidor> getServidores() {
		List<Servidor> servidores = new ArrayList<Servidor>(6);
		Servidor s = new Servidor();
		s.setCpf("149.123.286-91");
		s.setDataAdmissao(LocalDate.now());
		s.setDataNasc(LocalDate.now());
		s.setFuncao("Pedreiro");
		s.setMatricula("1324-8");
		s.setNome("Samuel Ferreira Silva");
		s.setQtdDependentes(0);
		s.setRG("567.897.86");
		Ficha fi = new Ficha();
		fi.setEstadoCivil("Solteiro");
		fi.setNomeMae("Nely Alves Soares");
		fi.setNomePai("José Aparecido Gomes Soares");
		fi.setSexo("Masculino");
		fi.setTelefone("97567956");
		Endereco e = new Endereco();
		e.setBairro("Residencial Ouro");
		e.setCep("37945-000");
		e.setCidadeAtual("São José da Barra");
		e.setCidadeNatal("Bocaiuva MG");
		e.setEstado("MG");
		e.setNumero(265);
		e.setRua("Rua Lázaro Flor");

		fi.setEndereco(e);
		s.setFicha(fi);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);

		return servidores;
	}
	public static List<Convenio> getConvenios() {
		List<Convenio> convenios = new ArrayList<Convenio>(6);
		Convenio conv = new Convenio();
		conv.setDataAdesao(LocalDate.now());
		conv.setDescricao("Dentista Geral");
		conv.setNome("Clinica Clean teth");
		conv.setValor(56.5);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		return convenios;
	}

	public static void main(String[] args) {
		new ServidorDB().saveAll(getServidores());
		new ConvenioDB().saveAll(getConvenios());
	}
}
