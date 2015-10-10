package gp.e3.autheo.authentication.persistence.factories.token;

public interface ITokenSQLFactory {
	
	public static final String TOKEN_VALUE_FIELD = "token_value";
	public static final String USERNAME_FIELD = "username";
	public static final String ORGANIZATION_ID_FIELD = "organization_id";
	public static final String ROLE_ID_FIELD = "role_id";
	public static final String TOKEN_TYPE = "token_type";
	public static final String USERNAME_DOC_TYPE = "documenttype";
	public static final String USERNAME_DOC_NUMBER = "documentnumber";
	public static final String SYSTEM_ROLE = "system_role";
	
	public String getCreateTokensTableIfNotExistsSQL();
	
	public String getCountTokensTableRowsSQL();
	
	public String getCreateTokenSQL();
	
	public String getGetTokenByOrganizationIdSQL();
	
	public String getGetAllTokensSQL();
	
	public String getUpdateTokenByTokenValueSQL();
	
	public String getDeleteTokenByTokenValueSQL();
	
	public String getDeleteTokenByUsernameSQL();
}