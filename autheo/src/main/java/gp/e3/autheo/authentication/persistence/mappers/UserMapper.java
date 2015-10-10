package gp.e3.autheo.authentication.persistence.mappers;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.google.common.io.ByteStreams;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.user.IUserSQLFactory;
import gp.e3.autheo.infrastructure.exceptions.ExceptionCodes;

public class UserMapper {

	private static Either<IException, User> getUserFromResultSet(ResultSet resultSet) {

	    Either<IException, User> userEither = null;

		try {

			String name = resultSet.getString(IUserSQLFactory.NAME_FIELD);
			String documentType = resultSet.getString(IUserSQLFactory.DOCUMENT_TYPE_FIELD);
			String documentNumber = resultSet.getString(IUserSQLFactory.DOCUMENT_NUMBER_FIELD);
			String email = resultSet.getString(IUserSQLFactory.EMAIL_FIELD);
			String telephoneNumber = resultSet.getString(IUserSQLFactory.TELEPHONE_NUMBER_FIELD);
			String address = resultSet.getString(IUserSQLFactory.ADDRESS_FIELD);
			String username = resultSet.getString(IUserSQLFactory.USERNAME_FIELD);
			String roleId = resultSet.getString(IUserSQLFactory.ROLE_ID_FIELD);
			String organizacionId = resultSet.getString(IUserSQLFactory.ORGANIZATION_ID_FIELD);
			String systemRoleId = resultSet.getString(IUserSQLFactory.SYSTEM_ROLE_FIELD);
			String businessRoleId = resultSet.getString(IUserSQLFactory.BUSINESS_ROLE_FIELD);
			String password = resultSet.getString(IUserSQLFactory.PASSWORD_FIELD);
			InputStream imageStream = resultSet.getBinaryStream(IUserSQLFactory.IMAGE_FIELD);

			byte[] image = null;
			if(imageStream != null){
	            image = ByteStreams.toByteArray(imageStream);   
			}
			User user = new User(name, documentType, documentNumber, email, username, telephoneNumber, address, password, false, organizacionId, roleId, systemRoleId, businessRoleId);
			user.setImage(image);

			userEither = Either.right(user);

		} catch (SQLException e) {

		    e.printStackTrace();
			userEither = Either.left(ExceptionFactory.getTechnicalException(e));
		} catch (IOException e) {
            
            e.printStackTrace();
            userEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

		return userEither;
	}
	
	public static Either<IException, List<User>> getUsersByRoleIdFromResultSet(ResultSet resultSet) {

	    Either<IException, List<User>> userListEither = null;
		List<User> userList = new ArrayList<User>();

		try {

			while (resultSet.next()) {
				
				String name = resultSet.getString(IUserSQLFactory.NAME_FIELD);
				String email = resultSet.getString(IUserSQLFactory.EMAIL_FIELD);
				String telephoneNumber = resultSet.getString(IUserSQLFactory.TELEPHONE_NUMBER_FIELD);
	            String address = resultSet.getString(IUserSQLFactory.ADDRESS_FIELD);
				String username = resultSet.getString(IUserSQLFactory.USERNAME_FIELD);
				String roleId = resultSet.getString(IUserSQLFactory.ROLE_ID_FIELD);
				String systemRole = resultSet.getString(IUserSQLFactory.SYSTEM_ROLE_FIELD);
				String businessRole = resultSet.getString(IUserSQLFactory.BUSINESS_ROLE_FIELD);

				User user  = new User(name, null, null, email, username, telephoneNumber, address, null, false, null, roleId, systemRole, businessRole);
				userList.add(user);
			}
			
			userListEither = Either.right(userList);

		} catch (SQLException e) {

			userListEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}

		return 	userListEither;
	}

	public static Either<IException, User> getSingleUserFromResultSet(ResultSet resultSet) {

	    Either<IException, User> userEither = null;

		try {

			if (resultSet.next()) {
			    
				userEither = getUserFromResultSet(resultSet);
				
			} else {
			    
			    IException businessException = new BusinessException(ExceptionCodes.AUTHEO_GET_USER_EXCEPTION);
			    userEither = Either.left(businessException);
			}

		} catch (SQLException e) {

			userEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}

		return userEither;
	}

	public static Either<IException, List<User>> getMultipleUsersFromResultSet(ResultSet resultSet) {

	    Either<IException, List<User>> usersListEither = null;
		List<User> usersList = new ArrayList<User>();

		try {

		    Optional<IException> optionalException = Optional.empty();
			while (resultSet.next() && !optionalException.isPresent()) {
			    
				Either<IException, User> userEither = getUserFromResultSet(resultSet);
				
				if (userEither.isRight()) {
				    
				    User user = userEither.right().value();
				    usersList.add(user);
				    
				} else {
				    
				    IException exception = userEither.left().value();
				    optionalException = Optional.of(exception);
				}
			}
			
			if (!optionalException.isPresent()) {
			    
			    usersListEither = Either.right(usersList);
			    
			} else {
			    
			    usersListEither = Either.left(optionalException.get());
			}

		} catch (SQLException e) {

			usersListEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}

		return usersListEither;
	}
}
