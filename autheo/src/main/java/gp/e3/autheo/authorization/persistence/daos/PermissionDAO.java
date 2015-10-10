package gp.e3.autheo.authorization.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.sql.SqlUtils;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.persistence.factories.permission.IPermissionSQLFactory;
import gp.e3.autheo.authorization.persistence.mappers.PermissionMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PermissionDAO {
	
	private final IPermissionSQLFactory permissionSQLFactory;
	
	public PermissionDAO(IPermissionSQLFactory permissionSQLFactory) {
		
		this.permissionSQLFactory = permissionSQLFactory;
	}

	private Either<IException, Integer> createPermissionsUniqueIndex(Connection dbConnection) {

	    Either<IException, Integer> resultEither = null;
	    String createPermissionsUniqueIndexSQL = permissionSQLFactory.getCreatePermissionsUniqueIndexSQL();

		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createPermissionsUniqueIndexSQL)) {
		    
			int result = prepareStatement.executeUpdate();
			resultEither = Either.right(result);

		} catch (SQLException e) {

			resultEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}

		return resultEither;
	}

	public Either<IException, Integer> createPermissionsTableIfNotExists(Connection dbConnection) {

	    Either<IException, Integer> affectedRowsEither = null;
	    String createPermissionsTableIfNotExistsSQL = permissionSQLFactory.getCreatePermissionsTableIfNotExistsSQL();

		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createPermissionsTableIfNotExistsSQL)) {

			prepareStatement.executeUpdate();
			affectedRowsEither = createPermissionsUniqueIndex(dbConnection);

		} catch (SQLException e) {
		    
		    affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}

		return affectedRowsEither;
	}

	public Either<IException, Integer> countPermissionsTable(Connection dbConnection) {

	    Either<IException, Integer> tableRowsEither = null;
	    String countPermissionsTableSQL = permissionSQLFactory.getCountPermissionsTableSQL();

		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(countPermissionsTableSQL)) {

			ResultSet resultSet = prepareStatement.executeQuery();
			int tableRows = 0;
			
			if (resultSet.next()) {
				tableRows = resultSet.getInt(1);
			}

			resultSet.close();
			tableRowsEither = Either.right(tableRows);

		} catch (SQLException e) {

			e.printStackTrace();
		}

		return tableRowsEither;
	}

	public Either<IException, Integer> countRolePermissionsTable(Connection dbConnection) {

	    Either<IException, Integer> tableRowsEither = null;
	    String countRolePermissionsTableSQL = permissionSQLFactory.getCountRolePermissionsTableSQL();

		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(countRolePermissionsTableSQL)) {

			ResultSet resultSet = prepareStatement.executeQuery();
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

	public Either<IException, Long> createPermission(Connection dbConnection, Permission permission) {

	    Either<IException, Long> permissionIdEither = null;
		String createPermissionSQL = permissionSQLFactory.getCreatePermissionSQL();

		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createPermissionSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
			
			prepareStatement.setString(1, permission.getName());
			prepareStatement.setString(2, permission.getHttpVerb());
			prepareStatement.setString(3, permission.getUrl());

			int affectedRows = prepareStatement.executeUpdate();
			long permissionId = 0;
			
			if (affectedRows > 0) {
			    
				permissionId = SqlUtils.getGeneratedIdFromResultSet(prepareStatement.getGeneratedKeys());
				permissionIdEither = Either.right(permissionId);
				
			} else {
			    
			    IException businessException = new BusinessException("Permission could not be created.");
			    permissionIdEither = Either.left(businessException);
			}

		} catch (SQLException e) {
		    
		    permissionIdEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}

		return permissionIdEither;
	}

	private int getTotalResultFromBatchResult(int[] batchResult) {

		int totalBatchResult = 0;

		for (int result : batchResult) {
			totalBatchResult += result;
		}

		return totalBatchResult;
	}
	
	public Either<IException, Integer> associateAllPermissionsToRole(Connection dbConnection, String roleName, List<Long> permissionIdList) {

	    Either<IException, Integer> affectedRowsEither = null;
		String associateAllPermissionsToRoleSQL = permissionSQLFactory.getAssociateAllPermissionsToRoleSQL();

		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(associateAllPermissionsToRoleSQL)) {

			for (Long permissionId : permissionIdList) {

				prepareStatement.setString(1, roleName);
				prepareStatement.setLong(2, permissionId);
				prepareStatement.addBatch();
			}

			int[] batchResult = prepareStatement.executeBatch();
			int affectedRows = getTotalResultFromBatchResult(batchResult);
			affectedRowsEither = Either.right(affectedRows);

		} catch (SQLException e) {
		    
		    affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return affectedRowsEither;
	}
	
	public Either<IException, Integer> disassociateAllPermissionsFromRole(Connection dbConnection, String roleName) {
		
	    Either<IException, Integer> affectedRowsEither = null;
		String disassociateAllPermissionsFromRoleSQL = permissionSQLFactory.getDisassociateAllPermissionsFromRoleSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(disassociateAllPermissionsFromRoleSQL)) {
			
			prepareStatement.setString(1, roleName);
			int affectedRows = prepareStatement.executeUpdate();
			affectedRowsEither = Either.right(affectedRows);
			
		} catch (SQLException e) {
			
			affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return affectedRowsEither;
	}
	
	public Either<IException, Integer> disassociatePermissionFromAllRoles(Connection dbConnection, long permissionId) {
		
	    Either<IException, Integer> affectedRowsEither = null;
		String disassociatePermissionFromAllRolesSQL = permissionSQLFactory.getDisassociatePermissionFromAllRolesSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(disassociatePermissionFromAllRolesSQL)) {
			
			prepareStatement.setLong(1, permissionId);
			int affectedRows = prepareStatement.executeUpdate();
			affectedRowsEither = Either.right(affectedRows);
			
		} catch (SQLException e) {
			
			affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return affectedRowsEither;
	}
	
	public Either<IException, Permission> getPermissionById(Connection dbConnection, long permissionId) {
		
	    Either<IException, Permission> permissionEither = null;
		String getPermissionByIdSQL = permissionSQLFactory.getGetPermissionByIdSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getPermissionByIdSQL)) {
			
			prepareStatement.setLong(1, permissionId);
			ResultSet resultSet = prepareStatement.executeQuery();
			permissionEither = PermissionMapper.getSinglePermission(resultSet);
			
			resultSet.close();
			
		} catch (SQLException e) {
			
			permissionEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return permissionEither;
	}
	
	public Either<IException, Permission> getPermissionByHttpVerbAndUrl(Connection dbConnection, String httpVerb, String url) {
		
	    Either<IException, Permission> permissionEither = null;
		String getPermissionByHttpVerbAndUrlSQL = permissionSQLFactory.getGetPermissionByHttpVerbAndUrlSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getPermissionByHttpVerbAndUrlSQL)) {
			
			prepareStatement.setString(1, httpVerb);
			prepareStatement.setString(2, url);
			
			ResultSet resultSet = prepareStatement.executeQuery();
			permissionEither = PermissionMapper.getSinglePermission(resultSet);
			
			resultSet.close();
			
		} catch (SQLException e) {
			
			permissionEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return permissionEither;
	}
	
	public Either<IException, List<Permission>> getAllPermissions(Connection dbConnection) {
		
	    Either<IException, List<Permission>> allPermissionsEither = null;
		String getAllPermissionsSQL = permissionSQLFactory.getGetAllPermissionsSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getAllPermissionsSQL)) {
			
			ResultSet resultSet = prepareStatement.executeQuery();
			allPermissionsEither = PermissionMapper.getMultiplePermissions(resultSet);
			
			resultSet.close();
			
		} catch (SQLException e) {
			
			allPermissionsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return allPermissionsEither;
	}
	
	public Either<IException, List<Permission>> getAllPermissionsOfAGivenRole(Connection dbConnection, String roleName) {
		
	    Either<IException, List<Permission>> permissionsFromRoleEither = null;
		String getAllPermissionsOfAGivenRoleSQL = permissionSQLFactory.getGetAllPermissionsOfAGivenRoleSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getAllPermissionsOfAGivenRoleSQL)) {
			
			prepareStatement.setString(1, roleName);
			ResultSet resultSet = prepareStatement.executeQuery();
			permissionsFromRoleEither = PermissionMapper.getMultiplePermissions(resultSet);
			resultSet.close();
			
		} catch (SQLException e) {
			
			permissionsFromRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return permissionsFromRoleEither;
	}
	
	public Either<IException, Integer> deletePermission(Connection dbConnection, long permissionId) {
		
	    Either<IException, Integer> affectedRowsEither = null;
		String deletePermissionSQL = permissionSQLFactory.getDeletePermissionSQL();
		
		try (PreparedStatement prepareStatement = dbConnection.prepareStatement(deletePermissionSQL)) {
			
			prepareStatement.setLong(1, permissionId);
			int affectedRows = prepareStatement.executeUpdate();
			affectedRowsEither = Either.right(affectedRows);
			
		} catch (SQLException e) {
			
		    affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
		}
		
		return affectedRowsEither;
	}
}