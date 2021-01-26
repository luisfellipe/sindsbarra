package br.com.sindsbarra.constantes;

public enum EstadoCivil {
	SOLT, CASAD, VIUV, DIV, NENHUM;
	
	/**
	 * 
	 * @return The String Type of Enum
	 */
	public String getType() {
		switch(this) {
		case SOLT: return "Solteiro(a)";
		case CASAD: return "Casado(a)";
		case VIUV: return "Viuvo(a)";
		case DIV: return "Divorciado(a)";
		default: return "Nenhum";
		}
	}
	
	/**
	 * 
	 * @param type
	 * @return the Enum Type of String
	 */
	public EstadoCivil getType(String type) {
		switch(type) {
		case "Solteiro(a)": return SOLT;
		case "Casado(a)": return CASAD;
		case "Viuvo(a)": return VIUV;
		case "Divorciado(A)": return DIV;
		default: return NENHUM;
		}
	}
}
