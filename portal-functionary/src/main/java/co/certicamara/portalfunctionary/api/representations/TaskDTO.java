package co.certicamara.portalfunctionary.api.representations;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Creation of a Json of a task entity. 
 * 
 * @author Lean Factory
 */
public class TaskDTO {

    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * The id of the task
     */
    private final String id;

    /**
     * The name of the task where the process is in
     */
    private final String taskNameWhereProcessIs;

    /**
     * The description of the task
     */
    private final String description;

    /**
     * The process instance id to which the task belongs to
     */
    private final String processInstanceId;
    
    /**
     * The name of the process to which the task belongs to
     */
    private final String processName;

    /**
     * The creation date of the task
     */
    private final Instant creationDate;

    /**
     * The date by which the task must be completed
     */
    private final Instant dueDate;

    /**
     * The person who is responsible for the resolution of the task where the process is in
     */
    private final String responsible;

    /**
     * Defines if the task is about to expire or expired
     */
    private final String status;

    /**
     * Defines a category task, It typify a task.  
     */
    private final String category;

    /**
     * Params specific to the type of task
     */
    private final CertiParamsDTO certiParams;

    /**
     * The list of form parameters of the task
     */
    private final List<FormPropertyDTO> formPropertiesParams;

    /**
     * The request of the task
     */
    private final RequestDTO requestDto; 

    /**
     * The list of documents of the task
     */
    private final List<DocumentDTO> documents;


    ////////////////////////
    // Constructor
    ////////////////////////


    /**
     * Implementation of the json based on the given attributes.
     * @param id the id
     * @param taksNameWhereProcessIs the name of the task where the process is in
     * @param description the description
     * @param processInstanceId the process id
     * @param processName the process name
     * @param creationDate the creation date
     * @param dueDate the due date
     * @param responsible the person who is responsible for the resolution of the task where the process is in
     * @param status the status of the task
     * @param category the category of the task
     * @param certiParams params specific to the type of task
     * @param formPropertiesParams the list of form parameters of the task
     * @param requestDTO the request of the task
     * @param documents the list of documents of the task
     */
    @JsonCreator
    public TaskDTO(@JsonProperty("id") String id, @JsonProperty("taskNameWhereProcessIs") String taskNameWhereProcessIs, @JsonProperty("description") String description, 
            @JsonProperty("processInstanceId") String processInstanceId, @JsonProperty("processName") String processName, @JsonProperty("creationDate") Instant creationDate, 
            @JsonProperty("dueDate") Instant dueDate, @JsonProperty("responsible") String responsible, @JsonProperty("status") String status, @JsonProperty("category") String category,
            @JsonProperty("certiParams") CertiParamsDTO certiParams, @JsonProperty("formPropertiesParams") List<FormPropertyDTO> formPropertiesParams, 
            @JsonProperty("requestDto") RequestDTO requestDto, @JsonProperty("documents") List<DocumentDTO> documents) {

        this.id = id;
        this.taskNameWhereProcessIs = taskNameWhereProcessIs;
        this.description = description;
        this.processInstanceId = processInstanceId;
        this.processName = processName;
        this.creationDate = creationDate;
        this.dueDate = dueDate;
        this.responsible = responsible;
        this.status = status;
        this.category = category;
        this.certiParams = certiParams;
        this.formPropertiesParams = formPropertiesParams;
        this.requestDto = requestDto;
        this.documents = documents;
    }


    ////////////////////////
    // Public Methods
    ////////////////////////

    public String getId() {
        return id;
    }

    public String getTaskNameWhereProcessIs() {
        return taskNameWhereProcessIs;
    }

    public String getDescription() {
        return description;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public String getProcessName() {
        return processName;
    }

    public Instant getCreationDate() {
        return creationDate;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public String getResponsible() {
        return responsible;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory() {
        return category;
    }

    public CertiParamsDTO getCertiParams() {
        return certiParams;
    }

    public List<FormPropertyDTO> getFormPropertiesParams() {
        return formPropertiesParams;
    }

    public RequestDTO getRequestDto() {
        return requestDto;
    }

    public List<DocumentDTO> getDocuments() {
        return documents;
    }
}