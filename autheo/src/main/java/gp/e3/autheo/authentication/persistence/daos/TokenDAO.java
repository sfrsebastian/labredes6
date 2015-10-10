package gp.e3.autheo.authentication.persistence.daos;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.ExceptionFactory;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.persistence.factories.token.ITokenSQLFactory;
import gp.e3.autheo.authentication.persistence.mappers.TokenMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class TokenDAO {

    private final ITokenSQLFactory tokenSQLFactory;

    public TokenDAO(ITokenSQLFactory tokenSQLFactory) {

        this.tokenSQLFactory = tokenSQLFactory;
    }

    public Either<IException, Boolean> createTokensTableIfNotExists(Connection dbConnection) {

        Either<IException, Boolean> tableWasCreatedEither = null;
        String createTokensTableIfNotExistsSQL = tokenSQLFactory.getCreateTokensTableIfNotExistsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createTokensTableIfNotExistsSQL)) {

            prepareStatement.executeUpdate();
            tableWasCreatedEither = Either.right(true);

        } catch (SQLException e) {

            tableWasCreatedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return tableWasCreatedEither;
    }

    public Either<IException, Integer> countTokensTableRows(Connection dbConnection) {

        Either<IException, Integer> countEither = null;
        String countTokensTableRowsSQL = tokenSQLFactory.getCountTokensTableRowsSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(countTokensTableRowsSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            int count = 0;

            if (resultSet.next()) {
                count = resultSet.getInt(1);
            }

            resultSet.close();
            countEither = Either.right(count);

        } catch (SQLException e) {

            countEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return countEither;
    }

    public Either<IException, Integer> createToken(Connection dbConnection, Token token) {

        Either<IException, Integer> rowsAffectedEither = null;
        String createTokenSQL = tokenSQLFactory.getCreateTokenSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(createTokenSQL)) {

            prepareStatement.setString(1, token.getTokenValue());
            prepareStatement.setString(2, token.getUsername());
            prepareStatement.setString(3, token.getUserOrganization());
            prepareStatement.setString(4, token.getUserRole());
            prepareStatement.setInt(5, token.getTokenType());
            prepareStatement.setString(6, token.getSystemRole());

            int rowsAffected = prepareStatement.executeUpdate();
            rowsAffectedEither = Either.right(rowsAffected);

        } catch (SQLException e) {

            rowsAffectedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rowsAffectedEither;
    }

    public Either<IException, List<Token>> getAllTokens(Connection dbConnection) {

        Either<IException, List<Token>> allTokensEither = null;
        String getAllTokensSQL = tokenSQLFactory.getGetAllTokensSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(getAllTokensSQL)) {

            ResultSet resultSet = prepareStatement.executeQuery();
            allTokensEither = TokenMapper.getMultipleTokensFromResultSet(resultSet);
            resultSet.close();

        } catch (SQLException e) {

            allTokensEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return allTokensEither;
    }

    public Either<IException, Integer> updateTokenByTokenValue(Connection dbConnection, String tokenValue, Token updatedToken) {

        Either<IException, Integer> rowsAffectedEither = null;
        String updateTokenByTokenValueSQL = tokenSQLFactory.getUpdateTokenByTokenValueSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(updateTokenByTokenValueSQL)) {

            prepareStatement.setString(1, updatedToken.getTokenValue());
            prepareStatement.setString(2, updatedToken.getUsername());
            prepareStatement.setString(3, updatedToken.getUserOrganization());
            prepareStatement.setString(4, updatedToken.getUserRole());
            prepareStatement.setInt(5, updatedToken.getTokenType());
            prepareStatement.setString(6, tokenValue);

            int rowsAffected = prepareStatement.executeUpdate();
            rowsAffectedEither = Either.right(rowsAffected);

        } catch (Exception e) {

            rowsAffectedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rowsAffectedEither;
    }

    public Either<IException, Integer> deleteTokenByTokenValue(Connection dbConnection, String tokenValue) {

        Either<IException, Integer> rowsAffectedEither = null;
        String deleteTokenByTokenValueSQL = tokenSQLFactory.getDeleteTokenByTokenValueSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(deleteTokenByTokenValueSQL)) {

            prepareStatement.setString(1, tokenValue);
            int rowsAffected = prepareStatement.executeUpdate();
            rowsAffectedEither = Either.right(rowsAffected);

        } catch (SQLException e) {

            rowsAffectedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rowsAffectedEither;
    }

    public Either<IException, Integer> deleteTokenByUsername(Connection dbConnection, String username) {

        Either<IException, Integer> rowsAffectedEither = null;
        String deleteTokenByUsernameSQL = tokenSQLFactory.getDeleteTokenByUsernameSQL();

        try (PreparedStatement prepareStatement = dbConnection.prepareStatement(deleteTokenByUsernameSQL)) {

            prepareStatement.setString(1, username.toLowerCase());
            int rowsAffected = prepareStatement.executeUpdate();
            rowsAffectedEither = Either.right(rowsAffected);

        } catch (SQLException e) {

            rowsAffectedEither = Either.left(ExceptionFactory.getTechnicalException(e));
        }

        return rowsAffectedEither;
    }
}