package co.certicamara.portalfunctionary.infrastructure.exceptions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class represents a technical exception handled by the system.
 * 
 * @author LeanFactory
 */
public class TechnicalException implements IException {

    private final String type;
    private final String errorMessage;

    @JsonCreator
    public TechnicalException(@JsonProperty("errorMessage") String errorMessage) {
        this.type = ExceptionType.TECHNICAL.getName();
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