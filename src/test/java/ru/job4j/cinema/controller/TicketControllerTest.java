package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.utility.HttpSessionUtility;
import javax.servlet.http.HttpSession;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Тесты для контроллера TicketController
 *
 * @author Ilya Kaltygin
 */
class TicketControllerTest {

    /**
     * Метод возвращает представление с информацией об успешной покупкой(добавлении в базу данных) билета
     */
    @Test
    public void whenSuccess() {
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page = ticketController.success(model, httpSession);
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        verify(model).addAttribute("row", httpSession.getAttribute("row"));
        verify(model).addAttribute("place", httpSession.getAttribute("place"));
        verify(model).addAttribute("ses", httpSession.getAttribute("ses"));
        assertThat(page).isEqualTo("ticket/ticketSuccess");
    }

    /**
     * Метод возвращает представление с информацией о том, что билет куплен куплен(не добавлен в базу данных)
     */
    @Test
    public void whenFail() {
        HttpSession httpSession = mock(HttpSession.class);
        Model model = mock(Model.class);
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page = ticketController.fail(model, httpSession);
        verify(model).addAttribute("message", "Билет с данными посадочными местами уже занят");
        verify(model).addAttribute("user", HttpSessionUtility.checkSession(httpSession));
        assertThat(page).isEqualTo("error/ticketFail");
    }

    /**
     * Метод удаляет из сессии данные о ряде, месте и id сеанса и
     */
    @Test
    public void whenCancelAnOrder() {
        HttpSession httpSession = mock(HttpSession.class);
        TicketService ticketService = mock(TicketService.class);
        TicketController ticketController = new TicketController(ticketService);
        String page = ticketController.cancelAnOrder(httpSession);
        verify(httpSession).removeAttribute("sessionId");
        verify(httpSession).removeAttribute("row");
        verify(httpSession).removeAttribute("place");
        assertThat(page).isEqualTo("redirect:/allSessions");
    }

}