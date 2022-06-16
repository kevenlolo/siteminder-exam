package com.siteminder.exam.rest;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.siteminder.exam.request.EmailRequest;
import com.siteminder.exam.service.EmailSenderService;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailSenderRestController {

	private final EmailSenderService emailSender;
	
	@PostMapping
	@Operation(summary = "Send email")
	public void sendEmail(@Valid @RequestBody EmailRequest emailRequest) {
		emailSender.send(emailRequest);
	}
}
