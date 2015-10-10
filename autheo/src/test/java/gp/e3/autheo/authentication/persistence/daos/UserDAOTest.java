package gp.e3.autheo.authentication.persistence.daos;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;
import gp.e3.autheo.authentication.persistence.factories.user.IUserSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.user.UserSQLFactorySingleton;
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

public class UserDAOTest {

    public static final String H2_IN_MEMORY_DB = "jdbc:h2:mem:test";

    private static Connection dbConnection;
    private static UserDAO userDAO;

    @BeforeClass
    public static void setUpClass() {

        try {

            dbConnection = DriverManager.getConnection(H2_IN_MEMORY_DB);
            IUserSQLFactory userSQLFactory = UserSQLFactorySingleton.INSTANCE.getUserSQLFactory(DatabaseNames.MY_SQL.getName());
            userDAO = new UserDAO(userSQLFactory);

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDownClass() {

        userDAO = null;
    }

    @Before
    public void setUp() {

        userDAO.createUsersTableIfNotExists(dbConnection);
    }

    @After
    public void tearDown() {

        try {

            String dropTableSQL = "DROP TABLE users";
            PreparedStatement prepareStatement = dbConnection.prepareStatement(dropTableSQL);
            prepareStatement.executeUpdate();
            prepareStatement.close();
            prepareStatement = null;

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }

    private int countUsersTable() {

        return userDAO.countUsersTable(dbConnection).right().value().intValue();
    }

    //    @Test
    //    public void testCountUsersTable_OK() {
    //
    //        User user = UserFactoryForTests.getDefaultTestUser();
    //        String salt = "salt";
    //
    //        assertEquals(0, countUsersTable());
    //        userDAO.createUser(dbConnection, user, user.getPassword(), salt);
    //
    //        Either<IException, List<User>> allUsersEither = userDAO.getAllUsers(dbConnection);
    //        assertEquals(true, allUsersEither.isRight());
    //
    //        List<User> allUsers = allUsersEither.right().value();
    //        int numberOfUsersIntoDb = allUsers.size();
    //
    //        assertEquals(1, numberOfUsersIntoDb);
    //        assertEquals(numberOfUsersIntoDb, countUsersTable());
    //
    //        Either<IException, User> retrievedUserEither = userDAO.getUserByUsername(dbConnection, user.getUsername());
    //        assertEquals(true, retrievedUserEither.isRight());
    //
    //        User retrievedUser = retrievedUserEither.right().value();
    //        assertEquals(0, user.compareTo(retrievedUser));
    //    }

    @Test
    public void testCountUsersTable_NOK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        assertEquals(0, countUsersTable());
        Either<IException, List<User>> allUsersEither = userDAO.getAllUsers(dbConnection);
        assertEquals(true, allUsersEither.isRight());

        List<User> allUsers = allUsersEither.right().value();
        int numberOfUsersIntoDb = allUsers.size();

        assertEquals(0, numberOfUsersIntoDb);
        assertEquals(numberOfUsersIntoDb, countUsersTable());

        Either<IException, User> retrievedUserEither = userDAO.getUserByUsername(dbConnection, user.getUsername(), user.getOrganizationId());
        assertEquals(false, retrievedUserEither.isRight());

        IException exception = retrievedUserEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    //    @Test
    //    public void testCreateUser_OK() {
    //
    //        User user = UserFactoryForTests.getDefaultTestUser();
    //        String salt = "salt";
    //
    //        assertEquals(0, countUsersTable());
    //        Either<IException, Boolean> userWasCreatedEither = userDAO.createUser(dbConnection, user, user.getPassword(), salt);
    //        assertEquals(true, userWasCreatedEither.isRight());
    //
    //        Boolean userWasCreated = userWasCreatedEither.right().value();
    //        assertEquals(true, userWasCreated);
    //
    //        assertEquals(1, countUsersTable());
    //        Either<IException, User> retrievedUserEither = userDAO.getUserByUsername(dbConnection, user.getUsername());
    //        assertEquals(true, retrievedUserEither.isRight());
    //
    //        User retrievedUser = retrievedUserEither.right().value();
    //        assertEquals(0, user.compareTo(retrievedUser));
    //    }

    @Test
    public void testCreateUser_NOK_nullUser() {

        User user = UserFactoryForTests.getNullUser();
        String salt = "salt";

        try {

            assertEquals(0, countUsersTable());
            userDAO.createUser(dbConnection, user, user.getPassword(), salt, null);
            fail("Should thorw a null pointer exception");

        } catch (Exception e) {

            assertNotNull(e);
        }
    }

    //    @Test
    //    public void testGetUserByUsername_OK() {
    //
    //        User user = UserFactoryForTests.getDefaultTestUser();
    //        String salt = "salt";
    //
    //        userDAO.createUser(dbConnection, user, user.getPassword(), salt);
    //        Either<IException, User> retrievedUserEither = userDAO.getUserByUsername(dbConnection, user.getUsername());
    //        assertEquals(true, retrievedUserEither.isRight());
    //
    //        User retrievedUser = retrievedUserEither.right().value();
    //        assertEquals(0, user.compareTo(retrievedUser));
    //    }

    @Test
    public void testGetUserByUsername_NOK_unknownUsername() {

        User user = UserFactoryForTests.getDefaultTestUser();
        String salt = "salt";

        userDAO.createUser(dbConnection, user, user.getPassword(), salt, null);
        String unknownUsername = "unknownUsername";

        Either<IException, User> retrievedUserEither = userDAO.getUserByUsername(dbConnection, unknownUsername, "organizationId");
        assertEquals(false, retrievedUserEither.isRight());

        IException exception = retrievedUserEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    //    @Test
    //    public void testGetAllUsers_OK() {
    //
    //        int listSize = 5;
    //        List<User> userList = UserFactoryForTests.getUserList(listSize);
    //        String salt = "123";
    //        
    //        assertEquals(0, countUsersTable());
    //
    //        for (User user : userList) {
    //            userDAO.createUser(dbConnection, user, user.getPassword(), salt);
    //        }
    //
    //        assertEquals(listSize, countUsersTable());
    //        
    //        Either<IException, List<User>> allUsersEither = userDAO.getAllUsers(dbConnection);
    //        assertEquals(true, allUsersEither.isRight());
    //
    //        List<User> allUsers = allUsersEither.right().value();
    //        assertEquals(listSize, allUsers.size());
    //    }

    @Test
    public void testGetAllUsers_NOK_emptyUsersList() {

        int listSize = 0;
        List<User> userList = UserFactoryForTests.getUserList(listSize);
        String salt = "123";

        for (User user : userList) {
            userDAO.createUser(dbConnection, user, user.getPassword(), salt, null);
        }

        assertEquals(listSize, countUsersTable());
        Either<IException, List<User>> allUsersEither = userDAO.getAllUsers(dbConnection);
        assertEquals(true, allUsersEither.isRight());

        List<User> allUsers = allUsersEither.right().value();
        assertEquals(listSize, allUsers.size());
    }

    @Test
    public void testGetPasswordByUsername_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        String salt = "salt";

        assertEquals(0, countUsersTable());
        userDAO.createUser(dbConnection, user, user.getPassword(), salt, null);
        assertEquals(1, countUsersTable());

        Either<IException, String> passwordEither = userDAO.getPasswordByUsername(dbConnection, user.getUsername(), user.getOrganizationId());
        assertEquals(true, passwordEither.isRight());

        String password = passwordEither.right().value();
        assertEquals(user.getPassword(), password);
    }

    @Test
    public void testGetPasswordByUsername_NOK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        assertEquals(0, countUsersTable());
        Either<IException, String> passwordEither = userDAO.getPasswordByUsername(dbConnection, user.getUsername(), user.getOrganizationId());
        assertEquals(false, passwordEither.isRight());

        IException exception = passwordEither.left().value();
        assertNotNull(exception);
        assertEquals(true, exception instanceof BusinessException);
    }

    //    @Test
    //    public void testUpdateUser_OK() {
    //
    //        User defaultUser = UserFactoryForTests.getDefaultTestUser();
    //        String salt = "salt";
    //
    //        assertEquals(0, countUsersTable());
    //        userDAO.createUser(dbConnection, defaultUser, defaultUser.getPassword(), salt);
    //        assertEquals(1, countUsersTable());
    //
    //        User updatedUser = UserFactoryForTests.getDefaultTestUser(1);
    //        Either<IException, Integer> numberOfRowsModifiedEither = userDAO.updateUser(dbConnection, defaultUser.getUsername(), updatedUser.getName(), updatedUser.getPassword());
    //        assertEquals(true, numberOfRowsModifiedEither.isRight());
    //
    //        Integer numberOfRowsModified = numberOfRowsModifiedEither.right().value();
    //        assertEquals(1, numberOfRowsModified.intValue());
    //        assertEquals(1, countUsersTable());
    //
    //        Either<IException, User> retrievedUserEither = userDAO.getUserByUsername(dbConnection, defaultUser.getUsername());
    //        assertEquals(true, retrievedUserEither.isRight());
    //
    //        User retrievedUser = retrievedUserEither.right().value();
    //        assertEquals(defaultUser.getUsername(), retrievedUser.getUsername());
    //        assertEquals(updatedUser.getName(), retrievedUser.getName());
    //        assertEquals(updatedUser.getPassword(), retrievedUser.getPassword());
    //    }

    //    @Test
    //    public void testUpdateUser_NOK_unknownUsername() {
    //
    //        User defaultUser = UserFactoryForTests.getDefaultTestUser();
    //        String salt = "salt";
    //
    //        assertEquals(0, countUsersTable());
    //        userDAO.createUser(dbConnection, defaultUser, defaultUser.getPassword(), salt);
    //        assertEquals(1, countUsersTable());
    //
    //        User updatedUser = UserFactoryForTests.getDefaultTestUser(1);
    //
    //        String unknownUsername = "unknownUsername";
    //        Either<IException, Integer> numberOfRowsModifiedEither = userDAO.updateUser(dbConnection, unknownUsername, updatedUser.getName(), updatedUser.getPassword());
    //        assertEquals(true, numberOfRowsModifiedEither.isRight());
    //
    //        Integer numberOfRowsModified = numberOfRowsModifiedEither.right().value();
    //        assertEquals(0, numberOfRowsModified.intValue());
    //        assertEquals(1, countUsersTable());
    //
    //        Either<IException, User> unknownUserEither = userDAO.getUserByUsername(dbConnection, unknownUsername);
    //        assertEquals(false, unknownUserEither.isRight());
    //
    //        IException exception = unknownUserEither.left().value();
    //        assertNotNull(exception);
    //        assertEquals(true, exception.isBusinessException());
    //
    //        Either<IException, User> retrievedDefaultUserEither = userDAO.getUserByUsername(dbConnection, defaultUser.getUsername());
    //        assertEquals(true, retrievedDefaultUserEither.isRight());
    //
    //        User retrievedDefaultUser = retrievedDefaultUserEither.right().value();
    //        assertEquals(defaultUser.getName(), retrievedDefaultUser.getName());
    //        assertEquals(defaultUser.getUsername(), retrievedDefaultUser.getUsername());
    //        assertEquals(defaultUser.getPassword(), retrievedDefaultUser.getPassword());
    //    }

    @Test
    public void testDeleteUser_OK() {

        User defaultUser = UserFactoryForTests.getDefaultTestUser();
        String salt = "salt";

        assertEquals(0, countUsersTable());
        userDAO.createUser(dbConnection, defaultUser, defaultUser.getPassword(), salt, null);
        assertEquals(1, countUsersTable());

        Either<IException, Integer> numberOfRowsModifiedEither = userDAO.deleteUser(dbConnection, defaultUser.getUsername());
        assertEquals(true, numberOfRowsModifiedEither.isRight());

        Integer numberOfRowsModified = numberOfRowsModifiedEither.right().value();
        assertEquals(1, numberOfRowsModified.intValue());
        assertEquals(0, countUsersTable());
    }

    @Test
    public void testDeleteUser_NOK_emptyTable() {

        User defaultUser = UserFactoryForTests.getDefaultTestUser();

        assertEquals(0, countUsersTable());
        Either<IException, Integer> numberOfRowsModifiedEither = userDAO.deleteUser(dbConnection, defaultUser.getUsername());
        assertEquals(true, numberOfRowsModifiedEither.isRight());

        Integer numberOfRowsModified = numberOfRowsModifiedEither.right().value();
        assertEquals(0, numberOfRowsModified.intValue());
        assertEquals(0, countUsersTable());
    }
}