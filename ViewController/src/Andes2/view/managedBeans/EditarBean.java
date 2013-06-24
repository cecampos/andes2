package Andes2.view.managedBeans;

import oracle.adf.controller.TaskFlowId;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;

public class EditarBean {
    private String taskFlowId = "/WEB-INF/editViews.xml#editViews";
    private String selectedTable;
    private RichSelectOneChoice asdf;

    public EditarBean() {
    }

    public TaskFlowId getDynamicTaskFlowId() {
        return TaskFlowId.parse(taskFlowId);
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
