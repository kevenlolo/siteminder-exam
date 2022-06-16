package com.siteminder.exam.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import com.siteminder.exam.exception.EmailSendingException;
import com.siteminder.exam.provider.IEmailProvider;
import com.siteminder.exam.provider.impl.MailJetProviderImpl;
import com.siteminder.exam.provider.impl.MailgunProviderImpl;
import com.siteminder.exam.request.EmailRequest;

@SpringBootTest
class EmailSenderServiceTest {

	private EmailSenderService emailSenderService;

	IEmailProvider mailgunProvider = mock(MailgunProviderImpl.class);
	IEmailProvider mailjetProvider = mock(MailJetProviderImpl.class);
	
	@PostConstruct
	public void setup() {
		emailSenderService = new EmailSenderService(
				Arrays.asList(mailgunProvider, mailjetProvider));
	}
	
	@BeforeEach
	public void before() {
		ReflectionTestUtils.setField(emailSenderService, "maxRetries", 3);
	}
	
	@Test
	public void should_sendAtFirstTry_forMailgun() {
		EmailRequest mockRequest = mockRequest();
		
		when(mailgunProvider.sendEmail(mockRequest)).thenReturn(true);
		emailSenderService.send(mockRequest);
		
		verify(mailgunProvider, times(1)).sendEmail(mockRequest);
		verify(mailjetProvider, times(0)).sendEmail(mockRequest);
		
	}
	
	@Test
	public void should_sendAtFirstTry_forMailjet() {
		EmailRequest mockRequest = mockRequest();

		when(mailgunProvider.sendEmail(mockRequest)).thenReturn(false);
		when(mailjetProvider.sendEmail(mockRequest)).thenReturn(true);
		emailSenderService.send(mockRequest);
		
		verify(mailgunProvider, times(1)).sendEmail(mockRequest);
		verify(mailjetProvider, times(1)).sendEmail(mockRequest);
		
	}
	
	@Test
	public void should_exhaustRetries() {
		EmailRequest mockRequest = mockRequest();

		when(mailgunProvider.sendEmail(mockRequest)).thenThrow(EmailSendingException.class);
		when(mailjetProvider.sendEmail(mockRequest)).thenThrow(EmailSendingException.class);
		
		EmailSendingException thrown = Assertions.assertThrows(EmailSendingException.class, () -> {
			emailSenderService.send(mockRequest);
		});
		assertEquals(EmailSendingException.class, thrown.getClass());
		
		verify(mailgunProvider, times(4)).sendEmail(mockRequest);
		verify(mailjetProvider, times(3)).sendEmail(mockRequest);
		
	}
	
	private EmailRequest mockRequest() {
		return EmailRequest.builder()
				.toEmails(Arrays.asList("email@test.com"))
				.content("this is a test content")
				.subject("this is a test subject")
				.build();
	}
}
