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
import model.ServidorConvenio;
import pdf.ServidorFile;

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
		s.setQtdDependentes(2);
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
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
		servidores.add(s);
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
		conv.setValor(23.1);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		convenios.add(conv);
		return convenios;
	}
	
	public static Servidor getServidor() {
		Servidor s = new Servidor();
		s.setCpf("327.276.657-25");
		s.setDataAdmissao(LocalDate.now());
		s.setDataNasc(LocalDate.now());
		s.setFuncao("Pedreiro");
		s.setMatricula("1324-8");
		s.setNome("Maria Antonia");
		s.setQtdDependentes(1);
		s.setRG("845.897.56");
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
		return s;
	}
	public static Convenio getConvenio() {
		Convenio conv = new Convenio();
		conv.setDataAdesao(LocalDate.now());
		conv.setDescricao("Saude Bucal");
		conv.setNome("OdontoMed");
		conv.setValor(23.1);
		
		return conv;
	}

	public static void main(String[] args) {
		String file = "file.pdf";
	
		new ServidorDB().save(getServidor());
		//new ConvenioDB().save(getConvenio());
		
		/*List<Convenio> convenios = getConvenios();
		List<Servidor> servidores = getServidores();
		List<ServidorConvenio> sc = new ArrayList<ServidorConvenio>();
		for(Convenio c : convenios) {
			for(Servidor s: servidores) {
				sc.add(new ServidorConvenio(s, c));
			}
		}
		
		//ConvenioFile conv = new ConvenioFile(file);
		//conv.addConvenioServidor(sc);
		ServidorFile sf = new ServidorFile(file);
		sf.addServidor(getServidores());*/
	}
}
