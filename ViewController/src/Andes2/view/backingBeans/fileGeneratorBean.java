package Andes2.view.backingBeans;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.Arrays;

import javax.faces.context.FacesContext;

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
        // Add event code here...
        
        CollectionModel model  = (CollectionModel)table.getValue();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) model.getWrappedData();
        DCIteratorBinding iterator = treeBinding.getIteratorBinding();
        
        /*Obtener binding programaticamente*/
        DCBindingContainer bindings = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dcIteratorBindings = bindings.findIteratorBinding("Vacaciones1Iterator");
        Row[] vacasRows = dcIteratorBindings.getAllRowsInRange();
        
        /*Bonus, generar Excel:*/
        String contentType = "application/vnd.ms-excel";
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletResponse response =
            (HttpServletResponse)fc.getExternalContext().getResponse();
        response.setHeader("Content-disposition", "attachment; filename=hola.csv");
        response.setContentType(contentType);
        PrintWriter out = response.getWriter();
        
        
        RowSetIterator rsi = iterator.getRowSetIterator();
        String[] attNames = rsi.getRowAtRangeIndex(0).getAttributeNames();
        out.println(Arrays.toString(attNames));
        Row[] rows = iterator.getAllRowsInRange();
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
    }
}
