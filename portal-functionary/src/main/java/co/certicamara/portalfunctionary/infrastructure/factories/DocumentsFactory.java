package co.certicamara.portalfunctionary.infrastructure.factories;

import java.util.ArrayList;
import java.util.List;

import co.certicamara.portalfunctionary.api.representations.SupportedFileTypes;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateCitizenDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.DocumentInfo;
import co.com.certicamara.sign.xml.xades.xades4j.utils.StringUtils;

public class DocumentsFactory {

    ////////////////////////
    // Attributes
    ////////////////////////

    ////////////////////////
    // Constructor
    ////////////////////////

    ////////////////////////
    // Public Methods
    ////////////////////////
    
    public static List<CreateCitizenDocumentDTO> createCitizenDocumentDTOList(List<DocumentInfo> documentInfoList, String requestId, String ownerName, String ownerRole) {

        List<CreateCitizenDocumentDTO> documentDtoList = new ArrayList<CreateCitizenDocumentDTO>();

        for (DocumentInfo documentInfo : documentInfoList) {

            String documentId = documentInfo.getDocumentId();
            if(StringUtils.allNullOrEmptyStrings(documentId)) {

                String documentName = documentInfo.getDocumentName();
                String temporalId = documentInfo.getTemporalId();
                SupportedFileTypes fileType= documentInfo.getFileType();
                String documentType = documentInfo.getDocumentType();

                CreateCitizenDocumentDTO documentDTO = new CreateCitizenDocumentDTO(documentName, requestId, "", temporalId, fileType, ownerName, ownerRole, documentType);

                documentDtoList.add(documentDTO);

            }

        }

        return documentDtoList; 
    }
    
    public static List<CreateDocumentDTO> createDocumentDTOList(List<DocumentInfo> documentInfoList, String requestId, String ownerName, String ownerRole) {

        List<CreateDocumentDTO> documentDtoList = new ArrayList<CreateDocumentDTO>();

        for (DocumentInfo documentInfo : documentInfoList) {

            String documentId = documentInfo.getDocumentId();
            if(StringUtils.allNullOrEmptyStrings(documentId)) {

                String documentName = documentInfo.getDocumentName();
                String temporalId = documentInfo.getTemporalId();
                SupportedFileTypes fileType= documentInfo.getFileType();

                CreateDocumentDTO documentDTO = new CreateDocumentDTO(documentName, requestId, "", temporalId, fileType, ownerName, ownerRole, "NA");

                documentDtoList.add(documentDTO);

            }

        }

        return documentDtoList; 
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
