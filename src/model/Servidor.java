package model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import dao.ConvenioDB;
import dao.ConvenioServidor;
import dao.ServidorDB;

public class Servidor extends Pessoa {
	private String funcao, matricula;
	private LocalDate dataAdmissao;
	private Ficha fi;
	private Integer qtdDependentes;
	private Set<ConvenioServidor> convenios;
	
	public Servidor() {
		qtdDependentes = 0;
		funcao = null;
		matricula = null;
		dataAdmissao = null;
		fi = null;
		convenios = null;
	}
	
	/**
	 * @return the funcao
	 */
	public String getFuncao() {
		return funcao;
	}

	/**
	 * @param funcao the funcao to set
	 */
	public void setFuncao(String funcao) {
		this.funcao = funcao;
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the dataAdmissao
	 */
	public LocalDate getDataAdmissao() {
		return dataAdmissao;
	}

	/**
	 * @param dataAdmissao the dataAdmissao to set
	 */
	public void setDataAdmissao(LocalDate dataAdmissao) {
		this.dataAdmissao = dataAdmissao;
	}

	/**
	 * @return the qtdDependentes
	 */
	public Integer getQtdDependentes() {
		return qtdDependentes;
	}

	/**
	 * @param qtdDependentes the qtdDependentes to set
	 */
	public void setQtdDependentes(Integer qtdDependentes) {
		this.qtdDependentes = qtdDependentes;
	}

	/**
	 * @param Convenio
	 */
	public void setConvenio(ConvenioServidor cs) {
		if (convenios != null) {
			convenios = new ServidorDB().getAllConvenios(this);
		} else {
			convenios = new HashSet<ConvenioServidor>();
		}
		convenios.add(cs);
		new ServidorDB().saveConvenio(cs, this);
	}

	/**
	 * @return Convenio
	 */
	public ConvenioServidor getConvenio(int codConv) {
		int qtd = 0;
		Convenio c = new Convenio();
		c = new ConvenioDB().select(codConv);
		ConvenioServidor cs = new ConvenioServidor(qtd);

		return cs;
	}

	/**
	 * @return lista de Convenios
	 */
	public Set<ConvenioServidor> getConvenios() {
		if (convenios == null) {
			convenios = new ServidorDB().getAllConvenios(this);
		}
		return convenios;
	}

	public void setConvenios(Set<ConvenioServidor> convenios) {
		if (convenios != null) {
			convenios.addAll(convenios);
		} else {
			convenios = new HashSet<ConvenioServidor>();
		}
		new ServidorDB().saveConvenios(convenios, this);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Nome: ").append(getNome()).append("\n")
		.append("CPF: ").append(getCpf()).append("\n")
		.append("RG: ").append(getRg()).append("\n")
		.append("Função: ").append(getFuncao()).append("\n")
		.append("Matricula: ").append(getMatricula()).append("\n")
		.append("Data Nascimento: ").append(getDataNasc()).append("\n")
		.append("Data Admissao: ").append(getDataAdmissao());
		
		return  sb.toString();
	}

	/**
	 * @return the fi
	 */
	public Ficha getFicha() {
		return fi;
	}

	/**
	 * @param fi the fi to set
	 */
	public void setFicha(Ficha fi) {
		this.fi = fi;
	}
}
