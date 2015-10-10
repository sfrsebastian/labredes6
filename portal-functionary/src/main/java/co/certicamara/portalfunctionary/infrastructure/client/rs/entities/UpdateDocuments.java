package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateDocuments {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private final String caseId;
    
    private final String requestId;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    @JsonCreator
    public UpdateDocuments(@JsonProperty("caseId") String caseId, @JsonProperty("requestId") String requestId) {
        
        this.caseId = caseId;
        this.requestId = requestId;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getCaseId() {
        return caseId;
    }

	public String getRequestId() {
		return requestId;
	}


    ////////////////////////
    // Private Methods
    ////////////////////////

}
