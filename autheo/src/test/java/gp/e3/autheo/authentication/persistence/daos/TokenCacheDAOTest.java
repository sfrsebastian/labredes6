package gp.e3.autheo.authentication.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.TokenFactory;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.util.UserFactoryForTests;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TokenCacheDAOTest {

    private JedisPool redisPoolMock;
    private Jedis redisMock;

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

        redisPoolMock = Mockito.mock(JedisPool.class);
        redisMock = Mockito.mock(Jedis.class);
        Mockito.when(redisPoolMock.getResource()).thenReturn(redisMock);
    }

    @After
    public void tearDown() {
        redisMock = null;
        redisPoolMock = null;
    }

    @Test
    public void testAddToken_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        assertEquals(true, tokenValueEither.isRight());

        String tokenValue = tokenValueEither.right().value();
        Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(),  user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        String returnValue = "OK";

        Mockito.when(redisMock.set(token.getTokenValue(), token.toString())).thenReturn(returnValue);

        TokenCacheDAO tokenDao = new TokenCacheDAO(redisPoolMock);
        boolean addTokenAnswer = tokenDao.addTokenUsingTokenValueAsKey(token);

        assertEquals(true, addTokenAnswer);
    }

    @Test
    public void testAddToken_NOK_notValidToken() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        assertEquals(true, tokenValueEither.isRight());

        String tokenValue = tokenValueEither.right().value();
        Token token = new Token(tokenValue, null, user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());

        TokenCacheDAO tokenDao = new TokenCacheDAO(redisPoolMock);
        assertEquals(false, tokenDao.addTokenUsingTokenValueAsKey(token));
    }

    @Test
    public void testGetTokenByTokenValue_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        assertEquals(true, tokenValueEither.isRight());

        String tokenValue = tokenValueEither.right().value();
        Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());

        Mockito.when(redisMock.get(token.getTokenValue())).thenReturn(token.toString());

        TokenCacheDAO tokenDao = new TokenCacheDAO(redisPoolMock);
        Either<IException, Token> retrievedTokenEither = tokenDao.getTokenByTokenValue(tokenValue);
        assertEquals(true, retrievedTokenEither.isRight());

        Token retrievedToken = retrievedTokenEither.right().value();
        assertEquals(0, token.compareTo(retrievedToken));
    }

    @Test
    public void testGetTokenByTokenValue_NOK_notValidUsername() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        assertEquals(true, tokenValueEither.isRight());

        String tokenValue = tokenValueEither.right().value();
        Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Mockito.when(redisMock.get(token.getTokenValue())).thenReturn(token.toString());

        String notValidUsername = "";
        TokenCacheDAO tokenDao = new TokenCacheDAO(redisPoolMock);
        Either<IException, Token> retrievedTokenEither = tokenDao.getTokenByTokenValue(notValidUsername);
        assertEquals(false, retrievedTokenEither.isRight());
        
        IException exception = retrievedTokenEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    @Test
    public void testRemoveUserAccessToken_OK(){

        String tokenValue = "tokenTest";
        TokenCacheDAO tokenDAO = new TokenCacheDAO(redisPoolMock);

        long deleteLong = (long) 1;
        Mockito.when(redisMock.del(tokenValue)).thenReturn(deleteLong);

        Either<IException, Boolean> tokenWasRemovedEither = tokenDAO.removeUserAccessToken(tokenValue);
        assertEquals(true, tokenWasRemovedEither.isRight());
        
        Boolean tokenWasRemoved = tokenWasRemovedEither.right().value();
        assertEquals(true, tokenWasRemoved);

        deleteLong = (long) 0;
        Mockito.when(redisMock.del(tokenValue)).thenReturn(deleteLong);

        Either<IException, Boolean> secondTokenWasRemovedEither = tokenDAO.removeUserAccessToken(tokenValue);
        assertEquals(true, secondTokenWasRemovedEither.isRight());
        
        Boolean secondTokenWasRemoved = secondTokenWasRemovedEither.right().value();
        assertEquals(false, secondTokenWasRemoved);
    }

    @Test
    public void testRemoveUserAccessToken_NOK_notValidTokenValue(){

        String tokenValue = "";
        TokenCacheDAO tokenDAO = new TokenCacheDAO(redisPoolMock);

        Either<IException, Boolean> tokenWasRemovedEither = tokenDAO.removeUserAccessToken(tokenValue);
        assertEquals(false, tokenWasRemovedEither.isRight());
        
        IException exception = tokenWasRemovedEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }
}