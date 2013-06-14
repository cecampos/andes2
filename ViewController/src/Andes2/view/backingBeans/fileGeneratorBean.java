package Andes2.view.backingBeans;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Arrays;

import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.data.RichTable;

import oracle.jbo.Row;
import oracle.jbo.RowSetIterator;
import oracle.jbo.uicli.binding.JUCtrlHierBinding;

import org.apache.myfaces.trinidad.model.CollectionModel;

public class fileGeneratorBean {
    private RichTable table;

    public fileGeneratorBean() {
    }

    public void setTable(RichTable table) {
        this.table = table;
    }

    public RichTable getTable() {
        return table;
    }

    public String exportToExcel() throws IOException {
        // Generar todos los archivos necesarios para el modelo


        
        CollectionModel model  = (CollectionModel)table.getValue();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) model.getWrappedData();
        DCIteratorBinding iterator = treeBinding.getIteratorBinding();
        RowSetIterator rsi = iterator.getRowSetIterator();
        
        
        /*Obtener binding programaticamente*/
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("Vacaciones1Iterator");
        Row[] vacasRows = dcIteratorBindings.getAllRowsInRange();
        

        
        //crear 1 archivo:
        BufferedWriter writer = new BufferedWriter(new FileWriter("file1.csv"));

        Row[] rows = iterator.getAllRowsInRange();
        for(Row row:rows){
            Object[] attValues = row.getAttributeValues();
            writer.write(Arrays.toString(attValues));
        }   
        writer.close();
        
        //crear segundo archivo
        
        BufferedWriter writer2 = new BufferedWriter(new FileWriter("file2.csv"));
        rows = iterator.getAllRowsInRange();
        for(Row row:rows){
            Object[] attValues = row.getAttributeValues();
            writer2.write(Arrays.toString(attValues));
        }   
        writer2.close();
                
        
        //Generar archivo zip
        
        FileOutputStream fos = new FileOutputStream("archivos.zip");
        ZipOutputStream zip = new ZipOutputStream(fos);
        byte[] buf = new byte[1024];
        ZipEntry entry = new ZipEntry("file1.csv");
        FileInputStream reader = new FileInputStream("file1.csv");
        
        int len;
        
        zip.putNextEntry(entry);
        while((len = reader.read(buf)) > 0)
            zip.write(buf, 0, len);
        
        reader.close();
        zip.closeEntry();
        
        ZipEntry entry2 = new ZipEntry("file2.csv");
        FileInputStream reader3 = new FileInputStream("file2.csv");
        
        zip.putNextEntry(entry2);
        while((len = reader3.read(buf)) > 0)
            zip.write(buf, 0, len);
        reader3.close();
        zip.closeEntry();
        zip.close();
        
        
        //Descargar archivo zip:
        
        String contentType = "application/x-zip-compressed";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response =
            (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=archivos.zip");
        response.setContentType(contentType);
        ServletOutputStream responseStream = response.getOutputStream();

        FileInputStream reader2 = new FileInputStream("archivos.zip");
        
        while((len = reader2.read(buf)) > 0)
            responseStream.write(buf, 0, len);
        
        responseStream.close();
        fc.responseComplete();
        
        
        

        //Crear archivo 1 y meterlo en el zip
        /*
        ZipEntry zipEntry = new ZipEntry("tabla1.csv");
        zip.putNextEntry(zipEntry);
        Row[] rows = iterator.getAllRowsInRange();
        for(Row row:rows){
            Object[] attValues = row.getAttributeValues();
            String rowByte = Arrays.toString(attValues);
              zip.write(attValues,0, rowByte.length() );
        }       

        
        String contentType = "application/vnd.ms-excel";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response =
            (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=hola.csv");
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        
        
        /*
        out.println(Arrays.toString(attNames));
        
        for(Row row:rows){
              Object[] attValues = row.getAttributeValues();  
              out.println(Arrays.toString(attValues));
        }
        for(Row row:vacasRows){
            Object[] attValues = row.getAttributeValues();  
            out.println(Arrays.toString(attValues));            
            }
        out.close();
        fc.responseComplete();
        
        return null;
        */
        return null;
    }
}
