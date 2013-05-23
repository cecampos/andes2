package Andes2.view;


import Andes2.model.java.CheckLastUpload;

import java.util.HashMap;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Date;

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

<<<<<<< HEAD
import java.text.SimpleDateFormat;

=======
import java.util.StringTokenizer;
>>>>>>> nombres de archivo separado

public class FilUpdateBean {
    UploadedFile empleado,skill;
    HashMap filesMap;
    HashMap outputMap;
    CheckLastUpload checker;
    


    public FilUpdateBean(){
        filesMap = new HashMap();
        outputMap = new HashMap();
        checker = new CheckLastUpload();
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
        
        Date checkResult = checker.check(aux.getFilename());
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String toPrint = "No hay cargas previas";
        if(checkResult!=null)
            toPrint = "Ultima subida: "+formatter.format(checkResult);
        ((RichOutputText)outputMap.get(event.getComponent().getId()+"OT")).setValue(toPrint);
    
    }

    public void FileValidator(FacesContext facesContext, UIComponent uIComponent, Object object) {
        // Add event code here...
        System.out.println("estoy validando");
        UploadedFile aux = (UploadedFile)object;
        RichInputFile uiID= (RichInputFile) uIComponent;
        String label= uiID.getId();
        
        StringTokenizer tokenizador= new StringTokenizer(aux.getFilename(),"_");
        String[] datosArchivos = new String[tokenizador.countTokens()];
        
        //por definicion de formato, el archivo tiene el siguiente formato
        //nombreArchivo_MM_YYYY
        for(int i=0; i<datosArchivos.length;i++){
            datosArchivos[i] = tokenizador.nextToken();    
            //System.out.println(i+":"+datosArchivos[i]);
        }
                
        
        //System.out.println(aux.getFilename());
        if( datosArchivos[0].equals(label)!=0){
            ((RichInputFile) uIComponent).resetValue();
            throw new ValidatorException(new FacesMessage("Archivo :"+aux.getFilename()+" no corresponde a un archivo valido" +
                "para campo:"+label ));
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

    //GETTERS Y SETTERS para los output text de que indican la existencia de una carga previa
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
    public void setVacacionOT(RichOutputText demSkillOT) {
        outputMap.put("vacacionOT", demSkillOT);
    }

    public RichOutputText getVacacionOT() {
        return (RichOutputText)outputMap.get("vacacionOT");
    }
    public void setCapTurnoOT(RichOutputText demSkillOT) {
        outputMap.put("capTurnoOT", demSkillOT);
    }

    public RichOutputText getCapTurnoOT() {
        return (RichOutputText)outputMap.get("capTurnoOT");
    }
    
    public void setTurnoNoPOT(RichOutputText demSkillOT) {
        outputMap.put("turnoNoPOT", demSkillOT);
    }

    public RichOutputText getTurnoNoPOT() {
        return (RichOutputText)outputMap.get("turnoNoPOT");
    }
    
    public void setTurnoFijadoOT(RichOutputText demSkillOT) {
        outputMap.put("turnoFijadoOT", demSkillOT);
    }

    public RichOutputText getTurnoFijadoOT() {
        return (RichOutputText)outputMap.get("turnoFijadoOT");
    }    

}
