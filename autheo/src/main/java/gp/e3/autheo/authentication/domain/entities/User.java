package gp.e3.autheo.authentication.domain.entities;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User implements Comparable<User> {

	private final String name;

	private final String documentType;
	private final String documentNumber;
	private final String email;
	private final String telephoneNumber;
	private final String address;

	private final String username;
	private final String password;
	private final boolean apiClient;

	private final String organizationId;
	private final String roleId;
	private final String systemRoleId;
	private final String businessRoleId;
	
	private byte[] image;

	@JsonCreator
	public User(@JsonProperty("name") String name, @JsonProperty("documentType") String documentType, @JsonProperty("documentNumber") String documentNumber,
			@JsonProperty("email") String email, @JsonProperty("username") String username, @JsonProperty("telephoneNumber") String telephoneNumber, 
			@JsonProperty("address") String address, @JsonProperty("password") String password, @JsonProperty("apiClient") boolean isApiClient, 
			@JsonProperty("organizationId") String organizationId, @JsonProperty("roleId") String roleId, @JsonProperty("systemRoleId") String systemRoleId,  
			@JsonProperty("businessRoleId") String businessRoleId) {

		this.name = name;

		this.documentType = documentType;
		this.documentNumber = documentNumber;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.address = address;

		this.username = username;
		this.password = password;
		this.apiClient = isApiClient;

		this.organizationId = organizationId;
		this.roleId = roleId;
		this.systemRoleId = systemRoleId;
		this.businessRoleId = businessRoleId;
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

	public boolean isApiClient() {
		return apiClient;
	}

	public String getOrganizationId() {

		return organizationId;
	}

	public String getRoleId() {
		
		return roleId;
	}

	public static boolean isAValidUser(User user) {
		
		return (user != null) && 
				(!StringUtils.isBlank(user.getName())) && 
				(!StringUtils.isBlank(user.getDocumentType())) && 
				(!StringUtils.isBlank(user.getDocumentNumber())) && 
				(!StringUtils.isBlank(user.getEmail())) && 
				(!StringUtils.isBlank(user.getUsername())) && 
				(!StringUtils.isBlank(user.getOrganizationId()) && 
						(!StringUtils.isBlank(user.getRoleId())));
	}

	@Override
	public int compareTo(User user) {

		int answer = 0;

		if (user instanceof User) {

			answer += this.name.compareTo(user.getName());
			answer += this.documentType.compareTo(user.getDocumentType());
			answer += this.documentNumber.compareTo(user.getDocumentNumber());
			answer += this.email.compareTo(user.getEmail());
			answer += this.username.compareTo(user.getUsername());
			answer += this.password.compareTo(user.getPassword());
			answer += (this.apiClient == user.isApiClient()) ? 0 : 1;
			answer += this.organizationId.compareTo(user.getOrganizationId());
			answer += this.roleId.compareTo(user.roleId);
		}

		return answer;
	}

	public String getSystemRoleId() {
		return systemRoleId;
	}

	public String getBusinessRoleId() {
		return businessRoleId;
	}

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
