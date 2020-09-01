package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
			DriveManager.close();
			e.printStackTrace();
		}
		DriveManager.close();
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
			DriveManager.close();
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
		String cpf = servidor.getCpf();

		/*
		 * recupera buffer de segurança temporário dos convenios associados ao servidor
		 */
		List<Convenio> cache = getAllConvenios(servidor);

		if (deleteConvenioServidor(cpf)) {// remove os convenios-servidor do bd

			if (deleteFicha(cpf)) {// remove a ficha-servidor do bd
				/*
				 * Deleta servidor do bd
				 */
				String query = "DELETE FROM servidor WHERE cpf=?";

				conn = DriveManager.getConnection();
				int rowsDeleted = 0;
				try {
					pstmt = conn.prepareStatement(query);
					pstmt.setString(1, cpf);
					rowsDeleted = pstmt.executeUpdate();
					if (rowsDeleted > 0) {
						Alert a = new Alert(AlertType.CONFIRMATION);
						a.setHeaderText("Servidor deletado!");
					}

				} catch (SQLException e) {
					{
						saveServidor(servidor);
						saveFicha(servidor);
						for (Convenio c : cache) {
							saveConvenio(c, servidor);
						}
					}

					e.printStackTrace();
				}
				DriveManager.close();
				return rowsDeleted;
			} else {
				for (Convenio c : cache) {
					saveConvenio(c, servidor);
				}
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao tentar deletar servidor: " + servidor.getCpf());
				a.show();
			}
		} else {
			Alert a = new Alert(AlertType.ERROR);
			a.setHeaderText("Falha ao tentar deletar servidor: " + servidor.getCpf());
			a.show();
		}
		DriveManager.close();

		return 0;
	}

	/*
	 * Atualiza servidor no bd
	 */
	public boolean updateServidor(Servidor servidor) {
		String query = "UPDATE servidor SET nome=?, cpf=?, rg=?, matricula=?, funcao=?, dependentes=?, data_admissao=?, data_nasc=? WHERE cpf=?";
		conn = DriveManager.getConnection();
		try {
			if (servidor.getFicha() != null) {
				updateFicha(servidor);
			}
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, servidor.getNome());
			pstmt.setString(2, servidor.getCpf());
			pstmt.setString(3, servidor.getRg());
			pstmt.setString(4, servidor.getMatricula());
			pstmt.setString(5, servidor.getFuncao());
			pstmt.setInt(6, servidor.getQtdDependentes());
			Data dataManager = new Data();
			pstmt.setDate(7, dataManager.getDate(servidor.getDataAdmissao()));
			pstmt.setDate(8, dataManager.getDate(servidor.getDataNasc()));
			pstmt.setString(9, servidor.getCpf());

		} catch (SQLException e) {
			DriveManager.close();
			e.printStackTrace();
			return false;
		}
		DriveManager.close();
		return true;

	}

	/*
	 * atualiza ficha do servidor no bd
	 */
	private void updateFicha(Servidor servidor) {
		String updateFichaServidor = "UPDATE ficha SET codigo_servidor=?, nome_pai=?, nome_mae=?, sexo=?, cidade_natal=?, estado_civil=?, telefone=?, estado=?, cidade_atual=?, cep=?, bairro=?, rua=?, numero_rua=? WHERE codigo_servidor=?";
		conn = DriveManager.getConnection();
		try {
			pstmt = conn.prepareStatement(updateFichaServidor);
			Ficha fichaServidor = servidor.getFicha();

			pstmt.setString(1, servidor.getCpf());
			pstmt.setString(2, fichaServidor.getNomePai());
			pstmt.setString(3, fichaServidor.getNomeMae());
			pstmt.setString(4, fichaServidor.getSexo());
			pstmt.setString(5, fichaServidor.getEndereco().getCidadeNatal());
			pstmt.setString(6, fichaServidor.getEstadoCivil());
			pstmt.setString(7, fichaServidor.getTelefone());
			pstmt.setString(8, fichaServidor.getEndereco().getEstado());
			pstmt.setString(9, fichaServidor.getEndereco().getCidadeAtual());
			pstmt.setString(10, fichaServidor.getEndereco().getCep());
			pstmt.setString(11, fichaServidor.getEndereco().getBairro());
			pstmt.setString(12, fichaServidor.getEndereco().getRua());
			pstmt.setInt(13, fichaServidor.getEndereco().getNumero());
			pstmt.setString(14, servidor.getCpf());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Ficha selectFicha(long codigoServidor) {
		String query = "SELEC FROM ficha WHERE " + codigoServidor + "=codigo_servidor;";
		Ficha fichaServidor = null;
		conn = DriveManager.getConnection();

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setLong(1, codigoServidor);
			rs = pstmt.executeQuery();
			fichaServidor = new Ficha();
			fichaServidor.setNomePai(rs.getString("nome_pai"));
			fichaServidor.setNomeMae(rs.getString("nome_mae"));
			fichaServidor.setSexo(rs.getString("sexo"));
			fichaServidor.setEstadoCivil(rs.getString("estado_civil"));
			fichaServidor.setTelefone(rs.getString("telefone"));
			Endereco enderecoServidor = new Endereco();
			enderecoServidor.setCidadeNatal(rs.getString("naturalidade"));
			enderecoServidor.setEstado(rs.getString(""));
			enderecoServidor.setCidadeAtual(rs.getString(""));
			enderecoServidor.setCep(rs.getString(""));
			enderecoServidor.setBairro(rs.getString(""));
			enderecoServidor.setRua(rs.getString(""));
			enderecoServidor.setNumero(rs.getInt("numero_rua"));

			fichaServidor.setEndereco(enderecoServidor);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DriveManager.close();

		return fichaServidor;
	}

	private void saveFicha(Servidor servidor) {

		String saveFichaServidor = "INSERT INTO ficha (codigo_servidor, nome_pai, nome_mae,"
				+ "sexo, cidade_natal, estado_civil, telefone, estado, cidade_atual, cep, bairro, rua, numero_rua)"
				+ "values(?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?,?);";
		conn = DriveManager.getConnection();

		try {
			pstmt = conn.prepareStatement(saveFichaServidor);
			Ficha fichaServidor = servidor.getFicha();

			pstmt.setString(1, servidor.getCpf());
			pstmt.setString(2, fichaServidor.getNomePai());
			pstmt.setString(3, fichaServidor.getNomeMae());
			pstmt.setString(4, fichaServidor.getSexo());
			pstmt.setString(5, fichaServidor.getEndereco().getCidadeNatal());
			pstmt.setString(6, fichaServidor.getEstadoCivil());
			pstmt.setString(7, fichaServidor.getTelefone());
			pstmt.setString(8, fichaServidor.getEndereco().getEstado());
			pstmt.setString(9, fichaServidor.getEndereco().getCidadeAtual());
			pstmt.setString(10, fichaServidor.getEndereco().getCep());
			pstmt.setString(11, fichaServidor.getEndereco().getBairro());
			pstmt.setString(12, fichaServidor.getEndereco().getRua());
			pstmt.setInt(13, fichaServidor.getEndereco().getNumero());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean deleteFicha(String codigoServidor) {
		String query = "DELETE FROM ficha WHERE codigo_servidor=?;";
		conn = DriveManager.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codigoServidor);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean deleteConvenioServidor(String codigoServidor) {
		String query = "Delete FROM convenio_servidor WHERE codigo_servidor=?;";
		conn = DriveManager.getConnection();

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codigoServidor);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
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

	public void saveConvenio(Convenio convenio, Servidor servidor) {
		String query1 = "INSERT INTO convenio_servidor (codigo_servidor, codigo_convenio, dependentes,data_adesao)"
				+ "values(?, ?, ?, ?);";
		String query2 = "SELECT codigo FROM convenio WHERE nome=?;";

		conn = DriveManager.getConnection();
		String codigo_servidor = servidor.getCpf();
		int codigo_convenio = 0;

		try {
			pstmt = conn.prepareStatement(query2);
			pstmt.setString(1, convenio.getNome());
			rs = pstmt.executeQuery();
			codigo_convenio = rs.getInt("codigo");

			pstmt.clearBatch();
			pstmt = conn.prepareStatement(query1);
			pstmt.setString(1, codigo_servidor);
			pstmt.setInt(1, codigo_convenio);
			pstmt.setInt(3, servidor.getQtdDependentes());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void saveConvenios(List<Convenio> convenios, Servidor s) {
		for (Convenio c : convenios) {
			this.saveConvenio(c, s);
		}
	}

	/*
	 * recupera todos os convenios relacionados ao servidor
	 */
	public List<Convenio> getAllConvenios(Servidor servidor) {
		/*
		 * primeiro é efetuada uma busca dos codigos dos convenios do servidor após é
		 * efetuada a busca dos convenios em si
		 */
		String query1 = "SELECT codigo_convenio FROM convenio_servidor WHERE  codigo_servidor=?;";
		conn = DriveManager.getConnection();
		List<Convenio> convenios = null;
		List<Integer> codigos_convenio = new ArrayList<Integer>();

		try {
			pstmt = conn.prepareStatement(query1);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				codigos_convenio.add(rs.getInt("codigo_convenio"));
			}

			convenios = new ArrayList<Convenio>();
			ConvenioDB cb = new ConvenioDB();
			for (Integer codigo : codigos_convenio) {
				convenios.add(cb.select(codigo));
			}

		} catch (SQLException e) {
			DriveManager.close();
			e.printStackTrace();
		}
		DriveManager.close();
		return convenios;
	}

}
