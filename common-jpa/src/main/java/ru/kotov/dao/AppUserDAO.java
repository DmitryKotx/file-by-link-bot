package ru.kotov.dao;

import ru.kotov.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppUserDAO extends JpaRepository<AppUser, Long> {
    AppUser findAppUserByTelegramUserId(Long id);
}
