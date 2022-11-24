package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;
import java.util.Optional;

/**
 * UserService - интерфейс, описывающий поведения для бизнес логики приложения
 *
 * @author Ilya Kaltygin
 */
public interface UserService {

    /**
     * Добавление пользователя в базу данных
     * @param user Объект типа User, который необходимо добавить в базу данных
     * @return Объект типа User, если такого объекта в базе данных нет
     */
    public Optional<User> add(User user);

    /**
     * Поиск пользователя в базе данных по почте и номеру телефона
     * @param email Email пользователя
     * @param password Номер телфона
     * @return Объект типа User
     */
    public Optional<User> findUserByEmailAndPassword(String email, String password);
}
