package co.labredes;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration.Dynamic;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import co.labredes.api.resources.VideoPayerResource;
import co.labredes.domain.business.VideoPlayerBusiness;
import co.labredes.infrastruncture.config.LabredesServerConfig;
import io.dropwizard.Application;
import io.dropwizard.java8.Java8Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class LabredesServer extends Application<LabredesServerConfig>{
	
	private final static Logger LOGGER = LoggerFactory.getLogger(LabredesServer.class);

    @Override
    public void initialize(Bootstrap<LabredesServerConfig> bootstrap) {
        bootstrap.addBundle(new Java8Bundle());
    }
    
    /**
     * Allow the application to support CORS requests.
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
    
    private VideoPayerResource createVideoPlayerResource(){
    	VideoPlayerBusiness videoPlayerBusiness = new VideoPlayerBusiness();
    	return new VideoPayerResource(videoPlayerBusiness);
    }
    
    @Override
    public void run(LabredesServerConfig labredesServerConfig, Environment environment){
    	
    	// Allow CORS
        initializeCORSSettings(environment);
        
        configureJacksonObjectMapper(environment);
        
        VideoPayerResource videoPayerResource = createVideoPlayerResource();
        
        environment.jersey().register(videoPayerResource);
    }
    
    public static void main(String[] args) {

        try {

            LabredesServer labredesServer = new LabredesServer();
            labredesServer.run(args);

        } catch (Exception e) {

            LOGGER.error("LabredesServer :: main", e);
            System.exit(1);
        }
    }
}
