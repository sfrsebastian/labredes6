package co.certicamara.portalfunctionary.infrastructure.config;

/**
 * This class contains the information required make Flyway work.
 * 
 * @author LeanFactory
 */
public class PortalFunctionaryFlywayConfig {

    private String tableName;

    private String scriptsLocation;

    public String getTableName() {
        return tableName;
    }

    public String getScriptsLocation() {
        return scriptsLocation;
    }

}
