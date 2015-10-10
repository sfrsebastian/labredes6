package gp.e3.autheo.client.dtos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class TokenDTO {

	private final String tokenValue;
	private final String username;
	private final String userOrganization;
	private final String userRole; 
	private final String systemRole;
    private final String businessRole;
    private final String userDocumentType;
    private final String userDocumentNumber;

	@JsonCreator
	public TokenDTO(@JsonProperty("tokenValue") String tokenValue, @JsonProperty("username") String username, 
			@JsonProperty("userOrganization") String userOrganization, @JsonProperty("userRole") String userRole, 
			@JsonProperty("systemRole") String systemRole, @JsonProperty("businessRole") String businessRole,
			@JsonProperty("userDocumentType") String userDocumentType, @JsonProperty("userDocumentNumber") String userDocumentNumber) {

		this.tokenValue = tokenValue;
		this.username = username;
		this.userOrganization = userOrganization;
		this.userRole = userRole;
		this.systemRole = systemRole;
		this.businessRole = businessRole;
		this.userDocumentType = userDocumentType;
		this.userDocumentNumber = userDocumentNumber;
	}

	public String getTokenValue() {
		return tokenValue;
	}

	public String getUsername() {
		return username;
	}

	public String getUserOrganization() {
		return userOrganization;
	}

	public String getUserRole() {
		return userRole;
	}

	public String getSystemRole() {
        return systemRole;
    }

    public String getBusinessRole() {
        return businessRole;
    }

    public String getUserDocumentType() {
        return userDocumentType;
    }

    public String getUserDocumentNumber() {
        return userDocumentNumber;
    }


	@Override
	public String toString() {

		Gson gson = new Gson();
		return gson.toJson(this);
	}
}