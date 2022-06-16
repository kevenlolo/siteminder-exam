package com.siteminder.exam.provider.impl;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.siteminder.exam.exception.EmailRequestException;
import com.siteminder.exam.exception.EmailSendingException;
import com.siteminder.exam.provider.IEmailProvider;
import com.siteminder.exam.request.EmailRequest;
import com.siteminder.exam.request.MailJetEmailRequest;
import com.siteminder.exam.request.MailJetEmailRequest.Message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Order(value = 2)
public class MailJetProviderImpl implements IEmailProvider {

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
	
	private final RestTemplate restTemplate;
	
	@Value("${email.mailjet.domain_url}")
	private String domainUrl;
	
	@Value("${email.mailjet.auth_header}")
	private String authKey;
	
	@Value("${email.default_from}")
	private String defaultFrom;
	
	@Override
	public boolean sendEmail(EmailRequest emailRequest) {
		
		boolean isSent = false;
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authKey);
		
		try {
			String request = buildRequest(emailRequest);
			ResponseEntity<Object> response = restTemplate.exchange(domainUrl, HttpMethod.POST,
					new HttpEntity<Object>(request, headers), Object.class);
			if(HttpStatus.OK == response.getStatusCode()) {
				isSent = true;
				log.info("Email sent. To: {}, Subject: {}", emailRequest.getToEmails(), emailRequest.getSubject());
			}
		} catch (Exception e) {
			if(e instanceof HttpClientErrorException) {
				HttpClientErrorException clientExc = (HttpClientErrorException) e;
				throw new EmailSendingException(e.getMessage(), clientExc.getStatusCode());
			} else if(e instanceof JsonProcessingException) {
				throw new EmailRequestException(e.getMessage());
			}
		}
		return isSent;
	}

	private String buildRequest(EmailRequest emailRequest) throws JsonProcessingException {
		Message message = new MailJetEmailRequest.Message();
		if(CollectionUtils.isNotEmpty(emailRequest.getBccEmails())) {
			message.setBcc(emailRequest.getBccEmails().stream()
					.map(e -> new MailJetEmailRequest.Email(e))
					.collect(Collectors.toList()));
		}
		if(CollectionUtils.isNotEmpty(emailRequest.getCcEmails())) {
			message.setCc(emailRequest.getCcEmails().stream()
					.map(e -> new MailJetEmailRequest.Email(e))
					.collect(Collectors.toList()));
		}
		message.setTo(emailRequest.getCcEmails().stream()
					.map(e -> new MailJetEmailRequest.Email(e))
					.collect(Collectors.toList()));
		message.setSubject(emailRequest.getSubject());
		message.setFrom(new MailJetEmailRequest.Email(defaultFrom));
		message.setTextPart(emailRequest.getContent());
		
		MailJetEmailRequest requestObj = new MailJetEmailRequest(Arrays.asList(message));
		
		return OBJECT_MAPPER.writeValueAsString(requestObj);
	}
}
