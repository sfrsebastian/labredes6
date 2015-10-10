package gp.e3.autheo.authorization.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;
import gp.e3.autheo.authentication.infrastructure.utils.sql.SqlUtils;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.domain.entities.Role;
import gp.e3.autheo.authorization.persistence.factories.permission.IPermissionSQLFactory;
import gp.e3.autheo.authorization.persistence.factories.permission.PermissionSQLFactorySingleton;
import gp.e3.autheo.authorization.persistence.factories.role.IRoleSQLFactory;
import gp.e3.autheo.authorization.persistence.factories.role.RoleSQLFactorySingleton;
import gp.e3.autheo.util.PermissionFactoryForTests;
import gp.e3.autheo.util.RoleFactoryForTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PermissionDAOTest {

    public static final String H2_IN_MEMORY_DB = "jdbc:h2:mem:test";

    private static Connection dbConnection;
    private static RoleDAO roleDAO;
    private static PermissionDAO permissionDAO;

    @BeforeClass
    public static void setUpClass() {

        try {

            dbConnection = DriverManager.getConnection(H2_IN_MEMORY_DB);

            IPermissionSQLFactory permissionSQLFactory = PermissionSQLFactorySingleton.INSTANCE.getPermissionSQLFactory(DatabaseNames.MY_SQL.getName());
            permissionDAO = new PermissionDAO(permissionSQLFactory);

            IRoleSQLFactory roleSQLFactory = RoleSQLFactorySingleton.INSTANCE.getRoleSQLFactory(DatabaseNames.MY_SQL.getName());
            roleDAO = new RoleDAO(roleSQLFactory);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {

        roleDAO = null;
        permissionDAO = null;
        SqlUtils.closeDbConnection(dbConnection);
    }

    @Before
    public void setUp() {

        roleDAO.createRolesAndPermissionsTable(dbConnection);
        permissionDAO.createPermissionsTableIfNotExists(dbConnection);
    }

    @After
    public void tearDown() {

        try {

            String dropRolesAndPermissionsTable = "DROP TABLE roles_permissions";
            PreparedStatement prepareStatement = dbConnection.prepareStatement(dropRolesAndPermissionsTable);
            prepareStatement.executeUpdate();
            prepareStatement.close();
            prepareStatement = null;

            String dropPermissionsTable = "DROP TABLE permissions";
            PreparedStatement prepareStatement2 = dbConnection.prepareStatement(dropPermissionsTable);
            prepareStatement2.executeUpdate();
            prepareStatement2.close();
            prepareStatement2 = null;

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    
    private int countPermissionsTable() {
        
        return permissionDAO.countPermissionsTable(dbConnection).right().value().intValue();
    }

    @Test
    public void testCreatePermission_OK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

        assertEquals(0, countPermissionsTable());
        Either<IException, Long> generatedIdEither = permissionDAO.createPermission(dbConnection, permission);
        assertEquals(true, generatedIdEither.isRight());
        assertEquals(1, countPermissionsTable());

        long generatedId = generatedIdEither.right().value().longValue();
        assertNotEquals(0, generatedId);

        Either<IException, Permission> retrievedPermissionEither = permissionDAO.getPermissionById(dbConnection, generatedId);
        assertEquals(true, retrievedPermissionEither.isRight());

        Permission retrievedPermission = retrievedPermissionEither.right().value();
        assertEquals(0, permission.compareTo(retrievedPermission));
    }

    @Test
    public void testCreatePermission_NOK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

        assertEquals(0, countPermissionsTable());
        Either<IException, Long> firstPermissionIdEither = permissionDAO.createPermission(dbConnection, permission);
        assertEquals(true, firstPermissionIdEither.isRight());
        assertEquals(1, countPermissionsTable());

        long firstPermissionId = firstPermissionIdEither.right().value().longValue();
        assertNotEquals(0, firstPermissionId);

        Either<IException, Long> secondPermissionIdEither = permissionDAO.createPermission(dbConnection, permission);
        assertEquals(false, secondPermissionIdEither.isRight());
        assertEquals(1, countPermissionsTable());

        IException exception = secondPermissionIdEither.left().value();
        assertNotNull(exception);
        assertEquals(false, exception instanceof BusinessException);
    }

    private int countRolePermissionsTable() {

        return permissionDAO.countRolePermissionsTable(dbConnection).right().value().intValue();
    }

    @Test
    public void testAssociateAllPermissionsToRole_OK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();

        List<Permission> permissions = PermissionFactoryForTests.getPermissionList(5);
        List<Long> permissionsIds = getPermissionsIdsAsList(permissions);

        assertEquals(0, countRolePermissionsTable());
        permissionDAO.associateAllPermissionsToRole(dbConnection, role.getName(), permissionsIds);
        assertEquals(permissionsIds.size(), countRolePermissionsTable());
    }

    @Test
    public void testAssociateAllPermissionsToRole_NOK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();
        List<Long> permissionsIds = new ArrayList<Long>();

        assertEquals(0, countRolePermissionsTable());
        permissionDAO.associateAllPermissionsToRole(dbConnection, role.getName(), permissionsIds);
        assertEquals(permissionsIds.size(), countRolePermissionsTable());
    }

    @Test
    public void testDisassociateAllPermissionsFromRole_OK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();

        List<Permission> permissions = PermissionFactoryForTests.getPermissionList(5);
        List<Long> permissionsIds = getPermissionsIdsAsList(permissions);

        assertEquals(0, countRolePermissionsTable());
        permissionDAO.associateAllPermissionsToRole(dbConnection, role.getName(), permissionsIds);
        assertEquals(permissionsIds.size(), countRolePermissionsTable());

        permissionDAO.disassociateAllPermissionsFromRole(dbConnection, role.getName());
        assertEquals(0, countRolePermissionsTable());
    }

    @Test
    public void testDisassociateAllPermissionsFromRole_NOK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();
        List<Long> permissionsIds = new ArrayList<Long>();

        assertEquals(0, countRolePermissionsTable());
        permissionDAO.associateAllPermissionsToRole(dbConnection, role.getName(), permissionsIds);
        assertEquals(permissionsIds.size(), countRolePermissionsTable());

        permissionDAO.disassociateAllPermissionsFromRole(dbConnection, role.getName());
        assertEquals(0, countRolePermissionsTable());
    }

    @Test
    public void testDisassociatePermissionFromAllRoles_OK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();

        List<Permission> permissions = PermissionFactoryForTests.getPermissionList(5);
        List<Long> permissionsIds = getPermissionsIdsAsList(permissions);

        assertEquals(0, countRolePermissionsTable());
        permissionDAO.associateAllPermissionsToRole(dbConnection, role.getName(), permissionsIds);
        assertEquals(permissionsIds.size(), countRolePermissionsTable());

        permissionDAO.disassociatePermissionFromAllRoles(dbConnection, permissionsIds.get(0));
        assertEquals(permissionsIds.size() - 1, countRolePermissionsTable());

        permissionDAO.disassociatePermissionFromAllRoles(dbConnection, permissionsIds.get(1));
        assertEquals(permissionsIds.size() - 2, countRolePermissionsTable());
    }

    private List<Long> getPermissionsIdsAsList(List<Permission> permissions) {

        List<Long> permissionsIds = new ArrayList<Long>();

        for (int i = 0; i < permissions.size(); i++) {
            permissionsIds.add(permissions.get(i).getId());
        }
        return permissionsIds;
    }

    @Test
    public void testDisassociatePermissionFromAllRoles_NOK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();
        List<Long> permissionsIds = new ArrayList<Long>();

        assertEquals(0, countRolePermissionsTable());
        permissionDAO.associateAllPermissionsToRole(dbConnection, role.getName(), permissionsIds);
        assertEquals(permissionsIds.size(), countRolePermissionsTable());

        int fakePermissionId = 0;
        permissionDAO.disassociatePermissionFromAllRoles(dbConnection, fakePermissionId);
        assertEquals(0, countRolePermissionsTable());

        permissionDAO.disassociatePermissionFromAllRoles(dbConnection, fakePermissionId);
        assertEquals(0, countRolePermissionsTable());
    }

    @Test
    public void testGetPermissionById_OK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

        Either<IException, Long> generatedIdEither = permissionDAO.createPermission(dbConnection, permission);
        assertEquals(true, generatedIdEither.isRight());

        long generatedId = generatedIdEither.right().value().longValue();
        Either<IException, Permission> retrievedPermissionEither = permissionDAO.getPermissionById(dbConnection, generatedId);
        assertEquals(true, retrievedPermissionEither.isRight());

        Permission retrievedPermission = retrievedPermissionEither.right().value();
        assertEquals(0, permission.compareTo(retrievedPermission));
    }

    @Test
    public void testGetPermissionById_NOK() {

        int fakePermissionId = 0;
        Either<IException, Permission> retrievedPermissionEither = permissionDAO.getPermissionById(dbConnection, fakePermissionId);
        assertEquals(false, retrievedPermissionEither.isRight());

        IException exception = retrievedPermissionEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    @Test
    public void testGetPermissionByHttpVerbAndUrl_OK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

        Either<IException, Long> generatedIdEither = permissionDAO.createPermission(dbConnection, permission);
        assertEquals(true, generatedIdEither.isRight());

        Either<IException, Permission> retrievedPermissionEither = permissionDAO.getPermissionByHttpVerbAndUrl(dbConnection, permission.getHttpVerb(), permission.getUrl());
        assertEquals(true, retrievedPermissionEither.isRight());

        long generatedId = generatedIdEither.right().value().longValue();
        Permission retrievedPermission = retrievedPermissionEither.right().value();

        assertEquals(generatedId, retrievedPermission.getId());
        assertEquals(0, permission.compareTo(retrievedPermission));
    }

    @Test
    public void testGetPermissionByHttpVerbAndUrl_NOK() {

        String fakeHttpVerb = "FAKE";
        String fakeUrl = "www.fake.com";
        Either<IException, Permission> retrievedPermissionEither = permissionDAO.getPermissionByHttpVerbAndUrl(dbConnection, fakeHttpVerb, fakeUrl);
        assertEquals(false, retrievedPermissionEither.isRight());

        IException exception = retrievedPermissionEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    private void createMultiplePermissions(List<Permission> permissionList) {

        for (int i = 0; i < permissionList.size(); i++) {

            Permission permission = permissionList.get(i);
            permissionDAO.createPermission(dbConnection, permission);
        }
    }

    @Test
    public void testGetAllPermissions_OK() {

        int listSize = 5;
        List<Permission> permissionList = PermissionFactoryForTests.getPermissionList(listSize);

        assertEquals(0, countPermissionsTable());
        createMultiplePermissions(permissionList);
        assertEquals(listSize, countPermissionsTable());

        Either<IException, List<Permission>> allPermissionsEither = permissionDAO.getAllPermissions(dbConnection);
        assertEquals(true, allPermissionsEither.isRight());

        List<Permission> allPermissions = allPermissionsEither.right().value();
        assertEquals(countPermissionsTable(), allPermissions.size());
    }

    @Test
    public void testGetAllPermissions_NOK() {

        assertEquals(0, countPermissionsTable());

        Either<IException, List<Permission>> allPermissionsEither = permissionDAO.getAllPermissions(dbConnection);
        assertEquals(true, allPermissionsEither.isRight());

        List<Permission> allPermissions = allPermissionsEither.right().value();
        assertEquals(countPermissionsTable(), allPermissions.size());
    }

    @Test
    public void testGetAllPermissionsOfAGivenRole_OK() {

        int adminNumberOfPermissions = 5;
        Role adminRole = RoleFactoryForTests.getDefaultTestRole(adminNumberOfPermissions);
        List<Long> adminPermissionsIds = getPermissionsIdsAsList(adminRole.getPermissions());
        createMultiplePermissions(adminRole.getPermissions());

        permissionDAO.associateAllPermissionsToRole(dbConnection, adminRole.getName(), adminPermissionsIds);
        assertEquals(adminNumberOfPermissions, countRolePermissionsTable());

        Either<IException, List<Permission>> retrievedAdminPermissionsEither = permissionDAO.getAllPermissionsOfAGivenRole(dbConnection, adminRole.getName());
        assertEquals(true, retrievedAdminPermissionsEither.isRight());

        List<Permission> retrievedAdminPermissions = retrievedAdminPermissionsEither.right().value();
        assertEquals(adminNumberOfPermissions, retrievedAdminPermissions.size());
    }

    @Test
    public void testGetAllPermissionsOfAGivenRole_OK2() {

        int adminNumberOfPermissions = 5;
        int nullNumberOfPermissions = 5;
        List<Permission> nullPermissions = new ArrayList<Permission>();
        nullPermissions.add(PermissionFactoryForTests.getDefaultTestPermission(nullNumberOfPermissions));
        nullNumberOfPermissions++;
        nullPermissions.add(PermissionFactoryForTests.getDefaultTestPermission(nullNumberOfPermissions));
        nullNumberOfPermissions++;
        nullPermissions.add(PermissionFactoryForTests.getDefaultTestPermission(nullNumberOfPermissions));
        nullNumberOfPermissions++;

        Role adminRole = RoleFactoryForTests.getDefaultTestRole(adminNumberOfPermissions);
        List<Long> adminPermissionsIds = getPermissionsIdsAsList(adminRole.getPermissions());

        createMultiplePermissions(adminRole.getPermissions());
        createMultiplePermissions(nullPermissions);

        permissionDAO.associateAllPermissionsToRole(dbConnection, adminRole.getName(), adminPermissionsIds);

        assertEquals(adminNumberOfPermissions, countRolePermissionsTable());
        assertEquals(nullNumberOfPermissions, countPermissionsTable());

        Either<IException, List<Permission>> retrievedAdminPermissionsEither = permissionDAO.getAllPermissionsOfAGivenRole(dbConnection, adminRole.getName());
        assertEquals(true, retrievedAdminPermissionsEither.isRight());

        List<Permission> retrievedAdminPermissions = retrievedAdminPermissionsEither.right().value();
        assertEquals(adminNumberOfPermissions, retrievedAdminPermissions.size());
    }

    @Test
    public void testGetAllPermissionsOfAGivenRole_NOK() {

        int adminNumberOfPermissions = 5;
        Role adminRole = RoleFactoryForTests.getDefaultTestRole(adminNumberOfPermissions);
        List<Long> adminPermissionsIds = getPermissionsIdsAsList(adminRole.getPermissions());

        permissionDAO.associateAllPermissionsToRole(dbConnection, adminRole.getName(), adminPermissionsIds);
        assertEquals(adminNumberOfPermissions, countRolePermissionsTable());

        Either<IException, List<Permission>> retrievedAdminPermissionsEither = permissionDAO.getAllPermissionsOfAGivenRole(dbConnection, adminRole.getName());
        assertEquals(true, retrievedAdminPermissionsEither.isRight());

        // Should return an empty list because the roles are not created into the db.
        List<Permission> retrievedAdminPermissions = retrievedAdminPermissionsEither.right().value();
        assertEquals(0, retrievedAdminPermissions.size());
    }

    @Test
    public void testDeletePermission_OK() {

        Permission firstPermission = PermissionFactoryForTests.getDefaultTestPermission(1);
        Permission secondPermission = PermissionFactoryForTests.getDefaultTestPermission(2);

        Either<IException, Long> firstPermissionIdEither = permissionDAO.createPermission(dbConnection, firstPermission);
        assertEquals(true, firstPermissionIdEither.isRight());
        Either<IException, Long> secondPermissionIdEither = permissionDAO.createPermission(dbConnection, secondPermission);
        assertEquals(true, secondPermissionIdEither.isRight());
        assertEquals(2, countPermissionsTable());

        Long firstPermissionId = firstPermissionIdEither.right().value();
        permissionDAO.deletePermission(dbConnection, firstPermissionId);

        assertEquals(1, countPermissionsTable());
        Either<IException, Permission> firstPermissionEither = permissionDAO.getPermissionById(dbConnection, firstPermissionId);
        assertEquals(false, firstPermissionEither.isRight());
        
        IException exception = firstPermissionEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);

        Long secondPermissionId = secondPermissionIdEither.right().value();
        permissionDAO.deletePermission(dbConnection, secondPermissionId);

        assertEquals(0, countPermissionsTable());
        Either<IException, Permission> secondPermissionEither = permissionDAO.getPermissionById(dbConnection, secondPermissionId);
        assertEquals(false, secondPermissionEither.isRight());
        
        IException secondException = secondPermissionEither.left().value();
        assertNotNull(secondException);
        assertEquals(true, exception instanceof BusinessException);
    }

    @Test
    public void testDeletePermission_NOK() {

        Permission firstPermission = PermissionFactoryForTests.getDefaultTestPermission(1);

        Either<IException, Long> firstPermissionIdEither = permissionDAO.createPermission(dbConnection, firstPermission);
        assertEquals(true, firstPermissionIdEither.isRight());
        assertEquals(1, countPermissionsTable());

        int fakePermissionId = 123;
        permissionDAO.deletePermission(dbConnection, fakePermissionId);
        assertEquals(1, countPermissionsTable());

        Long firstPermissionId = firstPermissionIdEither.right().value();
        permissionDAO.deletePermission(dbConnection, firstPermissionId);

        assertEquals(0, countPermissionsTable());
        Either<IException, Permission> permissionEither = permissionDAO.getPermissionById(dbConnection, firstPermissionId);
        assertEquals(false, permissionEither.isRight());
        
        IException exception = permissionEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }
}