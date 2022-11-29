package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.utility.HttpSessionUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для контроллера UserController
 *
 * @author Ilya Kaltygin
 */
class UserControllerTest {

    /**
     * Метод возвращает представление с формой добавления нового пользователя
     */
    @Test
    public void whenRegistration() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.registration(model, httpSession);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("registration/addUser");
    }

    /**
     * Метод добавляет пользователя в базу данных
     * Переадресация по url /success если успешно
     */
    @Test
    public void whenCreateUser() {
        User user = new User(1, "Name", "Password", "Email", "Phone");
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        when(userService.add(user)).thenReturn(Optional.of(user));
        userController.createUser(user);
        verify(userService).add(user);
        String page = userService.add(user).isEmpty() ? "redirect:/fail" : "redirect:/success";
        assertThat(page).isEqualTo("redirect:/success");
    }

    /**
     * Метод добавляет пользователя в базу данных
     * Переадресация по url /fail если добавление не успешно
     */
    @Test
    public void whenNotCreateUser() {
        User user = new User(1, "Name", "Password", "Email", "Phone");
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        when(userService.add(user)).thenReturn(Optional.empty());
        userController.createUser(user);
        verify(userService).add(user);
        String page = userService.add(user).isEmpty() ? "redirect:/fail" : "redirect:/success";
        assertThat(page).isEqualTo("redirect:/fail");
    }

    /**
     * Метод возвращает представление с информацией об успешной регистрации пользователя
     */
    @Test
    public void whenSuccess() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.success(model, httpSession);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("registration/success");
    }

    /**
     * Метод возвращает представление с информацией о неуспешной регистрации пользователя
     */
    @Test
    public void whenFail() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.fail(model, httpSession);
        verify(model).addAttribute("message", "Пользователь с такой почтой или номером телефона уже существует");
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("error/fail");
    }

    /**
     * Метод возвращает представление с формой для авторизации пользователя
     */
    @Test
    public void whenLoginPage() {
        Model model = mock(Model.class);
        Boolean fail = true;
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.loginPage(model, fail);
        verify(model).addAttribute("fail", fail != null);
        assertThat(page).isEqualTo("authorization/login");
    }

    /**
     * Метод производит поиск пользователя в базе данных по данным, которые были получены из метода loginPage
     * Если пользователь найден в базе данных произойдет переадресация по url /allSessions
     */
    @Test
    public void whenLogin() {
        Optional<User> user = Optional.of(new User(1, "Name", "Password", "Email", "Phone"));
        HttpSession httpSession = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        when(userService.findUserByEmailAndPassword(user.get().getEmail(), user.get().getPassword())).thenReturn(user);
        when(request.getSession()).thenReturn(httpSession);
        userController.login(user.get(), request);
        String page = userService.findUserByEmailAndPassword(user.get().getEmail(), user.get().getPassword()).isEmpty() ? "redirect:/loginPage?fail=true" : "redirect:/allSessions";
        verify(httpSession).setAttribute("user", user.get());
        assertThat(page).isEqualTo("redirect:/allSessions");
    }

    /**
     * Метод производит поиск пользователя в базе данных по данным, которые были получены из метода loginPage
     * Если пользователь не найден в базе данных произойдет переадресация по url /loginPage?fail=true
     */
    @Test
    public void whenNotLogin() {
        Optional<User> user = Optional.of(new User(1, "Name", "Password", "Email", "Phone"));
        HttpServletRequest request = mock(HttpServletRequest.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        when(userService.findUserByEmailAndPassword(user.get().getEmail(), user.get().getPassword())).thenReturn(Optional.empty());
        userController.login(user.get(), request);
        String page = userService.findUserByEmailAndPassword(user.get().getEmail(), user.get().getPassword()).isEmpty() ? "redirect:/loginPage?fail=true" : "redirect:/allSessions";
        assertThat(page).isEqualTo("redirect:/loginPage?fail=true");
    }

    /**
     * Метод очищает данные из сессии
     */
    @Test
    public void whenLogOut() {
        HttpSession httpSession = mock(HttpSession.class);
        UserService userService = mock(UserService.class);
        UserController userController = new UserController(userService);
        String page = userController.logout(httpSession);
        verify(httpSession).invalidate();
        assertThat(page).isEqualTo("redirect:/loginPage");
    }
}