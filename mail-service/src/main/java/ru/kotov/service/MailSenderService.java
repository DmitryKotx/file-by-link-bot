package ru.kotov.service;

import ru.kotov.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
