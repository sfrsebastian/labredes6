package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * A class to model the request Settings of the system
 *
 */
public class RequestSettings {

    /**
     * Time in minutes to set a request like an alert
     */
    public final int alertTime;

    @JsonCreator
    public RequestSettings(@JsonProperty("alertTime") int alertTime){
        this.alertTime = alertTime;
    }

    /**
     * Getter
     * @return
     */
    public int getAlertTime() {
        return alertTime;
    }

}
