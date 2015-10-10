package co.certicamara.portalfunctionary.infrastructure.client.rs;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.certicamara.portalfunctionary.api.representations.CreateSignedDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateCitizenDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.CreateDocumentDTO;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.UpdateDocuments;
import co.certicamara.portalfunctionary.infrastructure.exceptions.BusinessException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.ExceptionType;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

/*
 * Represents a client to talk to document-manager 
 */
public class DocumentManagerClient {
	
	////////////////////////
	// Attributes
	////////////////////////

	/**
	 * Logger for exception messages 
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(DocumentManagerClient.class);

	/**
	 * service url to point to
	 */
	private String serviceUrl;

	private final ObjectMapper objectMapper;
	
	////////////////////////
	// Constructor
	////////////////////////

	public DocumentManagerClient(String serviceUrl) {

		this.serviceUrl = serviceUrl;

		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JSR310Module());
	}
	
	////////////////////////
	// Public Methods
	////////////////////////

	/**
	 * Get the settings for a specific tenant.
	 * 
	 * @param idTenant The tenant ID.
	 * @return the setting of the tenant with the given ID.
	 */
	public Either<IException, Boolean> updateDocumentListCaseId(TokenDTO token, String requestId, long caseId) {

		Either<IException, Boolean> either = null;

		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPut httpPut = new HttpPut(serviceUrl +"/v2/signed-documents");
		httpPut.setHeader("Authorization", token.getTokenValue());

		CloseableHttpResponse response = null;

		try {
		    
		    UpdateDocuments updateDocuments = new UpdateDocuments(caseId + "", requestId);

			String updateDocumentCaseIdJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(updateDocuments, JsonNode.class));
			StringEntity updateDocumentCaseIdString = new StringEntity(updateDocumentCaseIdJson, ContentType.APPLICATION_JSON);
			httpPut.setEntity(updateDocumentCaseIdString);

			response = httpClient.execute(httpPut);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == 200) {

				either = Either.right(Boolean.TRUE);

			} else {
				
				String responseEntityString = EntityUtils.toString(response.getEntity());
				JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
				IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
			}

		} catch (IOException e) {

			LOGGER.error("updateDocumentListCaseId", e);
			IException exception = new TechnicalException(e.getMessage());
			either = Either.left(exception);

		} finally {

			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}

		return either;
	}

	/**
	 * Creates a document in document manager
	 * @param signedDocumentDto
	 * @return
	 */
	public Either<IException, Boolean> createSignedDocument(TokenDTO token, CreateSignedDocumentDTO signedDocumentDto) {
		
		Either<IException, Boolean> either = null;
		HttpPost httpPost = new HttpPost(serviceUrl + "/v2/signed-documents/client-signed");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		httpPost.setHeader("Authorization", token.getTokenValue());
		CloseableHttpResponse response = null;
		
		try {

			ObjectMapper objectMapper = new ObjectMapper();
			String documentJson = objectMapper.writeValueAsString(signedDocumentDto);
			StringEntity stringEntity = new StringEntity(documentJson);
			
			stringEntity.setContentType("application/json");
			
			httpPost.setEntity(stringEntity);

			response = httpClient.execute(httpPost);

			int statusCode = response.getStatusLine().getStatusCode();
			
			HttpEntity responseEntity = response.getEntity();
			String responseEntityString = EntityUtils.toString(responseEntity);

			if (statusCode == 201) {
				
				either = Either.right(true);

			} else {

				JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
				IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
			}

		} catch (IOException e) {

			LOGGER.error("createDocument", e);
			IException exception = new TechnicalException(e.getMessage());
			either = Either.left(exception);

		} finally {

			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}

		return either;

	}
	
   /**
     * Creates a document in document manager
     * @param signedDocumentDto
     * @return
     */
    public Either<IException, Boolean> createProcessDocument(TokenDTO token, String caseId, String documentName, InputStream file) {
        
        Either<IException, Boolean> either = null;
        HttpPost httpPost = new HttpPost(serviceUrl + "/v2/process/" + caseId + "/" + documentName + "/file");
        httpPost.setHeader("Authorization", token.getTokenValue());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        
        try {
            
            byte[] bytes = IOUtils.toByteArray(file);

            ByteArrayEntity byteArrayEntity = new ByteArrayEntity(bytes, ContentType.APPLICATION_OCTET_STREAM);
            
            httpPost.setEntity(byteArrayEntity);

            response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            
            HttpEntity responseEntity = response.getEntity();
            String responseEntityString = EntityUtils.toString(responseEntity);

            if (statusCode == 201) {
                
                either = Either.right(true);

            } else {

                JsonNode jsonException = objectMapper.readValue(responseEntityString, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                either = Either.left(exception);
            }

        } catch (IOException e) {

            LOGGER.error("createProcessDocument", e);
            IException exception = new TechnicalException(e.getMessage());
            either = Either.left(exception);

        } finally {

            HttpClientUtils.closeQuietly(response);
            HttpClientUtils.closeQuietly(httpClient);
        }

        return either;

    }
    
    public Either<IException, Boolean> createSignedCitizenDocuments(TokenDTO token, List<CreateCitizenDocumentDTO> documentList) {

        Either<IException, Boolean> rejectionDocumentWasCreatedEither = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        try {

            String uri = serviceUrl + "/v2/signed-documents/citizen-documents";
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Authorization", token.getTokenValue());
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

            String documentJSON = objectMapper.writeValueAsString(documentList);
            StringEntity documentStringEntity = new StringEntity(documentJSON, ContentType.APPLICATION_JSON);
            postRequest.setEntity(documentStringEntity);

            httpResponse = httpClient.execute(postRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 201) {
                //
                rejectionDocumentWasCreatedEither = Either.right(true);

            } else {

                HttpEntity entity = httpResponse.getEntity();
                String exceptionAsJSON = EntityUtils.toString(entity, Charsets.UTF_8);
                
                JsonNode jsonException = objectMapper.readValue(exceptionAsJSON, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                rejectionDocumentWasCreatedEither = Either.left(exception);
            }

        } catch (NoHttpResponseException e) {

            LOGGER.error("DocumentManagerClient :: createSignedCitizenDocuments", e);
            IException technicalException = new TechnicalException(e.getMessage());
            rejectionDocumentWasCreatedEither = Either.left(technicalException);

        }  catch (HttpHostConnectException e) {

            LOGGER.error("DocumentManagerClient :: createSignedCitizenDocuments", e);
            IException technicalException = new TechnicalException(e.getMessage());
            rejectionDocumentWasCreatedEither = Either.left(technicalException);

        } catch (Exception e) {

            LOGGER.error("DocumentManagerClient :: createSignedCitizenDocuments", e);
            IException businessException = new BusinessException(e.getMessage());
            rejectionDocumentWasCreatedEither = Either.left(businessException);

        } finally {

            HttpClientUtils.closeQuietly(httpResponse);
            HttpClientUtils.closeQuietly(httpClient);
        }

        return rejectionDocumentWasCreatedEither;
    }
    
    public Either<IException, Boolean> createDocuments(TokenDTO token, List<CreateDocumentDTO> documentList) {

        Either<IException, Boolean> rejectionDocumentWasCreatedEither = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse httpResponse = null;

        try {

            String uri = serviceUrl + "/v2/documents";
            HttpPost postRequest = new HttpPost(uri);
            postRequest.setHeader("Authorization", token.getTokenValue());
            postRequest.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);

            String documentJSON = objectMapper.writeValueAsString(documentList);
            StringEntity documentStringEntity = new StringEntity(documentJSON, ContentType.APPLICATION_JSON);
            postRequest.setEntity(documentStringEntity);

            httpResponse = httpClient.execute(postRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            if (statusCode == 201) {
                //
                rejectionDocumentWasCreatedEither = Either.right(true);

            } else {

                HttpEntity entity = httpResponse.getEntity();
                String exceptionAsJSON = EntityUtils.toString(entity, Charsets.UTF_8);
                
                JsonNode jsonException = objectMapper.readValue(exceptionAsJSON, JsonNode.class);
                IException exception = getIExceptionFromJSON(objectMapper, jsonException);
                rejectionDocumentWasCreatedEither = Either.left(exception);
            }

        } catch (NoHttpResponseException e) {

            LOGGER.error("DocumentManagerClient :: createDocuments", e);
            IException technicalException = new TechnicalException(e.getMessage());
            rejectionDocumentWasCreatedEither = Either.left(technicalException);

        }  catch (HttpHostConnectException e) {

            LOGGER.error("DocumentManagerClient :: createDocuments", e);
            IException technicalException = new TechnicalException(e.getMessage());
            rejectionDocumentWasCreatedEither = Either.left(technicalException);

        } catch (Exception e) {

            LOGGER.error("DocumentManagerClient :: createDocuments", e);
            IException businessException = new BusinessException(e.getMessage());
            rejectionDocumentWasCreatedEither = Either.left(businessException);

        } finally {

            HttpClientUtils.closeQuietly(httpResponse);
            HttpClientUtils.closeQuietly(httpClient);
        }

        return rejectionDocumentWasCreatedEither;
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
