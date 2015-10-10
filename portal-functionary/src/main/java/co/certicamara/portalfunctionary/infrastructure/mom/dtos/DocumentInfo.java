/**
 * 
 */
package co.certicamara.portalfunctionary.infrastructure.mom.dtos;

import co.certicamara.portalfunctionary.api.representations.SupportedFileTypes;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lean Factory
 *
 */
public class DocumentInfo {


    //////////////////////////
    // Attributes
    //////////////////////////

    private final SupportedFileTypes fileType;

    private final String documentName;

    private final String temporalId;
    
    private final String documentId;

    private final String documentType;

    //////////////////////////
    // Constructor
    //////////////////////////


    @JsonCreator
    public DocumentInfo(@JsonProperty("fileType") SupportedFileTypes fileType, @JsonProperty("documentName") String documentName, 
            @JsonProperty("temporalId") String temporalId, @JsonProperty("documentId") String documentId, @JsonProperty("documentType") String documentType) {

        this.fileType = fileType;
        this.documentName = documentName;
        this.temporalId = temporalId;
        this.documentId = documentId;
        this.documentType = documentType;

    }


    //////////////////////////
    // Getters
    //////////////////////////


    public SupportedFileTypes getFileType() {
        return fileType;
    }


    public String getDocumentName() {
        return documentName;
    }


    public String getTemporalId() {
        return temporalId;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentType() {
        return documentType;
    }
    
}
