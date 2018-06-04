package Citysystem.Dev.EBCA.CHF;


import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;

public class FillOrigineList extends Methodes{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log =Logger.getLogger(FillOrigineList.class);
  
    public boolean onAfterLoad() 
    {
    rc =getResourceController();
    rc.alert("Ceci est un message");
    
    instance= getWorkflowInstance();
    instance.setList("EBCA_Origine_CHF",Origines());
    
    //instance.setList("EBCA_Origine_CHF",GetPays());
    
    return super.onAfterLoad();
    }
    
    public boolean isOnChangeSubscriptionOn(IProperty Field)
    {
        if (Field.getName().equals("EBCA_Origine_CHF"))
            return true;
        
        if (Field.getName().equals("EBCA_Olefines_CHF"))
            return true;
        
        return false;
    }
    
    public void onPropertyChanged(IProperty Field) {
        instance=getWorkflowInstance();
        rc=getResourceController();
        
        if (Field.getName().equals("EBCA_Origine_CHF"))
        {
            String libelle = (String) instance.getValue("EBCA_Origine_CHF");
            if(IfEUR(libelle))
            {
               instance.setValue("EBCA_Eur1_CHF", "Oui");
            }else
               instance.setValue("EBCA_Eur1_CHF", "Non");
        }
        
        if (Field.getName().equals("EBCA_Olefines_CHF"))
        {
            String str_olefines = (String) instance.getValue("EBCA_Olefines_CHF");
            
            float olefine = Float.parseFloat(str_olefines);
            if(olefine>40) {
                rc.alert("La valeur Olefines est supérieur à 40%");
                
            }
        }
    }
    
    public boolean onBeforeSubmit(IAction action)
    {
        instance=getWorkflowInstance();
        rc=getResourceController();
        
        return super.onBeforeSubmit(action);
    }
    
}
