package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DriveManager {

	private static Connection conn = null;

	private static String host = "localhost";
	private static String user = "root";
	private static String password = "25302356";
	private static String url = "jdbc:mysql://localhost:3306/sindsbarra";
	//"jdbc:mysql://"+ host + ":" + port + "/" + dataBase;
	private static String dataBase = "sindsbarra";
	private static String port = "3306";


	public static Connection getConnection() {
		try {
			if (conn == null) {
				// This will load the MySQL driver, each DB has its own driver
				Class.forName("com.mysql.cj.jdbc.Driver");

				// Setup the connection with the DB
				conn = DriverManager.getConnection(url, user, password);

			} else if (conn.isClosed()) {
				conn = null;
		 
				return getConnection();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		if(conn != null) {
			System.out.println("Conexão estabelecida ! ! !");
			try {
				System.out.print(conn.getMetaData().getDatabaseProductName()+" ");
				System.out.println(conn.getMetaData().getDatabaseProductVersion()+" ");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			System.out.println("Conexão falhou ! ! !");
		}
		return conn;
	}

	// You need to close the resultSet
	public static void close() {
		try {
			if (conn != null)
				conn.close();

		} catch (Exception e) {

		}
	}
}
