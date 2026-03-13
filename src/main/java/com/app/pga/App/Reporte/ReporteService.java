package com.app.pga.App.Reporte;


import com.app.pga.App.Exception.ReporteException;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.UsuarioResponseDto;
import com.app.pga.App.Models.Entities.Inscripcion;
import com.app.pga.App.Services.Implements.InscripcionService;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {
    private  final InscripcionService inscripcionService;

    public ByteArrayInputStream generarReporteAlumno(UsuarioResponseDto alumno, List<Inscripcion> inscripciones, ExpedienteResponseDto expediente) throws Exception {

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaConsulta = hoy.format(formatter);

        //instancia Document, representa al pdf en construcción, configurado con un tamaño de pagina A4
        Document document = new Document(PageSize.A4);
        //buffer de memoria donde se escribirá el pdf generado, se utiliza de esta forma para no crear archivos temporales en disco.
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document, out); //vincula el documento con el buffer de datos.
        //Establece la relación de escritor y documento.
        writer.setPageEvent( new Footer());

        try {


            //abre el documento para escritura.
            //inicializa las estructuras necesarias para generar el pdf
            document.open();

            //fuentes
            Font fontTitulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font fontfecha = new Font(Font.HELVETICA, 8, Font.NORMAL);
            Font fontSubtitulo = new Font(Font.HELVETICA, 14, Font.NORMAL);
            Font fontSubtitulo2 = new Font(Font.HELVETICA, 12, Font.NORMAL);

            Font fontLabelDatos = new Font(Font.HELVETICA, 9, Font.BOLD);
            Font fontValorDatos = new Font(Font.HELVETICA, 9, Font.NORMAL);

            // CABECERA
            // DIVIDIR EN DOS COLUMNAS
            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1, 4, 1});


            // logo
            PdfPCell logoCell;
            URL url = getClass().getResource("/static/logo_infotec.png");
            if (url != null) {
                Image logoInfotec = Image.getInstance(url);
                logoInfotec.scaleToFit(70, 70);
                logoCell = new PdfPCell(logoInfotec);
            } else {
                logoCell = new PdfPCell(new Phrase(""));
            }
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            header.addCell(logoCell);


            // titulo
            //crea un objeto con la info del titulo y la tipografia personalizada
            Paragraph titulo = new Paragraph("PLATAFORMA DE GESTIÓN ACADÉMICA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            PdfPCell tituloCell = new PdfPCell(titulo);

            tituloCell.setBorder(Rectangle.NO_BORDER);
            tituloCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            header.addCell(tituloCell);

            //fecha
            //crea un objeto con la info de la fecha y la tipografia personalizada
            Paragraph fecha = new Paragraph("Fecha: " + fechaConsulta, fontfecha);
            fecha.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell fechaCell = new PdfPCell(fecha);

            fechaCell.setBorder(Rectangle.NO_BORDER);
            fechaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fechaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            header.addCell(fechaCell);

            //Añade la cabecera al documento
            document.add(header);

            //subtitulo ---- Reporte de alumno--

            LineSeparator linea = new LineSeparator();
            linea.setOffset(-3);
            document.add(linea);

            document.add(Chunk.NEWLINE);

            Paragraph subtitulo = new Paragraph("REPORTE DE ALUMNO", fontSubtitulo);

            subtitulo.setAlignment(Element.ALIGN_CENTER);

            document.add(subtitulo);

            // datos alumno
            //SUBTITULO
            Paragraph datos = new Paragraph("DATOS DEL ALUMNO", fontSubtitulo2);
            datos.setSpacingBefore(10);
            datos.setSpacingAfter(5);
            document.add(datos);
            //CONFIGURAR TABLA
            PdfPTable datosAlumno = new PdfPTable(2);
            datosAlumno.setWidthPercentage(100);
            datosAlumno.setWidths(new float[]{2, 6});
            datosAlumno.setSpacingBefore(10);

            addFila(datosAlumno, "ALUMNO:", alumno.nombre() + " " + alumno.apellidoPaterno() + " " + alumno.apellidoMaterno(),
                    fontLabelDatos, fontValorDatos);
            //addFila(datosAlumno, "EMAIL:", alumno.mail(),fontLabelDatos, fontValorDatos));

            addFila(datosAlumno, "TELÉFONO:", alumno.telefono(), fontLabelDatos, fontValorDatos);

            addFila(datosAlumno, "DIRECCIÓN:", alumno.direccion(), fontLabelDatos, fontValorDatos);

            addFila(datosAlumno, "ESTADO:", alumno.activo() ? "ACTIVO" : "INACTIVO", fontLabelDatos, fontValorDatos);

            addFila(datosAlumno, "FECHA CREACIÓN:", alumno.created_At().toString(), fontLabelDatos, fontValorDatos);

            addFila(datosAlumno, "FECHA ALTA:", alumno.fechaAlta() != null ? alumno.fechaAlta().toString() : "—", fontLabelDatos, fontValorDatos);
            addFila(datosAlumno, "FECHA BAJA:", alumno.fechaBaja() != null ? alumno.fechaBaja().toString() : "—", fontLabelDatos, fontValorDatos);

            document.add(datosAlumno);

            // expediente
            //SUBTITULO
            Paragraph exp= new Paragraph("EXPEDIENTE", fontSubtitulo2);
            exp.setSpacingBefore(10);
            exp.setSpacingAfter(5);
            document.add(exp);

            //CONFIGURAR TABLA expediente
            PdfPTable datosExp = new PdfPTable(2);
            datosExp.setWidthPercentage(100);
            datosExp.setWidths(new float[]{2, 6});
            datosExp.setSpacingBefore(10);

            addFila(datosExp, "ESTADO DEL EXPEDIENTE:", expediente.estado().name(),
                    fontLabelDatos, fontValorDatos);
            addFila(datosExp, "OBSERVACIONES:", (expediente.observaciones() != null ? expediente.observaciones() : "Sin observaciones"  ), fontLabelDatos, fontValorDatos);

            document.add(datosExp);

            //iNSCRIPCIONES
            Paragraph tituloInscripciones = new Paragraph("INSCRIPCIONES", fontSubtitulo2);
            tituloInscripciones.setSpacingBefore(10);
            tituloInscripciones.setSpacingAfter(5);
            document.add(tituloInscripciones);

            if(inscripciones.isEmpty()){
                Paragraph vacio = new Paragraph(
                        "El alumno no cuenta con inscripciones registradas.",
                        fontValorDatos
                );
                vacio.setIndentationLeft(10);
                document.add(vacio);
            }
            //configuracion tabla
            for (Inscripcion i : inscripciones) {

                PdfPTable tabla = new PdfPTable(3);
                tabla.setWidthPercentage(100);
                tabla.setWidths(new float[]{2,3,3});
                tabla.setSpacingBefore(10);
                tabla.setSpacingAfter(5);

                PdfPCell h1 = new PdfPCell(new Phrase("ID INSCRIPCIÓN: " + i.getIdInscripcion(), fontLabelDatos));
                PdfPCell h2 = new PdfPCell(new Phrase("TIPO: " + i.getTipo().name(), fontLabelDatos));
                PdfPCell h3 = new PdfPCell(new Phrase(i.getFechaInicio() + " - " + i.getFechaFin(), fontLabelDatos));

                h1.setBorder(Rectangle.NO_BORDER);
                h2.setBorder(Rectangle.NO_BORDER);
                h3.setBorder(Rectangle.NO_BORDER);

                h1.setBackgroundColor(new Color(240,240,240));
                h2.setBackgroundColor(new Color(240,240,240));
                h3.setBackgroundColor(new Color(240,240,240));

                h1.setHorizontalAlignment(Element.ALIGN_LEFT);
                h2.setHorizontalAlignment(Element.ALIGN_CENTER);
                h3.setHorizontalAlignment(Element.ALIGN_RIGHT);

                tabla.addCell(h1);
                tabla.addCell(h2);
                tabla.addCell(h3);

                // Grupo
                PdfPCell grupoCell = new PdfPCell(
                        new Phrase("GRUPO: " + (i.getGrupo() != null ? i.getGrupo().getNombre() : "Por asignar"), fontValorDatos)
                );
                grupoCell.setColspan(3);  //que abarque toda la fila
                grupoCell.setBorder(Rectangle.NO_BORDER);
                tabla.addCell(grupoCell);

                // Escuela
                PdfPCell escuelaCell = new PdfPCell(
                        new Phrase("ESCUELA PROCEDENCIA: " + i.getEscuela() +
                                        "     GRADO: " + i.getNivelEstudio() +
                                        "     CARRERA: " + i.getCarrera(),
                                fontValorDatos
                        )
                );
                escuelaCell.setColspan(3);   //que abarque toda la fila
                escuelaCell.setBorder(Rectangle.NO_BORDER);

                tabla.addCell(escuelaCell);

                // Estado
                PdfPCell estadoCell = new PdfPCell(
                        new Phrase(
                                "ESTADO: " + (i.getEstado() ? "ACTIVA" : "FINALIZADA") +
                                        "     FECHA INSCRIPCIÓN: " + (i.getFechaInscripcion() != null ? i.getFechaInscripcion() : "—") +
                                        "     FECHA BAJA: " + (i.getFechaBaja() != null ? i.getFechaBaja() : "—"),
                                fontValorDatos
                        )
                );
                estadoCell.setColspan(3);  //que abarque toda la fila
                estadoCell.setBorder(Rectangle.NO_BORDER);

                tabla.addCell(estadoCell);

                document.add(tabla);

                LineSeparator linea2 = new LineSeparator();
                linea2.setOffset(-5);
                document.add(linea2);
            }
            document.close();
        } catch (Exception e){
            throw new ReporteException("Error generando reporte del alumno", e);
        }
        //out.toByteArray() obtiene los bytes resultantes del PDF.
        //crea un InputStream de esos bytes para devolver desde el método.
        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addFila(PdfPTable table, String label, String value, Font labelFont, Font valueFont){

        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}
