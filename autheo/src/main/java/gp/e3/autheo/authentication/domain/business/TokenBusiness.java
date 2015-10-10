package gp.e3.autheo.authentication.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.daos.TokenCacheDAO;
import gp.e3.autheo.authentication.persistence.daos.TokenDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang.StringUtils;

public class TokenBusiness {

    public static final String INTERNAL_API_CLIENT_ROLE = "module";

    private final BasicDataSource dataSource;
    private final TokenDAO tokenDAO;
    private final TokenCacheDAO tokenCacheDao;
    
    public TokenBusiness(BasicDataSource basicDataSource, TokenDAO tokenDAO, TokenCacheDAO tokenCacheDao) {

        this.dataSource = basicDataSource;
        this.tokenDAO = tokenDAO;
        this.tokenCacheDao = tokenCacheDao;

        try (Connection dbConnection = dataSource.getConnection()) {

            this.tokenDAO.createTokensTableIfNotExists(dbConnection);
            updateTokensCache();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private void addAllTokensToCache(List<Token> allTokens) {

        for (Token tokenFromDb : allTokens) {

            tokenCacheDao.addTokenUsingTokenValueAsKey(tokenFromDb);

            if (tokenFromDb.getTokenType() == TokenTypes.INTERNAL_API_TOKEN_TYPE.getTypeNumber()) {
                tokenCacheDao.addTokenUsingOrganizationAsKey(tokenFromDb);
            }
        }
    }

    public Either<IException, Boolean> updateTokensCache() {

        Either<IException, Boolean> tokensCacheWasUpdatedEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, List<Token>> allTokensEither = tokenDAO.getAllTokens(dbConnection);

            if (allTokensEither.isRight()) {

                List<Token> allTokens = allTokensEither.right().value();
                addAllTokensToCache(allTokens);
                tokensCacheWasUpdatedEither = Either.right(true);

            } else {

                tokensCacheWasUpdatedEither = Either.left(ExceptionUtils.getIExceptionFromEither(allTokensEither));
            }

        } catch (SQLException e) {

            tokensCacheWasUpdatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tokensCacheWasUpdatedEither;
    }

    private Either<IException, Token> generateRandomTokenFromUserInfo(User user, int tokenType) {

        Either<IException, Token> tokenEither = null;

        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);

        if (tokenValueEither.isRight()) {

            String tokenValue = tokenValueEither.right().value();
            Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), tokenType, user.getSystemRoleId(), user.getBusinessRoleId(), 
                    user.getDocumentType(), user.getDocumentNumber());
            tokenEither = Either.right(token);

        } else {

            tokenEither = Either.left(ExceptionUtils.getIExceptionFromEither(tokenValueEither));
        }

