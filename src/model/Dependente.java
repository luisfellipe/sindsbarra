package model;

import java.time.LocalDate;

public class Dependente {
	private long codigo;
	private int quantidade;
	private LocalDate data; 
	
	public Dependente() {
		
	}
	public void setDependente(int quantidade, LocalDate data) {
		this.quantidade = quantidade;
		this.data = data;
	}
	
	public int getQtd() {
		return quantidade;
	}
	
	public LocalDate getData() {
		return data;
	}
	/**
	 * @return the codigo
	 */
	public long getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo() {
		this.codigo = new Data().genCodigo();
	}
}
