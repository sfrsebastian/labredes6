package gp.e3.autheo.authorization.persistence.factories.permission;

public class PermissionPostgreSQLFactory implements IPermissionSQLFactory {

	@Override
	public String getCreatePermissionsUniqueIndexSQL() {
		
		return "ALTER TABLE permissions ADD UNIQUE (http_verb, url);";
	}

	@Override
	public String getCreatePermissionsTableIfNotExistsSQL() {
		
		return "CREATE TABLE IF NOT EXISTS permissions (id SERIAL, name VARCHAR(32), http_verb VARCHAR(32), url VARCHAR(256), PRIMARY KEY (id));";
	}

	@Override
	public String getCountPermissionsTableSQL() {
		
		return "SELECT COUNT(*) FROM permissions;";
	}

	@Override
	public String getCountRolePermissionsTableSQL() {
		
		return "SELECT COUNT(*) FROM roles_permissions;";
	}

	@Override
	public String getCreatePermissionSQL() {
		
		return "INSERT INTO permissions (name, http_verb, url) VALUES (?, ?, ?);";
	}

	@Override
	public String getAssociateAllPermissionsToRoleSQL() {
		
		return "INSERT INTO roles_permissions (role_name, permission_id) VALUES (?, ?);";
	}

	@Override
	public String getDisassociateAllPermissionsFromRoleSQL() {
		
		return "DELETE FROM roles_permissions WHERE role_name = ?;";
	}

	@Override
	public String getDisassociatePermissionFromAllRolesSQL() {
		
		return "DELETE FROM roles_permissions WHERE permission_id = ?;";
	}

	@Override
	public String getGetPermissionByIdSQL() {
		
		return "SELECT * FROM permissions WHERE id = ?;";
	}

	@Override
	public String getGetPermissionByHttpVerbAndUrlSQL() {
		
		return "SELECT * FROM permissions WHERE http_verb = ? AND url = ?;";
	}

	@Override
	public String getGetAllPermissionsSQL() {
		
		return "SELECT * FROM permissions;";
	}

	@Override
	public String getGetAllPermissionsOfAGivenRoleSQL() {
		
		return "SELECT * FROM permissions LEFT JOIN roles_permissions ON permissions.id = roles_permissions.permission_id WHERE role_name = ?;";
	}

	@Override
	public String getDeletePermissionSQL() {
		
		return "DELETE FROM permissions WHERE id = ?;";
	}
}