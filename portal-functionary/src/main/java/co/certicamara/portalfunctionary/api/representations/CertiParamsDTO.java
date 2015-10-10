package co.certicamara.portalfunctionary.api.representations;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CertiParamsDTO {

    /**
     * Params specific to the type of task
     */
    private Map<String, String> certiParams;

    ////////////////////////
    // Constructor
    ////////////////////////

    /**
     * Implementation of the json based on the given attributes.
     * @param certiParams The params that are specific to the task
     */
    public CertiParamsDTO(@JsonProperty("certiParams") Map<String, String> certiParams) {
        this.certiParams = certiParams;
    }

    ////////////////////////
    // Public Methods
    ////////////////////////

    @JsonAnyGetter
    public Map<String, String> getCertiParams() {
        return certiParams;
    }

    @JsonAnySetter
    public void setCertiParams(String name, String value) {

        if(certiParams == null) {
            certiParams = new HashMap<String, String>();
        }

        this.certiParams.put(name, value);
    }
}
