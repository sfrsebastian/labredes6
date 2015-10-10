package co.certicamara.portalfunctionary.infrastructure.mom.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ApproveRequestDTO {

    ////////////////////////
    // Attributes
    ////////////////////////

    private final String id;

    private final String procedure;

    private String assignedPerson;

    private final String observations;
    
    private final String revisorName;
    
    private final List<DocumentInfo> documentsList;

    private final boolean signDocuments;
    
    // ////////////////////////////
    // Constructor
    // ////////////////////////////

    @JsonCreator
    public ApproveRequestDTO(@JsonProperty("id") String id, @JsonProperty("procedure") String procedure,
            @JsonProperty("assignedPerson") String assignedPerson, @JsonProperty("observations") String observations,
            @JsonProperty("revisorName") String revisorName, @JsonProperty("documentList") List<DocumentInfo> documentsList, 
            @JsonProperty("signDocuments") boolean signDocuments ) {
        
        this.id = id;
        this.procedure = procedure;
        this.assignedPerson = assignedPerson;
        this.observations = observations;
        this.revisorName = revisorName;
        this.documentsList = documentsList;
        this.signDocuments = signDocuments;
        
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getAssignedPerson() {
        return assignedPerson;
    }

    public void setAssignedPerson(String assignedPerson) {
        this.assignedPerson = assignedPerson;
    }

    public String getId() {
        return id;
    }

    public String getProcedure() {
        return procedure;
    }

    public String getObservations() {
        return observations;
    }

    public String getRevisorName() {
        return revisorName;
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
