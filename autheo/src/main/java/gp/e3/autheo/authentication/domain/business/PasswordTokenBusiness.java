package gp.e3.autheo.authentication.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.PasswordToken;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.infrastructure.utils.email.EmailUtil;
import gp.e3.autheo.authentication.infrastructure.utils.email.RecoverPasswordConstants;
import gp.e3.autheo.authentication.persistence.daos.PasswordTokenDAO;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.RandomStringUtils;

public class PasswordTokenBusiness {
    
    ////////////////////////
    // Attributes
    ////////////////////////

    private final BasicDataSource dataSource;
    
    private final PasswordTokenDAO passwordTokenDAO;
    
    private final UserDAO userDao;
    
    ////////////////////////
    // Constructor
    ////////////////////////
    
    public PasswordTokenBusiness(BasicDataSource basicDataSource, PasswordTokenDAO passwordTokenDAO, UserDAO userDao) {

        this.dataSource = basicDataSource;
        this.passwordTokenDAO = passwordTokenDAO;
        this.userDao = userDao;

        try (Connection dbConnection = dataSource.getConnection()) {

            this.passwordTokenDAO.createPasswordTokensTableIfNotExists(dbConnection);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public Either<IException, Boolean> generatePasswordToken(String email, String organizationId, boolean isCitizen) {

        Either<IException, Boolean> generatePasswordTokenEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            //Get the user of the given email
            Either<IException,User> userEither = userDao.getUserByEmail(dbConnection, email, organizationId);
            
            if(userEither.isRight()) {
                
                User user = userEither.right().value();
                
                Either<IException, String> passwordTokenByUsername = passwordTokenDAO.getPasswordTokenByUsername(dbConnection, user.getUsername(), organizationId);
                
                if(passwordTokenByUsername.isLeft()) {
                    
                    IException exception = passwordTokenByUsername.left().value();
                    
                    //If there was a technical exception then it will return the exception
                    if(exception instanceof TechnicalException) {
                        
                        generatePasswordTokenEither = Either.left(exception);
                        
                    } else { //If the error is business, this means the password token doesn't exist and one can continue normally
                        
                        generatePasswordTokenEither = createAndSavePasswordToken(organizationId, isCitizen, dbConnection, user);
                        
                    }
                    
                    
                } else {//If the token exists then it will delete it and then it will continue normaly
                    
                    String passwordToken = passwordTokenByUsername.right().value();
                    Either<IException, Integer> passwordTokenDeletedEither = passwordTokenDAO.deletePasswordToken(dbConnection, passwordToken, organizationId);
                    
                    if(passwordTokenDeletedEither.isRight()) {
                        
                        generatePasswordTokenEither = createAndSavePasswordToken(organizationId, isCitizen, dbConnection, user);
                        
                    } else {
                        
                        IException exception = passwordTokenDeletedEither.left().value();
                        generatePasswordTokenEither = Either.left(exception);
                        
                    }
                    
                }
            
            } else {
                
                IException exception = userEither.left().value();
                generatePasswordTokenEither = Either.left(exception);
                
            }
            
        } catch (SQLException e) {
            
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            generatePasswordTokenEither = Either.left(technicalException);
            
        }

        return generatePasswordTokenEither;
    }

    public Either<IException, Boolean> changePasswordGivenPasswordToken(String passwordToken, String newPassword, String organizationId) {
        
        Either<IException, Boolean> eitherResult = null;

        try (Connection dbConnection = dataSource.getConnection()) {
            
            dbConnection.setAutoCommit(false);
            
            Either<IException, String> usernameByPasswordTokenEither = passwordTokenDAO.getUsernameByPasswordToken(dbConnection, passwordToken, organizationId);
            
            if(usernameByPasswordTokenEither.isRight()) {
                
                String username = usernameByPasswordTokenEither.right().value();
                
                String passwordHash = PasswordHandler.getPasswordHash(newPassword);
                String passwordSalt = PasswordHandler.getSaltFromHashedAndSaltedPassword(passwordHash);
                
                Either<IException, Integer> updateUserEither = userDao.updateUserPassword(dbConnection, username, passwordHash, passwordSalt, organizationId);
                
                if(updateUserEither.isRight()) {
                    
                    Either<IException, Integer> passwordTokenDeletedEither = passwordTokenDAO.deletePasswordToken(dbConnection, passwordToken, organizationId);
                    
                    if(passwordTokenDeletedEither.isRight()) {
                        
                        dbConnection.commit();
                        eitherResult = Either.right(true);
                        
                    } else {
                        
                        dbConnection.rollback();
                        eitherResult = Either.left(passwordTokenDeletedEither.left().value());
                        
                    }
                    
                } else {
                    
                    eitherResult = Either.left(updateUserEither.left().value());
                    
                }
                
            } else {
                
                eitherResult = Either.left(usernameByPasswordTokenEither.left().value());
                
            }
            
        } catch (SQLException e) {
            
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            eitherResult = Either.left(technicalException);
            
        } catch (NoSuchAlgorithmException e) {
            
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            eitherResult = Either.left(technicalException);
            
        } catch (InvalidKeySpecException e) {
            
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            eitherResult = Either.left(technicalException);
            
        } catch (IllegalArgumentException e) {
            
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            eitherResult = Either.left(technicalException);
            
        }

        return eitherResult;
        
    }
    
    ////////////////////////
    // Private Methods
    ////////////////////////

    private String generateRandomPasswordToken() {

        return RandomStringUtils.randomAlphanumeric(45).toLowerCase();
    }
    
    private Either<IException, Boolean> createAndSavePasswordToken(String organizationId, boolean isCitizen, Connection dbConnection,
            User user) {
        
        Either<IException, Boolean> generatePasswordTokenEither;
        //Generate a random token
        String randomPasswordToken = generateRandomPasswordToken();
         
        //Save the new password token
        PasswordToken passwordToken = new PasswordToken(randomPasswordToken, user.getUsername(), organizationId, LocalDateTime.now());
        
        Either<IException, Integer> passwordTokenCreatedEither = passwordTokenDAO.createPasswordToken(dbConnection, passwordToken);
        
        if(passwordTokenCreatedEither.isRight()) {
            
            //Sends email so that the user can change the password
            //TODO the from email needs to be defined
            
            String passwordRecoveryMessage = isCitizen ? RecoverPasswordConstants.getPasswordRecoveryMessageForCitizen(randomPasswordToken) : 
                                                RecoverPasswordConstants.getPasswordRecoveryMessageForFunctionary(randomPasswordToken);
            Either<IException, Boolean> emailSentEither = EmailUtil.sendEmail(user.getEmail(), "steven.rigby@certicamara.com", RecoverPasswordConstants.RECOVER_PASSWORD_SUBJECT, 
                                passwordRecoveryMessage);
            
            if(emailSentEither.isRight()) {
                
                //We always send true so that no attacker can find out if the email is registered or not
                generatePasswordTokenEither = Either.right(true);
                
            } else {
                
                IException exception = emailSentEither.left().value();
                generatePasswordTokenEither = Either.left(exception);
                
            }
            
        } else {
            
            IException exception = passwordTokenCreatedEither.left().value();
            generatePasswordTokenEither = Either.left(exception);
            
        }
        
        return generatePasswordTokenEither;
    }
    
}
