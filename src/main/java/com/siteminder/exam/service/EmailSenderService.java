package com.siteminder.exam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.siteminder.exam.exception.EmailSendingException;
import com.siteminder.exam.provider.IEmailProvider;
import com.siteminder.exam.request.EmailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

	@Value("${email.retry}")
	private int maxRetries;
	
	private final List<IEmailProvider> emailProviders;
	
	/*
	 * Failover is handled in this service method
	 * A retry process has been added based from the configuration
	 * 
	 */
	public void send(EmailRequest emailRequest) {
		int retry = 0;
		for(;;) {
			var sent = false;
			for(IEmailProvider provider : emailProviders) {
				try {
					sent = provider.sendEmail(emailRequest);
					if(sent == true) {
						break;
					}
				} catch (EmailSendingException e) {
					log.warn("Encountered error while sending the email. Provider: {}. Cause: {}",
							provider.getClass(), e.getMessage());
					if(retry == maxRetries) {
						log.error("Error sending email after 3 retries.", e);
						throw e;
					}
				}
			}
			if(sent == true) {
				break;
			}
			retry++;
			log.debug("Retry sending: {}", retry);
		}
	}
}