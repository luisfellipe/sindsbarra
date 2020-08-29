package dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import model.Data;
import model.Pessoa;
import model.Servidor;

public class Leitor {

	private CSVReader csvReader;
	private FileInputStream fis;
	private InputStreamReader isr;
	private static int alocados;

	private void open(String path) {

		try {
			fis = new FileInputStream(path);
			isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
			csvReader = new CSVReader(isr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			fis.close();
			isr.close();
			csvReader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] read(String path, int indice) {
		this.open(path);
		String[] linha = null;

		try {
			while (((linha = csvReader.readNext()) != null) && (indice > 0)) {
				indice--;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.close();
		return linha;
	}

	public List<Pessoa> readAll(String path) {
		this.open(path);
		String[] nextLine = null;
		List<Pessoa> lista = new ArrayList<>(50);
		try {
			Servidor servidor;

			while ((nextLine = csvReader.readNext()) != null) {

				servidor = new Servidor();
				servidor.setMatricula(nextLine[0]);
				servidor.setNome(nextLine[1]);
				servidor.setFuncao(nextLine[2]);

				Data dataManager = new Data();
				servidor.setDataAdmissao(dataManager.getLocalDate(nextLine[3]));
				servidor.setDataNasc(dataManager.getLocalDate(nextLine[4]));

			}
		} catch (CsvValidationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (

		IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.close();
		return lista;
	}

}
