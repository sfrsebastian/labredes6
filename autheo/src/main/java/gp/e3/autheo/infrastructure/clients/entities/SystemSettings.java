package gp.e3.autheo.infrastructure.clients.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SystemSettings {
    /**
     * Identifier
     */
    private final String name;
    /**
     * Domain without www
     */
    private final String domain;
    
    /**
     * Path to the keystore
     */
    private final String keystorePath;
    
    /**
     * Path to the crl
     */
    private final String crlPath;
    
    /**
     * url to tsa
     */
    private final String tsaUrl;
    
    /**
     * url to tsa policy
     */
    private final String tsaPolicy;
    
    /**
     * 
     */
    private final String ocspServer;
    
    /**
     * 
     */
    private final String verificationType;
    


    @JsonCreator
    public SystemSettings(@JsonProperty("domain")String domain, @JsonProperty("_id")String name,
                          @JsonProperty("keystorePath") String keystorePath, @JsonProperty("crlPath") String crlPath,
                          @JsonProperty("tsaUrl") String tsaUrl, @JsonProperty("tsaPolicy") String tsaPolicy,
                          @JsonProperty("ocspServer")String ocspServer, @JsonProperty("verificationType") String verificationType){

        this.domain = domain;
        this.name = name;
        this.keystorePath = keystorePath;
        this.crlPath = crlPath;
        this.tsaUrl = tsaUrl;
        this.tsaPolicy = tsaPolicy;
        this.ocspServer = ocspServer;
        this.verificationType = verificationType;
    }

    public String getDomain() {
        return domain;
    }
    
    public String getName() {
        return name;
    }
    
    public String getKeystorePath() {
        return keystorePath;
    }
    
    public String getCrlPath() {
        return crlPath;
    }
    
    public String getTsaUrl() {
        return tsaUrl;
    }
    
    public String getTsaPolicy() {
        return tsaPolicy;
    }
    
    public String getOcspServer() {
        return ocspServer;
    }
    
    public String getVerificationType() {
        return verificationType;
    }
}
