package gp.e3.autheo.authentication.infrastructure.utils.http;

import gp.e3.autheo.authorization.service.representations.StringMessage;

import javax.ws.rs.core.Response;

public class HttpCommonResponses {
	
	public static Response getInvalidSyntaxResponse() {

		String errorMessage = "The request cannot be fulfilled due to bad syntax. Please check the URL and the payload of the HTTP request.";
		StringMessage stringMessage = new StringMessage(errorMessage);
		
		return Response.status(400).entity(stringMessage).build();
	}
}