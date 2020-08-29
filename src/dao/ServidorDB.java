package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Servidor;
import model.Convenio;
import model.Data;
import model.Endereco;
import model.Ficha;

public class ServidorDB {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	/*
	 * Salva servidor no banco de dados 
	 */
	public void saveServidor(Servidor s) {
		String saveServidor = "INSERT INTO servidor (nome, cpf, rg, matricula,funcao, dependentes,data_admissao, data_nasc)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?);";
		conn = DriveManager.getConnection();

		try {
			pstmt = conn.prepareStatement(saveServidor);

			pstmt.setString(1, s.getNome());
			pstmt.setString(2, s.getCpf());
			pstmt.setString(3, s.getRg());
			pstmt.setString(4, s.getMatricula());
			pstmt.setString(5, s.getFuncao());
			pstmt.setInt(6, s.getQtdDependentes());
			Data dataManager = new Data();
			pstmt.setDate(7, dataManager.getDate(s.getDataAdmissao()));
			pstmt.setDate(8, new Data().getDate(s.getDataNasc()));

			pstmt.execute();
			conn.close();
			if (s.getFicha() != null) {
				saveFicha(s);// salva a ficha de cadastro do servidor
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * query do placeholder de busca de um servidor de acordo com a string de busca
	 */
	public Servidor selectServidor(String str) {
		String query = "SELECT FROM servidor WHERE cpf=? OR matricula=? OR nome=?);";
		Servidor s = new Servidor();
		conn = DriveManager.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, str);
			pstmt.setString(2, str);
			pstmt.setString(3, str);
			rs = pstmt.executeQuery();

			s.setCpf(rs.getString("cpf"));
			Data dataManager = new Data();

			// Converte Date em LocalDate
			s.setDataAdmissao(dataManager.getLocalDate(rs.getDate("data_admissao")));
			s.setDataNasc(dataManager.getLocalDate(rs.getDate("data_nasc")));

			s.setFuncao(rs.getString("funcao"));
			s.setNome(rs.getString("nome"));
			s.setMatricula(rs.getString("matricula"));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// fecha conexão com o banco de dados
		DriveManager.close();

		return s;
	}

	/*
	 * Ao deletar servidor deve-se também deletar a ficha cadastrada
	 */
	public int deleteServidor(Servidor servidor) {
		String query = "DELETE f, s FROM ficha AS f JOIN servidor AS s WHERE f.codigo_servidor=s.cpf AND s.cpf=?;";
		conn = DriveManager.getConnection();
		int rowsDeleted = 0;
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, servidor.getCpf());

			rowsDeleted = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DriveManager.close();
		return rowsDeleted;
	}

	/*
	 * Atualiza servidor no bd
	 */
	public void updateServidor(Servidor servidor) {
		this.deleteServidor(servidor);
		this.saveServidor(servidor);
	}

	public Ficha selectFicha(long codigoServidor) {
		String query = "SELEC FROM ficha WHERE codigo_servidor=?;";
		Ficha f = null;
		conn = DriveManager.getConnection();

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, codigoServidor);
			rs = pstmt.executeQuery();
			f = new Ficha();
			f.setNomePai(rs.getString("nome_pai"));
			f.setNomeMae(rs.getString("nome_mae"));
			f.setSexo(rs.getString("sexo"));
			f.setNaturalidade(rs.getString("naturalidade"));
			f.setEstadoCivil(rs.getString("estado_civil"));
			f.setTelefone(rs.getString("telefone"));
			Endereco e = new Endereco();
			e.setEstado(rs.getString(""));
			e.setCidade(rs.getString(""));
			e.setCep(rs.getString(""));
			e.setBairro(rs.getString(""));
			e.setRua(rs.getString(""));
			e.setNumero(rs.getInt("numero_rua"));

			f.setEndereco(e);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DriveManager.close();

		return f;
	}

