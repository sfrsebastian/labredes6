package co.certicamara.portalfunctionary.infrastructure.constants;

import javax.ws.rs.core.Response;

import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;


/**
 * This class represents a business exception handled by the system.
 * 
 * @author LeanFactory
 */
public  class MessagesCodes  {

	///////////////////
	// Public Constants
	///////////////////
    
	///////////////////
	// Local Constants
	///////////////////
	
	private static final int BUSINESS_EXCEPTION_STATUS_CODE = 418;



	public int getStatusCode(IException exception) {
			
		int statusCode ;
			
			if ( exception instanceof TechnicalException ) {
				
				statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
				
			} else {
				
				statusCode = BUSINESS_EXCEPTION_STATUS_CODE ;
				
			}
			
			return statusCode;
		}

}