/**
 * 
 */
package co.certicamara.portalfunctionary.api.representations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Class to model a document version.
 * 
 * @author Lean Factory
 */
public class VersionDTO implements Comparable<VersionDTO> {

    // ////////////////////////////
    // Attributes
    // ////////////////////////////

    private final String path;
    private final String ownerName;
    private final String ownerRole;

    private final LocalDateTime creationDate;
    private final String type;

    /**
     * Size of the document version in KB.
     */
    private final BigDecimal size;

    private final long versionNumber;
    private final byte[] file;

    // ////////////////////////////
    // Constructor
    // ////////////////////////////

    @JsonCreator
    public VersionDTO(@JsonProperty("path") String path, @JsonProperty("ownerName") String ownerName, @JsonProperty("ownerRole") String ownerRole,
            @JsonProperty("creationDate") LocalDateTime creationDate, @JsonProperty("type") String type, @JsonProperty("size") BigDecimal size,
            @JsonProperty("versionNumber") long versionNumber, @JsonProperty("file") byte[] file) {

        this.path = path;
        this.ownerName = ownerName;
        this.ownerRole = ownerRole;
        this.creationDate = creationDate;
        this.type = type;
        this.size = size;
        this.versionNumber = versionNumber;
        this.file = file;
    }

    // ////////////////////////////
    // Getters
    // ////////////////////////////

    public String getPath() {
        return path;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerRole() {
        return ownerRole;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getSize() {
        return size;
    }

    public long getVersionNumber() {
        return versionNumber;
    }

    public byte[] getFile() {
        return file;
    }

    @Override
    public int compareTo(VersionDTO version) {

        int answer = 0;

        answer += path.compareTo(version.getPath());
        answer += ownerName.compareTo(version.getOwnerName());
        answer += ownerRole.compareTo(version.getOwnerRole());
        answer += creationDate.compareTo(version.getCreationDate());
        answer += type.compareToIgnoreCase(version.getType());
        answer += size.compareTo(version.getSize());
        answer += versionNumber == version.getVersionNumber() ? 0 : 1;

        boolean bothFilesAreNull = (file == null) && (version.getFile() == null);
        boolean bothFilesAreValid = (file != null) && (version.getFile() != null);

        if (bothFilesAreNull) {

            answer += 0;

        } else if (bothFilesAreValid) {

            boolean filesAreTheSameLenght = file.length == version.getFile().length;
            answer += filesAreTheSameLenght ? 0 : 1;

        } else {

            answer += 1;
        }

        return answer;
    }
}