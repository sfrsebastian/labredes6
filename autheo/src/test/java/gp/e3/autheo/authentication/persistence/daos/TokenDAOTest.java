package gp.e3.autheo.authentication.persistence.daos;

import static org.junit.Assert.*;
import fj.data.Either;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.infrastructure.exceptions.BusinessException;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.utils.sql.DatabaseNames;
import gp.e3.autheo.authentication.persistence.factories.token.ITokenSQLFactory;
import gp.e3.autheo.authentication.persistence.factories.token.TokenSQLFactorySingleton;
import gp.e3.autheo.util.TokenFactoryForTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TokenDAOTest {

	public static final String H2_IN_MEMORY_DB = "jdbc:h2:mem:test";

	private static Connection dbConnection;
	private static TokenDAO tokenDAO;

	@BeforeClass
	public static void setUpClass() {

		try {
			
			dbConnection = DriverManager.getConnection(H2_IN_MEMORY_DB);
			ITokenSQLFactory tokenSQLFactory = TokenSQLFactorySingleton.INSTANCE.getTokenSQLFactory(DatabaseNames.MY_SQL.getName());
			tokenDAO = new TokenDAO(tokenSQLFactory);
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownClass() {

		try {
			dbConnection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		dbConnection = null;
		tokenDAO = null;
	}

	@Before
	public void setUp() {

		tokenDAO.createTokensTableIfNotExists(dbConnection);
	}

	@After
	public void tearDown() {

		String dropTableSQL = "DROP TABLE tokens";
		
		try {
			PreparedStatement prepareStatement = dbConnection.prepareStatement(dropTableSQL);
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private int getTableRowCount() {
	    
        return tokenDAO.countTokensTableRows(dbConnection).right().value();
    }

	@Test
	public void testCountTokensTableRows_OK() {

		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
	}

	@Test
	public void testCountTokensTableRows_NOK() {

		assertEquals(0, getTableRowCount());

        Token token = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, token);

        assertEquals(1, getTableRowCount());

        // Add again the same token
        Either<IException, Integer> rowsAffectedEither = tokenDAO.createToken(dbConnection, token);
        assertEquals(false, rowsAffectedEither.isRight());
        
        IException exception = rowsAffectedEither.left().value();
        assertNotNull(exception);
        assertEquals(false, exception instanceof BusinessException);
	}

	@Test
	public void testCreateToken_OK() {

		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
	}
	
	@Test
	public void testCreateToken_NOK() {
		
		assertEquals(0, getTableRowCount());

        Token token = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, token);

        assertEquals(1, getTableRowCount());

        // Add again the same token
        Either<IException, Integer> rowsAffectedEither = tokenDAO.createToken(dbConnection, token);
        assertEquals(false, rowsAffectedEither.isRight());
        
        IException exception = rowsAffectedEither.left().value();
        assertNotNull(exception);
        assertEquals(false, exception instanceof BusinessException);
	}
	
	@Test
	public void testGetAllTokens_OK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(2, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
	}
	
	@Test
	public void testGetAllTokens_NOK() {
		
		assertEquals(0, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(getTableRowCount(), tokens.size());
	}
	
	@Test
	public void testUpdateTokenByTokenValue_OK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(1, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
        
        Token createdToken = tokens.get(0);
        assertEquals(0, firstToken.compareTo(createdToken));
        
        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.updateTokenByTokenValue(dbConnection, createdToken.getTokenValue(), secondToken);
        
        Either<IException, List<Token>> updatedTokensListEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, updatedTokensListEither.isRight());
        
        List<Token> updatedTokensList = updatedTokensListEither.right().value();
        assertEquals(1, getTableRowCount());
        assertEquals(getTableRowCount(), updatedTokensList.size());
        
        Token updatedToken = updatedTokensList.get(0);
        assertEquals(0, secondToken.compareTo(updatedToken));
	}
	
	@Test
	public void testUpdateTokenByTokenValue_NOK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(1, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
        
        Token createdToken = tokens.get(0);
        assertEquals(0, firstToken.compareTo(createdToken));
        
        String unknownTokenValue = "unknownTokenValue";
        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.updateTokenByTokenValue(dbConnection, unknownTokenValue, secondToken);
        
        Either<IException, List<Token>> updatedTokenListEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, updatedTokenListEither.isRight());
        
        List<Token> updatedTokenList = updatedTokenListEither.right().value();
        assertEquals(1, getTableRowCount());
        assertEquals(getTableRowCount(), updatedTokenList.size());
        
        // The token was not updated because the given token value was not into the db.
        Token updatedToken = updatedTokenList.get(0);
        assertEquals(0, firstToken.compareTo(updatedToken));
	}
	
	@Test
	public void testDeleteTokenByTokenValue_OK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(2, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
        
        tokenDAO.deleteTokenByTokenValue(dbConnection, firstToken.getTokenValue());
        assertEquals(1, getTableRowCount());
        
        tokenDAO.deleteTokenByTokenValue(dbConnection, secondToken.getTokenValue());
        
        assertEquals(0, getTableRowCount());
	}

	@Test
	public void testDeleteTokenByTokenValue_NOK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(2, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
        
        String unknownTokenValue = "unknownTokenValue";
        tokenDAO.deleteTokenByTokenValue(dbConnection, unknownTokenValue);
        assertEquals(2, getTableRowCount());
	}
	
	@Test
	public void testDeleteTokenByUsername_OK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());

        List<Token> tokens = tokensEither.right().value();
        assertEquals(2, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
        
        tokenDAO.deleteTokenByUsername(dbConnection, firstToken.getUsername());
        assertEquals(1, getTableRowCount());
        
        tokenDAO.deleteTokenByUsername(dbConnection, secondToken.getUsername());
        assertEquals(0, getTableRowCount());
	}
	
	@Test
	public void testDeleteTokenByUsername_NOK() {
		
		assertEquals(0, getTableRowCount());

        Token firstToken = TokenFactoryForTests.getDefaultTestToken();
        tokenDAO.createToken(dbConnection, firstToken);

        assertEquals(1, getTableRowCount());

        Token secondToken = TokenFactoryForTests.getDefaultTestToken(2);
        tokenDAO.createToken(dbConnection, secondToken);

        assertEquals(2, getTableRowCount());
        
        Either<IException, List<Token>> tokensEither = tokenDAO.getAllTokens(dbConnection);
        assertEquals(true, tokensEither.isRight());
        
        List<Token> tokens = tokensEither.right().value();
        assertEquals(2, getTableRowCount());
        assertEquals(getTableRowCount(), tokens.size());
        
        String unknownUsername = "unknownUsername";
        tokenDAO.deleteTokenByUsername(dbConnection, unknownUsername);
        assertEquals(2, getTableRowCount());
	}
}