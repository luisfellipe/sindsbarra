package br.com.sindsbarra.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Endereco;
import br.com.sindsbarra.models.Ficha;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.models.ServidorConvenio;

public class ServidorDB {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	Data data = null;
	Alert alertError = null;
	Alert alertSuccess = null;

	public ServidorDB() {
		alertError = new Alert(AlertType.ERROR);
		alertError.setHeaderText("Falha ao acessar banco de dados!!");
		alertSuccess = new Alert(AlertType.INFORMATION);
		data = new Data();
	}

	public boolean save(Servidor servidor) {
		String saveServidor = "INSERT INTO servidor (nome, cpf, rg, matricula,funcao, dependentes, data_admissao, data_nasc, nome_pai, nome_mae, "
				+ "sexo, cidade_natal, estado_civil, telefone, estado, cidade_atual, cep, bairro, rua, numero_rua)"
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?);";
		conn = DataBase.getConnection();

		try {
			pstmt = conn.prepareStatement(saveServidor);

			pstmt.setString(1, servidor.getNome());
			pstmt.setString(2, servidor.getCpf());
			pstmt.setString(3, servidor.getRg());
			pstmt.setString(4, servidor.getMatricula());
			pstmt.setString(5, servidor.getFuncao());
			pstmt.setInt(6, servidor.getQtdDependentes());
			{// TODO: verificar se a entrada é valida se não, exibir janela com mensagem de
				// erro
				Data dataManager = new Data();
				if (servidor.getDataAdmissao() != null)
					pstmt.setDate(7, dataManager.getDate(servidor.getDataAdmissao()));
				else
					pstmt.setDate(7, dataManager.getDate(LocalDate.now()));
				if (servidor.getDataNasc() != null)
					pstmt.setDate(8, new Data().getDate(servidor.getDataNasc()));
				else
					pstmt.setDate(8, dataManager.getDate(LocalDate.now()));
			}

			Ficha fichaServidor = servidor.getFicha();

			pstmt.setString(9, fichaServidor.getNomePai());
			pstmt.setString(10, fichaServidor.getNomeMae());
			pstmt.setString(11, fichaServidor.getSexo());
			pstmt.setString(12, fichaServidor.getEndereco().getCidadeNatal());
			pstmt.setString(13, fichaServidor.getEstadoCivil());
			pstmt.setString(14, fichaServidor.getTelefone());
			pstmt.setString(15, fichaServidor.getEndereco().getEstado());
			pstmt.setString(16, fichaServidor.getEndereco().getCidadeAtual());
			pstmt.setString(17, fichaServidor.getEndereco().getCep());
			pstmt.setString(18, fichaServidor.getEndereco().getBairro());
			pstmt.setString(19, fichaServidor.getEndereco().getRua());
			pstmt.setInt(20, fichaServidor.getEndereco().getNumero());
			pstmt.execute();
			DataBase.close();
		} catch (SQLException e) {
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Servidor select(String codigoServidor) {
		String query = "SELECT * FROM servidor WHERE cpf=? OR matricula=? OR nome=?";
		Servidor servidor = null;
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codigoServidor);
			pstmt.setString(2, codigoServidor);
			pstmt.setString(3, codigoServidor);
			rs = pstmt.executeQuery();

			servidor = new Servidor();
			while (rs.next()) {
				servidor.setCpf(rs.getString("cpf"));
				servidor.setRG(rs.getString("rg"));
				// Converte Date em LocalDate
				servidor.setDataAdmissao(data.getLocalDate(rs.getDate("data_admissao")));
				servidor.setDataNasc(data.getLocalDate(rs.getDate("data_nasc")));
				servidor.setFuncao(rs.getString("funcao"));
				servidor.setNome(rs.getString("nome"));
				servidor.setMatricula(rs.getString("matricula"));
				servidor.setQtdDependentes(rs.getInt("dependentes"));

				Ficha fichaServidor = new Ficha();
				fichaServidor.setNomePai(rs.getString("nome_pai"));
				fichaServidor.setNomeMae(rs.getString("nome_mae"));
				fichaServidor.setSexo(rs.getString("sexo"));
				fichaServidor.setEstadoCivil(rs.getString("estado_civil"));
				fichaServidor.setTelefone(rs.getString("telefone"));

				Endereco enderecoServidor = new Endereco();
				enderecoServidor.setCidadeNatal(rs.getString("cidade_natal"));
				enderecoServidor.setEstado(rs.getString("estado"));
				enderecoServidor.setCidadeAtual(rs.getString("cidade_atual"));
				enderecoServidor.setCep(rs.getString("cep"));
				enderecoServidor.setBairro(rs.getString("bairro"));
				enderecoServidor.setRua(rs.getString("rua"));
				enderecoServidor.setNumero(rs.getInt("numero_rua"));
				fichaServidor.setEndereco(enderecoServidor);
				servidor.setFicha(fichaServidor);
			}
			DataBase.close();
		} catch (SQLException e) {
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
		}
		return servidor;
	}

	/**
	 * Deleta o servidor do banco de dados junto com todas as suas informações de
	 * convenios.
	 * 
	 * @param cpf: utilizado como chave primária
	 * @return numero de linhas alteradas no banco de dados
	 */
	public boolean delete(String cpf) {
		String query = "DELETE FROM servidor WHERE cpf=?";
		conn = DataBase.getConnection();
		try {
			// Deleta convenios do servidor
			if (deleteConvenioServidor(cpf)) {// apos remover convenios a conexão é fechada
				conn = DataBase.getConnection();
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, cpf);
				pstmt.executeUpdate();
			}
			DataBase.close();
		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao tentar deletar servidor: " + cpf + "\n" + e.getMessage());
			alertError.show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean update(Servidor servidor) {
		String query = "UPDATE servidor SET nome=?, cpf=?, rg=?, matricula=?, funcao=?, dependentes=?, data_admissao=?, data_nasc=?, "
				+ "nome_pai=?, nome_mae=?, sexo=?, cidade_natal=?, estado_civil=?, telefone=?, estado=?, cidade_atual=?, cep=?, bairro=?, rua=?, numero_rua=?  WHERE cpf=?";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, servidor.getNome());
			pstmt.setString(2, servidor.getCpf());
			pstmt.setString(3, servidor.getRg());
			pstmt.setString(4, servidor.getMatricula());
			pstmt.setString(5, servidor.getFuncao());
			pstmt.setInt(6, servidor.getQtdDependentes());
			pstmt.setDate(7, data.getDate(servidor.getDataAdmissao()));
			pstmt.setDate(8, data.getDate(servidor.getDataNasc()));

			Ficha fichaServidor = servidor.getFicha();
			pstmt.setString(9, fichaServidor.getNomePai());
			pstmt.setString(10, fichaServidor.getNomeMae());
			pstmt.setString(11, fichaServidor.getSexo());
			pstmt.setString(12, fichaServidor.getEndereco().getCidadeNatal());
			pstmt.setString(13, fichaServidor.getEstadoCivil());
			pstmt.setString(14, fichaServidor.getTelefone());
			pstmt.setString(15, fichaServidor.getEndereco().getEstado());
			pstmt.setString(16, fichaServidor.getEndereco().getCidadeAtual());
			pstmt.setString(17, fichaServidor.getEndereco().getCep());
			pstmt.setString(18, fichaServidor.getEndereco().getBairro());
			pstmt.setString(19, fichaServidor.getEndereco().getRua());
			pstmt.setInt(20, fichaServidor.getEndereco().getNumero());
			pstmt.setString(21, servidor.getCpf());

			pstmt.executeUpdate();
			DataBase.close();
		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao tentar atualizar servidor");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<Servidor> selectAll() {
		String selectServidores = "SELECT * FROM servidor;";
		conn = DataBase.getConnection();
		List<Servidor> servidores = null;

		try {
			pstmt = conn.prepareStatement(selectServidores);
			rs = pstmt.executeQuery();

			servidores = new ArrayList<Servidor>();
			Servidor servidor = null;
			while (rs.next()) {
				servidor = new Servidor();
				servidor.setCpf(rs.getString("cpf"));
				servidor.setRG(rs.getString("rg"));

				// Converte Date em LocalDate
				servidor.setDataAdmissao(data.getLocalDate(rs.getDate("data_admissao")));
				servidor.setDataNasc(data.getLocalDate(rs.getDate("data_nasc")));
				servidor.setFuncao(rs.getString("funcao"));
				servidor.setNome(rs.getString("nome"));
				servidor.setMatricula(rs.getString("matricula"));
				servidor.setQtdDependentes(rs.getInt("dependentes"));

				Ficha fichaServidor = new Ficha();
				fichaServidor.setNomePai(rs.getString("nome_pai"));
				fichaServidor.setNomeMae(rs.getString("nome_mae"));
				fichaServidor.setSexo(rs.getString("sexo"));
				fichaServidor.setEstadoCivil(rs.getString("estado_civil"));
				fichaServidor.setTelefone(rs.getString("telefone"));

				Endereco enderecoServidor = new Endereco();
				enderecoServidor.setCidadeNatal(rs.getString("cidade_natal"));
				enderecoServidor.setEstado(rs.getString("estado"));
				enderecoServidor.setCidadeAtual(rs.getString("cidade_atual"));
				enderecoServidor.setCep(rs.getString("cep"));
				enderecoServidor.setBairro(rs.getString("bairro"));
				enderecoServidor.setRua(rs.getString("rua"));
				enderecoServidor.setNumero(rs.getInt("numero_rua"));
				fichaServidor.setEndereco(enderecoServidor);
				servidor.setFicha(fichaServidor);
				servidores.add(servidor);
			}
			DataBase.close();

		} catch (SQLException e) {
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
		}
		return servidores;
	}

	/**
	 * Salva todos servidores da lista no Banco de Dados
	 * 
	 * @param servidores
	 */
	public void saveAll(List<Servidor> servidores) {
		servidores.forEach(s -> {
			save(s);
		});
	}

	public boolean deleteConvenio(String codigo) {
		String query = "DELETE FROM servidor_convenio WHERE codigo_convenio=?;";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codigo);
			pstmt.executeUpdate();
			DataBase.close();
		} catch (SQLException e) {
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Salva lista de ServidorConvenio no Banco de Dados
	 * 
	 * @param sclist
	 */
	public void saveAllServidorConvenio(List<ServidorConvenio> sclist) {
		sclist.forEach(a -> saveServidorConvenio(a));
	}

	private List<ServidorConvenio> getAllServidorConvenio(String query) {
		conn = DataBase.getConnection();
		List<ServidorConvenio> sclist = null;

		try {
			System.out.println(query);
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();
			sclist = new ArrayList<ServidorConvenio>(5);

			ServidorDB sDB = new ServidorDB();
			ConvenioDB cvDB = new ConvenioDB();
			Data data = new Data();
			Servidor s = null;
			Convenio c = null;
			ServidorConvenio sc = null;
			Double valor = null;
			while (rs.next()) {
				s = sDB.select(rs.getString("codigo_servidor"));
				c = cvDB.select(rs.getLong("codigo_convenio"));
				/*
				 * Calcula valor total do convenio do servidor
				 */
				if (c.isDependentesInlude()) {
					valor = Double.valueOf(s.getQtdDependentes() * c.getValor());
				} else
					valor = Double.valueOf(c.getValor());
				sc = new ServidorConvenio(c.getNome(), c.getCodigo(),s.getCpf(), data.getLocalDate(rs.getDate("data_adesao")), valor);
				sclist.add(sc);
			}
			DataBase.close();
		} catch (SQLException e) {
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
		}
		return sclist;
	}

	public List<ServidorConvenio> getAllServidorConvenio(Convenio convenio) {
		String query = "SELECT * FROM servidor_convenio WHERE  codigo_convenio='" + convenio.getCodigo() + "';";
		return getAllServidorConvenio(query);
	}

	public List<ServidorConvenio> getAllServidorConvenio(Servidor servidor) {
		String query = "SELECT * FROM servidor_convenio WHERE  codigo_servidor='" + servidor.getCpf() + "';";
		return getAllServidorConvenio(query);
	}

	public List<ServidorConvenio> getAllServidorConvenio() {
		String query = "SELECT * FROM servidor_convenio";
		return getAllServidorConvenio(query);
	}
	/// Convenio Servidor //////////////

	public boolean saveServidorConvenio(ServidorConvenio sc) {
		String query = "INSERT INTO servidor_convenio (codigo_servidor, codigo_convenio, data_adesao)"
				+ "values(?, ?, ?);";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, sc.getCpf());
			pstmt.setLong(2, sc.getCodigoConvenio());
			pstmt.setDate(3, new Data().getDate(sc.getDataAdesao()));
			pstmt.executeUpdate();
			DataBase.close();
		} catch (SQLException e) {
			alertError.setHeaderText("Falha ao salvar convenio!!");
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean deleteConvenioServidor(String codigoServidor) {
		String query = "DELETE FROM servidor_convenio WHERE codigo_servidor='" + codigoServidor + "';";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.executeUpdate();
			DataBase.close();
		} catch (SQLException e) {
			alertError.setContentText(e.getMessage());
			alertError.show();
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
