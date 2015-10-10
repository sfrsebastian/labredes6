package gp.e3.autheo.authorization.filter;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.business.TicketBusiness;
import gp.e3.autheo.authorization.domain.entities.Ticket;
import gp.e3.autheo.client.dtos.TokenDTO;
import gp.e3.autheo.client.utils.TokenDTOUtils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import com.google.gson.Gson;

public class InternalRequestFilter implements Filter {
    
    ////////////////////////
    // Attributes
    ////////////////////////

	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_ATTRIBUTE = "Token";
	
    private final TicketBusiness ticketBusiness;

	private Gson gson;
	
    ////////////////////////
    // Constructor
    ////////////////////////

	public InternalRequestFilter(TicketBusiness ticketBusiness) {

		gson = new Gson();
		this.ticketBusiness = ticketBusiness;
	}
	
    ////////////////////////
    // Public Methods
    ////////////////////////

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

		String tokenValue = httpRequest.getHeader(TOKEN_HEADER);
		String httpVerb = httpRequest.getMethod();
		String requestedUri = httpRequest.getRequestURI();

		/* 
		 * Typically the browser sends an OPTIONS request by itself. Due to the browser does not 
		 * include the Authorization header we will allow all OPTIONS requests to pass.
		 */
		if (httpVerb.equalsIgnoreCase("OPTIONS")) {

			// Go to the next filter. If this filter is the last one, then go to the resource.
			filterChain.doFilter(servletRequest, servletResponse);

		} else {
		    
		    //Checks to see if it is a public authorization
            Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(httpVerb, requestedUri);
            
            if (isPublicPermissionEither.isRight()) {
                
                Boolean isPublicPermission = isPublicPermissionEither.right().value();
                
                if (isPublicPermission) {
                    
                    // Go to the next filter. If this filter is the last one, then go to the resource.
                    filterChain.doFilter(servletRequest, servletResponse);
                    
                } else {
                    
                    //Gets the token
                    Either<IException, Token> tokenWasIssuedByAutheoEither = ticketBusiness.getAPITokenByTokenValue(tokenValue);
                    
                    if (tokenWasIssuedByAutheoEither.isRight()) {
                        
                        //Checks to see if the person is authorized
                        Ticket ticket = new Ticket(tokenValue, httpVerb, requestedUri);
                        Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
                        
                        if (userIsAuthorizedEither.isRight()) {
                            
                            Boolean userIsAuthorized = userIsAuthorizedEither.right().value();
                            
                            if(userIsAuthorized) {
                                
                                // Puts the token in the http request.
                                Token token = tokenWasIssuedByAutheoEither.right().value();
                                TokenDTO tokenDTO = TokenDTOUtils.buildTokenDTOFromToken(token);
                                servletRequest.setAttribute(TOKEN_ATTRIBUTE, tokenDTO);
                                
                                // Go to the next filter. If this filter is the last one, then go to the resource.
                                filterChain.doFilter(servletRequest, servletResponse);
                                
                            } else {//The person is not authorized
                                
                                // Response with the given status code.
                                HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
                                httpResponse.setContentType("application/json");
                                httpResponse.setStatus(Status.FORBIDDEN.getStatusCode());

                                
                                //Sends the exception in the body
                                String errorMessage = "You don't have permissions.";
                                httpResponse.getWriter().write(errorMessage);
                                
                            }
                            
                        } else {//Had problems checking the authorization
                            
                            buildExceptionResponse(servletResponse, userIsAuthorizedEither);
                            
                        }
                        
                    } else {//Couldn't get the token
                        
                        buildExceptionResponse(servletResponse, tokenWasIssuedByAutheoEither);
                        
                    }
                    
                }
                
            } else {//It is not a public permission
                
                buildExceptionResponse(servletResponse, isPublicPermissionEither);
                
            }
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
	
    ////////////////////////
    // Private Methods
    ////////////////////////
	
    private void buildExceptionResponse(ServletResponse servletResponse, Either<IException, ?> isPublicPermissionEither) throws IOException {
        
        // Response with the given status code.
        IException exception = isPublicPermissionEither.left().value();
        Status statusCode = exception instanceof BusinessException ? Status.UNAUTHORIZED : Status.INTERNAL_SERVER_ERROR;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setContentType("application/json");
        httpResponse.setStatus(statusCode.getStatusCode());
        
        //Sends the exception in the body
        String jsonException = gson.toJson(exception);
        httpResponse.getWriter().write(jsonException);
    }
	
}