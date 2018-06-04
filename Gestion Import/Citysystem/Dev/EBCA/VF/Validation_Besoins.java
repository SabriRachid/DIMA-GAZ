package Citysystem.Dev.EBCA.VF;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import beans.Besoins;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
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
        try {
            
        Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VF");

        if (associ.size() == 0) {
     
         //------------------------------------------------------------------------------------------
         // Importation des valeurs via la base de données 
          //------------------------------------------------------------------------------------------
            
            RemplirTableauBesoins();
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
    
    public void TotalPartiel() {
        try {
            
            instance=getWorkflowInstance();
            rc = getResourceController();
            
            Collection  associ =  (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VF");
            
            if (associ.size()!=0) {
                float Total_Ventes=0;
                float Total_Achats=0;
                float Total_StockFinal=0;
                float Total_Besoins=0;
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        
                        
                        // Calcule Total Besoins ------------------------------------------------
                        float Qte_Besoin=(float) asso.getValue("EBCA_BesoinQte_VF");
                       Total_Besoins+=Qte_Besoin;
                       
                       //Calcule Total Ventes ----------------------------------------------------
                       float Qte_Vendue=(float) asso.getValue("EBCA_QteVendu_VF");
                      Total_Ventes+=Qte_Vendue;
                     
                      //Calcule Total Achats -------------------------------------------------------
                        float Qte_Achete=(float) asso.getValue("EBCA_QteAchetee_VF");
                        Total_Achats+=Qte_Achete;
                       
                      //Calcule Total Stock Final ----------------------------------------------------
                       float Stock_Final=(float) asso.getValue("EBCA_StockFinal_VF");
                       Total_StockFinal+=Stock_Final;
  
                     
                       instance.setValue("EBCA_TotalStockFinal_VF", Total_StockFinal);
                       instance.setValue("EBCA_TotalAchat_VF", Total_Achats);
                       instance.setValue("EBCA_TotalVentes_VF", Total_Ventes);
                       instance.setValue("VBCA_TotalBesions_VF", Total_Besoins);
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
            
            Collection  associ =  (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VF");
            
            if (associ.size()!=0) {
                float Total_Ventes=0;
                float Total_Achats=0;
                float Total_StockFinal=0;
                float Total_Besoins=0;
                float Total_Prévisions=0;
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        
                        
                       // Calcule Total Besoins ------------------------------------------------
                        float Qte_Besoin=(float) asso.getValue("EBCA_BesoinQte_VF");
                       Total_Besoins+=Qte_Besoin;
                       
                       //Calcule Total Ventes ----------------------------------------------------
                       float Qte_Vendue=(float) asso.getValue("EBCA_QteVendu_VF");
                      Total_Ventes+=Qte_Vendue;
                     
                      //Calcule Total Achats -------------------------------------------------------
                        float Qte_Achete=(float) asso.getValue("EBCA_QteAchetee_VF");
                        Total_Achats+=Qte_Achete;
                       
                      //Calcule Total Stock Final ----------------------------------------------------
                       float Stock_Final=(float) asso.getValue("EBCA_StockFinal_VF");
                       Total_StockFinal+=Stock_Final;
  
                        
                      //Calcule Total Prévisions -------------------------------------------------------
                      float Prévisions=(float) asso.getValue("EBCA_Prevision_VF");
                      Total_Prévisions+=Prévisions;
 
                
                instance.setValue("EBCA_TotalPrevision_VF", Total_Prévisions);
                instance.setValue("EBCA_TotalStockFinal_VF", Total_StockFinal);
                instance.setValue("EBCA_TotalAchat_VF", Total_Achats);
                instance.setValue("EBCA_TotalVentes_VF", Total_Ventes);
                instance.setValue("VBCA_TotalBesions_VF", Total_Besoins);
                     
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
        if(champ.getName().equals("EBCA_BesoinParCentre_VF")) {
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
        if(champ.getName().equals("EBCA_BesoinParCentre_VF")) {
            CalculerTotalBesoin();
        }
        super.onPropertyChanged(champ);
    }
    /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ON BEFORE SUBMIT
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onBeforeSubmit(IAction action) {
       
         instance = getWorkflowInstance();
         rc = getResourceController();
         float P_total_prev = (float) instance.getValue("EBCA_TotalPrevisions_VSA");
         if (P_total_prev==0) {
                 rc.alert("Veuillez remplire les  Prévisions des besoins");
         }      
         
        return super.onBeforeSubmit(action);
    }
 
     /*---------------------------------------------------------------------------------------------------------------------------
    //Cette methode permet de remplir le tableau des besoins depuis l'etape de validation achat 
     * ---------------------------------------------------------------------------------------------------------------------------*/
    
     @SuppressWarnings("unchecked")
     public void RemplirTableauBesoins() {
 
         instance = getWorkflowInstance();
         rc=getResourceController();
         try {
             ArrayList<Besoins> list = new ArrayList<Besoins>();

             // recuperer les lignes de le tableau besoin 'Etape VSA'
             Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA");

         if (associ.size() != 0) {

             for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                 Besoins Tab = new Besoins();

                 ILinkedResource asso = (ILinkedResource) iter1.next();
                 Tab.setChefCentre((String) asso.getValue("EBCA_NomPrenom_VSA"));
                 Tab.setCentre((String) asso.getValue("EBCA_Centre_VSA"));
                 Tab.setQte_Besoin((Float) asso.getValue("EBCA_BesoinQte_VSA"));
                 Tab.setQte_Vendu((Float) asso.getValue("EBCA_QteVendu_VSA"));
                 Tab.setQte_Achtee((Float) asso.getValue("EBCA_QteAchetee_VSA"));
                 Tab.setStock_Final((Float) asso.getValue("EBCA_StockFinal_VSA"));
                 Tab.setPrevision((Float) asso.getValue("EBCA_Prevision_VSA"));
             
                 list.add(Tab);
                 
             }//End for
         }//End If
         /*---------------------------------------------------------------------------------------------------------------------------*/
         // mettre les lignes recuperent de le tableau besoin [VSA] dans le tableau besoin [VF]
         /*---------------------------------------------------------------------------------------------------------------------------*/
         Collection TableVF = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VF");
     if(TableVF.size()==0)
     {
         for (Besoins T_Besoin : list) {
             // création d'une ligne 
             ILinkedResource linkedResource = instance.createLinkedResource( "EBCA_BesoinParCentre_VF" );
             
             //positionnement de quelques valeurs 
             linkedResource.setValue("EBCA_NomPrenom_VF", T_Besoin.getChefCentre());
             linkedResource.setValue("EBCA_Centre_VF",T_Besoin .getCentre());
             linkedResource.setValue("EBCA_BesoinQte_VF", T_Besoin.getQte_Besoin());
             linkedResource.setValue("EBCA_QteVendu_VF",T_Besoin.getQte_Vendu() );
             linkedResource.setValue("EBCA_QteAchetee_VF",T_Besoin.getQte_Achtee());
             linkedResource.setValue("EBCA_StockFinal_VF",T_Besoin.getStock_Final());
             linkedResource.setValue("EBCA_Prevision_VF",T_Besoin.getPrevision());
           
             // ajout de la ligne au tableau
             instance.addLinkedResource( linkedResource );
         }//End For
     }//End If
     
     //appel la methode CalculerTotalEstimeArticle() pour calcule le total estime
     CalculerTotalBesoin();
 } catch (Exception e) {
     log.error("CS-DIMA GAZ : RemplirTableauBesoins()  :" + e.getClass() + " - " + e.getMessage());
 }
 
}

     
}
