package co.certicamara.portalfunctionary.api.healthcheck;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.codahale.metrics.health.HealthCheck;

public class RedisHealthCheck extends HealthCheck {

    ////////////////////////
    // Attributes
    ////////////////////////

    private final JedisPool pool;

    ////////////////////////
    // Constructor
    ////////////////////////

    public RedisHealthCheck(JedisPool pool) {
        
        this.pool = pool;
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    @Override
    protected Result check() throws Exception {
        
        Result result = null;
        
        try (Jedis jedis = pool.getResource()) {
            
            final String pong = jedis.ping();
            if ("PONG".equals(pong)) {
                
                result = Result.healthy();
            }
            
        } catch(Exception e) {
            
            result = Result.unhealthy("Cannot connect to redis");
        }
        
        return result;
    }

}
