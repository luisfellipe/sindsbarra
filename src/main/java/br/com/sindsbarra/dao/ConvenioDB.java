package br.com.sindsbarra.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;

public class ConvenioDB {
	private Connection conn = null;
	private Statement stmt = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	Alert alertError = null;

	public ConvenioDB() {
		alertError = new Alert(AlertType.ERROR);
	}

	public boolean save(Convenio convenio) {
		String query = "INSERT INTO convenio (nome, descricao, inclui_dependentes,codigo, data_adesao, valor) values(?, ?, ?, ?, ?, ?);";
		conn = DataBase.getConnection();
		try {

			pstmt = conn.prepareStatement(query);
			Data dataManager = new Data();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			pstmt.setString(2, convenio.getDescricao());
			pstmt.setBoolean(3, convenio.isDependentesInlude());
			pstmt.setLong(4, new Data().getTimeLong());
			pstmt.setDate(5, dataManager.getDate(convenio.getDataAdesao()));
			pstmt.setDouble(6, convenio.getValor());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao salvar convenio!!");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			DataBase.close();
			return false;
		}
		DataBase.close();
		return true;
	}

	public boolean delete(Convenio convenio) {
		// deleta todos os convenios dos servidores
		new ServidorDB().deleteConvenio(convenio.getNome());

		String query = "DELETE FROM convenio WHERE nome=?;";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao deletar convênio!!");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			DataBase.close();
			return false;
		}
		DataBase.close();
		return true;
	}

	public boolean update(Convenio convenio) {
		String query = "UPDATE convenio SET nome=?, descricao=?, inclui_dependentes=?, data_adesao=?, valor=? WHERE codigo=?";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			pstmt.setString(2, convenio.getDescricao());
			pstmt.setBoolean(3, convenio.isDependentesInlude());
			pstmt.setDate(4, new Data().getDate(convenio.getDataAdesao()));
			pstmt.setDouble(5, convenio.getValor());
			pstmt.setLong(6, convenio.getCodigo());// atualiza codigo se nome tiver mudado
			pstmt.executeUpdate();
		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao atualizar convênio!!");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			DataBase.close();
			return false;
		}
		DataBase.close();
		return true;
	}

	/*
	 * recebe um cogigo de um cenvenio e retorna um objeto desse convenio
	 */
	public Convenio select(Long codigoConvenio) {
		String query = "SELECT * FROM convenio WHERE codigo=?;";
		conn = DataBase.getConnection();
		Convenio convenio = null;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, codigoConvenio);
			rs = pstmt.executeQuery();

			Data dataManager = new Data();
			while (rs.next()) {
				convenio = new Convenio(codigoConvenio);
				convenio.setDataAdesao(dataManager.getLocalDate(rs.getDate("data_adesao")));
				convenio.setNome(rs.getString("nome"));
				convenio.setValor(rs.getDouble("valor"));
				convenio.includeDependentes(rs.getBoolean("inclui_dependentes"));
				convenio.setDescricao(rs.getString("descricao"));
			}
		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao selecionar convênio!!");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
		}
		DataBase.close();
		return convenio;
	}

	public List<Convenio> getAll() {
		String query = "SELECT * FROM convenio";
		conn = DataBase.getConnection();
		List<Convenio> convenios = null;

		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			convenios = new ArrayList<Convenio>();
			Convenio convenio = null;
			Data dataManager = new Data();
			while (rs.next()) {
				convenio = new Convenio(rs.getLong("codigo"));
				convenio.setDataAdesao(dataManager.getLocalDate(rs.getDate("data_adesao")));
				convenio.setNome(rs.getString("nome"));
				convenio.setValor(rs.getDouble("valor"));
				convenio.includeDependentes(rs.getBoolean("inclui_dependentes"));
				convenio.setDescricao(rs.getString("descricao"));
				convenios.add(convenio);
			}

		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao acessar convênios!!");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
		}
		DataBase.close();
		return convenios;
	}

	public boolean saveAll(List<Convenio> convenios) {
		convenios.forEach(c -> {
			save(c);
		});
		return true;
	}
}
