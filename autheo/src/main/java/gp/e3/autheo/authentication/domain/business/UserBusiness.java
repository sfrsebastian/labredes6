package gp.e3.autheo.authentication.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Citizen;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;
import gp.e3.autheo.authorization.persistence.daos.RoleDAO;
import gp.e3.autheo.infrastructure.clients.entities.SystemSettings;
import gp.e3.autheo.infrastructure.clients.rs.CustomerSettingsClient;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserBusiness {

    private final BasicDataSource dataSource;
    private final UserDAO userDao;
    private final RoleDAO roleDao;
    private final CustomerSettingsClient customerSettingsClient;
    
    /**
     * An object used to log.
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(UserBusiness.class);

    public UserBusiness(BasicDataSource dataSource, UserDAO userDao, RoleDAO roleDao,  CustomerSettingsClient customerSettingsClient) {

        this.dataSource = dataSource;
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.customerSettingsClient = customerSettingsClient;
        
    }

    public Either<IException, Boolean> createUser(User newUser) {

        Either<IException, Boolean> userWasCreatedEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            String originalPassword = newUser.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            String passwordSalt = PasswordHandler.getSaltFromHashedAndSaltedPassword(passwordHash);

            userWasCreatedEither = userDao.createUser(dbConnection, newUser, passwordHash, passwordSalt, null);

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {

            userWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasCreatedEither;
    }

    public Either<IException, Boolean> authenticateUser(String username, String password, String organizationId) {

        Either<IException, Boolean> isAuthenticatedEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, String> passwordHashEither = userDao.getPasswordByUsername(dbConnection, username, organizationId);

            if (passwordHashEither.isRight()) {

                String passwordHash = passwordHashEither.right().value();
                boolean isAuthenticated = PasswordHandler.validatePassword(password, passwordHash);
                isAuthenticatedEither = Either.right(isAuthenticated);

            } else {

                isAuthenticatedEither = Either.left(ExceptionUtils.getIExceptionFromEither(passwordHashEither));
            }

        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {

            isAuthenticatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return isAuthenticatedEither;
    }
    
    public Either<IException, User> getUserByUsername(String username, String organizationId) {

        Either<IException, User> userEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            userEither = userDao.getUserByUsername(dbConnection, username, organizationId);

        } catch (SQLException e) {

            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userEither;
    }

    public Either<IException, User> getUserByDocumentNumber(String documentNumber, String organizationId){
        Either<IException, User> userEither = null;
        try (Connection dbConnection = dataSource.getConnection()) {

            userEither = userDao.getUserByDocumentNumber(dbConnection, documentNumber, organizationId);

        } catch (SQLException e) {

            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        return userEither;
    }

    public Either<IException, List<User>> getUsersByRoleId(String roleId, String organization) {

        Either<IException, List<User>> userListEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            userListEither = userDao.getUsersByRoleId(dbConnection, roleId, organization);

        } catch (SQLException e) {

            userListEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userListEither;
    }


    public Either<IException, List<User>> getAllUsers() {

        Either<IException, List<User>> usersListEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            usersListEither = userDao.getAllUsers(dbConnection);

        } catch (SQLException e) {

            usersListEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return usersListEither;
    }

    public Either<IException, Boolean> updateUser(String username, User updatedUser) {

        Either<IException, Boolean> userWasUpdatedEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Integer> rowsAffectedEither = userDao.updateUser(dbConnection, username, updatedUser.getName(), updatedUser.getPassword(), updatedUser.getOrganizationId());

            if (rowsAffectedEither.isRight()) {

                Integer rowsAffected = rowsAffectedEither.right().value();
                boolean userWasUpdated = (rowsAffected == 1);
                userWasUpdatedEither = Either.right(userWasUpdated);

            } else {

                userWasUpdatedEither = Either.left(ExceptionUtils.getIExceptionFromEither(rowsAffectedEither));
            }

        } catch (SQLException e) {

            userWasUpdatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasUpdatedEither;
    }

    public Either<IException, Boolean> deleteUser(String username) {

        Either<IException, Boolean> userWasDeletedEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Integer> affectedRowsEither = userDao.deleteUser(dbConnection, username);

            if (affectedRowsEither.isRight()) {

                Integer affectedRows = affectedRowsEither.right().value();
                boolean userWasDeleted = (affectedRows == 1);
                userWasDeletedEither = Either.right(userWasDeleted);

            } else {

                userWasDeletedEither = Either.left(ExceptionUtils.getIExceptionFromEither(affectedRowsEither));
            }

        } catch (SQLException e) {

            userWasDeletedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasDeletedEither;
    }
    
    public Either<IException, Boolean> userDocumentExists(String documentId, String organizationId) {

        Either<IException, Boolean> existsUserEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Boolean> existsEither = userDao.userWithDocumentIdExists(dbConnection, documentId, organizationId);

            if (existsEither.isRight()) {

                boolean existsUser = existsEither.right().value();
                existsUserEither = Either.right(existsUser);

            } else {

                existsUserEither = Either.left(ExceptionUtils.getIExceptionFromEither(existsEither));
            }

        } catch (SQLException e) {

            existsUserEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return existsUserEither;
    }
    
    public Either<IException, Boolean> usernameExists(String username, String organizationId) {

        Either<IException, Boolean> existsUserEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Boolean> existsEither = userDao.userWithUsernameExists(dbConnection, username, organizationId);

            if (existsEither.isRight()) {

                boolean existsUser = existsEither.right().value();
                existsUserEither = Either.right(existsUser);

            } else {

                existsUserEither = Either.left(ExceptionUtils.getIExceptionFromEither(existsEither));
            }

        } catch (SQLException e) {

            existsUserEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return existsUserEither;
    }
    
    public Either<IException, User> getUserByEmail(String email, String organizationId) {

        Either<IException, User> emailExistsEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            emailExistsEither = userDao.getUserByEmail(dbConnection, email, organizationId);

        } catch (SQLException e) {

            emailExistsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return emailExistsEither;
    }
    
    public Either<IException,Boolean> createUserWithPhoto(User user, byte[] photo) {
        
        Either<IException, Boolean> userCreatedEither = null;
        
        try (Connection dbConnection = dataSource.getConnection()) {
            
            dbConnection.setAutoCommit(false);
            
            String originalPassword = user.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            String passwordSalt = PasswordHandler.getSaltFromHashedAndSaltedPassword(passwordHash);

            userCreatedEither = userDao.createUser(dbConnection, user, passwordHash, passwordSalt, photo);
            
            if(userCreatedEither.isRight()) {
                
                Either <IException, Integer > roleRelationCreatedEither = roleDao.addUserToRole(dbConnection, user.getUsername(), user.getBusinessRoleId(), user.getOrganizationId());
            
                if(roleRelationCreatedEither.isRight()) {
                    
                    dbConnection.commit();          
                        
                } else {
                    

                    dbConnection.rollback();
                    IException exeption = roleRelationCreatedEither.left().value();
                    LOGGER.error("UserBusiness :: createUserWithPhoto", exeption);
                    userCreatedEither = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_USER"));
                }
            }
        } catch (SQLException e) {

            userCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
            
        } catch (Exception e){
            
            userCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        return userCreatedEither;
    }
    
    public Either<IException,Boolean> createCitizen(Citizen citizen){
        
        Either<IException, Boolean> userCreatedEither = null;
        
        try (Connection dbConnection = dataSource.getConnection()) {
            
            dbConnection.setAutoCommit(false);
            
            String originalPassword = citizen.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            String passwordSalt = PasswordHandler.getSaltFromHashedAndSaltedPassword(passwordHash);

            
            Either<IException, String> systemEither = userDao.getSystemRoleId(dbConnection, "ciudadano", "ciudadano");
            
            if (systemEither.isRight()){
            
            	String systemRoleId = systemEither.right().value();
	            User user = new User(citizen.getName(), citizen.getDocumentType(), citizen.getDocumentNumber(), citizen.getEmail(), citizen.getUsername(), citizen.getTelephoneNumber(), 
	            		citizen.getAddress(), citizen.getPassword(), false, citizen.getOrganizationId(), systemRoleId, "ciudadano", "ciudadano");
	            
	            userCreatedEither = userDao.createUser(dbConnection, user, passwordHash, passwordSalt, null);
	            
	            if(userCreatedEither.isRight()) {
	                
	                Either <IException, Integer > roleRelationCreatedEither = roleDao.addUserToRole(dbConnection, user.getUsername(), user.getBusinessRoleId(), user.getOrganizationId());
	            
	                if(roleRelationCreatedEither.isRight()) {
	                    
	                    dbConnection.commit();          
	                        
	                } else {
	                    
	                    dbConnection.rollback();
	                    userCreatedEither = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_USER"));
	                }
	                
	            }
	            
            } else {
            	
            	IException value = systemEither.left().value();
            	System.out.println("error: "+ value.getErrorMessage());
            	userCreatedEither = Either.left(value);
            	
            }
            
        } catch (SQLException e) {

            userCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
            
        } catch (Exception e){
            
            userCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        return userCreatedEither;
    }
    
    
    public Either<IException,Boolean> createCitizen(User user){
        
        Either<IException, Boolean> userCreatedEither = null;
        
        try (Connection dbConnection = dataSource.getConnection()) {
            
            dbConnection.setAutoCommit(false);
            
            String originalPassword = user.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            String passwordSalt = PasswordHandler.getSaltFromHashedAndSaltedPassword(passwordHash);
            
            userCreatedEither = userDao.createUser(dbConnection, user, passwordHash, passwordSalt, null);
            
            if(userCreatedEither.isRight()) {
                
                Either <IException, Integer > roleRelationCreatedEither = roleDao.addUserToRole(dbConnection, user.getUsername(), user.getBusinessRoleId(), user.getOrganizationId());
            
                if(roleRelationCreatedEither.isRight()) {
                    
                    dbConnection.commit();          
                        
                } else {
                    
                    dbConnection.rollback();
                    userCreatedEither = Either.left( new TechnicalException("AUTHEO_ERROR_ON_CREATE_USER"));
                }
                
            }
        } catch (SQLException e) {

            userCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
            
        } catch (Exception e){
            
            userCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        return userCreatedEither;
    }
    
}