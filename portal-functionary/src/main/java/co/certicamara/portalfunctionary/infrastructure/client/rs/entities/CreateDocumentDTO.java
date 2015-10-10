package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import co.certicamara.portalfunctionary.api.representations.SupportedFileTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateDocumentDTO {
    
    ////////////////////////
    // Attributes
    ////////////////////////

    private final String name;
    
    private final String requestId;

    private final String caseId;
    
    private final String temporalFileId;
    
    private final SupportedFileTypes fileType;
    
    private final String ownerName;
    
    private final String ownerRol;
    
    private final String documentId;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    @JsonCreator
    public CreateDocumentDTO(@JsonProperty("name") String name, @JsonProperty("requestId") String requestId, @JsonProperty("caseId") String caseId,
            @JsonProperty("temporalFileId") String temporalFileId, @JsonProperty("fileType")  SupportedFileTypes fileType,
            @JsonProperty("ownerName") String ownerName, @JsonProperty("ownerRol") String ownerRol, @JsonProperty("documentId") String documentId) {
        
        this.name = name;
        this.requestId = requestId;
        this.caseId = caseId;
        this.temporalFileId = temporalFileId;
        this.fileType = fileType;
        this.ownerName = ownerName;
        this.ownerRol = ownerRol;
        this.documentId = documentId;
        
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

    public String getCaseId() {
        return caseId;
    }

    public String getTemporalFileId() {
        return temporalFileId;
    }

    public SupportedFileTypes getFileType() {
        return fileType;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerRol() {
        return ownerRol;
    }
    
    public String getDocumentId() {
        return documentId;
    }

}