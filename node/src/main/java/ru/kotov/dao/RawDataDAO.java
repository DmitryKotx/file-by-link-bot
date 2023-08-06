package ru.kotov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotov.entity.RawData;

public interface RawDataDAO extends JpaRepository<RawData, Long> {
}
