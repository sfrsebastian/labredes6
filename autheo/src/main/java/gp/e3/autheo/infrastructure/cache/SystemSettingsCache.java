package gp.e3.autheo.infrastructure.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.gson.Gson;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;
import gp.e3.autheo.infrastructure.clients.entities.SystemSettings;


public class SystemSettingsCache {
    /*
     * Redis poool
     */
    private JedisPool redisPool;

    /**
     * Time to expire the keys in redis
     */
    private final static int EXPIRATION_TIME_IN_SECONDS = 60;
    
    private final static String REDIS_KEY = "SystemSettings01";

    /**
     * Logger object
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(SystemSettingsCache.class);

    /**
     * Constructor
     * @param redisPool
     */
    public SystemSettingsCache(JedisPool redisPool){
        this.redisPool = redisPool;
    }
    
    /**
     * Adds the systemSettings object to redis
     * @param systemSettings the system settings
     * @return Returns an either that can contain a IException in case there's an error or a boolean indicating if the action was done succesfully
     */
    public Either<IException, Boolean> addSystemSettings(SystemSettings systemSettings) {

        Either<IException,Boolean> either =null;
        String answer = "NOK";

        try {

            Jedis redisClient = getRedisClient();
            answer = redisClient.set(REDIS_KEY, buildStringFromObject(systemSettings));
            redisClient.expire(REDIS_KEY, EXPIRATION_TIME_IN_SECONDS);
            returnResource(redisClient);
            either = Either.right("OK".equalsIgnoreCase(answer));

        } catch (Exception e) {

            LOGGER.error("SystemSettingsCache :: addSystemSettings", e);
            TechnicalException technicalException = new TechnicalException(e.getMessage());
            either = Either.left(technicalException);

        }   

        return either;
    } 
    
    /**
     * Gets the systemSettings from redis
     * @return Returns an either that can contain a IException in case there's an error or the system settings
     */
    public Either<IException, SystemSettings> getSystemSettings() {

        Either<IException,SystemSettings> either = null;
        SystemSettings systemSettings = null;

        try {

            Jedis redisClient = getRedisClient();
            String systemSettingsString = redisClient.get(REDIS_KEY);
            returnResource(redisClient);            
            systemSettings = buildSystemSettingsFromString(systemSettingsString);
            either = Either.right(systemSettings);
        } catch (Exception e) {

            LOGGER.error("SystemSettingsCache :: getSystemSettings", e);
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
    
    private SystemSettings buildSystemSettingsFromString(String systemSettingsString) {

        Gson gson = new Gson();
        return gson.fromJson(systemSettingsString, SystemSettings.class);
    }
    
    private String buildStringFromObject(SystemSettings systemSettings){
        Gson gson = new Gson();
        return gson.toJson(systemSettings);
    }
}
