package confirmationChefFile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.axemble.vdoc.sdk.interfaces.IContext;
import com.axemble.vdoc.sdk.interfaces.IResourceController;
import com.axemble.vdoc.sdk.interfaces.IWorkflowInstance;
import com.axemble.vdoc.sdk.interfaces.IOptionList.IOption;
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
	
	
	
	public ArrayList<IOption> GetPays()
	{
		ArrayList<IOption> lst = new ArrayList<IOption>();
		
		try {	
			cnx=getConnectionVDoc("Ref_DimaGaz").getConnection();
			
			String req="SELECT * FROM FournisseurPays'";
			st = cnx.prepareStatement(req);
			rs=st.executeQuery();
			
			while(rs.next())
			{
				lst.add(getWorkflowModule().createListOption(rs.getString("Libelle"),rs.getString("Libelle")));
			}
		} catch (Exception e) {
			log.error("CS Erreur in GetPays methode : "+e.getClass()+" _ "+e.getMessage());
		}
		
		return lst;
	}

	public boolean IfEUR(String Libelle)
	{
		String str = "";
		try {	
			cnx=getConnectionVDoc("Ref_DimaGaz").getConnection();
			
			String req="SELECT * FROM FournisseurPays where libelle ='"+Libelle+"'";
			st = cnx.prepareStatement(req);
			rs=st.executeQuery();
			
			while(rs.next())
			{
				str=rs.getString("Eur");
			}
			
			if(str.equals("EUR"))
				return true;
			
		} catch (Exception e) {
			log.error("CS Erreur in IfEUR methode : "+e.getClass()+" _ "+e.getMessage());
		}
		
		return false;
	}
}
