package ru.kotov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import ru.kotov.controller.UpdateProcessor;
import ru.kotov.service.AnswerConsumerService;

import static ru.kotov.RabbitQueue.ANSWER_MESSAGE;

@Service
@AllArgsConstructor
public class AnswerConsumerServiceImpl implements AnswerConsumerService {
    private final UpdateProcessor updateProcessor;

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        updateProcessor.setView(sendMessage);
    }
}