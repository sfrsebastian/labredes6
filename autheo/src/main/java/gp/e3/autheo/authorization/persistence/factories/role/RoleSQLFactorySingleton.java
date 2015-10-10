package gp.e3.autheo.authorization.persistence.factories.role;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum RoleSQLFactorySingleton {
	
	INSTANCE;
	
	private IRoleSQLFactory roleSQLFactory;
	
	public IRoleSQLFactory getRoleSQLFactory(String dbName) throws SQLException {
		
		if (roleSQLFactory == null) {
			
			if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {

				roleSQLFactory = new RolePostgreSQLFactory();

			} else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {

				roleSQLFactory = new RoleMySQLFactory();

			} else {

				throw new SQLException("The DB: " + dbName + " is not supported.");
			}
		}
		
		return roleSQLFactory;
	}
}