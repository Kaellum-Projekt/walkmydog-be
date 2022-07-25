package com.kaellum.walkmydog.emailsender;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:email-messages.properties")
public class EmailService {

    @Value("${activation.email.service.subject}") 
    String activationSubject;
    @Value("${activation.email.service.body}") 
    String activationBody;
    @Value("${reset-password.email.service.subject}") 
    String resetPassSubject;
    @Value("${reset-password.email.service.body}") 
    String resetPassBody;
    
	public void sendMail(String userFullName, String email, String code, EmailType emailType) throws Exception {
    	
    	final String username = "kaellumprojekt@hotmail.com";
        final String password = "gotosky2020!";
        
        Properties prop = new Properties();
        
        prop.put("mail.smtp.host", "smtp.office365.com");
        prop.put("mail.smtp.post", "587");
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
        	
        	final String subject;
            final String body;
        	
        	if(emailType.equals(EmailType.ACTIVATION)) {
        		subject = activationSubject;
        		body = String.format(activationBody, userFullName, email, code);
        	}else {
        		subject = resetPassSubject;
        		body = String.format(resetPassBody, userFullName, email, code);
        	}

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("kaellumprojekt@hotmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email));
            
            message.setSubject(subject);
            
//            String emailBody =
//            "<!DOCTYPE html>\r\n"
//            + "<html>\r\n"
//            + "<body>\r\n"
//            + "\r\n"
//            + "<p><b>Dear " + userFullName + ",<b></p>\r\n"
//            + "\r\n"
//            + "<p>Thank you for your subscription</p>"
//            + "<p>In order to utilize Kaellum's service, please click on the link below to activate your account</p>"
//            + "<p><i>(This link will expire in 30 minutes)<i></p>"
//            + "<p><a href=\"http://localhost:8080/api/user/activation/" + email +"/" + code +"\">Active your account now :)</a></p>\r\n"
//            + "\r\n"
//            + "</body>\r\n"
//            + "</html>";
            
            message.setContent(body, "text/html");
            
            Transport.send(message);

        } catch (MessagingException e) {
            throw e;
        }
    }

}
