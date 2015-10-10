package co.certicamara.portalfunctionary.domain.business;

import java.util.List;

import co.certicamara.portalfunctionary.api.representations.ApprovalResponse;
import co.certicamara.portalfunctionary.domain.entities.RequestStatus;
import co.certicamara.portalfunctionary.infrastructure.client.rs.DocumentManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.WorkflowManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CaseInfo;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateCitizenDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.factories.DocumentsFactory;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.ApproveRequestDTO;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.DocumentInfo;
import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

/**
 * Implementation of the Case business
 * @author LeanFactory
 *
 */
public class CaseBusiness {

    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * The client used to communicate to the workflow-manager
     */
    private final WorkflowManagerClient workflowManagerClient;

    /**
     * The client used to communicate to the document-manager
     */
    private final DocumentManagerClient documentManagerClient;


    ////////////////////////
    // Constructor
    ////////////////////////

    /**
     * Constructor method
     * @param workflowManagerClient The client used to communicate to the workflow-manager
     */
    public CaseBusiness(WorkflowManagerClient workflowManagerClient, DocumentManagerClient documentManagerClient) {

        this.workflowManagerClient = workflowManagerClient;
        this.documentManagerClient = documentManagerClient;

    }

    ////////////////////////
    // Public Methods
    ////////////////////////


    /**
     * Attains the list tasks of the given user
     * 
     * @param username The username used to find the tasks
     * @return Returns an either that can contain a IException in case there's an error or the list of tasks of the given user
     */
    public Either<IException, ApprovalResponse> createNewCase( ApproveRequestDTO approval, TokenDTO token ) {

        Either<IException, ApprovalResponse> eitherApprove = null;

        Either<IException, Boolean> signedCitizenDocumentsCreatedEither = Either.right(true);
        List<DocumentInfo> documentsList = approval.getDocumentsList();
        if(documentsList != null && documentsList.size() > 0) {
            
            if(approval.isSignDocuments()) {
                
                List<CreateCitizenDocumentDTO> documentDTOList = DocumentsFactory.createCitizenDocumentDTOList(documentsList, approval.getId(), token.getUsername(), token.getUserRole());
                signedCitizenDocumentsCreatedEither = documentManagerClient.createSignedCitizenDocuments(token, documentDTOList);
                
            } else {
                
                List<CreateDocumentDTO> documentDTOList = DocumentsFactory.createDocumentDTOList(documentsList, approval.getId(), token.getUsername(), token.getUserRole());
                signedCitizenDocumentsCreatedEither = documentManagerClient.createDocuments(token, documentDTOList);
            }
            
        }
        
        if(signedCitizenDocumentsCreatedEither.isRight()) {

            // Crear instancia de proceso
            Either<IException, CaseInfo> eitherProcessInstance = workflowManagerClient.startProcessInstance(approval, token);
    
            if (eitherProcessInstance.isRight()) {
    
                CaseInfo caseInfo = eitherProcessInstance.right().value();
                
                // Actualizar Documento con el id del caso
                Either<IException, Boolean> eitherUpdateDocumentList = documentManagerClient.updateDocumentListCaseId(token, approval.getId(), caseInfo.getId());
    
                if (eitherUpdateDocumentList.isRight()) {
    
                    ApprovalResponse approvalResponse = new ApprovalResponse(approval.getId(), caseInfo, RequestStatus.APPROVED, approval.getAssignedPerson(), approval.getObservations(), "PQR Created",
                            approval.getRevisorName());
                    eitherApprove = Either.right(approvalResponse);
    
                } else {
    
                    ApproveRequestCompensation.documentUpdateByCaseIdFailCompensate(caseInfo.getId(), workflowManagerClient);
                    eitherApprove = Either.left(eitherUpdateDocumentList.left().value());
                }
    
            } else {
    
                System.out.println("Error al crear el proceso");
                eitherApprove = Either.left(eitherProcessInstance.left().value());
            }

        } else {
            
            eitherApprove = Either.left(signedCitizenDocumentsCreatedEither.left().value());
            
        }

        return eitherApprove;
    }

}