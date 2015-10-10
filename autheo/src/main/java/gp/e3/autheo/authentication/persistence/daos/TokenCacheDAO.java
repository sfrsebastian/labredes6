package gp.e3.autheo.authentication.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;

import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class TokenCacheDAO {

    // See: http://tool.oschina.net/uploads/apidocs/jedis-2.1.0/index.html?redis/clients/jedis/Jedis.html get(String key) method.
    public static final String NIL = "nil";

    public static final String OK = "OK";
    public static final String NOK = "NOK";

    private JedisPool redisPool;

    public TokenCacheDAO(JedisPool jedisPool) {

        this.redisPool = jedisPool;
    }

    private Jedis getRedisClient() {
        return redisPool.getResource();
    }

    private void returnResource(Jedis jedis){
        redisPool.returnResource(jedis);
    }

    public boolean addTokenUsingTokenValueAsKey(Token token) {

        String answer = NOK;

        if (Token.isAValidToken(token)) {

            Jedis redisClient = getRedisClient();
            answer = redisClient.set(token.getTokenValue(), token.toString());
            returnResource(redisClient);
        }

        return answer.equalsIgnoreCase(OK);
    }

    private Either<IException, Token> buildTokenFromTokenAsString(String tokenValue, String tokenToString) {

        Either<IException, Token> tokenEither = null;

        if (!StringUtils.isBlank(tokenToString) && !tokenToString.equalsIgnoreCase(NIL)) {

            Token token = Token.buildTokenFromTokenToString(tokenToString);
            tokenEither = Either.right(token);

        } else {

            String errorMessage = "There is no token for the token value " + tokenValue;
            IException businessException = new BusinessException(errorMessage);
            tokenEither = Either.left(businessException);
        }

        return tokenEither;
    }

    public Either<IException, Token> getTokenByTokenValue(String tokenValue) {

        Either<IException, Token> tokenEither = null;

        if (!StringUtils.isBlank(tokenValue)) {

            Jedis redisClient = getRedisClient();
            String tokenToString = redisClient.get(tokenValue);
            returnResource(redisClient);
            tokenEither = buildTokenFromTokenAsString(tokenValue, tokenToString);

        } else {

            String errorMessage = "The given token value is null or empty.";
            IException businessException = new BusinessException(errorMessage);
            tokenEither = Either.left(businessException);
        }

        return tokenEither;
    }

    public boolean addTokenUsingOrganizationAsKey(Token token) {

        String answer = NOK;

        if (Token.isAValidToken(token)) {

            Jedis redisClient = getRedisClient();
            answer = redisClient.set(token.getUserOrganization(), token.toString());
            returnResource(redisClient);
        }

        return answer.equalsIgnoreCase(OK);
    }

    public Either<IException, Token> getTokenByOrganization(String userOrganization) {

        Either<IException, Token> tokenEither = null;

        if (!StringUtils.isBlank(userOrganization)) {

            Jedis redisClient = getRedisClient();
            String tokenToString = redisClient.get(userOrganization);
            returnResource(redisClient);

            if (!StringUtils.isBlank(tokenToString) && !tokenToString.equalsIgnoreCase(NIL)) {

                Token token = Token.buildTokenFromTokenToString(tokenToString);
                tokenEither = Either.right(token);
            }

        } else {

            String errorMessage = "There is no token for the organization " + userOrganization;
            IException businessException = new BusinessException(errorMessage);
            tokenEither = Either.left(businessException);
        }

        return tokenEither;
    }

    public Either<IException, Boolean> removeUserAccessToken(String tokenValue) {

        Either<IException, Boolean> tokenWasRemovedEither = null;

        long keysRemoved = 0;

        if(!StringUtils.isBlank(tokenValue)) {

            Jedis redisClient = getRedisClient();
            keysRemoved = redisClient.del(tokenValue);
            returnResource(redisClient);

            boolean tokenWasRemoved = (keysRemoved == 1);
            tokenWasRemovedEither = Either.right(tokenWasRemoved);

        } else {

            String errorMessage = "The given token value is null or empty.";
            IException businessException = new BusinessException(errorMessage);
            tokenWasRemovedEither = Either.left(businessException);
        }

        return tokenWasRemovedEither;
    }
}