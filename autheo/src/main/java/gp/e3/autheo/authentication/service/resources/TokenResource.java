package gp.e3.autheo.authentication.service.resources;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.service.representations.StringMessage;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/tokens")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TokenResource {
	
	private final TokenBusiness tokenBusiness;
	
	public TokenResource(TokenBusiness tokenBusiness) {
		this.tokenBusiness = tokenBusiness;
	}
	
	@DELETE
	@Path("/{tokenValue}/cache")
	public Response removeUserAccessTokenFromCache(@PathParam("tokenValue") String tokenValue) {
		
		Response response = null;
		
		Either<IException, Boolean> tokenWasRemovedEither = tokenBusiness.removeUserAccessToken(tokenValue);
        
        if(tokenWasRemovedEither.isRight()) {
            
            Boolean tokenWasRemoved = tokenWasRemovedEither.right().value();
            
            if (tokenWasRemoved) {
                
                String errorMessage = "Access token : " + tokenValue + " was successfully removed from the cache";
                StringMessage stringMessage = new StringMessage(errorMessage);
                response = Response.status(Status.OK).entity(stringMessage).build();
                
            } else {
                
                String errorMessage = "Access token : " + tokenValue + " was NOT removed from the cache because it was not found";
                StringMessage stringMessage = new StringMessage(errorMessage);
                response = Response.status(Status.CONFLICT).entity(stringMessage).build();
            }
        	
        } else {
        	
        	String errorMessage = "Access token : " + tokenValue + " was NOT removed from the cache because it was not found";
        	StringMessage stringMessage = new StringMessage(errorMessage);
        	response = Response.status(Status.CONFLICT).entity(stringMessage).build();
        }
		
		return response;
	}
}