package gp.e3.autheo.authentication.persistence.factories.organization;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum OrganizationSQLFactorySingleton {

	INSTANCE;

	private IOrganizationSQLFactory organizationSQLFactory;

	public IOrganizationSQLFactory getOrganizationSQLFactory(String dbName) throws SQLException {

		if (organizationSQLFactory == null) {

			if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {

				organizationSQLFactory = new OrganizationPostgresSQLFactory();

			} else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {

				organizationSQLFactory = null; //TODO: Implement MySQL case

			} else {

				throw new SQLException("The DB: " + dbName + " is not supported.");
			}
		}

		return organizationSQLFactory;
	}
}