        return tokenEither;
    }

    private Either<IException, Token> generateRandomTokenFromUserInfo(User user, String roleId, int tokenType) {

        Either<IException, Token> tokenEither = null;
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);

        if (tokenValueEither.isRight()) {

            String tokenValue = tokenValueEither.right().value();
            Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), roleId, tokenType, user.getSystemRoleId(), user.getBusinessRoleId(),
                    user.getDocumentType(), user.getDocumentNumber());
            tokenEither = Either.right(token);

        } else {

            tokenEither = Either.left(ExceptionUtils.getIExceptionFromEither(tokenValueEither));
        }

        return tokenEither;
    }

    private Either<IException, Token> generateSaveAndGetAPIToken(BasicDataSource dataSource, User user) {

        Either<IException, Token> apiTokenEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            apiTokenEither = generateRandomTokenFromUserInfo(user, TokenTypes.API_KEY_TOKEN_TYPE.getTypeNumber());

            if (apiTokenEither.isRight()) {

                Token apiToken = apiTokenEither.right().value();
                tokenDAO.createToken(dbConnection, apiToken);
                tokenCacheDao.addTokenUsingTokenValueAsKey(apiToken);
            }

        } catch (SQLException e) {

            apiTokenEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return apiTokenEither;
    }

    private Either<IException, Token> generateSaveAndGetInternalAPIToken(BasicDataSource dataSource, User user) {

        Either<IException, Token> internalAPITokenEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            internalAPITokenEither = generateRandomTokenFromUserInfo(user, INTERNAL_API_CLIENT_ROLE, TokenTypes.INTERNAL_API_TOKEN_TYPE.getTypeNumber());

            if (internalAPITokenEither.isRight()) {

                Token internalAPIToken = internalAPITokenEither.right().value();

                tokenDAO.createToken(dbConnection, internalAPIToken);
                tokenCacheDao.addTokenUsingTokenValueAsKey(internalAPIToken);
                tokenCacheDao.addTokenUsingOrganizationAsKey(internalAPIToken);
            }

        } catch (SQLException e) {

            internalAPITokenEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return internalAPITokenEither;
    }

    public Either<IException, Boolean> generateAndSaveTokensForAnAPIUser(User user) {

        Either<IException, Boolean> tokensWereGeneratedAndSavedEither = null;

        if (User.isAValidUser(user) && user.isApiClient()) {

            Either<IException, Token> apiTokenEither = generateSaveAndGetAPIToken(dataSource, user);
            Either<IException, Token> internalAPITokenEither = generateSaveAndGetInternalAPIToken(dataSource, user);
            boolean tokensWereGeneratedAndSaved = (apiTokenEither.isRight() && internalAPITokenEither.isRight());

            if (tokensWereGeneratedAndSaved) {

                tokensWereGeneratedAndSavedEither = Either.right(tokensWereGeneratedAndSaved);

            } else {

                List<Either<IException, ?>> eitherList = Arrays.asList(apiTokenEither, internalAPITokenEither);
                Optional<Either<IException, ?>> firstLeftEither = ExceptionUtils.getFirstLeftEither(eitherList);
                tokensWereGeneratedAndSavedEither = Either.left(ExceptionUtils.getIExceptionFromEither(firstLeftEither));
            }
            
        } else {
            
            if(user != null && !user.isApiClient() ) {
                
                tokensWereGeneratedAndSavedEither = Either.right(true);
                
            } else {
                
                IException businessException = new BusinessException("Please verify the given user is valid.");
                tokensWereGeneratedAndSavedEither = Either.left(businessException);        
            }   
        }

        return tokensWereGeneratedAndSavedEither;
    }

    public Either<IException, Token> generateToken(User user) {

        Either<IException, Token> temporalTokenEither = null;

        if (User.isAValidUser(user)) {

            // Generate temporal token
            temporalTokenEither = generateRandomTokenFromUserInfo(user, TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber());

            if (temporalTokenEither.isRight()) {

                Token temporalToken = temporalTokenEither.right().value();
                tokenCacheDao.addTokenUsingTokenValueAsKey(temporalToken);
                temporalToken.setImage(user.getImage());
            }

        } else {

            String errorMessage = "The user given as argument is invalid.";
            IException businessException = new BusinessException(errorMessage);
            temporalTokenEither = Either.left(businessException);
        }

        return temporalTokenEither;
    }

    public Either<IException, Token> getAPIToken(String tokenValue) {

        Either<IException, Token> tokenEither = null;

        if (!StringUtils.isBlank(tokenValue)) {

            tokenEither = tokenCacheDao.getTokenByTokenValue(tokenValue);

            if (tokenEither.isLeft()) {

                updateTokensCache();
                tokenEither = tokenCacheDao.getTokenByTokenValue(tokenValue);
            }

        } else {

            String errorMessage = "The given token value is null or empty.";
            IException businessException = new BusinessException(errorMessage);
            tokenEither = Either.left(businessException);
        }

        return tokenEither;
    }

    public Either<IException, Token> getModuleToken(String userOrganizationId) {

        Either<IException, Token> moduleTokenEither = null;

        if (!StringUtils.isBlank(userOrganizationId)) {

            moduleTokenEither = tokenCacheDao.getTokenByOrganization(userOrganizationId);

            if (moduleTokenEither.isLeft()) {

                updateTokensCache();
                moduleTokenEither = tokenCacheDao.getTokenByOrganization(userOrganizationId);
            }
            
        } else {
            
            String message = "The given userOrganizationId is not valid.";
            IException businessException = new BusinessException(message);
            moduleTokenEither = Either.left(businessException);
        }

        return moduleTokenEither;
    }

    public Either<IException, Boolean> removeUserAccessToken(String tokenValue) {

        return tokenCacheDao.removeUserAccessToken(tokenValue);
    }
}
