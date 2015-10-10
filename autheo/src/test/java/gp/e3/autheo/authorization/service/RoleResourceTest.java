package gp.e3.autheo.authorization.service;

import static org.junit.Assert.*;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.business.RoleBusiness;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.domain.entities.Role;
import gp.e3.autheo.authorization.service.representations.StringMessage;
import gp.e3.autheo.authorization.service.resources.RoleResource;
import gp.e3.autheo.util.RoleFactoryForTests;
import gp.e3.autheo.util.UserFactoryForTests;
import io.dropwizard.testing.junit.ResourceTestRule;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource.Builder;

public class RoleResourceTest {

	private static final RoleBusiness roleBusinessMock = Mockito.mock(RoleBusiness.class);
	private static final RoleResource roleResource = new RoleResource(roleBusinessMock);
	
	@ClassRule
	public static final ResourceTestRule resources = ResourceTestRule.builder().addResource(roleResource).build();
	
	private Builder getDefaultHttpRequest(String url) {

		return resources.client().resource(url).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
	}
	
	@Before
	public void setUp() {
		
	}

	@After
	public void tearDown() {
		
		Mockito.reset(roleBusinessMock);
	}

	@Test
	public void testCreateRole_OK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		Either<IException, Role> roleEither = Either.right(role);
		Mockito.when(roleBusinessMock.createRole((Role) Mockito.any())).thenReturn(roleEither);

