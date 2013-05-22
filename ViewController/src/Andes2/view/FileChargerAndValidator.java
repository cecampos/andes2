package Andes2.view;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import javax.faces.validator.ValidatorException;

import oracle.adf.view.rich.component.rich.input.RichInputFile;


import org.apache.myfaces.trinidad.model.UploadedFile;

public class FileChargerAndValidator {
    UploadedFile file1;
    public FileChargerAndValidator() {
    }

    public void setUploaded(ValueChangeEvent event) {
        System.out.println("hola1");
        file1 = (UploadedFile)event.getNewValue();
    }

    public void asdf(ValueChangeEvent valueChangeEvent) {
        // Add event code here...
        System.out.println("hola");
        System.out.println(file1.getFilename());
    }

    public String ValidateAndUpload() {
        // Add event code here...
        System.out.println("Estoy validando :D");
        return null;
    }

    public void validateFile(FacesContext facesContext, UIComponent component, Object object) {
        ((EditableValueHolder) component).setValid(false);
        facesContext.addMessage(component.getClientId(), new FacesMessage("hola"));
    }
}
