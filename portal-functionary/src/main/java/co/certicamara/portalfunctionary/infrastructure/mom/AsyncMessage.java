package co.certicamara.portalfunctionary.infrastructure.mom;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * A class to model the request Settings of the system
 *
 */
public class AsyncMessage {

    /**
     * Number of retries if the consumer transaction 
     */
    
	public final String token;
	
	public final int maxRetryAttempts;

    public int retryAttemptsCount;

    public final String jsonMessageContent;

    @JsonCreator
    public AsyncMessage(@JsonProperty("token") String token, @JsonProperty("maxRetryAttempts") int maxRetryAttempts , @JsonProperty("retryAttemptsCount") int retryAttemptsCount, @JsonProperty("jsonMessageContent") String jsonMessageContent){
       
    	this.token = token;
    	this.maxRetryAttempts = maxRetryAttempts;
        this.retryAttemptsCount = retryAttemptsCount;
        this.jsonMessageContent = jsonMessageContent;
    }

    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public int getRetryAttemptsCount() {
        return retryAttemptsCount;
    }

    public String getJsonMessageContent() {
        return jsonMessageContent;
    }

    public void addAttemptToCount(){
        retryAttemptsCount+=1;
    }

    public void refreshAttemptCount(){
        retryAttemptsCount=0;
    }

	public String getToken() {
        return token;
    }
}
