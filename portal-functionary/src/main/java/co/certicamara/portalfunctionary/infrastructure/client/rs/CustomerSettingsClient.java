package co.certicamara.portalfunctionary.infrastructure.client.rs;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.AsyncTxParams;
import co.certicamara.portalfunctionary.infrastructure.exceptions.BusinessException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.ExceptionType;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import fj.data.Either;

/**
 * Represents a client to talk to customer-settings 
 */
public class CustomerSettingsClient {

    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * Logger for exception messages 
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerSettingsClient.class);

    /**
     * service url to point to
     */
    private String serviceUrl;

    ////////////////////////
    // Constructor
    ////////////////////////

    public CustomerSettingsClient(String serviceUrl) {

        this.serviceUrl = serviceUrl;
    }

    ////////////////////////
    // Public Methods
    ////////////////////////


    /**
     * gets the settings for a async tx
     * @return Returns an either that can contain a IException in case there's an error or the async tx settings
     */
    public Either<IException, AsyncTxParams> getAsyncTxSettings() {

        Either<IException, AsyncTxParams > either = null;

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(serviceUrl +"/settings/async-tx-params");
        CloseableHttpResponse response = null;

        try {

            response = httpclient.execute(httpget);
            HttpEntity responseEntity = response.getEntity();
            ObjectMapper objectMapper = new ObjectMapper();

            int statusCode = response.getStatusLine().getStatusCode();

            String responseEntityString = EntityUtils.toString(responseEntity);

            if(statusCode == Response.Status.OK.getStatusCode()) {

                AsyncTxParams asyncTxParams = objectMapper.readValue(responseEntityString, AsyncTxParams.class);
                either = Either.right(asyncTxParams);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);

            }

        } catch (Exception e) {

            LOGGER.error("CustomerSettingsClient :: getTenantSettings", e);
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            either = Either.left(technicalException);

        } finally {
            
            IOUtils.closeQuietly(response);
            IOUtils.closeQuietly(httpclient);
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
