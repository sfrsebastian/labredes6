package co.certicamara.portalfunctionary.infrastructure.client.rs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * A class to model the tenant Settings of the system
 *
 */
public class StartProcessInfo {

    //////////////////////////////////////
    // Attributes
    ///////////////////////////////////////

    private final String processKey;

    private final String assignee;


    //////////////////////////////////////
    // Constructor
    ///////////////////////////////////////

    @JsonCreator
    public StartProcessInfo(@JsonProperty("processKey")String processKey, @JsonProperty("assignee")String assignee){
        this.processKey = processKey;
        this.assignee = assignee;
    }
    //////////////////////////////////////
    // Getters
    ///////////////////////////////////////



    public String getProcessKey() {
        return processKey;
    }


    public String getAssignee() {
        return assignee;
    }




}
