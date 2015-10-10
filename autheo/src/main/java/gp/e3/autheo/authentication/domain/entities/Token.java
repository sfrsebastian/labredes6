package gp.e3.autheo.authentication.domain.entities;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

public class Token implements Comparable<Token> {

	private final String tokenValue;
	private final String username;
	private final String userOrganization;
	private final String userRole; 
	private final int tokenType;
	private final String systemRole;
	private final String businessRole;
	private byte[] image;
    private final String userDocumentType;
    private final String userDocumentNumber;
	
	@JsonCreator
	public Token(@JsonProperty("tokenValue") String tokenValue, @JsonProperty("username") String username, 
				 @JsonProperty("userOrganization") String userOrganization, @JsonProperty("userRole") String userRole,
				 @JsonProperty("tokenType") int tokenType, @JsonProperty("systemRole") String systemRole,
				 @JsonProperty("businessRole") String businessRole, @JsonProperty("userDocumentType") String userDocumentType, @JsonProperty("userDocumentNumber") String userDocumentNumber) {
		
		this.tokenValue = tokenValue;
		this.username = username;
		this.userOrganization = userOrganization;
		this.userRole = userRole;
		this.tokenType = tokenType;
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

	public int getTokenType() {
		return tokenType;
	}
	
    public String getUserDocumentType() {
        return userDocumentType;
    }

    public String getUserDocumentNumber() {
        return userDocumentNumber;
    }

	public static boolean isAValidToken(Token token) {

		return (token != null) && 
				!StringUtils.isBlank(token.getTokenValue()) && 
				!StringUtils.isBlank(token.getUsername()) && 
				!StringUtils.isBlank(token.getUserOrganization()) && 
				!StringUtils.isBlank(token.getUserRole()) && 
				(token.getTokenType() > 0);
	}
	
	public static Token buildTokenFromTokenToString(String tokenToString) {
		
		Gson gson = new Gson();
		return gson.fromJson(tokenToString, Token.class);
	}

	@Override
	public int compareTo(Token token) {

		int answer = 0;

		answer += this.tokenValue.compareTo(token.getTokenValue());
		answer += this.username.compareTo(token.getUsername());
		answer += this.userOrganization.compareTo(token.getUserOrganization());
		answer += this.userRole.compareTo(token.getUserRole());
		answer += (this.tokenType == token.getTokenType() ? 0 : 1);

		return answer;
	}
	
	@Override
	public String toString() {
		
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	public String getSystemRole() {
		return systemRole;
	}

	public String getBusinessRole() {
		return businessRole;
	}

	
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
