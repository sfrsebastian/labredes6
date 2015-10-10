package co.certicamara.portalfunctionary.api.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FormPropertyDTO {

    ////////////////////////
    // Attributes
    ////////////////////////

    /**
     * The key used to submit the property in
     * {@link FormService#submitStartFormData(String, java.util.Map)} or
     * {@link FormService#submitTaskFormData(String, java.util.Map)}
     */
    private String id;

    /** The display label */
    private String name;

    /** Type of the property. */
    private String type;

    /** Optional value that should be used to display in this property */
    private String value;

    /**
     * Is this property read to be displayed in the form and made accessible
     * with the methods {@link FormService#getStartFormData(String)} and
     * {@link FormService#getTaskFormData(String)}.
     */
    private boolean readable;

    /** Is this property expected when a user submits the form? */
    private boolean writable;

    /** Is this property a required input field */
    private boolean required;

    ////////////////////////
    // Constructor
    ////////////////////////

    /**
     * Implementation of the json based on the given attributes.
     * @param id
     * @param name
     * @param type
     * @param value
     * @param readable
     * @param writable
     * @param required
     */
    public FormPropertyDTO(@JsonProperty("id") String id, @JsonProperty("name") String name, @JsonProperty("type") String type, @JsonProperty("value") String value,
            @JsonProperty("readable") boolean readable, @JsonProperty("writable") boolean writable, @JsonProperty("required") boolean required) {
        super();
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.readable = readable;
        this.writable = writable;
        this.required = required;
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

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isReadable() {
        return readable;
    }

    public boolean isWritable() {
        return writable;
    }

    public boolean isRequired() {
        return required;
    }

}
