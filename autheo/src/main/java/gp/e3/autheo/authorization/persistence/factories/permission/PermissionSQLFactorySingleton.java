package gp.e3.autheo.authorization.persistence.factories.permission;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum PermissionSQLFactorySingleton {

	INSTANCE; 

	private IPermissionSQLFactory permissionSQLFactory;

	public IPermissionSQLFactory getPermissionSQLFactory(String dbName) throws SQLException {

		if (permissionSQLFactory == null) {

			if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {

				permissionSQLFactory = new PermissionPostgreSQLFactory();

			} else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {

				permissionSQLFactory = new PermissionMySQLFactory();

			} else {

				throw new SQLException("The DB: " + dbName + " is not supported.");
			}
		}

		return permissionSQLFactory;
	}
}