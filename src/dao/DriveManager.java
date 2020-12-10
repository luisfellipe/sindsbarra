package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class DriveManager {

	private static Connection conn = null;

	private static String host = "localhost";
	private static String user = "root";
	private static String password = "25302356";
	private static String url = null;
	private static String dataBase = "sindsbarra";
	private static String port = "3306";

	public static Connection getConnection() {
		url = "jdbc:mysql://" + host + ":" + port + "/" + dataBase;
		try {

			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Setup the connection with the DB
			conn = DriverManager.getConnection(url, user, password);

			if (conn != null) {
				System.out.println("Conexão estabelecida ! ! !");
				System.out.print(conn.getMetaData().getDatabaseProductName() + "\n"
						+ conn.getMetaData().getDatabaseProductVersion());
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
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
