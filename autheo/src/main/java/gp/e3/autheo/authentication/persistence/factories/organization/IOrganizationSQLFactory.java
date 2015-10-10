package gp.e3.autheo.authentication.persistence.factories.organization;

public interface IOrganizationSQLFactory {
    
    public String getCreateOrganizationsTableIfNotExistsSQL();

    public String getCreateOrganizationSQL();
    

}
