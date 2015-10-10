package co.certicamara.portalfunctionary.infrastructure.config;

/**
 * This class contains the information required to connect to redis.
 * 
 * @author LeanFactory
 */
public class PortalFunctionaryRedisConfig {

    private String host;

    private int port;

    private int database;


    /**
     * Getter
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     * Getter
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Getter
     * @return
     */
    public int getDatabase() {
        return database;
    }
}