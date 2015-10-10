package co.certicamara.portalfunctionary.infrastructure.client.rs.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;

/**
 * 
 * A class to model the tenant Settings of the system
 *
 */
public class TenantSettings {


    private final int id;

    private final RequestSettings requestSettings;

    private String name;
    
    /**
     * Max rows of a request response
     */
    private final String maxRows;

    @JsonCreator
    public TenantSettings(@JsonProperty("_id")int id, @JsonProperty("requestSettings")RequestSettings requestSettings, @JsonProperty("maxRows")String maxRows){
        this.id = id;
        this.requestSettings = requestSettings;
        this.maxRows = maxRows;
    }

    /**
     * Getter
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Getter
     * @return
     */
    public RequestSettings getRequestSettings() {
        return requestSettings;
    }

    /**
     * Getter
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return
     */
    public String getMaxRows() {
        return maxRows;
    }
    
    @Override
    public String toString() {

        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static TenantSettings buildTenantSettingsFromString(String tenantSettingsString) {

        Gson gson = new Gson();
        return gson.fromJson(tenantSettingsString, TenantSettings.class);
    }

}
