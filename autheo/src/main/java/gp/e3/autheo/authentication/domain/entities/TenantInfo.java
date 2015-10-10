package gp.e3.autheo.authentication.domain.entities;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TenantInfo implements Comparable<TenantInfo> {

	private final String organizationId;
	
	private final String name;
	
	private final String username;
	
	private final String password;
	
	private final String documentType;
	
	private final String documentNumber;
	
	private final String email;
	
	private final String address;
	
	private final String telephone;
	
	
	@JsonCreator
	public TenantInfo(@JsonProperty("email") String email, @JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("name") String name,
			@JsonProperty("organizationId") String organizationId, @JsonProperty("documentType") String documentType, @JsonProperty("documentNumber") String documentNumber, 
			@JsonProperty("address") String address, @JsonProperty("telephone") String telephone) {

		this.name = name;
		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.username = username;
		this.password = password;

		this.organizationId = organizationId;
		this.email = email;
		this.address = address;
		
		this.telephone = telephone;
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


	public static boolean isAValidUser(TenantInfo user) {
		
		return (user != null) && 
				(!StringUtils.isBlank(user.getUsername())) && 
				(!StringUtils.isBlank(user.getOrganizationId()));
	}

	@Override
	public int compareTo(TenantInfo user) {

		int answer = 0;

		if (user instanceof TenantInfo) {
			answer += this.username.compareTo(user.getUsername());
			answer += this.password.compareTo(user.getPassword());
			answer += this.organizationId.compareTo(user.getOrganizationId());
		}

		return answer;
	}


	public String getEmail() {
		return email;
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


	public String getAddress() {
		return address;
	}


	public String getTelephone() {
		return telephone;
	}

}
