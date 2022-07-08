package com.kaellum.walkmydog.emailsender;

import org.springframework.context.ApplicationEvent;

import lombok.Getter;

@SuppressWarnings("serial")
@Getter
public class EmailSenderEvent extends ApplicationEvent{
	private EmailDetailsDtos dto;
	
	public EmailSenderEvent(Object source, EmailDetailsDtos dto) {
		super(source);
		this.dto = dto;
	}
}
