package br.com.sindsbarra.models;
import java.time.LocalDate;
public class Convenio {
	private String nomeConvenio = null, descricao = null;
	private LocalDate dataAdesao = null;
	private Double valor = null;
	private Long codigo = null;
	private boolean dependentesInclude = false;

	public Convenio(Long codigo) {
		this.codigo = codigo;
	}

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
	public Double getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

	/**
	 * @return the incluiDependentes
	 */
	public boolean isDependentesInlude() {
		return dependentesInclude;
	}

	/**
	 * @param incluiDependentes the incluiDependentes to set
	 */
	public void includeDependentes(boolean dependentesInclude) {
		this.dependentesInclude = dependentesInclude;
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

	public Long getCodigo() {
		return codigo;
	}
}
