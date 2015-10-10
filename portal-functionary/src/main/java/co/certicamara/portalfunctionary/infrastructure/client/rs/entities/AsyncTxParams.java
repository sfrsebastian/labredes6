/**
 * 
 */
package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lean Factory
 *
 */
public class AsyncTxParams {

    //////////////////////////
    // Attributes
    //////////////////////////

    private final int maxRetryAttempts;

    private final int retryDelayMs;


    //////////////////////////
    // Constructor
    //////////////////////////


    @JsonCreator
    public AsyncTxParams(@JsonProperty("maxRetryAttempts")int maxRetryAttempts, @JsonProperty("retryDelayMs")int retryDelayMs){
        this.maxRetryAttempts = maxRetryAttempts;
        this.retryDelayMs = retryDelayMs;
    }

    //////////////////////////
    // Getters
    //////////////////////////


    public int getMaxRetryAttempts() {
        return maxRetryAttempts;
    }

    public int getRetryDelayMs() {
        return retryDelayMs;
    }



}
