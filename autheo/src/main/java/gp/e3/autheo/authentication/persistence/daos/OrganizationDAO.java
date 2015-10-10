package gp.e3.autheo.authentication.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.organization.IOrganizationSQLFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class OrganizationDAO {

    private final IOrganizationSQLFactory organizationSQLFactory;

    public OrganizationDAO(IOrganizationSQLFactory organizationSQLFactory) {

        this.organizationSQLFactory = organizationSQLFactory;
    }

    public Either<IException, Integer> createOrganizationssTableIfNotExists(Connection dbConnection) {

        Either<IException, Integer> resultEither = null;
        String createOrganizationsTableIfNotExistsSQL = organizationSQLFactory.getCreateOrganizationsTableIfNotExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createOrganizationsTableIfNotExistsSQL)) {

            int result = prepareStatement.executeUpdate();
            resultEither = Either.right(result);

        } catch (SQLException e) {

            resultEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return resultEither;
    }
    
   public Either<IException, Boolean> createOrganization(Connection dbConnection, String organization){
        
        Either<IException, Boolean> either = null;
        
        String createOrganizationSQL = organizationSQLFactory.getCreateOrganizationSQL();

        try(PreparedStatement preparedStatement = dbConnection.prepareStatement(createOrganizationSQL)){
            
            preparedStatement.setString(1, organization);
            
            int result = preparedStatement.executeUpdate();
            
            System.out.println("result: "+result);
            if ( result > 0 ){
                
                either = Either.right(true);
                
            } else {
                
                Exception e = new Exception("Error creating organization");
                either = Either.left(ExceptionFactory.getTechnicalException(e));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            either = Either.left(ExceptionFactory.getTechnicalException(e));
        }
        
        return either;
    }
   
   
}