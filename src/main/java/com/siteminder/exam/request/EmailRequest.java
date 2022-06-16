package com.siteminder.exam.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.siteminder.exam.validator.constraints.Email;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class EmailRequest {

	@Email
	private List<String> toEmails;
	
	@Email(optional = true)
	private List<String> ccEmails;
	
	@Email(optional = true)
	private List<String> bccEmails;
	
	@NotEmpty
	private String content;
	
	@NotEmpty
	private String subject;
}