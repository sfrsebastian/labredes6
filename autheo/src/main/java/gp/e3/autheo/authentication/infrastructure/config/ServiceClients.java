package gp.e3.autheo.authentication.infrastructure.config;

import io.dropwizard.Configuration;

/**
 * This class contains the information required to get the service urls
 * 
 * @author LeanFactory
 */
public class ServiceClients extends Configuration {

    private String customerSettings;

    /**
     * Getter
     * @return
     */
    public String getCustomerSettings() {
        return customerSettings;
    }

}