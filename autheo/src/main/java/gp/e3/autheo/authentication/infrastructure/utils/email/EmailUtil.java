package gp.e3.autheo.authentication.infrastructure.utils.email;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import fj.data.Either;
import gp.e3.autheo.authentication.infrastructure.exceptions.IException;
import gp.e3.autheo.authentication.infrastructure.exceptions.TechnicalException;

public class EmailUtil {

    ////////////////////////
    // Attributes
    ////////////////////////
    
    //TODO this should come from customer settings
    private final static String USERNAME = "steven.rigby@certicamara.com";
    
    private final static String PASSWORD = "LeanFactory123456789";
    
    private final static String SMTP_HOST = "smtp.gmail.com";
    
    private final static String SMTP_PORT = "587";

    ////////////////////////
    // Constructor
    ////////////////////////

    ////////////////////////
    // Public Methods
    ////////////////////////
    
    public static Either<IException, Boolean> sendEmail(String emailTo, String emailFrom, String subject, String htmlMessageContent) {
       
        Either<IException, Boolean> eitherResult = null;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", SMTP_PORT);

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });

       try {
           
          // Create a default MimeMessage object.
          MimeMessage message = new MimeMessage(session);

          // Set From: header field of the header.
          message.setFrom(new InternetAddress(emailFrom));

          // Set To: header field of the header.
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));

          // Set Subject: header field
          message.setSubject(subject);

          // Send the actual HTML message, as big as you like
          message.setContent(htmlMessageContent, "text/html");

          // Send message
          Transport.send(message);
          
          eitherResult = Either.right(true);
          
       } catch (MessagingException mex) {
          
           TechnicalException exception = new TechnicalException(mex.getMessage());
           eitherResult = Either.left(exception);
           
       }
       
       return eitherResult;
    }
    
    ////////////////////////
    // Private Methods
    ////////////////////////

}
