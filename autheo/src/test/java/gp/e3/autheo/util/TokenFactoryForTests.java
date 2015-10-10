package gp.e3.autheo.util;

import fj.data.Either;
import gp.e3.autheo.authentication.domain.business.TokenFactory;
import gp.e3.autheo.authentication.domain.business.constants.TokenTypes;
import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.authentication.domain.entities.User;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;

import java.util.ArrayList;
import java.util.List;

public class TokenFactoryForTests {

    public static Token getDefaultTestToken() {


        User user = UserFactoryForTests.getDefaultTestUser();
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        String tokenValue = tokenValueEither.right().value();
        Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.INTERNAL_API_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());

        return token;
    }

    public static Token getDefaultTestToken(int number) {

        User user = UserFactoryForTests.getDefaultTestUser(number);
        Either<IException, String> tokenValueEither = TokenFactory.getToken(user);
        String tokenValue = tokenValueEither.right().value();
        Token token = new Token(tokenValue, user.getUsername(), user.getOrganizationId(), user.getRoleId(), TokenTypes.INTERNAL_API_TOKEN_TYPE.getTypeNumber(), user.getSystemRoleId(), user.getBusinessRoleId(),
                user.getDocumentType(), user.getDocumentNumber());

        return token;
    }

    public static List<Token> getTokenList(int listSize) {

        List<Token> tokens = new ArrayList<Token>();

        for (int i = 0; i < listSize; i ++) {
            tokens.add(getDefaultTestToken(i));
        }

        return tokens;
    }
}