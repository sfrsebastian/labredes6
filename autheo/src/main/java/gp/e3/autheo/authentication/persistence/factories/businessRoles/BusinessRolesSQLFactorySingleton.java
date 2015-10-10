package gp.e3.autheo.authentication.persistence.factories.businessRoles;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum BusinessRolesSQLFactorySingleton {
    INSTANCE;

    private IBusinessRolesSQLFactory businessRolesSQLFactory;

    public IBusinessRolesSQLFactory getBusinessRolesSQLFactory(String dbName) throws SQLException {

        if (businessRolesSQLFactory == null) {

            if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {

                businessRolesSQLFactory = new BusinessRolesPostgresSQLFactory();

            } else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {

                businessRolesSQLFactory = null; //TODO: Implement MySQL case

            } else {

                throw new SQLException("The DB: " + dbName + " is not supported.");
            }
        }

        return businessRolesSQLFactory;
    }
}


