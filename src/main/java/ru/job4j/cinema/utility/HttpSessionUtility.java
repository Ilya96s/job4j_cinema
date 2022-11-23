package ru.job4j.cinema.utility;

import ru.job4j.cinema.model.User;
import javax.servlet.http.HttpSession;

/**
 * HttpSessionUtility - утилитный класс для првоерки данных в объекте HttpSession
 *
 * @author Ilya Kaltygin
 */
public class HttpSessionUtility {

    private HttpSessionUtility() {

    }

    /**
     * Метод проверяет данные в объекте HttpSession.
     * Если в сессии по ключу "user" лежит объект, то возвращаем его, иначе создаем новый объект типа User и возвращаем его
     * @param session Объект типа HttpSession
     * @return Объект типа User
     */
    public static User checkSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setName("Гость");
        }
        return user;
    }
}
