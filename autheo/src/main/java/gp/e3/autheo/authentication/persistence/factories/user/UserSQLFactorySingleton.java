package gp.e3.autheo.authentication.persistence.factories.user;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum UserSQLFactorySingleton {

	INSTANCE;

	private IUserSQLFactory userSQLFactory;

	public IUserSQLFactory getUserSQLFactory(String dbName) throws SQLException {

		if (userSQLFactory == null) {

			if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {

				userSQLFactory = new UserPostgreSQLFactory();

			} else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {

				userSQLFactory = new UserMySQLFactory();

			} else {

				throw new SQLException("The DB: " + dbName + " is not supported.");
			}
		}

		return userSQLFactory;
	}
}