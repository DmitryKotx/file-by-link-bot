package ru.kotov.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kotov.entity.BinaryContent;

public interface BinaryContentDAO extends JpaRepository<BinaryContent, Long> {
}
