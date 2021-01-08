package br.com.sindsbarra.models;

/**
 * Esta classe mantem uma relação entre Servidor e Convenio
 * 
 * @author luis
 *
 */
public class ServidorConvenio {
	private Servidor servidor = null;
	private Convenio convenio = null;
	private String nome = null;
	private Double valor = 0.0;

	public ServidorConvenio(Servidor servidor, Convenio conv) {
		this.servidor = servidor;
		this.convenio = conv;
		nome = convenio.getNome();
		valor = convenio.getValor() * servidor.getQtdDependentes();

	}

	public Servidor getServidor() {
		return servidor;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public Double getValor() {
		return valor;
	}

	public String getNome() {
		return nome;
	}
}
