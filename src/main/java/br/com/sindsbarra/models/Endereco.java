package br.com.sindsbarra.models;

public class Endereco {
	private Integer numero;
	private String rua, cidadeAtual, cidadeNatal, bairro, cep, estado;

	public Endereco() {
		numero = null;
		rua = null;
		cidadeAtual = null;
		cidadeNatal = null;
		bairro = null;
		cep = null;
		estado = null;
	}

	/**
	 * @return the numero
	 */
	public Integer getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 */
	public void setNumero(Integer numero) {
		this.numero = numero;
	}

	/**
	 * @return the rua
	 */
	public String getRua() {
		return rua;
	}

	/**
	 * @param rua the rua to set
	 */
	public void setRua(String rua) {
		this.rua = rua;
	}

	/**
	 * @return the naturalidade
	 */
	public String getCidadeNatal() {
		return cidadeNatal;
	}

	/**
	 * @param cidadeNatal the naturalidade to set
	 */
	public void setCidadeNatal(String cidadeNatal) {
		this.cidadeNatal = cidadeNatal;
	}

	/**
	 * @return the cidade
	 */
	public String getCidadeAtual() {
		return cidadeAtual;
	}

	/**
	 * @param cidade the cidade to set
	 */
	public void setCidadeAtual(String cidade) {
		this.cidadeAtual = cidade;
	}

	/**
	 * @return the bairro
	 */
	public String getBairro() {
		return bairro;
	}

	/**
	 * @param bairro the bairro to set
	 */
	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	/**
	 * @return the cep
	 */
	public String getCep() {
		return cep;
	}

	/**
	 * @param cep the cep to set
	 */
	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEstado() {

		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
