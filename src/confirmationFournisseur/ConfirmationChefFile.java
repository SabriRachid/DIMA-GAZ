package confirmationFournisseur;



import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;

public class ConfirmationChefFile extends Methodes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(ConfirmationChefFile.class);
//	protected ActionListener lisnerBtn;
//
//	@SuppressWarnings("serial")
//	public PremiereSinature() {
//		lisnerBtn = new ActionListener() {  
//			public void onClick(ActionEvent arg0) {
//				// Navigator.getNavigator().showAlertBox("Click");
//				try {
//					decrypte_singature();
//				} catch (Exception e) {
//					log.error("CS:Erreur in PremiereSinature methode 1: "+e.getClass()+" _ "+e.getMessage());
//				}
//			}
//		};
//	}
	
	
	public boolean onAfterLoad() 
    {
	
	instance= getWorkflowInstance();
	
	String user =this.getWorkflowModule().getLoggedOnUser().getFullName();
	
	instance.setValue("REG_NomPrenomDGPDAFP",user);
	
	getResourceController().setMandatory( "REG_PinDGZDGFExp2", true );
	
	return super.onAfterLoad();
	}
	
	public boolean isOnChangeSubscriptionOn(IProperty Field)
	{
		if (Field.getName().equals("REG_PinDGPDAFP"))
			return true;
		
		return false;
	}
	
	public void onPropertyChanged(IProperty Field) {
		instance=getWorkflowInstance();
		rc=getResourceController();
		
		if (Field.getName().equals("REG_PinDGPDAFP"))
		{

		}
	}
	
	public boolean onBeforeSubmit(IAction action)
	{
		instance=getWorkflowInstance();
		rc=getResourceController();
		
		
			
		return super.onBeforeSubmit(action);
	}
}
