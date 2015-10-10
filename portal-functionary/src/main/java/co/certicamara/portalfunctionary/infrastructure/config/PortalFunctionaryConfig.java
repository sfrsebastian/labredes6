package co.certicamara.portalfunctionary.infrastructure.config;

import io.dropwizard.Configuration;

/**
 * The class to configure the whole application. 
 * This class is filled with the data in the YAML file.
 * 
 * @author LeanFactory
 */
public class PortalFunctionaryConfig extends Configuration {

    /////////////////////
    // Attributes
    ////////////////////

    private PortalFunctionaryServiceClients portalFunctionaryServiceClients;

    private PortalFunctionaryRedisConfig portalFunctionaryRedisConfig;

    private PortalFunctionaryApprovalConsumerQueuesInfo portalFunctionaryApprovalConsumerQueuesInfo;
    
    private PortalFunctionaryAddEventToRequestPublishQueuesInfo portalFunctionaryAddEventToRequestPublishQueuesInfo;

    private PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig;

    // ///////////////////
    // Methods
    // //////////////////

    public PortalFunctionaryServiceClients getPortalFunctionaryServiceClients() {
        return portalFunctionaryServiceClients;
    }

    public PortalFunctionaryRedisConfig getPortalFunctionaryRedisConfig() {
        return portalFunctionaryRedisConfig;
    }

    public PortalFunctionaryRabbitMQConfig getPortalFunctionaryRabbitMQConfig() {
        return portalFunctionaryRabbitMQConfig;
    }

    public PortalFunctionaryApprovalConsumerQueuesInfo getPortalFunctionaryApprovalConsumerQueuesInfo() {
        return portalFunctionaryApprovalConsumerQueuesInfo;
    }

    public PortalFunctionaryAddEventToRequestPublishQueuesInfo getPortalFunctionaryAddEventToRequestPublishQueuesInfo() {
        return portalFunctionaryAddEventToRequestPublishQueuesInfo;
    }

}