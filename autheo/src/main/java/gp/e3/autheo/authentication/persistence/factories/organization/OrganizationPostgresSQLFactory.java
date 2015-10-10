package gp.e3.autheo.authentication.persistence.factories.organization;

public class OrganizationPostgresSQLFactory implements IOrganizationSQLFactory {

    @Override
    public String getCreateOrganizationsTableIfNotExistsSQL() {
        
    	String sql = "CREATE TABLE IF NOT EXISTS  organizations ("
                +"name text NOT NULL,"
                +"CONSTRAINT organizations_pkey PRIMARY KEY (name)"
                + " );";

        return sql;
    }
    
    @Override
    public String getCreateOrganizationSQL() {
        
    	String sql = "INSERT INTO organizations (name) "
                + "VALUES (?);";
    	
        return sql;
    }


}
