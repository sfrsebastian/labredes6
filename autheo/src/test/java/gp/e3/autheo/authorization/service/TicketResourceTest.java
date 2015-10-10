package gp.e3.autheo.authorization.service;

import static org.junit.Assert.assertEquals;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authorization.domain.business.TicketBusiness;
import gp.e3.autheo.authorization.domain.entities.Ticket;
import gp.e3.autheo.authorization.service.resources.TicketResource;
import gp.e3.autheo.util.TicketFactoryForTests;
import gp.e3.autheo.util.UserFactoryForTests;
import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class TicketResourceTest {

	private static final TicketBusiness ticketBusinessMock = Mockito.mock(TicketBusiness.class);
	private static final TicketResource ticketResource = new TicketResource(ticketBusinessMock);
	
	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(ticketResource).build();
	
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {
		
		Mockito.reset(ticketBusinessMock);
	}
	
	private Builder getDefaultHttpRequest(String url) {

		return resources.client().resource(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
	}
	
	@Test
	public void testIsAuthorized_OK_1() {
		
		Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
		
		Either<IException, Boolean> trueEither = Either.right(true);
		Mockito.when(ticketBusinessMock.isPublicPermission(Mockito.anyString(), Mockito.anyString())).thenReturn(trueEither);
		
		String url = "/auth";
		ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, ticket);
		
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testIsAuthorized_OK_2() {
		
		Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
		
		User user = UserFactoryForTests.getDefaultTestUser();
		Token token = new Token(ticket.getTokenValue(), user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), 
		        user.getBusinessRoleId(), user.getDocumentType(), user.getDocumentNumber());
		Either<IException, Token> tokenEither = Either.right(token);
		
		Either<IException, Boolean> trueEither = Either.right(true);
		Either<IException, Boolean> falseEither = Either.right(false);
		Mockito.when(ticketBusinessMock.isPublicPermission(Mockito.anyString(), Mockito.anyString())).thenReturn(falseEither);
		Mockito.when(ticketBusinessMock.getAPITokenByTokenValue(Mockito.anyString())).thenReturn(tokenEither);
		Mockito.when(ticketBusinessMock.userIsAuthorized(Mockito.any(Ticket.class))).thenReturn(trueEither);
		
		String url = "/auth";
		ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, ticket);
		
		assertEquals(200, response.getStatus());
	}
	
	@Test
    public void testIsAuthorized_NOK_unauthorized() {
        
        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
        
        Either<IException, Boolean> falseEither = Either.right(false);
        Mockito.when(ticketBusinessMock.isPublicPermission(Mockito.anyString(), Mockito.anyString())).thenReturn(falseEither);
        
        IException businessException = new BusinessException("The given token value is null or empty.");
        Either<IException, Token> tokenEither = Either.left(businessException);
        
        Mockito.when(ticketBusinessMock.getAPITokenByTokenValue(Mockito.anyString())).thenReturn(tokenEither);
        
        String url = "/auth";
        ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, ticket);
        assertEquals(401, response.getStatus());
    }
	
	@Test
	public void testIsAuthorized_NOK_forbidden() {
		
		Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
		
		Either<IException, Boolean> falseEither = Either.right(false);
		Mockito.when(ticketBusinessMock.isPublicPermission(Mockito.anyString(), Mockito.anyString())).thenReturn(falseEither);
		
		User user = UserFactoryForTests.getDefaultTestUser();
		Token token = new Token(ticket.getTokenValue(), user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), 
		        user.getBusinessRoleId(), user.getDocumentType(), user.getDocumentNumber());
		
		Either<IException, Token> tokenEither = Either.right(token);
		Mockito.when(ticketBusinessMock.getAPITokenByTokenValue(Mockito.anyString())).thenReturn(tokenEither);
		Mockito.when(ticketBusinessMock.userIsAuthorized(Mockito.any(Ticket.class))).thenReturn(falseEither);
		
		String url = "/auth";
		ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, ticket);
		assertEquals(403, response.getStatus());
	}
	
	@Test
    public void testIsAuthorized_NOK_technicalException() {
        
        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
        
        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, Boolean> leftEither = Either.left(technicalException);
        Mockito.when(ticketBusinessMock.isPublicPermission(Mockito.anyString(), Mockito.anyString())).thenReturn(leftEither);
        
        String url = "/auth";
        ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, ticket);
        
        assertEquals(500, response.getStatus());
    }
}