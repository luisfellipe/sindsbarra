package pdf;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
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
import model.Servidor;

public class ServidorFile {
	private String RESULT = null, LOGO = "src/img/logo.png";
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

	public void addServidor(List<Servidor> servidores) {
		createPdf();

		Text texto = new Text("RELAÇÃO DE SERVIDORES SINDICALIZADOS");
		texto.setBold();
		texto.setFontSize(14);
		doc.add(new Paragraph("\n"));
		Paragraph p = new Paragraph(texto);
		p.setTextAlignment(TextAlignment.CENTER);
		doc.add(p);
		doc.add(new Paragraph("\n"));

		float[] pointColumnWidths = { 50F, 180F, 130F, 30F,50F,50F};
		table = new Table(pointColumnWidths);
		table.setFontSize(10);
		table.setTextAlignment(TextAlignment.CENTER);
		table.addCell("Matricula");
		table.addCell("Nome");
		table.addCell("Cargo/Função");
		table.addCell("Nascimento");
		table.addCell("Admissão");
		table.addCell("CPF");
		
		for (Servidor s : servidores) {
			table.addCell(s.getMatricula());
			table.addCell(s.getNome());
			table.addCell(s.getFuncao());
			table.addCell(new Data().getStringDate(s.getDataNasc()));
			table.addCell(new Data().getStringDate(s.getDataAdmissao()));
			table.addCell(s.getCpf());
			table.startNewRow();
		}
		doc.add(table);
		doc.close();
	}

}
