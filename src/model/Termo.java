package model;


public class Termo {
	private Servidor servidor;
	private Ficha fs;

	public Termo(Servidor servidor) {
		this.servidor = servidor;
		this.fs = servidor.getFicha();
	}

	public String getToken(String lex) {
		switch (lex) {
		case "$NOMEREQ":
			return servidor.getNome();
		case "$MES":
			return servidor.getDataNasc().getMonth()+"";
		case "$DIA":
			return servidor.getDataNasc().getDayOfMonth()+"";
		case "$ANO":
			return servidor.getDataNasc().getYear()+"";
		case "$NATUR":
			return fs.getNaturalidade();
		case "$DNASC":
			return new Data().getStringDate(servidor.getDataNasc());
		case "$CPF":
			return servidor.getCpf();
		case "$RG":
			return servidor.getRg();
		case "$TEL":
			return fs.getTelefone();
		case "$ESTADOCIVL": fs.getEstadoCivil();
		case "$SEXO": fs.getSexo();
		case "$PAI": fs.getNomePai();
		case "$MAE": fs.getNomeMae();
		case "$ENDR":
			return new StringBuffer().append("Rua ").append(fs.getEndereco().getRua())
					.append(" Nº ").append(fs.getEndereco().getNumero())
					.append(", ").append(fs.getEndereco().getCep())
					.append(", Bairro ").append(fs.getEndereco().getBairro())
					.append(", ").append(fs.getEndereco().getCidade())
					.append(", ").append(fs.getEndereco().getEstado()).toString();
		case "$NUMR": fs.getEndereco().getNumero();
		case "$ADMIS": new Data().getStringDate(servidor.getDataAdmissao());
		
		default:
			throw new IllegalArgumentException("Unexpected value: " + lex);
		}

	}
}
