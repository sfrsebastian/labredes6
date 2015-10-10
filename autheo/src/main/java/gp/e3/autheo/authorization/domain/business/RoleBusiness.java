package gp.e3.autheo.authorization.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.entities.BusinessRole;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.domain.entities.Role;
import gp.e3.autheo.authorization.infrastructure.dtos.PermissionTuple;
import gp.e3.autheo.authorization.persistence.daos.RoleDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RoleBusiness {

    private BasicDataSource dataSource;
    private JedisPool redisPool;
    private final RoleDAO roleDAO;
    private final PermissionBusiness permissionBusiness;

    public RoleBusiness(BasicDataSource dataSource, JedisPool redisPool, RoleDAO roleDAO, PermissionBusiness permissionBusiness) {

        this.dataSource = dataSource;
        this.redisPool = redisPool;
        this.roleDAO = roleDAO;
        this.permissionBusiness = permissionBusiness;
    }

    private Jedis getRedisClient() {

        return redisPool.getResource();
    }

    private void returnResource(Jedis jedis) {

        redisPool.returnResource(jedis);
    }

    private Either<IException, Role> overwriteRolePermissionsIfRoleWasCreated(boolean roleWasCreated, Role role) {

        Either<IException, Role> createdRoleEither = null;

        if (roleWasCreated) {

            permissionBusiness.overwritePermissionsToRole(role.getName(), role.getPermissions());
            createdRoleEither = Either.right(role);

        } else {

            IException businessException = new BusinessException("The role could not be created");
            createdRoleEither = Either.left(businessException);
        }

        return createdRoleEither;
    }

    public Either<IException, Role> createRole(Role role) {

        Either<IException, Role> createdRoleEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            String roleName = role.getName();
            Either<IException, Integer> affectedRowsEither = roleDAO.createRole(dbConnection, roleName);

            if (affectedRowsEither.isRight()) {

                Integer affectedRows = affectedRowsEither.right().value();
                boolean roleWasCreated = (affectedRows == 1);
                createdRoleEither = overwriteRolePermissionsIfRoleWasCreated(roleWasCreated, role);

            } else {

                createdRoleEither = Either.left(ExceptionUtils.getIExceptionFromEither(affectedRowsEither));
            }

        } catch (SQLException e) {

            createdRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return createdRoleEither;
    }

    public Either<IException, Role> getRoleByName(String roleName) {

        Either<IException, Role> roleEither = null;
        Either<IException, List<Permission>> allPermissionsOfAGivenRoleEither = permissionBusiness.getAllPermissionsOfAGivenRole(roleName);

        if (allPermissionsOfAGivenRoleEither.isRight()) {

            List<Permission> allPermissionsOfAGivenRole = allPermissionsOfAGivenRoleEither.right().value();
            Role role = new Role(roleName, allPermissionsOfAGivenRole);
            roleEither = Either.right(role);

        } else {

            roleEither = Either.left(ExceptionUtils.getIExceptionFromEither(allPermissionsOfAGivenRoleEither));
        }

        return roleEither;
    }
    
    public Either<IException, List<String>> getCitizenBusinessRoles(String tenatId) {

    	Either<IException, List<String>> businessRoleseither = null;

    	String systemRole = "ciudadano";
    	
        try (Connection dbConnection = dataSource.getConnection()) {

            businessRoleseither = roleDAO.getCitizenBusinessRoles( dbConnection, tenatId , systemRole );

        } catch (SQLException e) {

            businessRoleseither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return businessRoleseither;
    }
    

    public Either<IException, List<String>> getAllRolesNames() {

        Either<IException, List<String>> allRolesNamesEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            allRolesNamesEither = roleDAO.getAllRolesNames(dbConnection);

        } catch (SQLException e) {

            allRolesNamesEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return allRolesNamesEither;
    }

    public Either<IException, Boolean> addRolePermissionsToRedis(String roleName) {

        Either<IException, Boolean> permissionsWereAddedToRedisEither = null;
        Either<IException, List<Permission>> allPermissionsOfAGivenRoleEither = permissionBusiness.getAllPermissionsOfAGivenRole(roleName);

        if (allPermissionsOfAGivenRoleEither.isRight()) {

            List<Permission> rolePermissions = allPermissionsOfAGivenRoleEither.right().value();
            Gson gson = new Gson();
            String rolePermissionsAsJson = gson.toJson(rolePermissions);

            Jedis redisClient = getRedisClient();
            String setResult = redisClient.set(roleName, rolePermissionsAsJson);
            returnResource(redisClient);

            // See: http://redis.io/commands/SET
            boolean permissionsWereAddedToRedis = (setResult.equals("OK"));
            permissionsWereAddedToRedisEither = Either.right(permissionsWereAddedToRedis);

        } else {

            permissionsWereAddedToRedisEither = Either.left(ExceptionUtils.getIExceptionFromEither(allPermissionsOfAGivenRoleEither));
        }

        return permissionsWereAddedToRedisEither;
    }

    public Either<IException, Boolean> updateRole(String roleName, List<Permission> updatedPermissions) {

        permissionBusiness.overwritePermissionsToRole(roleName, updatedPermissions);
        return addRolePermissionsToRedis(roleName); // Update role in Redis.
    }

    public Either<IException, Boolean> deleteRole(String roleName) {

        Either<IException, Boolean> roleWasDeletedEither = null;
        Jedis redisClient = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            permissionBusiness.disassociateAllPermissionsFromRole(roleName);
            roleDAO.removeAllUsersFromRole(dbConnection, roleName);
            roleDAO.deleteRole(dbConnection, roleName);

            redisClient = getRedisClient();
            long keysRemoved = redisClient.del(roleName);
            boolean roleWasDeleted = (keysRemoved > 0);
            returnResource(redisClient);

            roleWasDeletedEither = Either.right(roleWasDeleted);

        } catch (JedisConnectionException e) {

            redisPool.returnBrokenResource(redisClient);
            roleWasDeletedEither = Either.left(ExceptionFactory.getTechnicalException(e));

        } catch (SQLException e) {

            returnResource(redisClient);
            roleWasDeletedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return roleWasDeletedEither;
    }

    public Either<IException, Boolean> addUserToRole(String username, String roleName, String organizationId) {

        Either<IException, Boolean> userWasAddedToRoleEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Integer> affectedRowsEither = roleDAO.addUserToRole(dbConnection, username, roleName, organizationId);

            if (affectedRowsEither.isRight()) {

                int affectedRows = affectedRowsEither.right().value();
                boolean userWasAddedToRole = (affectedRows == 1);
                userWasAddedToRoleEither = Either.right(userWasAddedToRole);

            } else {

                userWasAddedToRoleEither = Either.left(ExceptionUtils.getIExceptionFromEither(affectedRowsEither));
            }

        } catch (SQLException e) {

            userWasAddedToRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasAddedToRoleEither;
    }

    public Either<IException, Boolean> removeUserFromRole(String username, String roleName) {

        Either<IException, Boolean> userWasRemovedFromRoleEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Integer> affectedRowsEither = roleDAO.removeUserFromRole(dbConnection, username);

            if (affectedRowsEither.isRight()) {

                int affectedRows = affectedRowsEither.right().value();
                boolean userWasRemovedFromRole = (affectedRows > 0);
                userWasRemovedFromRoleEither = Either.right(userWasRemovedFromRole);

            } else {

                userWasRemovedFromRoleEither = Either.left(ExceptionUtils.getIExceptionFromEither(affectedRowsEither));
            }

        } catch (SQLException e) {

            userWasRemovedFromRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return userWasRemovedFromRoleEither;
    }

    public Either<IException, List<Permission>> getAllPermissionsOfAGivenRole(String roleName) {

        return permissionBusiness.getAllPermissionsOfAGivenRole(roleName);
    }

    public boolean rolePermissionsAreInRedis(String roleName) {

        Jedis redisClient = getRedisClient();
        String getResult = redisClient.get(roleName);
        returnResource(redisClient);

        return !StringUtils.isBlank(getResult);
    }

    public List<PermissionTuple> getRolePermissionsFromRedis(String roleName) {

        List<PermissionTuple> rolePermissions = new ArrayList<PermissionTuple>();

        Jedis redisClient = getRedisClient();
        String rolePermissionsString = redisClient.get(roleName);
        returnResource(redisClient);

        if (!StringUtils.isBlank(rolePermissionsString)) {

            Gson gson = new Gson();
            List<Permission> listOfPermissions = gson.fromJson(rolePermissionsString, new TypeToken<List<Permission>>(){}.getType());

            if (listOfPermissions.size() > 0) {

                for (int i = 0; i < listOfPermissions.size(); i++) {

                    Permission permission = listOfPermissions.get(i);
                    rolePermissions.add(new PermissionTuple(permission.getHttpVerb(), permission.getUrl()));
                }
            }
        }

        return rolePermissions;
    }
    
    public Either<IException,List<BusinessRole>> getBusinessRoles(String organizationId){
        
        Either<IException, List<BusinessRole>> businessRolesEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {
            
            businessRolesEither = roleDAO.getBusinessRoles(dbConnection, organizationId);

        } catch (SQLException e) {
            
            businessRolesEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return businessRolesEither;
    }
    
    public Either<IException,BusinessRole> createBusinessRole(BusinessRole businessRole){
        Either<IException, BusinessRole> businessRoleEither = null;
        
        try (Connection dbConnection = dataSource.getConnection()) {
            
            dbConnection.setAutoCommit(false);
            businessRoleEither = roleDAO.createBusinessRole(dbConnection, businessRole);
            if(businessRoleEither.isRight()){
                businessRoleEither = roleDAO.createSystemBusinessRoleRelation(dbConnection, businessRole);
                if(businessRoleEither.isRight()){
                    dbConnection.commit();
                }else{
                    dbConnection.rollback();
                }
                
            }
        } catch (SQLException e) {

            businessRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        return businessRoleEither;
    }
}