package Andes2.view;


import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import javax.faces.validator.ValidatorException;

import oracle.adf.view.rich.component.rich.input.RichInputFile;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import org.apache.myfaces.trinidad.model.UploadedFile;

import Andes2.model.java.FileUploader;

import java.io.IOException;
import java.io.InputStream;

public class FilUpdateBean {
    UploadedFile empleado,skill;
    HashMap filesMap;
    HashMap outputMap;


    public FilUpdateBean(){
        filesMap = new HashMap();
        outputMap = new HashMap();
        }

    public void FileChange(ValueChangeEvent event) {
        // Add event code here...
        
        UploadedFile aux = (UploadedFile)event.getNewValue();
        //System.out.println("subido:"+empleado.getFilename());
        //Buscar si el archivo ha sido subido antes, mostrar fecha del ultimo submit si es asi
        try {
            filesMap.put(event.getComponent().getId(), aux.getInputStream());
        } catch (IOException e) {
            System.out.println("Error al obtener stream fileChanged");
        }
        ((RichOutputText)outputMap.get(event.getComponent().getId()+"OT")).setValue("ASDF");
    
    }

    public void FileValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...
        System.out.println("estoy validando");
        UploadedFile aux = (UploadedFile)object;
        //System.out.println(aux.getFilename());
        if(aux.getFilename().equals("CWAE.rkt")){
            ((RichInputFile) uIComponent).resetValue();
            throw new ValidatorException(new FacesMessage("hola"));
        }
        /*
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR,"error","Hola");
        facesContext.addMessage(uIComponent.getClientId(), msg);
        facesContext.renderResponse();
        */
    }

    public void skillsChange(ValueChangeEvent event) {
        // Add event code here...
        skill = (UploadedFile)event.getNewValue();
        //filesMap.put("skill", (UploadedFile)event.getNewValue());
        filesMap.put("skill", (UploadedFile)event.getNewValue());
    }


    public String Submit() {
        // Add event code here...
        System.out.println("estoy en el submit, archivos:");
        FileUploader uploader = new FileUploader();
        Set set = filesMap.entrySet(); 
        // Get an iterator 
        Iterator i = set.iterator(); 
        // Display elements 
        while(i.hasNext()) { 
        Map.Entry me = (Map.Entry)i.next();
    
        uploader.uploadFileToDb((String)me.getKey(),(InputStream)me.getValue());

        } 
        return null;
    }

    public void setEmpleadoOT(RichOutputText empleadoOT) {
        //this.empleadoOT = empleadoOT;
        outputMap.put("empleadoOT", empleadoOT);
    }

    public RichOutputText getEmpleadoOT() {
        //return empleadoOT;
        return (RichOutputText)outputMap.get("empleadoOT");
    }

    public void setSkillOT(RichOutputText skillOT) {
        outputMap.put("skillOT", skillOT);
    }

    public RichOutputText getSkillOT() {
        return (RichOutputText)outputMap.get("skillOT");
    }
    public void setGrupoOT(RichOutputText grupoOT) {
        outputMap.put("grupoOT", grupoOT);
    }

    public RichOutputText getGrupoOT() {
        return (RichOutputText)outputMap.get("grupoOT");
    }
    
    public void setCapacityOT(RichOutputText capacityOT) {
        outputMap.put("capacityOT", capacityOT);
    }

    public RichOutputText getCapacityOT() {
        return (RichOutputText)outputMap.get("capacityOT");
    }
    public void setCapacitacionOT(RichOutputText capacitacionesOT) {
        outputMap.put("capacitacionOT", capacitacionesOT);
    }

    public RichOutputText getCapacitacionOT() {
        return (RichOutputText)outputMap.get("capacitacionOT");
    }
    public void setDemSkillOT(RichOutputText demSkillOT) {
        outputMap.put("demSkillOT", demSkillOT);
    }

    public RichOutputText getDemSkillOT() {
        return (RichOutputText)outputMap.get("demSkillOT");
    }
}
