package com.app.pga.App.Reporte;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;



@NoArgsConstructor
public class Footer extends PdfPageEventHelper {
    private Font footerFont = new Font(Font.HELVETICA, 8);

    @Override
    //el metodo se ejecuta cada vez que se termina una pagina
    public void onEndPage(PdfWriter writer, Document document) {

        PdfPTable footer = new PdfPTable(3);

        try {
            footer.setWidths(new int[]{3,3,1});
            footer.setTotalWidth(520);

        } catch (DocumentException e) {
            e.printStackTrace();
        }

        PdfPCell cell1 = new PdfPCell(new Phrase("Plataforma de Gestión Académica", footerFont));
        PdfPCell cell2 = new PdfPCell(new Phrase("Documento generado automáticamente", footerFont));
        PdfPCell cell3 = new PdfPCell(new Phrase("Página " + writer.getPageNumber(), footerFont));
        cell1.setBorder(Rectangle.NO_BORDER);
        cell2.setBorder(Rectangle.NO_BORDER);
        cell3.setBorder(Rectangle.NO_BORDER);

        cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell3.setHorizontalAlignment(Element.ALIGN_RIGHT);

        footer.addCell(cell1);
        footer.addCell(cell2);
        footer.addCell(cell3);
        //dibuja una tabla por cooordenadas
        footer.writeSelectedRows(
                0,                  //fila inicial
                -1,                          //fila final
                document.left(),             //a partir del margen izquierdo
                document.bottom() - 10,     //10 unidades debajo del margen inferior
                writer.getDirectContent()     //obtiene el canvas del pdf donde se dibuja el contenido manualmente
        );
    }
}

