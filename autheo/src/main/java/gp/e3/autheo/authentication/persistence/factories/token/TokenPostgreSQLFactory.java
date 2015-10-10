package gp.e3.autheo.authentication.persistence.factories.token;

public class TokenPostgreSQLFactory implements ITokenSQLFactory {

	@Override
	public String getCreateTokensTableIfNotExistsSQL() {
		
		return "CREATE TABLE IF NOT EXISTS tokens (token_value varchar(128), username varchar(32), "
				+ "organization_id varchar(32), role_id varchar(32), token_type boolean, system_role varchar(32), PRIMARY KEY (organization_id, token_type));";
	}

	@Override
	public String getCountTokensTableRowsSQL() {
		
		return "SELECT COUNT(*) FROM tokens;";
	}

	@Override
	public String getCreateTokenSQL() {
		
		return "INSERT INTO tokens (token_value, username, organization_id, role_id, token_type, system_role) VALUES (?, ?, ?, ?, ?, ?);";
	}

	@Override
	public String getGetTokenByOrganizationIdSQL() {
		
		return "SELECT * FROM tokens WHERE organization_id = ?;";
	}

	@Override
	public String getGetAllTokensSQL() {
		
		return "SELECT * FROM tokens;";
	}

	@Override
	public String getUpdateTokenByTokenValueSQL() {
		
		return "UPDATE tokens SET token_value = ?, username = ?, organization_id = ?, role_id = ?, token_type = ? WHERE token_value = ?;";
	}

	@Override
	public String getDeleteTokenByTokenValueSQL() {
		
		return "DELETE FROM tokens WHERE token_value = ?;";
	}

	@Override
	public String getDeleteTokenByUsernameSQL() {
		
		return "DELETE FROM tokens WHERE username = ?;";
	}
}