package gp.e3.autheo.authentication.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a business exception handled by the system.
 * 
 * @author LeanFactory
 */
public class BusinessException implements IException {

	private final String type;
	private final String errorMessage;

	@JsonCreator
	public BusinessException(@JsonProperty("errorMessage") String errorMessage) {
		this.type = ExceptionType.BUSINESS.getName();
		this.errorMessage = errorMessage;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public int compareTo(IException exception) {

		int answer = 0;

		answer += type.compareToIgnoreCase(exception.getType());
		answer += errorMessage.compareToIgnoreCase(exception.getErrorMessage());

		return answer;
	}
}