package gp.e3.autheo.authentication.infrastructure.config;

import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SigningConfig extends Configuration {

    @NotNull
    @NotEmpty
    @JsonProperty
    private String crlPath;

    @NotNull
    @NotEmpty
    @JsonProperty
    private String keystorePath;
    
    @NotNull
    @NotEmpty
    @JsonProperty
    private String ocspServer;
    
    @NotNull
    @NotEmpty
    @JsonProperty    
    private String verificationType;

    
    public String getCrlPath() {
	return crlPath;
    }

    public String getKeystorePath() {
	return keystorePath;
    }
    
    public String getOcspServer() {
        return ocspServer;
    }
    
    public String getVerificationType() {
        return verificationType;
    }

}