package com.siteminder.exam.provider.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.siteminder.exam.request.EmailRequest;

@SpringBootTest
class MailgunProviderImplTest {

	@Bean
	public RestTemplate mockRestTemplate() {
		return Mockito.mock(RestTemplate.class);
	}
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

	private EmailRequest mockRequest() {
		return EmailRequest.builder()
				.toEmails(Arrays.asList("email@test.com"))
				.content("this is a test content")
				.subject("this is a test subject")
				.build();
	}
	
}
