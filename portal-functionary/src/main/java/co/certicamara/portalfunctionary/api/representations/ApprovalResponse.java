package co.certicamara.portalfunctionary.api.representations;

import co.certicamara.portalfunctionary.domain.entities.RequestStatus;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CaseInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A data structure to represent error messages.
 * 
 * @author LeanFactory
 */
public class ApprovalResponse {

    /**
     * The HTTP status code to represent by the error message.
     */
    private final String idRequest;

    /**
     * The exception that contains the information of the error.
     */
    private final CaseInfo caseInfo;

    /**
     * The exception that contains the information of the error.
     */
    private final RequestStatus state;

    /**
     * The revisor who approved the request
     */
    private final String revisorName;
    
    /**
     * The responsable person for the case execution.
     */
    private final String assignePerson;

    /**
     * The request approval observations.
     */
    private final String observations;

    /**
     * The request approval observations.
     */
    private final String responseMessage;

    /**
     * Constructor of the class.
     * 
     * @param httpStatusCode, The HTTP status code to represent by the error message.
     * @param exception, The exception that contains the information of the error.
     */
    @JsonCreator
    public ApprovalResponse(@JsonProperty("idRequest") String idRequest, @JsonProperty("caseInfo") CaseInfo caseInfo, @JsonProperty("state") RequestStatus state, 
            @JsonProperty("assignePerson") String assignePerson, @JsonProperty("observations") String observations, @JsonProperty("responseMessage") String responseMessage, 
            @JsonProperty("revisorName") String revisorName) {
        
        this.caseInfo = caseInfo;
        this.idRequest = idRequest;
        this.state = state;
        this.assignePerson = assignePerson;
        this.observations = observations;
        this.responseMessage = responseMessage;
        this.revisorName = revisorName;
    }

    public String getIdRequest() {
        return idRequest;
    }

    public CaseInfo getCaseInfo() {
        return caseInfo;
    }

    public RequestStatus getState() {
        return state;
    }

    public String getAssignePerson() {
        return assignePerson;
    }

    public String getObservations() {
        return observations;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getRevisorName() {
        return revisorName;
    }


}