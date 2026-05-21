package com.app.pga.App.Reporte;

import com.app.pga.App.Exception.ReporteException;
import com.app.pga.App.Models.Dtos.ResponseDto.DetalleDashboardDto;
import com.app.pga.App.Models.Dtos.ResponseDto.InscripcionResponseDto;
import com.app.pga.App.Models.Dtos.ResponseDto.SeguimientoDashboardResponseDto;
import com.app.pga.App.Models.Enum.EstadoTarea;
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
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteSeguimientoAlumnoService {
    public ByteArrayInputStream generarSeguimientoSemanal(List <SeguimientoDashboardResponseDto> semanas, InscripcionResponseDto inscripcion) throws Exception{

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

        try{
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

            Paragraph subtitulo = new Paragraph("REPORTE DEL SEGUIMIENTO SEMANAL DEL ALUMNO", fontSubtitulo);

            subtitulo.setAlignment(Element.ALIGN_CENTER);

            document.add(subtitulo);

            // datos alumno
            //CONFIGURAR TABLA
            PdfPTable datosAlumno = new PdfPTable(4);
            datosAlumno.setWidthPercentage(100);
            datosAlumno.setWidths(new float[]{2,4,2,4});
            datosAlumno.setSpacingBefore(10);

            addFila(datosAlumno, "ALUMNO:", inscripcion.alumno(), fontLabelDatos, fontValorDatos);
            addFila(datosAlumno, "ESTADO:", inscripcion.estado() ? "ACTIVO" : "INACTIVO", fontLabelDatos, fontValorDatos);
            addFila(datosAlumno, "PERIODO:", inscripcion.fechaInicio().toString() + " - " + inscripcion.fechaFin().toString(), fontLabelDatos, fontValorDatos);
            addFila(datosAlumno, "GRUPO:", inscripcion.nombreGrupo(), fontLabelDatos, fontValorDatos);

            document.add(datosAlumno);
            document.add(Chunk.NEWLINE);


            //grafica de lineas del seguimiento
            // tabla contenedora para evitar que la gráfica se separe del nombre
            PdfPTable contenedorAlumno = new PdfPTable(1);
            contenedorAlumno.setWidthPercentage(95);
            contenedorAlumno.setKeepTogether(true);
            contenedorAlumno.setSpacingAfter(15);

            List<Integer> xData = new ArrayList<>();
            List<Long> yData= new ArrayList<>();

            for (SeguimientoDashboardResponseDto semana : semanas ){
                xData.add(semana.numeroSemana().intValue());
                yData.add(semana.porcentajeAvance());
            }

            //crear dataset
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for(SeguimientoDashboardResponseDto semana : semanas){
                dataset.addValue(semana.porcentajeAvance(),
                        "Avance",
                        "S" + semana.numeroSemana());
            }

            JFreeChart indivChart = ChartFactory.createLineChart(
                    "Avance Semanal",
                    "Semana",
                    "Porcentaje",
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false
            );

            // Personalización de la gráfica
            indivChart.setBackgroundPaint(Color.WHITE);
            CategoryPlot indivPlot = indivChart.getCategoryPlot();
            indivPlot.setBackgroundPaint(new Color(252, 252, 252));


            //convertir imagen
            ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(
                    chartOut,
                    indivChart,
                    500, 300
            );

            //insertar al PDF
            Image indivImage = Image.getInstance(chartOut.toByteArray());
            indivImage.setAlignment(Element.ALIGN_CENTER);

            PdfPCell celdaGrafica = new PdfPCell(indivImage);
            celdaGrafica.setBorder(Rectangle.NO_BORDER);
            celdaGrafica.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaGrafica.setPaddingBottom(10);
            contenedorAlumno.addCell(celdaGrafica);

            LineSeparator linea2 = new LineSeparator();
            linea2.setOffset(-5);
            document.add(linea2);
            document.add(Chunk.NEWLINE);
            document.add(contenedorAlumno);

            document.add(Chunk.NEWLINE);
            LineSeparator linea3 = new LineSeparator();
            linea3.setOffset(-5);
            document.add(linea3);
            document.add(Chunk.NEWLINE);

            //grafica de pastel
            long enProgreso = semanas.stream()
                    .flatMap(semana -> semana.detalles().stream())
                    .filter(detalle -> detalle.estadoSemana() == EstadoTarea.En_Progreso)
                    .count();

            long completadas = semanas.stream()
                    .flatMap(semana -> semana.detalles().stream())
                    .filter(detalle -> detalle.estadoSemana() == EstadoTarea.Completada)
                    .count();

            long pendientes = semanas.stream()
                    .flatMap(semana -> semana.detalles().stream())
                    .filter(detalle -> detalle.estadoSemana() == EstadoTarea.Sin_Iniciar)
                    .count();

            DefaultPieDataset datasetpastel = new DefaultPieDataset();
            datasetpastel.setValue("En Progreso", enProgreso);
            datasetpastel.setValue("Completadas", completadas);
            datasetpastel.setValue("Sin Iniciar", pendientes);

            JFreeChart pieChart = ChartFactory.createPieChart(
                    "Estado de Actividades",
                    datasetpastel,
                    true,
                    true,
                    false
            );

            pieChart.setBackgroundPaint(Color.WHITE);
            PiePlot plot = (PiePlot) pieChart.getPlot();
            plot.setBackgroundPaint(Color.WHITE);//fondo del circulo
            plot.setOutlineVisible(false); //quita el borde cuadrado del grafico
            plot.setShadowPaint(null);//quitar la sombre

            plot.setSectionPaint("Completadas", new Color (200, 246, 201));
            plot.setSectionPaint("Sin Iniciar", new Color (191, 130, 124));
            plot.setSectionPaint("En Progreso", new Color (248, 247, 165));

            plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} : {2}"));//nombre mas porcentaje

            ByteArrayOutputStream chartOutPie = new ByteArrayOutputStream();
            ChartUtils.writeChartAsPNG(chartOutPie, pieChart, 400, 300);

            Image chartImage = Image.getInstance(chartOutPie.toByteArray());
            chartImage.scaleToFit(350,250);//ajusta el tamaño para que sea mas pequeña y quepa en el espacio restante
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


            //tabla con el seguimiento por semana
            for (SeguimientoDashboardResponseDto semana : semanas) {

                document.add(Chunk.NEWLINE);

                // TITULO SEMANA
                Paragraph semanaTitulo = new Paragraph(
                        "SEMANA " + semana.numeroSemana(),
                        fontSubtitulo2
                );

                semanaTitulo.setSpacingBefore(10);
                semanaTitulo.setSpacingAfter(5);

                document.add(semanaTitulo);

                // DATOS SEMANA
                Paragraph infoSemana = new Paragraph(
                        "Periodo: "
                                + semana.semanaInicio()
                                + " - "
                                + semana.semanaFin()
                                + "    |    Avance general: "
                                + semana.porcentajeAvance()
                                + "%",
                        fontValorDatos
                );

                infoSemana.setSpacingAfter(10);

                document.add(infoSemana);

                // TABLA DETALLES
                PdfPTable tabla = new PdfPTable(6);

                tabla.setWidthPercentage(100);

                tabla.setWidths(new float[]{3, 3, 2, 2, 2, 3});

                // ENCABEZactividadADOS
                PdfPCell hActividad = new PdfPCell((new Phrase("Actividad", fontSubtitulo2)));
                hActividad.setBackgroundColor(null);//quita el color de fondo
                hActividad.setPadding(5);
                hActividad.setBorder(Rectangle.NO_BORDER);

                PdfPCell hEstado = new PdfPCell((new Phrase("Estado", fontSubtitulo2)));
                hEstado.setBackgroundColor(null);//quita el color de fondo
                hEstado.setPadding(5);
                hEstado.setBorder(Rectangle.NO_BORDER);

                PdfPCell hAvanceSem = new PdfPCell((new Phrase("Avance Sem.", fontSubtitulo2)));
                hAvanceSem.setBackgroundColor(null);//quita el color de fondo
                hAvanceSem.setPadding(5);
                hAvanceSem.setBorder(Rectangle.NO_BORDER);

                PdfPCell hAvanceGlobal = new PdfPCell((new Phrase("Avance Global", fontSubtitulo2)));
                hAvanceGlobal.setBackgroundColor(null);//quita el color de fondo
                hAvanceGlobal.setPadding(5);
                hAvanceGlobal.setBorder(Rectangle.NO_BORDER);

                PdfPCell hEntrega = new PdfPCell((new Phrase("Entrega", fontSubtitulo2)));
                hEntrega.setBackgroundColor(null);//quita el color de fondo
                hEntrega.setPadding(5);
                hEntrega.setBorder(Rectangle.NO_BORDER);

                PdfPCell hObservaciones = new PdfPCell((new Phrase("Observaciones", fontSubtitulo2)));
                hObservaciones.setBackgroundColor(null);//quita el color de fondo
                hObservaciones.setPadding(5);
                hObservaciones.setBorder(Rectangle.NO_BORDER);

                tabla.addCell(hActividad);
                tabla.addCell(hEstado);
                tabla.addCell(hAvanceSem);
                tabla.addCell(hAvanceGlobal);
                tabla.addCell(hEntrega);
                tabla.addCell(hObservaciones);

                // FILAS
                for (DetalleDashboardDto detalle : semana.detalles()) {
                    //titulo de actividad
                    PdfPCell nombreCell = new PdfPCell(new Phrase(detalle.tituloActividad(), fontValorDatos));
                    nombreCell.setPadding(5);
                    nombreCell.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                    nombreCell.setBorder(Rectangle.NO_BORDER);
                    nombreCell.setBorder(Rectangle.BOTTOM);
                    nombreCell.setBorderColor(new Color(96, 93, 93));
                    tabla.addCell(nombreCell);

                    //estado semanal
                    PdfPCell estadoSemCell = new PdfPCell(new Phrase(detalle.estadoSemana().name(), fontValorDatos));
                    estadoSemCell.setPadding(5);
                    estadoSemCell.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                    estadoSemCell.setBorder(Rectangle.NO_BORDER);
                    estadoSemCell.setBorder(Rectangle.BOTTOM);
                    estadoSemCell.setBorderColor(new Color(96, 93, 93));
                    tabla.addCell(estadoSemCell);

                    //detalla avance real
                    PdfPCell estadoGlobalCell = new PdfPCell(new Phrase(detalle.avanceReal() + "%", fontValorDatos));
                    estadoGlobalCell.setPadding(5);
                    estadoGlobalCell.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                    estadoGlobalCell.setBorder(Rectangle.NO_BORDER);
                    estadoGlobalCell.setBorder(Rectangle.BOTTOM);
                    estadoGlobalCell.setBorderColor(new Color(96, 93, 93));
                    tabla.addCell(estadoGlobalCell);

                    //avance global
                    PdfPCell estadoGCell = new PdfPCell(new Phrase(detalle.avanceGlobalActividad() + "%", fontValorDatos));
                    estadoGCell.setPadding(5);
                    estadoGCell.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                    estadoGCell.setBorder(Rectangle.NO_BORDER);
                    estadoGCell.setBorder(Rectangle.BOTTOM);
                    estadoGCell.setBorderColor(new Color(96, 93, 93));
                    tabla.addCell(estadoGCell);

                    //req  entrega
                    PdfPCell reqEntrega = new PdfPCell(new Phrase(detalle.requiereEntrega() ? "SI" : "NO", fontValorDatos));
                    reqEntrega.setPadding(5);
                    reqEntrega.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                    reqEntrega.setBorder(Rectangle.NO_BORDER);
                    reqEntrega.setBorder(Rectangle.BOTTOM);
                    reqEntrega.setBorderColor(new Color(96, 93, 93));
                    tabla.addCell(reqEntrega);

                    // observaciones
                    PdfPCell observaciones = new PdfPCell(new Phrase(detalle.observacionesAlumno() != null
                            ? detalle.observacionesAlumno()
                            : "-", fontValorDatos));
                    observaciones.setPadding(5);
                    observaciones.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                    observaciones.setBorder(Rectangle.NO_BORDER);
                    observaciones.setBorder(Rectangle.BOTTOM);
                    observaciones.setBorderColor(new Color(96, 93, 93));
                    tabla.addCell(observaciones);
                }
                document.add(tabla);
            }

            document.close();
        } catch (Exception e){
            throw new ReporteException("Error generando reporte del seguimiento semnaal", e);
        }

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

    private void addHeader(PdfPTable table, String text){
        Font font = new Font(Font.HELVETICA, 9, Font.BOLD);
        PdfPCell header = new PdfPCell(new Phrase(text, font));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        table.addCell(header);
    }
}