		String url = "/roles";
		ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, role);

		assertEquals(201, response.getStatus());
		assertEquals(role.getName(), response.getEntity(Role.class).getName());
	}

	@Test
	public void testCreateRole_NOK_businessException() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		IException businessException = new BusinessException("A role with the given name alredy exists.");
		Either<IException, Role> roleEither = Either.left(businessException);
		Mockito.when(roleBusinessMock.createRole((Role) Mockito.any())).thenReturn(roleEither);

		String url = "/roles";
		ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, role);

		assertEquals(500, response.getStatus());
		
		IException exception = response.getEntity(IException.class);
		assertNotNull(exception);
		assertEquals(0, businessException.compareTo(exception));
	}

	@Test
	public void testCreateRole_NOK_emptyRole() {

		Role role = new Role(null, null);
		String url = "/roles";
		ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, role);

		assertEquals(400, response.getStatus());
		
		StringMessage stringMessage = response.getEntity(StringMessage.class);
		assertEquals(false, StringUtils.isBlank(stringMessage.getMessage()));
	}

	@Test
	public void testGetRoleByName_OK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		Either<IException, Role> roleEither = Either.right(role);
		Mockito.when(roleBusinessMock.getRoleByName((String) Mockito.any())).thenReturn(roleEither);

		String url = "/roles/" + role.getName();
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

		assertEquals(200, response.getStatus());
		assertEquals(role.getName(), response.getEntity(Role.class).getName());
	}

	@Test
	public void testGetRoleByName_NOK_businessException() {

		Role nonExistentRole = RoleFactoryForTests.getDefaultTestRole();

		IException businessException = new BusinessException("Role not found.");
		Either<IException, Role> roleEither = Either.left(businessException);
		Mockito.when(roleBusinessMock.getRoleByName((String) Mockito.any())).thenReturn(roleEither);

		String url = "/roles/" + nonExistentRole.getName();
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);
		assertEquals(500, response.getStatus());
		
		IException exception = response.getEntity(IException.class);
		assertNotNull(exception);
		assertEquals(0, businessException.compareTo(exception));
	}

	@Test
	public void testGetRoleByName_NOK_2() {

		String invalidRoleName = "";
		
		List<String> emptyList = new ArrayList<String>();
		Either<IException, List<String>> emptyListEither = Either.right(emptyList);
		Mockito.when(roleBusinessMock.getAllRolesNames()).thenReturn(emptyListEither);

		String url = "/roles/" + invalidRoleName;
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

		// Gets 200 because the request goes this way: /roles/"" -> /roles
		assertEquals(200, response.getStatus());
	}

	@Test
	public void testGetAllRolesNames_OK() {

		List<String> roleNamesList = new ArrayList<String>();
		roleNamesList.add("admin");
		roleNamesList.add("tester");
		
		Either<IException, List<String>> roleNamesListEither = Either.right(roleNamesList);
		Mockito.when(roleBusinessMock.getAllRolesNames()).thenReturn(roleNamesListEither);

		String url = "/roles";
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

		assertEquals(200, response.getStatus());
		assertEquals(roleNamesList.size(), response.getEntity(String[].class).length);
	}

	@Test
	public void testGetAllRolesNames_NOK() {

		List<String> emptyRoleNamesList = new ArrayList<String>();
		Either<IException, List<String>> emptyRoleNamesListEither = Either.right(emptyRoleNamesList);
		Mockito.when(roleBusinessMock.getAllRolesNames()).thenReturn(emptyRoleNamesListEither);

		String url = "/roles";
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);

		assertEquals(200, response.getStatus());
		assertEquals(0, response.getEntity(String[].class).length);
	}

	@Test
	public void testUpdateRole_OK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		Role updatedRole = RoleFactoryForTests.getDefaultTestRole(3);

		String url = "/roles/" + role.getName();
		ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, updatedRole);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testUpdateRole_NOK_1() {

		Role updatedRole = RoleFactoryForTests.getDefaultTestRole(3);

		String emptyRoleName = "";
		String url = "/roles/" + emptyRoleName;
		ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, updatedRole);

		// The operation PUT is not supported on /roles
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testUpdateRole_NOK_2() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		Role updatedRole = new Role(null);

		String url = "/roles/" + role.getName();
		ClientResponse response = getDefaultHttpRequest(url).put(ClientResponse.class, updatedRole);

		assertEquals(400, response.getStatus());
	}

	@Test
	public void testDeleteRole_OK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();

		String url = "/roles/" + role.getName();
		ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);

		assertEquals(200, response.getStatus());
	}

	@Test
	public void testDeleteRole_NOK() {

		String emptyRoleName = "";
		String url = "/roles/" + emptyRoleName;

		ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);

		// The operation DELETE is not supported on /roles
		assertEquals(405, response.getStatus());
	}

	@Test
	public void testAddUserToRole_OK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		User user = UserFactoryForTests.getDefaultTestUser();

		String url = "/roles/" + role.getName() + "/users";
		ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, user);
		
		assertEquals(201, response.getStatus());
	}
	
	@Test
	public void testAddUserToRole_NOK_1() {

		User user = UserFactoryForTests.getDefaultTestUser();

		String emptyUsername = "";
		String url = "/roles/" + emptyUsername + "/users";
		ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, user);
		
		// Does not found the resource which match the route: /roles//users
		assertEquals(404, response.getStatus());
	}
	
	@Test
	public void testAddUserToRole_NOK_2() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		User user = UserFactoryForTests.getNullUser();

		String url = "/roles/" + role.getName() + "/users";
		ClientResponse response = getDefaultHttpRequest(url).post(ClientResponse.class, user);
		
		assertEquals(400, response.getStatus());
	}
	
	@Test
	public void testRemoveUserFromRole_OK() {
		
		Role role = RoleFactoryForTests.getDefaultTestRole();
		User user = UserFactoryForTests.getDefaultTestUser();
		
		String url = "/roles/" + role.getName() + "/users/" + user.getUsername();
		ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);
		
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testRemoveUserFromRole_NOK_1() {
		
		Role role = RoleFactoryForTests.getDefaultTestRole();
		String emptyUsername = "";
		
		String url = "/roles/" + role.getName() + "/users/" + emptyUsername;
		ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);
		
		// DELETE is not supported on /roles/roleName/users
		assertEquals(405, response.getStatus());
	}
	
	@Test
	public void testRemoveUserFromRole_NOK_2() {
		
		Role role = RoleFactoryForTests.getDefaultTestRole();
		String nullUsername = null;
		
		String url = "/roles/" + role.getName() + "/users/" + nullUsername;
		ClientResponse response = getDefaultHttpRequest(url).delete(ClientResponse.class);
		
		// The username is parsed as "null" 
		assertEquals(200, response.getStatus());
	}
	
	@Test
	public void testGetAllPermissionsOfAGivenRole_OK() {
		
		int numberOfPermissions = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(numberOfPermissions);
		List<Permission> rolePermissions = role.getPermissions();
		Either<IException, List<Permission>> rolePermissionsEither = Either.right(rolePermissions);
		
        Mockito.when(roleBusinessMock.getAllPermissionsOfAGivenRole(role.getName())).thenReturn(rolePermissionsEither);
		
		String url = "/roles/" + role.getName() + "/permissions";
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);
		
		assertEquals(200, response.getStatus());
		assertEquals(numberOfPermissions, rolePermissions.size());
		assertEquals(rolePermissions.size(), response.getEntity(List.class).size());
	}
	
	@Test
	public void testGetAllPermissionsOfAGivenRole_NOK() {
		
		int numberOfPermissions = 0;
		Role role = RoleFactoryForTests.getDefaultTestRole(numberOfPermissions);
		List<Permission> rolePermissions = role.getPermissions();
		Either<IException, List<Permission>> rolePermissionsEither = Either.right(rolePermissions);
		
        Mockito.when(roleBusinessMock.getAllPermissionsOfAGivenRole(role.getName())).thenReturn(rolePermissionsEither);
		
		String url = "/roles/" + role.getName() + "/permissions";
		ClientResponse response = getDefaultHttpRequest(url).get(ClientResponse.class);
		
		assertEquals(200, response.getStatus());
		assertEquals(numberOfPermissions, rolePermissions.size());
		assertEquals(rolePermissions.size(), response.getEntity(List.class).size());
	}
}