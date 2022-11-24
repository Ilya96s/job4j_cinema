package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;
import java.util.Collection;

/**
 * SessionService - интерфейс, описывающий поведения для бизнес логики приложения
 *
 * @author Ilya Kaltygin
 */
public interface SessionService {

    /**
     * Добавление сеанса в базу данных
     * @param session Добавляемый объект типа Session
     */
    public void add(Session session);

    /**
     * Поиск всех сеансов в базе данных
     * @return Список всех объектов типа Session из базы данных
     */
    public Collection<Session> findAll();

    /**
     * Поиск сеанса в базе данных по id
     * @param id id искомого объекта
     * @return Объект типа Session
     */
    public Session findById(int id);

    /**
     * Обновление старого сеанса типа Session в базе данных
     * @param session Новый объект типа Session
     */
    public void update(Session session);
    }
