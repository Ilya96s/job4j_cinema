package ru.job4j.cinema.service;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;
import java.util.Collection;

/**
 * ImplSessionService - класс, описывающий бизнес логику приложения
 *
 * @author Ilya Kaltygin
 */
@Service
@ThreadSafe
public class ImplSessionService implements SessionService {

    @GuardedBy("this")
    private final SessionRepository sessionRepository;

    public ImplSessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    /**
     * Добавление объекта в базу данных
     * @param session Добавляемый объект типа Session
     */
    public void add(Session session) {
        sessionRepository.add(session);
    }

    /**
     * Поиск всех объектов в базе данных
     * @return Список всех объектов типа Session из базы данных
     */
    public Collection<Session> findAll() {
        return sessionRepository.findAll();
    }

    /**
     * Поиск объекта в базе данных по id
     * @param id id искомого объекта
     * @return Объект типа Session
     */
    public Session findById(int id) {
        return sessionRepository.findById(id);
    }

    /**
     * Обновление старого объекта типа Session в базе данных
     * @param session Новый объект типа Session
     */
    public void update(Session session) {
        sessionRepository.update(session);
    }
}
