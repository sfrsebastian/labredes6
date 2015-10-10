package gp.e3.autheo.authentication.service.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreatePasswordToken {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private final String email;
    
    private final String organizationId;
    
    private final boolean isCitizen;
    
    ////////////////////////
    // Constructor
    ////////////////////////

    @JsonCreator
    public CreatePasswordToken(@JsonProperty("email") String email, @JsonProperty("organizationId") String organizationId, @JsonProperty("isCitizen") boolean isCitizen) {
        
        this.email = email;
        this.organizationId = organizationId;
        this.isCitizen = isCitizen;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getEmail() {
        return email;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    @JsonIgnore
    public boolean isCitizen() {
        return isCitizen;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
