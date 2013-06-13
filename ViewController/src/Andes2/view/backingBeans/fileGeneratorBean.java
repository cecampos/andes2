package Andes2.view.backingBeans;

import java.util.Arrays;

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

    public String exportToExcel() {
        // Add event code here...
        
        CollectionModel model  = (CollectionModel)table.getValue();
        JUCtrlHierBinding treeBinding = (JUCtrlHierBinding) model.getWrappedData();
        DCIteratorBinding iterator = treeBinding.getIteratorBinding();
        
        RowSetIterator rsi = iterator.getRowSetIterator();
        String[] attNames = rsi.getRowAtRangeIndex(0).getAttributeNames();
        System.out.println(Arrays.toString(attNames));
        Row[] rows = iterator.getAllRowsInRange();
        for(Row row:rows){
              Object[] attValues = row.getAttributeValues();  
              System.out.println(Arrays.toString(attValues));
        }
        
        
        return null;
    }
}
