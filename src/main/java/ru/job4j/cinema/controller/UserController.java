package ru.job4j.cinema.controller;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.utility.HttpSessionUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * UserController - контроллер, обрабатывающий запросы от клиента и возвращающий результаты
 *
 * Класс является потокобезопасным. Проблема, возникающая при добавлении одинаковых пользователей в методе add()
 * решена на уровне базы данных с помощью ограничений. Поля email и phone должны быть уникальынми. Eсли две параллельные транзакции
 * выполнят запрос с одинаковой почтой и одинаковым номером телеофна, то та что будет быстрее выполнится,
 * а вторая вернется с ошибкой ConstrainsViolationException
 *
 * @author Ilya Kaltygin
 */
@Controller
@ThreadSafe
public class UserController {

    @GuardedBy("this")
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод возвращает представление с формой добавления нового пользователя
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @return представление addUser
     */
    @GetMapping("/formAddUser")
    public String registration(Model model, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        return "registration/addUser";
    }

    /**
     * Метод добавляет пользователя в базу данных
     * @param user Объект типа User
     * @return Переадресация по url /success если успешно, иначе /fail
     */
    @PostMapping("/registration")
    public String createUser(@ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            return "redirect:/fail";
        }
        return "redirect:/success";
    }

    /**
     * Метод возвращает представление с информацией об успешной регистрации пользователя
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @return Представление success
     */
    @GetMapping("/success")
    public String success(Model model, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        return "registration/success";
    }

    /**
     * Метод возвращает представление с информацией о неуспешной регистрации пользователя
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @return Представление fail
     */
    @GetMapping("/fail")
    public String fail(Model model, HttpSession session) {
        model.addAttribute("message", "Пользователь с такой почтой или номером телефона уже существует");
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        return "error/fail";
    }

    /**
     * Метод возвращает представление с формой для авторизации пользователя
     * @param model Модель с данными
     * @param fail Параметр запроса
     * @return представление login
     */
    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "authorization/login";
    }

    /**
     * Метод производит поиск пользователя в базе данных по данным, которые были получены из метода loginPage
     * @param user Объект типа User
     * @param request Объект типа HttpServletRequest
     * @return Если пользователь найден в базе данных произойдет переадресация по url /allSessions, иначе по url /loginPage?fail=true
     */
    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest request) {
        Optional<User> userDb = userService.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = request.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/allSessions";
    }

    /**
     * Метод очищает данные из сессии
     * @param session Объекти типа HttpSession
     * @return Происходит переадресация по url loginPage
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/loginPage";
    }
}
