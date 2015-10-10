/**
 * 
 */
package co.certicamara.portalfunctionary.infrastructure.mom;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import co.certicamara.portalfunctionary.api.representations.ApprovalResponse;
import co.certicamara.portalfunctionary.domain.business.CaseBusiness;
import co.certicamara.portalfunctionary.domain.entities.RequestStatus;
import co.certicamara.portalfunctionary.infrastructure.client.rs.CustomerSettingsClient;
import co.certicamara.portalfunctionary.infrastructure.client.rs.entities.AsyncTxParams;
import co.certicamara.portalfunctionary.infrastructure.config.PortalFunctionaryApprovalConsumerQueuesInfo;
import co.certicamara.portalfunctionary.infrastructure.exceptions.BusinessException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.IException;
import co.certicamara.portalfunctionary.infrastructure.exceptions.TechnicalException;
import co.certicamara.portalfunctionary.infrastructure.mom.dtos.ApproveRequestDTO;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

import fj.data.Either;
import gp.e3.autheo.client.dtos.TokenDTO;

/**
 * @author Lean Factory
 *
 *
 */
public class ApproveRequestMessageReceiver extends Thread {

    /////////////////////////
    // Attributes
    /////////////////////////

    private static final int DEFAULT_MESSAGE_MAX_RETRY = 1;
    private static int RETRY_ATTEMPTS_DEFAULT = 0;

    private CaseBusiness caseBusiness;

    private static ObjectMapper objectMapper;

    private CustomerSettingsClient customerSettingsClient;

    private PortalFunctionaryApprovalConsumerQueuesInfo approvalConsumerQueuesInfo;

    private String rabbitMQHost;

    ////////////////////////
    // Constructor
    ////////////////////////

