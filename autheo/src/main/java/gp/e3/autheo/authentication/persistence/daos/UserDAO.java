package gp.e3.autheo.authentication.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.user.IUserSQLFactory;
import gp.e3.autheo.authentication.persistence.mappers.UserMapper;
import gp.e3.autheo.infrastructure.exceptions.ExceptionCodes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO {

    private final IUserSQLFactory userSQLFactory;

    public UserDAO(IUserSQLFactory userSQLFactory) {

        this.userSQLFactory = userSQLFactory;
    }

    public Either<IException, Integer> createUsersTableIfNotExists(Connection dbConnection) {

        Either<IException, Integer> resultEither = null;
        String createUsersTableIfNotExistsSQL = userSQLFactory.getCreateUsersTableIfNotExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createUsersTableIfNotExistsSQL)) {

            int result = prepareStatement.executeUpdate();
            resultEither = Either.right(result);

        } catch (SQLException e) {

            resultEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return resultEither;
    }

    public Either<IException, Integer> countUsersTable(Connection dbConnection) {
        
        Either<IException, Integer> tableRowsEither = null;
        String countUsersTableSQL = userSQLFactory.getCountUsersTableSQL();

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(countUsersTableSQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            int tableRows = 0;
            
            if (resultSet.next()) {
                tableRows = resultSet.getInt(1);
            }

            resultSet.close();
            tableRowsEither = Either.right(tableRows);

        } catch (SQLException e) {
            
            tableRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableRowsEither;
    }

    public Either<IException, Boolean> createUser(Connection dbConnection, User user, String passwordHash, String passwordSalt, byte[] photo) {

        Either<IException, Boolean> userWasCreatedEither = null;
        String createUserSQL = userSQLFactory.getCreateUserSQL();

        try {

            PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getDocumentType());
            prepareStatement.setString(3, user.getDocumentNumber());
            prepareStatement.setString(4, user.getEmail().toLowerCase());
            prepareStatement.setString(5, user.getUsername().toLowerCase());
            prepareStatement.setString(6, passwordHash);
            prepareStatement.setString(7, passwordSalt);
            prepareStatement.setBoolean(8, user.isApiClient());
            prepareStatement.setString(9, user.getOrganizationId());
            prepareStatement.setInt(10, Integer.parseInt(user.getRoleId()));
            prepareStatement.setString(12, user.getTelephoneNumber());
            prepareStatement.setString(13, user.getAddress());
            
            if(photo != null) {
                
                prepareStatement.setBytes(11, photo);
                
            } else {
                
                prepareStatement.setBytes(11, null);
            }
                
            
            int affectedRows = prepareStatement.executeUpdate();
            prepareStatement.close();

            boolean userWasCreated = (affectedRows == 1);

            if (userWasCreated) {

                userWasCreatedEither = Either.right(userWasCreated);

            } else {

                String errorMessage = "Please check that the given user is valid.";
                IException businessException = new BusinessException(errorMessage);
                userWasCreatedEither = Either.left(businessException);
            }

        } catch (SQLException e) {

            e.printStackTrace();
            userWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasCreatedEither;
    };
    
    public Either<IException, Boolean> createUserSpecificRoleId(Connection dbConnection, User user, String passwordHash, String passwordSalt, int roleId, byte[] photo) {

        Either<IException, Boolean> userWasCreatedEither = null;
        String createUserSQL = userSQLFactory.getCreateUserSQL();

        try {

            PreparedStatement prepareStatement = dbConnection.prepareStatement(createUserSQL);
            prepareStatement.setString(1, user.getName());
            prepareStatement.setString(2, user.getDocumentType());
            prepareStatement.setString(3, user.getDocumentNumber());
            prepareStatement.setString(4, user.getEmail().toLowerCase());
            prepareStatement.setString(5, user.getUsername().toLowerCase());
            prepareStatement.setString(6, passwordHash);
            prepareStatement.setString(7, passwordSalt);
            prepareStatement.setBoolean(8, user.isApiClient());
            prepareStatement.setString(9, user.getOrganizationId());
            prepareStatement.setInt(10, roleId);
           
            if(photo != null){
                prepareStatement.setBytes(11, photo);
            }else{
                prepareStatement.setBytes(11, null);
            }
            
            prepareStatement.setString(12, user.getTelephoneNumber());
            prepareStatement.setString(13, user.getAddress());
                
            
            int affectedRows = prepareStatement.executeUpdate();
            prepareStatement.close();

            boolean userWasCreated = (affectedRows == 1);

            if (userWasCreated) {

                userWasCreatedEither = Either.right(userWasCreated);

            } else {

                String errorMessage = "Please check that the given user is valid.";
                IException businessException = new BusinessException(errorMessage);
                userWasCreatedEither = Either.left(businessException);
            }

        } catch (SQLException e) {
        	e.printStackTrace();
            userWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasCreatedEither;
    };

    public Either<IException, User> getUserByUsername(Connection dbConnection, String username, String organizationId) {

        Either<IException, User> userEither = null;
        String getUserByUsernameSQL = userSQLFactory.getGetUserByUsernameSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByUsernameSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            prepareStatement.setString(2, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();

            userEither = UserMapper.getSingleUserFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userEither;
    }
    
    public Either<IException, User> getUserByDocumentNumber(Connection dbConnection, String documentNumber, String organizationId){
        
        Either<IException, User> userEither = null;
        String getUserByUsernameSQL = userSQLFactory.getGetUserByDocumentNumberSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByUsernameSQL)) {

            prepareStatement.setString(1, documentNumber);
            prepareStatement.setString(2, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();

            userEither = UserMapper.getSingleUserFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userEither;
    }

    public Either<IException, List<User>> getUsersByRoleId(Connection dbConnection, String roleId, String organization) {

        Either<IException, List<User>> userListEither = null;
        String getUserByUsernameSQL = userSQLFactory.getGetUsersByRoleIdSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getUserByUsernameSQL)) {
            
            prepareStatement.setString(1, roleId);
            prepareStatement.setString(2, organization);
            ResultSet resultSet = prepareStatement.executeQuery();
            
            userListEither = UserMapper.getUsersByRoleIdFromResultSet(resultSet);
            resultSet.close();
            
        } catch (SQLException e) {
            
            userListEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userListEither;
    }

    public Either<IException, List<User>> getAllUsers(Connection dbConnection) {

        Either<IException, List<User>> allUsersEither = null;
        String getAllUsersSQL = userSQLFactory.getGetAllUsersSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getAllUsersSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            allUsersEither = UserMapper.getMultipleUsersFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            allUsersEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return allUsersEither;
    }

    public Either<IException, String> getPasswordByUsername(Connection dbConnection, String username, String organizationId) {

        Either<IException, String> passwordEither = null;

        String getPasswordByUsernameSQL = userSQLFactory.getGetPasswordByUsernameSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getPasswordByUsernameSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            prepareStatement.setString(2, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();

            if (resultSet.next()) {

                String password = resultSet.getString(IUserSQLFactory.PASSWORD_FIELD);
                passwordEither = Either.right(password);
                
            } else {
                
                IException businessException = new BusinessException(ExceptionCodes.AUTHEO_GET_USER_EXCEPTION);
                passwordEither = Either.left(businessException);
            }

            resultSet.close();

        } catch (SQLException e) {

            passwordEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return passwordEither;
    }

    public Either<IException, Integer> updateUser(Connection dbConnection, String username, String updatedName, String updatedPassword, String organizationId) {

        Either<IException, Integer> affectedRowsEither = null;
        int affectedRows = 0;
        String updateUserSQL = userSQLFactory.getUpdateUserSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(updateUserSQL)) {

            prepareStatement.setString(1, updatedName.toLowerCase());
            prepareStatement.setString(2, updatedPassword);
            prepareStatement.setString(3, username.toLowerCase());
            prepareStatement.setString(4, organizationId);

            affectedRows = prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(affectedRows);

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }
    
    public Either<IException, Integer> updateUserPassword(Connection dbConnection, String username, String newPassword, String newSalt, String organizationId) {

        Either<IException, Integer> affectedRowsEither = null;
        int affectedRows = 0;
        String updateUserSQL = userSQLFactory.getUpdateUserPasswordSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(updateUserSQL)) {

            prepareStatement.setString(1, newPassword);
            prepareStatement.setString(2, newSalt);
            prepareStatement.setString(3, username.toLowerCase());
            prepareStatement.setString(4, organizationId);

            affectedRows = prepareStatement.executeUpdate();
            if(affectedRows == 1) {
                
                affectedRowsEither = Either.right(affectedRows);
                
            } else {
                
                BusinessException exception = new BusinessException(ExceptionCodes.AUTHEO_UPDATE_USER_PASSWORD_EXCEPTION);
                affectedRowsEither = Either.left(exception);
                
            }

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }

    public Either<IException, Integer> deleteUser(Connection dbConnection, String username) {

        Either<IException, Integer> affectedRowsEither = null;
        int affectedRows = 0;
        String deleteUserSQL = userSQLFactory.getDeleteUserSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(deleteUserSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            affectedRows = prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(affectedRows);

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }
    
    public Either<IException, Boolean> userWithDocumentIdExists(Connection dbConnection, String documentId,  String organizationId) {

        Either<IException, Boolean> existsEither = null;
        String existsUserSQL = userSQLFactory.getUserWithDocumentIdExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(existsUserSQL)) {

            prepareStatement.setString(1, documentId);
            prepareStatement.setString(2, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();

            if(resultSet.next()) {
            	
                existsEither = Either.right(true);
                
            } else {
            	
                existsEither = Either.right(false);
            }

            resultSet.close();

        } catch (SQLException e) {

        	existsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        	
        }
        return existsEither;
    }
    
    public Either<IException, Boolean> userWithUsernameExists(Connection dbConnection, String username,  String organizationId) {

        Either<IException, Boolean> existsEither = null;
        String existsUserSQL = userSQLFactory.getUserWithUsernameExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(existsUserSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            prepareStatement.setString(2, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();
            
            if(resultSet.next()) {
                
                existsEither = Either.right(true);
            } else {
                
                existsEither = Either.right(false);
            }
            
            resultSet.close();

        } catch (SQLException e) {

            existsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return existsEither;
    }
    
    public Either<IException, User> getUserByEmail(Connection dbConnection, String email,  String organizationId) {


        Either<IException, User> userEither = null;
        String existsUserSQL = userSQLFactory.getUserByEmailSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(existsUserSQL)) {

            prepareStatement.setString(1, email.toLowerCase());
            prepareStatement.setString(2, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();
            
            userEither = UserMapper.getSingleUserFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userEither;
    }
    
    public Either<IException, String> getSystemRoleId (Connection dbConnection, String systemRole,  String businessRole) {


        Either<IException, String> userEither = null;
        String existsUserSQL = userSQLFactory.getSystemBusinessRoleId();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(existsUserSQL)) {

            prepareStatement.setString(1, systemRole);
            prepareStatement.setString(2, businessRole);
            ResultSet resultSet = prepareStatement.executeQuery();
            
            while (resultSet.next()) {

            	userEither = Either.right(resultSet.getString(1));
				
			}
            
            resultSet.close();

        } catch (SQLException e) {

            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userEither;
    }
}