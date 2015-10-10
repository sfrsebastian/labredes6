package gp.e3.autheo.authentication.service;

import static org.junit.Assert.assertEquals;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.service.resources.TokenResource;
import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class TokenResourceTest {
	
	private static final TokenBusiness tokenBusinessMock = Mockito.mock(TokenBusiness.class);
	private static final TokenResource tokenResource = new TokenResource(tokenBusinessMock);
	
	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(tokenResource).build();
	
	private Builder getDefaultHttpRequest(String url) {
		
		return resources.client().resource(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
	}
	
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {
		
		Mockito.reset(tokenBusinessMock);
	}
	
	@Test
	public void testRemoveUserAccessTokenFromCache_OK1() {
		
		String url = "/tokens/test/cache";
		
		Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(tokenBusinessMock.removeUserAccessToken("test")).thenReturn(trueEither);

        ClientResponse httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);
        assertEquals(200, httpResponse.getStatus());
        
        Either<IException, Boolean> falseEither = Either.right(false);
        Mockito.when(tokenBusinessMock.removeUserAccessToken("test")).thenReturn(falseEither);

        httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);
        assertEquals(409, httpResponse.getStatus());
	}
	
	@Test
	public void testRemoveUserAccessTokenFromCache_NOK_leftEither() {
		
		String url = "/tokens/test/cache";
		
		IException technicalException = new TechnicalException("Could not access tokens cache.");
		Either<IException, Boolean> leftEither = Either.left(technicalException);
		Mockito.when(tokenBusinessMock.removeUserAccessToken(Mockito.anyString())).thenReturn(leftEither);

		ClientResponse httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);
        assertEquals(409, httpResponse.getStatus());
	}
}