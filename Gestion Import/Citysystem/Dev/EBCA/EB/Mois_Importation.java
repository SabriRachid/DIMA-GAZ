package Citysystem.Dev.EBCA.EB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import beans.ConnextionBDD;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
/**
 * Created on 08 juin 2016
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class Mois_Importation  extends ConnextionBDD{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(Mois_Importation.class);
            
    //Déclaration
    IWorkflowInstance instance;
    IResourceController rc;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    
    @Override
    public boolean onAfterLoad() {
        instance = getWorkflowInstance();
   
       /** Liste des importations **/
       instance.setList("EB_RefPIM",getPIM());
       
        return super.onAfterLoad();
 
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *IS ON CHANGES
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty champ) {
        if(champ.getName().equals("EB_RefPIM")) {
            return true;
        }
        return false;
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ON PROPERTY CHANGED
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public void onPropertyChanged(IProperty champ) {
        if(champ.getName().equals("EB_RefPIM")) {
                // Affichage du mois de PIM
            try {
                
                String RefPIM = (String) instance.getValue ("EB_RefPIM");
                connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
                String req="Select Mois_PIM  from PIM where  Ref_PIM ='"+RefPIM+"'";
                 st = connection.prepareStatement(req);
                result=st.executeQuery();
                while(result.next())
                {
                   instance.setValue("sys_Title", result.getString("Mois_PIM"));
                }
            } catch (Exception e) {
                log.error("Erreur dans la sélection des references PIM :"+e.getClass()+" _ "+ e.getStackTrace() +e.getMessage());
            }
        }
        super.onPropertyChanged(champ);
    }

    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CHARGER LES PIM
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public ArrayList<IOption> getPIM()
    {
        module=getWorkflowModule();
        ArrayList<IOption> lst = new ArrayList<IOption>();
        try {
            
            connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
            //stt=Cnx("ref_ext_CIMAT");
            String req="Select Ref_PIM  from PIM order by Ref_PIM Desc";
             st = connection.prepareStatement(req);
            result=st.executeQuery();
            while(result.next())
            {
                lst.add(getWorkflowModule().createListOption(result.getString("Ref_PIM"),result.getString("Ref_PIM")));
            }
        } catch (Exception e) {
            log.error("Erreur dans la methode liste des references PIM :"+e.getClass()+" _ "+ e.getStackTrace() +e.getMessage());
        }
        return lst;
    }
 
}
