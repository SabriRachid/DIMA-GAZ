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
import beans.ChefFile;

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
public class ChefFileProvider extends BaseViewProvider implements ICollectionModelViewProvider {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(ChefFileProvider.class);

	PreparedStatement st;
	Connection connection;
	IContext ctx;
	ResultSet rs;
	
	/**
	 * @param context
	 * @param view
	 */
	public ChefFileProvider(INavigateContext context, CtlAbstractView view) {
		super(context, view);
	}

	public void init() {
		super.init();
		// We get view model
		CollectionViewModel viewModel = (CollectionViewModel) getModel();
		
		// We build columns and add them to view model
		//ViewModelColumn modelColumn = new ViewModelColumn("Chef_Id", "Id Chef de File",ViewModelColumn.TYPE_INTEGER);
		//viewModel.addColumn(modelColumn);
		
		ViewModelColumn modelColumn = new ViewModelColumn("Chef_Libelle", "Libelle",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);
		
		modelColumn = new ViewModelColumn("Chef_Adresse", "Adresse",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);

		modelColumn = new ViewModelColumn("Chef_Mail", "Mail",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);

		modelColumn = new ViewModelColumn("Chef_Tel", "Téléphone",ViewModelColumn.TYPE_STRING);
		viewModel.addColumn(modelColumn);


	}


	public List<ViewModelItem> getModelItems() {
		
		IDirectoryModule iDirectoryModule = Modules.getDirectoryModule();
		ArrayList<ViewModelItem> cViewModelItem = new ArrayList<ViewModelItem>();
		try {
			
			IWorkflowInstance instance =ApplicationDataProvider.workflowInstance;
			// we get articles list
			ArrayList<ChefFile> ChefFiles = getChefFiles(instance);
			for (ChefFile chefFile : ChefFiles) {
				
				ViewModelItem viewModelItem = new ViewModelItem();			
				viewModelItem.setKey(chefFile.getLibelle());
				
				//viewModelItem.setValue("Chef_Id", chefFile.getId());
				viewModelItem.setValue("Chef_Libelle", chefFile.getLibelle());
				viewModelItem.setValue("Chef_Adresse", chefFile.getAdresse());
				viewModelItem.setValue("Chef_Mail", chefFile.getMail());
				viewModelItem.setValue("Chef_Tel", chefFile.getTel());
				cViewModelItem.add(viewModelItem);
			}
		} catch (Exception e) {
			log.error("CS:CIMAF_SI: Erreur in getModelItems method" +e.getClass()+" _ "+e.getMessage());
		} finally {
		
			Modules.releaseModule(iDirectoryModule);
  		}
		return cViewModelItem;
	}

	public ArrayList<ChefFile> getChefFiles(IWorkflowInstance instance) {

		ArrayList<ChefFile> ChefFilesList = new ArrayList<ChefFile>();
	
		
		try {

			connection=ConnectionDefinition("Ref_DimaGaz").getConnection();
			
				String query = "select * from ChefFile" ;
				
		    	// we prepare the query
				st=connection.prepareStatement(query);
				rs = st.executeQuery();
		 
			while (rs.next()) {
				ChefFile chefFile = new ChefFile();
				//chefFile.setId(rs.getInt("Id"));
				chefFile.setLibelle(rs.getString("Libelle"));
				chefFile.setAdresse(rs.getString("Adresse"));
				chefFile.setMail(rs.getString("Mail"));
				chefFile.setTel(rs.getString("Tel"));
				ChefFilesList.add(chefFile);
			}

		} catch (Exception e) {
			
			log.error("CS:CIMAF_SI:Error in ChefFilesProvider getChefFiles method : "+ e.getClass() + "e4 - " + e.getMessage());
		} finally {
				
		}
		return ChefFilesList;
	}

    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    	ctx = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(ctx, Ref_externe);
    }
}
