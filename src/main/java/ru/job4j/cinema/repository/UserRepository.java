package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;
import java.util.Optional;

/**
 * UserRepository - интерфейс, описывающий поведения для работы с базой данных
 *
 * @author Ilya Kaltygin
 */
public interface UserRepository {

    /**
     * Добавление пользователя в базе данных
     * @param user объект типа User
     * @return Объект типа Optional<User>
     */
    public Optional<User> add(User user);

    /**
     * Поиск пользователя в базе данных по email и номеру телефона
     * @param email Email пользователя
     * @param password Номер телефона пользователя
     * @return найденный Объект типа Optional<User>
     */
    public Optional<User> findUserByEmailAndPassword(String email, String password);
}
