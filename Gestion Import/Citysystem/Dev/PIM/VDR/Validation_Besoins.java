package Citysystem.Dev.PIM.VDR;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
import com.axemble.vdp.ui.framework.components.events.ActionEvent;
import com.axemble.vdp.ui.framework.components.listeners.ActionListener;
import com.axemble.vdp.ui.framework.widgets.CtlButton;
import com.axemble.vdp.ui.framework.widgets.CtlText;
/**
 * Created on 09 avril 2016
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
            
        Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VDR");

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
            
            Collection  associ =  (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VDR");
            
            if (associ.size()!=0) {
                float Total_Ventes=0;
                float Total_Achats=0;
                float Total_StockFinal=0;
                float Total_Besoins=0;
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        
                        
                        // Calcule Total Besoins ------------------------------------------------
                        float Qte_Besoin=(float) asso.getValue("EBCA_BesoinQte_VDR");
                       Total_Besoins+=Qte_Besoin;
                       
                       //Calcule Total Ventes ----------------------------------------------------
                       float Qte_Vendue=(float) asso.getValue("EBCA_QteVendu_VDR");
                      Total_Ventes+=Qte_Vendue;
                     
                      //Calcule Total Achats -------------------------------------------------------
                        float Qte_Achete=(float) asso.getValue("EBCA_QteAchetee_VDR");
                        Total_Achats+=Qte_Achete;
                       
                      //Calcule Total Stock Final ----------------------------------------------------
                       float Stock_Final=(float) asso.getValue("EBCA_StockFinal_VDR");
                       Total_StockFinal+=Stock_Final;
  
                     
                       instance.setValue("EBCA_TotalStockFinal_VDR", Total_StockFinal);//EBCA_TotalStockFinal_VDR
                       instance.setValue("EBCA_TotalAchats_VDR", Total_Achats);//EBCA_TotalAchats_VDR
                       instance.setValue("EBCA_TotalVentes_VDR", Total_Ventes);//EBCA_TotalVentes_VDR
                       instance.setValue("EBCA_TotalBesoins_VDR", Total_Besoins);//EBCA_TotalBesoins_VDR
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
            
            Collection  associ =  (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VDR");
            
            if (associ.size()!=0) {
                float Total_Ventes=0;
                float Total_Achats=0;
                float Total_StockFinal=0;
                float Total_Besoins=0;
                float Total_Prévisions=0;
                for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                        ILinkedResource asso = (ILinkedResource) iter1.next();
                        
                        
                        // Calcule Total Besoins ------------------------------------------------
                        float Qte_Besoin=(float) asso.getValue("EBCA_BesoinQte_VDR");
                       Total_Besoins+=Qte_Besoin;
                       
                       //Calcule Total Ventes ----------------------------------------------------
                       float Qte_Vendue=(float) asso.getValue("EBCA_QteVendu_VDR");
                      Total_Ventes+=Qte_Vendue;
                     
                      //Calcule Total Achats -------------------------------------------------------
                        float Qte_Achete=(float) asso.getValue("EBCA_QteAchetee_VDR");
                        Total_Achats+=Qte_Achete;
                       
                      //Calcule Total Stock Final ----------------------------------------------------
                       float Stock_Final=(float) asso.getValue("EBCA_StockFinal_VDR");
                       Total_StockFinal+=Stock_Final;
                        
                      //Calcule Total Prévisions -------------------------------------------------------
                      float Prévisions=(float) asso.getValue("EBCA_Prevision_VDR");
                      Total_Prévisions+=Prévisions;
      
                      instance.setValue("EBCA_TotalStockFinal_VDR", Total_StockFinal);//EBCA_TotalStockFinal_VDR
                      instance.setValue("EBCA_TotalAchats_VDR", Total_Achats);//EBCA_TotalAchats_VDR
                      instance.setValue("EBCA_TotalVentes_VDR", Total_Ventes);//EBCA_TotalVentes_VDR
                      instance.setValue("EBCA_TotalBesoins_VDR", Total_Besoins);//EBCA_TotalBesoins_VDR
                      instance.setValue("EBCA_TotalPrevisions_VDR", Total_Prévisions);//EBCA_TotalPrevisions_VDR
                      
                
                }  // End for
                   
            }// End If
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
        if(champ.getName().equals("EBCA_BesoinParCentre_VDR")) {
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
        if(champ.getName().equals("EBCA_BesoinParCentre_VDR")) {
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
        float P_total_prev = (float) instance.getValue("EBCA_TotalPrevisions_VDR");
        float P_total_besoin = (float) instance.getValue("EBCA_TotalBesoins_VDR");
        if (P_total_prev==0 && P_total_besoin==0) {
                rc.alert("Veuillez remplire les  Prévisions / Quantités besoin ");
                return false;
        }    
        return super.onBeforeSubmit(action);
    }
   //------------------------------------------------------------------------------------------
    // ONAFTERSUBMIT
    //-----------------------------------------------------------------------------------------
    @Override
    public boolean onAfterSubmit(IAction action) {
        ModificationBesoins();
        return super.onAfterSubmit(action);
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

             // recuperer les lignes de le tableau besoin 'Etape VF'
             Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VF");

         if (associ.size() != 0) {

             for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                 Besoins Tab = new Besoins();

                 ILinkedResource asso = (ILinkedResource) iter1.next();
                 Tab.setChefCentre((String) asso.getValue("EBCA_NomPrenom_VF"));
                 Tab.setCentre((String) asso.getValue("EBCA_Centre_VF"));
                 Tab.setQte_Besoin((Float) asso.getValue("EBCA_BesoinQte_VF"));
                 Tab.setQte_Vendu((Float) asso.getValue("EBCA_QteVendu_VF"));
                 Tab.setQte_Achtee((Float) asso.getValue("EBCA_QteAchetee_VF"));
                 Tab.setStock_Final((Float) asso.getValue("EBCA_StockFinal_VF"));
                 Tab.setPrevision((Float) asso.getValue("EBCA_Prevision_VF"));
             
                 list.add(Tab);
                 
             }//End for
         }//End If
         /*---------------------------------------------------------------------------------------------------------------------------*/
         // mettre les lignes recuperent de le tableau besoin [VF] dans le tableau besoin [VDR]
         /*---------------------------------------------------------------------------------------------------------------------------*/
         Collection TableVDR = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VDR");
     if(TableVDR.size()==0)
     {
         for (Besoins T_Besoin : list) {
             // création d'une ligne 
             ILinkedResource linkedResource = instance.createLinkedResource( "EBCA_BesoinParCentre_VDR" );
             
             //positionnement de quelques valeurs 
             linkedResource.setValue("EBCA_NomPrenom_VDR", T_Besoin.getChefCentre());
             linkedResource.setValue("EBCA_Centre_VDR",T_Besoin .getCentre());
             linkedResource.setValue("EBCA_BesoinQte_VDR", T_Besoin.getQte_Besoin());
             linkedResource.setValue("EBCA_QteVendu_VDR",T_Besoin.getQte_Vendu() );
             linkedResource.setValue("EBCA_QteAchetee_VDR",T_Besoin.getQte_Achtee());
             linkedResource.setValue("EBCA_StockFinal_VDR",T_Besoin.getStock_Final());
             linkedResource.setValue("EBCA_Prevision_VDR",T_Besoin.getPrevision());
           
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
     /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
      *EXPORT BESOINS 
      *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
      */
     public void ModificationBesoins() {
         try{
             
             instance=getWorkflowInstance();
             rc=getResourceController();
             connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
           
             //Reference PIM
             String Ref_PMI =(String) instance.getValue(IProperty.System.REFERENCE);
             float T_vente = (float) instance.getValue("EBCA_TotalVentes_VDR");
             float T_Achat = (float) instance.getValue("EBCA_TotalAchats_VDR");
             float T_Stock = (float) instance.getValue("EBCA_TotalStockFinal_VDR");
             float T_Besoin = (float) instance.getValue("EBCA_TotalBesoins_VDR");
             float T_prevision = (float) instance.getValue("EBCA_TotalPrevisions_VDR");
             float besoi_qte=0;
             float Prevision=0;
             
             Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VDR");
             if (associ.size() != 0) {

                 for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                     ILinkedResource asso = (ILinkedResource) iter1.next();
                  
                          besoi_qte = (float) asso.getValue("EBCA_BesoinQte_VDR");
                          Prevision = (float) asso.getValue("EBCA_Prevision_VDR");
                     rc.alert("QTE BESOIN ('"+besoi_qte+"') | PREVISION('"+Prevision+"')");
                     String query = "select Ref from besoins where Ref_PIM='"+Ref_PMI+"'";
                     st = connection.prepareStatement(query);
                     ResultSet result = st.executeQuery();
                  
                     while (result.next()) {
                         String ref = result.getString("Ref");
                         
                         String req = "UPDATE besoins SET ref=?"
                                 +" , Besoin_Qte=? ,Prevision=? , Total_Ventes=?"
                                 +" ,Total_Achats=? , Total_StockF=? , Total_Besoins=?"
                                 +" ,Total_Prevision=?";
                         st = connection.prepareStatement(req);
                         st.setString(1, ref);
                         st.setFloat(2, besoi_qte);
                         st.setFloat(3, Prevision);
                         st.setFloat(4, T_vente);
                         st.setFloat(5, T_Achat);
                         st.setFloat(6, T_Stock);
                         st.setFloat(7, T_Besoin);
                         st.setFloat(8,T_prevision); 
                         st.executeUpdate();
                     }
                     
                 } // End for
               //---------------------------------------------------------------------------------------    
                
             }
                 
     }catch(Exception e)
     {
         log.error("CS-Error in ModificationBesoins method : " + e.getClass() + " - " + e.getMessage() + "  - "+ Thread.currentThread().getStackTrace()[1].getMethodName());
        
     }
     }

     
}
