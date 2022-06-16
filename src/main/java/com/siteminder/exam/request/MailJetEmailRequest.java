package com.siteminder.exam.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class MailJetEmailRequest {

	@JsonProperty("Messages")
	private List<Message> messages;
	
	@Setter
	@Getter
	public static class Message {

		@JsonProperty("From")
		private Email from;

		@JsonProperty("Subject")
		private String subject;

		@JsonProperty("TextPart")
		private String textPart;

		@JsonProperty("To")
		private List<Email> to;

		@JsonProperty("Cc")
		private List<Email> cc;

		@JsonProperty("Bcc")
		private List<Email> bcc;
	}
	
	@Setter
	@Getter
	@AllArgsConstructor
	public static class Email {
		private String email;
	}
}
