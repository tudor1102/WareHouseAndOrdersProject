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
 * Aceasta clasa este responsabila de creare unui document PDF in care se afla tabelul cu comenzi
 */
public class PDFGeneratorOrder {
    private String[] columnHeaders;
    private int columnCount;
    private int rowCount;
    String[] clientNames;
    float[] totalPrices;
    String[] productNames;
    int[] quantities;
    float[] prices;
    public PDFGeneratorOrder(int columnCount,int rowCount,String[] columnHeaders,String[] clientNames,String[] productNames,int[] quantities,float[] totalPrices,float[] prices,String fileName) throws DocumentException, FileNotFoundException {
        this.columnCount=columnCount;
        this.columnHeaders=columnHeaders;
        this.rowCount=rowCount;
        this.clientNames=clientNames;
        this.productNames=productNames;
        this.quantities=quantities;
        this.prices=prices;
        this.totalPrices=totalPrices;
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
            PdfPCell cell2=new PdfPCell(new Paragraph(((this.productNames[i]))));
            PdfPCell cell3=new PdfPCell(new Paragraph(Integer.toString(this.quantities[i])));
            PdfPCell cell4=new PdfPCell(new Paragraph(Float.toString(this.totalPrices[i])));
            PdfPCell cell5=new PdfPCell(new Paragraph(Float.toString(this.prices[i])));

            table.addCell(cell1);
            table.addCell(cell2);
            table.addCell(cell3);
            table.addCell(cell4);
            table.addCell(cell5);
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
    public void setProductNames(String[] productNames){this.productNames=productNames;}
    public void setQuantities(int[] quantities){this.quantities=quantities;}
    public void setPrices(float[] prices){this.prices=prices;}
    public String[] getProductNames(){return this.productNames;}
    public int[] getQuantities(){return this.quantities;}
    public float[] getPrices(){ return this.prices;}
    public String[] getColumnHeaders() { return this.columnHeaders; }
}
