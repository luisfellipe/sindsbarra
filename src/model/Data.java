package model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

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
		return Date.valueOf(data);
	}
	
	public String getStringDate(LocalDate data) {
		return data.format(formatter);
	}
	
	public LocalDate getLocalDate(String data) {
		return LocalDate.parse(data, formatter);
	}
	public LocalDate getLocalDate(Date data) {
		return data.toLocalDate();
	}
	/*
	 * gera codigos para o banco de dados
	 */
	public long genCodigo() {
		int dayOfMonth, dayOfYear, month, year, offset;
		
		Random gerador = new Random();
		LocalDate time = LocalDate.now();
		dayOfMonth = time.getDayOfMonth();
		year = time.getYear();
		month = time.getMonthValue();
		dayOfYear = time.getDayOfYear();
		
		offset = dayOfMonth+dayOfYear+month+year;
		
		year = month ^ gerador.nextInt(1000000);
		dayOfMonth = dayOfYear ^gerador.nextInt(1000000);
		dayOfYear = year ^gerador.nextInt(1000000);
		month = dayOfYear^gerador.nextInt(1000000);
		
		String str =  (dayOfMonth^dayOfYear^month) +""+(year^offset^gerador.nextInt(1000000));
		
		return Long.parseLong(str);
	}
}
