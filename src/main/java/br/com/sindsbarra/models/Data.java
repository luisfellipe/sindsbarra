package br.com.sindsbarra.models;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Data {
	String pattern = null;
	DateTimeFormatter formatter = null;

	public Data() {
		pattern = "dd/MM/yyyy";
		formatter = DateTimeFormatter.ofPattern(pattern);
	}

	public Data(String pattern) {
		this.pattern = " pattern";
		formatter = DateTimeFormatter.ofPattern(pattern);
	}

	public Date getDate(LocalDate data) {
		if (data == null)
			return null;
		return Date.valueOf(data);
	}

	public String getStringDate(LocalDate data) {
		if (data == null)
			return null;
		return data.format(formatter);
	}

	public LocalDate getLocalDate(String data) {
		if (data == null)
			if (data.matches("[0-9]{2}/[0-9]{2}/[0-9]{4}"))
				return LocalDate.parse(data, formatter);
		return null;
	}

	public LocalDate getLocalDate(Date data) {
		if (data == null)
			return null;
		return data.toLocalDate();
	}

	public String getMes(int monthValue) {
		switch (monthValue) {
		case 1:
			return "janeiro";
		case 2:
			return "fevereiro";
		case 3:
			return "março";
		case 4:
			return "abril";
		case 5:
			return "maio";
		case 6:
			return "junho";
		case 7:
			return "julho";
		case 8:
			return "agosto";
		case 9:
			return "setembro";
		case 10:
			return "outubro";
		case 11:
			return "novembro";
		case 12:
			return "dezembro";

		default:
			return null;
		}
	}
	/**
	 * 
	 * @param String strNum;
	 * @return true if strNum is numeric
	 */
	public boolean isNumeric(String strNum) {
		if (strNum == null || strNum.isBlank()) {
			return false;
		}
		try {
			double d = Double.parseDouble(strNum);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}
