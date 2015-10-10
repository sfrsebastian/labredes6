package gp.e3.autheo.authentication.domain.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordToken implements Comparable<PasswordToken> {

	private final String tokenValue;
	private final String username;
	private final String userOrganization;
	private final LocalDateTime requestDate; 
	
	@JsonCreator
	public PasswordToken(@JsonProperty("tokenValue") String tokenValue, @JsonProperty("username") String username, 
				 @JsonProperty("userOrganization") String userOrganization, @JsonProperty("requestDate") LocalDateTime requestDate) {
		
		this.tokenValue = tokenValue;
		this.username = username;
		this.userOrganization = userOrganization;
		this.requestDate = requestDate;
        
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

	public LocalDateTime getRequestDate() {
        return requestDate;
    }

    @Override
	public int compareTo(PasswordToken passwordToken) {

		int answer = 0;

		answer += this.tokenValue.compareTo(passwordToken.getTokenValue());
		answer += this.username.compareTo(passwordToken.getUsername());
		answer += this.userOrganization.compareTo(passwordToken.getUserOrganization());
		answer += this.requestDate.compareTo(passwordToken.getRequestDate());

		return answer;
	}
	
}
