package gp.e3.autheo;

import gp.e3.autheo.authentication.infrastructure.config.RedisConfig;
import gp.e3.autheo.authentication.infrastructure.config.ServiceClients;
import gp.e3.autheo.authentication.infrastructure.config.SigningConfig;
import gp.e3.autheo.authentication.infrastructure.config.SqlDbConfig;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AutheoConfig extends Configuration {

	@NotNull
	@JsonProperty
	private SqlDbConfig sqlDbConfig;

	@NotNull
	@JsonProperty
	private RedisConfig redisConfig;
	
	@NotNull
	@JsonProperty
	private SigningConfig signingConfig;
	
	@NotNull
	@JsonProperty
	private ServiceClients serviceClients;

	public SqlDbConfig getSqlDbConfig() {
		return sqlDbConfig;
	}

	public RedisConfig getRedisConfig() {
		return redisConfig;
	}
	
	public SigningConfig getSigningConfig() {
        return signingConfig;
    }
	
	public ServiceClients getServiceClients() {
        return serviceClients;
    }
}