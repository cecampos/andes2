package Andes2.view.managedBeans;

import java.util.ArrayList;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;

import javax.el.MethodExpression;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

import oracle.adf.view.rich.event.QueryEvent;
import oracle.adf.view.rich.model.FilterableQueryDescriptor;

public class RosterCustomQueryBean {
    public RosterCustomQueryBean() {
    }

    public void CargoQueryListener(QueryEvent queryEvent) {
        // Listener customizado para Filtrar los resultados del Roster segun varios cargos
        FilterableQueryDescriptor fqd = (FilterableQueryDescriptor) queryEvent.getDescriptor();
        //Obtener los datos de filtrado, como los ingreso el cliente:
        Map _criteriaMap = fqd.getFilterCriteria();
        StringBuffer cargoNameFilterString = new StringBuffer();
        StringBuffer nombreFilterString = new StringBuffer();
        ArrayList<String> cargosArray = null;
        String nombreFilter = (String)_criteriaMap.get("EmplNombre");
        
        if(_criteriaMap.get("CrgoNombre")!=null){
            cargosArray = (ArrayList<String>)_criteriaMap.get("CrgoNombre");
            for(int i=0; i<cargosArray.size();i++){
                if(i==0){
                    cargoNameFilterString.append(cargosArray.get(i));
                    }
                else{
                    cargoNameFilterString.append(" OR ");
                    cargoNameFilterString.append(cargosArray.get(i));
                    }
                }
            cargoNameFilterString.append(" OR -1"); 
            _criteriaMap.put("CrgoNombre", cargoNameFilterString.toString());
            _criteriaMap.put("EmplNombre",nombreFilter);
            fqd.setFilterCriteria(_criteriaMap);
        }/*
        if(!_criteriaMap.get("EmplNombre").equals("")){
            nombreFilter = (String)_criteriaMap.get("EmplNombre");
            //nombreFilter = nombreFilter+" OR -1";
            _criteriaMap.put("EmplNombre", nombreFilter);
            
        }
        else
            _criteriaMap.put("EmplNombre","");
        fqd.setFilterCriteria(_criteriaMap);
    */
        //Mantener la funcionalidad previa
        FacesContext fctx = FacesContext.getCurrentInstance();
        Application application = fctx.getApplication();
        ExpressionFactory expressionFactory = 
        application.getExpressionFactory();
        ELContext elctx = fctx.getELContext();
        MethodExpression methodExpression =
        expressionFactory.createMethodExpression(
        elctx, 
        "#{bindings.FullRosterForMonth1Query.processQuery}",
        Object.class,
        new Class[] {QueryEvent.class});
        methodExpression.invoke(elctx, new Object[]{queryEvent});
        //restore filter selection done by the user. Note that this 
        //needs to be saved as an ArrayList
        _criteriaMap.put("CrgoNombre", cargosArray);
        fqd.setFilterCriteria(_criteriaMap);

    }
}
