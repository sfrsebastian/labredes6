package gp.e3.autheo.authorization.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BusinessRole {

    private final int id;
    private final String systemRole;
    private final String businessRole;
    private final String description;
    private final String organization;

    public BusinessRole(@JsonProperty("id") int id, @JsonProperty("systemRole") String systemRole, 
            @JsonProperty("businessRole") String businessRole, 
            @JsonProperty("description") String description, @JsonProperty("organization") String organization ) {

        this.id = id;;
        this.systemRole = systemRole;
        this.businessRole = businessRole;
        this.description = description;
        this.organization = organization;
    }



    public int getId() {
        return id;
    }

    public String getSystemRole() {
        return systemRole;
    }

    public String getBusinessRole() {
        return businessRole;
    }   
    
    public String getDescription() {
        return description;
    }
    
    public String getOrganization() {
        return organization;
    }
}