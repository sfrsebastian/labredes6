package gp.e3.autheo.authentication.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.util.UserFactoryForTests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TokenFactoryTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetToken_OK() {

        User defaultUser = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenFromDefaultUserEither = TokenFactory.getToken(defaultUser);
        assertEquals(true, tokenFromDefaultUserEither.isRight());

        User user2 = UserFactoryForTests.getDefaultTestUser(2);
        Either<IException, String> tokenFromUser2Either = TokenFactory.getToken(user2);
        assertEquals(true, tokenFromUser2Either.isRight());

        String tokenFromDefaultUser = tokenFromDefaultUserEither.right().value();
        String tokenFromUser2 = tokenFromUser2Either.right().value();
        assertNotEquals(tokenFromDefaultUser, tokenFromUser2);

        Either<IException, String> tokenFromDefaultUserSecondTokenEither = TokenFactory.getToken(defaultUser);
        String defaultUserSecondToken = tokenFromDefaultUserSecondTokenEither.right().value();

        /*
         * Not even the tokens generated from the same user should be the same
         * because the tokens are generated randomly.
         */
        assertNotEquals(tokenFromDefaultUser, defaultUserSecondToken);
    }

    @Test
    public void testGetToken_NOK() {

        User defaultUser = null;
        Either<IException, String> tokenValueEither = TokenFactory.getToken(defaultUser);
        assertEquals(false, tokenValueEither.isRight());
        
        IException exception = tokenValueEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }
}