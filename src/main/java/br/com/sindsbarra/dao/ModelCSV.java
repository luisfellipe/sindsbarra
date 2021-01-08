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

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

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
	private String conveniosHeader = "nome;valor;data adesao;descricao";
	private String servconvHeader = "cpf;nome convenio";

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
		String[] nextLine;

		List<ServidorConvenio> sclist = new ArrayList<ServidorConvenio>(lines.size());

		ServidorConvenio serv_conv = null;
		Servidor servidor;
		Convenio convenio;
		ServidorDB sDB = new ServidorDB();
		ConvenioDB cDB = new ConvenioDB();
		boolean first = true;
		for (String[] line : lines) {
			if (first) {
				first = false;
				continue;
			}
			servidor = sDB.select(line[0]);
			convenio = cDB.select(line[1]);
			serv_conv = new ServidorConvenio(servidor, convenio);
			sclist.add(serv_conv);
		}
		return sclist;
	}

	public void exportServidorConvenio(List<ServidorConvenio> scList) {
		List<String[]> allLines = new ArrayList<String[]>(scList.size() + 1);
		allLines.add(servconvHeader.toUpperCase().split(";"));

		Data data = new Data();
		String line[] = null;
		for (ServidorConvenio sc : scList) {
			line = new String[2];
			line[0] = sc.getServidor().getCpf();
			line[1] = sc.getNome();
			allLines.add(line);
		}
		CSVWriter csvWriter = getCSVWriter(
				chooserDirectory("Cadastro Convenio dos Servidores" + LocalDate.now() + ".csv"));
		csvWriter.writeAll(allLines);
		closeCSVWriter(csvWriter);
	}

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
		String[] nextLine;

		List<Convenio> convList = new ArrayList<Convenio>(lines.size());

		Convenio convenio = null;
		boolean first = true;
		for (String[] line : lines) {
			if (first) {
				first = false;
				continue;
			}
			convenio = new Convenio();
			convenio.setNome(line[0]);
			convenio.setValor(Double.parseDouble(line[1]));
			convenio.setDataAdesao(new Data().getLocalDate(line[2]));
			convenio.setDescricao(line[3]);
			convList.add(convenio);
		}
		return convList;
	}

	public void exportConvenios(List<Convenio> cList) {

		List<String[]> allLines = new ArrayList<String[]>(cList.size() + 1);
		allLines.add(conveniosHeader.toUpperCase().split(";"));

		Data data = new Data();
		String line[] = null;
		for (Convenio c : cList) {
			line = new String[4];
			line[0] = c.getNome();
			line[1] = c.getValor().toString();
			line[2] = data.getStringDate(c.getDataAdesao());
			line[3] = c.getDescricao();
			allLines.add(line);
		}
		CSVWriter csvWriter = getCSVWriter(chooserDirectory("Lista de Convenios" + LocalDate.now() + ".csv"));
		csvWriter.writeAll(allLines);
		closeCSVWriter(csvWriter);
	}

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

		List<Servidor> servidoresListista = new ArrayList<Servidor>(lines.size());
		Servidor servidor = null;
		Data data = new Data();
		String[] line = null;

		Iterator<String[]> it = lines.iterator();
		it.next();
		synchronized (it) {
			while (it.hasNext()) {
				line = it.next();
				toStrings(line);
				servidor = new Servidor();
				servidor.setCpf(line[0].trim());
				servidor.setMatricula(line[1].trim());
				servidor.setNome(line[2].trim());
				servidor.setRG(line[3].trim());
				servidor.setFuncao(line[4].trim());
				servidor.setDataAdmissao(data.getLocalDate(line[5].trim()));
				servidor.setDataNasc(data.getLocalDate(line[6].trim()));
				servidor.setQtdDependentes(new Data().isNumeric(line[7].trim())?Integer.parseInt(line[7].trim()): 0);

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
				endereco.setNumero(new Data().isNumeric(line[19].trim())?Integer.parseInt(line[19].trim()): null);
				ficha.setEndereco(endereco);
				servidor.setFicha(ficha);
				new ServidorDB().save(servidor);
				servidoresListista.add(servidor);
			}

		}
		
		return servidoresListista;
	}
	private void toStrings(String[] words) {
		for(String w: words) {
			System.out.print(w + " ");
		}
		System.out.println("\n");
	}

	public void exportServidores(List<Servidor> sList) {
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
	}

	///////////// opencsv//////////////

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

}
