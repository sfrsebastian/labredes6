package gp.e3.autheo.authorization.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.TokenBusiness;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authorization.domain.entities.Ticket;
import gp.e3.autheo.authorization.infrastructure.constants.EnumRoleConstants;
import gp.e3.autheo.authorization.infrastructure.dtos.PermissionTuple;
import gp.e3.autheo.util.TicketFactoryForTests;
import gp.e3.autheo.util.UserFactoryForTests;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class TicketBusinessTest {

    private RoleBusiness roleBusinessMock;
    private TokenBusiness tokenBusinessMock;

    private TicketBusiness ticketBusiness;

    @Before
    public void setUp() {

        roleBusinessMock = Mockito.mock(RoleBusiness.class);
        tokenBusinessMock = Mockito.mock(TokenBusiness.class);

        ticketBusiness = new TicketBusiness(tokenBusinessMock, roleBusinessMock);
    }

    @After
    public void tearDown() {

        roleBusinessMock = null;
        tokenBusinessMock = null;

        ticketBusiness = null;
    }

    @Test
    public void testTokenWasIssuedByUs_OK() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();

        User user = UserFactoryForTests.getDefaultTestUser();
        String tokenValue = ticket.getTokenValue();
        Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> tokenEither = Either.right(token);

        Mockito.when(tokenBusinessMock.getAPIToken(tokenValue)).thenReturn(tokenEither);

        Either<IException, Token> retrievedTokenEither = ticketBusiness.getAPITokenByTokenValue(tokenValue);
        assertEquals(true, retrievedTokenEither.isRight());

        Token retrievedToken = retrievedTokenEither.right().value();
        assertTrue(retrievedToken != null);
    }

    @Test
    public void testTokenWasIssuedByUs_NOK_notValidTokenValue() {

        String tokenValue = "";
        IException businessException = new BusinessException("The given token value is empty.");
        Either<IException, Token> tokenEither = Either.left(businessException);

        Mockito.when(tokenBusinessMock.getAPIToken(tokenValue)).thenReturn(tokenEither);
        Either<IException, Token> retrievedTokenEither = ticketBusiness.getAPITokenByTokenValue(tokenValue);
        assertEquals(false, retrievedTokenEither.isRight());

        IException exception = retrievedTokenEither.left().value();
        assertNotNull(exception);
        assertEquals(0, businessException.compareTo(exception));
    }

    @Test
    public void testUserIsAuthorized_OK_1() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();

        User user = UserFactoryForTests.getDefaultTestUser();
        Token token = new Token(ticket.getTokenValue(), user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), 
                user.getBusinessRoleId(), user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> tokenEither = Either.right(token);

        String roleName = token.getSystemRole();

        int numberOfPermissions = 3;
        List<PermissionTuple> permissionTuples = new ArrayList<PermissionTuple>();

        for (int i = 0; i < numberOfPermissions; i++) {

            PermissionTuple permissionTuple = new PermissionTuple("GET", "www.google.com" + i);
            permissionTuples.add(permissionTuple);
        }

        // Add the permission requested in the ticket.
        permissionTuples.add(new PermissionTuple(ticket.getHttpVerb(), ticket.getRequestedUrl()));

        Mockito.when(tokenBusinessMock.getAPIToken(ticket.getTokenValue())).thenReturn(tokenEither);
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(roleName)).thenReturn(true);
        Mockito.when(roleBusinessMock.getRolePermissionsFromRedis(roleName)).thenReturn(permissionTuples);

        Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
        assertEquals(true, userIsAuthorizedEither.isRight());
        
        Boolean userIsAuthorized = userIsAuthorizedEither.right().value();
        assertEquals(true, userIsAuthorized);
    }

    @Test
    public void testUserIsAuthorized_OK_2() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();

        User user = UserFactoryForTests.getDefaultTestUser();
        Token token = new Token(ticket.getTokenValue(), user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), 
                user.getBusinessRoleId(), user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> tokenEither = Either.right(token);

        String roleName = token.getSystemRole();
        
        int numberOfPermissions = 3;
        List<PermissionTuple> permissionTuples = new ArrayList<PermissionTuple>();

        for (int i = 0; i < numberOfPermissions; i++) {

            PermissionTuple permissionTuple = new PermissionTuple("GET", "www.google.com" + i);
            permissionTuples.add(permissionTuple);
        }

        // Add the permission requested in the ticket.
        permissionTuples.add(new PermissionTuple(ticket.getHttpVerb(), ticket.getRequestedUrl()));
        Mockito.when(tokenBusinessMock.getAPIToken(ticket.getTokenValue())).thenReturn(tokenEither);
        
        // First time say return false.
        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(roleName)).thenReturn(false);
        Mockito.when(roleBusinessMock.addRolePermissionsToRedis(roleName)).thenReturn(trueEither);
        
        // Second time return true.
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(roleName)).thenReturn(true);
        Mockito.when(roleBusinessMock.getRolePermissionsFromRedis(roleName)).thenReturn(permissionTuples);

        Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
        assertEquals(true, userIsAuthorizedEither.isRight());
        
        Boolean userIsAuthorized = userIsAuthorizedEither.right().value();
        assertEquals(true, userIsAuthorized);
    }

    @Test
    public void testUserIsAuthorized_NOK_1() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();

        User user = UserFactoryForTests.getDefaultTestUser();
        Token token = new Token(ticket.getTokenValue(), user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(),  
                user.getBusinessRoleId(), user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> tokenEither = Either.right(token);
        
        String roleName = token.getSystemRole();

        List<PermissionTuple> permissionTuples = new ArrayList<PermissionTuple>();

        Mockito.when(tokenBusinessMock.getAPIToken(ticket.getTokenValue())).thenReturn(tokenEither);
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(roleName)).thenReturn(true);

        // Return an empty list of permission tuples.
        Mockito.when(roleBusinessMock.getRolePermissionsFromRedis(roleName)).thenReturn(permissionTuples);

        Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
        assertEquals(true, userIsAuthorizedEither.isRight());
        
        Boolean userIsAuthorized = userIsAuthorizedEither.right().value();
        assertEquals(false, userIsAuthorized);
    }

    @Test
    public void testUserIsAuthorized_NOK_2() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();

        User user = UserFactoryForTests.getDefaultTestUser();
        Token token = new Token(ticket.getTokenValue(), user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.TEMPORAL_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), 
                user.getBusinessRoleId(), user.getDocumentType(), user.getDocumentNumber());
        Either<IException, Token> tokenEither = Either.right(token);

        String roleName = token.getSystemRole();

        int numberOfPermissions = 3;
        List<PermissionTuple> permissionTuples = new ArrayList<PermissionTuple>();

        for (int i = 0; i < numberOfPermissions; i++) {

            PermissionTuple permissionTuple = new PermissionTuple("GET", "www.google.com" + i);
            permissionTuples.add(permissionTuple);
        }

        // Add the permission requested in the ticket.
        permissionTuples.add(new PermissionTuple(ticket.getHttpVerb(), ticket.getRequestedUrl()));

        Mockito.when(tokenBusinessMock.getAPIToken(ticket.getTokenValue())).thenReturn(tokenEither);
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(roleName)).thenReturn(false);
        
        IException technicalException = new TechnicalException("Could not access to the tokens cache.");
        Either<IException, Boolean> falseEither = Either.left(technicalException);
        Mockito.when(roleBusinessMock.addRolePermissionsToRedis(roleName)).thenReturn(falseEither);

        Either<IException, Boolean> userIsAuthorizedEither = ticketBusiness.userIsAuthorized(ticket);
        assertEquals(false, userIsAuthorizedEither.isRight());
        
        IException exception = userIsAuthorizedEither.left().value();
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }

    @Test
    public void testIsPublicPermission_OK_1() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
        String httpVerb = ticket.getHttpVerb();
        String requestedUrl = ticket.getRequestedUrl();

        String publicRole = EnumRoleConstants.PUBLIC_ROLE.getRoleName();
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(publicRole)).thenReturn(true);

        List<PermissionTuple> permissionsFromRedis = new ArrayList<PermissionTuple>();
        permissionsFromRedis.add(new PermissionTuple(httpVerb, requestedUrl));
        Mockito.when(roleBusinessMock.getRolePermissionsFromRedis(publicRole)).thenReturn(permissionsFromRedis);

        Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(httpVerb, requestedUrl);
        assertEquals(true, isPublicPermissionEither.isRight());
        
        Boolean isPublicPermission = isPublicPermissionEither.right().value();
        assertEquals(true, isPublicPermission);
    }

    @Test
    public void testIsPublicPermission_OK_2() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
        String httpVerb = ticket.getHttpVerb();
        String requestedUrl = ticket.getRequestedUrl();

        String publicRole = EnumRoleConstants.PUBLIC_ROLE.getRoleName();
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(publicRole)).thenReturn(false).thenReturn(true);
        
        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(roleBusinessMock.addRolePermissionsToRedis(publicRole)).thenReturn(trueEither);

        List<PermissionTuple> permissionsFromRedis = new ArrayList<PermissionTuple>();
        permissionsFromRedis.add(new PermissionTuple(httpVerb, requestedUrl));
        Mockito.when(roleBusinessMock.getRolePermissionsFromRedis(publicRole)).thenReturn(permissionsFromRedis);

        Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(httpVerb, requestedUrl);
        assertEquals(true, isPublicPermissionEither.isRight());
        
        Boolean isPublicPermission = isPublicPermissionEither.right().value();
        assertEquals(true, isPublicPermission);
    }

    @Test
    public void testIsPublicPermission_NOK_1() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
        String httpVerb = ticket.getHttpVerb();
        String requestedUrl = ticket.getRequestedUrl();

        String publicRole = EnumRoleConstants.PUBLIC_ROLE.getRoleName();
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(publicRole)).thenReturn(true);

        List<PermissionTuple> permissionsFromRedis = new ArrayList<PermissionTuple>();
        permissionsFromRedis.add(new PermissionTuple(httpVerb, "unknownUrl"));
        Mockito.when(roleBusinessMock.getRolePermissionsFromRedis(publicRole)).thenReturn(permissionsFromRedis);

        Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(httpVerb, requestedUrl);
        assertEquals(true, isPublicPermissionEither.isRight());
        
        Boolean isPublicPermission = isPublicPermissionEither.right().value();
        assertEquals(false, isPublicPermission);
    }

    @Test
    public void testIsPublicPermission_NOK_2() {

        Ticket ticket = TicketFactoryForTests.getDefaultTestTicket();
        String httpVerb = ticket.getHttpVerb();
        String requestedUrl = ticket.getRequestedUrl();

        String publicRole = EnumRoleConstants.PUBLIC_ROLE.getRoleName();
        Mockito.when(roleBusinessMock.rolePermissionsAreInRedis(publicRole)).thenReturn(false);
        
        IException technicalException = new TechnicalException("Could not access to redis.");
        Either<IException, Boolean> falseEither = Either.left(technicalException);
        Mockito.when(roleBusinessMock.addRolePermissionsToRedis(publicRole)).thenReturn(falseEither);

        Either<IException, Boolean> isPublicPermissionEither = ticketBusiness.isPublicPermission(httpVerb, requestedUrl);
        assertEquals(false, isPublicPermissionEither.isRight());
        
        IException exception = isPublicPermissionEither.left().value();
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }
}