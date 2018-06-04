package expressionBesoin;



import com.axemble.vdoc.sdk.utils.Logger;

public class ExpressionBesoin extends Methodes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(ExpressionBesoin.class);

	public boolean onAfterLoad() 
    {
	
	instance= getWorkflowInstance();
	rc=getResourceController();
	module=getWorkflowModule();

	
//	String Chef =this.getWorkflowModule().getLoggedOnUser().getFullName();
//	
//	instance.setValue("EBCA_NomPrenom_EB", Chef);
//	
//	instance.setValue("EBCA_Centre_EB", GetCentre(Chef));
	
	return super.onAfterLoad();
	}
	
//	public boolean isOnChangeSubscriptionOn(IProperty Field)
//	{
////		if (Field.getName().equals("REG_PinDGPDAFP"))
////			return true;
//		
//		return false;
//	}
//	
//	public void onPropertyChanged(IProperty Field) {
//		instance=getWorkflowInstance();
//		rc=getResourceController();
//		
////		if (Field.getName().equals("REG_PinDGPDAFP"))
////		{
////
////		}
//	}
//	
//	public boolean onBeforeSubmit(IAction action)
//	{
//		instance=getWorkflowInstance();
//		rc=getResourceController();
//		
//		return super.onBeforeSubmit(action);
//	}
}
