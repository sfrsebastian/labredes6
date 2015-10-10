package gp.e3.autheo.authorization.domain.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authorization.domain.entities.Permission;
import gp.e3.autheo.authorization.domain.entities.Role;
import gp.e3.autheo.authorization.persistence.daos.PermissionDAO;
import gp.e3.autheo.util.PermissionFactoryForTests;
import gp.e3.autheo.util.RoleFactoryForTests;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class PermissionBusinessTest {

	private Connection dbConnectionMock;
	private BasicDataSource dataSourceMock;
	private PermissionDAO permissionDaoMock;

	private PermissionBusiness permissionBusiness;

	@Before
	public void setUp() {

		dbConnectionMock = Mockito.mock(Connection.class);
		dataSourceMock = Mockito.mock(BasicDataSource.class);
		
		try {
			Mockito.when(dataSourceMock.getConnection()).thenReturn(dbConnectionMock);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		permissionDaoMock = Mockito.mock(PermissionDAO.class);
		permissionBusiness = new PermissionBusiness(dataSourceMock, permissionDaoMock);
	}

	@After
	public void tearDown() {

		dbConnectionMock = null;
		dataSourceMock = null;

		permissionDaoMock = null;
		permissionBusiness = null;
	}

	@Test
	public void testCreatePermission_OK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

		long expectedPermissionId = 1;
		Either<IException, Long> expectedPermissionIdEither = Either.right(expectedPermissionId);
		Mockito.when(permissionDaoMock.createPermission(dbConnectionMock, permission)).thenReturn(expectedPermissionIdEither);

		Either<IException, Long> permissionIdEither = permissionBusiness.createPermission(permission);
		assertEquals(true, permissionIdEither.isRight());

		long permissionId = permissionIdEither.right().value().longValue();
		assertNotEquals(0, permissionId);
		assertEquals(expectedPermissionId, permissionId);
	}

	@Test
	public void testCreatePermission_NOK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

		long expectedPermissionId = 0;
		Either<IException, Long> expectedPermissionIdEither = Either.right(expectedPermissionId);
		Mockito.when(permissionDaoMock.createPermission(dbConnectionMock, permission)).thenReturn(expectedPermissionIdEither);
		
		Either<IException, Long> permissionIdEither = permissionBusiness.createPermission(permission);
		assertEquals(true, permissionIdEither.isRight());

		long permissionId = permissionIdEither.right().value().longValue();
		assertEquals(0, permissionId);
	}

	@Test
	public void testGetPermissionById_OK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
		Either<IException, Permission> permissionEither = Either.right(permission);

		Mockito.when(permissionDaoMock.getPermissionById(dbConnectionMock, permission.getId())).thenReturn(permissionEither);
		Either<IException, Permission> retrievedPermissionEither = permissionBusiness.getPermissionById(permission.getId());
		assertEquals(true, retrievedPermissionEither.isRight());

		Permission retrievedPermission = retrievedPermissionEither.right().value();
		assertNotNull(retrievedPermission);
		assertEquals(0, permission.compareTo(retrievedPermission));
	}

	@Test
	public void testGetPermissionById_NOK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
		IException technicalException = new TechnicalException("Could not reach the DB.");
		Either<IException, Permission> leftEither = Either.left(technicalException);

		Mockito.when(permissionDaoMock.getPermissionById(dbConnectionMock, permission.getId())).thenReturn(leftEither);
		Either<IException, Permission> retrievedPermissionEither = permissionBusiness.getPermissionById(permission.getId());
		assertEquals(false, retrievedPermissionEither.isRight());
		
		IException exception = retrievedPermissionEither.left().value();
		assertNotNull(exception);
		assertEquals(0, technicalException.compareTo(exception));
	}

	@Test
	public void testGetPermissionByHttpVerbAndUrl_OK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
		Either<IException, Permission> permissionEither = Either.right(permission);

		Mockito.when(permissionDaoMock.getPermissionByHttpVerbAndUrl(dbConnectionMock, permission.getHttpVerb(), permission.getUrl())).thenReturn(permissionEither);
		Either<IException, Permission> retrievedPermissionEither = permissionBusiness.getPermissionByHttpVerbAndUrl(permission.getHttpVerb(), permission.getUrl());
		assertEquals(true, retrievedPermissionEither.isRight());

		Permission retrievedPermission = retrievedPermissionEither.right().value();
		assertNotNull(retrievedPermission);
		assertEquals(0, permission.compareTo(retrievedPermission));
	}

	@Test
	public void testGetPermissionByHttpVerbAndUrl_NOK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();
		IException technicalException = new TechnicalException("Could not reach the DB.");
		Either<IException, Permission> leftEither = Either.left(technicalException);

		Mockito.when(permissionDaoMock.getPermissionByHttpVerbAndUrl(dbConnectionMock, permission.getHttpVerb(), permission.getUrl())).thenReturn(leftEither);
		Either<IException, Permission> retrievedPermissionEither = permissionBusiness.getPermissionByHttpVerbAndUrl(permission.getHttpVerb(), permission.getUrl());
		assertEquals(false, retrievedPermissionEither.isRight());
		
		IException exception = retrievedPermissionEither.left().value();
		assertNotNull(exception);
		assertEquals(0, technicalException.compareTo(exception));
	}

	@Test
	public void testGetAllPermissions_OK() {

		int listSize = 5;
		List<Permission> permissionList = PermissionFactoryForTests.getPermissionList(listSize);
		Either<IException, List<Permission>> permissionListEither = Either.right(permissionList);
		
		Mockito.when(permissionDaoMock.getAllPermissions(dbConnectionMock)).thenReturn(permissionListEither);
		Either<IException, List<Permission>> allPermissionsEither = permissionBusiness.getAllPermissions();
		assertEquals(true, allPermissionsEither.isRight());

		List<Permission> allPermissions = allPermissionsEither.right().value();
		assertNotNull(allPermissions);
		assertEquals(listSize, allPermissions.size());
	}

	@Test
	public void testGetAllPermissions_NOK() {

		int listSize = 0;
		List<Permission> permissionList = PermissionFactoryForTests.getPermissionList(listSize);
		Either<IException, List<Permission>> permissionListEither = Either.right(permissionList);
		
		Mockito.when(permissionDaoMock.getAllPermissions(dbConnectionMock)).thenReturn(permissionListEither);
		Either<IException, List<Permission>> allPermissionsEither = permissionBusiness.getAllPermissions();
		assertEquals(true, allPermissionsEither.isRight());

		List<Permission> allPermissions = allPermissionsEither.right().value();
		assertNotNull(allPermissions);
		assertEquals(listSize, allPermissions.size());
	}

	@Test
	public void testGetAllPermissionsOfAGivenRole_OK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);

		String roleName = role.getName();
		List<Permission> expectedRolePermissions = role.getPermissions();
		Either<IException, List<Permission>> expectedRolePermissionsEither = Either.right(expectedRolePermissions);
		
        Mockito.when(permissionDaoMock.getAllPermissionsOfAGivenRole(dbConnectionMock, roleName)).thenReturn(expectedRolePermissionsEither);
		Either<IException, List<Permission>> rolePermissionsEither = permissionBusiness.getAllPermissionsOfAGivenRole(roleName);
		assertEquals(true, rolePermissionsEither.isRight());

		List<Permission> rolePermissions = rolePermissionsEither.right().value();
		assertNotNull(rolePermissions);
		assertEquals(listSize, rolePermissions.size());
	}

	@Test
	public void testGetAllPermissionsOfAGivenRole_NOK() {

		int listSize = 5;
		Role role = RoleFactoryForTests.getDefaultTestRole(listSize);

		String roleName = role.getName();
		List<Permission> expectedRolePermissions = role.getPermissions();
		Either<IException, List<Permission>> expectedRolePermissionsEither = Either.right(expectedRolePermissions);
		
        Mockito.when(permissionDaoMock.getAllPermissionsOfAGivenRole(dbConnectionMock, roleName)).thenReturn(expectedRolePermissionsEither);
		Either<IException, List<Permission>> rolePermissionsEither = permissionBusiness.getAllPermissionsOfAGivenRole(roleName);
		assertEquals(true, rolePermissionsEither.isRight());

		List<Permission> rolePermissions = rolePermissionsEither.right().value();
		assertNotNull(rolePermissions);
		assertEquals(listSize, rolePermissions.size());
	}

	@Test
	public void testDeletePermission_OK() {

		Permission permission = PermissionFactoryForTests.getDefaultTestPermission();

		int updatedRows = 1;
		Either<IException, Integer> updatedRowsEither = Either.right(updatedRows);
		long permissionId = permission.getId();
		Mockito.when(permissionDaoMock.deletePermission(dbConnectionMock, permissionId)).thenReturn(updatedRowsEither);

		Either<IException, Boolean> permissionWasDeletedEither = permissionBusiness.deletePermission(permissionId);
		assertEquals(true, permissionWasDeletedEither.isRight());
		
		Boolean permissionWasDeleted = permissionWasDeletedEither.right().value();
		assertEquals(true, permissionWasDeleted);
	}

	@Test
	public void testDeletePermission_NOK() {

		int updatedRows = 0;
		Either<IException, Integer> updatedRowsEither = Either.right(updatedRows);
		long fakePermissionId = -1;
		Mockito.when(permissionDaoMock.deletePermission(dbConnectionMock, fakePermissionId)).thenReturn(updatedRowsEither);

		Either<IException, Boolean> permissionWasDeletedEither = permissionBusiness.deletePermission(fakePermissionId);
		assertEquals(true, permissionWasDeletedEither.isRight());
		
		Boolean permissionWasDeleted = permissionWasDeletedEither.right().value();
		assertEquals(false, permissionWasDeleted);
	}
}