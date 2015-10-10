package gp.e3.autheo.authentication.persistence.factories.passwordtoken;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum PasswordTokenSQLFactorySingleton {
	
	INSTANCE;
	
	private IPasswordTokenSQLFactory passwordTokenSQLFactory;

	public IPasswordTokenSQLFactory getTokenSQLFactory(String dbName) throws SQLException {
		
		if (passwordTokenSQLFactory == null) {
			
			if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {
				
				passwordTokenSQLFactory = new PasswordTokenPostgreSQLFactory();
				
			} else {
				
				throw new SQLException("The DB: " + dbName + " is not supported.");
			}
		}
		
		return passwordTokenSQLFactory;
	}
}