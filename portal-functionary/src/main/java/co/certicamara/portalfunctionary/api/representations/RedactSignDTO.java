package co.certicamara.portalfunctionary.api.representations;

import java.util.List;

import co.certicamara.portalfunctionary.infrastructure.mom.dtos.DocumentInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class RedactSignDTO {
    
    ////////////////////////
    // Attributes
    ////////////////////////

    private final CreateSignedDocumentDTO signedDocument;

    private final JsonNode formValues;
    
    private final List<DocumentInfo> documentsList;
    
    private final boolean signDocuments;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    public RedactSignDTO(@JsonProperty("signedDocument") CreateSignedDocumentDTO signedDocument, @JsonProperty("formValues") JsonNode formValues,
            @JsonProperty("documentList") List<DocumentInfo> documentsList, @JsonProperty("signDocuments") boolean signDocuments) {
        
        this.signedDocument = signedDocument;
        this.formValues = formValues;
        this.documentsList = documentsList;
        this.signDocuments = signDocuments;
        
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public CreateSignedDocumentDTO getSignedDocument() {
        return signedDocument;
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

}
