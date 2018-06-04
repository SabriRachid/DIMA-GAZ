package Citysystem.Dev.EBCA.RM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.axemble.vdoc.sdk.document.extensions.BaseDocumentExtension;
import com.axemble.vdoc.sdk.exceptions.PortalModuleException;
import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IConnectionDefinition;
import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.modules.IWorkflowModule;

public class InsertionPlaches extends BaseDocumentExtension{
    /**
     * Created on 07 avril 2016
     * Modify on 06 juin 1016 V 3.0
     * @author R.SABRI 
     * Version  vdoc14
     * 
     */
    private static final long serialVersionUID = 1L;
    protected static final Logger log = Logger.getLogger(InsertionPlaches.class);
    
  //Déclaration
    IWorkflowInstance instance;
    IResourceController rc;
    Connection connection;
    PreparedStatement st;
    ResultSet result;
    public IContext context;
    IWorkflowModule module;
    /*------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *CONNEXION AU BASE DE DONNEES 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    public IConnectionDefinition<java.sql.Connection> ConnectionDefinition (String Ref_externe) throws PortalModuleException
    {
    context = getWorkflowModule().getContextByLogin("sysadmin");
     return (IConnectionDefinition<Connection>) getPortalModule().getConnectionDefinition(context, Ref_externe);
    }
    /*------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ONAFTERLOAD
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onAfterLoad() {
       
        return super.onAfterLoad();
    }
    /*------------------------------------------------------------------------------------------------------------------------------------------------------------------
     *ONBEFORESUBMIT 
     *-------------------------------------------------------------------------------------------------------------------------------------------------------------------
     */
    @Override
    public boolean onBeforeSubmit(IAction action) {
      
        return super.onBeforeSubmit(action);
    }

}
