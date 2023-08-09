package ru.kotov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.BinaryContent;

public interface AppDocumentDAO extends JpaRepository<AppDocument, Long> {
}
