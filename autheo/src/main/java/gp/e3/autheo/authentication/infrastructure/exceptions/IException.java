package gp.e3.autheo.authentication.infrastructure.exceptions;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * The common interface implemented by all the exceptions handled by the system.
 * 
 * @author LeanFactory
 */
@JsonDeserialize(as = BusinessException.class)
public interface IException extends Comparable<IException> {
	
	public String getType();
	
	public String getErrorMessage();
	
}