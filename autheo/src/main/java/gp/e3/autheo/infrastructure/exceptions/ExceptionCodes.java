package gp.e3.autheo.infrastructure.exceptions;

import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;

import javax.ws.rs.core.Response;


/**
 * This class handles the exception codes of the system.
 * 
 * @author LeanFactory
 */
public class ExceptionCodes  {

	///////////////////
	// Public Constants
	///////////////////
    
	public static final String AUTHEO_GET_USERNAME_BY_PASSWORD_TOKEN_EXCEPTION = "AUTHEO_GET_USERNAME_BY_PASSWORD_TOKEN_EXCEPTION";
	
	public static final String AUTHEO_GET_PASSWORD_TOKEN_BY_USERNAME_EXCEPTION = "AUTHEO_GET_PASSWORD_TOKEN_BY_USERNAME_EXCEPTION";
	
	public static final String AUTHEO_GET_USER_EXCEPTION = "AUTHEO_GET_USER_EXCEPTION";
	
	public static final String AUTHEO_CREATE_PASSWORD_TOKEN_EXCEPTION = "AUTHEO_CREATE_PASSWORD_TOKEN_EXCEPTION";
	
	public static final String AUTHEO_DELETE_PASSWORD_TOKEN_EXCEPTION = "AUTHEO_DELETE_PASSWORD_TOKEN_EXCEPTION";
	
	public static final String AUTHEO_UPDATE_USER_PASSWORD_EXCEPTION = "AUTHEO_UPDATE_USER_PASSWORD_EXCEPTION";
	
	///////////////////
	// Local Constants
	///////////////////
	
	private static final int BUSINESS_EXCEPTION_STATUS_CODE = 418;
    
    public static int getStatusCode(IException exception) {
        
        int statusCode;

        if (exception instanceof TechnicalException) {

            statusCode = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();

        } else {

            statusCode = BUSINESS_EXCEPTION_STATUS_CODE;

        }
        
        return statusCode;
    }
	
    
}