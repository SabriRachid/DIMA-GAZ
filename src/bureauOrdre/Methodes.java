package bureauOrdre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;

import dao.ConnextionBDD;

public class Methodes extends ConnextionBDD{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

protected static final Logger log = Logger.getLogger(Methodes.class);
	
	
	
	IWorkflowInstance instance;
	IWorkflowModule module;
	IResourceController rc;
	Connection cnx;
	IContext cx;
	PreparedStatement st;
	ResultSet rs;
	
	public String getDesignation()
	{
		String lst = "";
		instance=getWorkflowInstance();
		module = getWorkflowModule();
		rc = getResourceController();
		
		//String ref=(String) instance.getValue("sys_reference");
		
		try{
			cnx=getConnectionVDoc("Ref_DimaGaz").getConnection();
		    String query = "select Libelle from ChefFile where Id=1";
			       	
		   st = cnx.prepareStatement(query);
		   rs=st.executeQuery();
		   
		   while(rs.next())
		   {
			   lst=rs.getString("Libelle");
		   }
			 
		}
		catch(Exception e){
			log.error("CS:ERREur in getDesignation method "+e.getClass()+" _ "+e.getMessage());
		}

		return lst;
	}
}
