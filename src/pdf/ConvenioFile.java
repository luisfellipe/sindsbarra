package pdf;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import model.Data;
import model.ServidorConvenio;

public class ConvenioFile {
	private String RESULT = null, LOGO = "src/assets/img/logox3.png";
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
			ImageData data = ImageDataFactory.create(LOGO);
			Image image = new Image(data);
			image.setHeight(119);
			image.setWidth(520);
			doc.add(image);

		} catch (FileNotFoundException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addConvenioServidor(List<ServidorConvenio> scList) {
		createPdf();

		Text texto = new Text(scList.get(0).getConvenio().getNome());
		texto.setBold();
		texto.setFontSize(14);
		doc.add(new Paragraph("\n"));
		Paragraph p = new Paragraph(texto);
		p.setTextAlignment(TextAlignment.CENTER);
		doc.add(p);

		float[] pointColumnWidths = { 80F, 280F, 80F, 30F };
		table = new Table(pointColumnWidths);
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
		while (it.hasNext()) {
			sc = it.next();
			if (sc.getValor() != 0) {
				table.addCell(new Cell()
						.add(new Paragraph(sc.getServidor().getMatricula()).setTextAlignment(TextAlignment.CENTER)));

				table.addCell(sc.getServidor().getNome());
				val = sc.getValor();
				table.addCell(new Cell().add(new Paragraph("R$ " + val)).setTextAlignment(TextAlignment.CENTER));
				table.addCell(new Cell().add(new Paragraph(new Data().getStringDate(sc.getConvenio().getDataAdesao()))
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
		Text totalTexto = new Text("Total:  R$ " + df.format(total));
		doc.add(new Paragraph(totalTexto));
		doc.close();
	}

}
