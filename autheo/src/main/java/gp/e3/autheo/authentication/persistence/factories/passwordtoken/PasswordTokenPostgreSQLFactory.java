package gp.e3.autheo.authentication.persistence.factories.passwordtoken;

public class PasswordTokenPostgreSQLFactory implements IPasswordTokenSQLFactory{


    ////////////////////////
    // Attributes
    ////////////////////////

    ////////////////////////
    // Constructor
    ////////////////////////

    ////////////////////////
    // Public Methods
    ////////////////////////

    @Override
    public String getCreatePasswordTokensTableIfNotExistsSQL() {
        
        return "CREATE TABLE IF NOT EXISTS password_tokens ("
                + "token_value varchar(128), "
                + "username varchar(32), "
                + "organization_id varchar(32), "
                + "request_date timestamp without time zone NOT NULL, "
                + "PRIMARY KEY (organization_id, token_value));";
        
    }
    
    @Override
    public String getCreatePasswordTokenSQL() {
        
        return "INSERT INTO password_tokens (token_value, username, organization_id, request_date) VALUES (?, ?, ?, ?);";
        
    }

    @Override
    public String getQueryUsernameByPasswordToken() {
        
        return "SELECT username FROM password_tokens WHERE token_value = ? AND NOW() < (request_date + '1 day'::interval) AND organization_id = ?";
        
    }

    @Override
    public String getDeletePasswordTokenSQL() {
        
        return "DELETE FROM password_tokens WHERE token_value = ? AND organization_id = ?";
        
    }

    @Override
    public String getQueryPasswordTokenByUsername() {
        
        return "SELECT token_value FROM password_tokens WHERE username = ? AND organization_id = ?";
        
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
