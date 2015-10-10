package co.certicamara.portalfunctionary.infrastructure.client.rs;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.HttpHeaders;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.certicamara.portalfunctionary.api.representations.QueryHistoricTaskListResponse;
import co.certicamara.portalfunctionary.api.representations.TaskDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CaseInfo;
import co.certicamara.portalfunctionary.infrastructure.exceptions.BusinessException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.ExceptionType;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.ApproveRequestDTO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

/*
 * Represents a client to talk to workflow-manager 
 */
public class WorkflowManagerClient {

    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * Logger for exception messages 
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(WorkflowManagerClient.class);

    /**
     * service url to point to
     */
    private String serviceUrl;

    ////////////////////////
    // Constructor
    ////////////////////////

    /**
     * Constructor method
     * @param serviceUrl service url to point to
     */
    public WorkflowManagerClient(String serviceUrl) {
        
        this.serviceUrl = serviceUrl;
        
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    /**
     * Attains the tasks of the given user
     * 
     * @param username The user name
     * @param alert indicates if the tasks are alerts or not
     * @return Returns an either that can contain a IException in case there's an error or the list of tasks
     */
    @SuppressWarnings("unchecked")
    public Either<IException, List<TaskDTO>> getUserTasks(String username, TokenDTO token) {

        Either<IException, List<TaskDTO>> either = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(serviceUrl + "/task?username=" + username );
        httpGet.addHeader("Authorization", token.getTokenValue());

        CloseableHttpResponse response = null;

        try {

            List<TaskDTO> taskDTO = null;

            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();

            String responseEntityString = EntityUtils.toString(responseEntity);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {

                taskDTO = objectMapper.readValue(responseEntityString, List.class);
                either = Either.right(taskDTO);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (ClientProtocolException e) {

            LOGGER.error("WorkflowManagerClient :: getTask", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } catch (IOException e) {

            LOGGER.error("WorkflowManagerClient :: getTask", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } finally {

            try {

                response.close();

            } catch (IOException e) {

                LOGGER.error("WorkflowManagerClient :: getTask", e);
                if(!either.isLeft() && !either.isRight()) {

                    TechnicalException exception = new TechnicalException(e.getMessage());
                    either = Either.left(exception);

                }
            }
        }

        return either;
    }
    
    /**
     * Attains the tasks alerts of the given user
     * 
     * @param username The user name
     * @param alert indicates if the tasks are alerts or not
     * @return Returns an either that can contain a IException in case there's an error or the list of tasks
     */
    @SuppressWarnings("unchecked")
    public Either<IException, List<TaskDTO>> getUserTasksAlerts(String username, TokenDTO token) {

        Either<IException, List<TaskDTO>> either = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(serviceUrl + "/task/alerts?username=" + username );
        httpGet.addHeader(HttpHeaders.AUTHORIZATION, token.getTokenValue());

        CloseableHttpResponse response = null;

        try {

            List<TaskDTO> taskDTO = null;

            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();

            String responseEntityString = EntityUtils.toString(responseEntity);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {

                taskDTO = objectMapper.readValue(responseEntityString, List.class);
                either = Either.right(taskDTO);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (ClientProtocolException e) {

            LOGGER.error("WorkflowManagerClient :: getTask", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } catch (IOException e) {

            LOGGER.error("WorkflowManagerClient :: getTask", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } finally {

            try {

                response.close();

            } catch (IOException e) {

                LOGGER.error("WorkflowManagerClient :: getTask", e);
                if(!either.isLeft() && !either.isRight()) {

                    TechnicalException exception = new TechnicalException(e.getMessage());
                    either = Either.left(exception);

                }
            }
        }

        return either;
    }


    /**
     * Send a complete post request to workflow manager.
     * @param formValues form values, it have userId.
     * @param taskId Id task to complete. 
     * @return Either include the confirmation of the post. 
     */
    public Either<IException, Boolean> completeTask(JsonNode formValues, String taskId, TokenDTO token) {

        Either<IException, Boolean> either = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(serviceUrl + "/task/complete?taskId=" + taskId);
        httpPost.addHeader("Authorization",token.getTokenValue());

        CloseableHttpResponse response = null;

        try {
            StringEntity stringEntity = new StringEntity(formValues.toString());

            stringEntity.setContentType("application/json");

            httpPost.setEntity(stringEntity);

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseEntityString = EntityUtils.toString(responseEntity);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {

                Boolean status = new Boolean(true);
                either = Either.right(status);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (ClientProtocolException e) {

            LOGGER.error("WorkflowManagerClient :: completeTask", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } catch (IOException e) {

            LOGGER.error("WorkflowManagerClient :: completeTask", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } finally {

            try {

                response.close();

            } catch (IOException e) {

                LOGGER.error("WorkflowManagerClient :: completeTask", e);
                if(!either.isLeft() && !either.isRight()) {

                    TechnicalException exception = new TechnicalException(e.getMessage());
                    either = Either.left(exception);

                }
            }
        }

        return either;
    }

    /**
     * Attains the historic tasks of the given user
     * 
     * @param requestParamsIterator The params to filter the search with
     * @param token the token representing the session of the user
     * @return Returns an either that can contain a IException in case there's an error or the list of tasks
     */
    public Either<IException, QueryHistoricTaskListResponse> getHistoricTasks( TokenDTO token) {

        Either<IException,QueryHistoricTaskListResponse> either = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());


        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = null;

        try {

            HttpGet httpGet = new HttpGet(serviceUrl + "/historic-tasks");
            httpGet.addHeader("Authorization", token.getTokenValue());
            
            QueryHistoricTaskListResponse tasksDTO = null;

            response = httpClient.execute(httpGet);
            HttpEntity responseEntity = response.getEntity();

            String responseEntityString = EntityUtils.toString(responseEntity);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
            	
                tasksDTO = objectMapper.readValue(responseEntityString, QueryHistoricTaskListResponse.class);
                either = Either.right(tasksDTO);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (ClientProtocolException e) {

            LOGGER.error("WorkflowManagerClient :: getHistoricTasks", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } catch (IOException e) {

            LOGGER.error("WorkflowManagerClient :: getHistoricTasks", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } finally {

            try {

                response.close();

            } catch (IOException e) {

                LOGGER.error("WorkflowManagerClient :: getHistoricTasks", e);
                if(!either.isLeft() && !either.isRight()) {

                    TechnicalException exception = new TechnicalException(e.getMessage());
                    either = Either.left(exception);

                }
            }
        }

        return either;
    }

    /**
     * Create a process instance for a Request
     * @param RequestApproval request info
     * @return
     */
    public Either<IException, CaseInfo> startProcessInstance(ApproveRequestDTO approval, TokenDTO token) {

        Either<IException, CaseInfo> either = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(serviceUrl +"/process-instances");
        httpPost.addHeader("Authorization", token.getTokenValue());

        CloseableHttpResponse response = null;

        StartProcessInfo processInfo = new StartProcessInfo(approval.getProcedure(), approval.getAssignedPerson());

        try {

            String startProcessJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(processInfo, JsonNode.class));

            StringEntity startProcessString = new StringEntity(startProcessJson);
            startProcessString.setContentType("application/json");

            httpPost.setEntity(startProcessString);

            CaseInfo caseInfo = null;

            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            String responseEntityString = EntityUtils.toString(responseEntity);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 201) {

                caseInfo = objectMapper.readValue(responseEntityString, CaseInfo.class);
                either = Either.right(caseInfo);
                
            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (ClientProtocolException e) {

            LOGGER.error("ClientProtocolException", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } catch (HttpHostConnectException e) {

            LOGGER.error("HttpHostConnectException", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } catch (IOException e) {

            LOGGER.error("IOException", e);
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } finally {
            
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpClient);
        }
        
        return either;
    }

    /**
     * Create a process instance for a Request
     * @param RequestApproval request info
     * @return
     */
    public Either<IException, CaseInfo> deleteProcessInstance(long processInstanceId) {

        Either<IException, CaseInfo> either = null;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        String deleteReason = "Approve_request_transacction_error";

        HttpDelete httDelete = new HttpDelete(serviceUrl +"/process-instances?processInstanceId="+processInstanceId+"&deleteReason="+deleteReason);

        CloseableHttpResponse response = null;


        try {

            CaseInfo caseInfo = null;

            response = httpClient.execute(httDelete);
            HttpEntity responseEntity = response.getEntity();

            String responseEntityString = EntityUtils.toString(responseEntity);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                caseInfo = objectMapper.readValue(responseEntityString, CaseInfo.class);
                either = Either.right(caseInfo);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                System.out.println("jsonException: "+jsonException.textValue());
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (ClientProtocolException e) {
            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

            e.printStackTrace();
        } catch (IOException e) {

            TechnicalException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return either;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////
    
    private IException getIExceptionFromJSON(ObjectMapper objectMapper, JsonNode jsonException) throws IOException, JsonParseException,
            JsonMappingException {

        IException exception = null;
        JsonNode typeString = jsonException.get("type");
        
        if(typeString != null) {
        
            String asText = typeString.asText();
    
            if (asText.equals(ExceptionType.BUSINESS.getName())) {
    
                exception = objectMapper.readValue(jsonException.toString(), BusinessException.class);
    
            } else {
    
                exception = objectMapper.readValue(jsonException.toString(), TechnicalException.class);
            }
        
        } else {
            
            exception = new TechnicalException("There was an error in " + this.getClass().getName() + ": " + jsonException.toString());
            
        }

        return exception;
    }

}


