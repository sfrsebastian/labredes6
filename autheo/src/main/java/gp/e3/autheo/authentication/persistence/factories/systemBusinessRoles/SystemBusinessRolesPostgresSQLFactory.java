package gp.e3.autheo.authentication.persistence.factories.systemBusinessRoles;

public class SystemBusinessRolesPostgresSQLFactory implements ISystemBusinessRolesSQLFactory{
    
    public String getCreateSystemBusinessRoles() {
    	
        return "INSERT INTO system_business_roles (systemrole, businessrole, organization) "
                + "VALUES (?,?,?);";
        
    }
    
    @Override
	public String getSystemAdminRoleIdSQL() {
		
		String sql = "SELECT id FROM SYSTEM_BUSINESS_ROLES WHERE systemrole = ? AND organization = ?;";

        return sql;
	}


}
