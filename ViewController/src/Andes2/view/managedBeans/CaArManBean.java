package Andes2.view.managedBeans;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.component.rich.input.RichSelectBooleanCheckbox;

public class CaArManBean {
    private RichSelectBooleanCheckbox empCheck;

    public CaArManBean() {
    }

    public void setEmpCheck(RichSelectBooleanCheckbox empCheck) {
        this.empCheck = empCheck;
    }

    public RichSelectBooleanCheckbox getEmpCheck() {
        return empCheck;
    }
}
