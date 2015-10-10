package gp.e3.autheo.authentication.domain.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Citizen  {

	private final String name;

	private final String documentType;
	private final String documentNumber;
	private final String email;
	private final String telephoneNumber;
	private final String address;

	private final String username;
	private final String password;

	private final String organizationId;

	@JsonCreator
	public Citizen(@JsonProperty("name") String name, @JsonProperty("documentType") String documentType, @JsonProperty("documentNumber") String documentNumber,
			@JsonProperty("email") String email, @JsonProperty("username") String username, @JsonProperty("telephoneNumber") String telephoneNumber,
			@JsonProperty("address") String address, @JsonProperty("password") String password, @JsonProperty("organizationId") String organizationId
			) {

		this.name = name;

		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.address = address;

		this.username = username;
		this.password = password;
		this.organizationId = organizationId;
	}

	public String getName() {
		return name;
	}

	public String getDocumentType() {
		return documentType;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public String getEmail() {
		return email;
	}

	public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getOrganizationId() {
		return organizationId;
	}



}
