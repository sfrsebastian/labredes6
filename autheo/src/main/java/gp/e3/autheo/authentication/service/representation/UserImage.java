package gp.e3.autheo.authentication.service.representation;

public class UserImage {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    private final byte[] image;

    ////////////////////////
    // Constructor
    ////////////////////////
    
    public UserImage(byte[] image) {
        
        this.image = image;
    }
    
    ////////////////////////
    // Public Methods
    ////////////////////////

    public byte[] getImage() {
        return image;
    }

}
