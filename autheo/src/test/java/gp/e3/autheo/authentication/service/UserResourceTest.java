package gp.e3.autheo.authentication.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.PasswordTokenBusiness;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.domain.business.UserBusiness;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.service.representation.ChangePasswordGivenPasswordToken;
import gp.e3.autheo.authentication.service.representation.CreatePasswordToken;
import gp.e3.autheo.authentication.service.resources.UserResource;
import gp.e3.autheo.authorization.domain.business.RoleBusiness;
import gp.e3.autheo.util.TokenProviderTest;
import gp.e3.autheo.util.UserFactoryForTests;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

/**
 * @author Lean factory
 */
public class UserResourceTest {

    private static final UserBusiness userBusinessMock = Mockito.mock(UserBusiness.class);
    private static final RoleBusiness roleBusinessMock = Mockito.mock(RoleBusiness.class);
    private static final TokenBusiness tokenBusinessMock = Mockito.mock(TokenBusiness.class);
    private static final PasswordTokenBusiness passwordTokenBusinessMock = Mockito.mock(PasswordTokenBusiness.class);
    private static final UserResource userResource = new UserResource(userBusinessMock, roleBusinessMock, tokenBusinessMock, passwordTokenBusinessMock);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(userResource).addProvider(TokenProviderTest.class).build();

    private Builder getDefaultHttpRequest(String url) {

        return resources.client().resource(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

        Mockito.reset(userBusinessMock);
        Mockito.reset(roleBusinessMock);
        Mockito.reset(tokenBusinessMock);
    }

    @Test
    public void testCreateUser_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(userBusinessMock.createUser(Mockito.any(User.class))).thenReturn(trueEither);
        Mockito.when(tokenBusinessMock.generateAndSaveTokensForAnAPIUser(Mockito.any(User.class))).thenReturn(trueEither);
        Mockito.when(roleBusinessMock.addUserToRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(trueEither);

        String url = "/users";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, user);

        assertEquals(201, httpResponse.getStatus());
    }

