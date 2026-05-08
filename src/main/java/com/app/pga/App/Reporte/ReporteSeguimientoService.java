package com.app.pga.App.Reporte;

import com.app.pga.App.Exception.ReporteException;
import com.app.pga.App.Models.Dtos.ReporteSeguimientoDto;
import com.app.pga.App.Models.Enum.Alcance;
import com.app.pga.App.Models.Enum.Estado;
import com.app.pga.App.Models.Enum.EstadoTarea;
import com.app.pga.App.Models.Enum.Origen;
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
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteSeguimientoService {

    public ByteArrayInputStream generarReporteSeguimiento (List<ReporteSeguimientoDto> seguimiento) throws Exception{

        if(seguimiento.isEmpty()){//pendiente
            throw new Exception("No hay alumnos inscritos en el grupo");
        }

        //extraemos los datos generales del primer registro
        ReporteSeguimientoDto primerRegistro = seguimiento.get(0);

        //obtenemos lista de alumnos unicos para la seccion de inscripciones
        List<String> nombresAlumnos = seguimiento.stream()
                .map(ReporteSeguimientoDto::nombreAlumno)
                .distinct()
                .toList();

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
            Font fontSubtitulo = new Font(Font.HELVETICA, 12, Font.NORMAL);
            Font fontSubtitulo2 = new Font(Font.HELVETICA, 11, Font.NORMAL);
            Font fontAlertaRoja = new Font(Font.HELVETICA, 12, Font.BOLD, new Color(211,47,47));
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
            logoCell.setBorder(Rectangle.NO_BORDER);
            logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            header.addCell(logoCell);

            Paragraph titulo = new Paragraph("PLATAFORMA DE GESTIÓN ACADÉMICA", fontTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);

            PdfPCell tituloCell = new PdfPCell(titulo);

            tituloCell.setBorder(Rectangle.NO_BORDER);
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

            Paragraph subtitulo = new Paragraph("REPORTE DE SEGUIMIENTO ACADÉMICO", fontSubtitulo);

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

            addFila(datosGrupo, "REGISTRO:", primerRegistro.createdAtGrupo().toString(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "GRUPO:", primerRegistro.nombreGrupo(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "CURSO:", primerRegistro.nombreCurso(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "ESTADO:", (primerRegistro.estadoGrupo() == Estado.HABILITADO) ? "ACTIVO" : "INACTIVO", fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "PERIODO:", primerRegistro.periodo(), fontLabelDatos, fontValorDatos);
            addFila(datosGrupo, "DOCENTE:", primerRegistro.nombreDocente(), fontLabelDatos, fontValorDatos);

            document.add(datosGrupo);

            //INSCRIPCIONES
            Paragraph tituloInscripciones = new Paragraph("INSCRIPCIONES", fontSubtitulo2);
            tituloInscripciones.setSpacingBefore(10);
            tituloInscripciones.setSpacingAfter(5);
            document.add(tituloInscripciones);

            //configuracion tabla
            PdfPTable tabla = new PdfPTable(2);
            tabla.setWidthPercentage(100); //espacio que ocupa en la hoja
            tabla.setWidths(new float[]{5,5});//define la proporcion del ancho de cada columna
            tabla.setSpacingBefore(10);


            for (String nombreAlumno : nombresAlumnos) {

                PdfPCell h1 = new PdfPCell(new Phrase("- " + nombreAlumno, fontLabelDatos));

                //estilos
                h1.setBorder(Rectangle.NO_BORDER);
                h1.setBackgroundColor(new Color(251, 243, 243));
                h1.setHorizontalAlignment(Element.ALIGN_CENTER);
                tabla.setSpacingAfter(5);
                tabla.addCell(h1);
            }

            //si el numero de alumnos es impar, se van a completar con celdas vacias, para evitar un error en la ultima fila
            if (nombresAlumnos.size() % 2 != 0) {
                PdfPCell empty = new PdfPCell(new Phrase(""));
                empty.setBorder(Rectangle.NO_BORDER);
                tabla.addCell(empty);
            }
            document.add(tabla);

            document.add(Chunk.NEWLINE);

            //filtrar actividades que ya tienen un progreso
            Map<String, List<ReporteSeguimientoDto>> actividadesFiltradas =
                    seguimiento.stream()
                            .collect(Collectors.groupingBy(ReporteSeguimientoDto::titulo))
                            .entrySet().stream()
                            .filter(entry -> entry.getValue().stream()
                                    .anyMatch(s -> s.estadoTarea() != EstadoTarea.Sin_Iniciar))
                            .collect(Collectors.toMap(
                                    Map.Entry::getKey,
                                    Map.Entry::getValue
                            ));
            //se convierte a lista plana para que sea la nueva fuente de datos
            List<ReporteSeguimientoDto> seguimientoFiltrado = actividadesFiltradas.values()
                    .stream().flatMap(List :: stream).toList();

            //conteo de estados de las actividades
            long Sin_Iniciar = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.Sin_Iniciar).count();
            long En_Progreso = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.En_Progreso).count();
            long En_Espera = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.En_Espera).count();
            long Completada = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.Completada).count();
            long Exenta = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.Exenta).count();
            long Aprobada = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.Aprobada).count();
            long Incompleta = seguimientoFiltrado.stream().filter(s -> s.estadoTarea() == EstadoTarea.Incompleta).count();

            long total = Sin_Iniciar+En_Progreso+En_Espera+Completada+Exenta+Aprobada+Incompleta;

            double pSin_Iniciar = total > 0 ? (Sin_Iniciar * 100.0 / total) : 0;
            double pEn_Progreso = total > 0 ? (En_Progreso * 100.0 / total) : 0;
            double pEn_Espera = total > 0 ? (En_Espera * 100.0 / total) : 0;
            double pCompletada = total > 0 ? (Completada * 100.0 / total) : 0;
            double pExenta = total > 0 ? (Exenta * 100.0 / total) : 0;
            double pAprobada = total > 0 ? (Aprobada * 100.0 / total) : 0;
            double pIncompleta = total > 0 ? (Incompleta * 100.0 / total) : 0;

            LineSeparator linea3 = new LineSeparator();
            linea3.setOffset(-5);
            document.add(linea3);

            document.add(Chunk.NEWLINE);

            boolean hayActividadesIniciadas = seguimiento.stream()
                            .anyMatch(s -> s.estadoTarea() != EstadoTarea.Sin_Iniciar);
            if(!hayActividadesIniciadas){
                Paragraph aviso = new Paragraph(
                        "Aún no hay avances registrados en las actividades",
                        fontValorDatos
                );

                aviso.setSpacingBefore(20);
                aviso.setAlignment(Element.ALIGN_CENTER);

                document.add(aviso);

                document.close();
                return new ByteArrayInputStream(out.toByteArray());
            }else{
                //bloque de datos de vista general
                Paragraph tituloResumen = new Paragraph("RESUMEN GENERAL" , fontSubtitulo2);
                tituloResumen.setSpacingAfter(10f);
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

                //celdas
                PdfPCell sinIniciar = new PdfPCell(new Phrase("Sin Iniciar", fontValorDatos));
                sinIniciar.setPadding(5);
                sinIniciar.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                sinIniciar.setBorder(Rectangle.NO_BORDER);
                sinIniciar.setBorder(Rectangle.BOTTOM);
                sinIniciar.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(sinIniciar);

                PdfPCell sinIniciar1 = new PdfPCell(new Phrase(String.valueOf(Sin_Iniciar), fontValorDatos));
                sinIniciar1.setPadding(5);
                sinIniciar1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                sinIniciar1.setHorizontalAlignment(Element.ALIGN_CENTER);
                sinIniciar1.setBorder(Rectangle.NO_BORDER);
                sinIniciar1.setBorder(Rectangle.BOTTOM);
                sinIniciar1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(sinIniciar1);

                PdfPCell sinIniciar2 = new PdfPCell(new Phrase(String.format("%.2f %%", pSin_Iniciar), fontValorDatos));
                sinIniciar2.setPadding(5);
                sinIniciar2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                sinIniciar2.setHorizontalAlignment(Element.ALIGN_CENTER);
                sinIniciar2.setBorder(Rectangle.NO_BORDER);
                sinIniciar2.setBorder(Rectangle.BOTTOM);
                sinIniciar2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(sinIniciar2);

                PdfPCell enProgreso = new PdfPCell(new Phrase("En progreso", fontValorDatos));
                enProgreso.setPadding(5);
                enProgreso.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                enProgreso.setBorder(Rectangle.NO_BORDER);
                enProgreso.setBorder(Rectangle.BOTTOM);
                enProgreso.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(enProgreso);

                PdfPCell enProgreso1 = new PdfPCell(new Phrase(String.valueOf(En_Progreso), fontValorDatos));
                enProgreso1.setPadding(5);
                enProgreso1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                enProgreso1.setHorizontalAlignment(Element.ALIGN_CENTER);
                enProgreso1.setBorder(Rectangle.NO_BORDER);
                enProgreso1.setBorder(Rectangle.BOTTOM);
                enProgreso1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(enProgreso1);

                PdfPCell enProgreso2 = new PdfPCell(new Phrase(String.format("%.2f %%", pEn_Progreso), fontValorDatos));
                enProgreso2.setPadding(5);
                enProgreso2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                enProgreso2.setHorizontalAlignment(Element.ALIGN_CENTER);
                enProgreso2.setBorder(Rectangle.NO_BORDER);
                enProgreso2.setBorder(Rectangle.BOTTOM);
                enProgreso2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(enProgreso2);

                PdfPCell enEspera = new PdfPCell(new Phrase("En espera", fontValorDatos));
                enEspera.setPadding(5);
                enEspera.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                enEspera.setBorder(Rectangle.NO_BORDER);
                enEspera.setBorder(Rectangle.BOTTOM);
                enEspera.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(enEspera);

                PdfPCell enEspera1 = new PdfPCell(new Phrase(String.valueOf(En_Espera), fontValorDatos));
                enEspera1.setPadding(5);
                enEspera1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                enEspera1.setHorizontalAlignment(Element.ALIGN_CENTER);
                enEspera1.setBorder(Rectangle.NO_BORDER);
                enEspera1.setBorder(Rectangle.BOTTOM);
                enEspera1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(enEspera1);

                PdfPCell enEspera2 = new PdfPCell(new Phrase(String.format("%.2f %%", pEn_Espera), fontValorDatos));
                enEspera2.setPadding(5);
                enEspera2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                enEspera2.setHorizontalAlignment(Element.ALIGN_CENTER);
                enEspera2.setBorder(Rectangle.NO_BORDER);
                enEspera2.setBorder(Rectangle.BOTTOM);
                enEspera2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(enEspera2);

                PdfPCell completa = new PdfPCell(new Phrase("Completada", fontValorDatos));
                completa.setPadding(5);
                completa.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                completa.setBorder(Rectangle.NO_BORDER);
                completa.setBorder(Rectangle.BOTTOM);
                completa.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(completa);

                PdfPCell completa1 = new PdfPCell(new Phrase(String.valueOf(Completada), fontValorDatos));
                completa1.setPadding(5);
                completa1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                completa1.setHorizontalAlignment(Element.ALIGN_CENTER);
                completa1.setBorder(Rectangle.NO_BORDER);
                completa1.setBorder(Rectangle.BOTTOM);
                completa1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(completa1);

                PdfPCell completa2 = new PdfPCell(new Phrase(String.format("%.2f %%", pCompletada), fontValorDatos));
                completa2.setPadding(5);
                completa2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                completa2.setHorizontalAlignment(Element.ALIGN_CENTER);
                completa2.setBorder(Rectangle.NO_BORDER);
                completa2.setBorder(Rectangle.BOTTOM);
                completa2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(completa2);

                PdfPCell exenta = new PdfPCell(new Phrase("Exenta", fontValorDatos));
                exenta.setPadding(5);
                exenta.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                exenta.setBorder(Rectangle.NO_BORDER);
                exenta.setBorder(Rectangle.BOTTOM);
                exenta.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(exenta);

                PdfPCell exenta1 = new PdfPCell(new Phrase(String.valueOf(Exenta), fontValorDatos));
                exenta1.setPadding(5);
                exenta1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                exenta1.setHorizontalAlignment(Element.ALIGN_CENTER);
                exenta1.setBorder(Rectangle.NO_BORDER);
                exenta1.setBorder(Rectangle.BOTTOM);
                exenta1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(exenta1);

                PdfPCell exenta2 = new PdfPCell(new Phrase(String.format("%.2f %%", pExenta), fontValorDatos));
                exenta2.setPadding(5);
                exenta2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                exenta2.setHorizontalAlignment(Element.ALIGN_CENTER);
                exenta2.setBorder(Rectangle.NO_BORDER);
                exenta2.setBorder(Rectangle.BOTTOM);
                exenta2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(exenta2);

                PdfPCell aprobada = new PdfPCell(new Phrase("Aprobada", fontValorDatos));
                aprobada.setPadding(5);
                aprobada.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                aprobada.setBorder(Rectangle.NO_BORDER);
                aprobada.setBorder(Rectangle.BOTTOM);
                aprobada.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(aprobada);

                PdfPCell aprobada1 = new PdfPCell(new Phrase(String.valueOf(Aprobada), fontValorDatos));
                aprobada1.setPadding(5);
                aprobada1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                aprobada1.setHorizontalAlignment(Element.ALIGN_CENTER);
                aprobada1.setBorder(Rectangle.NO_BORDER);
                aprobada1.setBorder(Rectangle.BOTTOM);
                aprobada1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(aprobada1);

                PdfPCell aprobada2 = new PdfPCell(new Phrase(String.format("%.2f %%", pAprobada), fontValorDatos));
                aprobada2.setPadding(5);
                aprobada2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                aprobada2.setHorizontalAlignment(Element.ALIGN_CENTER);
                aprobada2.setBorder(Rectangle.NO_BORDER);
                aprobada2.setBorder(Rectangle.BOTTOM);
                aprobada2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(aprobada2);

                PdfPCell incompleta = new PdfPCell(new Phrase("Incompleta", fontValorDatos));
                incompleta.setPadding(5);
                incompleta.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                incompleta.setBorder(Rectangle.NO_BORDER);
                incompleta.setBorder(Rectangle.BOTTOM);
                incompleta.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(incompleta);

                PdfPCell incompleta1 = new PdfPCell(new Phrase(String.valueOf(Incompleta), fontValorDatos));
                incompleta1.setPadding(5);
                incompleta1.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                incompleta1.setHorizontalAlignment(Element.ALIGN_CENTER);
                incompleta1.setBorder(Rectangle.NO_BORDER);
                incompleta1.setBorder(Rectangle.BOTTOM);
                incompleta1.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(incompleta1);

                PdfPCell incompleta2 = new PdfPCell(new Phrase(String.format("%.2f %%", pIncompleta), fontValorDatos));
                incompleta2.setPadding(5);
                incompleta2.setBackgroundColor(new Color(245, 245, 245));//uso de gris claro para diferenciar filas
                incompleta2.setHorizontalAlignment(Element.ALIGN_CENTER);
                incompleta2.setBorder(Rectangle.NO_BORDER);
                incompleta2.setBorder(Rectangle.BOTTOM);
                incompleta2.setBorderColor(new Color(96, 93, 93));
                resumen.addCell(incompleta2);

                document.add(resumen);
                document.add(Chunk.NEWLINE);

                //alumnos en riesgo
                Set<EstadoTarea> estadosRiesgo = Set.of(
                        EstadoTarea.Sin_Iniciar,
                        EstadoTarea.Incompleta,
                        EstadoTarea.En_Espera
                );
                //se agrupa por alumno y se cuentan las faltas
                Map<String , Long> riesgoPorAlumno = seguimientoFiltrado.stream()
                        .filter(s -> estadosRiesgo.contains(s.estadoTarea()))
                        .collect(Collectors.groupingBy(ReporteSeguimientoDto::nombreAlumno, Collectors.counting()));
                //definicion del umbral (mas de tres faltas en riesgo
                int umbralRiesgo = 3;
                List<Map.Entry<String, Long>> alumnosEnRiesgo = riesgoPorAlumno.entrySet().stream()
                        .filter(entry -> entry.getValue() >= umbralRiesgo)
                        .toList();

                //estilo de alerta
                if (!alumnosEnRiesgo.isEmpty()){
                    document.add(new LineSeparator());

                    Paragraph alertaTitulo = new Paragraph("ALERTA: ALUMNOS EN RIESGO (ACTIVIDADES PENDIENTES)", fontAlertaRoja);
                    alertaTitulo.setSpacingBefore(5);
                    alertaTitulo.setSpacingAfter(5);
                    document.add(alertaTitulo);


                    for(var entry : alumnosEnRiesgo){
                        //limpieza segura del nombre
                        String nombreAlumno = entry.getKey();
                        Long totalAct = entry.getValue();
                        // tabla contenedora para evitar que la gráfica se separe del nombre
                        PdfPTable contenedorAlumno = new PdfPTable(1);
                        contenedorAlumno.setWidthPercentage(95);
                        contenedorAlumno.setKeepTogether(true);
                        contenedorAlumno.setSpacingAfter(15);

                        // Celda para el nombre y el total de actividades
                        PdfPCell celdaNombre = new PdfPCell();
                        celdaNombre.setBorder(Rectangle.NO_BORDER);
                        Paragraph pNombre = new Paragraph("Historial de: " + nombreAlumno +" ("+totalAct+" actividades)", fontLabelDatos);
                        pNombre.setSpacingAfter(5);
                        celdaNombre.addElement(pNombre);

                        // Leyenda de valores (0,1,2,3,4) pequeña debajo del nombre
                        Font fontMini = new Font(Font.HELVETICA, 7, Font.ITALIC, Color.GRAY);
                        Paragraph pLeyenda = new Paragraph("Escala: 4 = Aprobada-Exento | 3 = Completa | 2 = En progreso | 1 = En espera | 0 = Sin iniciar-Incompleta", fontMini);
                        celdaNombre.addElement(pLeyenda);
                        contenedorAlumno.addCell(celdaNombre);

                        // dataset para el alumno específico
                        DefaultCategoryDataset individualDataset = new DefaultCategoryDataset();

                        // se filtra por las actividades de este alumno específico
                        List<ReporteSeguimientoDto> historialAlumno = seguimientoFiltrado.stream()
                                .filter(s -> s.nombreAlumno().equals(nombreAlumno))
                                .toList();

                        //Si el historial esta vacio despues de filtrar, no graficar nada para el alumno
                        if (historialAlumno.isEmpty()) continue;

                        for (ReporteSeguimientoDto reg : historialAlumno) {
                            int valorEstado = switch (reg.estadoTarea()) {
                                case Sin_Iniciar -> 0;
                                case En_Espera -> 1;
                                case En_Progreso -> 2;
                                case Completada -> 3;
                                case Aprobada -> 4;
                                case Exenta -> 4;
                                case Incompleta -> 0;
                            };

                            String etiqueta = reg.titulo();
                            individualDataset.addValue(valorEstado, "Actividad", etiqueta);
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
                        rangeAxis.setRange(-0.5, 4.5);
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

                LineSeparator linea4 = new LineSeparator();
                linea4.setOffset(-5);
                document.add(linea4);

                //grafica de pastel
                DefaultPieDataset dataset = new DefaultPieDataset();
                dataset.setValue("Sin Iniciar", Sin_Iniciar);
                dataset.setValue("En Progreso", En_Progreso);
                dataset.setValue("En Espera", En_Espera);
                dataset.setValue("Completada", Completada);
                dataset.setValue("Exenta", Exenta);
                dataset.setValue("Aprobada", Aprobada);
                dataset.setValue("Incompleta", Incompleta);

                JFreeChart chart = ChartFactory.createPieChart(
                        "Distribución de Actividades",
                        dataset,
                        true,
                        true,
                        false
                );

                chart.setBackgroundPaint(Color.WHITE);//color de fondo de todo el cuadro

                PiePlot plot = (PiePlot) chart.getPlot();
                plot.setBackgroundPaint(Color.WHITE);//fondo del circulo
                plot.setOutlineVisible(false); //quita el borde cuadrado del grafico
                plot.setShadowPaint(null);//quitar la sombra

                configurarColoresPie(plot);

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
                document.add(Chunk.NEWLINE);
                document.add(contenedorImagen);
                document.add(Chunk.NEWLINE);
                LineSeparator linea5 = new LineSeparator();
                linea5.setOffset(-5);
                document.add(linea5);
                document.add(Chunk.NEWLINE);

                //grafica de barras para el progreso por actividad
                Paragraph tituloBarras = new Paragraph("PROGRESO POR ACTIVIDAD", fontSubtitulo2);
                tituloBarras.setSpacingAfter(10f);
                document.add(tituloBarras);

                DefaultCategoryDataset datasetActividades = new DefaultCategoryDataset();
                //agrupamos los datos
                Map<String, Map<String, Long>> agrupado =
                        seguimientoFiltrado.stream().collect(
                                Collectors.groupingBy(ReporteSeguimientoDto::titulo, Collectors.groupingBy(
                                        s -> s.estadoTarea().toString(), Collectors.counting())));

                for (var entry : agrupado.entrySet()) {
                    String actividad = entry.getKey();
                    for (var estadoAct : entry.getValue().entrySet()) {
                        datasetActividades.addValue(estadoAct.getValue(), estadoAct.getKey(), actividad);
                    }
                }

                JFreeChart barChart = ChartFactory.createStackedBarChart(
                        "Estado por Actividad",
                        "Actividades",
                        "Cantidad de Alumnos",
                        datasetActividades,
                        PlotOrientation.HORIZONTAL, // Horizontal para que los títulos de tareas se lean mejor
                        true, true, false
                );

                configurarColoresBarras(barChart);
                ByteArrayOutputStream barOut = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(barOut, barChart, 500, 300);
                Image barImage = Image.getInstance(barOut.toByteArray());
                barImage.scaleToFit(500, 250);
                barImage.setAlignment(Element.ALIGN_CENTER);

                PdfPTable contenedorBarras = new PdfPTable(1);
                contenedorBarras.setWidthPercentage(100);


                PdfPCell celdaBarras = new PdfPCell(barImage);
                celdaBarras.setBorder(PdfPCell.NO_BORDER);
                celdaBarras.setHorizontalAlignment(Element.ALIGN_CENTER);
                contenedorBarras.addCell(celdaBarras);

                contenedorBarras.setKeepTogether(true);//intenta que la grafica quede en la misma hoja
                document.add(contenedorBarras);

                Map<String, List<ReporteSeguimientoDto>> actividadesAgrupadas =
                        seguimiento.stream()
                                .collect(Collectors.groupingBy(ReporteSeguimientoDto::titulo));

                document.add(Chunk.NEWLINE);
                LineSeparator linea2 = new LineSeparator();
                linea2.setOffset(-5);
                document.add(linea2);
                document.add(Chunk.NEWLINE);

                //tablas
                for (Map.Entry<String, List<ReporteSeguimientoDto>> entry : actividadesFiltradas.entrySet()) {

                    String actividad = entry.getKey();
                    List <ReporteSeguimientoDto> listaActividades = entry.getValue();
                    Alcance alcance = listaActividades.get(0).alcance();
                    Origen origen = listaActividades.get(0).origen();

                    // título de actividad
                    Paragraph tituloActividad = new Paragraph("Actividad: " + actividad, fontLabelDatos);
                    tituloActividad.setSpacingAfter(5);
                    document.add(tituloActividad);

                    Paragraph tituloOriAlc = new Paragraph("Alcance: " + alcance + "     Origen: " + origen, fontLabelDatos);
                    document.add(tituloOriAlc);
                    document.add(new Paragraph(" ")); //da mejor control

                    PdfPTable tablaAlumno = new PdfPTable(2);
                    tablaAlumno.setWidthPercentage(100);
                    tablaAlumno.setWidths(new float[]{6, 2});

                    //encabezados sin color
                    PdfPCell hAlumno = new PdfPCell((new Phrase("Alumno", fontSubtitulo2)));
                    hAlumno.setBackgroundColor(null);//quita el color de fondo
                    hAlumno.setBorder(Rectangle.NO_BORDER);
                    hAlumno.setPadding(5);

                    PdfPCell hEstado = new PdfPCell((new Phrase("Estado", fontSubtitulo2)));
                    hEstado.setBackgroundColor(null);//quita el color de fondo
                    hEstado.setBorder(Rectangle.NO_BORDER);
                    hEstado.setPadding(5);

                    tablaAlumno.addCell(hAlumno);
                    tablaAlumno.addCell(hEstado);

                    boolean alternar = false;
                    //tabla de actividades por alumno
                    for (ReporteSeguimientoDto a : listaActividades) {
                        //celda nommbre
                        PdfPCell nombreCell = new PdfPCell(new Phrase(a.nombreAlumno(), fontValorDatos));
                        nombreCell.setPadding(5);
                        nombreCell.setBackgroundColor(alternar ? new Color(245,245,245) : Color.WHITE);//uso de gris claro para diferenciar filas
                        nombreCell.setBorder(Rectangle.NO_BORDER);
                        nombreCell.setBorder(Rectangle.BOTTOM);
                        nombreCell.setBorderColor(new Color(96, 93, 93));
                        tablaAlumno.addCell(nombreCell);

                        //celda estado
                        PdfPCell estadoCell = new PdfPCell(new Phrase(a.estadoTarea().name(), fontValorDatos));
                        estadoCell.setPadding(5);
                        estadoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        estadoCell.setBorder(Rectangle.NO_BORDER);
                        estadoCell.setBorder(Rectangle.BOTTOM);
                        estadoCell.setBorderColor(new Color(96, 93, 93));
                        estadoCell.setBackgroundColor(getColorEstado(a.estadoTarea()));

                        tablaAlumno.addCell(estadoCell);

                        alternar = !alternar;
                    }
                    document.add(tablaAlumno);
                    document.add(Chunk.NEWLINE);
                }
            }

            document.close();

        }catch (Exception e){
            throw new ReporteException("Error generando el reporte del grupo", e);
        }finally{
            if(document.isOpen()){
                document.close();
            }
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

    private void configurarColoresBarras ( JFreeChart chart){
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        StackedBarRenderer renderer = (StackedBarRenderer) plot.getRenderer();

        //se deben mapear los inidices de lasa series sesgun el orden en que aparecen en el data set
        //lo ideal es iterar las series y asignar color por nombre
        CategoryDataset dataset = plot.getDataset();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            String key = (String) dataset.getRowKey(i);
            switch (key) {
                case "Sin Iniciar" -> renderer.setSeriesPaint(i, new Color(143, 142, 142));
                case "En Progreso" -> renderer.setSeriesPaint(i, new Color(91, 241, 237));
                case "En Espera" -> renderer.setSeriesPaint(i, new Color(230, 228, 49));
                case "Completada" -> renderer.setSeriesPaint(i, new Color(87, 107, 243));
                case "Exenta" -> renderer.setSeriesPaint(i, new Color(241, 167, 70));
                case "Aprobada" -> renderer.setSeriesPaint(i, new Color(96, 251, 91));
                case "Incompleta" -> renderer.setSeriesPaint(i, new Color(251, 91, 103));
            }
        }
    }

    private void configurarColoresPie(PiePlot plot) {
        plot.setSectionPaint("Sin Iniciar", new Color(143, 142, 142));
        plot.setSectionPaint("En Progreso", new Color(91, 241, 237));
        plot.setSectionPaint("En Espera", new Color(230, 228, 49));
        plot.setSectionPaint("Completada", new Color(87, 107, 243));
        plot.setSectionPaint("Exenta", new Color(241, 167, 70));
        plot.setSectionPaint("Aprobada", new Color(96, 251, 91));
        plot.setSectionPaint("Incompleta", new Color(251, 91, 103));
    }

    private Color getColorEstado(EstadoTarea estado) {
        return switch (estado) {
            case Sin_Iniciar -> new Color(200, 200, 200);     // gris
            case En_Progreso -> new Color(91, 241, 237);      // azul claro
            case En_Espera -> new Color(255, 235, 120);       // amarillo
            case Completada -> new Color(87, 107, 243);       // azul fuerte
            case Exenta -> new Color(241, 167, 70);           // naranja
            case Aprobada -> new Color(96, 251, 91);          // verde
            case Incompleta -> new Color(251, 91, 103);       // rojo
        };
    }

}
