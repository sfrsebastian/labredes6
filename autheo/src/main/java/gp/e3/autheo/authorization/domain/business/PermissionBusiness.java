package gp.e3.autheo.authorization.domain.business;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionUtils;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.persistence.daos.PermissionDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbcp2.BasicDataSource;

public class PermissionBusiness {

    private final BasicDataSource dataSource;
    private final PermissionDAO permissionDao;

    public PermissionBusiness(BasicDataSource dataSource, PermissionDAO permissionDao) {

        this.dataSource = dataSource;
        this.permissionDao = permissionDao;
    }

    public Either<IException, Long> createPermission(Permission permission) {

        Either<IException, Long> permissionIdEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            permissionIdEither = permissionDao.createPermission(dbConnection, permission);

        } catch (SQLException e) {

            permissionIdEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionIdEither;
    }

    private Either<IException, List<Long>> createMultiplePermissions(List<Permission> permissions) {

        Either<IException, List<Long>> permissionsIdsEither = null;
        List<Long> permissionsIds = new ArrayList<Long>();
        Optional<IException> optionalException = Optional.empty();

        for (int i = 0; i < permissions.size() && (!optionalException.isPresent()); i++) {

            Permission permission = permissions.get(i);
            long permissionId = permission.getId();

            if (permissionId <= 0) {

                Either<IException, Long> permissionIdEither = createPermission(permission);

                if (permissionIdEither.isRight()) {

                    long createdPermissionId = permissionIdEither.right().value();
                    permissionsIds.add(createdPermissionId);

                } else {

                    IException exception = permissionIdEither.left().value();
                    optionalException = Optional.of(exception);
                }
            }
        }

        if (!optionalException.isPresent()) {

            permissionsIdsEither = Either.right(permissionsIds);

        } else {

            permissionsIdsEither = Either.left(optionalException.get());
        }

        return permissionsIdsEither;
    }

    private Either<IException, Boolean> verifyAllPermissionsWereAssociated(List<Permission> permissions, Either<IException, Integer> associatedPermissionsEither) {

        Either<IException, Boolean> permissionsWereOverwrittenEither = null; 

        if (associatedPermissionsEither.isRight()) {

            Integer associatedPermissions = associatedPermissionsEither.right().value();
            boolean permissionsWereOverwritten = (associatedPermissions == permissions.size());
            permissionsWereOverwrittenEither = Either.right(permissionsWereOverwritten);

        } else {

            permissionsWereOverwrittenEither = Either.left(ExceptionUtils.getIExceptionFromEither(associatedPermissionsEither));
        }

        return permissionsWereOverwrittenEither;
    }

    public Either<IException, Boolean> overwritePermissionsToRole(String roleName, List<Permission> permissions) {

        Either<IException, Boolean> permissionsWereOverwrittenEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, List<Long>> permissionsIdsEither = createMultiplePermissions(permissions);

            if (permissionsIdsEither.isRight()) {

                List<Long> permissionsIds = permissionsIdsEither.right().value();
                permissionDao.disassociateAllPermissionsFromRole(dbConnection, roleName);
                Either<IException, Integer> associatedPermissionsEither = permissionDao.associateAllPermissionsToRole(dbConnection, roleName, permissionsIds);
                permissionsWereOverwrittenEither = verifyAllPermissionsWereAssociated(permissions, associatedPermissionsEither);

            } else {

                permissionsWereOverwrittenEither = Either.left(ExceptionUtils.getIExceptionFromEither(permissionsIdsEither));
            }

        } catch (SQLException e) {

            permissionsWereOverwrittenEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionsWereOverwrittenEither;
    }

    public Either<IException, Boolean> disassociatePermissionFromAllRoles(int permissionId) {

        Either<IException, Boolean> permissionWasDisassociatedEither = null; 

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Integer> resultEither = permissionDao.disassociatePermissionFromAllRoles(dbConnection, permissionId);

            if (resultEither.isRight()) {

                Integer result = resultEither.right().value();
                boolean permissionWasDisassociated = (result != 0);
                permissionWasDisassociatedEither = Either.right(permissionWasDisassociated);

            } else {

                permissionWasDisassociatedEither = Either.left(ExceptionUtils.getIExceptionFromEither(resultEither));
            }

        } catch (SQLException e) {

            permissionWasDisassociatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionWasDisassociatedEither;
    }

    public Either<IException, Boolean> disassociateAllPermissionsFromRole(String roleName) {

        Either<IException, Boolean> permissionsWereDisassociatedFromRoleEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            Either<IException, Integer> resultEither = permissionDao.disassociateAllPermissionsFromRole(dbConnection, roleName);

            if (resultEither.isRight()) {

                Integer result = resultEither.right().value();
                boolean permissionsWereDisassociatedFromRole = (result != 0);
                permissionsWereDisassociatedFromRoleEither = Either.right(permissionsWereDisassociatedFromRole);

            } else {

                permissionsWereDisassociatedFromRoleEither = Either.left(ExceptionUtils.getIExceptionFromEither(resultEither));
            }

        } catch (SQLException e) {

            permissionsWereDisassociatedFromRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionsWereDisassociatedFromRoleEither;
    }

    public Either<IException, Permission> getPermissionById(long permissionId) {

        Either<IException, Permission> permissionEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            permissionEither = permissionDao.getPermissionById(dbConnection, permissionId);

        } catch (SQLException e) {

            permissionEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionEither;
    }

    public Either<IException, Permission> getPermissionByHttpVerbAndUrl(String httpVerb, String url) {

        Either<IException, Permission> permissionEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            permissionEither = permissionDao.getPermissionByHttpVerbAndUrl(dbConnection, httpVerb, url);

        } catch (SQLException e) {

            permissionEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionEither;
    }

    public Either<IException, List<Permission>> getAllPermissions() {

        Either<IException, List<Permission>> allPermissionsEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            allPermissionsEither = permissionDao.getAllPermissions(dbConnection);

        } catch (SQLException e) {

            allPermissionsEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return allPermissionsEither;
    }

    public Either<IException, List<Permission>> getAllPermissionsOfAGivenRole(String roleName) {

        Either<IException, List<Permission>> permissionsFromRoleEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            permissionsFromRoleEither = permissionDao.getAllPermissionsOfAGivenRole(dbConnection, roleName);

        } catch (SQLException e) {

            permissionsFromRoleEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionsFromRoleEither;
    }

    public Either<IException, Boolean> deletePermission(long permissionId) {

        Either<IException, Boolean> permissionWasDeletedEither = null;

        try (Connection dbConnection = dataSource.getConnection()) {

            permissionDao.disassociatePermissionFromAllRoles(dbConnection, permissionId);
            Either<IException, Integer> numberOfDeletedPermissionsEither = permissionDao.deletePermission(dbConnection, permissionId);

            if (numberOfDeletedPermissionsEither.isRight()) {

                Integer numberOfDeletedPermissions = numberOfDeletedPermissionsEither.right().value();
                boolean permissionWasDeleted = (numberOfDeletedPermissions == 1);
                permissionWasDeletedEither = Either.right(permissionWasDeleted);

            } else {

                permissionWasDeletedEither = Either.left(ExceptionUtils.getIExceptionFromEither(numberOfDeletedPermissionsEither));
            }

        } catch (SQLException e) {

            permissionWasDeletedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return permissionWasDeletedEither;
    }
}