package gp.e3.autheo.authentication.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;
import gp.e3.autheo.authorization.persistence.daos.RoleDAO;
import gp.e3.autheo.infrastructure.clients.rs.CustomerSettingsClient;
import gp.e3.autheo.util.UserFactoryForTests;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class UserBusinessTest {

    private UserDAO userDaoMock;
    private RoleDAO roleDaoMock;
    private Connection dbConnectionMock;
    private BasicDataSource dataSourceMock;
    private CustomerSettingsClient customerSettingsClientMock;

    @Before
    public void setUp() {

        userDaoMock = Mockito.mock(UserDAO.class);
        dbConnectionMock = Mockito.mock(Connection.class);
        dataSourceMock = Mockito.mock(BasicDataSource.class);
        roleDaoMock = Mockito.mock(RoleDAO.class);
        customerSettingsClientMock = Mockito.mock(CustomerSettingsClient.class);

        try {
            Mockito.when(dataSourceMock.getConnection()).thenReturn(dbConnectionMock);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {

        userDaoMock = null;
        dbConnectionMock = null;
        dataSourceMock = null;
    }

    @Test
    public void testCreateUser_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        Either<IException, Boolean> expectedUserWasCreatedEither = Either.right(true);
        Mockito.when(userDaoMock.createUser(Mockito.any(Connection.class), Mockito.any(User.class), Mockito.anyString(), Mockito.anyString(),Mockito.anyObject())).thenReturn(expectedUserWasCreatedEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, Boolean> userWasCreatedEither = userBusiness.createUser(user);

        assertEquals(true, userWasCreatedEither.isRight());
    }

    @Test
    public void testCreateUser_NOK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, Boolean> expectedUserWasCreatedEither = Either.left(technicalException);

        Mockito.when(userDaoMock.createUser(Mockito.any(Connection.class), Mockito.any(User.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyObject())).thenReturn(expectedUserWasCreatedEither);
        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, Boolean> userWasCreatedEither = userBusiness.createUser(user);

        assertEquals(false, userWasCreatedEither.isRight());
    }

    @Test
    public void testAuthenticateUser_OK() {

        try {

            User user = UserFactoryForTests.getDefaultTestUser();

            String originalPassword = user.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            Either<IException, String> passwordHashEither = Either.right(passwordHash);
            Mockito.when(userDaoMock.getPasswordByUsername(dbConnectionMock, user.getUsername(), user.getOrganizationId())).thenReturn(passwordHashEither);

            UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
            Either<IException, Boolean> userIsAuthenticated = userBusiness.authenticateUser(user.getUsername(), user.getPassword(), user.getOrganizationId());
            assertEquals(true, userIsAuthenticated.isRight());

            Boolean result = userIsAuthenticated.right().value();
            assertEquals(true, result);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testAuthenticateUser_NOK() {

        try {

            User user = UserFactoryForTests.getDefaultTestUser();

            String originalPassword = user.getPassword();
            String passwordHash = PasswordHandler.getPasswordHash(originalPassword);
            Either<IException, String> passwordHashEither = Either.right(passwordHash);
            Mockito.when(userDaoMock.getPasswordByUsername(dbConnectionMock, user.getUsername(), user.getOrganizationId())).thenReturn(passwordHashEither);

            UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);

            // The password is modified in order to fail the authentication process.
            String modifiedOriginalPassword = user.getPassword() + "qwe123";

            Either<IException, Boolean> userIsAuthenticated = userBusiness.authenticateUser(user.getUsername(), modifiedOriginalPassword, user.getOrganizationId());
            assertEquals(true, userIsAuthenticated.isRight());

            Boolean isAuthenticated = userIsAuthenticated.right().value();
            assertEquals(false, isAuthenticated);

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {

            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetUserByUsername_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> userEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByUsername(dbConnectionMock, user.getUsername(), user.getOrganizationId())).thenReturn(userEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, User> retrievedUserEither = userBusiness.getUserByUsername(user.getUsername(), user.getOrganizationId());
        assertEquals(true, retrievedUserEither.isRight());

        User retrievedUser = retrievedUserEither.right().value();
        assertEquals(0, user.compareTo(retrievedUser));
    }

    @Test
    public void testGetUserByUsername_NOK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        IException businessException = new BusinessException("User not found.");
        Either<IException, User> userEither = Either.left(businessException);
        Mockito.when(userDaoMock.getUserByUsername(dbConnectionMock, user.getUsername(), user.getOrganizationId())).thenReturn(userEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, User> retrievedUserEither = userBusiness.getUserByUsername(user.getUsername(), user.getOrganizationId());
        assertEquals(false, retrievedUserEither.isRight());

        IException exception = retrievedUserEither.left().value();
        assertEquals(0, businessException.compareTo(exception));
    }
    
    @Test
    public void testGetUsersByRoleId_OK() {

        String roleId = "roleId";

        int listSize = 5;
        List<User> usersList = UserFactoryForTests.getUserList(listSize);
        Either<IException, List<User>> usersListEither = Either.right(usersList);
        Mockito.when(userDaoMock.getUsersByRoleId(dbConnectionMock, roleId, "organization")).thenReturn(usersListEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, List<User>> usersByRoleIdEither = userBusiness.getUsersByRoleId(roleId, "organization");
        assertEquals(true, usersByRoleIdEither.isRight());

        List<User> usersByRoleId = usersByRoleIdEither.right().value();
        assertNotNull(usersByRoleId);
        assertEquals(listSize, usersByRoleId.size());
    }

    @Test
    public void testGetUsersByRoleId_NOK() {

        String roleId = "roleId";

        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, List<User>> usersListEither = Either.left(technicalException);
        Mockito.when(userDaoMock.getUsersByRoleId(dbConnectionMock, roleId, "organization")).thenReturn(usersListEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, List<User>> usersByRoleIdEither = userBusiness.getUsersByRoleId(roleId, "organization");
        assertEquals(false, usersByRoleIdEither.isRight());

        IException exception = usersByRoleIdEither.left().value();
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }

    @Test
    public void testGetAllUsers_OK() {

        int listSize = 5;
        List<User> userList = UserFactoryForTests.getUserList(listSize);
        Either<IException, List<User>> userListEither = Either.right(userList);
        Mockito.when(userDaoMock.getAllUsers(dbConnectionMock)).thenReturn(userListEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, List<User>> retreivedUserListEither = userBusiness.getAllUsers();
        assertEquals(true, retreivedUserListEither.isRight());

        List<User> retreivedUserList = retreivedUserListEither.right().value();
        assertEquals(listSize, retreivedUserList.size());
    }

    @Test
    public void testGetAllUsers_NOK() {

        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, List<User>> userListEither = Either.left(technicalException);
        Mockito.when(userDaoMock.getAllUsers(dbConnectionMock)).thenReturn(userListEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, List<User>> retreivedUserListEither = userBusiness.getAllUsers();
        assertEquals(false, retreivedUserListEither.isRight());

        IException exception = retreivedUserListEither.left().value();
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }

    @Test
    public void testUpdateUser_OK() {

        User defaultUser = UserFactoryForTests.getDefaultTestUser();
        User updatedUser = UserFactoryForTests.getDefaultTestUser(1);

        int numberOfRowsModified = 1;
        Either<IException, Integer> numberOfRowsModifiedEither = Either.right(numberOfRowsModified);
        Mockito.when(userDaoMock.updateUser(dbConnectionMock, defaultUser.getUsername(), updatedUser.getName(), 
                updatedUser.getPassword(), updatedUser.getOrganizationId())).thenReturn(numberOfRowsModifiedEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, Boolean> userWasUpdatedEither = userBusiness.updateUser(defaultUser.getUsername(), updatedUser);
        assertEquals(true, userWasUpdatedEither.isRight());

        Boolean userWasUpdated = userWasUpdatedEither.right().value();
        assertEquals(true, userWasUpdated);
    }

    @Test
    public void testUpdateUser_NOK() {

        // The default user was never used in this test.
        // User defaultUser = UserFactoryForTests.getDefaultTestUser();
        User updatedUser = UserFactoryForTests.getDefaultTestUser(1);

        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, Integer> numberOfRowsModifiedEither = Either.left(technicalException);
        Mockito.when(userDaoMock.updateUser(dbConnectionMock, updatedUser.getUsername(), updatedUser.getName(), 
                updatedUser.getPassword(), updatedUser.getOrganizationId())).thenReturn(numberOfRowsModifiedEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, Boolean> userWasUpdatedEither = userBusiness.updateUser(updatedUser.getUsername(), updatedUser);
        assertEquals(false, userWasUpdatedEither.isRight());

        IException exception = userWasUpdatedEither.left().value();
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }

    @Test
    public void testDeleteUser_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        int numberOfRowsModified = 1;
        Either<IException, Integer> numberOfRowsModifiedEither = Either.right(numberOfRowsModified);
        Mockito.when(userDaoMock.deleteUser(dbConnectionMock, user.getUsername())).thenReturn(numberOfRowsModifiedEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, Boolean> userWasDeletedEither = userBusiness.deleteUser(user.getUsername());
        assertEquals(true, userWasDeletedEither.isRight());

        Boolean userWasDeleted = userWasDeletedEither.right().value();
        assertEquals(true, userWasDeleted);
    }

    @Test
    public void testDeleteUser_NOK() {

        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, Integer> numberOfRowsModifiedEither = Either.left(technicalException);
        String unknownUsername = "unknownUsername";
        Mockito.when(userDaoMock.deleteUser(dbConnectionMock, unknownUsername)).thenReturn(numberOfRowsModifiedEither);

        UserBusiness userBusiness = new UserBusiness(dataSourceMock, userDaoMock, roleDaoMock, customerSettingsClientMock);
        Either<IException, Boolean> userWasDeletedEither = userBusiness.deleteUser(unknownUsername);
        assertEquals(false, userWasDeletedEither.isRight());

        IException exception = userWasDeletedEither.left().value();
        assertEquals(0, technicalException.compareTo(exception));
    }
}