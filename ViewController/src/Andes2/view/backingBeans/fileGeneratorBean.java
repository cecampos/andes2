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

import java.util.Collections;
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
            Object[] rowValues = row.getAttributeValues();
            StringBuffer line = new StringBuffer();
            line.append(rowValues[0]);
            for(int i=1; i< rowValues.length;i++){
                line.append(";"+rowValues[i]);
                }
            writer.write(line.toString()+"\n");
        }   
        writer.close();
        }
    
    public void generateFile(String fileName) throws IOException {
        //Prepara la informacion que sera guardada en un .csv, filtra los resultados de un iterador si corresponde
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding reqIterator = bindings.findIteratorBinding(fileName+"Iterator");
        
        //A futuro: para no tener el if feo de abajo:
        String[] needsCargo = {"RequerimientoSkills", "Requerimientos"};
        
        if(fileName.equals("RequerimientoSkills") || fileName.equals("Requerimientos") || fileName.equals("Capacitaciones")
            || fileName.equals("RestriccionAsignacion")){
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
        String[] filesNames = {"Turnos","RequerimientoSkills","Requerimientos","Capacitaciones","RestriccionAsignacion"};
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
