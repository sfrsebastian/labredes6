package gp.e3.autheo.authentication.infrastructure.healthchecks;

import redis.clients.jedis.Jedis;

import com.codahale.metrics.health.HealthCheck;

public class RedisHealthCheck extends HealthCheck {

	private Jedis redisClient;
	
	public RedisHealthCheck(Jedis redisClient) {
		
		this.redisClient = redisClient;
	}

	@Override
	protected Result check() throws Exception {
		
		return redisClient.isConnected()? Result.healthy() : Result.unhealthy("Redis client is not connected.");
	}
}