    public ApproveRequestMessageReceiver (CaseBusiness caseBusiness, CustomerSettingsClient customerSettingsClient, 
            PortalFunctionaryApprovalConsumerQueuesInfo approvalConsumerQueuesInfo, String rabbitMQHost) {
        
        this.caseBusiness = caseBusiness;
        this.customerSettingsClient = customerSettingsClient;
        this.approvalConsumerQueuesInfo = approvalConsumerQueuesInfo;
        this.rabbitMQHost = rabbitMQHost;
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    @Override
    public void run() {

        try {
            System.out.println("RUN del threat");

            objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JSR310Module());

            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQHost);
            Connection connection = factory.newConnection();


            Channel channel = connection.createChannel();

            QueueingConsumer consumer = configChannel(channel);

            while (true) {

                QueueingConsumer.Delivery delivery = consumer.nextDelivery();

                String message = new String(delivery.getBody());

                AsyncMessage asyncMessage  = objectMapper.readValue(message, AsyncMessage.class);
                TokenDTO token  = objectMapper.readValue(asyncMessage.getToken(), TokenDTO.class);

                System.out.println(" [x] Received '" + message + "'");

                ApproveRequestDTO approval = objectMapper.readValue(asyncMessage.getJsonMessageContent(), ApproveRequestDTO.class);

                Either<IException, ApprovalResponse> either = caseBusiness.createNewCase(approval, token);

                deliveryConfirmation(channel, delivery, either, asyncMessage, approval);

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ShutdownSignalException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ConsumerCancelledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    
    ////////////////////////
    // Private Methods
    ////////////////////////

    private int getMessageMaxRetries() {

        Either<IException, AsyncTxParams> either = customerSettingsClient.getAsyncTxSettings();

        if (either.isRight()){

            AsyncTxParams asyncTxParams = either.right().value();
            return asyncTxParams.getMaxRetryAttempts();

        }else {

            return DEFAULT_MESSAGE_MAX_RETRY;
        }
    }



    private QueueingConsumer configChannel(Channel channel) throws IOException {

        // Config Response Queue
        channel.exchangeDeclare(approvalConsumerQueuesInfo.getResponseExchange(), approvalConsumerQueuesInfo.getResponseExchangeType());

        channel.queueDeclare(approvalConsumerQueuesInfo.getResponseQueue(), approvalConsumerQueuesInfo.isResponseQueueDurable(),
                approvalConsumerQueuesInfo.isResponseQueueExclusive(),approvalConsumerQueuesInfo.isResponseQueueAutoDelete(), null);

        channel.queueBind(approvalConsumerQueuesInfo.getResponseQueue(), approvalConsumerQueuesInfo.getResponseExchange(), "", null);

        // Config Work Queue
        channel.exchangeDeclare(approvalConsumerQueuesInfo.getWorkExchange(), approvalConsumerQueuesInfo.getWorkExchangeType());
        channel.queueDeclare(approvalConsumerQueuesInfo.getWorkQueue(), approvalConsumerQueuesInfo.isWorkQueueDurable(), 
                approvalConsumerQueuesInfo.isWorkQueueExclusive(), approvalConsumerQueuesInfo.isWorkQueueAutoDelete(), null);

        channel.queueBind(approvalConsumerQueuesInfo.getWorkQueue(), approvalConsumerQueuesInfo.getWorkExchange(), "", null);

        Map<String, Object> queueArgs = new HashMap<String, Object>();
        queueArgs.put("x-dead-letter-exchange", approvalConsumerQueuesInfo.getWorkExchange());
        queueArgs.put("x-message-ttl", approvalConsumerQueuesInfo.getRetryDelay());

        // Config Retry Queue
        channel.exchangeDeclare(approvalConsumerQueuesInfo.getRetryExchange(), approvalConsumerQueuesInfo.getRetryExchangeType());
        channel.queueDeclare(approvalConsumerQueuesInfo.getRetryQueue(), approvalConsumerQueuesInfo.isRetryQueueDurable(), 
                approvalConsumerQueuesInfo.isRetryQueueExclusive(), approvalConsumerQueuesInfo.isRetryQueueAutoDelete(), queueArgs);

        channel.queueBind(approvalConsumerQueuesInfo.getRetryQueue(),approvalConsumerQueuesInfo.getRetryExchange(), "", null);

        // Config UnDelivered messages

        channel.exchangeDeclare(approvalConsumerQueuesInfo.getUndeliveredExchange(), approvalConsumerQueuesInfo.getUndeliveredExchangeType());
        channel.queueDeclare(approvalConsumerQueuesInfo.getUndeliveredQueue(), approvalConsumerQueuesInfo.isUndeliveredQueueDurable(), 
                approvalConsumerQueuesInfo.isUndeliveredQueueExclusive(), approvalConsumerQueuesInfo.isUndeliveredQueueAutoDelete(),null);

        channel.queueBind(approvalConsumerQueuesInfo.getUndeliveredQueue(), approvalConsumerQueuesInfo.getUndeliveredExchange(), "", null);

        //How many UnAcknowledge messages the channel can receive- 0 unlimited 
        channel.basicQos(0, false);

        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(approvalConsumerQueuesInfo.getWorkQueue(), false, consumer);

        return consumer;
    }

    //	

    private void deliveryConfirmation(Channel channel, QueueingConsumer.Delivery delivery, Either<IException, ApprovalResponse> either, 
            AsyncMessage asyncMessage, ApproveRequestDTO approval) throws IOException {

        // TODO ENVIAR ACKNOWLEDGE  MULTIPLES DESPUES DE N MENSAJES
        int messageMaxRetries = getMessageMaxRetries();

        if (either.isLeft()){

            if (either.left().value() instanceof TechnicalException){

                System.out.println("TECHNICAL' ");

                if (asyncMessage.getRetryAttemptsCount()< asyncMessage.getMaxRetryAttempts()){


                    asyncMessage.addAttemptToCount();
                    String stringAsyncMessage = getStringAsyncMessage(asyncMessage);
                    channel.basicPublish(approvalConsumerQueuesInfo.getRetryExchange(),"", null, stringAsyncMessage.getBytes());
                }else {

                    // TODO NOTIFICAR, GUARDAR O HACER SABER QUE ESTE MENSAJE NO PUDO SER ENTREGADO
                    System.out.println("MENSAJE NO ENTREGADO'");
                    asyncMessage.refreshAttemptCount();
                    String stringAsyncMessage = getStringAsyncMessage(asyncMessage);
                    channel.basicPublish(approvalConsumerQueuesInfo.getUndeliveredExchange(),"", null, stringAsyncMessage.getBytes());

                }

            } else if (either.left().value() instanceof BusinessException){

                System.out.println("BUSINESS'");
                ApprovalResponse approvalResponse = new ApprovalResponse(approval.getId(), null, RequestStatus.REJECTED, null, approval.getObservations(), either.left().value().getErrorMessage(),
                        approval.getAssignedPerson());
                String approvalInfoStrng = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(approvalResponse, JsonNode.class));
                AsyncMessage responseAsyncMessage = new AsyncMessage (asyncMessage.getToken(),messageMaxRetries, RETRY_ATTEMPTS_DEFAULT , approvalInfoStrng);
                String stringAsyncMessage = getStringAsyncMessage(responseAsyncMessage);
                channel.basicPublish(approvalConsumerQueuesInfo.getResponseExchange(),"", null, stringAsyncMessage.getBytes());

            } 

        }else{

            System.out.println("ENVIANDO RESPUESTA EXITOSA'");
            asyncMessage.addAttemptToCount();
            ApprovalResponse approvalResponse = either.right().value();
            System.out.println("Case Info: "+ approvalResponse.getCaseInfo());
            String approvalInfoStrng = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(approvalResponse, JsonNode.class));
            AsyncMessage responseAsyncMessage = new AsyncMessage (asyncMessage.getToken(),messageMaxRetries, RETRY_ATTEMPTS_DEFAULT , approvalInfoStrng);
            String stringAsyncMessage = getStringAsyncMessage(responseAsyncMessage);
            channel.basicPublish(approvalConsumerQueuesInfo.getResponseExchange(),"", null, stringAsyncMessage.getBytes());
        }

        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

    }

    private static String getStringAsyncMessage( Object message) throws JsonProcessingException {


        String jsonMessageContent = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(objectMapper.convertValue(message, JsonNode.class));

        return jsonMessageContent;
    }

}
