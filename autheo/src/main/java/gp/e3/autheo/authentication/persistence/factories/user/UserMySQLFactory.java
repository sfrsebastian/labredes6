package gp.e3.autheo.authentication.persistence.factories.user;

public class UserMySQLFactory implements IUserSQLFactory {

	@Override
	public String getCreateUsersTableIfNotExistsSQL() {
		
		return "CREATE TABLE IF NOT EXISTS users (name varchar(32), documentType varchar(32), documentNumber varchar(32), email varchar(64), telephone_number varchar(32), address varchar(256), "
				+ "username varchar(32) primary key, password varchar(256), salt varchar(256), api_client TINYINT(1), organization_id varchar(32), "
				+ "role_id varchar(32), image blob);";
	}

	@Override
	public String getCountUsersTableSQL() {
		
		return "SELECT COUNT(*) FROM users;";
	}

	@Override
	public String getCreateUserSQL() {
		
		return "INSERT INTO users (name, documentType, documentNumber, email,  username, password, salt, api_client, organization_id, role_id, image,telephone_number, address,) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
	}

	@Override
	public String getGetUserByUsernameSQL() {
		
		return "SELECT * FROM users WHERE username = ? AND organization_id = ?;";
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
		
		return "UPDATE users SET name = ?, password = ? WHERE username = ?;";
	}

	@Override
	public String getDeleteUserSQL() {
		
		return "DELETE FROM users WHERE username = ?;";
	}

	@Override
	public String getGetUsersByRoleIdSQL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBusinessRoleSQL() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public String getGetUserByDocumentNumberSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserWithDocumentIdExistsSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserWithUsernameExistsSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUserByEmailSQL() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getUpdateUserPasswordSQL() {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public String getSystemBusinessRoleId() {
		// TODO Auto-generated method stub
		return null;
	}

	
}