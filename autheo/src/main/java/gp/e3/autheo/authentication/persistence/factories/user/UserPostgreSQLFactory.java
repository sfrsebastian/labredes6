package gp.e3.autheo.authentication.persistence.factories.user;

public class UserPostgreSQLFactory implements IUserSQLFactory {

	@Override
	public String getCreateUsersTableIfNotExistsSQL() {
		
		String sql = "CREATE TABLE IF NOT EXISTS  users ("
		  +"name character varying(32),"
		  +"documenttype character varying(32),"
		  +"documentnumber character varying(32),"
		  +"email character varying(64),"
		  +"telephone_number character varying(32) NOT NULL, "
		  +"address character varying(256) NOT NULL, "
		  +"username character varying(32) NOT NULL,"
		  +"password character varying(256),"
		  +"salt character varying(256),"
		  +"api_client boolean,"
		  +"organization_id character varying(32),"
		  +"role_id integer NOT NULL, "
		  +"image bytea, "
		  +"CONSTRAINT users_pkey PRIMARY KEY (username, organization_id),"
		  +"CONSTRAINT FK_USERS_SYSTEMBUSINESSROLES FOREIGN KEY (role_id) "
		  +"REFERENCES system_business_roles (id) MATCH SIMPLE "
		  + "ON UPDATE NO ACTION ON DELETE NO ACTION"
		  + " );";
		
		return sql;
	}

	@Override
	public String getCountUsersTableSQL() {
		
		return "SELECT COUNT(*) FROM users;";
	}

	@Override
	public String getCreateUserSQL() {
		
		return "INSERT INTO users (name, documentType, documentNumber, email, username, password, salt, api_client, organization_id, role_id, image,  telephone_number, address) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	}

	@Override
	public String getGetUserByUsernameSQL() {
		
		String sql = "SELECT USR.name, USR.documenttype, USR.documentnumber, USR.email, USR.telephone_number, USR.address, USR.username, USR.role_id, USR.organization_id, USR.password,  "
		        + "SBR.systemrole, SBR.businessrole, USR.image "
				+ "FROM users USR LEFT OUTER JOIN system_business_roles SBR ON (USR.role_id = SBR.id) WHERE USR.username = ? AND USR.organization_id = ?;";
		
		return sql;
	}
	
	@Override
	public String getGetUserByDocumentNumberSQL() {
	    
	    String sql = "SELECT USR.name, USR.documenttype, USR.documentnumber, USR.email, USR.address, USR.telephone_number, USR.username, USR.role_id, USR.organization_id, USR.password, USR.image,  "
	            + "SBR.systemrole, SBR.businessrole "
                + "FROM users USR LEFT OUTER JOIN system_business_roles SBR ON (USR.role_id = SBR.id) WHERE USR.documentnumber = ? AND USR.organization_id = ?;";
	    return sql;
	}

	
	@Override
	public String getGetUsersByRoleIdSQL() {
		
		String sql = "SELECT USR.username, USR.name, USR.email, USR.telephone_number, USR.address, USR.role_id, SBR.systemrole, SBR.businessrole  FROM users USR INNER JOIN system_business_roles "
				+"SBR ON (USR.role_id = SBR.id) WHERE SBR.businessrole = ? AND USR.organization_id = ?";
		
		return sql;
	}
	
	
	@Override
	public String getGetAllUsersSQL() {
		
		return "SELECT * FROM users;";
	}

	@Override
	public String getGetPasswordByUsernameSQL() {
		
		return "SELECT password FROM users WHERE username = ? AND organization_id = ?;";
	}

	@Override
	public String getUpdateUserSQL() {
		
		return "UPDATE users SET name = ?, password = ? WHERE username = ? AND organization_id = ?;";
	}
	
	@Override
	public String getUpdateUserPasswordSQL() {
	    return "UPDATE users SET password = ?, salt = ? WHERE username = ? AND organization_id = ?;";
	}

	@Override
	public String getDeleteUserSQL() {
		
		return "DELETE FROM users WHERE username = ?;";
	}
	
	@Override
	public String getUserWithDocumentIdExistsSQL() {
	    return "SELECT 1 FROM users WHERE documentnumber =  ? AND organization_id = ?;";
	}

	@Override
	public String getBusinessRoleSQL() {
	    return "SELECT id, systemrole, businessrole FROM system_business_roles;";
	}

    @Override
    public String getUserWithUsernameExistsSQL() {
        return "SELECT 1 FROM users WHERE username =  ? AND organization_id = ?;";
    }

    @Override
    public String getUserByEmailSQL() {
        
        String sql = "SELECT USR.name, USR.documenttype, USR.documentnumber, USR.email, USR.telephone_number, USR.address, USR.username, USR.role_id, USR.organization_id, USR.password,  "
                + "SBR.systemrole, SBR.businessrole, USR.image "
                + "FROM users USR LEFT OUTER JOIN system_business_roles SBR ON (USR.role_id = SBR.id) WHERE USR.email = ? AND USR.organization_id = ?;";
        
        return sql;
    }
    
    @Override
    public String getSystemBusinessRoleId() {
        
        String sql = "SELECT id FROM system_business_roles WHERE systemrole = ? AND businessrole = ?";
        
        return sql;
    }

}
