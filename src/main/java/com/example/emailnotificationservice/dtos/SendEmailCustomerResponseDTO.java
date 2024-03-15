package com.example.emailnotificationservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEmailCustomerResponseDTO {
    private String to;
    private String from;
    private String body;
    private String subject;
}
