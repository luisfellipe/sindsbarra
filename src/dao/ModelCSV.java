package dao;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Convenio;
import model.Data;
import model.Endereco;
import model.Ficha;
import model.Servidor;
import model.ServidorConvenio;

public class ModelCSV {

	public List<ServidorConvenio> importservidorConvenio() {

		return null;
	}

	public void exportServidorConvenio(List<ServidorConvenio> scList) {
		String csvHeader = "cpf;nome_convenio;";
	}

	public List<Convenio> importConvenios() {
		CSVReader csvReader = getCSVReader(getFile("Lista de Convenios" + LocalDate.now() + ".csv"));
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
		for (String[] line : lines) {
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
		allLines.add("nome;valor;data_adesao;descricao".split(";"));

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

		String nomeArquivo = "";
		CSVWriter csvWriter = getCSVWriter(getFile("Lista de Convenios" + LocalDate.now() + ".csv"));
		csvWriter.writeAll(allLines);
		closeCSVWriter(csvWriter);
	}

	public List<Servidor> importServidores() {

		return null;
	}

	public void exportServidores(List<Servidor> sList) {
		List<String[]> allLines = new ArrayList<String[]>(sList.size() + 1);

		allLines.add(("cpf;matricula;nome;rg;funcao;data_admissao;data_nasc;dependentes;estado_civil;nome_mae;"
				+ "nome_pai;sexo;telefone;cidade_natal;cep;cidade_atual;estado;bairro;rua;numero;").split(";"));

		StringBuilder line = null;
		Data data = new Data();
		for (Servidor s : sList) {
			System.out.println(s.toString());
			line = new StringBuilder().append(s.getCpf()).append(";").append(s.getMatricula()).append(";")
					.append(s.getNome()).append(";").append(s.getRg()).append(";").append(s.getFuncao()).append(";")
					.append(data.getStringDate(s.getDataAdmissao())).append(";")
					.append(data.getStringDate(s.getDataNasc())).append(";")
					.append(s.getQtdDependentes()).append(";");
			Ficha f = s.getFicha();
			line.append((f.getEstadoCivil() != null? f.getEstadoCivil():"")).append(";")
			.append(f.getNomeMae()).append(";").append(f.getNomePai())
					.append(";").append((f.getSexo()!=null? f.getSexo():"")).append(";").append(f.getTelefone()).append(";");
			Endereco e = f.getEndereco();
			line.append(e.getCidadeNatal()).append(";").append(e.getCep()).append(";").append(e.getCidadeAtual())
					.append(";").append(e.getEstado()).append(";").append(e.getBairro()).append(";").append(e.getRua())
					.append(";").append(e.getNumero()).append(";");

			allLines.add(line.toString().split(";"));
		}
		CSVWriter csvWriter = getCSVWriter(getFile("Lista de Servidores" + LocalDate.now() + ".csv"));
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

	private File getFile(String fileName) {
		DirectoryChooser dirChooser = new DirectoryChooser();
		Stage stage = new Stage();
		stage.centerOnScreen();
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Salvar arquivo");
		dirChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		String pathname = dirChooser.showDialog(stage).getAbsolutePath() + "/" + fileName;
		File file = new File(pathname);

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
