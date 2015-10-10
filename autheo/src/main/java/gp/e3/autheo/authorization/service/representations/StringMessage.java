package gp.e3.autheo.authorization.service.representations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StringMessage {
	
	private final String message;

	@JsonCreator
	public StringMessage(@JsonProperty("message") String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}