package gp.e3.autheo.infrastructure.clients.rs;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionType;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.infrastructure.cache.SystemSettingsCache;
import gp.e3.autheo.infrastructure.clients.entities.SystemSettings;

import java.io.IOException;

import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Represents a client to talk to customer-settings 
 */
public class CustomerSettingsClient {

    /**
     * Logger for exception messages 
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerSettingsClient.class);

    /**
     * service url to point to
     */
    private String serviceUrl;

    /**
     * The system settings cache
     */
    private SystemSettingsCache systemSettingsCache;

    ////////////////////////
    // Constructor
    ////////////////////////

    public CustomerSettingsClient(String serviceUrl, SystemSettingsCache systemSettingsCache) {

        this.serviceUrl = serviceUrl;
        this.systemSettingsCache = systemSettingsCache;
    }


    /**
     *  Get the system settings
     * @return
     */
    public Either<IException, SystemSettings> getSystemSettings() {

        Either<IException,SystemSettings> eitherCache  = systemSettingsCache.getSystemSettings();
        Either<IException, Boolean> eitherSave;

        if(eitherCache.isRight() && eitherCache.right().value() == null) {

            HttpGet httpget = new HttpGet(serviceUrl +"/systemSettings");
            httpget.addHeader("Authorization", "ANONYMOUS");

            try (CloseableHttpClient httpclient = HttpClients.createDefault();CloseableHttpResponse response = httpclient.execute(httpget)){

                HttpEntity responseEntity = response.getEntity();

                ObjectMapper objectMapper = new ObjectMapper();

                int statusCode = response.getStatusLine().getStatusCode();

                String responseEntityString = EntityUtils.toString(responseEntity);

                System.out.println("statusCode: " + statusCode);

                if(statusCode == Response.Status.OK.getStatusCode()) {

                    SystemSettings systemSettings = objectMapper.readValue(responseEntityString, SystemSettings.class);
                    eitherCache = Either.right(systemSettings);
                    eitherSave = systemSettingsCache.addSystemSettings(systemSettings);

                    if(eitherSave.isLeft()) {

                        LOGGER.error("CustomerSettingsClient :: getSystemSettings", eitherSave.left().value());
                    }

                } else {

                    JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                    IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                    eitherCache = Either.left(exception);
                }

            } catch (Exception e) {

                LOGGER.error("CustomerSettingsClient :: getSystemSettings", e);
                TechnicalException technicalException = new TechnicalException(e.getMessage());
                eitherCache = Either.left(technicalException);
            } 
        }
        return eitherCache;
    }

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
