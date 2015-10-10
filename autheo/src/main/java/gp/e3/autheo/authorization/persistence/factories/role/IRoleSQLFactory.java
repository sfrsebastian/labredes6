package gp.e3.autheo.authorization.persistence.factories.role;

public interface IRoleSQLFactory {
	
	public static final String USER_USERNAME_FIELD = "username";
	public static final String ROLE_ROLE_NAME_FIELD = "role_name";

	public static final String NAME_FIELD = "name";
	
	public String getCreateRolesTableIfNotExistsSQL();
	
	public String getCreateRolesAndPermissionsTableSQL();
	
	public String getCreateRolesAndUsersTableSQL();
	
	public String getCountRolesTableSQL();
	
	public String getCountRolePermissionsTableSQL();
	
	public String getCountRoleUsersTableSQL();
	
	public String getCreateRoleSQL();
	
	public String getGetAllRolesNamesSQL();
	
	public String getDeleteRoleSQL();
	
	public String getAddUserToRoleSQL();
	
	public String getRemoveUserFromRoleSQL();
	
	public String getRemoveAllUsersFromRoleSQL();
	
	public String getCreateBusinessRolesSQL();
	
	public String getCreateSystemBusinessRolesSQL();
	
	public String getGetBusinessRolesSQL();
	
	public String getCreateBusinessRoleSQL();
	
	public String getCreateSystemBusinessRoleRelationSQL();
	
	public String getGetCitizenBusinessRolesSQL();
	
}