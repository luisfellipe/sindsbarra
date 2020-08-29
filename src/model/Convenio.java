package model;

import java.time.LocalDate;

public class Convenio {
	private String nomeConvenio, descricao;
	private LocalDate dataAdesao;
	private float valor;

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nomeConvenio;
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nomeConvenio = nome;
	}

	/**
	 * @return the dataAdesao
	 */
	public LocalDate getDataAdesao() {
		return dataAdesao;
	}

	/**
	 * @param dataAdesao the dataAdesao to set
	 */
	public void setDataAdesao(LocalDate dataAdesao) {
		this.dataAdesao = dataAdesao;
	}

	/**
	 * @return the valor
	 */
	public float getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(float valor) {
		this.valor = valor;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		return descricao;
	}

	/**
	 * @param descricao the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
}
