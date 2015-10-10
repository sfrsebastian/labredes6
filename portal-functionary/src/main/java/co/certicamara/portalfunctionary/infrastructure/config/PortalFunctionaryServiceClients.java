package co.certicamara.portalfunctionary.infrastructure.config;

/**
 * This class contains the information required to get the service urls
 * 
 * @author LeanFactory
 */
public class PortalFunctionaryServiceClients {

    private String customerSettings;

    private String workflowManagerUrl;

    private String documentManagerUrl;

    private String singleWindowUrl;

    /**
     * Getter
     * @return
     */
    public String getCustomerSettings() {
        return customerSettings;
    }

    public String getWorkflowManagerUrl() {
        return workflowManagerUrl;
    }

    public String getDocumentManagerUrl() {
        return documentManagerUrl;
    }

    public String getSingleWindowUrl() {
        return singleWindowUrl;
    }
}