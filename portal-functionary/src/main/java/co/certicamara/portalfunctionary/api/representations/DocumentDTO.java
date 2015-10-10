package co.certicamara.portalfunctionary.api.representations;

import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A class to model a Document of the system.
 * 
 * @author LeanFactory
 */
public class DocumentDTO implements Comparable<DocumentDTO> {

    // ////////////////////////////
    // Attributes
    // ////////////////////////////

    private final long id;
    private final String name;
    private final List<VersionDTO> versionsList;

    // ////////////////////////////
    // Constructor
    // ////////////////////////////

    @JsonCreator
    public DocumentDTO(@JsonProperty("id") long id, @JsonProperty("name") String name, @JsonProperty("versionsList") List<VersionDTO> versionsList) {

        this.id = id;
        this.name = name;
        this.versionsList = versionsList;
    }

    public static DocumentDTO getDocument(long id, DocumentDTO document) {

        return new DocumentDTO(id, document.getName(), document.getVersionsList());
    }

    // ////////////////////////////
    // Getter and Setter
    // ////////////////////////////

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<VersionDTO> getVersionsList() {
        return versionsList;
    }

    @Override
    public int compareTo(DocumentDTO document) {

        int answer = 0;

        answer += (id == document.getId()) ? 0 : 1;
        answer += name.compareToIgnoreCase(document.getName());

        boolean sameNumberOfVersions = versionsList.size() == document.getVersionsList().size();
        answer += sameNumberOfVersions ? 0 : 1;

        return answer;
    }

    public static boolean validateDocument(DocumentDTO document) {

        return !StringUtils.isBlank(document.getName()) &&
                (document.getVersionsList() != null) && 
                (!document.getVersionsList().isEmpty());
    }
}