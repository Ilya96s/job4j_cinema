package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.utility.HttpSessionUtility;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Collection;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для контроллера SessionController
 *
 * @author Ilya Kaltygin
 */
class SessionControllerTest {

    /**
     * Метод возвращет представление со списком всех сеансов из базы данных
     */
    @Test
    public void whenAllFilms() {
        Collection<Session> sessionCollection = Arrays.asList(
                new Session(1, "title 1", "desc 1", new byte[]{1, 2}),
                new Session(2, "title 2", "desc 2", new byte[]{3, 4}),
                new Session(3, "title 3", "desc 3", new byte[]{5, 6})
        );
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        when(sessionService.findAll()).thenReturn(sessionCollection);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.allFilms(model, httpSession);
        verify(model).addAttribute("sessions", sessionCollection);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("session/sessions");
    }

    /**
     * Метод возвращает представление с возможностью для редактированием всех сеансов
     */
    @Test
    public void whenEditAllSessions() {
        Collection<Session> sessionCollection = Arrays.asList(
                new Session(1, "title 1", "desc 1", new byte[]{1, 2}),
                new Session(2, "title 2", "desc 2", new byte[]{3, 4}),
                new Session(3, "title 3", "desc 3", new byte[]{5, 6})
        );
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        when(sessionService.findAll()).thenReturn(sessionCollection);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.editAllSessions(model, httpSession);
        verify(model).addAttribute("sessions", sessionCollection);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("session/editAllSessions");
    }

    /**
     * Метод возвращает представление с формой добавления нового сеанса
     */
    @Test
    public void whenAddSession() {
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.addSession(model, httpSession);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("session/addSession");
    }

    /**
     * Метод добавляет сеанс в базу данных
     * @throws Exception
     */
    @Test
    public void whenCreateSession() throws Exception {
        Session session = new Session(1, "title 1", "desc 1", new byte[]{1, 2});
        MultipartFile file = mock(MultipartFile.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.createSession(session, file);
        verify(sessionService).add(session);
        assertThat(page).isEqualTo("redirect:/allSessions");
    }

    /**
     * Метод возвращает представление с формой для редактирования сеанса
     */
    @Test
    public void whenUpdateSession() {
        Session session = new Session(1, "title 1", "desc 1", new byte[]{1, 2});
        int id = 1;
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        when(sessionService.findById(id)).thenReturn(session);
        String page = sessionController.updateSession(model, id, httpSession);
        verify(model).addAttribute("ses", sessionService.findById(id));
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("session/updateSession");
    }

    /**
     * Метод обновляет сеанс в базе данных
     */
    @Test
    public void whenUpdate() {
        Session session = new Session(1, "title 1", "desc 1", new byte[]{1, 2});
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.updateSession(session);
        verify(sessionService).update(session);
        assertThat(page).isEqualTo("redirect:/allSessions");
    }

    /**
     * Метод возвращает представление с информацией о фильме и формой для выбора ряда
     */
    @Test
    public void whenSelectRow() {
        Session session = new Session(1, "title 1", "desc 1", new byte[]{1, 2});
        int id = 1;
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        when(sessionService.findById(id)).thenReturn(session);
        String page = sessionController.selectRow(model, id, httpSession);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        verify(model).addAttribute("ses", sessionService.findById(id));
        verify(httpSession).setAttribute("ses", sessionService.findById(id));
        assertThat(page).isEqualTo("session/selectRow");
    }

    /**
     * Метод возвращает представление с информацией о сеансе и формой выбора места
     */
    @Test
    public void whenSelectPlace() {
        Session session = new Session(1, "title 1", "desc 1", new byte[]{1, 2});
        int id = 1;
        int row = 1;
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        when(sessionService.findById(id)).thenReturn(session);
        String page = sessionController.selectPlace(model, id, httpSession, row);
        verify(model).addAttribute("ses", sessionService.findById(id));
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        verify(httpSession).setAttribute("row", row);
        assertThat(page).isEqualTo("session/selectPlace");
    }

    /**
     * Метод возвращает представление с информацией о сеансе, номере ряда и номере места
     */
    @Test
    public void whenAboutSession() {
        int place = 1;
        Model model = mock(Model.class);
        HttpSession httpSession = mock(HttpSession.class);
        SessionService sessionService = mock(SessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        String page = sessionController.aboutSession(model, httpSession, place);
        verify(model).addAttribute("ses", httpSession.getAttribute("ses"));
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        verify(model).addAttribute("place", place);
        verify(model).addAttribute("row", httpSession.getAttribute("row"));
        verify(httpSession).setAttribute("place", place);
        assertThat(page).isEqualTo("session/aboutSession");
    }

}