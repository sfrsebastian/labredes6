package co.certicamara.portalfunctionary.domain.business;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

import co.certicamara.portalfunctionary.api.representations.CompleteTaskDTO;
import co.certicamara.portalfunctionary.api.representations.CreateSignedDocumentDTO;
import co.certicamara.portalfunctionary.api.representations.RedactAndCompleteTaskDTO;
import co.certicamara.portalfunctionary.api.representations.TaskDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.CustomerSettingsClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.DocumentManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.WorkflowManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.AsyncTxParams;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateCitizenDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryAddEventToRequestPublishQueuesInfo;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryRabbitMQConfig;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.factories.DocumentsFactory;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.CreateRequestEventDTO;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.DocumentInfo;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.RequestEventDTO;
import co.certicamara.portalfunctionary.infrastructure.mom.senders.AsyncMessageSender;

import com.fasterxml.jackson.databind.JsonNode;

import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

/**
 * Implementation of the Task business
 * @author LeanFactory
 *
 */
public class TaskBusiness {
	
	////////////////////////
	// Attributes
	////////////////////////
	
    public static final int DEFAULT_MAX_RETRY = 5;
    
	/**
	 * The client used to communicate with workflow-manager
	 */
	private final WorkflowManagerClient workflowManagerClient;
	
	/**
	 * The client used to communicate with document-manager
	 */
	private final DocumentManagerClient documentManagerClient;
	
	/**
	 * The client used to communicate with cutomer-settings
	 */
	private final CustomerSettingsClient customerSettingsClient;
	
	/**
     * This object contains the queue info to publish messages for adding events to a request for reports.
     */
	private final PortalFunctionaryAddEventToRequestPublishQueuesInfo addEventToRequestReportPublishQueuesInfo;
	
	/**
     * This object contains the message broker info.
     */
    private final AsyncMessageSender asyncMessageSender;
	
    /**
     * This object sends messages to queues.
     */
    private final PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig;
    
	////////////////////////
	// Constructor
	////////////////////////
	
	/**
	 * Constructor method
	 * @param workflowManagerClient The client used to communicate with workflow-manager
	 * @param documentManagerClient The client used to communicate with document-manager 
	 * @param customerSettingsClient The client used to communicate with cutomer-settings
	 * @param addEventToRequestReportPublishQueuesInfo contains the queue info to publish messages for adding events to a request for reports
	 * @param asyncMessageSender contains the message broker info
	 * @param portalFunctionaryRabbitMQConfig this object sends messages to queues
	 */
	public TaskBusiness(WorkflowManagerClient workflowManagerClient, DocumentManagerClient documentManagerClient, 
	        CustomerSettingsClient customerSettingsClient, PortalFunctionaryAddEventToRequestPublishQueuesInfo addEventToRequestReportPublishQueuesInfo,
	        PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig) {
		
		this.workflowManagerClient = workflowManagerClient;
		this.documentManagerClient = documentManagerClient;
		this.customerSettingsClient = customerSettingsClient; 
		this.addEventToRequestReportPublishQueuesInfo = addEventToRequestReportPublishQueuesInfo;
		this.portalFunctionaryRabbitMQConfig = portalFunctionaryRabbitMQConfig;
		
		asyncMessageSender = new AsyncMessageSender();
		
	}
	
	
	
	////////////////////////
	// Public Methods
	////////////////////////

	/**
	 * Attains the list tasks of the given user
	 * 
	 * @param username The username used to find the tasks
	 * @param alerts Indicates if alerts are to be brought or not
	 * @return Returns an either that can contain a IException in case there's an error or the list of tasks of the given user
	 */
	public Either<IException, List<TaskDTO>> getUserTasks(String username, TokenDTO  token) {
		
		return workflowManagerClient.getUserTasks(username, token);
	}
	
	/**
	 * Attains the list tasks of the given user
	 * 
	 * @param username The username used to find the tasks
	 * @param alerts Indicates if alerts are to be brought or not
	 * @return Returns an either that can contain a IException in case there's an error or the list of tasks of the given user
	 */
	public Either<IException, List<TaskDTO>> getUserTasksAlerts(String username, TokenDTO token) {
		
		return workflowManagerClient.getUserTasksAlerts(username, token);
	}
	
