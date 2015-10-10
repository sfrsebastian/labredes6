package gp.e3.autheo.authentication.persistence.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.authentication.persistence.factories.businessRoles.IBusinessRolesSQLFactory;

public class BusinessRolesDAO {

    private final IBusinessRolesSQLFactory businessRolesSQLFactory;

    public BusinessRolesDAO(IBusinessRolesSQLFactory businessRolesSQLFactory){

        this.businessRolesSQLFactory = businessRolesSQLFactory;
    }

    public Either<IException, Boolean> createBusinessRole(Connection dbConnection, String role, String organization){

        Either<IException, Boolean> either = null;
        String createBusinessRoles = businessRolesSQLFactory.getCreateBusinessRoleSQL();

        try(PreparedStatement preparedStatement = dbConnection.prepareStatement(createBusinessRoles)){

            preparedStatement.setString(1, role);
            preparedStatement.setString(2, role + " " +organization);
            preparedStatement.setString(3, organization);
            int result = preparedStatement.executeUpdate();

            if (result > 0){

                either = Either.right(true);

            } else {

                TechnicalException e = new TechnicalException("Error creating business role");
                either = Either.left(e);
            }

        } catch (SQLException e) {	
        	e.printStackTrace();
            either = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return either;
    }
}
