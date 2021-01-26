package br.com.sindsbarra.models;

import java.time.LocalDate;

/**
 * Esta classe mantem uma relação entre Servidor e Convenio
 * 
 * @author luis
 *
 */
public class ServidorConvenio {
	private String nome = null, cpf = null;
	private Double valor = null;
	private Long codigoConvenio = null;
	private LocalDate dataAdesao = null;

	public ServidorConvenio(String nomeConvenio,Long codigoConvenio, String cpf, LocalDate dataAdesao, Double valor) {
		this.nome = nomeConvenio;
		this.valor = valor;
		this.dataAdesao = dataAdesao;
		this.cpf = cpf;
		this.codigoConvenio = codigoConvenio;

	}

	public LocalDate getDataAdesao() {
		return dataAdesao;
	}

	public Double getValor() {
		return valor;
	}

	public String getNome() {
		return nome;
	}
	public String getCpf() {
		return cpf;
	}
	/**
	 * @return the codigoConvenio
	 */
	public Long getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
