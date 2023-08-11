package ru.kotov.controller;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Log4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    private final UpdateProcessor updateProcessor;
    public TelegramBot(@Value("${bot.token}")String botToken, UpdateProcessor updateProcessor) {
        super(botToken);
        this.updateProcessor = updateProcessor;
    }
    @Override
    public String getBotUsername() {
        return botName;
    }

    @PostConstruct
    public void init() {
        updateProcessor.registerBot(this);
    }
    @Override
    public void onUpdateReceived(Update update) {
       updateProcessor.processUpdate(update);
    }
    public void sendAnswerMessage(SendMessage message) {
        if(message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(String.valueOf(e));
            }
        }
    }

}