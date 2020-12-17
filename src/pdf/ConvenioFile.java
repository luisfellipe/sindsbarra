package pdf;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.List;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import model.Data;
import model.ServidorConvenio;

public class ConvenioFile {
	private String RESULT = null, LOGO = "src/img/logo.png";
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

			//
			ImageData data = ImageDataFactory.create(LOGO);
			Image image = new Image(data);
			image.setHeight(187);
			image.setWidth(698);
			doc.add(image);

			//

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
		doc.add(new Paragraph("\n"));

		float[] pointColumnWidths = { 80F, 280F, 80F, 30F };
		table = new Table(pointColumnWidths);
		table.setFontSize(10);
		table.setTextAlignment(TextAlignment.CENTER);
		table.addCell("MATRICULA");
		table.addCell("NOME");
		table.addCell("VALOR");
		table.addCell("DATA ADESÃO");
		double total = 0, valor = 0;
		for (ServidorConvenio sc : scList) {
			table.addCell(sc.getServidor().getMatricula());
			table.addCell(sc.getServidor().getNome());
			valor = sc.getValor();
			table.addCell("R$ "+ valor);
			table.addCell(new Data().getStringDate(sc.getConvenio().getDataAdesao()));
			table.startNewRow();
			total += valor;
			 valor = 0;
		}
		doc.add(table);
		
		doc.add(new Paragraph("\n"));
		DecimalFormat df = new DecimalFormat("0.00");
		Text totalTexto = new Text("Total:  R$ " + 	df.format(total));
		doc.add(new Paragraph(totalTexto));
		

		doc.close();
	}

}
