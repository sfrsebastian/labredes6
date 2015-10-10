package co.certicamara.portalfunctionary.api.representations;

import java.time.LocalDateTime;

import co.certicamara.portalfunctionary.domain.entities.RequestStatus;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to model a request of the system.
 * 
 * @author LeanFactory
 */
public class RequestDTO {

    //////////////////////////////
    // Attributes
    //////////////////////////////

	private final String id;

	    private LocalDateTime requestDate;

	    private final String procedure;

	    private final String user;

	    private String status;

	    private LocalDateTime assignedDate;

	    private String assignedPerson;

	    private String subject;

	    private LocalDateTime deadline;

	    private String idCase;

	    private final String observations;

	    private RequestStatus requestStatus;
	    
	    private String username;
	    
	    private String organizationId;

    //////////////////////////////
    // Constructor
    //////////////////////////////

    @JsonCreator
    public RequestDTO(@JsonProperty("id") String id, @JsonProperty("procedure") String procedure, @JsonProperty("user") String user, 
            @JsonProperty("assignedDate") LocalDateTime assignedDate, @JsonProperty("assignedPerson") String assignedPerson, @JsonProperty("subject") String subject,
            @JsonProperty("deadline") LocalDateTime deadline, @JsonProperty("requestStatus") RequestStatus requestStatus, @JsonProperty("observations") String observations,
            @JsonProperty("username") String username, @JsonProperty("organizationId") String organizationId ) {
        
    	super();
        this.id = id;
        this.procedure = procedure;
        this.user = user;
        this.status = null;
        this.requestDate = LocalDateTime.now();
        this.assignedDate = assignedDate;
        this.assignedPerson = assignedPerson;
        this.subject = subject;
        this.deadline = deadline;
        this.requestStatus = requestStatus;
        this.observations = observations;
        this.username = username;
        this.organizationId = organizationId;
    }
    
    
    //////////////////////////////
    // Getter and Setters
    //////////////////////////////


	public String getId() {
		return id;
	}

	public LocalDateTime getRequestDate() {
		return requestDate;
	}

	public String getProcedure() {
		return procedure;
	}

	public String getUser() {
		return user;
	}

	public String getStatus() {
		return status;
	}

	public LocalDateTime getAssignedDate() {
		return assignedDate;
	}

	public String getAssignedPerson() {
		return assignedPerson;
	}

	public String getSubject() {
		return subject;
	}

	public LocalDateTime getDeadline() {
		return deadline;
	}

	public String getIdCase() {
		return idCase;
	}

	public String getObservations() {
		return observations;
	}

	public RequestStatus getRequestStatus() {
		return requestStatus;
	}

	public String getUsername() {
		return username;
	}

	public String getOrganizationId() {
		return organizationId;
	}

   
}