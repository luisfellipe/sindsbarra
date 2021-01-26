package br.com.sindsbarra.pdf;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
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

import br.com.sindsbarra.Main;
import br.com.sindsbarra.dao.ConvenioDB;
import br.com.sindsbarra.dao.ServidorDB;
import br.com.sindsbarra.models.Convenio;
import br.com.sindsbarra.models.Data;
import br.com.sindsbarra.models.Servidor;
import br.com.sindsbarra.models.ServidorConvenio;

public class ConvenioFile {
	private String RESULT = null;
	private PdfDocument pdfDoc = null;
	Document doc = null;
	PdfWriter pdfWriter = null;
	private Table table = null;

	public ConvenioFile(final String RESULT) {
		this.RESULT = RESULT;
	}

	private void createPdf() {
		try {
			pdfWriter = new PdfWriter(RESULT);
			pdfDoc = new PdfDocument(pdfWriter);
			doc = new Document(pdfDoc);
			ImageData data = ImageDataFactory.create(new Main().LOGO);
			Image image = new Image(data);
			image.setHeight(119);
			image.setWidth(520);
			doc.add(image);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addConvenioServidor(List<ServidorConvenio> scList) {
		createPdf();
		
		ServidorDB sDB = new ServidorDB();
		ConvenioDB cDB = new ConvenioDB();
		Servidor servidor = null;
		Convenio convenio = cDB.select(scList.get(0).getCodigoConvenio());

		Text texto = new Text(scList.get(0).getNome()+"\n");
		Style style = new Style();
		style.setBold();
		style.setFontSize(14);
		style.setUnderline();
		texto.addStyle(style);
		
		doc.add(new Paragraph("\n"));
		Paragraph p = new Paragraph(texto);
		p.setTextAlignment(TextAlignment.CENTER);
		doc.add(p);


		if(convenio.isDependentesInlude()) {
			 float[] pointColumnWidths = { 70F, 230F, 15F, 15F, 15F, 60F, 90F };
			 table = new Table(pointColumnWidths);
		}else {
			float[] pointColumnWidths = { 80F, 280F, 80F, 30F };
			table = new Table(pointColumnWidths);
		}
		
		table.setFontSize(10);
		
		Text matricula = new Text("MATRICULA");
		matricula.setBold();
		Cell c_matricula = new Cell();
		c_matricula.add(new Paragraph(matricula).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_matricula);

		Text nome = new Text("NOME");
		nome.setBold();
		Cell c_nome = new Cell();
		c_nome.add(new Paragraph(nome).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_nome);

		if(convenio.isDependentesInlude()) {
			Text dependentes = new Text("Dependentes");
			dependentes.setBold();
			dependentes.setBold();
			Cell cell_dep = new Cell(0,3);
			cell_dep.add(new Paragraph(dependentes).setTextAlignment(TextAlignment.CENTER));
			table.addCell(cell_dep);
		}
		
		Text valor = new Text("VALOR");
		valor.setBold();
		Cell c_valor = new Cell();
		c_valor.add(new Paragraph(valor).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_valor);

		Text adesao = new Text("DATA ADESÃO");
		adesao.setBold();
		adesao.setBold();
		Cell c_adesao = new Cell();
		c_adesao.add(new Paragraph(adesao).setTextAlignment(TextAlignment.CENTER));
		table.addCell(c_adesao);

		double total = 0, val = 0;
		ServidorConvenio sc = null;
		Iterator<ServidorConvenio> it = scList.iterator();
		
		TextAlignment ta = TextAlignment.LEFT;//alinhamento do conteudo das celulas
		while (it.hasNext()) {
			sc = it.next();
			servidor = sDB.select(sc.getCpf());
			if (sc.getValor() != 0) {
				table.addCell(new Cell()
						.add(new Paragraph(servidor.getMatricula()).setTextAlignment(ta)));
				
				table.addCell(servidor.getNome());
				
				if(convenio.isDependentesInlude()) {
					table.addCell(new Cell().add(new Paragraph(1+"" )).setTextAlignment(TextAlignment.CENTER));
					table.addCell(new Cell().add(new Paragraph((servidor.getQtdDependentes()-1)+"" )).setTextAlignment(TextAlignment.CENTER));
					table.addCell(new Cell().add(new Paragraph((servidor.getQtdDependentes())+"" )).setTextAlignment(TextAlignment.CENTER));
				}
				
				val = sc.getValor();
				table.addCell(new Cell().add(new Paragraph("R$ " + val)).setTextAlignment(ta));
				table.addCell(new Cell().add(new Paragraph(new Data().getStringDate(sc.getDataAdesao()))
						.setTextAlignment(TextAlignment.CENTER)));
				total += val;
				val = 0;
				if (it.hasNext()) {
					table.startNewRow();
				}
			}
		}
		doc.add(table);
		doc.add(new Paragraph("\n"));
		DecimalFormat df = new DecimalFormat("0.00");
		Text totalTexto = new Text("Total ..................................................................................................."
				+ "   R$ " + df.format(total));
		doc.add(new Paragraph(totalTexto));
		
		LocalDate data = LocalDate.now();
		StringBuilder sb = new StringBuilder("\n\n");
		sb.append(data.getDayOfMonth()).append(" de ")
		.append(new Data().getMes(data.getMonthValue())).append(" de ").append(data.getYear() )
		.append("\n\nJoão de Deus Oliveira\nPresidente Sindsbarra");
		
		Text assText = new Text(sb.toString());
		doc.add(new Paragraph(assText).setTextAlignment(TextAlignment.RIGHT));
		doc.close();
	}

}