	private void saveFicha(Servidor s) {

		String saveFichaServidor = "INSERT INTO ficha (codigo_servidor, nome_pai, nome_mae,"
				+ "sexo, naturalidade, estado_civil, telefone, estado, cidade, cep, bairro, rua, numero_rua)"
				+ "values(?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?);";
		conn = DriveManager.getConnection();

		try {
			pstmt = conn.prepareStatement(saveFichaServidor);
			Ficha f = s.getFicha();

			pstmt.setString(1, s.getCpf());
			pstmt.setString(2, f.getNomePai());
			pstmt.setString(3, f.getNomeMae());
			pstmt.setString(4, f.getSexo());
			pstmt.setString(5, f.getNaturalidade());
			pstmt.setString(6, f.getEstadoCivil());
			pstmt.setString(7, f.getTelefone());

			pstmt.setString(8, f.getEndereco().getEstado());
			pstmt.setString(9, f.getEndereco().getCidade());
			pstmt.setString(10, f.getEndereco().getCep());
			pstmt.setString(11, f.getEndereco().getBairro());
			pstmt.setString(12, f.getEndereco().getRua());
			pstmt.setInt(13, f.getEndereco().getNumero());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DriveManager.close();
	}

	/*
	 * retorna todos os servidores do bd
	 */
	public List<Servidor> getAllServidores() {
		String selectServidores = "SELECT * FROM servidor;";
		conn = DriveManager.getConnection();
		List<Servidor> servidores = null;

		try {
			pstmt = conn.prepareStatement(selectServidores);
			rs = pstmt.executeQuery();

			servidores = new ArrayList<Servidor>();
			Servidor s = null;

			Data dataManager = new Data();
			while (rs.next()) {
				s = new Servidor();
				s.setCpf(rs.getString("cpf"));
				s.setMatricula(rs.getString("matricula"));
				s.setNome(rs.getString("nome"));
				s.setFuncao(rs.getString("funcao"));
				s.setRG(rs.getString("rg"));
				s.setQtdDependentes(rs.getInt("dependentes"));

				s.setDataAdmissao(dataManager.getLocalDate(rs.getDate("data_admissao")));
				s.setDataNasc(dataManager.getLocalDate(rs.getDate("data_nasc")));

				servidores.add(s);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return servidores;
	}

	public void saveConvenio(ConvenioServidor cs, Servidor s) {
		String query1 = "INSERT INTO convenio_servidor (codigo_servidor, codigo_convenio, dependentes,data_adesao)"
				+ "values(?, ?, ?, ?);";
		String query2 = "SELECT c.codigo, s.codigo FROM convenio AS c JOIN servidor s ON c.nome=? AND s.nome=?);";
		conn = DriveManager.getConnection();
		int codigo_servidor = 0, codigo_convenio = 0;

		try {
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, cs.getNome());
			pstmt.setString(2, s.getNome());
			rs = pstmt.executeQuery();
			codigo_servidor = rs.getInt(1);
			codigo_convenio = rs.getInt(2);

			pstmt.clearBatch();
			pstmt = conn.prepareStatement(query1);
			pstmt.setInt(1, codigo_servidor);
			pstmt.setInt(1, codigo_convenio);
			pstmt.setInt(3, cs.getDependente());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveConvenios(Set<ConvenioServidor> convenios, Servidor s) {
		for (ConvenioServidor cs : convenios) {
			this.saveConvenio(cs, s);
		}
	}

	public Set<ConvenioServidor> getAllConvenios(Servidor servidor) {
		String query = "SELECT c.nome,c.data_adesao,c.descricao, cs.dependentes, c.valor FROM (convenio AS c LEFT JOIN "
				+ "servidor AS s,convenio_servidor as cs) ON cs.codigo_servidor=s.codigo AND "
				+ "cs.codigo_convenio=c.codigo;";
		conn = DriveManager.getConnection();
		Set<ConvenioServidor> convenios = null;

		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			convenios = new HashSet<ConvenioServidor>();
			Convenio c = null;
			while (rs.next()) {
				c = new Convenio();
				c.setNome(rs.getString(1));
				c.setDataAdesao(new Data().getLocalDate(rs.getDate(2)));
				c.setDescricao(rs.getString(3));
				c.setValor(Float.parseFloat(rs.getString(1)));
				c.setDescricao(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		servidor.setConvenios(convenios);
		return convenios;
	}

}
