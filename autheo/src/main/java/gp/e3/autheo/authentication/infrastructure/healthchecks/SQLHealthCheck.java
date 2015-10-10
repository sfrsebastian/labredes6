package gp.e3.autheo.authentication.infrastructure.healthchecks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.codahale.metrics.health.HealthCheck;

public class SQLHealthCheck extends HealthCheck {
	
	private Connection dbConnection;
	
	public SQLHealthCheck(Connection dbConnection) {
		
		this.dbConnection = dbConnection;
	}

	@Override
	protected Result check() throws Exception {
		
		PreparedStatement prepareStatement = dbConnection.prepareStatement("SELECT 1;");
		ResultSet resultSet = prepareStatement.executeQuery();
		
		return resultSet.next() ? Result.healthy() : Result.unhealthy("sql failure.");
	}
}