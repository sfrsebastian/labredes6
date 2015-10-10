package gp.e3.autheo.authorization.service.resources;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.http.HttpCommonResponses;
import gp.e3.autheo.authorization.domain.business.TicketBusiness;
import gp.e3.autheo.authorization.domain.entities.Ticket;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codahale.metrics.annotation.Timed;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TicketResource {

    private static Logger logger = LoggerFactory.getLogger(TicketResource.class);

    private final TicketBusiness ticketBusiness;

    public TicketResource(TicketBusiness ticketBusiness) {

        this.ticketBusiness = ticketBusiness;
    }

//    private Response checkIfUserIsAuthorized(Ticket ticket) {
//
//        Response response = null;
//
//        if (Ticket.isValidTicket(ticket)) {
//
//            String tokenValue = ticket.getTokenValue();
//            Either<IException, Token> apiTokenEither = ticketBusiness.getAPITokenByTokenValue(tokenValue);
//
//            if (apiTokenEither.isRight()) {
//
//                Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
//
//                if (userIsAuthorizedEither.isRight()) {
//
//                    Boolean userIsAuthorized = userIsAuthorizedEither.right().value();
//
//                    if (userIsAuthorized) {
//
//                        Token apiToken = apiTokenEither.right().value();
//                        response = Response.status(Status.OK).entity(apiToken).build();
//
//                    } else {
//
//                        // 403 Forbidden.
//                        String errorMessage = "You don't have permissions.";
//                        StringMessage stringMessage = new StringMessage(errorMessage);
//                        response = Response.status(Status.FORBIDDEN).entity(stringMessage).build();
//                    }
//
//                } else {
//
//                    IException exception = ExceptionUtils.getIExceptionFromEither(userIsAuthorizedEither);
//                    response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
//                }
//
//            } else {
//
//                // 401 Unauthorized.
//                String errorMessage = "You are not authenticated, please login.";
//                StringMessage stringMessage = new StringMessage(errorMessage);
//                response = Response.status(Status.UNAUTHORIZED).entity(stringMessage).build();
//            }
//
//        } else {
//
//            response = HttpCommonResponses.getInvalidSyntaxResponse();
//        }
//
//        return response;
//    }

//    @PUT
//    @Timed
//    public Response isAuthorized(Ticket ticket) {
//
//        logger.info("Received ticket: " + ticket);
//
//        Response response = null;
//
//        if (ticket != null) {
//
//            Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(ticket.getHttpVerb(), ticket.getRequestedUrl());
//
//            if (isPublicPermissionEither.isRight()) {
//
//                boolean isPublicPermission = isPublicPermissionEither.right().value();
//
//                if (isPublicPermission) {
//
//                    String message = "public permission";
//                    StringMessage stringMessage = new StringMessage(message);
//                    response = Response.status(Status.OK).entity(stringMessage).build();
//
//                } else {
//
//                    response = checkIfUserIsAuthorized(ticket);
//                }
//
//            } else {
//
//                IException exception = isPublicPermissionEither.left().value();
//                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
//            }
//
//        } else {
//
//            response = HttpCommonResponses.getInvalidSyntaxResponse();
//        }
//
//        return response;
//    }

    private Response getAuthorizationResponse(Either<IException, Boolean> userIsAuthorizedEither, Token token) {

        Response response = null;

        Boolean userIsAuthorized = userIsAuthorizedEither.right().value();

        if (userIsAuthorized) {

            response = Response.status(Status.OK).entity(token).build();

        } else {

            // 403 Forbidden.
            String errorMessage = "You don't have permissions.";
            BusinessException businessException = new BusinessException(errorMessage);
            response = Response.status(Status.FORBIDDEN).entity(businessException).build();
        }

        return response;
    }
    
    private Response getAuthorizationAndAuthenticationResponse(Either<IException, Token> tokenWasIssuedByAutheoEither, 
            Either<IException, Boolean> userIsAuthorizedEither) {
        
        Response response = null;
        
        if (tokenWasIssuedByAutheoEither.isRight()) {

            Token token = tokenWasIssuedByAutheoEither.right().value();

            if (userIsAuthorizedEither.isRight()) {

                response = getAuthorizationResponse(userIsAuthorizedEither, token);

            } else {

                IException exception = ExceptionUtils.getIExceptionFromEither(userIsAuthorizedEither);
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            // 401 Unauthorized.
            String errorMessage = "You are not authenticated, please login.";
            BusinessException businessException = new BusinessException(errorMessage);
            response = Response.status(Status.UNAUTHORIZED).entity(businessException).build();
        }
        
        return response;
    }

    @PUT
    @Timed
    public Response isAuthorized(Ticket ticket) {

        logger.info("Received ticket: " + ticket);

        Response response = null;

        if (Ticket.isValidTicket(ticket)) {

            String tokenValue = ticket.getTokenValue();
            String httpVerb = ticket.getHttpVerb();
            String requestedUrl = ticket.getRequestedUrl();
            
            Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(httpVerb, requestedUrl);
            Either<IException, Token> tokenWasIssuedByAutheoEither = ticketBusiness.getAPITokenByTokenValue(tokenValue);
            Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
            
            if (isPublicPermissionEither.isRight()) {
                
                Boolean isPublicPermission = isPublicPermissionEither.right().value();
                
                if (isPublicPermission) {
                    
                    response = Response.status(Status.OK).build();
                    
                } else {
                    
                    response = getAuthorizationAndAuthenticationResponse(tokenWasIssuedByAutheoEither, userIsAuthorizedEither);
                }
                
            } else {
                
                IException exception = isPublicPermissionEither.left().value();
                response = Response.status(Status.INTERNAL_SERVER_ERROR).entity(exception).build();
            }

        } else {

            response = HttpCommonResponses.getInvalidSyntaxResponse();
        }

        return response;
    }
}