package com.email.controller;

import com.email.dto.EmailDto;
import com.email.model.Email;
import com.email.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Email controller.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private EmailService emailService;

    /**
     * Instantiates a new Email controller.
     *
     * @param emailService the email service
     */
    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    /**
     * Gets emails.
     *
     * @return the emails
     */
    @GetMapping
    public ResponseEntity<List<EmailDto>> getEmails() {
        try {
            return new ResponseEntity<>(emailService.retrieveAllSavedEmails(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Save email response entity.
     *
     * @param emailDto the email dto
     * @return the response entity
     */
    @PostMapping
    public ResponseEntity<List<EmailDto>> saveEmail(@RequestBody EmailDto emailDto) {
        try {

            emailService.saveEmail(Email.builder()
                    .from(emailDto.getFrom())
                    .subject(emailDto.getSubject())
                    .date(new Date())
                    .build());
            return new ResponseEntity<>(emailService.retrieveAllSavedEmails(), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}