package co.certicamara.portalfunctionary.infrastructure.config;

/**
 * This class contains the information required to get queues info
 * 
 * @author LeanFactory
 */
public class PortalFunctionaryApprovalConsumerQueuesInfo {

    // ///////////////////
    // Attributes
    // //////////////////

    private String workExchange;

    private String workExchangeType;

    private String workQueue;

    private boolean workQueueDurable;

    private boolean workQueueExclusive;

    private boolean workQueueAutoDelete;

    private String retryExchange;

    private String retryExchangeType;

    private String retryQueue;

    private boolean retryQueueDurable;

    private boolean retryQueueExclusive;

    private boolean retryQueueAutoDelete;

    private String undeliveredExchange;

    private String undeliveredExchangeType;

    private String undeliveredQueue;

    private boolean undeliveredQueueDurable;

    private boolean undeliveredQueueExclusive;

    private boolean undeliveredQueueAutoDelete;

    private String responseExchange;

    private String responseExchangeType;

    private String responseQueue;

    private boolean responseQueueDurable;

    private boolean responseQueueExclusive;

    private boolean responseQueueAutoDelete;

    private int maxMessageAcknowledge;

    private int retryDelay;

    // ///////////////////
    // Getters and Setters
    // //////////////////

    public String getWorkExchange() {
        return workExchange;
    }

    public void setWorkExchange(String workExchange) {
        this.workExchange = workExchange;
    }

    public String getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(String workQueue) {
        this.workQueue = workQueue;
    }

    public String getRetryExchange() {
        return retryExchange;
    }

    public void setRetryExchange(String retryExchange) {
        this.retryExchange = retryExchange;
    }

    public String getRetryQueue() {
        return retryQueue;
    }

    public void setRetryQueue(String retryQueue) {
        this.retryQueue = retryQueue;
    }

    public String getUndeliveredExchange() {
        return undeliveredExchange;
    }

    public void setUndeliveredExchange(String undeliveredExchange) {
        this.undeliveredExchange = undeliveredExchange;
    }

    public String getUndeliveredQueue() {
        return undeliveredQueue;
    }

    public void setUndeliveredQueue(String undeliveredQueue) {
        this.undeliveredQueue = undeliveredQueue;
    }

    public String getResponseExchange() {
        return responseExchange;
    }

    public void setResponseExchange(String responseExchange) {
        this.responseExchange = responseExchange;
    }

    public String getResponseQueue() {
        return responseQueue;
    }

    public void setResponseQueue(String responseQueue) {
        this.responseQueue = responseQueue;
    }

    public int getMaxMessageAcknowledge() {
        return maxMessageAcknowledge;
    }

    public void setMaxMessageAcknowledge(int maxMessageAcknowledge) {
        this.maxMessageAcknowledge = maxMessageAcknowledge;
    }

    public int getRetryDelay() {
        return retryDelay;
    }

    public void setRetryDelay(int retryDelay) {
        this.retryDelay = retryDelay;
    }

    public String getResponseExchangeType() {
        return responseExchangeType;
    }

    public void setResponseExchangeType(String responseExchangeType) {
        this.responseExchangeType = responseExchangeType;
    }

    public String getWorkExchangeType() {
        return workExchangeType;
    }

    public void setWorkExchangeType(String workExchangeType) {
        this.workExchangeType = workExchangeType;
    }

    public String getRetryExchangeType() {
        return retryExchangeType;
    }

    public void setRetryExchangeType(String retryExchangeType) {
        this.retryExchangeType = retryExchangeType;
    }

    public String getUndeliveredExchangeType() {
        return undeliveredExchangeType;
    }

    public void setUndeliveredExchangeType(String undeliveredExchangeType) {
        this.undeliveredExchangeType = undeliveredExchangeType;
    }

    public boolean isWorkQueueDurable() {
        return workQueueDurable;
    }

    public void setWorkQueueDurable(boolean workQueueDurable) {
        this.workQueueDurable = workQueueDurable;
    }

    public boolean isWorkQueueExclusive() {
        return workQueueExclusive;
    }

    public void setWorkQueueExclusive(boolean workQueueExclusive) {
        this.workQueueExclusive = workQueueExclusive;
    }

    public boolean isWorkQueueAutoDelete() {
        return workQueueAutoDelete;
    }

    public void setWorkQueueAutoDelete(boolean workQueueAutoDelete) {
        this.workQueueAutoDelete = workQueueAutoDelete;
    }

    public boolean isRetryQueueDurable() {
        return retryQueueDurable;
    }

    public void setRetryQueueDurable(boolean retryQueueDurable) {
        this.retryQueueDurable = retryQueueDurable;
    }

    public boolean isRetryQueueExclusive() {
        return retryQueueExclusive;
    }

    public void setRetryQueueExclusive(boolean retryQueueExclusive) {
        this.retryQueueExclusive = retryQueueExclusive;
    }

    public boolean isRetryQueueAutoDelete() {
        return retryQueueAutoDelete;
    }

    public void setRetryQueueAutoDelete(boolean retryQueueAutoDelete) {
        this.retryQueueAutoDelete = retryQueueAutoDelete;
    }

    public boolean isUndeliveredQueueDurable() {
        return undeliveredQueueDurable;
    }

    public void setUndeliveredQueueDurable(boolean undeliveredQueueDurable) {
        this.undeliveredQueueDurable = undeliveredQueueDurable;
    }

    public boolean isUndeliveredQueueExclusive() {
        return undeliveredQueueExclusive;
    }

    public void setUndeliveredQueueExclusive(boolean undeliveredQueueExclusive) {
        this.undeliveredQueueExclusive = undeliveredQueueExclusive;
    }

    public boolean isUndeliveredQueueAutoDelete() {
        return undeliveredQueueAutoDelete;
    }

    public void setUndeliveredQueueAutoDelete(boolean undeliveredQueueAutoDelete) {
        this.undeliveredQueueAutoDelete = undeliveredQueueAutoDelete;
    }

    public boolean isResponseQueueDurable() {
        return responseQueueDurable;
    }

    public void setResponseQueueDurable(boolean responseQueueDurable) {
        this.responseQueueDurable = responseQueueDurable;
    }

    public boolean isResponseQueueExclusive() {
        return responseQueueExclusive;
    }

    public void setResponseQueueExclusive(boolean responseQueueExclusive) {
        this.responseQueueExclusive = responseQueueExclusive;
    }

    public boolean isResponseQueueAutoDelete() {
        return responseQueueAutoDelete;
    }

    public void setResponseQueueAutoDelete(boolean responseQueueAutoDelete) {
        this.responseQueueAutoDelete = responseQueueAutoDelete;
    }

}