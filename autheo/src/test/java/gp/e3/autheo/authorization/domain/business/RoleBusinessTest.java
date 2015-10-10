package gp.e3.autheo.authorization.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.domain.entities.Role;
import gp.e3.autheo.authorization.infrastructure.dtos.PermissionTuple;
import gp.e3.autheo.authorization.persistence.daos.RoleDAO;
import gp.e3.autheo.util.RoleFactoryForTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.gson.Gson;

public class RoleBusinessTest {

	private Connection dbConnectionMock;
	private BasicDataSource dataSourceMock;

	private Jedis redisMock;
	private JedisPool redisPoolMock;

	private RoleDAO roleDaoMock;
	private PermissionBusiness permissionBusinessMock;

	private RoleBusiness roleBusiness;

	@Before
	public void setUp() {

		dbConnectionMock = Mockito.mock(Connection.class);
		dataSourceMock = Mockito.mock(BasicDataSource.class);

		try {
			Mockito.when(dataSourceMock.getConnection()).thenReturn(dbConnectionMock);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		redisMock = Mockito.mock(Jedis.class);
		redisPoolMock = Mockito.mock(JedisPool.class);
		Mockito.when(redisPoolMock.getResource()).thenReturn(redisMock);

		roleDaoMock = Mockito.mock(RoleDAO.class);
		permissionBusinessMock =  Mockito.mock(PermissionBusiness.class);

		roleBusiness = new RoleBusiness(dataSourceMock, redisPoolMock, roleDaoMock, permissionBusinessMock);
	}

	@After
	public void tearDown() {

		dbConnectionMock = null;
		dataSourceMock = null;

		redisMock = null;
		redisPoolMock = null;

		roleDaoMock = null;
		permissionBusinessMock = null;

		roleBusiness = null;
	}

	@Test
	public void testCreateRole_OK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		int affectedRows = 1;
		Either<IException, Integer> affectedRowsEither = Either.right(affectedRows);
		
        Mockito.when(roleDaoMock.createRole(dbConnectionMock, role.getName())).thenReturn(affectedRowsEither);
		Either<IException, Role> createdRoleEither = roleBusiness.createRole(role);
		assertEquals(true, createdRoleEither.isRight());

		Role createdRole = createdRoleEither.right().value();
		assertNotNull(createdRole);
		assertEquals(role.getName(), createdRole.getName());
	}

	@Test
	public void testCreateRole_NOK() {

		Role role = RoleFactoryForTests.getDefaultTestRole();
		int affectedRows = 0;
		Either<IException, Integer> affectedRowsEither = Either.right(affectedRows);
		
        String roleName = role.getName();
        Mockito.when(roleDaoMock.createRole(dbConnectionMock, roleName)).thenReturn(affectedRowsEither);
		Either<IException, Role> createdRoleEither = roleBusiness.createRole(role);
		assertEquals(false, createdRoleEither.isRight());
		
		IException exception = createdRoleEither.left().value();
		assertNotNull(exception);
		assertEquals(true, exception instanceof BusinessException);
	}

	@Test
	public void testGetRoleByName_OK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		String expectedRoleName = role.getName();
		List<Permission> expectedRolePermissions = role.getPermissions();
		Either<IException, List<Permission>> expectedRolePermissionsEither = Either.right(expectedRolePermissions);
		
        Mockito.when(permissionBusinessMock.getAllPermissionsOfAGivenRole(expectedRoleName)).thenReturn(expectedRolePermissionsEither);
		Either<IException, Role> retrievedRoleEither = roleBusiness.getRoleByName(expectedRoleName);
		assertEquals(true, retrievedRoleEither.isRight());

