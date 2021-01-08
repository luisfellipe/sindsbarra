package br.com.sindsbarra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DataBase {

	private static Connection conn = null;

	private static String host = "localhost";
	private static String user = "root";
	private static String password = "25302356";
	private static String url = null;
	private static String dataBase = "sindsbarra";
	private static String port = "3306";
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String driveManager = "jdbc:mysql://";

	public static Connection getConnection() {
		url = driveManager + host + ":" + port + "/" + dataBase;
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName(driver);

			// Setup the connection with the DB
			System.out.println("Conectando com o banco de dados ...");
			conn = DriverManager.getConnection(url, user, password);

			if (conn != null) {
				System.out.println("Conexão estabelecida ! ! !");
				System.out.print("Banco de dados: "+ conn.getMetaData().getDatabaseProductName() + "\n"
						+ "Versão: " + conn.getMetaData().getDatabaseProductVersion() + "\n");
			} else {
				System.out.println("Conexão falhou ! ! !");
			}
		} catch (ClassNotFoundException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha de conexão com o banco de dados\n" + e.getMessage());
				a.show();
			}
			e.printStackTrace();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha de conexão com o banco de dados\n" + e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}

		return conn;
	}

	// You need to close the resultSet
	public static void close() {
		System.out.println("Fechando banco de dados . . .");
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Banco de dados fechado . . .");
	}

	public void setConfigurations(String driveManager, String driver, String port, String dataBase, String url,
			String host, String user, String password) {
		this.driveManager = driveManager;
		this.driver = driver;
		this.port = port;
		this.dataBase = dataBase;
		this.url = url;		
		this.host = host;
		this.user = user;
		this.password = password;
	}
}
