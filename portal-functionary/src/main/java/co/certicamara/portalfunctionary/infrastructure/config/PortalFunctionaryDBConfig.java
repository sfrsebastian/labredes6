package co.certicamara.portalfunctionary.infrastructure.config;

/**
 * This class contains the information required to connect to the application DB.
 * 
 * @author LeanFactory
 */
public class PortalFunctionaryDBConfig {

    private String driverClass;

    private String user;

    private String password;

    private String url;

    private int removeAbandonedTimeoutInSeconds;
    private boolean ableToRemoveAbandonedConnections;

    /**
     * Getter
     * @return
     */
    public String getDriverClass() {
        return driverClass;
    }

    /**
     * Getter
     * @return
     */
    public String getUser() {
        return user;
    }

    /**
     * Getter
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * Getter
     * @return
     */
    public String getUrl() {
        return url;
    }

    /**
     * Getter
     * @return
     */
    public int getRemoveAbandonedTimeoutInSeconds() {
        return removeAbandonedTimeoutInSeconds;
    }

    /**
     * Is
     * @return
     */
    public boolean isAbleToRemoveAbandonedConnections() {
        return ableToRemoveAbandonedConnections;
    }
}