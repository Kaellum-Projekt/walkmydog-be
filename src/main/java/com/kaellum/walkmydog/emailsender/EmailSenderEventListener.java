package com.kaellum.walkmydog.emailsender;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Component
@Log4j2
public class EmailSenderEventListener implements ApplicationListener<EmailSenderEvent>{

	private EmailService emailService;
	
	@Override
	public void onApplicationEvent(EmailSenderEvent event) {
		try {
			emailService.sendMail(event.getDto().getUserFullName(), event.getDto().getEmail(), event.getDto().getActivationCode());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, "Error sending activation email");
		}
		
	}

}
