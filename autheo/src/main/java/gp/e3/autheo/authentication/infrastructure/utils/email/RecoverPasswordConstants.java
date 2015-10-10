package gp.e3.autheo.authentication.infrastructure.utils.email;

public class RecoverPasswordConstants {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    public final static String RECOVER_PASSWORD_SUBJECT = "Recuperación de la contraseña";
    
    

    ////////////////////////
    // Constructor
    ////////////////////////

    ////////////////////////
    // Public Methods
    ////////////////////////
    
    public static String getPasswordRecoveryMessageForCitizen(String passwordTokenValue) {
        
        //TODO the URL must be configurable
        String message = "<h3>Si desea recuperar su contraseña por favor dirigirse a la siguiente página: </h3>" +
                        "<br>" +
                        "<a href=\"http://192.168.168.186:9050/#/recover-password?token=" + passwordTokenValue + "\"> Cambio de contraseña </a>";
        
        return message;
        
    }
    
    public static String getPasswordRecoveryMessageForFunctionary(String passwordTokenValue) {
        
        //TODO the URL must be configurable
        String message = "<h3>Si desea recuperar su contraseña por favor dirigirse a la siguiente página: </h3>" +
                        "<br>" +
                        "<a href=\"http://192.168.168.186:9051/#/recover-password?token=" + passwordTokenValue + "\"> Cambio de contraseña </a>";
        
        return message;
        
    }

    ////////////////////////
    // Private Methods
    ////////////////////////

}
