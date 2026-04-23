package com.app.pga.App.Reporte;

import com.app.pga.App.Exception.ReporteException;
import com.app.pga.App.Models.Dtos.ResponseDto.DocumentoResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.ExpedienteReporteDto;
import com.app.pga.App.Models.Enum.EstadoExpediente;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.pdf.draw.LineSeparator;
import lombok.RequiredArgsConstructor;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
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
public class ReporteExpGenService {

    public ByteArrayInputStream generarResporteExpediente (List<ExpedienteReporteDto>expedientes, List<DocumentoResponseDto>documentos) throws Exception{

        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaConsulta = hoy.format(formatter);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        writer.setPageEvent(new Footer());

        try {
            document.open();

            Font fontTitulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font fontFecha = new Font(Font.HELVETICA, 8, Font.NORMAL);
            Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font fontSubtitulo2 = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font fontLabelDatos = new Font(Font.HELVETICA, 9, Font.BOLD);
            Font fontValorDatos = new Font(Font.HELVETICA, 9, Font.NORMAL);

            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1, 4, 1});

            PdfPCell logoCell;
            URL url = getClass().getResource("/static/logo_infotec.png");
            if (url != null) {
                com.lowagie.text.Image logoInfotec = com.lowagie.text.Image.getInstance(url);
                logoInfotec.scaleToFit(70, 70);
                logoCell = new PdfPCell(logoInfotec);
            } else {
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

            Paragraph subtitulo = new Paragraph("REPORTE DEL ESTADO DE LOS EXPEDIENTES", fontSubtitulo);

            subtitulo.setAlignment(Element.ALIGN_CENTER);

            document.add(subtitulo);

            if(documentos.isEmpty()){
                Paragraph vacio = new Paragraph(
                        "NO EXISTEN DOCUMENTOS REGISTRADOS EN EL SISTEMA", fontValorDatos);
                vacio.setIndentationLeft(10);
                document.add(vacio);
                document.close();
                return new ByteArrayInputStream(out.toByteArray());
            }else{
                //DATOS DE LOS DOCUMENTOS
                Paragraph tituloDoc = new Paragraph("DOCUMENTOS REGISTRADOS EN EL SISTEMA", fontSubtitulo2);
                tituloDoc.setSpacingBefore(10);
                tituloDoc.setSpacingAfter(5);
                document.add(tituloDoc);

                //configuracion tabla
                PdfPTable tabla = new PdfPTable(3);
                tabla.setWidthPercentage(100); //espacio que ocupa en la hoja
                tabla.setWidths(new float[]{4, 3, 3});//define la proporcion del ancho de cada columna
                tabla.setSpacingBefore(10);

                //encabezados sin color
                PdfPCell hTitulo = new PdfPCell((new Phrase("Nombre", fontSubtitulo)));
                hTitulo.setBackgroundColor(null);//quita el color de fondo
                hTitulo.setPadding(5);

                PdfPCell hTipo = new PdfPCell((new Phrase("Tipo", fontSubtitulo)));
                hTipo.setBackgroundColor(null);//quita el color de fondo
                hTipo.setPadding(5);

                PdfPCell hEstado = new PdfPCell((new Phrase("Estado", fontSubtitulo)));
                hEstado.setBackgroundColor(null);//quita el color de fondo
                hEstado.setPadding(5);

                tabla.addCell(hTitulo);
                tabla.addCell(hTipo);
                tabla.addCell(hEstado);

                for (DocumentoResponseDto d : documentos) {
                    //celda nommbre
                    PdfPCell nombreCell = new PdfPCell(new Phrase(d.nombre(), fontValorDatos));
                    nombreCell.setPadding(5);
                    //uso de gris claro para diferenciar filas
                    nombreCell.setBackgroundColor(new Color(245, 245, 245));
                    tabla.addCell(nombreCell);

                    //celda tipo
                    PdfPCell tipoCell = new PdfPCell(new Phrase(d.tipo(), fontValorDatos));
                    nombreCell.setPadding(5);
                    //uso de gris claro para diferenciar filas
                    tipoCell.setBackgroundColor(new Color(245, 245, 245));
                    tabla.addCell(tipoCell);

                    //celda estado
                    String estadoTexto = d.activo() ? "HABILITADO" : "INHABILITADO";

                    //celda estado
                    PdfPCell estadoCell = new PdfPCell(new Phrase(estadoTexto, fontValorDatos));
                    estadoCell.setPadding(5);
                    estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                    if (d.activo() == Boolean.TRUE) {
                        estadoCell.setBackgroundColor(new Color(200, 255, 200));
                    } else {
                        estadoCell.setBackgroundColor(new Color(255, 200, 200));
                    }
                    tabla.addCell(estadoCell);
                }

                document.add(tabla);

                if(expedientes.isEmpty()){
                    Paragraph vacio = new Paragraph(
                            "NO EXISTEN EXPEDIENTES REGISTRADOS EN EL SISTEMA", fontValorDatos);
                    vacio.setIndentationLeft(10);
                    document.add(vacio);
                    document.close();
                    return new ByteArrayInputStream(out.toByteArray());
                }else{
                    document.add(Chunk.NEWLINE);//es un salto de linea
                    LineSeparator linea4 = new LineSeparator();
                    linea4.setOffset(-5);
                    document.add(linea4);
                    document.add(Chunk.NEWLINE);

                    // GRAFICA DE PASTEL DEL ESTADO DE LOS EXPEDIENTES
                    long completo = expedientes.stream().filter(e -> e.estadoExpediente() == EstadoExpediente.APROBADO).count();
                    long incompleto = expedientes.stream().filter(e -> e.estadoExpediente() == EstadoExpediente.NO_APROBADO).count();

                    long total = completo + incompleto;

                    double pCompleto = total > 0 ? (completo * 100.0 / total) : 0;
                    double pIncompleto = total > 0 ? (incompleto * 100.0 / total) : 0;

                    DefaultPieDataset dataset = new DefaultPieDataset();
                    dataset.setValue("APROBADO", completo);
                    dataset.setValue("NO APROBADO", incompleto);

                    JFreeChart chart = ChartFactory.createPieChart(
                            "Distribución en el Estado del Expediente",
                            dataset,
                            true,
                            true,
                            false
                    );
                    chart.setBackgroundPaint(Color.WHITE);//color de fondo de todo el cuadro

                    PiePlot plot = (PiePlot) chart.getPlot();
                    plot.setBackgroundPaint(Color.WHITE);//fondo del circulo
                    plot.setOutlineVisible(false); //quita el borde cuadrado del grafico
                    plot.setShadowPaint(null);//quitar la sombre

                    plot.setSectionPaint("APROBADOS", new Color(200, 246, 201));
                    plot.setSectionPaint("NO APROBADOSS", new Color(191, 124, 124));

                    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} : {2}"));//nombre mas porcentaje

                    ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
                    ChartUtils.writeChartAsPNG(chartOut, chart, 400, 300);

                    Image chartImage = Image.getInstance(chartOut.toByteArray());
                    chartImage.scaleToFit(350, 250);//ajusta el tamaño para que sea mas pequeña y quepa en el espacio restante
                    chartImage.setAlignment(Element.ALIGN_CENTER);

                    PdfPTable contenedorImagen = new PdfPTable(1);
                    contenedorImagen.setWidthPercentage(100);

                    PdfPCell celda = new PdfPCell(chartImage);
                    celda.setBorder(PdfPCell.NO_BORDER);//evita que se vea el maco de la tabla
                    celda.setHorizontalAlignment(Element.ALIGN_CENTER);

                    contenedorImagen.addCell(celda);

                    contenedorImagen.setKeepTogether(true);//intenta que la grafica quede en la misma hoja
                    document.add(contenedorImagen);
                    LineSeparator linea5 = new LineSeparator();
                    linea5.setOffset(-5);
                    document.add(linea5);

                    document.add(Chunk.NEWLINE);

                    //DATOS DE LOS ALUMNOS NO APROBADOS
                    Paragraph estadoAlumnos = new Paragraph("EXPEDIENTES NO APROBADOS", fontSubtitulo2);
                    estadoAlumnos.setSpacingBefore(10);
                    estadoAlumnos.setSpacingAfter(5);
                    document.add(estadoAlumnos);

                    List<ExpedienteReporteDto> expedientesNoAprobados =
                            expedientes.stream()
                                    .filter(entry -> entry.estadoExpediente() == EstadoExpediente.NO_APROBADO)
                                    .collect(Collectors.toList());

                    boolean hayExpedientesNoAprobados = expedientes.stream()
                            .anyMatch(e -> e.estadoExpediente() != EstadoExpediente.APROBADO);
                    if (!hayExpedientesNoAprobados) {
                        Paragraph aviso = new Paragraph(
                                "SIN EXPEDIENTES EN ESTADO: NO APROBADO",
                                fontValorDatos
                        );

                        aviso.setSpacingBefore(20);
                        aviso.setAlignment(Element.ALIGN_CENTER);

                        document.add(aviso);
                    } else {
                        //configuracion tabla
                        PdfPTable tablaNoAprobado = new PdfPTable(4);
                        tablaNoAprobado.setWidthPercentage(100); //espacio que ocupa en la hoja
                        tablaNoAprobado.setWidths(new float[]{3, 2, 2, 3});//define la proporcion del ancho de cada columna
                        tablaNoAprobado.setSpacingBefore(10);

                        //encabezados sin color
                        PdfPCell alum = new PdfPCell((new Phrase("Alumno", fontSubtitulo)));
                        alum.setBackgroundColor(null);//quita el color de fondo
                        alum.setPadding(5);

                        PdfPCell estadoExp = new PdfPCell((new Phrase("Estado Expediente", fontSubtitulo)));
                        estadoExp.setBackgroundColor(null);//quita el color de fondo
                        estadoExp.setPadding(5);

                        PdfPCell estadoAlumno = new PdfPCell((new Phrase("Estado Alumno", fontSubtitulo)));
                        estadoAlumno.setBackgroundColor(null);//quita el color de fondo
                        estadoAlumno.setPadding(5);

                        PdfPCell observaciones = new PdfPCell((new Phrase("Observaciones", fontSubtitulo)));
                        observaciones.setBackgroundColor(null);//quita el color de fondo
                        observaciones.setPadding(5);

                        tablaNoAprobado.addCell(alum);
                        tablaNoAprobado.addCell(estadoAlumno);
                        tablaNoAprobado.addCell(estadoExp);
                        tablaNoAprobado.addCell(observaciones);

                        for (ExpedienteReporteDto e : expedientesNoAprobados) {
                            //celda nommbre
                            PdfPCell nombreCell = new PdfPCell(new Phrase(e.alumno(), fontValorDatos));
                            nombreCell.setPadding(5);
                            //uso de gris claro para diferenciar filas
                            nombreCell.setBackgroundColor(new Color(245, 245, 245));
                            tablaNoAprobado.addCell(nombreCell);

                            //celda estado alumno
                            String estadoTexto = e.estadoAlumno() ? "HABILITADO" : "INHABILITADO";

                            PdfPCell estadoCell = new PdfPCell(new Phrase(estadoTexto, fontValorDatos));
                            estadoCell.setPadding(5);
                            estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            if (e.estadoAlumno() == Boolean.TRUE) {
                                estadoCell.setBackgroundColor(new Color(200, 255, 200));
                            } else {
                                estadoCell.setBackgroundColor(new Color(255, 200, 200));
                            }
                            tablaNoAprobado.addCell(estadoCell);

                            //celda estado expediente
                            String estadoFormateado = e.estadoExpediente().toString().replace("_", " ");
                            PdfPCell estadoE = new PdfPCell(new Phrase(estadoFormateado, fontValorDatos));
                            estadoE.setPadding(5);
                            estadoE.setHorizontalAlignment(Element.ALIGN_CENTER);
                            estadoE.setBackgroundColor(new Color(241, 141, 158));
                            tablaNoAprobado.addCell(estadoE);

                            //celda observaciones
                            String obsTexto = (e.Observaciones() == null || e.Observaciones().isEmpty()) ? "SIN OBSERVACIONES" : e.Observaciones();
                            PdfPCell observacionesCell = new PdfPCell(new Phrase(obsTexto, fontValorDatos));
                            nombreCell.setPadding(5);
                            //uso de gris claro para diferenciar filas
                            observacionesCell.setBackgroundColor(new Color(245, 245, 245));
                            tablaNoAprobado.addCell(observacionesCell);
                        }

                        document.add(tablaNoAprobado);
                    }
                    document.add(Chunk.NEWLINE);//es un salto de linea
                    LineSeparator linea2 = new LineSeparator();
                    linea2.setOffset(-5);
                    document.add(linea2);
                    document.add(Chunk.NEWLINE);

                    //DATOS DE LOS ALUMNOS APROBADOS
                    Paragraph estadoAlumnosAprobados = new Paragraph("EXPEDIENTES APROBADOS", fontSubtitulo2);
                    estadoAlumnosAprobados.setSpacingBefore(10);
                    estadoAlumnosAprobados.setSpacingAfter(5);
                    document.add(estadoAlumnosAprobados);

                    List<ExpedienteReporteDto> expedientesAprobados =
                            expedientes.stream()
                                    .filter(entry -> entry.estadoExpediente() == EstadoExpediente.APROBADO)
                                    .collect(Collectors.toList());

                    boolean hayExpedientesAprobados = expedientes.stream()
                            .anyMatch(e -> e.estadoExpediente() != EstadoExpediente.NO_APROBADO);
                    if (!hayExpedientesAprobados) {
                        Paragraph aviso = new Paragraph(
                                "SIN EXPEDIENTES EN ESTADO: APROBADO",
                                fontValorDatos
                        );

                        aviso.setSpacingBefore(20);
                        aviso.setAlignment(Element.ALIGN_CENTER);

                        document.add(aviso);
                    } else {
                        //configuracion tabla
                        PdfPTable tablaAprobado = new PdfPTable(3);
                        tablaAprobado.setWidthPercentage(100); //espacio que ocupa en la hoja
                        tablaAprobado.setWidths(new float[]{4, 3, 3});//define la proporcion del ancho de cada columna
                        tablaAprobado.setSpacingBefore(10);

                        //encabezados sin color
                        PdfPCell alum = new PdfPCell((new Phrase("Alumno", fontSubtitulo)));
                        alum.setBackgroundColor(null);//quita el color de fondo
                        alum.setPadding(5);

                        PdfPCell estadoExp = new PdfPCell((new Phrase("Estado Expediente", fontSubtitulo)));
                        estadoExp.setBackgroundColor(null);//quita el color de fondo
                        estadoExp.setPadding(5);

                        PdfPCell estadoAlumno = new PdfPCell((new Phrase("Estado Alumno", fontSubtitulo)));
                        estadoAlumno.setBackgroundColor(null);//quita el color de fondo
                        estadoAlumno.setPadding(5);

                        tablaAprobado.addCell(alum);
                        tablaAprobado.addCell(estadoAlumno);
                        tablaAprobado.addCell(estadoExp);

                        for (ExpedienteReporteDto e : expedientesAprobados) {
                            //celda nommbre
                            PdfPCell nombreCell = new PdfPCell(new Phrase(e.alumno(), fontValorDatos));
                            nombreCell.setPadding(5);
                            //uso de gris claro para diferenciar filas
                            nombreCell.setBackgroundColor(new Color(245, 245, 245));
                            tablaAprobado.addCell(nombreCell);

                            //celda estado alumno
                            String estadoTexto = e.estadoAlumno() ? "HABILITADO" : "INHABILITADO";

                            PdfPCell estadoCell = new PdfPCell(new Phrase(estadoTexto, fontValorDatos));
                            estadoCell.setPadding(5);
                            estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                            if (e.estadoAlumno() == Boolean.TRUE) {
                                estadoCell.setBackgroundColor(new Color(200, 255, 200));
                            } else {
                                estadoCell.setBackgroundColor(new Color(255, 200, 200));
                            }
                            tablaAprobado.addCell(estadoCell);

                            //celda estado expediente
                            PdfPCell estadoE = new PdfPCell(new Phrase(e.estadoExpediente().toString(), fontValorDatos));
                            estadoE.setPadding(5);
                            estadoE.setHorizontalAlignment(Element.ALIGN_CENTER);
                            estadoE.setBackgroundColor(new Color(200, 255, 200));
                            tablaAprobado.addCell(estadoE);
                        }

                        document.add(tablaAprobado);
                    }
                }
            }

            document.close();


        } catch (Exception e) {
            throw new ReporteException("Error generando el reporte", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
