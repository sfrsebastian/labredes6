package gp.e3.autheo.authorization.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authorization.domain.business.PermissionBusiness;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.service.resources.PermissionResource;
import gp.e3.autheo.util.PermissionFactoryForTests;
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

public class PermissionResourceTest {

    private static final PermissionBusiness permissionBusinessMock = Mockito.mock(PermissionBusiness.class);
    private static final PermissionResource permissionResource = new PermissionResource(permissionBusinessMock);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(permissionResource).build();

    private Builder getDefaultHttpRequest(String url) {

        return resources.client().resource(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {

        Mockito.reset(permissionBusinessMock);
    }

    @Test
    public void testCreatePermission_OK() {

        long expectedPermissionId = 1;
        Either<IException, Long> expectedPermissionIdEither = Either.right(expectedPermissionId);
        Mockito.when(permissionBusinessMock.createPermission((Permission) Mockito.any())).thenReturn(expectedPermissionIdEither);

        String url = "/permissions";
        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
        ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, permission);

        assertEquals(201, response.getStatus());

        long permissionId = response.getEntity(Long.class);
        assertEquals(expectedPermissionId, permissionId);
    }

    @Test
    public void testCreatePermission_NOK_1() {

        IException businessException = new BusinessException("Permission could not be created");
        Either<IException, Long> expectedPermissionIdEither = Either.left(businessException);
        Mockito.when(permissionBusinessMock.createPermission((Permission) Mockito.any())).thenReturn(expectedPermissionIdEither);

        String url = "/permissions";
        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
        ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, permission);
        assertEquals(500, response.getStatus());
        
        IException exception = response.getEntity(IException.class);
        assertEquals(0, businessException.compareTo(exception));
    }

    @Test
    public void testCreatePermission_NOK_2() {

        Permission nullPermission = new Permission(null, null, null);

        String url = "/permissions";
        ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, nullPermission);

        assertEquals(400, response.getStatus());
    }

    @Test
    public void testGetPermissionById_OK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
        Either<IException, Permission> permissionEither = Either.right(permission);

        Mockito.when(permissionBusinessMock.getPermissionById(permission.getId())).thenReturn(permissionEither);

        String url = "/permissions/" + permission.getId();
        ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

        assertEquals(200, response.getStatus());
        assertEquals(0, permission.compareTo(response.getEntity(Permission.class)));
    }

    @Test
    public void testGetPermissionById_NOK_permissionNotFound() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
        
        IException businessException = new BusinessException("Permission not found.");
        Either<IException, Permission> permissionEither = Either.left(businessException);
        Mockito.when(permissionBusinessMock.getPermissionById(permission.getId())).thenReturn(permissionEither);

        String url = "/permissions/" + permission.getId();
        ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);
        assertEquals(404, response.getStatus());

        IException exception = response.getEntity(IException.class);
        assertNotNull(exception);
        assertEquals(0, businessException.compareTo(exception));
    }

    @Test
    public void testGetAllPermissions_OK() {

        int listSize = 5;
        List<Permission> permissionList = PermissionFactoryForTests.getPermissionList(listSize);
        Either<IException, List<Permission>> permissionListEither = Either.right(permissionList);

        Mockito.when(permissionBusinessMock.getAllPermissions()).thenReturn(permissionListEither);

        String url = "/permissions";
        ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

        assertEquals(200, response.getStatus());
        assertEquals(listSize, permissionList.size());

        Permission[] retrievedPermissions = response.getEntity(Permission[].class);
        assertEquals(permissionList.size(), retrievedPermissions.length);
    }

    @Test
    public void testGetAllPermissions_NOK() {

        int listSize = 0;
        List<Permission> permissionList = PermissionFactoryForTests.getPermissionList(listSize);
        Either<IException, List<Permission>> permissionListEither = Either.right(permissionList);

        Mockito.when(permissionBusinessMock.getAllPermissions()).thenReturn(permissionListEither);

        String url = "/permissions";
        ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

        assertEquals(200, response.getStatus());
        assertEquals(listSize, permissionList.size());

        Permission[] retrievedPermissions = response.getEntity(Permission[].class);
        assertEquals(permissionList.size(), retrievedPermissions.length);
    }

    @Test
    public void testDeletePermission_OK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
        long permissionId = permission.getId();
        
        Either<IException, Boolean> trueEither = Either.right(true);
        Mockito.when(permissionBusinessMock.deletePermission(permissionId)).thenReturn(trueEither);
        
        String url = "/permissions/" + permissionId;
        ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);
        assertEquals(200, response.getStatus());
    }

    @Test
    public void testDeletePermission_NOK() {

        Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
        long permissionId = permission.getId();
        
        IException technicalException = new TechnicalException("Could not reach the DB.");
        Either<IException, Boolean> leftEither = Either.left(technicalException);
        Mockito.when(permissionBusinessMock.deletePermission(permissionId)).thenReturn(leftEither);
        
        String url = "/permissions/" + permissionId;
        ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);
        assertEquals(500, response.getStatus());
        
        IException exception = response.getEntity(IException.class);
        assertNotNull(exception);
        assertEquals(0, technicalException.compareTo(exception));
    }
}