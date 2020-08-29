package dao;

import model.Convenio;

public class ConvenioServidor extends Convenio{
	private int qtdDependentes;
	
	public ConvenioServidor(int qtdDependentes) {
		this. qtdDependentes =  qtdDependentes;
	}
	
	public int getDependente() {
		return  qtdDependentes;
	}
	
}
