package com.email.controller;

import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.email.dto.EmailDto;

import com.email.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Email controller.
 */
@RestController
@RequestMapping("/api/email")
public class EmailController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

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
     * Gets emails from DB.
     *
     * @return the emails
     */
    @ApiOperation(value = "Get Email Info",
            httpMethod = "GET",
            notes = "Get email information from DB",
            response = ResponseEntity.class)
    @GetMapping
    public ResponseEntity<List<EmailDto>> getEmails() {
        try {
            LOGGER.info("Retrieving email list from DB");
            return new ResponseEntity<>(emailService.retrieveAllSavedEmails(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
