package com.email.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.mail.search.SearchTerm;


import java.util.Arrays;
import java.util.Comparator;
import java.util.Properties;

//@Component
@Service
public class EmailListener {

    //@EventListener(ContextRefreshedEvent.class)

    //public void contextRefreshedEvent() throws MessagingException {
    @Scheduled(fixedDelay = 3000)
    public void scheduleEmailCheck() throws MessagingException {

        String email = "martin.alejandro.grellet@gmail.com";
        String pswd = "***";

        Session session = Session.getDefaultInstance(new Properties());
        Store store = session.getStore("imaps");
        store.connect("imap.googlemail.com", 993, email, pswd);
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        //Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        Message[] messages = inbox.getMessages();

        /*Arrays.sort(messages, Comparator.comparing((Message m) -> {
            try {
                return m.getSentDate();
            } catch (MessagingException e) {
                throw new RuntimeException( e );
            }
        }).reversed());*/

        for (Message message : messages) {
            System.out.println(String.format("Send Date: %s %s Subject: %s %s From: %s",
                    message.getSentDate(), System.lineSeparator(),message.getSubject(), System.lineSeparator(), message.getFrom()[0]));
        }
    }


}
