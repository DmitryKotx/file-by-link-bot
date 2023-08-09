package ru.kotov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotov.entity.AppDocument;
import ru.kotov.entity.AppPhoto;

public interface AppPhotoDAO extends JpaRepository<AppPhoto, Long> {
}
