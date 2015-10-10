package co.certicamara.portalfunctionary.persistence.queryDefiners.interfaces;

import java.util.List;
import java.util.Map.Entry;

public interface IRequestQueryDefiner {

    /**
     * Defines the insert sentence to create a request
     * 
     * @return returns the insert sentence as a string
     * 
     * @author LeanFactory
     */
    public String insertRequest();

    /**
     * Defines the query sentence to find a request List by a criteria param list
     * 
     * @param paramsList The list of parameters used to build the query
     * @param queryAlerts Indicates if alerts are to be returned or not
     * @param alertTime indicates the time for a notification to be alerted before it expires
     * @return returns the query as a string
     * 
     * @author LeanFactory
     * 
     */
    public String requestListByCriteria(List<Entry<String, List<String>>> paramsList, boolean queryAlerts, int alertTime);

    /**
     * Defines the query sentence to find a count the number of requests by a criteria param list
     * 
     * @param paramsList The list of parameters used to build the query
     * @param queryAlerts Indicates if alerts are to be returned or not
     * @param alertTime indicates the time for a notification to be alerted before it expires
     * @return returns the query as a string
     * 
     * @author LeanFactory
     */
    public String requestCountByCriteria(List<Entry<String, List<String>>> paramsList, boolean queryAlerts, int alertTime);




    public String updateRequestCaseIdSql();

}
