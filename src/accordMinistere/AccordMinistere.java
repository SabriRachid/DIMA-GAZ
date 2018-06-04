package accordMinistere;



import com.axemble.vdoc.sdk.interfaces.IAction;
import com.axemble.vdoc.sdk.interfaces.IProperty;
import com.axemble.vdoc.sdk.utils.Logger;

public class AccordMinistere extends Methodes{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected static final Logger log = Logger.getLogger(AccordMinistere.class);

	public boolean onAfterLoad() 
    {
	
	instance= getWorkflowInstance();
	rc=getResourceController();


	
	return super.onAfterLoad();
	}
	
	public boolean isOnChangeSubscriptionOn(IProperty Field)
	{
//		if (Field.getName().equals("REG_PinDGPDAFP"))
//			return true;
		
		return false;
	}
	
	public void onPropertyChanged(IProperty Field) {
		instance=getWorkflowInstance();
		rc=getResourceController();
		
//		if (Field.getName().equals("REG_PinDGPDAFP"))
//		{
//
//		}
	}
	
	public boolean onBeforeSubmit(IAction action)
	{
		instance=getWorkflowInstance();
		rc=getResourceController();
		
		return super.onBeforeSubmit(action);
	}
}
