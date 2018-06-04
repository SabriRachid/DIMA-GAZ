package Citysystem.Dev.EBCA.VSA;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;

/**
 * Created on 23 mars 2016
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class Total_Besoins extends BaseDocumentExtension{
private static final long serialVersionUID = 1L;
protected static final Logger log = Logger.getLogger(Total_Besoins.class);
     //Déclaration
     IWorkflowInstance instance;
     IResourceController rc;
     @Override
        public boolean onAfterLoad() {
         instance = getWorkflowInstance();
         
         // Champs de calcule 
         float Total_ventes=0;
          float Total_achat=0;
          float Total_stock_final=0;
          float Total_besoin=0;
          
          //  Récupérer les valeurs de formulaire expression de besoin
          Total_ventes  = (Float) instance.getValue("EBCA_TotalVentes_EB");
          Total_achat  = (Float) instance.getValue("EBCA_TotalAchats_EB");
          Total_stock_final  = (Float) instance.getValue("EBCA_TotalStockFinal_EB");
          Total_besoin  = (Float) instance.getValue("EBCA_TotalBesoins_EB");
         
          // Affecter les valeur de l'étape précedent EB
          instance.setValue("EBCA_TotalVentes_VSA", Total_ventes);
          instance.setValue("EBCA_TotalAchats_VSA", Total_achat);
          instance.setValue("EBCA_TotalStockFinal_VSA", Total_stock_final);
          instance.setValue("EBCA_TotalBesoins_VSA", Total_besoin);
          
          getResourceController().setMandatory("EBCA_TotalPrevisions_VSA", true);
            return super.onAfterLoad();
        }

     @Override
     public boolean isOnChangeSubscriptionOn(IProperty champ) {
         if(champ.getName().equals("EBCA_BesoinParCentre_VSA")) {
             return true;
         }
         return false;
     }
     
     @Override
     public void onPropertyChanged(IProperty champ) {
         if(champ.getName().equals("EBCA_BesoinParCentre_VSA")) {
             CalculerTotalBesoin();
         }
         super.onPropertyChanged(champ);
     }
     
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
                         
                         
                         // Calcule Total Besoins
                         if(asso.getValue("EBCA_BesoinQte_VSA")!=null){
                             float Qte_Besoin=(Float) asso.getValue("EBCA_BesoinQte_VSA");
                             Total_Besoins+=Qte_Besoin;
                             instance.setValue("EBCA_TotalBesoins_VSA", ""+Total_Besoins);
                         }
                         
                         //Calcule Total Ventes
                         if(asso.getValue("EBCA_QteVendu_VSA")!=null){
                             float Qte_Vendue=(Float) asso.getValue("EBCA_QteVendu_VSA");
                             Total_Ventes+=Qte_Vendue;
                             instance.setValue("EBCA_TotalVentes_VSA", ""+Total_Ventes);
                         }
                         
                       //Calcule Total Achats
                         if(asso.getValue("EBCA_QteAchetee_VSA")!=null){
                             float Qte_Achete=(Float) asso.getValue("EBCA_QteAchetee_VSA");
                             Total_Achats+=Qte_Achete;
                             instance.setValue("EBCA_TotalAchats_VSA", ""+Total_Achats);
                         }
                         
                       //Calcule Total Stock Final
                         if(asso.getValue("EBCA_StockFinal_VSA")!=null){
                             float Stock_Final=(Float) asso.getValue("EBCA_StockFinal_VSA");
                             Total_StockFinal+=Stock_Final;
                             instance.setValue("EBCA_TotalStockFinal_VSA", ""+Total_StockFinal);
                         }
                         
                       //Calcule Total Prévisions
                             float Prévisions=(Float) asso.getValue("EBCA_Prevision_VSA");
                            Total_Prévisions+=Prévisions;
                            instance.setValue("EBCA_TotalPrevisions_VSA", ""+Total_Prévisions);
                 }
                      
                 
             }
         } catch (Exception e) {
             log.error("Error in CalculerTotalBesoins() method : " + e.getClass() + " - " + e.getMessage() + "  - "+e.getStackTrace());
         }
         
     }
     
     @Override
        public boolean onBeforeSubmit(IAction action) {
           
             instance = getWorkflowInstance();
             rc = getResourceController();
             
             if ( instance.getValue("EBCA_TotalPrevisions_VSA")==null) 
                     rc.alert("Veuillez remplire les  Prévisions des besoins");
                 
             
            return super.onBeforeSubmit(action);
        }

         @Override
         public boolean onBeforeSave() {
             instance = getWorkflowInstance();
             rc = getResourceController();
             
             if ( instance.getValue("EBCA_TotalPrevisions_VSA")==null) 
                     rc.alert("Veuillez remplire les  Prévisions des besoins");
                 
                return super.onBeforeSave();
            }

}
