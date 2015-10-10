package co.certicamara.portalfunctionary.api.resources;

import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

import java.io.InputStream;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import co.certicamara.portalfunctionary.api.representations.CompleteTaskDTO;
import co.certicamara.portalfunctionary.api.representations.RedactAndCompleteTaskDTO;
import co.certicamara.portalfunctionary.api.representations.RedactSignDTO;
import co.certicamara.portalfunctionary.api.representations.TaskDTO;
import co.certicamara.portalfunctionary.domain.business.TaskBusiness;
import co.certicamara.portalfunctionary.infrastructure.constants.MessagesCodes;
import co.certicamara.portalfunctionary.infrastructure.exceptions.BusinessException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.multipart.FormDataParam;

/**
 * Service that exposes task as resources
 * @author steven
 *
 */
@Path("/task")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TaskResource {
    
    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * The business object in charged of handling the task
     */
    private final TaskBusiness taskBusiness;
    
    private final MessagesCodes messagesCodes;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    /**
     * The task resource constructor
     * @param userBusiness the business object
     */
    public TaskResource(TaskBusiness taskBusiness) {
        
    	this.messagesCodes = new MessagesCodes();
        this.taskBusiness = taskBusiness;
        
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    /**
     * Attains the list tasks of the given user
     * 
     * @param username The username used to find the tasks
     * @param alerts Indicates if alerts are to be brought or not
     * @return Returns a response object containing the http status code. In case it is successful it returns the list of tasks of the given user, 
     * if not, it returns the error message.
     */
    @GET
    public Response getUserTasks(@Context TokenDTO token, @QueryParam("username") String username) {

        Response response = null;

        Either<IException, List<TaskDTO>> eitherResult = taskBusiness.getUserTasks(username, token);

        if (eitherResult.isLeft()) {

            IException exception = eitherResult.left().value();
            int statusCode = messagesCodes.getStatusCode(exception);
            response = Response.status(statusCode).entity(exception).build();

        } else {

            List<TaskDTO> userTaskDTOList = eitherResult.right().value();
            response = Response.status(200).entity(userTaskDTOList).build();

        }

        return response;
    }
    
    /**
     * Attains the list tasks alerts of the given user
     * 
     * @param username The username used to find the tasks
     * @param alerts Indicates if alerts are to be brought or not
     * @return Returns a response object containing the http status code. In case it is successful it returns the list of tasks of the given user, 
     * if not, it returns the error message.
     */
    @GET
    @Path("/alerts")
    public Response getUserTasksAlerts(@Context TokenDTO token, @QueryParam("username") String username) {

        Response response = null;

        Either<IException, List<TaskDTO>> eitherResult = taskBusiness.getUserTasksAlerts(username, token);

        if (eitherResult.isLeft()) {

            IException exception = eitherResult.left().value();
            int statusCode = messagesCodes.getStatusCode(exception);
            response = Response.status(statusCode).entity(exception).build();

        } else {

            List<TaskDTO> userTaskDTOList = eitherResult.right().value();
            response = Response.status(200).entity(userTaskDTOList).build();

        }

        return response;
    }

    /**
     * Send a complete post request to workflow manager.
     * 
     * @param token the token that represents the users session
     * @param completeTaskDTO the information needed to complete the task.
     * @param taskId Id task to complete. 
     * @return Response which includes the confirmation of the post. 
     */
    @POST
    @Path("/complete")
    public Response completeTask(@Context TokenDTO token, CompleteTaskDTO completeTaskDTO, @QueryParam("taskId") String taskId) {

        Response response = null;
        
        Either<IException, Boolean> eitherResult = taskBusiness.completeTask(token, completeTaskDTO, taskId);
        
        if (eitherResult.isLeft()) {
            
            IException exception = eitherResult.left().value();
            int statusCode = messagesCodes.getStatusCode(exception);
            response = Response.status(statusCode).entity(exception).build();
            
        } else {
            
            Boolean booleanResponse = eitherResult.right().value();
            response = Response.status(200).entity(booleanResponse.toString()).build();
            
        }
            
        return response;

    }

    /**
     * Signs a document and completes the task
     * @param formValues
     * @param document
     * @return
     */
    @POST
    @Path("/SignAndCompleteTypeTask/{taskId}")
    public Response signAndCompleteTask(@Context TokenDTO token, @PathParam("taskId") String taskId, RedactSignDTO taskDTO) {
        
        Response response = null;
        Either<IException, Boolean> eitherResult = 
                taskBusiness.signAndCompleteTask(token, taskId, taskDTO.getFormValues(), taskDTO.getSignedDocument(), taskDTO.getDocumentsList(), taskDTO.isSignDocuments());
        
        if (eitherResult.isLeft()) {
            
            IException exception = eitherResult.left().value();
            int statusCode = messagesCodes.getStatusCode(exception);
            response = Response.status(statusCode).entity(exception).build();
            
        } else {
            
            Boolean booleanResponse = eitherResult.right().value();
            response = Response.status(200).entity(booleanResponse.toString()).build();	
            
        }
        
        return response;
    }
    
    /**
     * Saves a redacted document and complete the task
     * @param uploadedInputStream the byte stream of the file to save
     * @param taskDetailsJson the details of the task in a json
     * @return Returns a response object containing the http status code. In case it is successful it returns a boolean, 
     * if not, it returns the error message.
     */
    @POST
    @Path("/RedactAndCompleteTypeTask/{taskId}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response saveRedactedDocumentAndCompleteTask(@Context TokenDTO token, @FormDataParam("file") InputStream uploadedInputStream, @FormDataParam("taskDetails") String taskDetailsJson,
            @PathParam("taskId") String taskId) {
        
        Response response = null;
        
        try {
        
            ObjectMapper objectMapper = new ObjectMapper();
            RedactAndCompleteTaskDTO redactAndCompleteTaskDTO  = objectMapper.readValue(taskDetailsJson, RedactAndCompleteTaskDTO.class);
            
            Either<IException, Boolean> eitherResult = taskBusiness.saveRedactedDocumentAndCompleteTask(token, redactAndCompleteTaskDTO, uploadedInputStream, taskId);
            
            if (eitherResult.isLeft()) {
                
                IException exception = eitherResult.left().value();
                int statusCode = messagesCodes.getStatusCode(exception);
                response = Response.status(statusCode).entity(exception).build();
                
            } else {
                
                Boolean booleanResponse = eitherResult.right().value();
                response = Response.status(200).entity(booleanResponse.toString()).build(); 
                
            }
        
        } catch(Exception e) {
            
            IException exception = new BusinessException("Invalid task details");
            response = Response.status(Response.Status.BAD_REQUEST).entity(exception).build();
            
        }
        
        return response;
    }

}