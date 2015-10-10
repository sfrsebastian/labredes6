package co.certicamara.portalfunctionary.infrastructure.mom.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestEventDTO {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private LocalDateTime dateCreated;
    
    private String username;
    
    private String description;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    @JsonCreator
    public RequestEventDTO(@JsonProperty("dateCreated") LocalDateTime dateCreated, @JsonProperty("username") String username, @JsonProperty("description") String description) {
        
        this.dateCreated = dateCreated;
        this.username = username;
        this.description = description;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
