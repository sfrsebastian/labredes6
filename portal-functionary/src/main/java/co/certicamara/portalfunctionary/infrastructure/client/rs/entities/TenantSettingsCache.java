package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;
import fj.data.Either;

/**
 * Manages the tenantSetting object in redis cache 
 * 
 *
 */
public class TenantSettingsCache {

    /*
     * Redis poool
     */
    private JedisPool redisPool;

    /**
     * Time to expire the keys in redis
     */
    private final int EXPIRATION_TIME_IN_SECONDS = 60;

    /**
     * Logger object
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(TenantSettingsCache.class);

    /**
     * Constructor
     * @param redisPool
     */
    public TenantSettingsCache(JedisPool redisPool){
        this.redisPool = redisPool;
    }

    /**
     * Adds the tenantSettings object to redis
     * @param tenantName the tenant name
     * @param tenantSettings the tenant settings
     * @return Returns an either that can contain a IException in case there's an error or a boolean indicating if the action was done succesfully
     */
    public Either<IException, Boolean> addTenantSettings(String tenantName, TenantSettings tenantSettings) {

        Either<IException,Boolean> either =null;
        String answer = "NOK";

        try {

            Jedis redisClient = getRedisClient();
            answer = redisClient.set(tenantName, tenantSettings.toString());
            redisClient.expire(tenantName, EXPIRATION_TIME_IN_SECONDS);
            returnResource(redisClient);
            either = Either.right(answer.equalsIgnoreCase("OK"));

        } catch (Exception e) {

            LOGGER.error("TenantSettingsCache :: addTenantSettings", e);
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            either = Either.left(technicalException);

        }	

        return either;
    } 

    /**
     * Gets the tenantSettings from redis
     * @param tenantName the tenant name
     * @return Returns an either that can contain a IException in case there's an error or the tenant settings
     */
    public Either<IException, TenantSettings> getTenantSettings(String tenantName) {

        Either<IException,TenantSettings> either = null;
        TenantSettings tenantSettings = null;

        try {

            Jedis redisClient = getRedisClient();
            String tenantSettingsString = redisClient.get(tenantName);
            returnResource(redisClient);			
            tenantSettings = TenantSettings.buildTenantSettingsFromString(tenantSettingsString);
            either = Either.right(tenantSettings);
        } catch (Exception e) {

            LOGGER.error("TenantSettingsCache :: getTenantSettings", e);
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            either = Either.left(technicalException);
        }

        return either;	
    }

    private Jedis getRedisClient() {
        return redisPool.getResource();
    }

    private void returnResource(Jedis jedis){
        redisPool.returnResource(jedis);
    }

}
