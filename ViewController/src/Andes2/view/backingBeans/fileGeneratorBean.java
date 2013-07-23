package Andes2.view.backingBeans;

import com.sun.faces.util.CollectionsUtils;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Arrays;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.adf.view.rich.component.rich.input.RichInputText;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.ViewObject;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;


import org.apache.myfaces.trinidad.model.CollectionModel;

public class fileGeneratorBean {
    private RichTable table;
    private RichInputText cargo;

    public fileGeneratorBean() {
    }

    public void setTable(RichTable table) {
        this.table = table;
    }

    public RichTable getTable() {
        return table;
    }

    private void generateCSV(Row[] input, String fileName) throws IOException {
        //Crea un archivos filename.csv parseando el contenido de input
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName+".csv")); 
        
        for(Row row:input){
            Object[] rowValues;
            rowValues = row.getAttributeValues();
            StringBuffer line = new StringBuffer();
            line.append(rowValues[0]);
            for(int i=1; i< rowValues.length;i++){
                //formato de archivo de entrada al modelo
                //pedido con ',' para separar campos
                line.append(","+rowValues[i]);
            }
            if(fileName.equals("RestriccionAsignacion")){
                String newLine=line.toString();
                newLine=formatoMes(newLine);
                writer.write(newLine);
            }
            else if(fileName.equals("Capacitaciones")){
                String newLine=line.toString();
                newLine=formatoMesCapacitaciones(newLine);
                writer.write(newLine);
            }
            else{
                writer.write(line.toString()+"\n");
            }
        }   
        writer.close();
        }
    public int getDayOfTheWeek(Date d){
            GregorianCalendar cal = new GregorianCalendar();
            cal.setTime(d);
            return cal.get(Calendar.DAY_OF_WEEK);           
    }
    public String formatoMes(String line){
        //por implementar
        //transforma un periodo de tiempo dado a secuencia de dias
        //ejemplo: 01/07/2013 - 03/07/2013
        // 1 - linea
        // 2 - linea
        // 3 - linea
        //RestriccionAsignacion "13277964-3,61,2013-08-01 00:00:00.0,1,0,0,0,0,0,0,0,0,1,0"
        //RUT, dia, turno
        //[0-9]EMPL_RUT,DIA_INICIO,MES_INICIO,ANNIO_INICIO,DIA_TERMINO,MES_TERMINO,ANNIO_TERMINO,DIA_MES,MES_MES,ANNIO_MES,
        //[10-17]RTAG_TODOS,RTAG_LUNES,RTAG_MARTES,RTAG_MIERCOLES,RTAG_JUEVES,RTAG_VIERNES,RTAG_SABADO,RTAG_DOMINGO,
        //[18-20]RTAG_APERTURA,RTAG_TARDE,RTAG_NOCHE             
        String aux= "";
        String[] rowTokens = line.split(",");
        String TODOS=rowTokens[10];
        String APERTURA=rowTokens[18];
        String TARDE = rowTokens[19];
        String NOCHE = rowTokens[20];
        int LUNES = Integer.parseInt(rowTokens[11]);
        int MARTES= Integer.parseInt(rowTokens[12]);
        int MIERCOLES = Integer.parseInt(rowTokens[13]);
        int JUEVES = Integer.parseInt(rowTokens[14]);
        int VIERNES = Integer.parseInt(rowTokens[15]);
        int SABADO = Integer.parseInt(rowTokens[16]);
        int DOMINGO = Integer.parseInt(rowTokens[17]);

        int diferencia= Integer.parseInt(rowTokens[2]);
        
        //Date f_inicio = new Date();
        //Date f_termino = new Date();
        //Date f_actual = new Date();
        
        GregorianCalendar c_actual= new GregorianCalendar();
        c_actual.set(Integer.parseInt(rowTokens[9]),Integer.parseInt(rowTokens[8])-1,Integer.parseInt(rowTokens[7]));
        //c_actual.add(Calendar.DAY_OF_MONTH,-1);
        
        GregorianCalendar c_inicio = new GregorianCalendar();
        //c_inicio.setTime(f_inicio);
        c_inicio.set(Integer.parseInt(rowTokens[3]), Integer.parseInt(rowTokens[2])-1, Integer.parseInt(rowTokens[1]));
        
        GregorianCalendar c_termino = new GregorianCalendar();
        c_termino.set(Integer.parseInt(rowTokens[6]),Integer.parseInt(rowTokens[5])-1,Integer.parseInt(rowTokens[4]));
    
        GregorianCalendar c_aux = new GregorianCalendar();
        c_aux.set(Integer.parseInt(rowTokens[3]), Integer.parseInt(rowTokens[2])-1, Integer.parseInt(rowTokens[1]));
        
        while (c_aux.before(c_termino) && c_aux.before(c_actual)){
                                                                     
            if (c_aux.get(Calendar.MONTH) != c_actual.get(Calendar.MONTH)) {
                                                               
                c_aux.add(Calendar.DAY_OF_MONTH, 1);
                continue;
            }
            else {
                if(TODOS.equals("1")){//TODOS
                    if(APERTURA.equals("1")){//Apertura
                        aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                   }
                    if(TARDE.equals("1")){//tarde
                        aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                    }
                    if(NOCHE.equals("1")){//noche
                        aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                    }
                }
                    else {
                        //LUNES
                        if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && rowTokens[LUNES].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        //MARTES
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && rowTokens[MARTES].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        //MIERCOLES
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY && rowTokens[MIERCOLES].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        //JUEVES
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY && rowTokens[JUEVES].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        //VIERNES
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY && rowTokens[VIERNES].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        //SABADO
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY && rowTokens[SABADO].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        //DOMINGO
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY && rowTokens[DOMINGO].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                        else if (c_aux.get(Calendar.DAY_OF_WEEK) == 1 && rowTokens[LUNES].equals("1")){
                            if(APERTURA.equals("1")){//Apertura
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",A"+"\n";
                            }
                            if(TARDE.equals("1")){//tarde
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",T"+"\n";
                            }
                            if(NOCHE.equals("1")){//noche
                                aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+",N"+"\n";
                            }
                        }
                    }                                 
                c_aux.add(Calendar.DAY_OF_MONTH, 1);                        
                }  
            }        
            return aux;
    }
    
    public String queTurnoEs(int hora){
        String resultado="";
        //A:3
        //T:11
        //N:19
        if(hora>=19){
            return "N";
        }
        if(hora>=11){
            return "T";
        }
        return "A";
    }

    public String formatoMesCapacitaciones(String input){
        //por implementar
        //transforma un periodo de tiempo dado a secuencia de dias
        //[0-9]EMPL_RUT,DIA_INICIO,MES_INICIO,ANNIO_INICIO,DIA_TERMINO,MES_TERMINO,ANNIO_TERMINO,DIA_MES,MES_MES,ANNIO_MES
        //[10-11]CAPT_HORA_INICIO,CAPT_HORA_TERMINO 
        String aux="";
        String[] rowTokens = input.split(",");
        String[] horaCompleta= rowTokens[10].split(":");
        int HORA = Integer.parseInt(horaCompleta[0]);
       
        GregorianCalendar c_actual= new GregorianCalendar();
        c_actual.set(Integer.parseInt(rowTokens[9]),Integer.parseInt(rowTokens[8])-1,Integer.parseInt(rowTokens[7]));
 
        GregorianCalendar c_inicio = new GregorianCalendar();
        c_inicio.set(Integer.parseInt(rowTokens[3]), Integer.parseInt(rowTokens[2])-1, Integer.parseInt(rowTokens[1]));
        
        GregorianCalendar c_termino = new GregorianCalendar();
        c_termino.set(Integer.parseInt(rowTokens[6]),Integer.parseInt(rowTokens[5])-1,Integer.parseInt(rowTokens[4]));
        
        GregorianCalendar c_aux = new GregorianCalendar();
        c_aux.set(Integer.parseInt(rowTokens[3]), Integer.parseInt(rowTokens[2])-1, Integer.parseInt(rowTokens[1]));
        
        String turno= queTurnoEs(HORA);
        
        while ( (c_aux.before(c_termino) || (c_aux.compareTo(c_termino)==0))&& c_aux.before(c_actual)){
                                                                     
            if (c_aux.get(Calendar.MONTH) != c_actual.get(Calendar.MONTH)) {
                                                               
                c_aux.add(Calendar.DAY_OF_MONTH, 1);
                continue;
            }
            else {//estoy en el periodo
                    aux+=rowTokens[0]+","+c_aux.get(Calendar.DAY_OF_MONTH)+","+turno+"\n";

                }                                
                c_aux.add(Calendar.DAY_OF_MONTH, 1);                        
        }         
        return aux;
    }

    public void generateFile(String fileName) throws IOException {
        //Prepara la informacion que sera guardada en un .csv, filtra los resultados de un iterador si corresponde
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding reqIterator = bindings.findIteratorBinding(fileName+"Iterator");
        
        //A futuro: para no tener el if feo de abajo:
        String[] needsCargo = {"RequerimientoSkills", "Requerimientos"};
        
        if(fileName.equals("RequerimientoSkills") || fileName.equals("Requerimientos") || fileName.equals("Capacitaciones")
            || fileName.equals("RestriccionAsignacion") || fileName.equals("DatosSkills")){
                ViewObject vo = reqIterator.getViewObject();
                vo.ensureVariableManager().setVariableValue("elCargo", cargo.getValue());
                vo.executeQuery();    
            }
        Row[] result = reqIterator.getAllRowsInRange();
        
        generateCSV(result,fileName);
        
        
    }
    
    public void generateZipFile(String[] filesNames, String output) throws FileNotFoundException, IOException {
        //Genera un archivo .zip que contiene todos los archivos recien generados
            FileOutputStream fos = new FileOutputStream(output);
            ZipOutputStream zip = new ZipOutputStream(fos);
            for(String fileName:filesNames){        
                byte[] buf = new byte[1024];
                ZipEntry entry = new ZipEntry(fileName+".csv");
                FileInputStream reader = new FileInputStream(fileName+".csv");
                zip.putNextEntry(entry);    
                int len;
                while((len = reader.read(buf)) > 0)
                    zip.write(buf, 0, len);
                reader.close();
                zip.closeEntry();
            }
            zip.close();        
        } 
    
    public String exportToExcel() throws IOException {
        // Generar todos los archivos necesarios para el modelo
        /*Para cada caso (archivo) 
         * Obtener su iterador
         * Setear variables de consulta (si corresponde)
         * Executar consulta con variables seteades (si corresponde)
         * Obtener todas las filas y copiarlas en un archivo csv
         * Generar un .zip con todos los archivos generados
         *    
         * */
        
        //Generar archivos csv:
        String[] filesNames = {"Turnos","RequerimientoSkills","Requerimientos","Capacitaciones","RestriccionAsignacion",
                               "DatosSkills"};
        for(String file:filesNames)
            generateFile(file);

        
        //Generar archivo zip
        String outputFileName = "archivos.zip";
        generateZipFile(filesNames,outputFileName);
        
        
        //Descargar archivo zip:
        
        String contentType = "application/x-zip-compressed";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response =
            (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename="+outputFileName);
        response.setContentType(contentType);
        ServletOutputStream responseStream = response.getOutputStream();

        FileInputStream reader2 = new FileInputStream(outputFileName);
        byte[] buf = new byte[1024];
        int len;
        while((len = reader2.read(buf)) > 0)
            responseStream.write(buf, 0, len);
        
        responseStream.close();
        fc.responseComplete();
        
        return null;
    }

    public void setCargo(RichInputText cargo) {
        this.cargo = cargo;
    }

    public RichInputText getCargo() {
        return cargo;
    }
}
