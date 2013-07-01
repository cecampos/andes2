package Andes2.view.managedBeans;

import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

public class EditarBean {
    private String selectedTable = "";
    private RichSelectOneChoice asdf;

    public EditarBean() {
    }


    public void setSelectedTable(String selectedTable) {
        this.selectedTable = selectedTable;
    }

    public String getSelectedTable() {
        return selectedTable;
    }

    public void setAsdf(RichSelectOneChoice asdf) {
        this.asdf = asdf;
        selectedTable = (String)asdf.getValue();
    }

    public RichSelectOneChoice getAsdf() {
        return asdf;
    }
}
