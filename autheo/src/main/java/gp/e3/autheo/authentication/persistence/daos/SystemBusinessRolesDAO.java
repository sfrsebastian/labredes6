package gp.e3.autheo.authentication.persistence.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.systemBusinessRoles.ISystemBusinessRolesSQLFactory;

public class SystemBusinessRolesDAO {

    private final ISystemBusinessRolesSQLFactory systemBusinessRolesSQLFactory;

    public SystemBusinessRolesDAO(ISystemBusinessRolesSQLFactory systemBusinessRolesSQLFactory){

        this.systemBusinessRolesSQLFactory = systemBusinessRolesSQLFactory;

    }

    public Either<IException, Integer> createSystemBusinessRole(Connection dbConnection, String role, String organization){

        Either<IException, Integer> either = null;

        String createSystemBusinessRoles = systemBusinessRolesSQLFactory.getCreateSystemBusinessRoles();

        try(PreparedStatement preparedStatement = dbConnection.prepareStatement(createSystemBusinessRoles)){

            preparedStatement.setString(1, role);
            preparedStatement.setString(2, role);
            preparedStatement.setString(3, organization);
            int result = preparedStatement.executeUpdate();


            int systemBusinessRole = 0;
            
            if (result > 0){

            	either = Either.right(systemBusinessRole);

            } else {

                Exception e = new Exception("Error creating system business role");
                either = Either.left(ExceptionFactory.getTechnicalException(e));
            }

        } catch (SQLException e) {
        	e.printStackTrace();
            either = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return either;
    }
    
    public Either<IException, String> getSystemAdminRoleId(Connection dbConnection, String roleId, String tenantId){
        
        Either<IException, String > either = null;
        
        String createOrganizationSQL = systemBusinessRolesSQLFactory.getSystemAdminRoleIdSQL();
        
        try ( PreparedStatement preparedStatement = dbConnection.prepareStatement(createOrganizationSQL) ){
            
            preparedStatement.setString(1, roleId);
            preparedStatement.setString(2, tenantId);
            ResultSet resultSet = preparedStatement.executeQuery();
          
            while (resultSet.next()) {

                String id = resultSet.getString(1);
                either = Either.right(id);
               
            }

            
            
        } catch (SQLException e) {
            
            either = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        
        return either;
    }
    
    
}
