package teste;

import java.time.LocalDate;

import model.Data;

public class DateTeste {

	public static void main(String[] args) {
		LocalDate agora = LocalDate.now(); 
		Data d = new Data();
		
		String str = d.getStringDate(agora);
		
		System.out.println(str);
		
		System.out.println(d.getLocalDate(str));

	}

}
