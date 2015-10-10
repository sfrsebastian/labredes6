package gp.e3.autheo.authentication.service.representation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordGivenPasswordToken {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private final String passwordToken;
    
    private final String hashedNewPassword;
    
    private final String organizationId;

    ////////////////////////
    // Constructor

    ////////////////////////
    
    @JsonCreator
    public ChangePasswordGivenPasswordToken(@JsonProperty("passwordToken") String passwordToken, @JsonProperty("hashedNewPassword") String hashedNewPassword, 
                        @JsonProperty("organizationId") String organizationId) {
        
        this.passwordToken = passwordToken;
        this.hashedNewPassword = hashedNewPassword;
        this.organizationId = organizationId;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getPasswordToken() {
        return passwordToken;
    }

    public String getHashedNewPassword() {
        return hashedNewPassword;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
