package ru.job4j.cinema.service;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;
import java.util.Optional;

/**
 * ImplUserService - класс, описывающий бизнес логику приложения
 *
 * @author Ilya Kaltygin
 */
@Service
@ThreadSafe
public class ImplUserService implements UserService {

    @GuardedBy("this")
    private final UserRepository userRepository;

    public ImplUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Добавление пользователя в базу данных
     * @param user Объект типа User, который необходимо добавить в базу данных
     * @return Объект типа User, если такого объекта в БД нет
     */
    @Override
    public Optional<User> add(User user) {
        return userRepository.add(user);
    }

    /**
     * Поиск пользователя в базе данных по email и номеру телефона
     * @param email Email пользователя
     * @param password Номер телефона
     * @return Объект типа User
     */
    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        return userRepository.findUserByEmailAndPassword(email, password);
    }
}
