package gp.e3.autheo.client.utils;

import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import gp.e3.autheo.authentication.domain.entities.Token;
import gp.e3.autheo.client.dtos.TokenDTO;

public class TokenDTOUtils {
    
    public static boolean isAValidToken(Token token) {

        return (token != null) && 
                !StringUtils.isBlank(token.getTokenValue()) && 
                !StringUtils.isBlank(token.getUsername()) && 
                !StringUtils.isBlank(token.getUserOrganization()) && 
                !StringUtils.isBlank(token.getUserRole());
    }

    public static TokenDTO buildTokenDTOFromTokenToString(String tokenToString) {

        Gson gson = new Gson();
        return gson.fromJson(tokenToString, TokenDTO.class);
    }
    
    public static TokenDTO buildTokenDTOFromToken(Token token) {

        TokenDTO tokenDTO = new TokenDTO(token.getTokenValue(), token.getUsername(), token.getUserOrganization(), token.getUserRole(), 
                token.getSystemRole(), token.getBusinessRole(), token.getUserDocumentType(), token.getUserDocumentNumber());
        return tokenDTO;
    }
}
