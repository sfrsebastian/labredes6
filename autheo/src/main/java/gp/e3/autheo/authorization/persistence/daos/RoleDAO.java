package gp.e3.autheo.authorization.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authorization.domain.entities.BusinessRole;
import gp.e3.autheo.authorization.persistence.factories.role.IRoleSQLFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RoleDAO {

    private final IRoleSQLFactory roleSQLFactory;

    public RoleDAO(IRoleSQLFactory roleSQLFactory) {

        this.roleSQLFactory = roleSQLFactory;
    }

    public Either<IException, Boolean> createRolesTableIfNotExists(Connection dbConnection) {

        Either<IException, Boolean> rolesTableWasCreatedEither = null;
        String createRolesTableIfNotExistsSQL = roleSQLFactory.getCreateRolesTableIfNotExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createRolesTableIfNotExistsSQL)) {

            int result = prepareStatement.executeUpdate();
            boolean rolesTableWasCreated = (result != 0);
            rolesTableWasCreatedEither = Either.right(rolesTableWasCreated);

        } catch (SQLException e) {

            rolesTableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rolesTableWasCreatedEither;
    }

    public Either<IException, Boolean> createRolesAndPermissionsTable(Connection dbConnection) {

        Either<IException, Boolean> rolesAndPermissionsTableWasCreatedEither = null;
        String createRolesAndPermissionsTableSQL = roleSQLFactory.getCreateRolesAndPermissionsTableSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createRolesAndPermissionsTableSQL)) {

            int result = prepareStatement.executeUpdate();
            boolean rolesAndPermissionsTableWasCreated = (result != 0);
            rolesAndPermissionsTableWasCreatedEither = Either.right(rolesAndPermissionsTableWasCreated);

        } catch (SQLException e) {

            rolesAndPermissionsTableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rolesAndPermissionsTableWasCreatedEither;
    }

    public Either<IException, Boolean> createBusinessRolesTable(Connection dbConnection) {

        Either<IException, Boolean> businessRolesTableWasCreatedEither = null;
        String createRolesAndPermissionsTableSQL = roleSQLFactory.getCreateBusinessRolesSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createRolesAndPermissionsTableSQL)) {

            int result = prepareStatement.executeUpdate();
            boolean businessRolesTableWasCreated = (result != 0);
            businessRolesTableWasCreatedEither = Either.right(businessRolesTableWasCreated);

        } catch (SQLException e) {

            businessRolesTableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return businessRolesTableWasCreatedEither;
    }


    public Either<IException, Boolean> createSystemBusinessRolesTable(Connection dbConnection) {

        Either<IException, Boolean> systemBusinessRolesTableWasCreatedEither = null;
        String createRolesAndPermissionsTableSQL = roleSQLFactory.getCreateSystemBusinessRolesSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createRolesAndPermissionsTableSQL)) {

            int result = prepareStatement.executeUpdate();
            boolean systemBusinessRolesTableWasCreated = (result != 0);
            systemBusinessRolesTableWasCreatedEither = Either.right(systemBusinessRolesTableWasCreated);

        } catch (SQLException e) {

            systemBusinessRolesTableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return systemBusinessRolesTableWasCreatedEither;
    }

    public Either<IException, Boolean> createRolesAndUsersTable(Connection dbConnection) {

        Either<IException, Boolean> rolesAndUsersTableWasCreatedEither = null;
        String createRolesAndUsersTableSQL = roleSQLFactory.getCreateRolesAndUsersTableSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createRolesAndUsersTableSQL)) {

            int result = prepareStatement.executeUpdate();
            boolean rolesAndUsersTableWasCreated = (result != 0);
            rolesAndUsersTableWasCreatedEither = Either.right(rolesAndUsersTableWasCreated);

        } catch (SQLException e) {

            rolesAndUsersTableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rolesAndUsersTableWasCreatedEither;
    }

    private Either<IException, Integer> getTableRowsFromResultSet(ResultSet resultSet) {

        Either<IException, Integer> tableRowsEither = null;

        try {

            int tableRows = 0;

            if (resultSet.next()) {
                tableRows = resultSet.getInt(1);
            }

            tableRowsEither = Either.right(tableRows);

        } catch (SQLException e) {

            tableRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableRowsEither;
    }

    public Either<IException, Integer> countRolesTable(Connection dbConnection) {

        Either<IException, Integer> tableRowsEither = null;
        String countRolesTableSQL = roleSQLFactory.getCountRolesTableSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(countRolesTableSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            tableRowsEither = getTableRowsFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            tableRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableRowsEither;
    }

    public Either<IException, Integer> countRolePermissionsTable(Connection dbConnection) {

        Either<IException, Integer> tableRowsEither = null;
        String countRolePermissionsTableSQL = roleSQLFactory.getCountRolePermissionsTableSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(countRolePermissionsTableSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            tableRowsEither = getTableRowsFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            tableRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableRowsEither;
    }

    public Either<IException, Integer> countRoleUsersTable(Connection dbConnection) {

        Either<IException, Integer> tableRowsEither = null;
        String countRoleUsersTableSQL = roleSQLFactory.getCountRoleUsersTableSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(countRoleUsersTableSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            tableRowsEither = getTableRowsFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            tableRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableRowsEither;
    }

    public Either<IException, Integer> createRole(Connection dbConnection, String roleName) {

        Either<IException, Integer> affectedRowsEither = null;
        String createRoleSQL = roleSQLFactory.getCreateRoleSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createRoleSQL)) {

            prepareStatement.setString(1, roleName);
            int affectedRows = prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(affectedRows);

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }

    public Either<IException, List<String>> getAllRolesNames(Connection dbConnection) {

        Either<IException, List<String>> roleNamesListEither = null;
        String getAllRolesNamesSQL = roleSQLFactory.getGetAllRolesNamesSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getAllRolesNamesSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            List<String> roleNamesList = new ArrayList<String>();

            while (resultSet.next()) {

                String roleName = resultSet.getString(1);
                roleNamesList.add(roleName);
            }

            resultSet.close();
            roleNamesListEither = Either.right(roleNamesList);

        } catch (SQLException e) {

            roleNamesListEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return roleNamesListEither;
    }
    
    public Either<IException, List<String>> getCitizenBusinessRoles(Connection dbConnection, String tenatId, String systemRole) {

        Either<IException, List<String>> roleNamesListEither = null;
        
        List< String > businessRolesList = new ArrayList<String>();
        
        String getGetCitizenBusinessRolesSQL = roleSQLFactory.getGetCitizenBusinessRolesSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getGetCitizenBusinessRolesSQL)) {

            prepareStatement.setString(1, tenatId);
            prepareStatement.setString(2, systemRole);
            
            ResultSet resultSet = prepareStatement.executeQuery();
            
            while (resultSet.next()) {

                String roleName = resultSet.getString(1);
                businessRolesList.add(roleName);
                
            }
            System.out.println("size: "+ businessRolesList.size());
            roleNamesListEither = Either.right(businessRolesList);

        } catch (SQLException e) {
        	e.printStackTrace();
        	roleNamesListEither = Either.left(new TechnicalException(e.getMessage()));
        	
        }
        
        return roleNamesListEither;
    }

    public Either<IException, Integer> deleteRole(Connection dbConnection, String roleName) {

        Either<IException, Integer> affectedRowsEither = null;
        String deleteRoleSQL = roleSQLFactory.getDeleteRoleSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(deleteRoleSQL)) {

            prepareStatement.setString(1, roleName);
            int affectedRows = prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(affectedRows);

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }

    public Either<IException, Integer> addUserToRole(Connection dbConnection, String username, String roleName, String organizationId) {

        Either<IException, Integer> affectedRowsEither = null;
        String addUserToRoleSQL = roleSQLFactory.getAddUserToRoleSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(addUserToRoleSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            prepareStatement.setString(2, roleName);
            prepareStatement.setString(3, organizationId);

            int affectedRows = prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(affectedRows);

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }

    public Either<IException, Integer> removeUserFromRole(Connection dbConnection, String username) {

        Either<IException, Integer> affectedRowsEither = null;
        String removeUserFromRoleSQL = roleSQLFactory.getRemoveUserFromRoleSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(removeUserFromRoleSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            int affectedRows = prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(affectedRows);

        } catch (SQLException e) {

            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return affectedRowsEither;
    }

    public Either<IException, Boolean> removeAllUsersFromRole(Connection dbConnection, String roleName) {

        Either<IException, Boolean> allUsersWereRemovedFromRoleEither = null;
        String removeAllUsersFromRoleSQL = roleSQLFactory.getRemoveAllUsersFromRoleSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(removeAllUsersFromRoleSQL)) {

            prepareStatement.setString(1, roleName);
            int affectedRows = prepareStatement.executeUpdate();
            boolean allUsersWereRemovedFromRole = (affectedRows != 0);
            allUsersWereRemovedFromRoleEither = Either.right(allUsersWereRemovedFromRole);

        } catch (SQLException e) {

            allUsersWereRemovedFromRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return allUsersWereRemovedFromRoleEither;
    }

    public Either<IException, List<BusinessRole>> getBusinessRoles(Connection dbConnection, String organizationId){

        Either<IException, List<BusinessRole>> businessRolesEither = null;
        String getBusinessRolesSQL = roleSQLFactory.getGetBusinessRolesSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getBusinessRolesSQL)) {

            prepareStatement.setString(1, organizationId);
            ResultSet resultSet = prepareStatement.executeQuery();
            List<BusinessRole> rolesList = new ArrayList<BusinessRole>();

            while (resultSet.next()) {

                int id = resultSet.getInt(1);
                String systemRole = resultSet.getString(2);
                String businessRole = resultSet.getString(3);
                rolesList.add(new BusinessRole(id, systemRole, businessRole, null, null));
            }

            resultSet.close();
            businessRolesEither = Either.right(rolesList);

        } catch (SQLException e) {

            businessRolesEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return businessRolesEither;	    
    }

    public Either<IException, BusinessRole> createBusinessRole(Connection dbConnection, BusinessRole businessRole){
        
        Either<IException, BusinessRole> affectedRowsEither = null;

        String createBusinessRoleSQL = roleSQLFactory.getCreateBusinessRoleSQL();

        try {
            PreparedStatement prepareStatement = dbConnection.prepareStatement(createBusinessRoleSQL);
            prepareStatement.setString(1, businessRole.getBusinessRole());
            prepareStatement.setString(2, businessRole.getDescription());
            prepareStatement.setString(3, businessRole.getOrganization());
            prepareStatement.executeUpdate();

            affectedRowsEither = Either.right(businessRole);

        } catch (SQLException e) {
            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        
        return affectedRowsEither;
    }
    
    public Either<IException, BusinessRole> createSystemBusinessRoleRelation(Connection dbConnection, BusinessRole businessRole){
        
        Either<IException, BusinessRole> affectedRowsEither = null;

        String createSystemBusinessRoleRelationSQL = roleSQLFactory.getCreateSystemBusinessRoleRelationSQL();

        try {
            
            PreparedStatement prepareStatement = dbConnection.prepareStatement(createSystemBusinessRoleRelationSQL, Statement.RETURN_GENERATED_KEYS);

            prepareStatement = dbConnection.prepareStatement(createSystemBusinessRoleRelationSQL);
            prepareStatement.setString(1, businessRole.getSystemRole());
            prepareStatement.setString(2, businessRole.getBusinessRole());
            prepareStatement.setString(3, businessRole.getOrganization());
            prepareStatement.executeUpdate();
            affectedRowsEither = Either.right(businessRole);

        } catch (SQLException e) {
            affectedRowsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        
        return affectedRowsEither;
    }
}