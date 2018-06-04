package ConfirmationChefile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

public class FillOrigineList  extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log =Logger.getLogger(FillOrigineList.class);

    IWorkflowInstance instance;
    IResourceController rc;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module   ;
    
    public boolean onAfterLoad() 
    {
     // rc =getResourceController();
    //rc.alert("Ceci est un message");
    instance= getWorkflowInstance();
        
    instance.setList("PIM_Origine_CHF",Origines());
    
    return super.onAfterLoad();
    }
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    context = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }
    //-------------------------------------------------------------------------------------------------------------------------------------------
    public ArrayList<IOption> Origines()
    {
    ArrayList<IOption>list = new ArrayList<IOption>();
        try{
                instance=getWorkflowInstance();
                connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
                String req = "SELECT * FROM FournisseurPays";
                st = connection.prepareStatement(req);
                ResultSet result = st.executeQuery();
                while(result.next())
                {
                    list.add(getWorkflowModule().createListOption(result.getString("Libelle"),result.getString("Libelle")));
                   
                }
        }catch(Exception e)
        {
            log.error("CS_Error in Origines method : " + e.getClass() + " - " + e.getMessage() + " - " + e.getStackTrace());
           
        }
        return list;
    }
    
    //--------------------------------------------------------------------------------------------------------------------------------------------
    
    
    public boolean isOnChangeSubscriptionOn(IProperty Field)
    {
        if (Field.getName().equals("PIM_Origine_CHF"))
            return true;
        
        if (Field.getName().equals("PIM_Olefines_CHF"))
            return true;
        
        return false;
    }
    
    public void onPropertyChanged(IProperty Field) {
        instance=getWorkflowInstance();
        rc=getResourceController();
        
        if (Field.getName().equals("PIM_Origine_CHF"))
        {
            String libelle = (String) instance.getValue("PIM_Origine_CHF");
            if(IfEUR(libelle))
            {
               instance.setValue("PIM_Eur1_CHF", "Oui");
            }else
               instance.setValue("PIM_Eur1_CHF", "Non");
        }
        
        if (Field.getName().equals("PIM_Olefines_CHF"))
        {
            String str_olefines = (String) instance.getValue("PIM_Olefines_CHF");
            
            float olefine = Float.parseFloat(str_olefines);
            if(olefine>40) {
                rc.alert("La valeur Olefines est supérieur à 40%");
                getResourceController().inform("PIM_Olefines_CHF", "La valeur Olefines est supérieur à 40%");
            }
        }
        
    }
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------------
    
    public boolean IfEUR(String Libelle)
    {
        String str = "";
        try {   
            connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
            
            String req="SELECT * FROM FournisseurPays where libelle ='"+Libelle+"'";
            st = connection.prepareStatement(req);
            result=st.executeQuery();
            
            while(result.next())
            {
                str=result.getString("Eur");
            }
            
            if(str.equals("EUR"))
                return true;
            
        } catch (Exception e) {
            log.error("CS Erreur in IfEUR methode : "+e.getClass()+" _ "+e.getMessage()+"_"+ e.fillInStackTrace());
        }
        
        return false;
    }
    
    //---------------------------------------------------------------------------------------------------------------------------------------------
}
