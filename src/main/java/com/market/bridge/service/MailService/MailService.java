package com.market.bridge.service.MailService;

public interface MailService {
    public void sendEmailAlert(String recipient, String subject, String messageBody);
}
