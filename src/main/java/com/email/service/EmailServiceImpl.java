package com.email.service;

import com.email.dto.EmailDto;
import com.email.exception.UnableToGetEmailBodyException;
import com.email.model.Email;
import com.email.repository.EmailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMultipart;

import javax.mail.search.FlagTerm;
import javax.transaction.Transactional;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * The type Email service.
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${mail.protocol}")
    private String mailProtocol;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private Integer port;

    @Value("${mail.folder}")
    private String mailFolder;

    @Value("${mail.username}")
    private String userName;

    @Value("${mail.pwd}")
    private String pwd;

    @Value("${mail.search.term:ALL}")
    private String searchTerm;

    @Value("${mail.word.to.search}")
    private String wordToSearch;


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
            LOGGER.info("Saving Email in DB");
            emailRepository.save(email);
        } else {
            LOGGER.info("Email already exist in DB! Subject: {} - From: {} - Date: {}"
                    , email.getSubject(), email.getFrom(), email.getDate());
        }
    }

    @Override
    public List<EmailDto> retrieveAllSavedEmails() {
        return parseEntityToDto(emailRepository.findAll());
    }

    @Scheduled(fixedDelay = 3000)
    public void scheduleEmailCheck() throws MessagingException {

        LOGGER.info("Connecting with {} account", userName);

        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore(mailProtocol);
        store.connect(host, port, userName, pwd);
        Folder inbox = store.getFolder(mailFolder);
        inbox.open(Folder.READ_ONLY);

        LOGGER.info("Getting email list from {}", userName);


        Message[] messages = "UNSEEN".equals(searchTerm)
                ? inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false))
                : inbox.getMessages();


        for (Message message : messages) {
            if (getTextFromMessageBody(message).contains(wordToSearch) || message.getSubject().contains(wordToSearch)) {
                LOGGER.info("Email founded with subject or body with word {} !", wordToSearch);
                saveEmail(Email.builder()
                        .date(message.getSentDate())
                        .from(parseFromMessage(message.getFrom()[0].toString()))
                        .subject(message.getSubject())
                        .build());
            }
        }
    }

    private String parseFromMessage(String fromMessage) {
        return fromMessage
                .substring(0, fromMessage.indexOf(">"))
                .substring(fromMessage.indexOf("<") + 1);
    }

    private List<EmailDto> parseEntityToDto(List<Email> emails) {
        List<EmailDto> emailList = new ArrayList<>();
        emails.forEach(email ->
                emailList.add(EmailDto.builder()
                        .from(email.getFrom())
                        .subject(email.getSubject())
                        .date((parseDateToReadableString(email.getDate())))
                        .build())
        );
        return emailList;
    }

    private String parseDateToReadableString(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy 'at' hh:mm a"));
    }


    private String getTextFromMessageBody(Message message) {
        try {
            if (message.isMimeType("text/plain")) {
                return message.getContent().toString();
            } else if (message.isMimeType("multipart/*")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                return getTextFromMimeMultipartMessageBody(mimeMultipart);
            } else {
                return "";
            }
        } catch (MessagingException | IOException e) {
            throw new UnableToGetEmailBodyException("Unable to get Email Body", e);
        }
    }

    private String getTextFromMimeMultipartMessageBody(MimeMultipart mimeMultipart) throws MessagingException, IOException {

        boolean isMultipartAlternative = new ContentType(mimeMultipart.getContentType()).match("multipart/alternative");
        if (isMultipartAlternative) {
            return getTextFromBodyPartMessageBody(mimeMultipart.getBodyPart(mimeMultipart.getCount() - 1));
        }
        StringBuilder textFromBody = new StringBuilder();
        for (int i = 0; i < mimeMultipart.getCount(); i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            textFromBody.append(getTextFromBodyPartMessageBody(bodyPart));
        }
        return textFromBody.toString();
    }

    private String getTextFromBodyPartMessageBody(BodyPart bodyPart) throws MessagingException, IOException {

        if (bodyPart.isMimeType("text/plain")) {
            return bodyPart.getContent().toString();
        } else if (bodyPart.isMimeType("text/html")) {
            return bodyPart.getContent().toString();
        } else if (bodyPart.getContent() instanceof MimeMultipart) {
            return getTextFromMimeMultipartMessageBody((MimeMultipart) bodyPart.getContent());
        } else {
            return "";
        }
    }
}
