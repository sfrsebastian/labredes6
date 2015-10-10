package gp.e3.autheo.authorization.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;
import gp.e3.autheo.authentication.infrastructure.utils.sql.SqlUtils;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;
import gp.e3.autheo.authentication.persistence.factories.user.IUserSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.user.UserSQLFactorySingleton;
import gp.e3.autheo.authorization.domain.entities.Role;
import gp.e3.autheo.authorization.persistence.factories.role.IRoleSQLFactory;
import gp.e3.autheo.authorization.persistence.factories.role.RoleSQLFactorySingleton;
import gp.e3.autheo.util.RoleFactoryForTests;
import gp.e3.autheo.util.UserFactoryForTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoleDAOTest {

    public static final String H2_IN_MEMORY_DB = "jdbc:h2:mem:test";

    private static Connection dbConnection;
    private static RoleDAO roleDAO;
    private static UserDAO userDAO;

    @BeforeClass
    public static void setUpClass() {

        try {

            dbConnection = DriverManager.getConnection(H2_IN_MEMORY_DB);

            IRoleSQLFactory roleSQLFactory = RoleSQLFactorySingleton.INSTANCE.getRoleSQLFactory(DatabaseNames.MY_SQL.getName());
            roleDAO = new RoleDAO(roleSQLFactory);
            IUserSQLFactory userSQLFactory = UserSQLFactorySingleton.INSTANCE.getUserSQLFactory(DatabaseNames.MY_SQL.getName());
            userDAO = new UserDAO(userSQLFactory);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {

        roleDAO = null;
        SqlUtils.closeDbConnection(dbConnection);
    }

    @Before
    public void setUp() {

        roleDAO.createRolesTableIfNotExists(dbConnection);
        roleDAO.createBusinessRolesTable(dbConnection);
        userDAO.createUsersTableIfNotExists(dbConnection);
        roleDAO.createRolesAndUsersTable(dbConnection);
        roleDAO.createRolesAndPermissionsTable(dbConnection);
    }

    @After
    public void tearDown() {

        try {

            String dropRolesPermissionsTable = "DROP TABLE roles_permissions;";
            PreparedStatement prepareStatement = dbConnection.prepareStatement(dropRolesPermissionsTable);
            prepareStatement.executeUpdate();
            prepareStatement.close();

            String dropRolesUsersTable = "DROP TABLE roles_users;";
            prepareStatement = dbConnection.prepareStatement(dropRolesUsersTable);
            prepareStatement.executeUpdate();
            prepareStatement.close();

            String dropRolesTable = "DROP TABLE roles;";
            prepareStatement = dbConnection.prepareStatement(dropRolesTable);
            prepareStatement.executeUpdate();
            prepareStatement.close();

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private int countRolesTable() {

        return roleDAO.countRolesTable(dbConnection).right().value().intValue();
    }

    @Test
    public void testCreateRole_OK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRolesTable());
        roleDAO.createRole(dbConnection, role.getName());
        assertEquals(1, countRolesTable());

        Either<IException, List<String>> rolesNamesEither = roleDAO.getAllRolesNames(dbConnection);
        assertEquals(true, rolesNamesEither.isRight());

        List<String> rolesNames = rolesNamesEither.right().value();
        assertEquals(1, rolesNames.size());
        assertEquals(role.getName(), rolesNames.get(0));
    }

    @Test
    public void testCreateRole_NOK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRolesTable());
        roleDAO.createRole(dbConnection, role.getName());
        assertEquals(1, countRolesTable());

        Either<IException, List<String>> rolesNamesEither = roleDAO.getAllRolesNames(dbConnection);
        assertEquals(true, rolesNamesEither.isRight());

        List<String> rolesNames = rolesNamesEither.right().value();
        assertEquals(1, rolesNames.size());
        assertEquals(role.getName(), rolesNames.get(0));

        Either<IException, Integer> affectedRowsEither = roleDAO.createRole(dbConnection, role.getName());
        assertEquals(false, affectedRowsEither.isRight());

        IException exception = affectedRowsEither.left().value();
        assertNotNull(exception);
        assertEquals(false, exception instanceof BusinessException);
    }

    @Test
    public void testGetAllRolesNames_OK() {

        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRolesTable());
        roleDAO.createRole(dbConnection, role.getName());
        assertEquals(1, countRolesTable());

        Either<IException, List<String>> rolesNamesEither = roleDAO.getAllRolesNames(dbConnection);
        assertEquals(true, rolesNamesEither.isRight());

        List<String> rolesNames = rolesNamesEither.right().value();
        assertEquals(1, rolesNames.size());
        assertEquals(role.getName(), rolesNames.get(0));

        Role secondRole = RoleFactoryForTests.getDefaultTestRole(2);
        roleDAO.createRole(dbConnection, secondRole.getName());
        assertEquals(2, countRolesTable());

        rolesNamesEither = roleDAO.getAllRolesNames(dbConnection);
        assertEquals(true, rolesNamesEither.isRight());

        rolesNames = rolesNamesEither.right().value();
        assertEquals(2, rolesNames.size());
    }

    @Test
    public void testGetAllRolesNames_NOK() {

        Either<IException, List<String>> rolesNamesEither = roleDAO.getAllRolesNames(dbConnection);
        assertEquals(true, rolesNamesEither.isRight());

        List<String> rolesNames = rolesNamesEither.right().value();
        assertEquals(0, rolesNames.size());
    }

    @Test
    public void testDeleteRole_OK() {

        Role defaultRole = RoleFactoryForTests.getDefaultTestRole();
        Role secondRole = RoleFactoryForTests.getDefaultTestRole(2);

        assertEquals(0, countRolesTable());
        roleDAO.createRole(dbConnection, defaultRole.getName());
        roleDAO.createRole(dbConnection, secondRole.getName());
        assertEquals(2, countRolesTable());

        roleDAO.deleteRole(dbConnection, defaultRole.getName());
        assertEquals(1, countRolesTable());

        Either<IException, List<String>> allRolesNamesEither = roleDAO.getAllRolesNames(dbConnection);
        assertEquals(true, allRolesNamesEither.isRight());

        List<String> allRolesNames = allRolesNamesEither.right().value();
        assertEquals(1, allRolesNames.size());
        assertEquals(secondRole.getName(), allRolesNames.get(0));

        roleDAO.deleteRole(dbConnection, secondRole.getName());
        assertEquals(0, countRolesTable());
    }

    @Test
    public void testDeleteRole_NOK() {

        String fakeRoleName = "fakeRoleName";

        assertEquals(0, countRolesTable());
        roleDAO.deleteRole(dbConnection, fakeRoleName);
        assertEquals(0, countRolesTable());
    }

    private int countRoleUsersTable() {

        return roleDAO.countRoleUsersTable(dbConnection).right().value().intValue();
    }

    @Test
    public void testAddUserToRole_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRoleUsersTable());
        roleDAO.addUserToRole(dbConnection, user.getUsername(), role.getName(), user.getOrganizationId());
        assertEquals(1, countRoleUsersTable());

        String secondUsername = "second";
        String thirdUsername = "third";

        roleDAO.addUserToRole(dbConnection, secondUsername, role.getName(), user.getOrganizationId());
        roleDAO.addUserToRole(dbConnection, thirdUsername, role.getName(), user.getOrganizationId());
        assertEquals(3, countRoleUsersTable());
    }

    @Test
    public void testAddUserToRole_NOK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRoleUsersTable());
        roleDAO.addUserToRole(dbConnection, user.getUsername(), role.getName(), user.getOrganizationId());
        assertEquals(1, countRoleUsersTable());

        Either<IException, Integer> affectedRowsEither = roleDAO.addUserToRole(dbConnection, user.getUsername(), role.getName(), user.getOrganizationId());
        assertEquals(false, affectedRowsEither.isRight());

        IException exception = affectedRowsEither.left().value();
        assertNotNull(exception);
        assertEquals(false, exception instanceof BusinessException);
    }

    @Test
    public void testRemoveUserFromRole_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRoleUsersTable());
        roleDAO.addUserToRole(dbConnection, user.getUsername(), role.getName(), user.getOrganizationId());
        assertEquals(1, countRoleUsersTable());

        String secondUsername = "second";
        String thirdUsername = "third";

        roleDAO.addUserToRole(dbConnection, secondUsername, role.getName(), user.getOrganizationId());
        roleDAO.addUserToRole(dbConnection, thirdUsername, role.getName(), user.getOrganizationId());
        assertEquals(3, countRoleUsersTable());

        roleDAO.removeUserFromRole(dbConnection, secondUsername);
        assertEquals(2, countRoleUsersTable());

        // The same username so nothing changes.
        roleDAO.removeUserFromRole(dbConnection, secondUsername);
        assertEquals(2, countRoleUsersTable());

        roleDAO.removeUserFromRole(dbConnection, user.getUsername());
        assertEquals(1, countRoleUsersTable());

        roleDAO.removeUserFromRole(dbConnection, thirdUsername);
        assertEquals(0, countRoleUsersTable());
    }

    @Test
    public void testRemoveUserFromRole_NOK() {

        String fakeUsername = "fakeUsername";

        assertEquals(0, countRoleUsersTable());
        roleDAO.removeUserFromRole(dbConnection, fakeUsername);
        assertEquals(0, countRoleUsersTable());
    }

    @Test
    public void testRemoveAllUsersFromRole_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRoleUsersTable());
        roleDAO.addUserToRole(dbConnection, user.getUsername(), role.getName(), user.getOrganizationId());
        assertEquals(1, countRoleUsersTable());

        String secondUsername = "second";
        String thirdUsername = "third";

        roleDAO.addUserToRole(dbConnection, secondUsername, role.getName(), user.getOrganizationId());
        roleDAO.addUserToRole(dbConnection, thirdUsername, role.getName(), user.getOrganizationId());
        assertEquals(3, countRoleUsersTable());

        roleDAO.removeAllUsersFromRole(dbConnection, role.getName());
        assertEquals(0, countRoleUsersTable());
    }

    @Test
    public void testRemoveAllUsersFromRole_NOK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Role role = RoleFactoryForTests.getDefaultTestRole();

        assertEquals(0, countRoleUsersTable());
        roleDAO.addUserToRole(dbConnection, user.getUsername(), role.getName(), user.getOrganizationId());
        assertEquals(1, countRoleUsersTable());

        String secondUsername = "second";
        String thirdUsername = "third";

        roleDAO.addUserToRole(dbConnection, secondUsername, role.getName(), user.getOrganizationId());
        roleDAO.addUserToRole(dbConnection, thirdUsername, role.getName(), user.getOrganizationId());
        assertEquals(3, countRoleUsersTable());

        String fakeRoleName = "fakeRoleName";
        roleDAO.removeAllUsersFromRole(dbConnection, fakeRoleName);
        assertEquals(3, countRoleUsersTable());
    }
}