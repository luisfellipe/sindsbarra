package br.com.sindsbarra.dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Endereco;
import br.com.sindsbarra.models.Ficha;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.models.ServidorConvenio;

public class ModelCSV {

	private String servidoresHeader = "cpf;matricula;nome;rg;funcao;data_admissao;data_nascimento;dependentes;estado civil;sexo;nome_mae;"
			+ "nome_pai;telefone;estado;cidade natal;cidade atual;cep;bairro;rua;numero";
	private String conveniosHeader = "codigo;nome;valor;data adesao;descricao";
	private String servconvHeader = "convenio;codigo;cpf;data adesao;valor;";

	/**
	 * Importa ServidorConvenio para arquivo CSV
	 * 
	 * @return List<ServidorConvenio>
	 */
	public List<ServidorConvenio> importservidorConvenio() {

		CSVReader csvReader = getCSVReader(chooserFile());
		List<String[]> lines = null;
		try {
			lines = csvReader.readAll();
			closeCSVReader(csvReader);
		} catch (IOException | CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<ServidorConvenio> sclist = new ArrayList<ServidorConvenio>(lines.size());

		ServidorConvenio serv_conv = null;
		
		String line[] = null;
		Iterator<String[]> it = lines.iterator();
		it.next();
		
		Double valor = null;
		LocalDate dataAdesao = null;
		Data data = new Data();
		synchronized (it) {
			while (it.hasNext()) {
				line = it.next();
				dataAdesao = data.getLocalDate(line[3]);
				valor = Double.valueOf(line[4]);
				serv_conv = new ServidorConvenio(line[1],Long.parseLong(line[0]),line[2], dataAdesao, valor);
				sclist.add(serv_conv);
			}
		}
		return sclist;
	}

	/**
	 * Exporta ServidorConvenio para arquivo CSV
	 * 
	 * @param List<ServidorConvenio> scList
	 * @return true se ação ocorreu corretamente
	 */
	public boolean exportServidorConvenio(List<ServidorConvenio> scList) {
		if (!messagemDeAviso()) {
			return false;
		}
		List<String[]> allLines = new ArrayList<String[]>(scList.size() + 1);
		allLines.add(servconvHeader.toUpperCase().split(";"));

		Data data = new Data();
		String line[] = null;
		for (ServidorConvenio sc : scList) {
			line = new String[5];
			line[0] = sc.getCodigoConvenio()+"";
			line[1] = sc.getNome();
			line[2] = sc.getCpf();
			line[3] = data.getStringDate(sc.getDataAdesao());
			line[4] = sc.getValor()+"";
			allLines.add(line);
		}
		CSVWriter csvWriter = getCSVWriter(
				chooserDirectory("Cadastro Convenio dos Servidores" + LocalDate.now() + ".csv"));
		csvWriter.writeAll(allLines);
		closeCSVWriter(csvWriter);
		return true;
	}

	/**
	 * Importa Convenios de arquivo CSV
	 * 
	 * @return List<Convenio>
	 */
	public List<Convenio> importConvenios() {
		CSVReader csvReader = getCSVReader(chooserFile());
		List<String[]> lines = null;
		try {
			lines = csvReader.readAll();
			closeCSVReader(csvReader);
		} catch (IOException | CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Convenio> convList = new ArrayList<Convenio>(lines.size());

		String line[] = null;
		Convenio convenio = null;
		Iterator<String[]> it = lines.iterator();
		it.next();
		Data data = new Data();
		synchronized (it) {
			while (it.hasNext()) {
				line = it.next();
				convenio = new Convenio(Long.parseLong(line[0]));
				convenio.setNome(line[1]);
				convenio.setValor(Double.parseDouble(line[2]));
				convenio.setDataAdesao(data.getLocalDate(line[3]));
				convenio.setDescricao(line[4]);
				convList.add(convenio);
			}
		}
		return convList;
	}

	/**
	 * Exporta convenios para arquivo CSV
	 * 
	 * @param cList
	 * @return List<Convenio>
	 */
	public boolean exportConvenios(List<Convenio> cList) {
		if (!messagemDeAviso()) {
			return false;
		}
		List<String[]> allLines = new ArrayList<String[]>(cList.size() + 1);
		allLines.add(conveniosHeader.toUpperCase().split(";"));

		Data data = new Data();
		String line[] = null;
		for (Convenio c : cList) {
			line = new String[5];
			line[0] = c.getCodigo()+"";
			line[1] = c.getNome();
			line[2] = c.getValor().toString();
			line[3] = data.getStringDate(c.getDataAdesao());
			line[4] = c.getDescricao();
			allLines.add(line);
		}
		CSVWriter csvWriter = getCSVWriter(chooserDirectory("[Lista de Convenios]" + LocalDate.now() + ".csv"));
		csvWriter.writeAll(allLines);
		closeCSVWriter(csvWriter);
		return true;
	}

	/**
	 * importa Servidores de arquivo CSV
	 * 
	 * @return List<Servidor>
	 */
	public List<Servidor> importServidores() {
		CSVReader csvReader = getCSVReader(chooserFile());
		List<String[]> lines = null;
		try {
			lines = csvReader.readAll();
			closeCSVReader(csvReader);
		} catch (IOException | CsvException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Servidor> servidoresLista = new ArrayList<Servidor>(lines.size());
		Servidor servidor = null;
		Data data = new Data();
		String[] line = null;

		Iterator<String[]> it = lines.iterator();
		it.next();
		synchronized (it) {
			while (it.hasNext()) {
				line = it.next();
				servidor = new Servidor();
				servidor.setCpf(line[0].trim());
				servidor.setMatricula(line[1].trim());
				servidor.setNome(line[2].trim());
				servidor.setRG(line[3].trim());
				servidor.setFuncao(line[4].trim());
				servidor.setDataAdmissao(data.getLocalDate(line[5].trim()));
				servidor.setDataNasc(data.getLocalDate(line[6].trim()));
				servidor.setQtdDependentes(new Data().isNumeric(line[7].trim()) ? Integer.parseInt(line[7].trim()) : 0);

				Ficha ficha = new Ficha();
				ficha.setEstadoCivil(line[8].trim());
				ficha.setNomeMae(line[9].trim());
				ficha.setNomePai(line[10].trim());
				ficha.setSexo(line[11].trim());
				ficha.setTelefone(line[12].trim());

				Endereco endereco = new Endereco();
				endereco.setEstado(line[13].trim());
				endereco.setCidadeNatal(line[14].trim());
				endereco.setCidadeAtual(line[15].trim());
				endereco.setCep(line[16].trim());
				endereco.setBairro(line[17].trim());
				endereco.setRua(line[18].trim());
				endereco.setNumero(new Data().isNumeric(line[19].trim()) ? Integer.parseInt(line[19].trim()) : null);
				ficha.setEndereco(endereco);
				servidor.setFicha(ficha);
				new ServidorDB().save(servidor);
				servidoresLista.add(servidor);
			}

		}

		return servidoresLista;
	}

	/**
	 * Exporta Servidor para arquivo CSV
	 * 
	 * @param sList
	 * @return true se ação ocorreu corretamente
	 */
	public boolean exportServidores(List<Servidor> sList) {
		if (!messagemDeAviso()) {
			return false;
		}
		List<String[]> allLines = new ArrayList<String[]>(sList.size() + 1);
		allLines.add(servidoresHeader.toUpperCase().split(" ;"));

		StringBuilder line = null;
		Data data = new Data();
		for (Servidor servidor : sList) {
			line = new StringBuilder();
			line.append(servidor.getCpf()).append(" ;");
			line.append(servidor.getMatricula()).append(" ;");
			line.append(servidor.getNome()).append(" ;");
			line.append(servidor.getRg()).append(" ;");
			line.append(servidor.getFuncao()).append(" ;");
			line.append(data.getStringDate(servidor.getDataAdmissao())).append(" ;");
			line.append(data.getStringDate(servidor.getDataNasc())).append(" ;");
			line.append(servidor.getQtdDependentes()).append(" ;");

			Ficha ficha = servidor.getFicha();
			line.append((ficha.getEstadoCivil() != null ? ficha.getEstadoCivil() : "")).append(" ;");
			line.append((ficha.getSexo() != null ? ficha.getSexo() : "")).append(" ;");
			line.append(ficha.getNomeMae()).append(" ;");
			line.append(ficha.getNomePai()).append(" ;");
			line.append(ficha.getTelefone()).append(" ;");

			Endereco endereco = ficha.getEndereco();
			line.append(endereco.getEstado()).append(" ;");
			line.append(endereco.getCidadeNatal()).append(" ;");
			line.append(endereco.getCidadeAtual()).append(" ;");
			line.append(endereco.getCep()).append(" ;");
			line.append(endereco.getBairro()).append(" ;");
			line.append(endereco.getRua()).append(" ;");
			line.append(endereco.getNumero()).append(" ;");
			allLines.add(line.toString().split(";"));
		}
		CSVWriter csvWriter = getCSVWriter(chooserDirectory("Lista de Servidores" + LocalDate.now() + ".csv"));
		csvWriter.writeAll(allLines);
		closeCSVWriter(csvWriter);
		return true;
	}

	///////////// opencsv //////////////
	/**
	 * 
	 * @param file
	 * @return Escritor de arquivo CSV
	 */
	private CSVReader getCSVReader(File file) {

		FileReader fr = null;
		CSVReader csvReader = null;
		List<String[]> lines = null;
		try {
			fr = new FileReader(file, StandardCharsets.UTF_8);
			csvReader = new CSVReader(fr);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csvReader;
	}

	/**
	 * 
	 * @param file
	 * @return Escritor de arquivo CSV
	 */
	private CSVWriter getCSVWriter(File file) {
		FileWriter fr = null;
		CSVWriter csvWriter = null;

		try {
			fr = new FileWriter(file, StandardCharsets.UTF_8);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		csvWriter = new CSVWriter(fr);
		return csvWriter;
	}

	/**
	 * 
	 * @param fileName
	 * @return Diretorio selecionando
	 */
	private File chooserDirectory(String fileName) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		String pathname = null;

		if (fileName != null)
			pathname = dirChooser.showDialog(stage).getAbsolutePath() + "/" + fileName;
		else
			pathname = dirChooser.showDialog(stage).getAbsolutePath();
		File file = new File(pathname);

		return file;
	}

	/**
	 * 
	 * @return Arquivo escolhido
	 */
	private File chooserFile() {
		FileChooser fileChooser = new FileChooser();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Salvar arquivo");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		File file = (File) fileChooser.showOpenDialog(stage);
		return file;
	}

	/**
	 * Fecha o CSVWriter
	 * 
	 * @param csvWriter:CSVWriter
	 */
	private void closeCSVWriter(CSVWriter csvWriter) {
		try {
			csvWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Fecha o CSVReader
	 * 
	 * @param csvReader:CSVReader
	 */
	private void closeCSVReader(CSVReader csvReader) {
		try {
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Exibi menssagem de aviso ao salvar arquivo CSV
	 * 
	 * @return true se usuario deseja continuar
	 */
	private boolean messagemDeAviso() {
		Alert a = new Alert(AlertType.CONFIRMATION);
		a.setHeaderText("Antes de exportas os dados em CSV");
		a.setContentText("Todos os dados devem estar preenchidos para que possam ser importados futuramente!"
				+ "\nApos exportados não altere o arquivo!!");
		Optional<ButtonType> result = a.showAndWait();

		if (result.get() == ButtonType.CANCEL) {
			return false;
		}
		return true;
	}

}
