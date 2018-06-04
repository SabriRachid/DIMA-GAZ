package Citysystem.Dev.EBCA.VSA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;


import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdp.ui.framework.components.listeners.ActionListener;
/**
 * Created on 07 avril 2016
 * Modify on 06 juin 1016 V 3.0
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class Validation_Besoins extends BaseDocumentExtension{
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(Validation_Besoins.class);
    
    //Déclaration
    IWorkflowInstance instance;
    IResourceController rc;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    protected ActionListener listnerBtn;
    
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    context = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ONAFTERLOAD 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterLoad() {
        instance = getWorkflowInstance();
        rc = getResourceController();
        try {
            
        Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA");

        if (associ.size() == 0) {
     
         // Importation des valeurs via la base de données 
          RemplireTableBesion_DB() ;
         
       // Calcul des totals
          TotalPartiel();
         
           }
        
        }catch (Exception e)
        {
            String message = e.getMessage();
            if (message == null)
            {
                message = "";
            }
            log.error("CS Error dans la methode qui remplire le tableau des besoins - onAfterLoad method : " + e.getClass() + " - " + message);
        }
        return super.onAfterLoad();
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *TOTAL PARTIEL
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void TotalPartiel() {
 try {
            
            instance=getWorkflowInstance();
            rc = getResourceController();
            
            Collection  associ =  (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA");
            
            if (associ.size()!=0) {
                float Total_Ventes=0;
                float Total_Achats=0;
                float Total_StockFinal=0;
                float Total_Besoins=0;
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        
                        
                       // Calcule Total Besoins ------------------------------------------------
                        float Qte_Besoin=(float) asso.getValue("EBCA_BesoinQte_VSA");
                       Total_Besoins+=Qte_Besoin;
                       
                       //Calcule Total Ventes ----------------------------------------------------
                       float Qte_Vendue=(float) asso.getValue("EBCA_QteVendu_VSA");
                      Total_Ventes+=Qte_Vendue;
                     
                      //Calcule Total Achats -------------------------------------------------------
                        float Qte_Achete=(float) asso.getValue("EBCA_QteAchetee_VSA");
                        Total_Achats+=Qte_Achete;
                       
                      //Calcule Total Stock Final ----------------------------------------------------
                       float Stock_Final=(float) asso.getValue("EBCA_StockFinal_VSA");
                       Total_StockFinal+=Stock_Final;
  
                       instance.setValue("EBCA_TotalStockFinal_VSA", Total_StockFinal);
                       instance.setValue("EBCA_TotalAchats_VSA", Total_Achats);
                       instance.setValue("EBCA_TotalVentes_VSA", Total_Ventes);
                       instance.setValue("EBCA_TotalBesoins_VSA", Total_Besoins);
                     
                }  // End for
                
             
                
            }
        } catch (Exception e) {
            log.error("CS Error in TotalPartiel() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CALCULER TOTAL BESOIN 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public void CalculerTotalBesoin()
    {
        try {
            
            instance=getWorkflowInstance();
            rc = getResourceController();
            
            Collection  associ =  (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA");
            
            if (associ.size()!=0) {
                float Total_Ventes=0;
                float Total_Achats=0;
                float Total_StockFinal=0;
                float Total_Besoins=0;
                float Total_Prévisions=0;
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        
                        
                       // Calcule Total Besoins ------------------------------------------------
                        float Qte_Besoin=(float) asso.getValue("EBCA_BesoinQte_VSA");
                       Total_Besoins+=Qte_Besoin;
                       
                       //Calcule Total Ventes ----------------------------------------------------
                       float Qte_Vendue=(float) asso.getValue("EBCA_QteVendu_VSA");
                      Total_Ventes+=Qte_Vendue;
                     
                      //Calcule Total Achats -------------------------------------------------------
                        float Qte_Achete=(float) asso.getValue("EBCA_QteAchetee_VSA");
                        Total_Achats+=Qte_Achete;
                       
                      //Calcule Total Stock Final ----------------------------------------------------
                       float Stock_Final=(float) asso.getValue("EBCA_StockFinal_VSA");
                       Total_StockFinal+=Stock_Final;
  
                        
                      //Calcule Total Prévisions -------------------------------------------------------
                      float Prévisions=(float) asso.getValue("EBCA_Prevision_VSA");
                      Total_Prévisions+=Prévisions;
 
                
                instance.setValue("EBCA_TotalPrevisions_VSA", Total_Prévisions);
                instance.setValue("EBCA_TotalStockFinal_VSA", Total_StockFinal);
                instance.setValue("EBCA_TotalAchats_VSA", Total_Achats);
                instance.setValue("EBCA_TotalVentes_VSA", Total_Ventes);
                instance.setValue("EBCA_TotalBesoins_VSA", Total_Besoins);
                     
                }  // End for
                
             
                
            }
        } catch (Exception e) {
            log.error("CS Error in CalculerTotalBesoin() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
        }
        
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *IS ON CHANGES
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean isOnChangeSubscriptionOn(IProperty champ) {
        if(champ.getName().equals("EBCA_BesoinParCentre_VSA")) {
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
        if(champ.getName().equals("EBCA_BesoinParCentre_VSA")) {
            CalculerTotalBesoin();
        }
        super.onPropertyChanged(champ);
    }
  
     /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      *REMPLIRE TABLE BESOIN VIA BDD
      *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
      */
     public void RemplireTableBesion_DB() {
         
         try {
          
             instance=getWorkflowInstance();
             connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
             String Ref_PIM = (String) instance.getValue("sys_Reference");
          
             // mettre les lignes recuperent de le tableau EB  dans le tableauValidation service achat
             if(((Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA")).size()==0)
             {
                 String query = "SELECT  Centre,ChefCentre,Qte_Vendue,Qte_Achats,Stock_Final,Besoin_Qte FROM besoins WHERE Ref_PIM='"+Ref_PIM+"'";
                 st = connection.prepareStatement(query);
                 ResultSet result = st.executeQuery();
              
                 while (result.next()) {
                   
                 // création d'une ligne 
                  ILinkedResource linkedResource = instance.createLinkedResource( "EBCA_BesoinParCentre_VSA" );
                 
                  //positionnement de quelques valeurs 
                  linkedResource.setValue("EBCA_NomPrenom_VSA", result.getString("ChefCentre") );
                  linkedResource.setValue("EBCA_Centre_VSA",result.getString("Centre"));  
                  linkedResource.setValue("EBCA_QteVendu_VSA",result.getFloat("Qte_Vendue")); 
                  linkedResource.setValue("EBCA_QteAchetee_VSA",result.getFloat("Qte_Achats")); 
                  linkedResource.setValue("EBCA_StockFinal_VSA",result.getFloat("Stock_Final")); 
                  linkedResource.setValue("EBCA_BesoinQte_VSA", result.getFloat("Besoin_Qte"));
                  
                  // ajout de la ligne au tableau
                  instance.addLinkedResource( linkedResource );
             }
             }
         } catch (Exception e) {
             log.info("Error in RemplireTableBesion_DB() method : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
         }
         
     }

    
     /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      *EXPORT BESOINS 
      *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
      */
     public void ModificationBesoins() {
         try{
             
             instance=getWorkflowInstance();
             connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
             Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA");

             //Reference PIM
             String Ref_PMI =(String) instance.getValue(IProperty.System.REFERENCE);
             float T_vente = (float) instance.getValue("EBCA_TotalVentes_VSA");
             float T_Achat = (float) instance.getValue("EBCA_TotalAchats_VSA");
             float T_Stock = (float) instance.getValue("EBCA_TotalStockFinal_VSA");
             float T_Besoin = (float) instance.getValue("EBCA_TotalBesoins_VSA");
             float T_prevision = (float) instance.getValue("EBCA_TotalPrevisions_VSA");
             
             if (associ.size() != 0) {

                 for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                     ILinkedResource asso = (ILinkedResource) iter1.next();
                  
                     float besoi_qte = (float) asso.getValue("EBCA_BesoinQte_VSA");
                     float Prevision = (float) asso.getValue("EBCA_Prevision_VSA");
                     
                   //---------------------------------------------------------------------------------------    
                   String req = "UPDATE besoins SET Ref_PIM=?"
                           +" , Besoin_Qte=? ,Prevision=? , Total_Ventes=?"
                           +" ,Total_Achats=? , Total_StockF=? , Total_Besoins=?"
                           +" ,Total_Prevision=?";
                   st = connection.prepareStatement(req);
                   st.setString(1, Ref_PMI);
                   st.setFloat(2, besoi_qte);
                   st.setFloat(3, Prevision);
                   st.setFloat(4, T_vente);
                   st.setFloat(5, T_Achat);
                   st.setFloat(6, T_Stock);
                   st.setFloat(7, T_Besoin);
                   st.setFloat(8,T_prevision); 
                   st.executeUpdate();
                     
                 } // End for
             }
                 
     }catch(Exception e)
     {
         log.error("CS-Error in ModificationBesoins method : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        
     }
     }
   
     }
    
