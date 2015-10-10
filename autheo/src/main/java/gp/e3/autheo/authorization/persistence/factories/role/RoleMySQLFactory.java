package gp.e3.autheo.authorization.persistence.factories.role;

public class RoleMySQLFactory implements IRoleSQLFactory {

	@Override
	public String getCreateRolesTableIfNotExistsSQL() {
		
		return "CREATE TABLE IF NOT EXISTS roles (name VARCHAR(32) PRIMARY KEY);";
	}

	@Override
	public String getCreateRolesAndPermissionsTableSQL() {
		
		return "CREATE TABLE IF NOT EXISTS roles_permissions (role_name VARCHAR(32), permission_id INT, PRIMARY KEY (role_name, permission_id));";
	}

	@Override
	public String getCreateRolesAndUsersTableSQL() {
		
		return "CREATE TABLE IF NOT EXISTS roles_users (username VARCHAR(32) PRIMARY KEY, role_name VARCHAR(32), organization VARCHAR(32));";
	}

	@Override
	public String getCountRolesTableSQL() {
		
		return "SELECT COUNT(*) FROM roles;";
	}

	@Override
	public String getCountRolePermissionsTableSQL() {
		
		return "SELECT COUNT(*) FROM roles_permissions;";
	}

	@Override
	public String getCountRoleUsersTableSQL() {
		
		return "SELECT COUNT(*) FROM roles_users;";
	}

	@Override
	public String getCreateRoleSQL() {
		
		return "INSERT INTO roles (name) VALUES (?);";
	}

	@Override
	public String getGetAllRolesNamesSQL() {
		
		return "SELECT name FROM roles;";
	}

	@Override
	public String getDeleteRoleSQL() {
		
		return "DELETE FROM roles WHERE name = ?;";
	}

	@Override
	public String getAddUserToRoleSQL() {
		
		return "INSERT INTO roles_users (username, role_name, organization) VALUES (?, ?, ?);";
	}

	@Override
	public String getRemoveUserFromRoleSQL() {
		
		return "DELETE FROM roles_users WHERE username = ?;";
	}

	@Override
	public String getRemoveAllUsersFromRoleSQL() {
		
		return "DELETE FROM roles_users WHERE role_name = ?;";
	}

	@Override
	public String getCreateBusinessRolesSQL() {
	    return "CREATE TABLE IF NOT EXISTS business_roles ( "
                + "name VARCHAR(32) , description VARCHAR(32), "
                + "organization VARCHAR(32) , "
                +"CONSTRAINT PK_BUSINESS_ROLES PRIMARY KEY (name, organization));";
	}

	@Override
	public String getCreateSystemBusinessRolesSQL() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String getGetBusinessRolesSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCreateBusinessRoleSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getCreateSystemBusinessRoleRelationSQL() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public String getGetCitizenBusinessRolesSQL() {
		// TODO Auto-generated method stub
		return null;
	}
}