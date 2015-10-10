package gp.e3.autheo.authorization.persistence.factories.permission;

public interface IPermissionSQLFactory {
	
	public static final String ROLE_NAME_FIELD = "role_name";
	public static final String PERMISSION_ID_FIELD = "permission_id";

	public static final String ID_FIELD = "id";
	public static final String NAME_FIELD = "name";
	public static final String HTTP_VERB_FIELD = "http_verb";
	public static final String URL_FIELD = "url";

	public String getCreatePermissionsUniqueIndexSQL();
	
	public String getCreatePermissionsTableIfNotExistsSQL();
	
	public String getCountPermissionsTableSQL();
	
	public String getCountRolePermissionsTableSQL();
	
	public String getCreatePermissionSQL();
	
	public String getAssociateAllPermissionsToRoleSQL();
	
	public String getDisassociateAllPermissionsFromRoleSQL();
	
	public String getDisassociatePermissionFromAllRolesSQL();
	
	public String getGetPermissionByIdSQL();
	
	public String getGetPermissionByHttpVerbAndUrlSQL();
	
	public String getGetAllPermissionsSQL();
	
	public String getGetAllPermissionsOfAGivenRoleSQL();
	
	public String getDeletePermissionSQL();
}