    @Test
    public void testCreateUser_NOK_technicalException() {

        User user = UserFactoryForTests.getDefaultTestUser();

        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, Boolean> leftEither = Either.left(technicalException);
        Either<IException, Boolean> trueEither = Either.right(true);

        Mockito.when(userBusinessMock.createUser(Mockito.any(User.class))).thenReturn(leftEither);
        Mockito.when(tokenBusinessMock.generateAndSaveTokensForAnAPIUser(Mockito.any(User.class))).thenReturn(trueEither);
        Mockito.when(roleBusinessMock.addUserToRole(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(trueEither);

        String url = "/users";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, user);

        assertEquals(500, httpResponse.getStatus());

        IException exception = httpResponse.getEntity(IException.class);
        assertEquals(0, technicalException.compareTo(exception));
    }

    @Test
    public void testCreateUser_NOK_notValidUser() {

        User user = UserFactoryForTests.getNullUser();

        String url = "/users";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, user);

        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void testAuthenticateUser_OK() {

        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(userBusinessMock.authenticateUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(trueEither);
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> userEither = Either.right(user);
        Mockito.when(userBusinessMock.getUserByUsername(Mockito.anyString(), Mockito.anyString())).thenReturn(userEither);
        
        String tokenValue = "Hello!123";
        String username = user.getUsername();
        Token testingToken = new Token(tokenValue, username, user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> testingTokenEither = Either.right(testingToken);

        Mockito.when(tokenBusinessMock.generateToken(Mockito.any(User.class))).thenReturn(testingTokenEither);

        String url = "/users/" + username + "/tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, user);

        assertEquals(201, httpResponse.getStatus());

        Token generatedToken = httpResponse.getEntity(Token.class);

        assertNotNull(generatedToken);
        assertEquals(0, testingToken.compareTo(generatedToken));
    }

    @Test
    public void testAuthenticateUser_NOK_notAuthenticatedUser() {

        // The user is not authenticated.
        Either<IException, Boolean> falseEither = Either.right(false);
        Mockito.when(userBusinessMock.authenticateUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(falseEither);

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> userEither = Either.right(user);
        Mockito.when(userBusinessMock.getUserByUsername(Mockito.anyString(), Mockito.anyString())).thenReturn(userEither);
        
        String tokenValue = "Hello!123";
        String username = user.getUsername();
        Token testingToken = new Token(tokenValue, username, user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> testingTokenEither = Either.right(testingToken);

        Mockito.when(tokenBusinessMock.generateToken((User) Mockito.any())).thenReturn(testingTokenEither);

        String url = "/users/" + username + "/tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, user);

        assertEquals(401, httpResponse.getStatus());
    }

    @Test
    public void testAuthenticateUser_NOK__nullUser() {

        // The user is not authenticated.
        Either<IException, Boolean> falseEither = Either.right(false);
        Mockito.when(userBusinessMock.authenticateUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(falseEither);

        User user = UserFactoryForTests.getDefaultTestUser();
        String tokenValue = "Hello!123";
        String username = user.getUsername();
        Token testingToken = new Token(tokenValue, username, user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(),user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> testingTokenEither = Either.right(testingToken);

        Mockito.when(tokenBusinessMock.generateToken((User) Mockito.any())).thenReturn(testingTokenEither);

        User nullUser = UserFactoryForTests.getNullUser();
        String url = "/users/" + username + "/tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, nullUser); // Send a null user.

        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void testGetUserByUsername_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> userEither = Either.right(user);
        Mockito.when(userBusinessMock.getUserByUsername(user.getUsername(), user.getOrganizationId())).thenReturn(userEither);

        String url = "/users/" + user.getUsername() + "/" + user.getOrganizationId();

        ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);

        assertEquals(200, httpResponse.getStatus());

        User retrievedUser = httpResponse.getEntity(User.class);
        assertEquals(0, user.compareTo(retrievedUser));
    }

    @Test
    public void testGetUserByUsername_NOK_emptyUsername() {

        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> userEither = Either.right(user);
        Mockito.when(userBusinessMock.getUserByUsername(user.getUsername(), user.getOrganizationId())).thenReturn(userEither);

        String emptyUsername = "";
        String url = "/users/" + emptyUsername;

        ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);

        /* 
         * The status code should be 200 because we are making the GET request to /users/"", 
         * which is translated to GET /users/, and this is the reason we get an empty list.
         */
        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void testGetUserByUsername_NOK_nullUsername() {

        IException businessException = new BusinessException("User not found.");
        Either<IException, User> userEither = Either.left(businessException);
        Mockito.when(userBusinessMock.getUserByUsername(Mockito.anyString(), Mockito.anyString())).thenReturn(userEither);

        String nullUsername = null;
        String url = "/users/" + nullUsername + "/certicamara";

        ClientResponse httpResponse = getDefaultHttpRequest(url).get(ClientResponse.class);

        /*
         * The status code should be 200 because we are making a GET request to /users/null,
         * and even if the user does not exist because GET is an idempotent operation.
         */
        assertEquals(404, httpResponse.getStatus());

        IException exception = httpResponse.getEntity(IException.class);
        assertNotNull(exception);
        assertEquals(0, businessException.compareTo(exception));
    }

    @Test
    public void testUpdateUser_OK() {

        User oldUser = UserFactoryForTests.getDefaultTestUser();
        User updatedUser = UserFactoryForTests.getDefaultTestUser(1);

        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(userBusinessMock.updateUser(Mockito.anyString(), Mockito.any(User.class))).thenReturn(trueEither);

        String url = "/users/" + oldUser.getUsername();
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, updatedUser);
        assertEquals(200, httpResponse.getStatus());
    }

    @Test
    public void testUpdateUser_NOK_notUpdatedUser() {

        User oldUser = UserFactoryForTests.getDefaultTestUser();
        User updatedUser = UserFactoryForTests.getDefaultTestUser(1);

        Either<IException, Boolean> falseEither = Either.right(false);
        Mockito.when(userBusinessMock.updateUser(Mockito.anyString(), Mockito.any(User.class))).thenReturn(falseEither);

        String url = "/users/" + oldUser.getUsername();
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, updatedUser);
        assertEquals(500, httpResponse.getStatus());
    }

    @Test
    public void testUpdateUser_NOK_updateWithNullUser() {

        User oldUser = UserFactoryForTests.getDefaultTestUser();
        User updatedUser = UserFactoryForTests.getNullUser();

        String url = "/users/" + oldUser.getUsername();
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, updatedUser);
        assertEquals(400, httpResponse.getStatus());
    }

    @Test
    public void testDeleteUser_OK() {

        User user = UserFactoryForTests.getDefaultTestUser();

        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(userBusinessMock.deleteUser(Mockito.anyString())).thenReturn(trueEither);

        String url = "/users/" + user.getUsername();
        ClientResponse httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);

        assertEquals(200, httpResponse.getStatus());
    }

    @Test
    public void testDeleteUser_NOK_notDeletedUser() {

        User user = UserFactoryForTests.getDefaultTestUser();

        Either<IException, Boolean> falseEither = Either.right(false);
        Mockito.when(userBusinessMock.deleteUser(Mockito.anyString())).thenReturn(falseEither);

        String url = "/users/" + user.getUsername();
        ClientResponse httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);

        assertEquals(500, httpResponse.getStatus());
    }

    @Test
    public void testDeleteUser_NOK_emptyUsername() {

        String emptyUsername = "";
        String url = "/users/" + emptyUsername;

        ClientResponse httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);

        /*
         * The status code should be 405 because we are making a DELETE request to /users/"",
         * that url gets translated to /users/ so we are trying to execute the DELETE verb over /users/
         * and that url does not support the DELETE operation.
         */
        assertEquals(405, httpResponse.getStatus());
    }

