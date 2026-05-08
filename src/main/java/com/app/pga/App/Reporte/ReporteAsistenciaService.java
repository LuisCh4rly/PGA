package com.app.pga.App.Reporte;

import com.app.pga.App.Exception.ReporteException;
import com.app.pga.App.Models.Dtos.ReporteAsistenciaGrupoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.AlumnoGrupoDto;
import com.app.pga.App.Models.Dtos.ResponseDto.GrupoResponseDto;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.EstadoAsistencia;
import com.app.pga.App.Services.Implements.SesionService;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteAsistenciaService {
    private final SesionService sesionService;
    //general un pdf en memoria - lo devuelve como flujo de bytes oara qye el controller lo descargue
    public ByteArrayInputStream generarReporteAsistencia(List<ReporteAsistenciaGrupoDto>sesiones, GrupoResponseDto grupo, List<AlumnoGrupoDto> inscripciones)throws Exception{
        // fecha
        LocalDate hoy = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaConsulta = hoy.format(formatter);

        //instancia document, representa al pdf en construccion, congigurado con un tamaño de pagina A4
        Document document = new Document(PageSize.A4);
        //buffer de memoria donde se escribirá el pdf generado, se utiliza de esta forma para no crear archivos temporales
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //vincula el documetno con el buffer de datos
        PdfWriter writer = PdfWriter.getInstance(document, out);
        //establece la relación de escritos y documento, activa el footer en automatico en cada pagina
        writer.setPageEvent(new Footer());

        try{
            //Aqui se abre el documento para escritura
            document.open();

            //fuentes, se definien estilos reutilizables
            Font fontTitulo = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font fontFecha = new Font(Font.HELVETICA, 8, Font.NORMAL);
            Font fontSubtitulo = new Font(Font.HELVETICA, 14, Font.NORMAL);
            Font fontSubtitulo2 = new Font(Font.HELVETICA, 11, Font.NORMAL);
            Font fontAlertaRoja = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(211,47,47));
            Font fontLabelDatos = new Font(Font.HELVETICA, 9, Font.BOLD);
            Font fontValorDatos = new Font(Font.HELVETICA, 9, Font.NORMAL);

            //CABECERA
            //creacion de tabla que se divide en tres columnas
            PdfPTable header = new PdfPTable(3);
            header.setWidthPercentage(100);
            header.setWidths(new float[]{1,4,1});

            //LOGO, lo busca dentro de recursos estaticos
            PdfPCell logoCell;
            URL url = getClass().getResource("/static/logo_infotec.png");
            if (url !=null){
                Image logoInfotec = Image.getInstance(url);
                logoInfotec.scaleToFit(70, 70);
                logoCell = new PdfPCell(logoInfotec);
            }else{
                logoCell = new PdfPCell(new Phrase(""));
            }
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            header.addCell(logoCell);

            //titulo - crea un objeto con la infor del titulo y la tipografia personalizada
            Paragraph titulo = new Paragraph("PLATAFORMA DE GESTIÓN ACADÉMICA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            PdfPCell tituloCell = new PdfPCell(titulo);

            tituloCell.setBorder(Rectangle.NO_BORDER);
            tituloCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            header.addCell(tituloCell);

            //fecha
            //crea un objeto con la info de la fecha y la tipografia personalizada
            Paragraph fecha = new Paragraph("Fecha: " + fechaConsulta, fontFecha);
            fecha.setAlignment(Element.ALIGN_RIGHT);

            PdfPCell fechaCell = new PdfPCell(fecha);

            fechaCell.setBorder(Rectangle.NO_BORDER);
            fechaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            fechaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            header.addCell(fechaCell);

            //Añade la cabecera al documento
            document.add(header);

            //subtitulo ---- Reporte de Asistencia--

            LineSeparator linea = new LineSeparator();//es un detalle visual
            linea.setOffset(-3);//espacio visual hacia abajo para que no quede pegado con las letras
            document.add(linea);

            document.add(Chunk.NEWLINE);//es un salto de linea

            Paragraph subtitulo = new Paragraph("REPORTE DE ASISTENCIA", fontSubtitulo);

            subtitulo.setAlignment(Element.ALIGN_CENTER);

            document.add(subtitulo);

            //datos del grupo
            //subtitulo
            Paragraph datos = new Paragraph("DATOS DEL GRUPO", fontSubtitulo2);
            datos.setSpacingBefore(10);
            datos.setSpacingAfter(5);
            document.add((datos));
            //configurar tabla
            PdfPTable datosGrupo = new PdfPTable(6);
            datosGrupo.setWidthPercentage(100);
            datosGrupo.setWidths(new float[]{1,1,1,2,1,3});

            addFila(datosGrupo, "REGISTRO:", grupo.created_at().toString(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "GRUPO:", grupo.nombre(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "CURSO:", grupo.nombreCurso(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "ESTADO:", (grupo.estado() == Estado.HABILITADO) ? "ACTIVO" : "INACTIVO", fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "PERIODO:", grupo.periodo(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "DOCENTE:", grupo.docente(), fontLabelDatos, fontValorDatos);

            document.add(datosGrupo);

            //INSCRIPCIONES
            Paragraph tituloInscripciones = new Paragraph("INSCRIPCIONES", fontSubtitulo2);
            tituloInscripciones.setSpacingBefore(10);
            tituloInscripciones.setSpacingAfter(5);
            document.add(tituloInscripciones);

            if(inscripciones.isEmpty()){
                Paragraph vacio = new Paragraph(
                        "El grupo no cuenta con inscripciones registradas.", fontValorDatos);
                vacio.setIndentationLeft(10);
                document.add(vacio);
            }

            //configuracion tabla
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100); //espacio que ocupa en la hoja
            tabla.setWidths(new float[]{5,5});//define la proporcion del ancho de cada columna
            tabla.setSpacingBefore(10);


            for (AlumnoGrupoDto a : inscripciones) {

                PdfPCell h1 = new PdfPCell(new Phrase("- " + a.nombre()+" "+a.apellidoPaterno()+" "+a.apellidoMaterno(), fontLabelDatos));

                //estilos
                h1.setBorder(Rectangle.NO_BORDER);
                h1.setBackgroundColor(new Color(251, 243, 243));
                h1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.setSpacingAfter(5);
                tabla.addCell(h1);
            }

            //si el numero de alumnos es impar, se van a completar con celdas vacias, para evitar un error en la ultima fila
            if (inscripciones.size() % 2 != 0) {
                PdfPCell empty = new PdfPCell(new Phrase(""));
                empty.setBorder(Rectangle.NO_BORDER);
                tabla.addCell(empty);
            }
            document.add(tabla);

            LineSeparator linea2 = new LineSeparator();
            linea2.setOffset(-5);
            document.add(linea2);

            document.add(Chunk.NEWLINE);

            if(sesiones.isEmpty()){
                Paragraph vacio = new Paragraph(
                        "El grupo no cuenta con sesiones registradas.", fontValorDatos);
                vacio.setIndentationLeft(10);
                document.add(vacio);
            } else{
                //se verifica que las seiones hayan iniciado
                boolean haySesionesIniciadas = sesiones.stream().anyMatch(s -> s.estado() != EstadoAsistencia.SIN_INICIAR);
                //usar anyMacth, java deja de buscar en cuanto encuentra la primera sesion iniciada.

                if(!haySesionesIniciadas){
                    // si todas las sesiones estan sin iniciar, se pone la leyenda
                    Paragraph aviso = new Paragraph("Las sesiones programadas para este grupo aún no se han iniciado. "
                    +"El contenido estadístico se generará una vez que se registre la primera asistencia. ", fontValorDatos);
                    aviso.setSpacingBefore(20);
                    aviso.setAlignment(Element.ALIGN_CENTER);
                    document.add(aviso);
                    //termina el flujo y no se hacen lods graficos
                } else{
                    //agrupaciones por sesiones
                    Map<Long, List<ReporteAsistenciaGrupoDto>> sesionesAgrupadas = sesiones.stream()
                            .filter(s -> s.estado() != EstadoAsistencia.SIN_INICIAR)//filtro para quitar las sesiones que aun no inician
                            .sorted(Comparator.comparing(ReporteAsistenciaGrupoDto :: fechaSesion))
                            .collect(Collectors.groupingBy(ReporteAsistenciaGrupoDto :: idSesion, LinkedHashMap::new, Collectors.toList()));

                    //grafico de lineas por alumnos
                    DefaultCategoryDataset lineDataset = new DefaultCategoryDataset();
                    //se ordenan las sesiones por fecha  para que la linea siga un orden cronologico
                    sesionesAgrupadas.forEach((idSesion, listaAsistencias)->{
                        ReporteAsistenciaGrupoDto sesionInfo = listaAsistencias.get(0);
                        String fechaLabel = sesionInfo.fechaSesion().format(DateTimeFormatter.ofPattern("dd/MM"));

                        long totalAsistieron = listaAsistencias.stream()
                                .filter(a -> a.estado() == EstadoAsistencia.ASISTIO)
                                .count();

                        //Asistenca es la serie, fechaLabel es el punto en el tiempo
                        lineDataset.addValue(totalAsistieron, "Alumnos Presentes", fechaLabel);
                    });

                    //crear y personalizar la grafica de lineas
                    JFreeChart lineChart = ChartFactory.createLineChart(
                            "Tendencia de Asistencia por Sesión",
                            "Fecha de Sesión",
                            "Número de Alumnos",
                            lineDataset
                    );

                    lineChart.setBackgroundPaint(Color.WHITE);
                    CategoryPlot linePlot = lineChart.getCategoryPlot();
                    linePlot.setBackgroundPaint(Color.WHITE);
                    linePlot.setRangeGridlinePaint(Color.LIGHT_GRAY);

                    //personalizacion de lineas
                    LineAndShapeRenderer lineRenderer = new LineAndShapeRenderer();
                    lineRenderer.setSeriesPaint(0, new Color(33, 150, 243)); // Azul brillante
                    lineRenderer.setSeriesStroke(0, new BasicStroke(2.0f));   // Línea más gruesa
                    lineRenderer.setSeriesShapesVisible(0, true);            // Mostrar puntos en cada fecha
                    //convertir a imagen iText
                    ByteArrayOutputStream lineOut = new ByteArrayOutputStream();
                    ChartUtils.writeChartAsPNG(lineOut, lineChart, 500, 300);
                    Image lineImage = Image.getInstance(lineOut.toByteArray());
                    lineImage.setAlignment(Element.ALIGN_CENTER);
                    lineImage.scaleToFit(450, 250);

                    document.add(lineImage);
                    document.add(Chunk.NEWLINE);

                    //alumnos en riesgo
                    //se agrupa por alumno y se cuentan las faltas
                    Map<String , Long> faltasPorAlumno = sesiones.stream()
                            .filter(s ->s.estado() == EstadoAsistencia.FALTO)
                            .collect(Collectors.groupingBy(ReporteAsistenciaGrupoDto::nombreAlumno, Collectors.counting()));
                    //definicion del umbral (mas de tres faltas en riesgo
                    int umbralRiesgo = 3;
                    List<String> alumnosEnRiesgo = faltasPorAlumno.entrySet().stream()
                            .filter(entry -> entry.getValue() >= umbralRiesgo)
                            .map(entry -> entry.getKey() + "(" + entry.getValue() + " faltas)")
                            .toList();

                    //estilo de alerta
                    if (!alumnosEnRiesgo.isEmpty()){
                        document.add(new LineSeparator());

                        Paragraph alertaTitulo = new Paragraph("ALERTA: ALUMNOS EN RIESGO (EXCESO DE FALTAS)", fontAlertaRoja);
                        alertaTitulo.setSpacingBefore(5);
                        alertaTitulo.setSpacingAfter(5);
                        document.add(alertaTitulo);


                        for(String info : alumnosEnRiesgo){
                            //limpieza segura del nombre
                            String nombreAlumno = info.contains("(") ? info.substring(0, info.indexOf("(")).trim() : info;
                            // tabla contenedora para evitar que la gráfica se separe del nombre
                            PdfPTable contenedorAlumno = new PdfPTable(1);
                            contenedorAlumno.setWidthPercentage(95);
                            contenedorAlumno.setKeepTogether(true);
                            contenedorAlumno.setSpacingAfter(15);

                            // Celda para el nombre y el total de faltas
                            PdfPCell celdaNombre = new PdfPCell();
                            celdaNombre.setBorder(Rectangle.NO_BORDER);
                            Paragraph pNombre = new Paragraph("Historial de: " + info, fontLabelDatos);
                            pNombre.setSpacingAfter(5);
                            celdaNombre.addElement(pNombre);

                            // Leyenda de valores (0,1,2) pequeña debajo del nombre
                            Font fontMini = new Font(Font.HELVETICA, 7, Font.ITALIC, Color.GRAY);
                            Paragraph pLeyenda = new Paragraph("Escala: 2 = Asistió | 1 = Retardo | 0 = Falta", fontMini);
                            celdaNombre.addElement(pLeyenda);
                            contenedorAlumno.addCell(celdaNombre);

                            // dataset para el alumno específico
                            DefaultCategoryDataset individualDataset = new DefaultCategoryDataset();

                            // se filtra por las sesiones de este alumno específico y se ordenan por fecha
                            List<ReporteAsistenciaGrupoDto> historialAlumno = sesiones.stream()
                                    .filter(s -> s.estado() != EstadoAsistencia.SIN_INICIAR)
                                    .filter(s -> s.nombreAlumno().equals(nombreAlumno))
                                    .sorted(Comparator.comparing(ReporteAsistenciaGrupoDto::fechaSesion))
                                    .toList();
                            //Si el historial esta vacio despues de filtrar, no graficar nada para el alumno
                            if (historialAlumno.isEmpty()) continue;

                            for (ReporteAsistenciaGrupoDto reg : historialAlumno) {
                                int valorEstado = (reg.estado() == EstadoAsistencia.ASISTIO) ? 2 :
                                        (reg.estado() == EstadoAsistencia.RETARDO) ? 1 : 0;

                                String fechaLabel = reg.fechaSesion().format(DateTimeFormatter.ofPattern("dd/MM"));
                                individualDataset.addValue(valorEstado, "Asistencia", fechaLabel);
                            }

                            // se crear la gráfica de líneas pequeña
                            JFreeChart indivChart = ChartFactory.createLineChart(
                                    null, null, null, individualDataset, // Sin títulos para que sea limpia
                                    PlotOrientation.VERTICAL, false, false, false
                            );

                            // Personalización de la gráfica
                            indivChart.setBackgroundPaint(Color.WHITE);
                            CategoryPlot indivPlot = indivChart.getCategoryPlot();
                            indivPlot.setBackgroundPaint(new Color(252, 252, 252));

                            // Ajustar el eje vertical para que siempre muestre de 0 a 2
                            NumberAxis rangeAxis = (NumberAxis) indivPlot.getRangeAxis();
                            rangeAxis.setRange(-0.5, 2.5);
                            rangeAxis.setTickUnit(new NumberTickUnit(1));

                            LineAndShapeRenderer indivRenderer = new LineAndShapeRenderer();
                            indivRenderer.setSeriesPaint(0, new Color(211, 47, 47)); // Rojo para resaltar riesgo
                            indivRenderer.setSeriesShapesVisible(0, true);
                            indivPlot.setRenderer(indivRenderer);

                            // Convertir a imagen e insertar en el PDF
                            ByteArrayOutputStream indivOut = new ByteArrayOutputStream();
                            ChartUtils.writeChartAsPNG(indivOut, indivChart, 400, 120); // Gráfica bajita/achatada
                            Image indivImage = Image.getInstance(indivOut.toByteArray());
                            indivImage.setAlignment(Element.ALIGN_CENTER);

                            PdfPCell celdaGrafica = new PdfPCell(indivImage);
                            celdaGrafica.setBorder(Rectangle.NO_BORDER);
                            celdaGrafica.setHorizontalAlignment(Element.ALIGN_CENTER);
                            celdaGrafica.setPaddingBottom(10);
                            contenedorAlumno.addCell(celdaGrafica);

                            document.add(contenedorAlumno);
                            //document.add(new Paragraph(" ")); // Espacio entre alumnos
                        }
                    }

                    //conteo de estados
                    long asistio = sesiones.stream().filter(a -> a.estado() == EstadoAsistencia.ASISTIO).count();
                    long falto = sesiones.stream().filter(a -> a.estado() == EstadoAsistencia.FALTO).count();
                    long retardo = sesiones.stream().filter(a -> a.estado() == EstadoAsistencia.RETARDO).count();

                    long total = asistio + falto + retardo;

                    double pAsistio = total > 0 ? (asistio * 100.0 / total) : 0;
                    double pFalto = total > 0 ? (falto * 100.0 / total) : 0;
                    double pRetardo = total > 0 ? (retardo * 100.0 / total) : 0;

                    LineSeparator linea3 = new LineSeparator();
                    linea3.setOffset(-5);
                    document.add(linea3);

                    //bloque de datos vista general
                    document.add(Chunk.NEWLINE);
                    Paragraph tituloResumen = new Paragraph("RESUMEN GENERAL" , fontSubtitulo2);
                    tituloResumen.setSpacingAfter(15f);
                    document.add(tituloResumen);

                    PdfPTable resumen = new PdfPTable(3);
                    resumen.setWidthPercentage(70);
                    resumen.setWidths(new float[]{4, 3, 3});//define la proporcion del ancho de cada columna
                    resumen.setSpacingBefore(10);

                    //encabezados sin color
                    PdfPCell estado = new PdfPCell((new Phrase("Estado", fontSubtitulo)));
                    estado.setBackgroundColor(null);//quita el color de fondo
                    estado.setPadding(5);
                    estado.setBorder(Rectangle.NO_BORDER);

                    PdfPCell cantidad = new PdfPCell((new Phrase("Cantidad", fontSubtitulo)));
                    cantidad.setBackgroundColor(null);//quita el color de fondo
                    cantidad.setPadding(5);
                    cantidad.setBorder(Rectangle.NO_BORDER);

                    PdfPCell porcentaje = new PdfPCell((new Phrase("Porcentaje", fontSubtitulo)));
                    porcentaje.setBackgroundColor(null);//quita el color de fondo
                    porcentaje.setPadding(5);
                    porcentaje.setBorder(Rectangle.NO_BORDER);

                    resumen.addCell(estado);
                    resumen.addCell(cantidad);
                    resumen.addCell(porcentaje);

                    //celdad
                    PdfPCell asistieron = new PdfPCell(new Phrase("Asistieron", fontValorDatos));
                    asistieron.setPadding(5);
                    asistieron.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    asistieron.setBorder(Rectangle.NO_BORDER);
                    asistieron.setBorder(Rectangle.BOTTOM);
                    asistieron.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(asistieron);

                    PdfPCell asistieron1 = new PdfPCell(new Phrase(String.valueOf(asistio), fontValorDatos));
                    asistieron1.setPadding(5);
                    asistieron1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    asistieron1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    asistieron1.setBorder(Rectangle.NO_BORDER);
                    asistieron1.setBorder(Rectangle.BOTTOM);
                    asistieron1.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(asistieron1);

                    PdfPCell asistieron2 = new PdfPCell(new Phrase(String.format("%.2f %%", pAsistio), fontValorDatos));
                    asistieron2.setPadding(5);
                    asistieron2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    asistieron2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    asistieron2.setBorder(Rectangle.NO_BORDER);
                    asistieron2.setBorder(Rectangle.BOTTOM);
                    asistieron2.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(asistieron2);

                    PdfPCell faltaron = new PdfPCell(new Phrase("Faltaron", fontValorDatos));
                    faltaron.setPadding(5);
                    faltaron.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    faltaron.setBorder(Rectangle.NO_BORDER);
                    faltaron.setBorder(Rectangle.BOTTOM);
                    faltaron.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(faltaron);

                    PdfPCell faltaron1 = new PdfPCell(new Phrase(String.valueOf(falto), fontValorDatos));
                    faltaron1.setPadding(5);
                    faltaron1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    faltaron1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    faltaron1.setBorder(Rectangle.NO_BORDER);
                    faltaron1.setBorder(Rectangle.BOTTOM);
                    faltaron1.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(faltaron1);

                    PdfPCell faltaron2 = new PdfPCell(new Phrase(String.format("%.2f %%", pFalto), fontValorDatos));
                    faltaron2.setPadding(5);
                    faltaron2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    faltaron2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    faltaron2.setBorder(Rectangle.NO_BORDER);
                    faltaron2.setBorder(Rectangle.BOTTOM);
                    faltaron2.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(faltaron2);

                    PdfPCell retardos = new PdfPCell(new Phrase("Retardos", fontValorDatos));
                    retardos.setPadding(5);
                    retardos.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    retardos.setBorder(Rectangle.NO_BORDER);
                    retardos.setBorder(Rectangle.BOTTOM);
                    retardos.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(retardos);

                    PdfPCell retardos1 = new PdfPCell(new Phrase(String.valueOf(retardo), fontValorDatos));
                    retardos1.setPadding(5);
                    retardos1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    retardos1.setHorizontalAlignment(Element.ALIGN_CENTER);
                    retardos1.setBorder(Rectangle.NO_BORDER);
                    retardos1.setBorder(Rectangle.BOTTOM);
                    retardos1.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(retardos1);

                    PdfPCell retardos2 = new PdfPCell(new Phrase(String.format("%.2f %%", pRetardo), fontValorDatos));
                    retardos2.setPadding(5);
                    retardos2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                    retardos2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    retardos2.setBorder(Rectangle.NO_BORDER);
                    retardos2.setBorder(Rectangle.BOTTOM);
                    retardos2.setBorderColor(new Color(96, 93, 93));
                    resumen.addCell(retardos2);

                    document.add(resumen);
                    document.add(Chunk.NEWLINE);
                    LineSeparator linea4 = new LineSeparator();
                    linea4.setOffset(-5);
                    document.add(linea4);
                    document.add(Chunk.NEWLINE);

                    //grafica de pastel
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    dataset.setValue("Asistieron", asistio);
                    dataset.setValue("Faltaron", falto);
                    dataset.setValue("Retardos", retardo);

                    JFreeChart chart = ChartFactory.createPieChart(
                            "Distribución de Asistencias",
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

                    plot.setSectionPaint("Asistieron", new Color (200, 246, 201));
                    plot.setSectionPaint("Faltaron", new Color (191, 130, 124));
                    plot.setSectionPaint("Retardos", new Color (248, 247, 165));

                    plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} : {2}"));//nombre mas porcentaje

                    ByteArrayOutputStream chartOut = new ByteArrayOutputStream();
                    ChartUtils.writeChartAsPNG(chartOut, chart, 400, 300);

                    Image chartImage = Image.getInstance(chartOut.toByteArray());
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

                    //agrupamos por mes
                    DateTimeFormatter formatterMes = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es", "ES"));
                    Map<String, List<ReporteAsistenciaGrupoDto>> mesesAgrupados = sesiones.stream()
                            .sorted(Comparator.comparing(ReporteAsistenciaGrupoDto :: fechaSesion))
                            .collect(Collectors.groupingBy( dto -> dto.fechaSesion().format(formatterMes),
                                    LinkedHashMap:: new,
                                    Collectors.toList()));//agrupa por mes-año

                    //grafica de barras
                    DefaultCategoryDataset barDataset = new DefaultCategoryDataset();
                    for (Map.Entry<String, List<ReporteAsistenciaGrupoDto>> entry : mesesAgrupados.entrySet()) {
                        String nombreMes = entry.getKey();
                        List<ReporteAsistenciaGrupoDto> lista = entry.getValue();

                        long a = lista.stream().filter(x -> x.estado() == EstadoAsistencia.ASISTIO).count();
                        long f = lista.stream().filter(x -> x.estado() == EstadoAsistencia.FALTO).count();
                        long r = lista.stream().filter(x -> x.estado() == EstadoAsistencia.RETARDO).count();

                        barDataset.addValue(a, "Asistieron", nombreMes);
                        barDataset.addValue(f, "Faltaron", nombreMes);
                        barDataset.addValue(r, "Retardos", nombreMes);
                    }

                    //aqui se crea la grafica
                    JFreeChart barChart = ChartFactory.createBarChart(
                            "Asistencia por Mes",
                            "Mes",
                            "Cantidad",
                            barDataset
                    );
                    barChart.setBackgroundPaint(Color.WHITE);

                    CategoryPlot barPlot = barChart.getCategoryPlot();
                    barPlot.setBackgroundPaint(Color.WHITE);
                    barPlot.setRangeGridlinePaint(Color.LIGHT_GRAY);

                    BarRenderer renderer = (BarRenderer) barPlot.getRenderer();

                    renderer.setSeriesPaint(0, new Color(145, 214, 148));  // verde
                    renderer.setSeriesPaint(1, new Color(228, 162, 157));  // rojo
                    renderer.setSeriesPaint(2, new Color(248, 247, 165));  // amarillo

                    //convertir imagen y ajustar tamaño
                    ByteArrayOutputStream barOut = new ByteArrayOutputStream();
                    ChartUtils.writeChartAsPNG(barOut, barChart, 500, 300);
                    Image barImage = Image.getInstance(barOut.toByteArray());
                    barImage.scaleToFit(480,280);//reduccion para asegurar que quepa

                    //tabla contrenedora
                    PdfPTable contenedorGrafica = new PdfPTable(1);
                    contenedorGrafica.setWidthPercentage(100);
                    contenedorGrafica.setSpacingBefore(10);
                    contenedorGrafica.setSpacingAfter(10);

                    PdfPCell celdaImag = new PdfPCell(barImage);
                    celdaImag.setBorder(PdfPCell.NO_BORDER);
                    celdaImag.setHorizontalAlignment(Element.ALIGN_CENTER);

                    contenedorGrafica.addCell(celdaImag);
                    contenedorGrafica.setKeepTogether(true);//evita que se separe si hay espacio

                    document.add(contenedorGrafica);

                    LineSeparator linea6 = new LineSeparator();
                    linea6.setOffset(-5);
                    document.add(linea6);
                    document.add(Chunk.NEWLINE);

                    //tablas
                    for (List<ReporteAsistenciaGrupoDto> listaSesion : sesionesAgrupadas.values()) {

                        ReporteAsistenciaGrupoDto info = listaSesion.get(0);

                        // título de sesión
                        Paragraph tituloSesion = new Paragraph("Tema: " + info.tema() + " - " + info.fechaSesion().format(formatter), fontLabelDatos);
                        document.add(tituloSesion);
                        Paragraph alcance = new Paragraph("Alcance: " + info.alcance(), fontLabelDatos);
                        document.add(alcance);
                        document.add(new Paragraph(" ")); //da mejor control

                        PdfPTable tablaAlumno = new PdfPTable(2);
                        tablaAlumno.setWidthPercentage(80);

                        //encabezados sin color
                        PdfPCell hAlumno = new PdfPCell((new Phrase("Alumno", fontSubtitulo)));
                        hAlumno.setBackgroundColor(null);//quita el color de fondo
                        hAlumno.setPadding(5);
                        hAlumno.setBorder(Rectangle.NO_BORDER);

                        PdfPCell hEstado = new PdfPCell((new Phrase("Estado", fontSubtitulo)));
                        hEstado.setBackgroundColor(null);//quita el color de fondo
                        hEstado.setPadding(5);
                        hEstado.setBorder(Rectangle.NO_BORDER);

                        tablaAlumno.addCell(hAlumno);
                        tablaAlumno.addCell(hEstado);

                        for (ReporteAsistenciaGrupoDto a : listaSesion) {
                            //celda nommbre
                            PdfPCell nombreCell = new PdfPCell(new Phrase(a.nombreAlumno(), fontValorDatos));
                            nombreCell.setPadding(5);
                            nombreCell.setBackgroundColor(new Color(245,245,245));//uso de gris claro para diferenciar filas
                            nombreCell.setBorder(Rectangle.NO_BORDER);
                            nombreCell.setBorder(Rectangle.BOTTOM);
                            nombreCell.setBorderColor(new Color(96, 93, 93));
                            tablaAlumno.addCell(nombreCell);

                            //celda estado
                            PdfPCell estadoCell = new PdfPCell(new Phrase(a.estado().name(), fontValorDatos));
                            estadoCell.setPadding(5);
                            estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                            estadoCell.setBorder(Rectangle.NO_BORDER);
                            estadoCell.setBorder(Rectangle.BOTTOM);
                            estadoCell.setBorderColor(new Color(96, 93, 93));

                            if (a.estado() == EstadoAsistencia.ASISTIO) {
                                estadoCell.setBackgroundColor(new Color(200,255,200));
                            } else if (a.estado() == EstadoAsistencia.FALTO) {
                                estadoCell.setBackgroundColor(new Color(255,200,200));
                            } else{
                                estadoCell.setBackgroundColor(new Color(255, 248,200));
                            }

                            tablaAlumno.addCell(estadoCell);
                        }

                        document.add(tablaAlumno);
                        document.add(new Paragraph(" "));
                    }
                }
            }
            document.close();
        } catch(Exception e){
            throw new ReporteException("Error generando reporte del grupo", e);
        } finally {//cerrar el documento si se encuentra abierto
            if(document.isOpen()){
                document.close();
            }
        }

        //out.toByteArray() obtiene los bytes resultantes del PDF.
        //crea un InputStream de esos bytes para devolver desde el metodo
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

    private PdfPCell getHeaderCell(String text) {
        Font font = new Font(Font.HELVETICA, 10, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        cell.setBackgroundColor(new Color(200, 200, 200));
        return cell;
    }

    private PdfPCell getCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        return cell;
    }


}
