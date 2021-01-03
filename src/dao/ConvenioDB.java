package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Convenio;
import model.Data;

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
			pstmt.setDouble(4, convenio.getValor());
			pstmt.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void delete(Convenio convenio) {
		// deleta todos os convenios dos servidores
		new ServidorDB().deleteConvenio(convenio.getNome());

		String query = "DELETE FROM convenio WHERE nome=?;";
		conn = DriveManager.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			DriveManager.close();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(Convenio convenio, String nome) {
		String query = "UPDATE convenio SET nome=? descricao=? data_adesao=? valor=? codigo =? WHERE nome=?";
		conn = DriveManager.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			;
			pstmt.setString(1, convenio.getNome());
			pstmt.setString(2, convenio.getDescricao());
			pstmt.setDate(3, new Data().getDate(convenio.getDataAdesao()));
			pstmt.setDouble(4, convenio.getValor());
			pstmt.setString(5, convenio.getNome());// atualiza codigo se nome tiver mudado
			pstmt.setString(6, convenio.getNome());
			pstmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * recebe um cogigo de um cenvenio e retorna um objeto desse convenio
	 */
	public Convenio select(String nomeConvenio) {
		String query = "SELECT * FROM convenio WHERE nome=?;";
		conn = DriveManager.getConnection();
		Convenio convenio = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, nomeConvenio);
			rs = pstmt.executeQuery();

			Data dataManager = new Data();
			while (rs.next()) {
				convenio = new Convenio();
				convenio.setDataAdesao(dataManager.getLocalDate(rs.getDate("data_adesao")));
				convenio.setNome(rs.getString("nome"));
				convenio.setValor(rs.getDouble("valor"));
				convenio.setDescricao(rs.getString("descricao"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return convenio;
	}

	public List<Convenio> getAll() {
		String query = "SELECT * FROM convenio";
		conn = DriveManager.getConnection();
		List<Convenio> convenios = null;

		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			convenios = new ArrayList<Convenio>();
			Convenio c = null;

			Data dataManager = new Data();
			while (rs.next()) {
				c = new Convenio();
				c.setDataAdesao(dataManager.getLocalDate(rs.getDate("data_adesao")));
				c.setNome(rs.getString("nome"));
				c.setValor(rs.getDouble("valor"));
				c.setDescricao(rs.getString("descricao"));
				convenios.add(c);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return convenios;
	}

	public void saveAll(List<Convenio> convenios) {
		convenios.forEach(c -> {
			save(c);
		});
	}
}
