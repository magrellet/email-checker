package com.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;

@SpringBootApplication
public class EmailApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailApplication.class, args);
	}

	/*@Bean
	public IntegrationFlow mailListener() {
		return IntegrationFlows.from(Mail.imapInboundAdapter("imaps://martin.alejandro.grellet@gmail.com:<pwd>k@imap.gmail.com/INBOX")
						.shouldDeleteMessages(false).get(),
				e -> e.poller(Pollers.fixedRate(5000).maxMessagesPerPoll(1)))
				.<Message>handle((payload, header) -> logMail(payload))
				.get();
	}*/

}
