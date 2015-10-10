package gp.e3.autheo.authorization.providers;

import gp.e3.autheo.authorization.filter.InternalRequestFilter;
import gp.e3.autheo.client.dtos.TokenDTO;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public class InternalTokenDTOProvider extends AbstractHttpContextInjectable<TokenDTO> implements InjectableProvider<Context, Type> {
    
    @Context
    private HttpServletRequest httpRequest;
    
    @Override
    public Injectable<TokenDTO> getInjectable(ComponentContext ic, Context a, Type c) {
        
        if (c.equals(TokenDTO.class)) {
            return this;
        }
        
        return null;
    }
    
    @Override
    public ComponentScope getScope() {
        
        return ComponentScope.PerRequest;
    }
    
    @Override
    public TokenDTO getValue(HttpContext c) {
        
        TokenDTO token = (TokenDTO) httpRequest.getAttribute(InternalRequestFilter.TOKEN_ATTRIBUTE);
        
        return token;
    }
}