package br.com.sindsbarra.constantes;

public enum Sexo {
	MASCULINO, FEMININO, NENHUM;
	public String getType() {
		switch(this) {
			case MASCULINO: return "Masculino";
			case FEMININO: return "Feminino";
			default: return "Nenhum";
		}
	}
	
	public Sexo getType(String type) {
		switch(type) {
			case "Masculino": return MASCULINO;
			case "Feminino": return FEMININO;
			default: return NENHUM;
		}
	}
}
