package Andes2.view.managedBeans;

import javax.faces.event.ValueChangeEvent;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichOutputText;

public class EditBean {
    private RichSelectOneChoice tableName;
    private RichOutputText selectedOption;
    private String selected="asdf";

    public EditBean() {
    }

    public void setTableName(RichSelectOneChoice tableName) {
        //selected = (String)selectedOption.getValue();
        this.tableName = tableName;
    }

    public RichSelectOneChoice getTableName() {
        return tableName;
    }

    public void setSelectedOption(RichOutputText selectedOption) {
        this.selectedOption = selectedOption;
    }

    public RichOutputText getSelectedOption() {
        return selectedOption;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String getSelected() {
        return selected;
    }

    public void prueba(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        selected=(String)valueChangeEvent.getNewValue();
        System.out.println(valueChangeEvent.getNewValue());
        selectedOption.setValue(valueChangeEvent.getNewValue());
    }
}
