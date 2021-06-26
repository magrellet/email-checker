package com.email.service;

import com.email.dto.EmailDto;
import com.email.model.Email;

import java.util.List;

/**
 * The interface Email service.
 */
public interface EmailService {

    /**
     * Is already saved boolean.
     *
     * @param email the email
     * @return the boolean
     */
    boolean isAlreadySaved(Email email);

    /**
     * Save email.
     *
     * @param email the email
     */
    void saveEmail(Email email);

    /**
     * Retrieve all saved emails list.
     *
     * @return the list
     */
    List<EmailDto> retrieveAllSavedEmails();
}
