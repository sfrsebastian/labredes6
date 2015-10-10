package co.certicamara.portalfunctionary.api.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateSignedDocumentDTO {
    
    ////////////////////////
    // Attributes
    ////////////////////////

    private final String name;
    
    private final String requestId;

    private final String signedFileHash;

    private final String caseId;
    
    private final String temporalFileHashId;
    
    private final String taskName;
    
    private String ownerName;
    
    private String ownerRol;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    
    @JsonCreator
    public CreateSignedDocumentDTO(@JsonProperty("name") String name, @JsonProperty("requestId") String requestId,
            @JsonProperty("signedFileHash") String signedFileHash, @JsonProperty("caseId") String caseId, @JsonProperty("temporalFileHashId") String temporalFileHashId,
            @JsonProperty("taskName") String taskName) {
        
        this.name = name;
        this.requestId = requestId;
        this.signedFileHash = signedFileHash;
        this.caseId = caseId;
        this.temporalFileHashId = temporalFileHashId;
        this.taskName = taskName;
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getName() {
        return name;
    }


    public String getRequestId() {
        return requestId;
    }


    public String getSignedFileHash() {
        return signedFileHash;
    }


    public String getCaseId() {
        return caseId;
    }


    public String getTemporalFileHashId() {
        return temporalFileHashId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerRol() {
        return ownerRol;
    }

    public void setOwnerRol(String ownerRol) {
        this.ownerRol = ownerRol;
    }

}