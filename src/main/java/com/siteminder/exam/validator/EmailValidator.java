package com.siteminder.exam.validator;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.collections.CollectionUtils;

import com.siteminder.exam.validator.constraints.Email;

public class EmailValidator implements ConstraintValidator<Email, List<String>>{

	private final static org.apache.commons.validator.routines.EmailValidator EMAIL_VALIDATOR
			= org.apache.commons.validator.routines.EmailValidator.getInstance();
	
	private boolean optional;
	
	@Override
    public void initialize(Email emailAnnotation) {
        optional = emailAnnotation.optional();
    }
	
	@Override
	public boolean isValid(List<String> emails, ConstraintValidatorContext context) {
        boolean emailsValid = true;

        if (!CollectionUtils.isEmpty(emails)) {
            emailsValid = emails.stream().anyMatch(email -> EMAIL_VALIDATOR.isValid(email));
        }
        else {
            emailsValid = optional;
        }
		
		return emailsValid;
	}
}