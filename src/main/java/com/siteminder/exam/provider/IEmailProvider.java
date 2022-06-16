package com.siteminder.exam.provider;

import com.siteminder.exam.request.EmailRequest;

public interface IEmailProvider {

	boolean sendEmail(EmailRequest emailRequest);
}
