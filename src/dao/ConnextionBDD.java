package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.ICatalog;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflow;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;
import com.axemble.vdoc.sdk.utils.Logger;




public class ConnextionBDD extends BaseDocumentExtension{

	/**
	 * 
	 */
	protected static final Logger log = Logger.getLogger(ConnextionBDD.class);
	private static final long serialVersionUID = 1L;
	
	public  ICatalog catalog ;
	public  IWorkflow workflow ;
	public  IContext processContext;
	public  IWorkflowInstance instance;
	public  IWorkflowModule module;
//	public  IJdbcReference jdbc;
	public  Connection cnx;
	public  PreparedStatement st;
	public  String ReferenceBDD;
	public  IResourceController rc ;
	public  ResultSet rs;
	
	
	/*
	 * Cette méthode permet d'établir une connexion entre la base de données et VDoc.
	 */
//	public PreparedStatement getConnectionVDoc(String ref,String req) 	{
//		try{
//			processContext=getWorkflowModule().getContextByLogin("sysadmin");
//			@SuppressWarnings("unchecked")
//			IConnectionDefinition<java.sql.Connection> connectionDefinition = (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(processContext, ref);
//			cnx = connectionDefinition.getConnection();
//			st=cnx.prepareStatement(req);
//		
//	} catch (Exception e) {
//		 log.error("CS: getConnectionVDoc() 2 :" + e.getClass() + " - " + e.getMessage());
//	}
//	return st;
//	}
	
    public IConnectionDefinition<java.sql.Connection> getConnectionVDoc (String Ref_externe) throws PortalModuleException
    {
    	processContext = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(processContext, Ref_externe);
    }
}
