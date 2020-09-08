package Start;

import com.itextpdf.text.DocumentException;
import presentation.FileParser;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class StartClass {

    public static void main(String[] args) throws SQLException, IOException, DocumentException {
        File f=new File(args[0]);
        FileParser fp=new FileParser(f);
    }
}
