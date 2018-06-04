package Citysystem.Dev.PIM.BO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.structs.Period;
/**
 * Created on 07 juin 2016
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class Mois_Importation extends BaseDocumentExtension{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(Mois_Importation.class);
            
    /**--------------------------------------------------------------------------
     * 
     *---------------------------------------------------------------------------*/
    IWorkflowInstance instance;
    IResourceController rc;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
     /**--------------------------------------------------------------------------
     * 
     *---------------------------------------------------------------------------*/
      @Override
      public boolean isOnChangeSubscriptionOn(IProperty Field) {
          if (Field.getName().equals("PIM_MoisIMPO_Title"))
              return true;
          return false;
      }
      /**--------------------------------------------------------------------------
      * 
      *---------------------------------------------------------------------------*/
       @Override
       public void onPropertyChanged(IProperty Field) {
            
            super.onPropertyChanged(Field);
        }
       /**--------------------------------------------------------------------------
        * 
        *---------------------------------------------------------------------------*/
       @Override
    public boolean onBeforeSubmit(IAction action) {
           
           instance = getWorkflowInstance();
           rc = getResourceController();
           
           // Reference vdoc
           String P_str_ref = (String) instance.getValue("sys_Reference");
           String P_Mois = (String) instance.getValue("PIM_MoisIMPO_Title");
           Period  Periode =(Period)instance.getValue("PIM_PeriodeImport_BO");
           
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
           String DPID = sdf.format(Periode.getStartDate());
           String DPIF = sdf.format(Periode.getEndDate());
           
          // rc.alert("Date debut : "+DPID +" Date fin "+DPIF);
           Insert_PIM(P_str_ref,P_Mois,DPID,DPIF);
           //--------------------------------------------------
           /*
           String P1,P2;
           P1 = Periode.getStartDate().toString();
           P2 = Periode.getEndDate().toString();
           rc.alert("Date debut : "+P1 +" Date fin "+P2);
           Insert_PIM(P_str_ref+"2",P_Mois,P1,P2);
           */
        return super.onBeforeSubmit(action);
    }
       /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
        *INSERT PIM BDD
        *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
        */
       public void Insert_PIM(String Ref_PIM,String Mois_PIM,String  Date_D_PIM,String  Date_F_PIM) {
           try{
               
               instance=getWorkflowInstance();
               connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
               String req = "INSERT INTO PIM (Ref_PIM,Mois_PIM,Date_Deb_PIM,Date_Fin_PIM)VALUES(?,?,?,?)";
               st = connection.prepareStatement(req);
               st.setString(1, Ref_PIM);
               st.setString(2, Mois_PIM);
               st.setString(3 , Date_D_PIM);
               st.setString(4,  Date_F_PIM);
               st.executeUpdate();
               
       }catch(Exception e)
       {
           log.error("CS-Error in  Insert_PIM method : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
          
       }
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
      
}
