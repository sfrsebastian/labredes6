package gp.e3.autheo.authentication.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.PasswordToken;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.passwordtoken.IPasswordTokenSQLFactory;
import gp.e3.autheo.infrastructure.exceptions.ExceptionCodes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class PasswordTokenDAO {

    private final IPasswordTokenSQLFactory passwordTokenSQLFactory;

    public PasswordTokenDAO(IPasswordTokenSQLFactory passwordTokenSQLFactory) {

        this.passwordTokenSQLFactory = passwordTokenSQLFactory;
    }

    public Either<IException, Boolean> createPasswordTokensTableIfNotExists(Connection dbConnection) {

        Either<IException, Boolean> tableWasCreatedEither = null;
        String createPasswordTokensTableIfNotExistsSQL = passwordTokenSQLFactory.getCreatePasswordTokensTableIfNotExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createPasswordTokensTableIfNotExistsSQL)) {

            prepareStatement.executeUpdate();
            tableWasCreatedEither = Either.right(true);

        } catch (SQLException e) {

            tableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableWasCreatedEither;
    }

    public Either<IException, Integer> createPasswordToken(Connection dbConnection, PasswordToken passwordToken) {

        Either<IException, Integer> rowsAffectedEither = null;
        String createPasswordTokenSQL = passwordTokenSQLFactory.getCreatePasswordTokenSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createPasswordTokenSQL)) {

            prepareStatement.setString(1, passwordToken.getTokenValue());
            prepareStatement.setString(2, passwordToken.getUsername());
            prepareStatement.setString(3, passwordToken.getUserOrganization());
            
            Timestamp requestDate = localDateTimeToTimeStamp(passwordToken.getRequestDate());
            prepareStatement.setTimestamp(4, requestDate);

            int rowsAffected = prepareStatement.executeUpdate();
            
            if(rowsAffected == 1) {
                
                rowsAffectedEither = Either.right(rowsAffected);
                
            } else {
                
                BusinessException exception = new BusinessException(ExceptionCodes.AUTHEO_CREATE_PASSWORD_TOKEN_EXCEPTION);
                rowsAffectedEither = Either.left(exception);
                
            }
            

        } catch (SQLException e) {

            rowsAffectedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rowsAffectedEither;
    }
    
    public Either<IException, String> getUsernameByPasswordToken(Connection dbConnection, String passwordToken, String organizationId) {

        Either<IException, String> usernameEither = null;
        String queryUsernameByPasswordToken = passwordTokenSQLFactory.getQueryUsernameByPasswordToken();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(queryUsernameByPasswordToken)) {

            prepareStatement.setString(1, passwordToken);
            prepareStatement.setString(2, organizationId);

            ResultSet resultSet = prepareStatement.executeQuery();
            
            if(resultSet.next()) {
                
                usernameEither = Either.right(resultSet.getString(1));
                
            } else {
                
                BusinessException exception = new BusinessException(ExceptionCodes.AUTHEO_GET_USERNAME_BY_PASSWORD_TOKEN_EXCEPTION);
                usernameEither = Either.left(exception);
                
            }
            

        } catch (SQLException e) {

            usernameEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return usernameEither;
    }
    
    public Either<IException, String> getPasswordTokenByUsername(Connection dbConnection, String username, String organizationId) {

        Either<IException, String> usernameEither = null;
        String queryUsernameByPasswordToken = passwordTokenSQLFactory.getQueryPasswordTokenByUsername();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(queryUsernameByPasswordToken)) {

            prepareStatement.setString(1, username.toLowerCase());
            prepareStatement.setString(2, organizationId);

            ResultSet resultSet = prepareStatement.executeQuery();
            
            if(resultSet.next()) {
                
                usernameEither = Either.right(resultSet.getString(1));
                
            } else {
                
                BusinessException exception = new BusinessException(ExceptionCodes.AUTHEO_GET_PASSWORD_TOKEN_BY_USERNAME_EXCEPTION);
                usernameEither = Either.left(exception);
                
            }
            

        } catch (SQLException e) {

            usernameEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return usernameEither;
    }
    
    public Either<IException, Integer> deletePasswordToken(Connection dbConnection, String passwordToken, String organizationId) {

        Either<IException, Integer> rowsAffectedEither = null;
        String deletePasswordTokenSQL = passwordTokenSQLFactory.getDeletePasswordTokenSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(deletePasswordTokenSQL)) {

            prepareStatement.setString(1, passwordToken);
            prepareStatement.setString(2, organizationId);
            
            int rowsAffected = prepareStatement.executeUpdate();
            
            if(rowsAffected == 1) {
                
                rowsAffectedEither = Either.right(rowsAffected);
                
            } else {
                
                BusinessException exception = new BusinessException(ExceptionCodes.AUTHEO_DELETE_PASSWORD_TOKEN_EXCEPTION);
                rowsAffectedEither = Either.left(exception);
                
            }

        } catch (SQLException e) {

            rowsAffectedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rowsAffectedEither;
    }
    
    //////////////////
    // Private Methods
    //////////////////
    
    private Timestamp localDateTimeToTimeStamp(LocalDateTime date) {
        
        Timestamp timestamp = null;
        
        if (date != null) {
            
            timestamp = Timestamp.valueOf(date);
        } 
        
        return timestamp;
    }

}