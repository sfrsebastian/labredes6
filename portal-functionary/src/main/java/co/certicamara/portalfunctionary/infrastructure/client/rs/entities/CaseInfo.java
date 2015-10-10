package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to model a request of the system.
 * 
 * @author LeanFactory
 */
public class CaseInfo {

    //////////////////////////////
    // Attributes
    //////////////////////////////

    private final long id;


    private final String processDefKey;

    private final Instant startDate;

    private final String message;


    //////////////////////////////
    // Constructor
    //////////////////////////////

    @JsonCreator
    public CaseInfo(@JsonProperty("id") long id, @JsonProperty("processDefKey") String processDefKey, 
            @JsonProperty("startDate") Instant startDate, @JsonProperty("message") String message) {
        this.id = id;
        this.processDefKey = processDefKey;
        this.startDate = startDate;
        this.message = message;
    }



    //////////////////////////////
    // Getter
    //////////////////////////////


    public long getId() {
        return id;
    }


    public String getProcessDefKey() {
        return processDefKey;
    }


    public Instant getStartDate() {
        return startDate;
    }



    public String getMessage() {
        return message;
    }












}