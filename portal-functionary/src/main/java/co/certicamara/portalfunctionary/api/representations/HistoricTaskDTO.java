package co.certicamara.portalfunctionary.api.representations;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Creation of a Json of a historic task entity. 
 * 
 * @author Lean Factory
 */
public class HistoricTaskDTO {

    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * The id of the task
     */
    private final String id;

    /**
     * The name of the task
     */
    private final String name;

    /**
     * The name of the process the task belongs to
     */
    private final String processName;

    /**
     * The name of the user to whom the next task was assigned to
     */
    private final String assignedUser;

    /**
     * The process instance id to which the task belongs to
     */
    private final String processInstanceId;

    /**
     * The date when the task was completed
     */
    private final Instant completedDate;

    /**
     * The user who initiated the process
     */
    private final String user;


    ////////////////////////
    // Constructor
    ////////////////////////

    /**
     * Implementation of the json based on the given attributes.
     * @param id the id
     * @param name name of the task
     * @param processName the name of the process the task belongs to
     * @param assignedUser the name of the user to whom the next task was assigned to
     * @param processInstanceId the process id
     * @param completedDate the date when the task was completed
     * @param user the user who initiated the process
     */
    @JsonCreator
    public HistoricTaskDTO(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("processName") String processName,
            @JsonProperty("assignedUser") String assignedUser, @JsonProperty("processInstanceId") String processInstanceId, @JsonProperty("completedDate") Instant completedDate,
            @JsonProperty("userDTO") String user) {
        super();
        this.id = id;
        this.name = name;
        this.processName = processName;
        this.assignedUser = assignedUser;
        this.processInstanceId = processInstanceId;
        this.completedDate = completedDate;
        this.user = user;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////



	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public String getProcessName() {
		return processName;
	}


	public String getAssignedUser() {
		return assignedUser;
	}


	public String getProcessInstanceId() {
		return processInstanceId;
	}


	public Instant getCompletedDate() {
		return completedDate;
	}


	public String getUser() {
		return user;
	}



}