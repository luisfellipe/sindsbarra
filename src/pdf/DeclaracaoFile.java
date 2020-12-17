package pdf;

import java.io.IOException;
import java.time.LocalDate;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import model.Data;
import model.Endereco;
import model.Ficha;
import model.Servidor;

public class DeclaracaoFile {
	private StringBuffer texto = null;
	private String RESULT = null;
	private PdfDocument pdfDoc = null;
	Document doc = null;
	PdfWriter pdfWriter = null;
	private Table table = null;
	private Servidor servidor = null;

	public DeclaracaoFile(Servidor servidor, final String RESULT) {
		this.RESULT = RESULT;
		this.servidor = servidor;

	}

	private void createPdf() {
		try {

			pdfWriter = new PdfWriter(RESULT);
			pdfDoc = new PdfDocument(pdfWriter);
			doc = new Document(pdfDoc);
			doc.setWordSpacing(1.5f);
			doc.setLeftMargin(60f);
			doc.setRightMargin(60f);
			doc.setTopMargin(60f);
			doc.setTextAlignment(TextAlignment.JUSTIFIED);
			

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void declaracao() {
		createPdf();

		Text titulo = new Text("Ao Sindicato dos Servidores de São José da Barra -- SindsBarra\n\n");
		Paragraph p1 = new Paragraph(titulo);
		p1.setFontSize(14);
		p1.setBold();
		p1.setFirstLineIndent(30);
		doc.add(p1);

		Paragraph p2 = new Paragraph("Sr. Presidente,");
		p2.setBold();
		p2.setFirstLineIndent(30);
		doc.add(p2);

		texto = new StringBuffer();
		texto.append("").append("Pelo presente, eu ").append(servidor.getNome() + ", ")
				.append("servidor(a) público municipal, solicito minha filiação no Sindicato dos ")
				.append("Servidores Públicos Municipais de São José da Barra (SINDSBARRA) nos ")
				.append("termos do Estatuto sindical vigente. Autorizando, após a deliberação ")
				.append("da filiação, que seja descontado em folha de pagamento o valor de ")
				.append("1% da remuneração mensal nos termos estatutários.\n");

		Paragraph p3 = new Paragraph(texto.toString());
		p3.setFirstLineIndent(30);
		doc.add(p3);
		Paragraph p4 = new Paragraph("Peço deferimento");
		p4.setFirstLineIndent(30);
		doc.add(p4);
		StringBuffer sb = new StringBuffer();
		LocalDate data = LocalDate.now();
		sb.append("São José da Barra, ").append(data.getDayOfMonth()).append(" de ")
				.append(new Data().getMes(data.getMonthValue())).append(" de ").append(data.getYear() + ".");
		Paragraph p5 = new Paragraph(sb.toString());
		p5.setTextAlignment(TextAlignment.RIGHT);
		doc.add(p5);

		Paragraph p6 = new Paragraph("______________________________________________________________________");
		p6.setBold();
		p6.setTextAlignment(TextAlignment.CENTER);
		doc.add(p6);

		Paragraph p7 = new Paragraph("\nFICHA DE INSCRIÇÃO\n\n");
		p7.setBold();
		p7.setTextAlignment(TextAlignment.CENTER);
		doc.add(p7);

		StringBuffer s = new StringBuffer();
		s.append("NOME: ").append(servidor.getNome()).append("\n");
		Ficha ficha = servidor.getFicha();
		s.append("ESTADO CIVIL: ").append(ficha.getEstadoCivil()).append("\n").append("NATURALIDADE: ")
				.append(ficha.getEndereco().getCidadeNatal()).append("\n").append("DATA DE NASCIMENTO: ")
				.append(new Data().getStringDate(servidor.getDataNasc())).append("\n").append("CPF: ")
				.append(servidor.getCpf()).append("\t\t\tRG: ").append(servidor.getCpf()).append("\n")
				.append("FILIAÇÃO\n");

		s.append("Pai: ").append(ficha.getNomePai()).append("\n").append("Mãe: ").append(ficha.getNomeMae())
				.append("\n");
		s.append("ENDEREÇO: Bairro ").append(ficha.getEndereco().getBairro()).append(", ")
				.append(ficha.getEndereco().getRua()).append(" Nº ").append(ficha.getEndereco().getNumero())
				.append("    Tel: ").append(ficha.getTelefone()).append("\nFUNÇÃO: ").append(servidor.getFuncao())
				.append("\t\t\tADMISSÃO: ").append(new Data().getStringDate(servidor.getDataAdmissao()))
				.append("\n\n\nASSINATURA:___________________________________________________________\n")
				.append("\nEu, João de Deus Oliveira, presidente do SindsBarra, declaro para os devidos fins que "
						+ "revendo os arquivos internos do Sindicato, constei que o solicitante acima qualificado, "
						+ "já é filiado desde a data de _____/______/______, assim sendo mantém o deferimento.");

		Paragraph p8 = new Paragraph(s.toString());
		doc.add(p8);
		Paragraph p9 = new Paragraph(
				"\n\n\n_______________________________________________________\n" + "João de Deus Oliveira");
		p9.setBold();
		p9.setTextAlignment(TextAlignment.CENTER);

		doc.add(p9);
		doc.setMargins(10, 10, 100, 100);
		doc.close();
	}

	public static void main(String[] args) {
		Servidor s = new Servidor();
		s.setCpf("149.123.286-91");
		s.setDataAdmissao(LocalDate.now());
		s.setDataNasc(LocalDate.now());
		s.setFuncao("Pedreiro");
		s.setMatricula("1324-8");
		s.setNome("Samuel Ferreira Silva");
		s.setQtdDependentes(2);
		s.setRG("567.897.86");
		Ficha fi = new Ficha();
		fi.setEstadoCivil("Solteiro");
		fi.setNomeMae("Nely Alves Soares");
		fi.setNomePai("José Aparecido Gomes Soares");
		fi.setSexo("Masculino");
		fi.setTelefone("97567956");
		Endereco e = new Endereco();
		e.setBairro("Residencial Ouro");
		e.setCep("37945-000");
		e.setCidadeAtual("São José da Barra");
		e.setCidadeNatal("Bocaiuva MG");
		e.setEstado("MG");
		e.setNumero(265);
		e.setRua("Rua Lázaro Flor");

		fi.setEndereco(e);
		s.setFicha(fi);
		DeclaracaoFile c = new DeclaracaoFile(s, "dec.pdf");
		c.declaracao();
	}
}
