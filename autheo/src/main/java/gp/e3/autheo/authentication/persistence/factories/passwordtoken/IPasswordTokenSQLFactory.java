package gp.e3.autheo.authentication.persistence.factories.passwordtoken;

public interface IPasswordTokenSQLFactory {
	
	public static final String TOKEN_VALUE_FIELD = "token_value";
	public static final String USERNAME_FIELD = "username";
	public static final String ORGANIZATION_ID_FIELD = "organization_id";
	public static final String REQUEST_DATE_FIELD = "request_date";
	
	public String getCreatePasswordTokensTableIfNotExistsSQL();
	
	public String getCreatePasswordTokenSQL();
	
	public String getQueryUsernameByPasswordToken();
	
	public String getQueryPasswordTokenByUsername();
	
	public String getDeletePasswordTokenSQL();
	
}