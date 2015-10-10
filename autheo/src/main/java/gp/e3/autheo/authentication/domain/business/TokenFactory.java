package gp.e3.autheo.authentication.domain.business;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.domain.exceptions.TokenGenerationException;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;

public class TokenFactory {

    public static final int TOKEN_CHARACTER_LIMIT = 20;

    /**
     * Calculate and return the hash of a given string using PBKDF2WithHmacSHA1.
     * 
     * @throws NoSuchAlgorithmException 
     * @throws InvalidKeySpecException 
     */
    private static final Either<IException, String> getHashFromString(String string) {

        Either<IException, String> hashFromStringEither = null;

        byte[] salt = new byte[16];

        Random random = new Random();
        random.nextBytes(salt);

        KeySpec spec = new PBEKeySpec("password".toCharArray(), salt, 65536, 128);

        try {

            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();

            String stringHash = new BigInteger(1, hash).toString(16);
            hashFromStringEither = Either.right(stringHash);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            hashFromStringEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return hashFromStringEither;
    }

    /**
     * Generates the authentication token.
     * 
     * @param user, user information used to generate the token.
     * @return A new authentication token. 
     * @throws TokenGenerationException, exception thrown when there is an error generating the authentication token.
     */
    public static final Either<IException, String> getToken(User user) {

        Either<IException, String> tokenEither = null;

        if ((user != null) &&  !StringUtils.isBlank(user.getUsername()) && !StringUtils.isBlank(user.getPassword())) {

            long currentMillis = DateTime.now().getMillis();
            String baseForToken = currentMillis + user.getUsername() + user.getPassword();

            Either<IException, String> hashFromStringEither = getHashFromString(baseForToken);
            
            if (hashFromStringEither.isRight()) {
                
                String hashFromString = hashFromStringEither.right().value();
                String generatedToken = hashFromString.substring(0, TOKEN_CHARACTER_LIMIT);
                tokenEither = Either.right(generatedToken);
                
            } else {
                
                tokenEither = Either.left(ExceptionUtils.getIExceptionFromEither(hashFromStringEither));
            }

        } else {

            String errorMessage = "The user given as argument is not valid.";
            IException businessException = new BusinessException(errorMessage);
            tokenEither = Either.left(businessException);
        }
        
        return tokenEither;
    }
}