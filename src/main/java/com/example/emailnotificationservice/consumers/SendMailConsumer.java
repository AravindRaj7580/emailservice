package com.example.emailnotificationservice.consumers;

import com.example.emailnotificationservice.MailSendingServer.EmailUtil;
import com.example.emailnotificationservice.dtos.SendEmailCustomerResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.Session;
import java.util.Properties;

@Service
public class SendMailConsumer {
    private ObjectMapper objectMapper;
    private EmailUtil emailUtil;

    public SendMailConsumer(EmailUtil emailUtil, ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
        this.emailUtil = emailUtil;
    }

    @KafkaListener(topics = "sendEmail", groupId = "emailService")
    public void handleSendEmail(String Message) {
        try{

            System.out.println("SimpleEmail Start");

            SendEmailCustomerResponseDTO sendEmailCustomerResponseDTO =  objectMapper.readValue(Message, SendEmailCustomerResponseDTO.class);

            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
            props.put("mail.smtp.port", "587"); //TLS Port
            props.put("mail.smtp.auth", "true"); //enable authentication
            props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

            //create Authenticator object to pass in Session.getInstance argument
            Authenticator auth = new Authenticator() {
                //override the getPasswordAuthentication method
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sendEmailCustomerResponseDTO.getFrom(), "should enable 2 step authetication in our gMail Account and add your new application name and get new password");
                }
            };
            Session session = Session.getInstance(props, auth);

            EmailUtil.sendEmail(session, sendEmailCustomerResponseDTO.getTo(),sendEmailCustomerResponseDTO.getSubject(), sendEmailCustomerResponseDTO.getBody());

        }catch (Exception e){
            System.out.print("Exception caught : " + e);
        }
    }
}
