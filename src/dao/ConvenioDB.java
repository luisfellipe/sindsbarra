package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import model.Convenio;
import model.Data;

/**
 * 
 * @author luis
 * 
 */

public class ConvenioDB {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	public void save(Convenio convenio) {
		String query = "INSERT INTO convenio (nome, descricao, data_adesao, valor) values(?, ?, ?, ?);";
		conn = DriveManager.getConnection();
		try {
			
			pstmt = conn.prepareStatement(query);
			Data dataManager = new Data();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			pstmt.setString(2, convenio.getDescricao());
			pstmt.setDate(3, dataManager.getDate(convenio.getDataAdesao()));
			pstmt.setFloat(4, convenio.getValor());
			pstmt.executeUpdate(); 
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
				
	}

	public void delete(Convenio convenio) {
		String query = "DELETE FROM convenio WHERE nome=?;";
		conn = DriveManager.getConnection();
		
		try {
			
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void update(Convenio convenio) {
		String query = "UPDATE convenio SET WHERE codigo=?";
	}

	public Convenio select(String codigo) {
		String query = "";
		return null;
	}

	public List<Convenio> selectConvenios(int until) {
		String query = "";
		return null;
	}

	public List<Convenio> getAll() {
		String query = "";
		return null;
	}
}
