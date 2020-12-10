package model;

import java.time.LocalDate;

public class Servidor extends Pessoa {
	private String funcao, matricula;
	private LocalDate dataAdmissao;
	private Ficha fi;
	private Integer qtdDependentes;
	
	public Servidor() {
		qtdDependentes = 0;
		funcao = null;
		matricula = null;
		dataAdmissao = null;
		fi = null;
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
}
