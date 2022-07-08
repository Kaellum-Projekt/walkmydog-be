package com.kaellum.walkmydog.emailsender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EmailSenderEventPublisher {
	
	@Autowired
    private ApplicationEventPublisher applicationEventPublisher;
	
	public void publishEmailSenderEvent(final EmailDetailsDtos dto) {
		EmailSenderEvent event = new EmailSenderEvent(this, dto);
		applicationEventPublisher.publishEvent(event);
	}

}
