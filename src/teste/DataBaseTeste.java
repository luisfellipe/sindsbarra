package teste;

import java.sql.Connection;

import dao.DriveManager;

public class DataBaseTeste {

	public static void main(String[] args) {
	
		Connection conn =  DriveManager.getConnection();
		
		if(conn != null) {
			System.out.println("Conectado!!");
		}else {
			System.out.println("Conexão falhou!!");
		}
	}

}
