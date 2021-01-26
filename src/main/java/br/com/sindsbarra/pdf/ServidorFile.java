package br.com.sindsbarra.pdf;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.WebColors;

import br.com.sindsbarra.Main;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Servidor;

public class ServidorFile {
	private String RESULT = null;;
	private PdfDocument pdfDoc = null;
	Document doc = null;
	PdfWriter pdfWriter = null;
	private Table table = null;

	public ServidorFile(final String RESULT) {
		this.RESULT = RESULT;
	}

	private void createPdf() {
		try {
			pdfWriter = new PdfWriter(RESULT);
			pdfDoc = new PdfDocument(pdfWriter);
			pdfDoc.setUserProperties(true);
			doc = new Document(pdfDoc);
			ImageData data = ImageDataFactory.create(new Main().LOGO);
			Image image = new Image(data);
			image.setHeight(119);
			image.setWidth(520);
			doc.add(image);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addServidor(List<Servidor> servidores) {
		createPdf();
		Text texto = new Text("RELAÇÃO DE SERVIDORES SINDICALIZADOS");
		Style style = new Style();
		style.setBold();
		style.setFontSize(14);
		style.setUnderline();
		texto.addStyle(style);

		doc.add(new Paragraph("\n"));
		Paragraph p1 = new Paragraph(texto);
		p1.setTextAlignment(TextAlignment.CENTER);
		doc.add(p1);
		doc.add(new Paragraph("\n"));

		float[] pointColumnWidths = { 50F, 160F, 130F, 30F, 50F, 80F };
		table = new Table(pointColumnWidths);
		table.setFontSize(10);// tamanho da fonte do conteudo da tabela

		Text matricula = new Text("Matricula");
		matricula.setFontSize(11);
		matricula.setBold();
		Cell c_matricula = new Cell();
		c_matricula.add(new Paragraph(matricula).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_matricula);

		Text nome = new Text("Nome");
		nome.setFontSize(11);
		nome.setBold();
		Cell c_nome = new Cell();
		c_nome.add(new Paragraph(nome).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_nome);

		Text funcao = new Text("Cargo/Função");
		funcao.setFontSize(11);
		funcao.setBold();
		Cell c_funcao = new Cell();
		c_funcao.add(new Paragraph(funcao).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_funcao);

		Text admissao = new Text("Admissão");
		admissao.setFontSize(11);
		admissao.setBold();
		admissao.setBold();
		Cell c_admissao = new Cell();
		c_admissao.add(new Paragraph(admissao).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_admissao);

		Text nascimento = new Text("Nascimento");
		nascimento.setFontSize(11);
		nascimento.setBold();
		Cell c_nascimento = new Cell();
		c_nascimento.add(new Paragraph(nascimento).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_nascimento);

		Text cpf = new Text("CPF");
		cpf.setFontSize(11);
		cpf.setBold();
		Cell c_cpf = new Cell();
		c_cpf.add(new Paragraph(cpf).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_cpf);

		int totalServidores = 0;

		Iterator<Servidor> it = servidores.iterator();
		Servidor serv = null;
		while (it.hasNext()) {
			serv = it.next();

			table.addCell(new Cell().add(new Paragraph(serv.getMatricula()).setTextAlignment(TextAlignment.CENTER)));
			table.addCell(serv.getNome());
			table.addCell(serv.getFuncao());
			table.addCell(new Data().getStringDate(serv.getDataAdmissao()));
			table.addCell(new Cell().add(new Paragraph(new Data().getStringDate(serv.getDataNasc()))
					.setTextAlignment(TextAlignment.CENTER)));
			table.addCell(new Cell().add(new Paragraph(serv.getCpf()).setTextAlignment(TextAlignment.CENTER)));

			totalServidores++;
			if (it.hasNext()) {
				table.startNewRow();
			}
		}

		doc.add(table);

		Text total = new Text("\nTOTAL DE " + totalServidores + " SINDICALIZADO(S)");
		Color color = WebColors.getRGBColor("yellow");
		style.setFontSize(11);
		style.setBackgroundColor(color);
		total.addStyle(style);
		Paragraph p2 = new Paragraph(total);
		p2.setTextAlignment(TextAlignment.LEFT);
		doc.add(p2);
		
		LocalDate data = LocalDate.now();
		StringBuilder sb = new StringBuilder();
		sb.append(data.getDayOfMonth()).append(" de ")
		.append(new Data().getMes(data.getMonthValue())).append(" de ").append(data.getYear() + ".");
		Paragraph p3 = new Paragraph(sb.toString());
		p3.setTextAlignment(TextAlignment.RIGHT);
		doc.add(p3);
		doc.close();
	}

}
