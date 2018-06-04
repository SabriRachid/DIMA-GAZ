package Citysystem.Dev.EBCA.EB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IUser;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;

import beans.ConnextionBDD;

/**
 * Created on 05 juin 2016
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class ExpressionBesoin extends ConnextionBDD{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(ExpressionBesoin.class);
    
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
        
     // We get connected user
        IUser connectedUser = getWorkflowModule().getLoggedOnUser();
        
        /** Chef de centre**/
        instance.setValue("EB_NomPrenom", connectedUser.getFullName());
        
       /** Centre  **/
       instance.setValue("EB_Centre", connectedUser.getSocialCategory());
       
       /** Liste des importations **/
     //  instance.setList("EB_RefPIM",getPIM());
       
        return super.onAfterLoad();
 
    }
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ON BEFORE SUBMIT
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onBeforeSubmit(IAction action) {
        instance = getWorkflowInstance();
        String RPIM = (String) instance.getValue("EB_RefPIM");
        String Centre = (String) instance.getValue("EB_Centre");
        Check_ChefCentre(RPIM,Centre);
        return super.onBeforeSubmit(action);
    }
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CHARGER LES PIM
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    /*
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
    */
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *INSERTION BESOIN PAR CENTRE
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void SaveBesoins(String Ref,String Centre,String Chef,float Qtven,float Qtach,float Stofin,float Qtbes,float Prev,float Totven,float Totach,float TotStofin,float Totbes,float Totprev, String PIM) {
        try{
            
            instance=getWorkflowInstance();
            connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
            String req = "INSERT INTO Besoins(Ref,Centre,ChefCentre,Qte_Vendue,Qte_Achats,Stock_Final,Besoin_Qte,Prevision,Total_Ventes,Total_Achats,Total_StockF,Total_Besoins,Total_Prevision,Ref_PIM)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            st = connection.prepareStatement(req);
            st.setString(1, Ref);
            st.setString(2, Centre);
            st.setString(3, Chef);
            st.setFloat(4, Qtven);
            st.setFloat(5, Qtach);
            st.setFloat(6, Stofin);
            st.setFloat(7, Qtbes);
            st.setFloat(8, Prev);
            st.setFloat(9, Totven);
            st.setFloat(10, Totach);
            st.setFloat(11, TotStofin);
            st.setFloat(12, Totbes);
            st.setFloat(13,Totprev); 
            st.setString(14,PIM); 
            st.executeUpdate();
            
    }catch(Exception e)
    {
        log.error("CS-Error in SaveBesoins method : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
       
    }
    }
   
    public boolean Check_ChefCentre(String RefPIM,String Centre) {
        try {
            
        rc = getResourceController();
        instance=getWorkflowInstance();
        int nb_centre =0;
        
        connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
        String query=("SELECT count(Centre) as NBC  FROM Besoins WHERE Ref_PIM ='"+RefPIM+"' and Centre ='"+Centre+"'");
        st = connection.prepareStatement(query);
        result =  st.executeQuery();
        while(result.next()) {
            nb_centre = result.getInt("NBC");
       
        }
        if (nb_centre==0) {
            
            // Reference vdoc
            String P_str_ref = (String) instance.getValue("sys_Reference");
            //String str_centre = (String) instance.getValue("EB_Centre");
            String str_chefC = (String) instance.getValue("EB_NomPrenom");
            float qte_ven = (float) instance.getValue("EB_QteVendue");
            float qte_ach = (float) instance.getValue("EB_QteAchetee");
            float stok_fin = (float) instance.getValue("EB_StockFinal");
            float besoi_qte = (float) instance.getValue("EB_BesoinQte");
            String P_PIM = (String) instance.getValue("EB_RefPIM");
            float prevision = 0;
            float P_total_ven =0;
            float P_total_ach = 0;
            float P_total_stkfin =0;
            float P_total_besoi =0;
            float P_total_prev = 0;
         
            SaveBesoins(P_str_ref,Centre,str_chefC,qte_ven,qte_ach,stok_fin,besoi_qte,prevision,P_total_ven,P_total_ach,P_total_stkfin,P_total_besoi,P_total_prev,P_PIM) ;
            
        }else {
         // rc.alert("Le centre '"+Centre+"' est remplire ses besoins veuillez annulez ce documents");  
          rc.inform("EB_Centre", "Le centre '"+Centre+"' existe déjà");
          log.info("CS-INFO :Le centre '"+Centre+"' est remplire ses besoins veuillez annulez ce documents");
        }
        
        }catch(Exception e)
        {
            rc.inform("EB_Centre", "Le centre '"+Centre+"' existe déjà");
            log.info("CS-INFO :Le centre '"+Centre+"' est remplire ses besoins veuillez annulez ce documents");
            log.error("CS-Error in Check_ChefCentre method : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
            
        }
       
        
        return true;
    }
}
