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

	public void save(Servidor servidor) {
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
			/*
			 * { Alert a = new Alert(AlertType.INFORMATION); a.setHeaderText("ID: " +
			 * servidor.getCpf()); a.setContentText("Servidor adicionado!!"); a.show(); }
			 */
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao salvar servidor!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
	}

	public Servidor select(String codigo) {
		String query = "SELECT * FROM servidor WHERE cpf=? OR matricula=? OR nome=?";
		Servidor s = null;
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codigo);
			pstmt.setString(2, codigo);
			pstmt.setString(3, codigo);
			rs = pstmt.executeQuery();

			s = new Servidor();
			while (rs.next()) {
				s.setCpf(rs.getString("cpf"));
				s.setRG(rs.getString("rg"));
				// Converte Date em LocalDate
				Data dataManager = new Data();
				s.setDataAdmissao(dataManager.getLocalDate(rs.getDate("data_admissao")));
				s.setDataNasc(dataManager.getLocalDate(rs.getDate("data_nasc")));
				s.setFuncao(rs.getString("funcao"));
				s.setNome(rs.getString("nome"));
				s.setMatricula(rs.getString("matricula"));
				s.setQtdDependentes(rs.getInt("dependentes"));

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
				s.setFicha(fichaServidor);
			}
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * Deleta o servidor do banco de dados junto com todas as suas informações de
	 * convenios.
	 * 
	 * @param cpf: utilizado como chave primária
	 * @return numero de linhas alteradas no banco de dados
	 */
	public int delete(String cpf) {
		String query = "DELETE FROM servidor WHERE cpf=?";
		conn = DataBase.getConnection();
		int rowsDeleted = 0;
		try {
			// Deleta convenios do servidor
			deleteConvenioServidor(cpf);// apos remover convenios a conexão é fechada
			conn = DataBase.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, cpf);
			rowsDeleted = pstmt.executeUpdate();
			DataBase.close();
			{
				Alert a = new Alert(AlertType.INFORMATION);
				a.setHeaderText("Servidor deletado!");
				a.setContentText("Todas as informações referentes ao servidor " + cpf + " foram deletadas!!");
				a.show();
			}
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao tentar deletar servidor: " + cpf + "\n" + e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}

		return rowsDeleted;
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
			Data dataManager = new Data();
			pstmt.setDate(7, dataManager.getDate(servidor.getDataAdmissao()));
			pstmt.setDate(8, dataManager.getDate(servidor.getDataNasc()));

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

			pstmt.execute();
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao tentar atualizar servidor");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
			return false;
		}
		{
			Alert a = new Alert(AlertType.INFORMATION);
			a.setHeaderText("Servidor atualizado!!!");
			a.setContentText("Servidor " + servidor.getCpf() + " atualizado!!");
			a.show();
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

			Data dataManager = new Data();
			while (rs.next()) {
				servidor = new Servidor();
				servidor.setCpf(rs.getString("cpf"));
				servidor.setRG(rs.getString("rg"));
				
				// Converte Date em LocalDate
				servidor.setDataAdmissao(dataManager.getLocalDate(rs.getDate("data_admissao")));
				servidor.setDataNasc(dataManager.getLocalDate(rs.getDate("data_nasc")));
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
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
		return servidores;
	}

	public void saveAll(List<Servidor> servidores) {
		servidores.forEach(s -> {
			save(s);
		});
	}

	/// Convenio Servidor //////////////

	public void saveServidorConvenio(ServidorConvenio sc) {
		String query = "INSERT INTO servidor_convenio (codigo_servidor, codigo_convenio, data_adesao)"
				+ "values(?, ?, ?);";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, sc.getServidor().getCpf());
			pstmt.setString(2, sc.getConvenio().getNome());
			pstmt.setDate(3, new Data().getDate(sc.getConvenio().getDataAdesao()));
			pstmt.executeUpdate();
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao salvar convenio!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
	}

	public List<ServidorConvenio> getAllServidorConvenio(Servidor servidor) {
		String query = "SELECT codigo_convenio FROM servidor_convenio WHERE  codigo_servidor=?;";
		conn = DataBase.getConnection();
		List<ServidorConvenio> sclist = null;

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, servidor.getCpf());
			rs = pstmt.executeQuery();
			sclist = new ArrayList<ServidorConvenio>();
			ConvenioDB cvDB = new ConvenioDB();
			ServidorConvenio sc = null;
			while (rs.next()) {
				sc = new ServidorConvenio(servidor, cvDB.select(rs.getString("codigo_convenio")));
				sclist.add(sc);
			}
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
		return sclist;
	}

	public List<ServidorConvenio> getAllServidorConvenio(Convenio convenio) {
		String query = "SELECT codigo_servidor FROM servidor_convenio WHERE  codigo_convenio=?;";
		conn = DataBase.getConnection();
		List<ServidorConvenio> sclist = null;

		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, convenio.getNome());
			rs = pstmt.executeQuery();
			sclist = new ArrayList<ServidorConvenio>();
			ServidorDB sDB = new ServidorDB();
			ServidorConvenio sc = null;
			while (rs.next()) {
				sc = new ServidorConvenio(sDB.select(rs.getString("codigo_servidor")), convenio);
				sclist.add(sc);
			}
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
		return sclist;
	}

	public void saveAllServidorConvenio(List<ServidorConvenio> sclist) {
		sclist.forEach(a -> saveServidorConvenio(a));
	}

	private boolean deleteConvenioServidor(String codigoServidor) {
		String query = "DELETE FROM servidor_convenio WHERE codigo_servidor=?;";
		conn = DataBase.getConnection();
		try {
			pstmt = conn.prepareStatement(query);
			pstmt.setString(1, codigoServidor);
			pstmt.executeUpdate();
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
			return false;
		}
		return true;
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
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public List<ServidorConvenio> getAllServidorConvenio() {
		String query = "SELECT * FROM servidor_convenio;";
		conn = DataBase.getConnection();
		List<ServidorConvenio> sclist = null;

		try {
			pstmt = conn.prepareStatement(query);
			rs = pstmt.executeQuery();

			sclist = new ArrayList<ServidorConvenio>();
			ServidorDB sdb = new ServidorDB();
			ConvenioDB cdb = new ConvenioDB();
			ServidorConvenio sc = null;
			Servidor servidor = null;
			Convenio convenio = null;
			while (rs.next()) {
				servidor = sdb.select(rs.getString("codigo_servidor"));
				convenio = cdb.select(rs.getString("codigo_convenio"));
				sc = new ServidorConvenio(servidor, convenio);
				sclist.add(sc);
			}
			DataBase.close();
		} catch (SQLException e) {
			{
				Alert a = new Alert(AlertType.ERROR);
				a.setHeaderText("Falha ao acessar banco de dados!!");
				a.setContentText(e.getMessage());
				a.show();
			}
			e.printStackTrace();
		}
		return sclist;
	}
}
