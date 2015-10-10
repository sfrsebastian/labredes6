package gp.e3.autheo.authorization.persistence.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.persistence.factories.permission.IPermissionSQLFactory;

public class PermissionMapper {
	
	private static Either<IException, Permission> getPermissionFromResultSet(ResultSet resultSet) {
		
	    Either<IException, Permission> permissionEither = null;
		
		try {
			
			int id = resultSet.getInt(IPermissionSQLFactory.ID_FIELD);
			String name = resultSet.getString(IPermissionSQLFactory.NAME_FIELD);
			String httpVerb = resultSet.getString(IPermissionSQLFactory.HTTP_VERB_FIELD);
			String url = resultSet.getString(IPermissionSQLFactory.URL_FIELD);
			
			Permission permission = new Permission(id, name, httpVerb, url);
			permissionEither = Either.right(permission);
			
		} catch (SQLException e) {
		    
		    permissionEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return permissionEither;
	}
	
	public static Either<IException, Permission> getSinglePermission(ResultSet resultSet) {
		
	    Either<IException, Permission> permissionEither = null;
		
		try {
			
			if (resultSet.next()) {
			    
				permissionEither = getPermissionFromResultSet(resultSet);
				
			} else {
			    
			    IException businessException = new BusinessException("permission not found.");
			    permissionEither = Either.left(businessException);
			}
			
		} catch (SQLException e) {
			
			permissionEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return permissionEither;
	}
	
	public static Either<IException, List<Permission>> getMultiplePermissions(ResultSet resultSet) {
		
	    Either<IException, List<Permission>> permissionsEither = null;
		List<Permission> permissions = new ArrayList<Permission>();
		
		try {
			
		    Optional<IException> exceptionOptional = Optional.empty();
			while (resultSet.next() && !exceptionOptional.isPresent()) {
			    
				Either<IException, Permission> permissionEither = getPermissionFromResultSet(resultSet);
				
				if (permissionEither.isRight()) {
				    
				    Permission permission = permissionEither.right().value();
				    permissions.add(permission);
				    
				} else {
				    
				    IException exception = permissionEither.left().value();
				    exceptionOptional = Optional.of(exception);
				}
			}
			
			if (!exceptionOptional.isPresent()) {
			    
			    permissionsEither = Either.right(permissions);
			    
			} else {
			    
			    permissionsEither = Either.left(exceptionOptional.get());
			}
			
		} catch (SQLException e) {
			
			permissionsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return permissionsEither;
	}
}