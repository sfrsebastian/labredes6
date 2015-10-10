package gp.e3.autheo.authentication.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.infrastructure.utils.email.EmailUtil;
import gp.e3.autheo.authentication.persistence.daos.PasswordTokenDAO;
import gp.e3.autheo.authentication.persistence.daos.UserDAO;
import gp.e3.autheo.util.UserFactoryForTests;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({EmailUtil.class, PasswordHandler.class})
public class PasswordTokenBusinessTest {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private PasswordTokenBusiness passwordTokenBusiness;
    
    private Connection dbConnectionMock;
    private BasicDataSource dataSourceMock;
    
    private PasswordTokenDAO passwordTokenDaoMock;
    
    private UserDAO userDaoMock;

    ////////////////////////
    // Constructor
    ////////////////////////
    
    @Before
    public void setUp() {

        dbConnectionMock = Mockito.mock(Connection.class);
        dataSourceMock = Mockito.mock(BasicDataSource.class);
        
        try {
            Mockito.when(dataSourceMock.getConnection()).thenReturn(dbConnectionMock);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        passwordTokenDaoMock = Mockito.mock(PasswordTokenDAO.class);
        userDaoMock = Mockito.mock(UserDAO.class);

        passwordTokenBusiness = new PasswordTokenBusiness(dataSourceMock, passwordTokenDaoMock, userDaoMock);
    }
    
    @After
    public void tearDown() {
        
        dataSourceMock = null;
        dbConnectionMock = null;
        
        passwordTokenDaoMock = null;
        userDaoMock = null;
        passwordTokenBusiness = null;
        
    }

    ////////////////////////
    // Public Methods
    ////////////////////////
    
    // generatePasswordToken
    
    @Test
    public void generatePasswordTokenTest_OK() {
        
        PowerMockito.mockStatic(EmailUtil.class);
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> getUserEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        BusinessException businessException = new BusinessException("The password token does not exist");
        Either<IException, String> passwordTokenEither = Either.left(businessException);
        Mockito.when(passwordTokenDaoMock.getPasswordTokenByUsername(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenEither);
        
        Either<IException, Integer> passwordTokenCreatedEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.createPasswordToken(Mockito.any(), Mockito.any())).thenReturn(passwordTokenCreatedEither);
        
        Either<IException, Boolean> emailWasSentEither = Either.right(true);
        Mockito.when(EmailUtil.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(emailWasSentEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.right());
        
        Boolean generatedPasswordToken = generatedPasswordTokenEither.right().value();
        assertEquals(true, generatedPasswordToken);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_OK_token_exists() {
        
        PowerMockito.mockStatic(EmailUtil.class);
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> getUserEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        Either<IException, String> passwordTokenEither = Either.right("passwordToken");
        Mockito.when(passwordTokenDaoMock.getPasswordTokenByUsername(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenEither);
        
        Either<IException, Integer> passwordTokenDeletedEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.deletePasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenDeletedEither);
        
        Either<IException, Integer> passwordTokenCreatedEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.createPasswordToken(Mockito.any(), Mockito.any())).thenReturn(passwordTokenCreatedEither);
        
        Either<IException, Boolean> emailWasSentEither = Either.right(true);
        Mockito.when(EmailUtil.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(emailWasSentEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.right());
        
        Boolean generatedPasswordToken = generatedPasswordTokenEither.right().value();
        assertEquals(true, generatedPasswordToken);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_NOK_error_sending_email() {
        
        PowerMockito.mockStatic(EmailUtil.class);
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> getUserEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        Either<IException, String> passwordTokenEither = Either.right("passwordToken");
        Mockito.when(passwordTokenDaoMock.getPasswordTokenByUsername(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenEither);
        
        Either<IException, Integer> passwordTokenDeletedEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.deletePasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenDeletedEither);
        
        Either<IException, Integer> passwordTokenCreatedEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.createPasswordToken(Mockito.any(), Mockito.any())).thenReturn(passwordTokenCreatedEither);
        
        TechnicalException technicalException = new TechnicalException("error sending email");
        Either<IException, Boolean> emailWasSentEither = Either.left(technicalException);
        Mockito.when(EmailUtil.sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(emailWasSentEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException errorSendingEmail = generatedPasswordTokenEither.left().value();
        assertEquals(true, errorSendingEmail instanceof TechnicalException);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_NOK_error_creating_password_token() {
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> getUserEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        Either<IException, String> passwordTokenEither = Either.right("passwordToken");
        Mockito.when(passwordTokenDaoMock.getPasswordTokenByUsername(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenEither);
        
        Either<IException, Integer> passwordTokenDeletedEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.deletePasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenDeletedEither);
        
        TechnicalException technicalException = new TechnicalException("error creating password token");
        Either<IException, Integer> passwordTokenCreatedEither = Either.left(technicalException);
        Mockito.when(passwordTokenDaoMock.createPasswordToken(Mockito.any(), Mockito.any())).thenReturn(passwordTokenCreatedEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException errorSendingEmail = generatedPasswordTokenEither.left().value();
        assertEquals(true, errorSendingEmail instanceof TechnicalException);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_NOK_error_deleting_password_token() {
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> getUserEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        Either<IException, String> passwordTokenEither = Either.right("passwordToken");
        Mockito.when(passwordTokenDaoMock.getPasswordTokenByUsername(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenEither);
        
        TechnicalException technicalException = new TechnicalException("error deleting password token");
        Either<IException, Integer> passwordTokenDeletedEither = Either.left(technicalException);
        Mockito.when(passwordTokenDaoMock.deletePasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenDeletedEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException errorSendingEmail = generatedPasswordTokenEither.left().value();
        assertEquals(true, errorSendingEmail instanceof TechnicalException);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_NOK_error_looking_for_password_token() {
        
        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, User> getUserEither = Either.right(user);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        TechnicalException technicalException = new TechnicalException("error looking for password token");
        Either<IException, String> passwordTokenEither = Either.left(technicalException);
        Mockito.when(passwordTokenDaoMock.getPasswordTokenByUsername(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(passwordTokenEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException errorSendingEmail = generatedPasswordTokenEither.left().value();
        assertEquals(true, errorSendingEmail instanceof TechnicalException);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_NOK_error_looking_for_user() {
        
        TechnicalException technicalException = new TechnicalException("error looking for password token");
        Either<IException, User> getUserEither = Either.left(technicalException);
        Mockito.when(userDaoMock.getUserByEmail(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(getUserEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException errorSendingEmail = generatedPasswordTokenEither.left().value();
        assertEquals(true, errorSendingEmail instanceof TechnicalException);
        
        
    }
    
    @Test
    public void generatePasswordTokenTest_NOK_error_getting_database_connection() {
        
        try {
            Mockito.doThrow(SQLException.class).when(dataSourceMock).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.generatePasswordToken("email@email.com", "certicamara", true);
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException errorSendingEmail = generatedPasswordTokenEither.left().value();
        assertEquals(true, errorSendingEmail instanceof TechnicalException);
        
        
    }
    
    // changePasswordGivenPasswordToken
    
    @Test
    public void changePasswordGivenPasswordTokenTest_OK() {
        
        PowerMockito.mockStatic(PasswordHandler.class);
        
        Either<IException, String> usernameEither = Either.right("username");
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        try {
            Mockito.when(PasswordHandler.getPasswordHash(Mockito.anyString())).thenReturn("password hash");
            Mockito.when(PasswordHandler.getSaltFromHashedAndSaltedPassword(Mockito.anyString())).thenReturn("salt");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Either<IException, Integer> updatePasswordEither = Either.right(1);
        Mockito.when(userDaoMock.updateUserPassword(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(updatePasswordEither);
        
        Either<IException, Integer> deletePasswordTokenEither = Either.right(1);
        Mockito.when(passwordTokenDaoMock.deletePasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(deletePasswordTokenEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.right());
        
        Boolean generatedPasswordToken = generatedPasswordTokenEither.right().value();
        assertEquals(true, generatedPasswordToken);
        
        
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_error_deleting_token() {
        
        PowerMockito.mockStatic(PasswordHandler.class);
        
        Either<IException, String> usernameEither = Either.right("username");
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        try {
            Mockito.when(PasswordHandler.getPasswordHash(Mockito.anyString())).thenReturn("password hash");
            Mockito.when(PasswordHandler.getSaltFromHashedAndSaltedPassword(Mockito.anyString())).thenReturn("salt");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Either<IException, Integer> updatePasswordEither = Either.right(1);
        Mockito.when(userDaoMock.updateUserPassword(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(updatePasswordEither);
        
        TechnicalException technicalException = new TechnicalException("error deleting password token");
        Either<IException, Integer> deletePasswordTokenEither = Either.left(technicalException);
        Mockito.when(passwordTokenDaoMock.deletePasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(deletePasswordTokenEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
        
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_error_updating_user_password() {
        
        PowerMockito.mockStatic(PasswordHandler.class);
        
        Either<IException, String> usernameEither = Either.right("username");
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        try {
            Mockito.when(PasswordHandler.getPasswordHash(Mockito.anyString())).thenReturn("password hash");
            Mockito.when(PasswordHandler.getSaltFromHashedAndSaltedPassword(Mockito.anyString())).thenReturn("salt");
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        TechnicalException technicalException = new TechnicalException("error deleting password token");
        Either<IException, Integer> updatePasswordEither = Either.left(technicalException);
        Mockito.when(userDaoMock.updateUserPassword(Mockito.any(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(updatePasswordEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_error_getting_salt() {
        
        PowerMockito.mockStatic(PasswordHandler.class);
        
        Either<IException, String> usernameEither = Either.right("username");
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        try {
            Mockito.when(PasswordHandler.getPasswordHash(Mockito.anyString())).thenReturn("password hash");
            Mockito.when(PasswordHandler.getSaltFromHashedAndSaltedPassword(Mockito.anyString())).thenThrow(NoSuchAlgorithmException.class);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_error_getting_password_hash() {
        
        PowerMockito.mockStatic(PasswordHandler.class);
        
        Either<IException, String> usernameEither = Either.right("username");
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        try {
            Mockito.when(PasswordHandler.getPasswordHash(Mockito.anyString())).thenThrow(InvalidKeySpecException.class);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
        
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_error_illegal_argument() {
        
        PowerMockito.mockStatic(PasswordHandler.class);
        
        Either<IException, String> usernameEither = Either.right("username");
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        try {
            Mockito.when(PasswordHandler.getPasswordHash(Mockito.anyString())).thenThrow(IllegalArgumentException.class);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
        
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_error_getting_username() {
        
        TechnicalException technicalException = new TechnicalException("error deleting password token");
        Either<IException, String> usernameEither = Either.left(technicalException);
        Mockito.when(passwordTokenDaoMock.getUsernameByPasswordToken(Mockito.any(), Mockito.anyString(), Mockito.anyString())).thenReturn(usernameEither);
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
    }
    
    @Test
    public void changePasswordGivenPasswordTokenTest_NOK_getting_database_connection() {
        
        try {
            Mockito.doThrow(SQLException.class).when(dataSourceMock).getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        Either<IException, Boolean> generatedPasswordTokenEither = passwordTokenBusiness.changePasswordGivenPasswordToken("password token", "new password", "certicamara");
        
        assertNotNull(generatedPasswordTokenEither);
        assertNotNull(generatedPasswordTokenEither.left());
        
        IException exception = generatedPasswordTokenEither.left().value();
        assertEquals(true, exception instanceof TechnicalException);
        
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
