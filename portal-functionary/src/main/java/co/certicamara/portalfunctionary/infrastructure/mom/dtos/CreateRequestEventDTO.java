package co.certicamara.portalfunctionary.infrastructure.mom.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateRequestEventDTO {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private final String requestId;
    
    private final String taskId;
    
    private final RequestEventDTO requestEvent;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    @JsonCreator
    public CreateRequestEventDTO(@JsonProperty("requestId") String requestId, @JsonProperty("taskId") String taskId, @JsonProperty("requestEvent") RequestEventDTO requestEvent) {
        
        this.requestId = requestId;
        this.taskId = taskId;
        this.requestEvent = requestEvent;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getRequestId() {
        return requestId;
    }

    public String getTaskId() {
        return taskId;
    }

    public RequestEventDTO getRequestEvent() {
        return requestEvent;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
