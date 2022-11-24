package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;
import java.util.Collection;

/**
 * SessionRepository - интерфейс, описывающий поведения для работы с базой данных
 *
 * @author Ilya Kaltygin
 */
public interface SessionRepository {

    /**
     * Поиск всех сеансов в базе данных
     * @return Список объектов типа Session
     */
    public Collection<Session> findAll();

    /**
     * Добавление сеанса в базе данных
     * @param session Объект типа Session
     */
    public void add(Session session);

    /**
     * Поиск сеанса в базе данных по id
     * @param id id по которому будет производиться поиск
     * @return Найденный объект типа Session
     */
    public Session findById(int id);

    /**
     * Обновление сеанса в базе данных
     * @param session Обновленный объект типа Session
     */
    public void update(Session session);
}
