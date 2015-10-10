package co.certicamara.portalfunctionary.api.representations;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to model a list of historic tasks of the system.
 * 
 * @author LeanFactory
 */
public class QueryHistoricTaskListResponse {

    //////////////////////////////
    // Attributes
    //////////////////////////////


    /**
     * The list of historic tasks
     */
    private List<HistoricTaskDTO> historicTasksList;

    //////////////////////////////
    // Constructor
    //////////////////////////////

    @JsonCreator
    public QueryHistoricTaskListResponse( @JsonProperty("tasksList") List<HistoricTaskDTO> historicTasksList) {
        this.historicTasksList = historicTasksList;
    }

    //////////////////////////////
    // Getter and Setters
    //////////////////////////////


    public List<HistoricTaskDTO> getHistoricTasksList() {
        return historicTasksList;
    }

}
