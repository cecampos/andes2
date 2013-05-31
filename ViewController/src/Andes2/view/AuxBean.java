package Andes2.view;

import java.io.Serializable;

import oracle.adf.view.rich.component.rich.input.RichInputText;

public class AuxBean implements Serializable {
    private RichInputText mes;
    
    private String mesVal= "6";

    public AuxBean() {
    }

    public void setMes(RichInputText mes) {
        this.mes = mes;
    }

    public RichInputText getMes() {
        return mes;
    }

    public void setMesVal(String mesVal) {
        this.mesVal = mesVal;
    }

    public String getMesVal() {
        return mesVal;
    }
}
