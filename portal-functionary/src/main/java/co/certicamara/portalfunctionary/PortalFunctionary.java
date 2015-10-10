package co.certicamara.portalfunctionary;

import gp.e3.autheo.client.filter.RequestFilter;
import gp.e3.autheo.client.providers.TokenDTOProvider;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import co.certicamara.portalfunctionary.api.healthcheck.RedisHealthCheck;
import co.certicamara.portalfunctionary.api.resources.TaskResource;
import co.certicamara.portalfunctionary.domain.business.CaseBusiness;
import co.certicamara.portalfunctionary.domain.business.TaskBusiness;
import co.certicamara.portalfunctionary.infrastructure.client.rs.CustomerSettingsClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.DocumentManagerClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.WorkflowManagerClient;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryAddEventToRequestPublishQueuesInfo;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryApprovalConsumerQueuesInfo;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryConfig;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryRabbitMQConfig;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryRedisConfig;
import co.certicamara.portalfunctionary.infrastructure.mom.ApproveRequestMessageReceiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This class represents the entry point of the application.
 * 
 * @author LeanFactory
 */
public class PortalFunctionary extends Application<PortalFunctionaryConfig> {

    /**
     * Logger for exception messages 
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(PortalFunctionary.class);


    private ApproveRequestMessageReceiver asyncMessageReceiver;

    @Override
    public void initialize(Bootstrap<PortalFunctionaryConfig> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());
    }

    /**
     * Allow the application to support CORS requests.
     * 
     * @param environment, the object that contains the environment configuration.
     */
    private void initializeCORSSettings(Environment environment) {

        // Jetty CORS support, see: http://jitterted.com/tidbits/2014/09/12/cors-for-dropwizard-0-7-x/
        Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,HEAD,GET,POST,PUT,DELETE,PATCH");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Content-Length,Accept,Origin,Authorization");
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }

    private void configureJacksonObjectMapper(Environment environment) {

        ObjectMapper objectMapper = environment.getObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private void addAutheoFilterAndProvider(Environment environment) {

        String autheoUri = "http://localhost:9002/autheo/api/auth";
        String urlPattern = "/*";

        environment.servlets().addFilter("autheoFilter", new RequestFilter(autheoUri)).addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, urlPattern);
        environment.jersey().register(TokenDTOProvider.class);
    }

    /**
     * Inits a redis instance
     * @param redisConfig
     * @return
     */
    private JedisPool getRedisPoolInstance(PortalFunctionaryRedisConfig redisConfig) {

        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), redisConfig.getHost(), redisConfig.getPort(),5, null, redisConfig.getDatabase());
        return jedisPool;
    }

    /**
     * Creates a task resource
     * @param workflowManagerUrl the url of the workflow-manager
     * @param documentManagerUrl the url of the document-manager 
     * @return returns a task resource
     */
    private TaskResource getTaskResource(String workflowManagerUrl, String documentManagerUrl, String customerSettingsUrl, 
            PortalFunctionaryAddEventToRequestPublishQueuesInfo portalFunctionaryAddEventToRequestReportPublishQueuesInfo, PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig) {

        WorkflowManagerClient workflowManagerClient = new WorkflowManagerClient(workflowManagerUrl);
        DocumentManagerClient documentManagerClient = new DocumentManagerClient(documentManagerUrl);
        CustomerSettingsClient customerSettingsClient = new CustomerSettingsClient(customerSettingsUrl);
        TaskBusiness taskBusiness = new TaskBusiness(workflowManagerClient, documentManagerClient, customerSettingsClient, portalFunctionaryAddEventToRequestReportPublishQueuesInfo,
                portalFunctionaryRabbitMQConfig);
        return new TaskResource(taskBusiness);

    }


    /**
     * Creates a RequestApproval Consumer
     * @param approvalConsumerQueuesInfo 
     * @param workflowManagerUrl the url of the workflow-manager
     * @param documentManagerUrl the url of the document-manager
     * @param rabbitMQHost 
     * @return 
     */
    private ApproveRequestMessageReceiver getRequestApprovalConsumer(PortalFunctionaryApprovalConsumerQueuesInfo approvalConsumerQueuesInfo, String workflowManagerUrl, String documentManagerUrl, 
            String customerSettingsUrl, String rabbitMQHost ) {

        WorkflowManagerClient workflowManagerClient = new WorkflowManagerClient(workflowManagerUrl);
        DocumentManagerClient documentManagerClient = new DocumentManagerClient(documentManagerUrl);
        CustomerSettingsClient customerSettingsClient = new CustomerSettingsClient(customerSettingsUrl);
        CaseBusiness caseBusiness = new CaseBusiness(workflowManagerClient, documentManagerClient);
        return new ApproveRequestMessageReceiver(caseBusiness, customerSettingsClient, approvalConsumerQueuesInfo, rabbitMQHost);

    }

    

    @Override
    public void run(PortalFunctionaryConfig config, Environment environment) throws Exception {

        // Allow CORS
        initializeCORSSettings(environment);

        configureJacksonObjectMapper(environment);

        // Add authentication and authorization filter
        addAutheoFilterAndProvider(environment);

        // Initialize Redis.
        JedisPool jedisPool = getRedisPoolInstance(config.getPortalFunctionaryRedisConfig());

        String customerSettingsUrl = config.getPortalFunctionaryServiceClients().getCustomerSettings();
        String workflowManagerUrl = config.getPortalFunctionaryServiceClients().getWorkflowManagerUrl();
        String documentManagerUrl = config.getPortalFunctionaryServiceClients().getDocumentManagerUrl();
        
        PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig = config.getPortalFunctionaryRabbitMQConfig();
        String rabbitMQHost = portalFunctionaryRabbitMQConfig.getHost();

        PortalFunctionaryApprovalConsumerQueuesInfo approvalConsumerQueuesInfo = config.getPortalFunctionaryApprovalConsumerQueuesInfo();
        PortalFunctionaryAddEventToRequestPublishQueuesInfo portalFunctionaryAddEventToRequestReportPublishQueuesInfo = config.getPortalFunctionaryAddEventToRequestPublishQueuesInfo();

        asyncMessageReceiver = getRequestApprovalConsumer(approvalConsumerQueuesInfo, workflowManagerUrl, documentManagerUrl, customerSettingsUrl, rabbitMQHost);
        asyncMessageReceiver.start();

        TaskResource taskResource = getTaskResource(workflowManagerUrl, documentManagerUrl, customerSettingsUrl, portalFunctionaryAddEventToRequestReportPublishQueuesInfo, 
                portalFunctionaryRabbitMQConfig );
        
        RedisHealthCheck redisHealthCheck = new RedisHealthCheck(jedisPool);
        environment.healthChecks().register("redis", redisHealthCheck);

        environment.jersey().register(taskResource);
    }

    public static void main(String[] args) {

        try {

            PortalFunctionary portalFunctionary = new PortalFunctionary();
            portalFunctionary.run(args);

        } catch (Exception e) {

            LOGGER.error("PortalFunctionary :: main", e);
            System.exit(1);
        }
    }
}