package com.email.service;

import com.email.dto.EmailDto;
import com.email.model.Email;
import com.email.repository.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * The type Email service.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    /**
     * The Email repository.
     */
    EmailRepository emailRepository;

    /**
     * Instantiates a new Email service.
     *
     * @param emailRepository the email repository
     */
    @Autowired
    public EmailServiceImpl(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    @Override
    public boolean isAlreadySaved(Email email) {
        return emailRepository
                .findByDateAndFromAndSubject(email.getDate(), email.getFrom(), email.getSubject())
                .isPresent();
    }

    @Override
    @Transactional
    public void saveEmail(Email email) {
        if (!isAlreadySaved(email)) {
            emailRepository.save(email);
        } else {
            //TODO: Apply logger
            System.out.println("email already in DB");
        }
    }

    @Override
    public List<EmailDto> retrieveAllSavedEmails() {
        return parseEntityToDto(emailRepository.findAll());
    }

    private List<EmailDto> parseEntityToDto(List<Email> emails) {
        List<EmailDto> emailList = new ArrayList<>();
        emails.forEach(email -> {
            emailList.add(EmailDto.builder()
                    .from(email.getFrom())
                    .subject(email.getSubject())
                    .date((parseDateToReadableString(email.getDate())))
                    .build());
        });
        return emailList;
    }

    private String parseDateToReadableString(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a"));
    }
}
