package gp.e3.autheo.authentication.persistence.factories.systemBusinessRoles;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;
import gp.e3.autheo.authentication.persistence.factories.organization.IOrganizationSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.organization.OrganizationPostgresSQLFactory;

import java.sql.SQLException;

public enum SystemBusinessRolesSQLFactorySingleton {
    
    INSTANCE;

    private ISystemBusinessRolesSQLFactory systemBusinessRolesSQLFactory;

    public ISystemBusinessRolesSQLFactory getOrganizationSQLFactory(String dbName) throws SQLException {

        if (systemBusinessRolesSQLFactory == null) {

            if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {

                systemBusinessRolesSQLFactory = new SystemBusinessRolesPostgresSQLFactory();

            } else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {

                systemBusinessRolesSQLFactory = null; //TODO: Implement MySQL case

            } else {

                throw new SQLException("The DB: " + dbName + " is not supported.");
            }
        }

        return systemBusinessRolesSQLFactory;
    }

}
