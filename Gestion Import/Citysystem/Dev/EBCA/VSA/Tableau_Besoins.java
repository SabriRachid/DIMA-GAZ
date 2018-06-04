package Citysystem.Dev.EBCA.VSA;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import beans.T_Besoins;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.ILinkedResource;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;

/**
 * Created on 23 mars 2016
 * @author R.SABRI 
 * Version  vdoc14
 * 
 */
public class Tableau_Besoins extends BaseDocumentExtension{
 
private static final long serialVersionUID = 1L;
protected static final Logger log = Logger.getLogger(Tableau_Besoins.class);
 //Déclaration
 IWorkflowInstance instance;
 IResourceController rc;
 Connection connection;
 PreparedStatement st;
 ResultSet result;
 public IContext context;
 IWorkflowModule module;
 
 
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
  *EVENNEMENT VDOC
  *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
  */
 @Override
public boolean onAfterLoad() {
    // IMPORT VAL IN DB
     RemplireTableBesion_DB() ;
    return super.onAfterLoad();
}
 
 @Override
public void onPropertyChanged(IProperty property) {


    super.onPropertyChanged(property);
}
 
 @Override
public boolean isOnChangeSubscriptionOn(IProperty property) {
     
     return false;
}
 /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  *IMPORTE TABLEAUX  
  *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
  */
 public void RemplireTableBesion_by_Table() {
     
     try {
         instance = getWorkflowInstance();
        ArrayList<T_Besoins> list = new ArrayList<T_Besoins>();

         // recuperer les lignes de le tableau de besoins EB
         Collection associ = (Collection) instance.getLinkedResources("EBCA_BesoinParCentre_EB");

         if (associ.size() != 0) {

             for (Iterator iter1 = associ.iterator(); iter1.hasNext();) {

                 T_Besoins Tab = new T_Besoins();

                 ILinkedResource asso = (ILinkedResource) iter1.next();
               
                 Tab.setNom_prenom((String) asso.getValue("EBCA_NomPrenom_EB"));
                 Tab.setCentre((String) asso.getValue("EBCA_Centre_EB"));
                 Tab.setQte_achetee((Float) asso.getValue("EBCA_QteAchetee_EB"));
                 Tab.setQte_vendue((Float) asso.getValue("EBCA_QteVendu_EB"));
                 Tab.setBesoin_qte((Float) asso.getValue("EBCA_BesoinQte_EB"));
                 Tab.setStock_final((Float) asso.getValue("EBCA_StockFinal_EB"));
                
                 list.add(Tab);
             }
         }
         // mettre les lignes recuperent de le tableau EB  dans le tableauValidation service achat
         if(((Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA")).size()==0)
         {
         for (T_Besoins Tbesoin : list) {
             // création d'une ligne 
              ILinkedResource linkedResource = instance.createLinkedResource( "EBCA_BesoinParCentre_VSA" );
              //positionnement de quelques valeurs 
              linkedResource.setValue("EBCA_NomPrenom_VSA", Tbesoin.getNom_prenom() );
              linkedResource.setValue("EBCA_Centre_VSA",Tbesoin.getCentre() );  
              linkedResource.setValue("EBCA_QteVendu_VSA",Tbesoin.getQte_vendue()); 
              linkedResource.setValue("EBCA_QteAchetee_VSA",Tbesoin.getQte_achetee()); 
              linkedResource.setValue("EBCA_StockFinal_VSA",Tbesoin.getStock_final()); 
              linkedResource.setValue("EBCA_BesoinQte_VSA", Tbesoin.getBesoin_qte());
              
              // ajout de la ligne au tableau
              instance.addLinkedResource( linkedResource );
         }
         }
     } catch (Exception e) {
         log.info("Error in RemplireTableBesion() method : " + e.getClass() + " - " + e.getMessage() +" - "+ e.getLocalizedMessage());
     }
     
 }
 /*---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
  *EXPORTE  TABLEAUX VERS BASE DE DONNEES 
  *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
  */
public void RemplireTableBesion_DB() {
     
     try {
      
         instance=getWorkflowInstance();
         connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
      
         // mettre les lignes recuperent de le tableau EB  dans le tableauValidation service achat
         if(((Collection) instance.getLinkedResources("EBCA_BesoinParCentre_VSA")).size()==0)
         {
             String query = "SELECT  Centre,ChefCentre,Qte_Vendue,Qte_Achats,Stock_Final,Besoin_Qte FROM BesoinParCentre";
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

}
