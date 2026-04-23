package com.app.pga.App.Reporte;

import com.app.pga.App.Exception.ReporteException;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionReporteDto;
import com.app.pga.App.Models.Enum.Estado;
import com.lowagie.text.*;
import com.lowagie.text.Font;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteInscripcionesService {

    public ByteArrayInputStream generarReporteInscripciones(List<InscripcionReporteDto> inscripciones, int conteoAlumAct, int conteoAlumInac, int conteoDocAct, int conteoDocInac) throws Exception {

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaConsulta = hoy.format(formatter);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new Footer());
        try{
            document.open();

            Font fontTitulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font fontFecha = new Font(Font.HELVETICA, 8, Font.NORMAL);
            Font fontSubtitulo = new Font(Font.HELVETICA, 14, Font.NORMAL);
            Font fontSubtitulo2 = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font fontLabelDatos = new Font(Font.HELVETICA, 9, Font.BOLD);
            Font fontValorDatos = new Font(Font.HELVETICA, 9, Font.NORMAL);

            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1,4,1});

            PdfPCell logoCell;
            URL url = getClass().getResource("/static/logo_infotec.png");
            if (url !=null){
                com.lowagie.text.Image logoInfotec = com.lowagie.text.Image.getInstance(url);
                logoInfotec.scaleToFit(70, 70);
                logoCell = new PdfPCell(logoInfotec);
            }else{
                logoCell = new PdfPCell(new Phrase(""));
            }
            logoCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.addCell(logoCell);

            Paragraph titulo = new Paragraph("PLATAFORMA DE GESTIÓN ACADÉMICA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            PdfPCell tituloCell = new PdfPCell(titulo);

            tituloCell.setBorder(com.lowagie.text.Rectangle.NO_BORDER);
            tituloCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.addCell(tituloCell);

            Paragraph fecha = new Paragraph("Fecha: " + fechaConsulta, fontFecha);
            fecha.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell fechaCell = new PdfPCell(fecha);

            fechaCell.setBorder(Rectangle.NO_BORDER);
            fechaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fechaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            header.addCell(fechaCell);
            document.add(header);

            LineSeparator linea = new LineSeparator();//es un detalle visual
            linea.setOffset(-3);//espacio visual hacia abajo para que no quede pegado con las letras
            document.add(linea);

            document.add(Chunk.NEWLINE);//es un salto de linea

            Paragraph subtitulo = new Paragraph("REPORTE DE USUARIOS EN EL SISTEMA", fontSubtitulo);

            subtitulo.setAlignment(Element.ALIGN_CENTER);

            document.add(subtitulo);

            //datos en tarjetas
            //subtitulo
            Paragraph datos = new Paragraph("REGISTRO DE USUARIOS", fontSubtitulo2);
            datos.setSpacingBefore(10);
            datos.setSpacingAfter(15);
            document.add((datos));
            //contendedor para las cartas
            PdfPTable contenedorCards = new PdfPTable(3);
            contenedorCards.setWidthPercentage(60);
            contenedorCards.setHorizontalAlignment(Element.ALIGN_CENTER);
            contenedorCards.setWidths(new float[]{5,1,5});
            //espacio
            PdfPCell espacio = new PdfPCell(new Phrase(""));
            espacio.setBorder(Rectangle.NO_BORDER);

            //creación de las cartas
            PdfPCell cardAlumnos = crearCard(
                    "ALUMNOS",conteoAlumAct, conteoAlumInac
            );

            PdfPCell cardDocentes = crearCard(
                    "DOCENTES",conteoDocAct, conteoDocInac
            );

            contenedorCards.addCell(cardAlumnos);
            contenedorCards.addCell(espacio);
            contenedorCards.addCell(cardDocentes);

            document.add(contenedorCards);
            document.add(Chunk.NEWLINE);//es un salto de linea
            LineSeparator linea1 = new LineSeparator();//es un detalle visual
            linea1.setOffset(-3);//espacio visual hacia abajo para que no quede pegado con las letras
            document.add(linea1);
            document.add(Chunk.NEWLINE);//es un salto de linea

            //tablas por docente y grupos

            Map<String , List<InscripcionReporteDto>> gruposAgrupados =
                    inscripciones.stream()
                            .collect(Collectors.groupingBy(InscripcionReporteDto::Docente));

            Paragraph subtitulo2 = new Paragraph("GRUPOS POR DOCENTE", fontSubtitulo);

            document.add(subtitulo2);

            for (Map.Entry<String, List<InscripcionReporteDto>> entry : gruposAgrupados.entrySet()) {

                String docente = entry.getKey();
                List <InscripcionReporteDto> listaGrupos = entry.getValue();

                // nombre del grupo
                Paragraph nombreGrupo = new Paragraph("Docente: " + docente, fontSubtitulo2);
                nombreGrupo.setSpacingBefore(10);
                nombreGrupo.setSpacingAfter(5);
                document.add(nombreGrupo);

                PdfPTable tablaGrupo = new PdfPTable(2);
                tablaGrupo.setWidthPercentage(70);
                tablaGrupo.setWidths(new float[]{6, 2});

                //encabezados sin color
                PdfPCell hGrupo = new PdfPCell((new Phrase("Grupo", fontSubtitulo)));
                hGrupo.setBackgroundColor(null);//quita el color de fondo
                hGrupo.setPadding(5);

                PdfPCell hEstado = new PdfPCell((new Phrase("Estado", fontSubtitulo)));
                hEstado.setBackgroundColor(null);//quita el color de fondo
                hEstado.setPadding(5);

                tablaGrupo.addCell(hGrupo);
                tablaGrupo.addCell(hEstado);

                //tabla de grupos por docente
                List<InscripcionReporteDto> gruposUnicos = listaGrupos.stream()
                        .collect(Collectors.toMap(
                                InscripcionReporteDto::nombreGrupo,
                                g -> g,
                                (existing, replacement) -> existing
                        ))
                        .values()
                        .stream()
                        .collect(Collectors.toList());

                for (InscripcionReporteDto g : gruposUnicos) {
                    //celda nommbre
                    PdfPCell nombreCell = new PdfPCell(new Phrase(g.nombreGrupo(), fontValorDatos));
                    nombreCell.setPadding(5);
                    //uso de gris claro para diferenciar filas
                    nombreCell.setBackgroundColor(new Color(245,245,245));
                    tablaGrupo.addCell(nombreCell);

                    //celda estado
                    PdfPCell estadoCell = new PdfPCell(new Phrase(g.estadoGrupo().toString(), fontValorDatos));
                    estadoCell.setPadding(5);
                    estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    if (g.estadoGrupo() == Estado.HABILITADO) {
                        estadoCell.setBackgroundColor(new Color(200,255,200));
                    } else{
                        estadoCell.setBackgroundColor(new Color(255, 248,200));
                    }

                    tablaGrupo.addCell(estadoCell);

                }
                document.add(tablaGrupo);
                document.add(Chunk.NEWLINE);
            }

            LineSeparator linea4 = new LineSeparator();//es un detalle visual
            linea4.setOffset(-3);//espacio visual hacia abajo para que no quede pegado con las letras
            document.add(linea4);
            document.add(Chunk.NEWLINE);
            //tablas
            Map<String, List<InscripcionReporteDto>> inscripcionesAgrupadas =
                    inscripciones.stream()
                                    .collect(Collectors.groupingBy(InscripcionReporteDto :: nombreGrupo));

            //document.add(Chunk.NEWLINE);//es un salto de linea

            Paragraph subtitulo3 = new Paragraph("INSCRIPCIONES POR GRUPO", fontSubtitulo);

            document.add(subtitulo3);

            for (Map.Entry<String, List<InscripcionReporteDto>> entry : inscripcionesAgrupadas.entrySet()) {

                    String grupo = entry.getKey();
                    List <InscripcionReporteDto> listaAlumnos = entry.getValue();
                    String curso = listaAlumnos.get(0).nombreCurso();

                    // nombre del grupo
                    Paragraph nombreGrupo = new Paragraph("Grupo: " + grupo + "   Curso: " + curso, fontSubtitulo2);
                    nombreGrupo.setSpacingBefore(10);
                    nombreGrupo.setSpacingAfter(5);
                    document.add(nombreGrupo);

                    PdfPTable tablaAlumno = new PdfPTable(2);
                    tablaAlumno.setWidthPercentage(100);
                    tablaAlumno.setWidths(new float[]{6, 2});

                    //encabezados sin color
                    PdfPCell hAlumno = new PdfPCell((new Phrase("Alumno", fontSubtitulo)));
                    hAlumno.setBackgroundColor(null);//quita el color de fondo
                    hAlumno.setPadding(5);

                    PdfPCell hEstado = new PdfPCell((new Phrase("Estado", fontSubtitulo)));
                    hEstado.setBackgroundColor(null);//quita el color de fondo
                    hEstado.setPadding(5);

                    tablaAlumno.addCell(hAlumno);
                    tablaAlumno.addCell(hEstado);


                    //tabla de alumno
                    for (InscripcionReporteDto a : listaAlumnos) {
                        //celda nommbre
                        PdfPCell nombreCell = new PdfPCell(new Phrase(a.alumno(), fontValorDatos));
                        nombreCell.setPadding(5);
                        //uso de gris claro para diferenciar filas
                        nombreCell.setBackgroundColor(new Color(245,245,245));
                        tablaAlumno.addCell(nombreCell);

                        String estadoTexto = a.estado() ? "HABILITADO" : "INHABILITADO";

                        //celda estado
                        PdfPCell estadoCell = new PdfPCell(new Phrase(estadoTexto, fontValorDatos));
                        estadoCell.setPadding(5);
                        estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                        if (a.estado() == Boolean.TRUE) {
                            estadoCell.setBackgroundColor(new Color(200,255,200));
                        } else{
                            estadoCell.setBackgroundColor(new Color(255, 248,200));
                        }

                        tablaAlumno.addCell(estadoCell);

                    }
                    document.add(tablaAlumno);
                    document.add(Chunk.NEWLINE);
                }

            document.close();
        } catch(Exception e){
            throw new ReporteException("Error generando el reporte", e);
        }

        return new ByteArrayInputStream(out.toByteArray());

    }

    private void addFila(PdfPTable table, String etiqueta, String value, Font labelFont, Font valueFont){

        PdfPCell labelCell = new PdfPCell(new Phrase(etiqueta, labelFont));
        labelCell.setBorder(Rectangle.NO_BORDER);//quita las lineas de la tabla

        //celda para el dato real
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.NO_BORDER);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    //metodo para construir las cartas / tarjetas
    private PdfPCell crearCard (String titulo, int activos, int inactivos){
        //Tabla interna de la card
        PdfPTable card = new PdfPTable(1);
        card.setWidthPercentage(100);

        //colores
        Color fondo = Color.WHITE;
        Color headerColor = new Color(188, 149, 92);

        //titulo
        PdfPCell tituloCell = new PdfPCell(new Phrase(titulo, new Font(Font.HELVETICA, 12, Font.NORMAL)));
        tituloCell.setBackgroundColor(headerColor);
        tituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        tituloCell.setPadding(10);
        tituloCell.setBorder(Rectangle.NO_BORDER);
        tituloCell.setPhrase(new Phrase(titulo, new Font(Font.HELVETICA, 12, Font.BOLD, Color.WHITE)));

        card.addCell(tituloCell);

        //datos
        card.addCell(crearFilaCard("Activos", activos, fondo));
        card.addCell(lineaSeparadora());
        card.addCell(crearFilaCard("Inactivos", inactivos, fondo));
        card.addCell(lineaSeparadora());
        card.addCell(crearFilaCard("Total", activos + inactivos, fondo));

        //contenedor final
        PdfPCell contenedor = new PdfPCell(card);
        contenedor.setPadding(12);
        contenedor.setBackgroundColor(Color.WHITE);
        contenedor.setBorderWidth(0.8f);

        return contenedor;
    }

    private PdfPCell crearFilaCard(String label, int valor, Color fondo) {

        Font fontLabelDatos = new Font(Font.HELVETICA, 9, Font.BOLD);
        Font fontValorDatos = new Font(Font.HELVETICA, 9, Font.NORMAL);

        PdfPTable fila = new PdfPTable(2);
        fila.setWidthPercentage(100);
        fila.setWidths(new float[]{6,4});

        PdfPCell l = new PdfPCell(new Phrase(label, fontLabelDatos));
        l.setBorder(Rectangle.NO_BORDER);
        l.setBackgroundColor(fondo);
        l.setPadding(5);

        PdfPCell v = new PdfPCell(new Phrase(String.valueOf(valor), fontValorDatos));
        v.setBorder(Rectangle.NO_BORDER);
        v.setHorizontalAlignment(Element.ALIGN_RIGHT);
        v.setBackgroundColor(fondo);
        v.setPadding(5);

        fila.addCell(l);
        fila.addCell(v);

        PdfPCell wrapper = new PdfPCell(fila);
        wrapper.setBorder(Rectangle.NO_BORDER);

        return wrapper;
    }

    private PdfPCell lineaSeparadora() {
        PdfPCell linea = new PdfPCell(new Phrase(""));
        linea.setBorderWidthTop(0.5f);
        linea.setBorderColorTop(new Color(119, 117, 117));
        linea.setBorder(Rectangle.TOP);
        linea.setPadding(3);
        return linea;
    }


}
