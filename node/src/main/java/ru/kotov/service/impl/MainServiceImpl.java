package ru.kotov.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.kotov.dao.RawDataDAO;
import ru.kotov.entity.RawData;
import ru.kotov.service.MainService;
import ru.kotov.service.ProducerService;
@Service
@AllArgsConstructor
public class MainServiceImpl implements MainService {
    private final RawDataDAO rawDataDAO;
    public final ProducerService producerService;
    @Override
    public void processTextMessage(Update update) {
        saveRawData(update);

        var message = update.getMessage();
        var sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(message.getText());
        producerService.producerAnswer(sendMessage);
    }

    private void saveRawData(Update update) {
        RawData rawData = RawData.builder()
                .event(update)
                .build();
        rawDataDAO.save(rawData);
    }
}
