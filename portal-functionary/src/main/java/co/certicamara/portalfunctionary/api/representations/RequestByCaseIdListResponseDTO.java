/**
 * 
 */
package co.certicamara.portalfunctionary.api.representations;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Lean Factory
 *
 */
public class RequestByCaseIdListResponseDTO {
	
	 ////////////////////////
    // Attributes
    ////////////////////////

    private final List<RequestDTO> requestList;
    
  
    
    ////////////////////////
    // Constructor
    ////////////////////////

    
    @JsonCreator
    public RequestByCaseIdListResponseDTO(@JsonProperty("requestList") List<RequestDTO> requestList) {
        
        this.requestList = requestList;
    }

    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public List<RequestDTO> getRequestList() {
		return requestList;
	}

   

}
