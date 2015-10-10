package co.certicamara.portalfunctionary.api.representations;

import java.util.List;

import co.certicamara.portalfunctionary.infrastructure.mom.dtos.DocumentInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class CompleteTaskDTO {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private final String requestId;
    
    private final String taskName;
    
    private final JsonNode formValues;
    
    private final List<DocumentInfo> documentsList;
    
    private final boolean signDocuments;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    @JsonCreator
    public CompleteTaskDTO(@JsonProperty("requestId") String requestId, @JsonProperty("taskName") String taskName, @JsonProperty("formValues") JsonNode formValues, 
            @JsonProperty("documentList") List<DocumentInfo> documentsList, @JsonProperty("signDocuments") boolean signDocuments) {
        
        this.requestId = requestId;
        this.taskName = taskName;
        this.formValues = formValues;
        this.documentsList = documentsList;
        this.signDocuments = signDocuments;
        
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getRequestId() {
        return requestId;
    }

    public String getTaskName() {
        return taskName;
    }

    public JsonNode getFormValues() {
        return formValues;
    }

    public List<DocumentInfo> getDocumentsList() {
        return documentsList;
    }

    public boolean isSignDocuments() {
        return signDocuments;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
