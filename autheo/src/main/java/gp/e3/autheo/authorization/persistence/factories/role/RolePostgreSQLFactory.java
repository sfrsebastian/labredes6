package gp.e3.autheo.authorization.persistence.factories.role;

public class RolePostgreSQLFactory implements IRoleSQLFactory {

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

        return "CREATE TABLE IF NOT EXISTS roles_users (username VARCHAR(32), role_name VARCHAR(32), organization text, PRIMARY KEY(username, organization) ,CONSTRAINT FK_USER_ROLES_BUSINESS_ROLES FOREIGN KEY (role_name, organization) REFERENCES business_roles(name,organization),CONSTRAINT FK_USER_ROLES_USERS FOREIGN KEY (username, organization) REFERENCES users(username,organization_id));";
    }

    @Override
    public String getCreateBusinessRolesSQL() {


        return "CREATE TABLE IF NOT EXISTS business_roles ( "
                + "name text NOT NULL, description text, "
                + "organization text NOT NULL, "
                +"CONSTRAINT PK_BUSINESS_ROLES PRIMARY KEY (name, organization), "
                +"CONSTRAINT FK_ORGANIZATIONS_NAME FOREIGN KEY(organization) REFERENCES organizations(name) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION);";
    }

    @Override
    public String getCreateSystemBusinessRolesSQL() {


        String sql = "CREATE TABLE IF NOT EXISTS system_business_roles ( id serial NOT NULL, systemrole text NOT NULL,  businessrole text NOT NULL, organization text NOT NULL, CONSTRAINT "
                + "PK_BUSINESS_SYSTEM_ROLES PRIMARY KEY (id), CONSTRAINT FK_BUSINESS_ROLES_NAME FOREIGN KEY (businessrole,organization) REFERENCES business_roles "
                + "(name, organization) MATCH SIMPLE  ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT FK_SYSTEM_ROLES_NAME FOREIGN KEY (systemrole) REFERENCES "
                + "roles (name) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION, CONSTRAINT unique_document_and_request UNIQUE (systemrole, businessrole, organization));";


        return sql;
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
    public String getGetCitizenBusinessRolesSQL() {

        return "SELECT businessrole FROM system_business_roles WHERE organization = ? AND systemrole = ?;";
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
    public String getGetBusinessRolesSQL() {
        
        return "SELECT s.id, s.systemrole, s.businessrole " 
                + "FROM system_business_roles s "
                + "INNER JOIN business_roles b ON s.businessrole = b.name AND s.organization = b.organization "
                + "WHERE b.organization = ?;";
        
    }

    @Override
    public String getCreateBusinessRoleSQL() {
        return "INSERT INTO business_roles (name,description,organization) VALUES (?, ?, ?);";
    }

    @Override
    public String getCreateSystemBusinessRoleRelationSQL() {
        return "INSERT INTO system_business_roles(systemrole, businessrole, organization) VALUES(?, ?, ?);";
    }
}