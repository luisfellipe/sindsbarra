package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.stage.FileChooser;
import model.Convenio;
import model.Servidor;

public class DriveManager {

	private static Connection conn = null;

	private static String host = "localhost";
	private static String user = "root";
	private static String password = "25302356";
	private static String url = null;
	private static String dataBase = "sindsbarra";
	private static String port = "3306";

	public static Connection getConnection() {
		url =  "jdbc:mysql://"+ host + ":" + port + "/" + dataBase;
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
		if (conn != null) {
			System.out.println("Conexão estabelecida ! ! !");
			try {
				System.out.print(conn.getMetaData().getDatabaseProductName() + " ");
				System.out.println(conn.getMetaData().getDatabaseProductVersion() + " ");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
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

	/**
	 * exportar dados para csv
	 */
	public static void saveAllContentServidor() {
		List<Servidor> servidores = new ArrayList<Servidor>();
		servidores.addAll(new ServidorDB().getAllServidores());
		List<List<Convenio>> servidorConvenios = new ArrayList<List<Convenio>>();
		Map<Servidor, List<Convenio>> map = new HashMap<Servidor, List<Convenio>>();

		List<Convenio> convenios = null;
		for (Servidor s : servidores) {
			convenios = new ServidorDB().getAllConvenios(s);
			servidorConvenios.add(convenios);
			map.put(s, convenios);
		}
		
		String file = "servidorcontent.csv";
		
		FileChooser fs = new FileChooser();
		fs.setTitle("Exportar dados dos servidores");
		
	}
}