	/**
	 * Attains the response of complete a task
	 * 
	 * @param token the token that represents the users session
	 * @param completeTaskDTO the information needed to complete the task.
	 * @param taskId Id task to complete. 
	 * @return Returns the either value from WorkFlowManagerClient.
	 */
	public Either<IException, Boolean> completeTask(TokenDTO token, CompleteTaskDTO completeTaskDTO, String taskId) {
	    
	    Either<IException, Boolean> taskCompletedEither = null;
	    
        Either<IException, Boolean> signedCitizenDocumentsCreatedEither = Either.right(true);
        if(completeTaskDTO.getDocumentsList() != null && completeTaskDTO.getDocumentsList().size() > 0) {
            
            if(completeTaskDTO.isSignDocuments()) {
                
                List<CreateCitizenDocumentDTO> documentDTOList = 
                        DocumentsFactory.createCitizenDocumentDTOList(completeTaskDTO.getDocumentsList(), completeTaskDTO.getRequestId(), token.getUsername(), token.getUserRole());
                signedCitizenDocumentsCreatedEither = documentManagerClient.createSignedCitizenDocuments(token, documentDTOList);
                
            } else {
                
                List<CreateDocumentDTO> documentDTOList = DocumentsFactory.createDocumentDTOList(completeTaskDTO.getDocumentsList(), completeTaskDTO.getRequestId(), token.getUsername(), token.getUserRole());
                signedCitizenDocumentsCreatedEither = documentManagerClient.createDocuments(token, documentDTOList);
            }
            
        }
        
        if(signedCitizenDocumentsCreatedEither.isRight()) {
	    
    	    taskCompletedEither = workflowManagerClient.completeTask(completeTaskDTO.getFormValues(), taskId, token);
    	 
    	    if(taskCompletedEither.isRight()) {
    	        
    	        
    	        Either<IException, Boolean> eitherAsync = null;
                Either<IException, AsyncTxParams> eitherCustomerSettings = customerSettingsClient.getAsyncTxSettings();
                
                LocalDateTime currentDateTime = LocalDateTime.now();
                CreateRequestEventDTO createRequestEventDTO = new CreateRequestEventDTO(completeTaskDTO.getRequestId(), taskId, new RequestEventDTO(currentDateTime, token.getUsername(), 
                        completeTaskDTO.getTaskName()));
    
                String workQueue = addEventToRequestReportPublishQueuesInfo.getWorkQueue();
                boolean workQueueDurable = addEventToRequestReportPublishQueuesInfo.isWorkQueueDurable();
                boolean workQueueExclusive = addEventToRequestReportPublishQueuesInfo.isWorkQueueExclusive();
                boolean workQueueAutoDelete = addEventToRequestReportPublishQueuesInfo.isWorkQueueAutoDelete();
                
                int maxRetryNumber = DEFAULT_MAX_RETRY;
                
                // Send Async message to Business -reports
                if (eitherCustomerSettings.isRight()) {
    
                    AsyncTxParams asyncTxSettings = eitherCustomerSettings.right().value();
                    maxRetryNumber = asyncTxSettings.getMaxRetryAttempts();
                }    
                 
                eitherAsync = asyncMessageSender.sendAsyncMessage(token, maxRetryNumber, createRequestEventDTO, workQueue, workQueueDurable, 
                            workQueueExclusive, workQueueAutoDelete, portalFunctionaryRabbitMQConfig);
    
                if(eitherAsync.isLeft()) {
                    
                    //TODO handle this properly
                    
                }
    	        
    	    }
	    
        } else {
            
            taskCompletedEither = Either.left(signedCitizenDocumentsCreatedEither.left().value());
            
        }
		
		return taskCompletedEither;
	}
	
