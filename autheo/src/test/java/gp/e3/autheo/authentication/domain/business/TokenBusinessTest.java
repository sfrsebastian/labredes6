package gp.e3.autheo.authentication.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.persistence.daos.TokenCacheDAO;
import gp.e3.autheo.authentication.persistence.daos.TokenDAO;
import gp.e3.autheo.util.TokenFactoryForTests;
import gp.e3.autheo.util.UserFactoryForTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TokenBusinessTest {

    private Connection dbConnectionMock;
    private BasicDataSource dataSourceMock;

    private TokenDAO tokenDAOMock;
    private TokenCacheDAO tokenCacheDaoMock;
    private TokenBusiness tokenBusiness;

    @Before
    public void setUp() {

        dbConnectionMock = Mockito.mock(Connection.class);
        dataSourceMock = Mockito.mock(BasicDataSource.class);

        try {
            Mockito.when(dataSourceMock.getConnection()).thenReturn(dbConnectionMock);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        tokenDAOMock = Mockito.mock(TokenDAO.class);
        tokenCacheDaoMock = Mockito.mock(TokenCacheDAO.class);

        int listSize = 5;
        List<Token> allTokens = TokenFactoryForTests.getTokenList(listSize);
        Either<IException, List<Token>> allTokensEither = Either.right(allTokens);
        Mockito.when(tokenDAOMock.getAllTokens(dbConnectionMock)).thenReturn(allTokensEither);

        tokenBusiness = new TokenBusiness(dataSourceMock, tokenDAOMock, tokenCacheDaoMock);
    }

    @After
    public void tearDown() {

        tokenDAOMock = null;
        tokenCacheDaoMock = null;
        tokenBusiness = null;
    }

    @Test
    public void testGenerateAndSaveTokensForAnAPIUser_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        Either<IException, Boolean> tokensWereGeneratedAndSavedEither = tokenBusiness.generateAndSaveTokensForAnAPIUser(user);
        assertEquals(true, tokensWereGeneratedAndSavedEither.isRight());

        boolean tokensWereGeneratedAndSaved = tokensWereGeneratedAndSavedEither.right().value();
        assertEquals(true, tokensWereGeneratedAndSaved);
    }

    @Test
    public void testGenerateAndSaveTokensForAnAPIUser_NOK() {

        User user = null;
        Either<IException, Boolean> tokensWereGeneratedAndSavedEither = tokenBusiness.generateAndSaveTokensForAnAPIUser(user);
        assertEquals(false, tokensWereGeneratedAndSavedEither.isRight());

        IException exception = tokensWereGeneratedAndSavedEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    @Test
    public void testGenerateToken_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        assertEquals(true, tokenValueEither.isRight());

        String tokenValue = tokenValueEither.right().value();
        Token testToken = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());

        boolean returnValue = true;
        Mockito.when(tokenCacheDaoMock.addTokenUsingTokenValueAsKey(testToken)).thenReturn(returnValue);

        Either<IException, Token> generatedTokenEither = tokenBusiness.generateToken(user);
        assertEquals(true, generatedTokenEither.isRight());

        /*
         * The username of both tokens should be the same, but the token values
         * should be different because they are generated randomly.
         */
        Token generatedToken = generatedTokenEither.right().value();
        assertEquals(testToken.getUsername(), generatedToken.getUsername());
        assertNotEquals(testToken.getTokenValue(), generatedToken.getTokenValue());
    }

    @Test
    public void testGenerateToken_NOK_nullUser() {

        User user = null;
        Either<IException, Token> generatedTokenEither = tokenBusiness.generateToken(user);
        assertEquals(false, generatedTokenEither.isRight());

        IException exception = generatedTokenEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    @Test
    public void testGetAPIToken_OK_1() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenEither = TokenFactory.getToken(user);
        assertEquals(true, tokenEither.isRight());
        
        String tokenValue = tokenEither.right().value();
        Token testToken = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> testTokenEither = Either.right(testToken);
        
        Mockito.when(tokenCacheDaoMock.getTokenByTokenValue(tokenValue)).thenReturn(testTokenEither);

        Either<IException, Token> apiTokenEither = tokenBusiness.getAPIToken(testToken.getTokenValue());
        assertEquals(true, apiTokenEither.isRight());
        
        Token apiToken= apiTokenEither.right().value();
        assertEquals(0, testToken.compareTo(apiToken));
    }

    @Test
    public void testGetAPIToken_OK_2_forceUpdateTokensCache() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenEither = TokenFactory.getToken(user);
        assertEquals(true, tokenEither.isRight());
        
        IException exception = new TechnicalException("Could not reach the tokens cache.");
        Either<IException, Token> leftTokenEither = Either.left(exception);
        
        String tokenValue = tokenEither.right().value();
        Token testToken = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> testTokenEither = Either.right(testToken);
        
        // First return a left wither then return a valid Token either object.
        Mockito.when(tokenCacheDaoMock.getTokenByTokenValue(tokenValue)).thenReturn(leftTokenEither).thenReturn(testTokenEither);

        Either<IException, Token> apiTokenEither = tokenBusiness.getAPIToken(testToken.getTokenValue());
        assertEquals(true, apiTokenEither.isRight());
        
        Token apiToken = apiTokenEither.right().value();
        assertEquals(0, testToken.compareTo(apiToken));
    }

    @Test
    public void testGetAPIToken_NOK_notValidTokenValue() {

        String nullTokenValue = null;
        Either<IException, Token> apiTokenEither = tokenBusiness.getAPIToken(nullTokenValue);
        assertEquals(false, apiTokenEither.isRight());

        IException exception = apiTokenEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }
    
    @Test
    public void testGetAPIToken_NOK_couldNotAccessTokenCache() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenEither = TokenFactory.getToken(user);
        assertEquals(true, tokenEither.isRight());
        
        IException technicalException = new TechnicalException("Could not reach the tokens cache.");
        Either<IException, Token> leftTokenEither = Either.left(technicalException);
        
        String tokenValue = tokenEither.right().value();
        
        // First return a left wither then return a valid Token either object.
        Mockito.when(tokenCacheDaoMock.getTokenByTokenValue(tokenValue)).thenReturn(leftTokenEither).thenReturn(leftTokenEither);

        Either<IException, Token> apiTokenEither = tokenBusiness.getAPIToken(tokenValue);
        assertEquals(false, apiTokenEither.isRight());

        IException exception = apiTokenEither.left().value();
        assertEquals(0, technicalException.compareTo(exception));
    }

    @Test
    public void testGetModuleToken_OK_1() {

        User user = UserFactoryForTests.getDefaultTestUser();
        String organizationId = user.getOrganizationId();
        Token expectedToken = TokenFactoryForTests.getDefaultTestToken();
        Either<IException, Token> expectedTokenEither = Either.right(expectedToken);
        
        Mockito.when(tokenCacheDaoMock.getTokenByOrganization(organizationId)).thenReturn(expectedTokenEither);
        Either<IException, Token> moduleTokenEither = tokenBusiness.getModuleToken(organizationId);
        assertEquals(true, moduleTokenEither.isRight());
        
        Token moduleToken = moduleTokenEither.right().value();
        assertEquals(0, expectedToken.compareTo(moduleToken));
    }

    @Test
    public void testGetModuleToken_OK_2() {

        User user = UserFactoryForTests.getDefaultTestUser();
        String organizationId = user.getOrganizationId();
        
        IException technicalException = new TechnicalException("Could not reach the tokens cache.");
        Either<IException, Token> leftEither = Either.left(technicalException);
        
        Token expectedToken = TokenFactoryForTests.getDefaultTestToken();
        Either<IException, Token> expectedTokenEither = Either.right(expectedToken);
        
        Mockito.when(tokenCacheDaoMock.getTokenByOrganization(organizationId)).thenReturn(leftEither).thenReturn(expectedTokenEither);
        Either<IException, Token> moduleTokenEither = tokenBusiness.getModuleToken(organizationId);
        assertEquals(true, moduleTokenEither.isRight());

        Token moduleToken = moduleTokenEither.right().value();
        assertEquals(0, expectedToken.compareTo(moduleToken));
    }

    @Test
    public void testGetModuleToken_NOK_notValidOrganizationId() {

        String nullOrganizationId = null;
        Either<IException, Token> moduleTokenEither = tokenBusiness.getModuleToken(nullOrganizationId);
        assertEquals(false, moduleTokenEither.isRight());
        
        IException exception = moduleTokenEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    @Test
    public void testRemoveUserAccessToken_OK() {

        String tokenValue = "tokenTest";

        Either<IException, Boolean> tokenWasRemovedFromCacheEither = Either.right(true);
        Mockito.when(tokenCacheDaoMock.removeUserAccessToken(tokenValue)).thenReturn(tokenWasRemovedFromCacheEither);

        Either<IException, Boolean> tokenWasRemovedEither = tokenBusiness.removeUserAccessToken(tokenValue);
        assertEquals(true, tokenWasRemovedEither.isRight());
        assertEquals(true, tokenWasRemovedEither.right().value());
    }

    @Test
    public void testRemoveUserAccessToken_NOK_couldNotAccessTokensCache() {

        String tokenValue = "tokenTest";

        IException technicalException = new TechnicalException("Could not access the tokens cache.");
        Either<IException, Boolean> tokenWasRemovedFromCacheEither = Either.left(technicalException);
        Mockito.when(tokenCacheDaoMock.removeUserAccessToken(tokenValue)).thenReturn(tokenWasRemovedFromCacheEither);

        Either<IException, Boolean> tokenWasRemovedEither = tokenBusiness.removeUserAccessToken(tokenValue);
        assertEquals(false, tokenWasRemovedEither.isRight());
        
        IException exception = tokenWasRemovedEither.left().value();
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }
}