		Role retrievedRole = retrievedRoleEither.right().value();
		assertNotNull(retrievedRole);
		assertEquals(expectedRoleName, retrievedRole.getName());
		assertEquals(listSize, expectedRolePermissions.size());
		assertEquals(expectedRolePermissions.size(), retrievedRole.getPermissions().size());
	}

	@Test
	public void testGetRoleByName_NOK() {

		int listSize = 0;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		String expectedRoleName = role.getName();
		List<Permission> expectedRolePermissions = role.getPermissions();
		Either<IException, List<Permission>> expectedRolePermissionsEither = Either.right(expectedRolePermissions);
		
        Mockito.when(permissionBusinessMock.getAllPermissionsOfAGivenRole(expectedRoleName)).thenReturn(expectedRolePermissionsEither);
		Either<IException, Role> retrievedRoleEither = roleBusiness.getRoleByName(expectedRoleName);
		assertEquals(true, retrievedRoleEither.isRight());

		Role retrievedRole = retrievedRoleEither.right().value();
		assertNotNull(retrievedRole);
		assertEquals(expectedRoleName, retrievedRole.getName());
		assertEquals(listSize, expectedRolePermissions.size());
		assertEquals(expectedRolePermissions.size(), retrievedRole.getPermissions().size());
	}

	@Test
	public void testGetAllRolesNames_OK() {

		List<String> rolesNamesList = new ArrayList<String>();
		rolesNamesList.add("admin");
		rolesNamesList.add("tester");

		Either<IException, List<String>> rolesNamesListEither = Either.right(rolesNamesList);
		Mockito.when(roleDaoMock.getAllRolesNames(dbConnectionMock)).thenReturn(rolesNamesListEither);
		Either<IException, List<String>> allRolesNamesEither = roleBusiness.getAllRolesNames();
		assertEquals(true, allRolesNamesEither.isRight());

		List<String> allRolesNames = allRolesNamesEither.right().value();
		assertNotNull(allRolesNames);
		assertEquals(rolesNamesList.size(), allRolesNames.size());
	}

	@Test
	public void testGetAllRolesNames_NOK() {

		List<String> rolesNamesList = new ArrayList<String>();
		Either<IException, List<String>> rolesNamesListEither = Either.right(rolesNamesList);

		Mockito.when(roleDaoMock.getAllRolesNames(dbConnectionMock)).thenReturn(rolesNamesListEither);
		Either<IException, List<String>> allRolesNamesEither = roleBusiness.getAllRolesNames();
		assertEquals(true, allRolesNamesEither.isRight());
		
		List<String> allRolesNames = allRolesNamesEither.right().value();
		assertNotNull(allRolesNames);
		assertEquals(rolesNamesList.size(), allRolesNames.size());
	}

	@Test
	public void testGetAllPermissionsOfAGivenRole_OK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		List<Permission> expectedRolePermissions = role.getPermissions();
		Either<IException, List<Permission>> expectedRolePermissionsEither = Either.right(expectedRolePermissions);
		
        Mockito.when(permissionBusinessMock.getAllPermissionsOfAGivenRole(role.getName())).thenReturn(expectedRolePermissionsEither);
		Either<IException, List<Permission>> rolePermissionsEither = roleBusiness.getAllPermissionsOfAGivenRole(role.getName());
		assertEquals(true, rolePermissionsEither.isRight());

		List<Permission> rolePermissions = rolePermissionsEither.right().value();
		assertNotNull(rolePermissions);
		assertEquals(expectedRolePermissions.size(), rolePermissions.size());
	}

	@Test
	public void testGetAllPermissionsOfAGivenRole_NOK() {

		int listSize = 0;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		List<Permission> expectedRolePermissions = role.getPermissions();
		Either<IException, List<Permission>> expectedRolePermissionsEither = Either.right(expectedRolePermissions);
		
        Mockito.when(permissionBusinessMock.getAllPermissionsOfAGivenRole(role.getName())).thenReturn(expectedRolePermissionsEither);
		Either<IException, List<Permission>> rolePermissionsEither = roleBusiness.getAllPermissionsOfAGivenRole(role.getName());
		assertEquals(true, rolePermissionsEither.isRight());

		List<Permission> rolePermissions = rolePermissionsEither.right().value();
		assertNotNull(rolePermissions);
		assertEquals(expectedRolePermissions.size(), rolePermissions.size());
	}

	@Test
	public void testRolePermissionsAreInRedis_OK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		String rolePermissionsToString = role.getPermissions().toString();
		Mockito.when(redisMock.get(role.getName())).thenReturn(rolePermissionsToString);

		assertTrue(roleBusiness.rolePermissionsAreInRedis(role.getName()));
	}

	@Test
	public void testRolePermissionsAreInRedis_NOK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		Mockito.when(redisMock.get(role.getName())).thenReturn(null);
		
		assertFalse(roleBusiness.rolePermissionsAreInRedis(role.getName()));
	}

	private String getRolePermissionsAsStringToRedis(List<Permission> rolePermissions) {
		
		Gson gson = new Gson();
		return gson.toJson(rolePermissions);
	}

	@Test
	public void testAddRolePermissionsToRedis_OK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);

		List<Permission> rolePermissions = role.getPermissions();
		Either<IException, List<Permission>> rolePermissionsEither = Either.right(rolePermissions);
		String rolePermissionsToString = getRolePermissionsAsStringToRedis(rolePermissions);

		String roleName = role.getName();
        Mockito.when(permissionBusinessMock.getAllPermissionsOfAGivenRole(roleName)).thenReturn(rolePermissionsEither);
		Mockito.when(redisMock.set(roleName, rolePermissionsToString)).thenReturn("OK");

		Either<IException, Boolean> permissionsWereAddedToRedisEither = roleBusiness.addRolePermissionsToRedis(roleName);
		assertEquals(true, permissionsWereAddedToRedisEither.isRight());
		
		Boolean rolePermissionsWereAddedToRedis = permissionsWereAddedToRedisEither.right().value();
        assertEquals(true, rolePermissionsWereAddedToRedis);
	}

	@Test
	public void testAddRolePermissionsToRedis_NOK() {

		int listSize = 0;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		List<Permission> rolePermissions = role.getPermissions();
		Either<IException, List<Permission>> rolePermissionsEither = Either.right(rolePermissions);
		
        String roleName = role.getName();
        Mockito.when(permissionBusinessMock.getAllPermissionsOfAGivenRole(roleName)).thenReturn(rolePermissionsEither);
        
        String rolePermissionsToString = getRolePermissionsAsStringToRedis(rolePermissions);
        Mockito.when(redisMock.set(roleName, rolePermissionsToString)).thenReturn("NIL");

		Either<IException, Boolean> permissionsWereAddedToRedisEither = roleBusiness.addRolePermissionsToRedis(roleName);
		assertEquals(true, permissionsWereAddedToRedisEither.isRight());
		
		Boolean permissionsWereAddedToRedis = permissionsWereAddedToRedisEither.right().value();
        assertEquals(false, permissionsWereAddedToRedis);
	}

	@Test
	public void testGetRolePermissionsFromRedis_OK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		String rolePermissionsToString = getRolePermissionsAsStringToRedis(role.getPermissions());

		Mockito.when(redisMock.get(role.getName())).thenReturn(rolePermissionsToString);

		List<PermissionTuple> retrievedRolePermissions = roleBusiness.getRolePermissionsFromRedis(role.getName());

		assertNotNull(retrievedRolePermissions);
		assertEquals(listSize, role.getPermissions().size());
		assertEquals(role.getPermissions().size(), retrievedRolePermissions.size());
	}

	@Test
	public void testGetRolePermissionsFromRedis_NOK() {

		int listSize = 0;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);
		String rolePermissionsToString = getRolePermissionsAsStringToRedis(role.getPermissions());

		Mockito.when(redisMock.get(role.getName())).thenReturn(rolePermissionsToString);

		List<PermissionTuple> retrievedRolePermissions = roleBusiness.getRolePermissionsFromRedis(role.getName());

		assertNotNull(retrievedRolePermissions);
		assertEquals(listSize, role.getPermissions().size());
		assertEquals(role.getPermissions().size(), retrievedRolePermissions.size());
	}
}