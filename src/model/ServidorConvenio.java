package model;

/**
 * Esta classe mantem uma relação entre Servidor e Convenio
 * 
 * @author luis
 *
 */
public class ServidorConvenio {
	private Servidor servidor = null;
	private Convenio convenio = null;
	private Double valor = null;

	public ServidorConvenio(Servidor servidor, Convenio conv) {
		this.servidor = servidor;
		this.convenio = conv;
		valor = convenio.getValor() * servidor.getQtdDependentes();

	}

	public Servidor getServidor() {
		return servidor;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public Double getValorTotal() {
		return valor;
	}
}
