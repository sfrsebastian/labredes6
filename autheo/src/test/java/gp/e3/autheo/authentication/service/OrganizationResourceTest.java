package gp.e3.autheo.authentication.service;


public class OrganizationResourceTest {

//	private static final TokenBusiness tokenBusinessMock = Mockito.mock(TokenBusiness.class);
//	private static final OrganizationBusiness organizationBusiness = Mockito.mock(OrganizationBusiness.class);
//	private static final OrganizationResource organizationResource = new OrganizationResource(tokenBusinessMock, organizationBusiness);
//
//	@ClassRule
//	public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(organizationResource).build();
//
//	private Builder getDefaultHttpRequest(String url) {
//		
//		return resources.client().resource(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
//	}
//
//	@Test
//	public void testGetModuleTokenByUserOrganization_OK() {
//
//		Token expectedToken = TokenFactoryForTests.getDefaultTestToken();
//		Either<IException, Token> expectedTokenEither = Either.right(expectedToken);
//		
//		String userOrganization = expectedToken.getUserOrganization();
//
//		Mockito.when(tokenBusinessMock.getModuleToken(Mockito.anyString())).thenReturn(expectedTokenEither);
//
//		String url = "/organizations/" + userOrganization + "/module-token";
//		ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);
//		int responseStatusCode = httpResponse.getStatus();
//
//		assertEquals(200, responseStatusCode);
//
//		Token token = httpResponse.getEntity(Token.class);
//		assertNotNull(token);
//		assertEquals(userOrganization, token.getUserOrganization());
//	}
//
//	@Test
//	public void testGetModuleTokenByUserOrganization_NOK_errorGettingModuleToken() {
//
//		Token expectedToken = TokenFactoryForTests.getDefaultTestToken();
//		IException businessException = new BusinessException("Token not found.");
//		Either<IException, Token> expectedTokenEither = Either.left(businessException);
//		
//		String userOrganization = expectedToken.getUserOrganization();
//		
//		Mockito.when(tokenBusinessMock.getModuleToken(Mockito.anyString())).thenReturn(expectedTokenEither);
//
//		String url = "/organizations/" + userOrganization + "/module-token";
//		ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);
//		int responseStatusCode = httpResponse.getStatus();
//
//		assertEquals(404, responseStatusCode);
//
//		String errorMessage = httpResponse.getEntity(String.class);
//		assertEquals(true, !StringUtils.isBlank(errorMessage));
//	}
//
//	@Test
//	public void testGetModuleTokenByUserOrganization_NOK_emptyUserOrganization() {
//
//		Token expectedToken = TokenFactoryForTests.getDefaultTestToken();
//		Either<IException, Token> expectedTokenEither = Either.right(expectedToken);
//		
//		String emptyUserOrganization = "";
//
//		Mockito.when(tokenBusinessMock.getModuleToken(Mockito.anyString())).thenReturn(expectedTokenEither);
//
//		String url = "/organizations/" + emptyUserOrganization + "/module-token";
//		ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);
//		int responseStatusCode = httpResponse.getStatus();
//
//		/* 
//		 * It does not found the route "GET /organizations//module-token".
//		 * Jersey parses as other route. Not to /organizations//module-token with an empty organizationId as expected, 
//		 * that's why the expected status code is 404. 
//		 */
//		assertEquals(404, responseStatusCode);
//	}
//
//	@Test
//	public void testGetModuleTokenByUserOrganization_NOK_nullUserOrganization() {
//
//		String nullUserOrganization = null;
//		
//		String message = "The given userOrganizationId is not valid.";
//        IException businessException = new BusinessException(message);
//        Either<IException, Token> expectedTokenEither = Either.left(businessException);
//		// A null string is parsed as "null".
//		Mockito.when(tokenBusinessMock.getModuleToken(Mockito.anyString())).thenReturn(expectedTokenEither);
//
//		String url = "/organizations/" + nullUserOrganization + "/module-token";
//		ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);
//		int responseStatusCode = httpResponse.getStatus();
//
//		assertEquals(404, responseStatusCode);
//
//		String errorMessage = httpResponse.getEntity(String.class);
//		assertEquals(true, !StringUtils.isBlank(errorMessage));
//	}
}