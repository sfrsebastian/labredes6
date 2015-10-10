/**
 * 
 */
package co.certicamara.portalfunctionary.infrastructure.mom.senders;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryRabbitMQConfig;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;
import co.certicamara.portalfunctionary.infrastructure.mom.AsyncMessage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;


/**
 * @author Lean Factory
 *
 *
 */
public class AsyncMessageSender {

    /////////////////////
    // Attributes
    /////////////////////

    private final static Logger LOGGER = LoggerFactory.getLogger(AsyncMessageSender.class);

    private static ConnectionFactory factory;

    private static Connection connection;

    private static Channel channel;

    private static final int DEFAULT_MESSAGE_RETRIES_COUNT = 0;
    
    /////////////////////
    // Public Methods
    /////////////////////

    public Either <IException, Boolean > sendAsyncMessage (TokenDTO token, int maxRetries,  Object message, String workQueue, boolean isWorkQueueDurable, boolean isWorkQueueExclusive, boolean isWorkQueueAutoDelete,
            PortalFunctionaryRabbitMQConfig portalFunctionaryRabbitMQConfig) {


        Either<IException, Boolean> either = null;

        try {

            factory = new ConnectionFactory();
            factory.setHost(portalFunctionaryRabbitMQConfig.getHost());

            getConnectionAndChannel();

            channel.queueDeclare(workQueue, isWorkQueueDurable, isWorkQueueExclusive, isWorkQueueAutoDelete, null);

            String asyncMessageString = getStringAsyncMessage(token, message, maxRetries);

            channel.basicPublish("", workQueue, null, asyncMessageString.getBytes());
            System.out.println(" [x] Sent '" + asyncMessageString + "'");

            either = Either.right(Boolean.TRUE);
            
        } catch (IOException e) {
            
            LOGGER.error("AsyncMessageSender:: sendAsyncMessage ", e);
            either = Either.left(new TechnicalException(e.getMessage()));
            
        }

        return either;
    }
    
    /////////////////////
    // Private Methods
    /////////////////////

    private static void closeConnectionAndChannel () throws IOException {

        channel.close();
        connection.close();
    }


    private static void getConnectionAndChannel() throws IOException {

        if (connection == null){
            connection = (Connection) factory.newConnection();
        }

        if (channel  == null){
            channel = ((com.rabbitmq.client.Connection) connection).createChannel();
        }
    }

    private static String getStringAsyncMessage(TokenDTO token, Object message, int maxRetries) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());

        String jsonMessageContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(message, JsonNode.class));

        AsyncMessage  asyncMessage = new AsyncMessage(token.toString(), maxRetries,DEFAULT_MESSAGE_RETRIES_COUNT, jsonMessageContent);

        String asyncMessageString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(asyncMessage, JsonNode.class));
        return asyncMessageString;
    }
}
