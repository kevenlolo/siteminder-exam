package com.siteminder.exam.provider.impl;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.siteminder.exam.exception.EmailSendingException;
import com.siteminder.exam.provider.IEmailProvider;
import com.siteminder.exam.request.EmailRequest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Primary
@Order(value = 1)
public class MailgunProviderImpl implements IEmailProvider {

	private final RestTemplate restTemplate;
	
	@Value("${email.mailgun.domain_url}")
	private String domainUrl;
	
	@Value("${email.mailgun.auth_header}")
	private String authKey;
	
	@Value("${email.default_from}")
	private String defaultFrom;
	
	@Override
	public boolean sendEmail(EmailRequest emailRequest) {
		boolean isSent = false;
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		addRecipients("to", emailRequest.getToEmails(), map);
		addRecipients("cc", emailRequest.getCcEmails(), map);
		addRecipients("bcc", emailRequest.getBccEmails(), map);
		map.put("from", Arrays.asList(defaultFrom));
		map.put("subject", Arrays.asList(emailRequest.getSubject()));
		map.put("text", Arrays.asList(emailRequest.getContent()));
		
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", authKey);
		
		try {
			ResponseEntity<Object> response = restTemplate.exchange(domainUrl, HttpMethod.POST,
					new HttpEntity<Object>(map, headers), Object.class);
			if(HttpStatus.OK == response.getStatusCode()) {
				isSent = true;
				log.info("Email sent. To: {}, Subject: {}", emailRequest.getToEmails(), emailRequest.getSubject());
			}
		} catch (Exception e) {
			if(e instanceof HttpClientErrorException) {
				HttpClientErrorException clientExc = (HttpClientErrorException) e;
				throw new EmailSendingException(e.getMessage(), clientExc.getStatusCode());
			}
		}
		return isSent;
	}
	
	private void addRecipients(String key, List<String> values, MultiValueMap<String, String> map) {
		if(CollectionUtils.isNotEmpty(values)) {
			map.put(key, values);
		}
	}
}