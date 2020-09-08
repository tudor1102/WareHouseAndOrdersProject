package presentation;

import business.ClientBLL;
import business.OrderBLL;
import business.OrderItemBLL;
import business.ProductBLL;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.stream.Stream;
/**
Aceasta clasa este responsabila de creare unui document PDF in care se afla tabelul cu clienti.
 */
public class PDFGeneratorClient {
    private String[] columnHeaders;
    private int columnCount;
    private String[] clientNames;
    private int rowCount;
    private String[] clientAddress;
    public PDFGeneratorClient(int columnCount,int rowCount,String[] columnHeaders,String[] clientNames,String[] clientAddress,String fileName) throws DocumentException, FileNotFoundException {
       this.columnCount=columnCount;
       this.columnHeaders=columnHeaders;
       this.clientNames=clientNames;
       this.rowCount=rowCount;
       this.clientAddress=clientAddress;
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(fileName+".pdf"));

        document.open();

        PdfPTable table = new PdfPTable(this.columnCount);
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
       for(int i=0;i<this.columnCount;i++)
       {
           table.addCell(this.columnHeaders[i]);
       }
    }
    /**
     * Adauga randuri intr-un tabel din documentul PDF
     * @param table PdfPTable
     */
    private void addRows(PdfPTable table) {
        for(int i=0;i<rowCount;i++)
        {
            PdfPCell cell1=new PdfPCell(new Paragraph((this.clientNames[i])));
            PdfPCell cell2=new PdfPCell(new Paragraph((this.clientAddress[i])));
            table.addCell(cell1);
            table.addCell(cell2);
        }

    }

    public void setColumnHeaders(String[] columnHeaders)
    {
        this.columnHeaders=columnHeaders;
    }
    public void setColumnCount(int columnCount)
    {
        this.columnCount=columnCount;
    }
    public void setClientNames(String[] clientNames){this.clientNames=clientNames;}
    public void setClientAddress(String[] clientAddress){this.clientAddress=clientAddress;}
    public String[] getClientNames(){return this.clientNames;}
    public String[] getClientAddress(){return this.clientAddress;}
    public String[] getColumnHeaders()
    {
        return this.columnHeaders;
    }
}