	/**
	 * Signs a document and completes the task 
	 * @param token the token that represents the users session
	 * @param formValues
	 * @param documentList 
	 * @param document
	 * @return
	 */
	public Either<IException, Boolean> signAndCompleteTask(TokenDTO token, String taskId, JsonNode formValues, CreateSignedDocumentDTO signedDocumentDto, 
	            List<DocumentInfo> documentList, boolean signDocuments ) {
	    
		Either<IException,Boolean> eitherResult = null;
		
		signedDocumentDto.setOwnerName(token.getUsername());
		signedDocumentDto.setOwnerRol(token.getBusinessRole());
		Either<IException, Boolean> eitherDocument = documentManagerClient.createSignedDocument(token, signedDocumentDto);
		
		if(eitherDocument.isRight()) {
		    
            Either<IException, Boolean> signedCitizenDocumentsCreatedEither = Either.right(true);
            if(documentList != null && documentList.size() > 0) {
                
                if(signDocuments) {
                    
                    List<CreateCitizenDocumentDTO> documentDTOList = 
                            DocumentsFactory.createCitizenDocumentDTOList(documentList, signedDocumentDto.getRequestId(), token.getUsername(), token.getUserRole());
                    signedCitizenDocumentsCreatedEither = documentManagerClient.createSignedCitizenDocuments(token, documentDTOList);
                    
                } else {
                    
                    List<CreateDocumentDTO> documentDTOList = DocumentsFactory.createDocumentDTOList(documentList, signedDocumentDto.getRequestId(), token.getUsername(), token.getUserRole());
                    signedCitizenDocumentsCreatedEither = documentManagerClient.createDocuments(token, documentDTOList);
                }
                
            }
            
            if(signedCitizenDocumentsCreatedEither.isRight()) {
		    
    			Either<IException, Boolean> eitherWorkflow = workflowManagerClient.completeTask(formValues, taskId, token);
    			if (eitherWorkflow.isRight()) {
    			    
    				eitherResult = Either.right(true);	
    				
    				Either<IException, Boolean> eitherAsync = null;
    	            Either<IException, AsyncTxParams> eitherCustomerSettings = customerSettingsClient.getAsyncTxSettings();
    	            
    	            LocalDateTime currentDateTime = LocalDateTime.now();
    	            CreateRequestEventDTO createRequestEventDTO = new CreateRequestEventDTO(signedDocumentDto.getRequestId(), taskId, new RequestEventDTO(currentDateTime, token.getUsername(), 
    	                    signedDocumentDto.getTaskName()));
    
    	            String workQueue = addEventToRequestReportPublishQueuesInfo.getWorkQueue();
    	            boolean workQueueDurable = addEventToRequestReportPublishQueuesInfo.isWorkQueueDurable();
    	            boolean workQueueExclusive = addEventToRequestReportPublishQueuesInfo.isWorkQueueExclusive();
    	            boolean workQueueAutoDelete = addEventToRequestReportPublishQueuesInfo.isWorkQueueAutoDelete();
    	            
    	            
    	            int maxRetryNumber = DEFAULT_MAX_RETRY;
    	            // Send Async message to Business -reports
    	            if (eitherCustomerSettings.isRight()) {
    
    	                AsyncTxParams asyncTxSettings = eitherCustomerSettings.right().value();
    	                maxRetryNumber = asyncTxSettings.getMaxRetryAttempts();
    
    
    	            }
    	            
                	eitherAsync = asyncMessageSender.sendAsyncMessage(token, maxRetryNumber, createRequestEventDTO, workQueue, workQueueDurable, 
                			workQueueExclusive, workQueueAutoDelete, portalFunctionaryRabbitMQConfig);
                
                	if(eitherAsync.isLeft()) {
                    
                    //TODO handle this properly
    	                
    	            }
    				
    				
    			} else {
    			    
    				eitherResult = Either.left(eitherWorkflow.left().value());	
    				
    			}
    			
            }  else {
                
                eitherResult = Either.left(signedCitizenDocumentsCreatedEither.left().value());
                
            }
			
		} else {
		    
			eitherResult = Either.left(eitherDocument.left().value());
			
		}
		
		return eitherResult;
	}
	
	
   /**
     * Signs and document and complete the task 
     * @param token the token that represents the users session
     * @param formValues
     * @param document
     * @return
     */
    public Either<IException, Boolean> saveRedactedDocumentAndCompleteTask(TokenDTO token, RedactAndCompleteTaskDTO redactAndCompleteTaskDTO, InputStream file, String taskId ) {
        
        Either<IException,Boolean> eitherResult = null;
        
        
            Either<IException, Boolean> eitherDocument = documentManagerClient.createProcessDocument(token, redactAndCompleteTaskDTO.getCaseId(), redactAndCompleteTaskDTO.getCertiDocumentName(), file);
            
            if(eitherDocument.isRight()) {
                
                Either<IException, Boolean> signedCitizenDocumentsCreatedEither = Either.right(true);
                List<DocumentInfo> documentsList = redactAndCompleteTaskDTO.getDocumentsList();
                if(documentsList != null && documentsList.size() > 0) {
                    
                    if(redactAndCompleteTaskDTO.isSignDocuments()) {
                        
                        List<CreateCitizenDocumentDTO> documentDTOList = 
                                DocumentsFactory.createCitizenDocumentDTOList(documentsList, redactAndCompleteTaskDTO.getRequestId(), token.getUsername(), token.getUserRole());
                        signedCitizenDocumentsCreatedEither = documentManagerClient.createSignedCitizenDocuments(token, documentDTOList);
                        
                    } else {
                        
                        List<CreateDocumentDTO> documentDTOList = DocumentsFactory.createDocumentDTOList(documentsList, redactAndCompleteTaskDTO.getRequestId(), token.getUsername(), token.getUserRole());
                        signedCitizenDocumentsCreatedEither = documentManagerClient.createDocuments(token, documentDTOList);
                    }
                    
                    
                    
                }
                
                if(signedCitizenDocumentsCreatedEither.isRight()) {
                
                    Either<IException, Boolean> eitherWorkflow = workflowManagerClient.completeTask(redactAndCompleteTaskDTO.getFormValues(), taskId, token);
                    if (eitherWorkflow.isRight()) {
                        
                        eitherResult = Either.right(true);  
                        
                        Either<IException, Boolean> eitherAsync = null;
                        Either<IException, AsyncTxParams> eitherCustomerSettings = customerSettingsClient.getAsyncTxSettings();
                        
                        LocalDateTime currentDateTime = LocalDateTime.now();
                        CreateRequestEventDTO createRequestEventDTO = new CreateRequestEventDTO(redactAndCompleteTaskDTO.getRequestId(), taskId, new RequestEventDTO(currentDateTime, token.getUsername(), 
                                redactAndCompleteTaskDTO.getTaskName()));
        
                        String workQueue = addEventToRequestReportPublishQueuesInfo.getWorkQueue();
                        boolean workQueueDurable = addEventToRequestReportPublishQueuesInfo.isWorkQueueDurable();
                        boolean workQueueExclusive = addEventToRequestReportPublishQueuesInfo.isWorkQueueExclusive();
                        boolean workQueueAutoDelete = addEventToRequestReportPublishQueuesInfo.isWorkQueueAutoDelete();
                        
                        
                        int maxRetryNumber = DEFAULT_MAX_RETRY;
                        // Send Async message to Business -reports
                        if (eitherCustomerSettings.isRight()) {
        
                            AsyncTxParams asyncTxSettings = eitherCustomerSettings.right().value();
                            maxRetryNumber = asyncTxSettings.getMaxRetryAttempts();
        
                        }
                        
                        eitherAsync = asyncMessageSender.sendAsyncMessage(token, maxRetryNumber, createRequestEventDTO, workQueue, workQueueDurable, 
                                workQueueExclusive, workQueueAutoDelete, portalFunctionaryRabbitMQConfig);
                    
                        if(eitherAsync.isLeft()) {
                        
                        //TODO handle this properly
                            
                        }
                        
                        
                    } else {
                        
                        eitherResult = Either.left(eitherWorkflow.left().value());  
                        
                    }
                
                }  else {
                    
                    eitherResult = Either.left(signedCitizenDocumentsCreatedEither.left().value());
                    
                }
        
        } else {
            
            eitherResult = Either.left(eitherDocument.left().value());
            
        }
        
        try {
            
            file.close();
            
        } catch (IOException e) {
            //TODO handle this exception some how
            e.printStackTrace();
        }
        
        return eitherResult;
    }
	
}