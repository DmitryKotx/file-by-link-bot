package ru.kotov.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.AppPhoto;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
}
