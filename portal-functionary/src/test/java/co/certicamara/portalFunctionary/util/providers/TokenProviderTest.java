package co.certicamara.portalFunctionary.util.providers;


import gp.e3.autheo.client.dtos.TokenDTO;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.server.impl.inject.AbstractHttpContextInjectable;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public class TokenProviderTest extends AbstractHttpContextInjectable<TokenDTO> implements InjectableProvider<Context, Type> {

    @Override
    public Injectable<TokenDTO>  getInjectable(ComponentContext ic, Context a, Type c) {

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

        String defaultString = "testToken";
        TokenDTO token = new TokenDTO(defaultString, defaultString, defaultString, defaultString, defaultString, defaultString, defaultString, defaultString);

        return token;
    }
}
