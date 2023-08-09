package ru.kotov.service;

import org.telegram.telegrambots.meta.api.objects.Message;
import ru.kotov.entity.AppDocument;

public interface FileService {
    AppDocument processDoc(Message externalMessage);
}
