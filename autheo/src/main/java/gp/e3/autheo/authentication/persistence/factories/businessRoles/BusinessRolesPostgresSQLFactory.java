package gp.e3.autheo.authentication.persistence.factories.businessRoles;

public class BusinessRolesPostgresSQLFactory implements IBusinessRolesSQLFactory{

    @Override
    public String getCreateBusinessRoleSQL() {
        return "INSERT INTO business_roles (name, description, organization) "
                + "VALUES (?,?,?);";
    }
    
    

}
