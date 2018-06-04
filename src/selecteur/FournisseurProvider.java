/**
 * 
 */
package selecteur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import provider.ApplicationDataProvider;
import beans.Fournisseurs;

import com.axemble.vdoc.sdk.Modules;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.runtime.INavigateContext;
import com.axemble.vdoc.sdk.modules.IDirectoryModule;
import com.axemble.vdoc.sdk.providers.BaseViewProvider;
import com.axemble.vdoc.sdk.utils.Logger;
import com.axemble.vdp.ui.core.providers.ICollectionModelViewProvider;
import com.axemble.vdp.ui.framework.composites.base.CtlAbstractView;
import com.axemble.vdp.ui.framework.composites.base.models.views.CollectionViewModel;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelColumn;
import com.axemble.vdp.ui.framework.composites.base.models.views.ViewModelItem;

/**
 * @author Administrateur
 * 
 */
@SuppressWarnings("deprecation")
public class FournisseurProvider extends BaseViewProvider implements ICollectionModelViewProvider {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(FournisseurProvider.class);

	PreparedStatement st;
	Connection connection;
	IContext ctx;
	ResultSet rs;
	
	/**
	 * @param context
	 * @param view
	 */
	public FournisseurProvider(INavigateContext context, CtlAbstractView view) {
		super(context, view);
	}

	public void init() {
		super.init();
		// We get view model
		CollectionViewModel viewModel = (CollectionViewModel) getModel();
		
		// We build columns and add them to view model
		//ViewModelColumn modelColumn = new ViewModelColumn("Four_Id", "Id Chef de File",ViewModelColumn.TYPE_INTEGER);
		//viewModel.addColumn(modelColumn);
		
		ViewModelColumn modelColumn = new ViewModelColumn("Four_Nom", "Nom",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		
	    modelColumn = new ViewModelColumn("Four_Pays", "Pays",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		
		modelColumn = new ViewModelColumn("Four_Adresse", "Adresse",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);

		modelColumn = new ViewModelColumn("Four_Mail", "Mail",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);

		modelColumn = new ViewModelColumn("Four_Tel", "Téléphone",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);


	}


	public List<ViewModelItem> getModelItems() {
		
		IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
		ArrayList<ViewModelItem> cViewModelItem = new ArrayList<ViewModelItem>();
		try {
			
			IWorkflowInstance instance =ApplicationDataProvider.workflowInstance;
			// we get articles list
			ArrayList<Fournisseurs> fournisseurs = getFournisseurs(instance);
			for (Fournisseurs four : fournisseurs) {
				
				ViewModelItem viewModelItem = new ViewModelItem();			
				viewModelItem.setKey(four.getNom_four());
				
				//viewModelItem.setValue("Four_Id", four.getId());
				viewModelItem.setValue("Four_Nom", four.getNom_four());
				viewModelItem.setValue("Four_Pays", four.getPays());
				viewModelItem.setValue("Four_Adresse", four.getAdresse());
				viewModelItem.setValue("Four_Mail", four.getMail());
				viewModelItem.setValue("Four_Tel", four.getTel());
				cViewModelItem.add(viewModelItem);
			}
		} catch (Exception e) {
			log.error("CS:CIMAF_SI: Erreur in getModelItems method" +e.getClass()+" _ "+e.getMessage());
		} finally {
		
			Modules.releaseModule(iDirectoryModule);
  		}
		return cViewModelItem;
	}

	public ArrayList<Fournisseurs> getFournisseurs(IWorkflowInstance instance) {

		ArrayList<Fournisseurs> FournisseursList = new ArrayList<Fournisseurs>();
	
		
		try {

			connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
			
				String query = "select * from fournisseur" ;
				
		    	// we prepare the query
				st=connection.prepareStatement(query);
				rs = st.executeQuery();
		 
			while (rs.next()) {
				Fournisseurs four = new Fournisseurs();
				//four.setId(rs.getInt("Id"));
				four.setNom_four(rs.getString("Nom_Fournisseur"));
				four.setPays(rs.getString("Pays"));
				four.setAdresse(rs.getString("Adresse"));
				four.setMail(rs.getString("Mail"));
				four.setTel(rs.getString("Tel"));
				FournisseursList.add(four);
			}

		} catch (Exception e) {
			
			log.error("CS:CIMAF_SI:Error in getFournisseurs method : "+ e.getClass() + "e4 - " + e.getMessage());
		} finally {
				
		}
		return FournisseursList;
	}

    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    	ctx = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
    }
}
