package accordMinistere;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
		
}