    @Test
    public void testDeleteUser_NOK_nullUsername() {

        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(userBusinessMock.deleteUser(Mockito.anyString())).thenReturn(trueEither);

        String nullUsername = null;
        String url = "/users/" + nullUsername;

        ClientResponse httpResponse = getDefaultHttpRequest(url).delete(ClientResponse.class);

        /*
         * The status code should be 200 because we are making a DELETE request to /users/null,
         * and even if the user does not exist because DELETE is an idempotent operation.
         */
        assertEquals(200, httpResponse.getStatus());
    }

    ///////////////////////////////
    // GetUsersByRoleId
    ///////////////////////////////

    @Test
    public void testGetUsersByRoleId_OK() {

        int listSize = 5;
        List<User> userList = UserFactoryForTests.getUserList(listSize);
        Either<IException, List<User>> userListEither = Either.right(userList);

//        List<User> expectedTasksList = new ArrayList<User>();
//
//        User user2 = new User("2", "2", "2", "2", "2", "2", true,"2", "2", "sr", "br");
//        User user3 = new User("3", "3", "3", "3", "3", "3", true,"3", "3","sr", "br");
//        User user4 = new User("4", "4", "4", "4", "4", "4", true,"4", "4", "sr", "br");
//
//        expectedTasksList.add(user2);
//        expectedTasksList.add(user3);
//        expectedTasksList.add(user4);

        Mockito.when(userBusinessMock.getUsersByRoleId("mao","organization")).thenReturn(userListEither);

        String url = "/users?roleId=mao&tenantId=organization";

        // getDefaultHttpRequest(url).get(ClientResponse.class);
        ClientResponse httpResponse =resources.client().resource(url).get(ClientResponse.class);

        /*
         * The status code should be 200 because we are making a DELETE request to /users/null,
         * and even if the user does not exist because DELETE is an idempotent operation.
         */
        assertEquals(200, httpResponse.getStatus());
    }
    
    ///////////////////////////////
    // generatePasswordToken
    ///////////////////////////////
    
    @Test
    public void generatePasswordTokenTest_OK() {
        
        CreatePasswordToken createPasswordToken = new CreatePasswordToken("email", "certicamara", true);

        Either<IException, Boolean> passwordGeneratedEither = Either.right(true);
        Mockito.when(passwordTokenBusinessMock.generatePasswordToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(passwordGeneratedEither);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, createPasswordToken);
        assertEquals(200, httpResponse.getStatus());
        
    }
    
