package presentation;
import business.ClientBLL;
import business.OrderBLL;
import business.OrderItemBLL;
import business.ProductBLL;
import com.itextpdf.text.DocumentException;
import dataaccess.ProductDAO;
import model.Client;
import model.Order;
import model.OrderItem;
import model.Product;

import java.io.*;
import java.sql.SQLException;

/**
 * Aceasta clasa este responsabila pentru "parsarea" fisierului.
 * Detecteaza comenzile pentru manipularea tabelelor.
 * Cand este detectata o anumita alaturare de cuvinte, spre exemplu: "Report Client", atunci
 * acest "FileParser" va apela metoda "genPDF" din ClientBLL.
 */
public class FileParser {
            static int numOfBill=0;
            static int failedBill=0;
            public FileParser(File f) throws IOException, SQLException, DocumentException {
                BufferedReader reader=new BufferedReader(new FileReader(f));
                String line=reader.readLine();
                ClientBLL clientBLL=new ClientBLL();
                ProductBLL productBLL=new ProductBLL();
                OrderItemBLL orderItemBLL=new OrderItemBLL();
                OrderBLL orderBLL=new OrderBLL();
                Client firstC=null;Order firstO=null;
                Product firstP=null;
               while (line!=null)
           {
                    String parts[]=line.split("[\\:\\,]");

                    if (parts[0].equals("Insert client"))
                    {

                        Client c=new Client(parts[1],parts[2]);
                        clientBLL.insertClient(c);
                        firstC=c;
                    }
                    else if (parts[0].equals("Insert product"))
                    {
                        String[] parts1=parts[1].split("\\s+");
                        String[] parts2=parts[2].split("\\s+");
                        String[] parts3=parts[3].split("\\s+");
                        Product p=new Product(parts1[1],Integer.parseInt(parts2[1]),Double.parseDouble(parts3[1]));
                        productBLL.insert(p);
                        firstP=p;
                    }
                    else if (parts[0].equals("Delete client"))
                    {
                        orderBLL.deleteClient(parts[1]);
                        clientBLL.delete(parts[1],parts[2]);
                    }
                    else if (parts[0].equals("Delete Product"))
                    {
                        String[] parts1=parts[1].split("\\s+");
                        orderBLL.deleteProduct(parts1[1]);
                        productBLL.delete(parts1[1]);
                    }
                    else if (parts[0].equals("Order"))
                    {
                        String parts2[]=parts[2].split("\\s+");
                        String parts3[]=parts[3].split("\\s+");

                        Product p=productBLL.findByName(parts2[1]);
                        OrderItem o=new OrderItem(parts[1],parts2[1],p.getPrice());
                        orderItemBLL.insertOrderItem(o);
                        Order order=new Order(parts[1],parts2[1],p.getPrice(),Integer.parseInt(parts3[1]));
                        firstO=order;
                        if (orderBLL.okToBill(order)==true)
                        {
                            orderBLL.insertOrder(order);
                            orderBLL.decreaseQuantity(order);
                            PDFGeneratorBill pdfGeneratorBill=new PDFGeneratorBill(order.getClientName(),order.getProductName(),order.getQuantity(),order.getTotalPrice(),"BillNo"+numOfBill);
                            numOfBill++;
                        }
                        else
                        {
                            FailedBillPDF failedBillPDF=new FailedBillPDF(order.getClientName(),order.getProductName(),order.getQuantity(),order.getTotalPrice(),"FailedBillNo"+failedBill);
                            failedBill++;
                        }
                    }
                    else if (parts[0].equals("Report client"))
                    {
                        PDFGeneratorClient pdfGenerator=null;
                        pdfGenerator=ClientBLL.genPDF(firstC.getName());
                    }
                    else if (parts[0].equals("Report product"))
                    {
                        PDFGeneratorProduct pdfGeneratorProduct=null;
                        pdfGeneratorProduct=ProductBLL.genPDF(firstP.getName());
                    }
                    else if (parts[0].equals("Report order"))
                    {
                        PDFGeneratorOrder pdfGeneratorOrder=null;
                        pdfGeneratorOrder=OrderBLL.genPDF(firstO.getClientName());
                    }
                    line=reader.readLine();
              }
                reader.close();
            }
}
