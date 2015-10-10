package gp.e3.autheo.authentication.persistence.factories.token;

import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;

import java.sql.SQLException;

public enum TokenSQLFactorySingleton {
	
	INSTANCE;
	
	private ITokenSQLFactory tokenSQLFactory;

	public ITokenSQLFactory getTokenSQLFactory(String dbName) throws SQLException {
		
		if (tokenSQLFactory == null) {
			
			if (dbName.equalsIgnoreCase(DatabaseNames.POSTGRE_SQL.getName())) {
				
				tokenSQLFactory = new TokenPostgreSQLFactory();
				
			} else if (dbName.equalsIgnoreCase(DatabaseNames.MY_SQL.getName())) {
				
				tokenSQLFactory = new TokenMySQLFactory();
				
			} else {
				
				throw new SQLException("The DB: " + dbName + " is not supported.");
			}
		}
		
		return tokenSQLFactory;
	}
}