    @Test
    public void generatePasswordTokenTest_OK_business_exception() {
        
        CreatePasswordToken createPasswordToken = new CreatePasswordToken("email", "certicamara", true);

        BusinessException businessException = new BusinessException("error generating password token");
        Either<IException, Boolean> passwordGeneratedEither = Either.left(businessException);
        Mockito.when(passwordTokenBusinessMock.generatePasswordToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(passwordGeneratedEither);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, createPasswordToken);
        assertEquals(418, httpResponse.getStatus());
        
    }
    
    @Test
    public void generatePasswordTokenTest_OK_technical_exception() {
        
        CreatePasswordToken createPasswordToken = new CreatePasswordToken("email", "certicamara", true);

        TechnicalException technicalException = new TechnicalException("error generating password token");
        Either<IException, Boolean> passwordGeneratedEither = Either.left(technicalException);
        Mockito.when(passwordTokenBusinessMock.generatePasswordToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyBoolean())).thenReturn(passwordGeneratedEither);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, createPasswordToken);
        assertEquals(500, httpResponse.getStatus());
        
    }
    
    @Test
    public void generatePasswordTokenTest_OK_invalid_email() {
        
        CreatePasswordToken createPasswordToken = new CreatePasswordToken("", "certicamara", true);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, createPasswordToken);
        assertEquals(400, httpResponse.getStatus());
        
    }
    
    @Test
    public void generatePasswordTokenTest_OK_invalid_organization() {
        
        CreatePasswordToken createPasswordToken = new CreatePasswordToken("email", "", true);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).post(ClientResponse.class, createPasswordToken);
        assertEquals(400, httpResponse.getStatus());
        
    }
    
    ///////////////////////////////
    // changePasswordGivenPasswordToken
    ///////////////////////////////
    
    @Test
    public void changePasswordGivenPasswordTokenTest_OK() {
        
        ChangePasswordGivenPasswordToken changePassword = new ChangePasswordGivenPasswordToken("passwordToken", "new password", "certicamara");

        Either<IException, Boolean> passwordChengedEither = Either.right(true);
        Mockito.when(passwordTokenBusinessMock.changePasswordGivenPasswordToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordChengedEither);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, changePassword);
        assertEquals(200, httpResponse.getStatus());
        
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_OK_business_exception() {
        
        ChangePasswordGivenPasswordToken changePassword = new ChangePasswordGivenPasswordToken("passwordToken", "new password", "certicamara");

        BusinessException businessException = new BusinessException("error changing password");
        Either<IException, Boolean> passwordChengedEither = Either.left(businessException);
        Mockito.when(passwordTokenBusinessMock.changePasswordGivenPasswordToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordChengedEither);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, changePassword);
        assertEquals(418, httpResponse.getStatus());
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_OK_technical_exception() {
        
        ChangePasswordGivenPasswordToken changePassword = new ChangePasswordGivenPasswordToken("passwordToken", "new password", "certicamara");

        TechnicalException technicalException = new TechnicalException("error changing password");
        Either<IException, Boolean> passwordChengedEither = Either.left(technicalException);
        Mockito.when(passwordTokenBusinessMock.changePasswordGivenPasswordToken(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordChengedEither);

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, changePassword);
        assertEquals(500, httpResponse.getStatus());
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_OK_invalid_password_token() {
        
        ChangePasswordGivenPasswordToken changePassword = new ChangePasswordGivenPasswordToken("", "new password", "certicamara");

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, changePassword);
        assertEquals(400, httpResponse.getStatus());
        
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_OK_invalid_password() {
        
        ChangePasswordGivenPasswordToken changePassword = new ChangePasswordGivenPasswordToken("passwordToken", "", "certicamara");

        String url = "/users/password-tokens";
        ClientResponse httpResponse = getDefaultHttpRequest(url).put(ClientResponse.class, changePassword);
        assertEquals(400, httpResponse.getStatus());
        
    }
}