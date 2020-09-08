package presentation;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Aceasta clasa este responsabila de crearea unui document PDf in care se metioneaza esuarea crearii unei chitante.
 */
public class FailedBillPDF {
    String clientName;
    String productName;
    int quantity;
    Double totalPrice;
    public FailedBillPDF(String clientName,String productName,int quantity,Double totalPrice,String fileName) throws DocumentException, FileNotFoundException {

        this.clientName=clientName;
        this.productName=productName;
        this.quantity=quantity;
        this.totalPrice=totalPrice;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName+".pdf"));

        document.open();
        document.add(new Paragraph("Comanda cu datele aferente nu a fost inregistrata! STOC INDISPONIBIL!"));
        document.add(new Paragraph("\n"));
        PdfPTable table = new PdfPTable(4);
        addTableHeader(table);
        addRows(table);
        document.add(table);
        document.close();
    }

    /**
     * Adauga header unui tabel din documentul PDF
     * @param table PdfPTable
     */
    private void addTableHeader(PdfPTable table) {

            table.addCell("clientName");
            table.addCell("productName");
            table.addCell("quantity");
            table.addCell("totalPrice");
    }

    /**
     * Adauga randuri intr-un tabel din documentul PDF
     * @param table PdfPTable
     */
    private void addRows(PdfPTable table) {
        table.addCell(this.clientName);
        table.addCell(this.productName);
        table.addCell(Integer.toString(quantity));
        table.addCell(Double.toString(this.totalPrice));
    }


    public String getClientName(){return this.clientName;}
    public String getProductName(){return this.productName;}
    public int getQuantity(){return this.quantity;}
    public Double getTotalPrice(){return this.totalPrice;}

}
