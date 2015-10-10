package gp.e3.autheo.authentication.persistence.factories.user;

public interface IUserSQLFactory {
	
	public static final String NAME_FIELD = "name";
	public static final String DOCUMENT_TYPE_FIELD = "documentType";
	public static final String DOCUMENT_NUMBER_FIELD = "documentNumber";
	public static final String EMAIL_FIELD = "email";
	public static final String TELEPHONE_NUMBER_FIELD = "telephone_number";
	public static final String ADDRESS_FIELD = "address";
	public static final String USERNAME_FIELD = "username";
	public static final String PASSWORD_FIELD = "password";
	public static final String SALT_FIELD = "salt";
	public static final String IS_API_CLIENT_FIELD = "api_client";
	public static final String ORGANIZATION_ID_FIELD = "organization_id";
	public static final String ROLE_ID_FIELD = "role_id";
	public static final String SYSTEM_ROLE_FIELD = "systemrole";
	public static final String BUSINESS_ROLE_FIELD = "businessrole";
	public static final String IMAGE_FIELD = "image";
	
	public String getCreateUsersTableIfNotExistsSQL();
	
	public String getCountUsersTableSQL();
	
	public String getCreateUserSQL();
	
	public String getGetUserByUsernameSQL();
	
	public String getGetUserByDocumentNumberSQL();
	
	public String getGetUsersByRoleIdSQL();
	
	public String getGetAllUsersSQL();
	
	public String getGetPasswordByUsernameSQL();
	
	public String getUpdateUserSQL();
	
	public String getUpdateUserPasswordSQL();
	
	public String getDeleteUserSQL();
	
	public String getUserWithDocumentIdExistsSQL();
	
	public String getUserWithUsernameExistsSQL();
	
	public String getUserByEmailSQL();
	
	public String getBusinessRoleSQL();

	String getSystemBusinessRoleId();

}
