package gp.e3.autheo.authentication.persistence.mappers;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.token.ITokenSQLFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TokenMapper {

	private static Either<IException, Token> getTokenFromResultSet(ResultSet resultSet) {

	    Either<IException, Token> tokenEither = null;
	    
	    try {
	        
	        Token retrievedToken = new Token(resultSet.getString(ITokenSQLFactory.TOKEN_VALUE_FIELD), resultSet.getString(ITokenSQLFactory.USERNAME_FIELD),
                    resultSet.getString(ITokenSQLFactory.ORGANIZATION_ID_FIELD), resultSet.getString(ITokenSQLFactory.ROLE_ID_FIELD),
                    3, resultSet.getString(ITokenSQLFactory.SYSTEM_ROLE), null, null, null);
	        
	        tokenEither = Either.right(retrievedToken);
            
        } catch (SQLException e) {
            
            tokenEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

		return tokenEither;
	}
	
	public static Either<IException, Token> getSingleTokenFromResultSet(ResultSet resultSet) {
	    
	    Either<IException, Token> tokenEither = null;
	    
	    try {
	        
            if (resultSet.next()) {
                
                tokenEither = getTokenFromResultSet(resultSet);
                
            } else {
                
                IException businessException = new BusinessException("Token not found.");
                tokenEither = Either.left(businessException);
            }
            
        } catch (SQLException e) {
            
            tokenEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
	    
	    return tokenEither;
	}
	
	public static Either<IException, List<Token>> getMultipleTokensFromResultSet(ResultSet resultSet) {
	    
	    Either<IException, List<Token>> tokenListEither = null;
	    List<Token> tokenList = new ArrayList<Token>();
	    
	    try {
	        
	        Optional<IException> exceptionOptional = Optional.empty();
	        while (resultSet.next() && !exceptionOptional.isPresent()) {
	            
	            Either<IException, Token> tokenEither = getTokenFromResultSet(resultSet);
	            
	            if (tokenEither.isRight()) {
	                
	                Token token = tokenEither.right().value();
	                tokenList.add(token);
	                
	            } else {
	                
	                IException exception = tokenEither.left().value();
	                exceptionOptional = Optional.of(exception);
	            }
	        }
	        
	        if (!exceptionOptional.isPresent()) {
	            
	            tokenListEither = Either.right(tokenList);
	            
	        } else {
	            
	            tokenListEither = Either.left(exceptionOptional.get());
	        }
            
        } catch (SQLException e) {
            
            tokenListEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
	    
	    return tokenListEither;
	}
}