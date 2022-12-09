package ru.job4j.cinema.controller;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.utility.HttpSessionUtility;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * TicketController - контроллер, обрабатывающий запросы от клиента и возвращающий результаты
 *
 * Класс является потокобезопасным. Проблема, возникающая при добавлении одинаковых билетов в методе add()
 * решена на уровне базы данных с помощью ограничений. Поля sessionId, row и place должны быть уникальынми. Eсли две параллельные транзакции
 * выполнят запрос с одинаковым id сеанса, рядом и местом, то та что будет быстрее выполнится,
 * а вторая вернется с ошибкой ConstrainsViolationException
 *
 * @author Ilya Kaltygin
 */
@Controller
@ThreadSafe
public class TicketController {

    @GuardedBy("this")
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    /**
     * Метод сохраняет билет в базу данных
     * @param session Объект типа HttpSession
     * @return Переадресация по url /ticketSuccess если успешно, иначе по url /ticketFail
     */
    @GetMapping("/createTicket")
    public String createTicket(HttpSession session) {
        Session ses = (Session) session.getAttribute("ses");
        User user = (User) session.getAttribute("user");
        Ticket ticket = new Ticket();
        ticket.setSessionId(ses.getId());
        ticket.setRow((int) session.getAttribute("row"));
        ticket.setPlace((int) session.getAttribute("place"));
        ticket.setUserId(user.getId());
        Optional<Ticket> regTicket = ticketService.add(ticket);
        if (regTicket.isEmpty()) {
            return "redirect:/ticketFail";
        }
        return "redirect:/ticketSuccess";
    }

    /**
     * Метод возвращает представление с информацией об успешной покупкой(добавлении в базу данных) билета
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @return Представление success
     */
    @GetMapping("/ticketSuccess")
    public String success(Model model, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        model.addAttribute("row", session.getAttribute("row"));
        model.addAttribute("place", session.getAttribute("place"));
        model.addAttribute("ses", session.getAttribute("ses"));
        return "ticket/ticketSuccess";
    }

    /**
     * Метод возвращает представление с информацией о том, что билет куплен куплен(не добавлен в базу данных)
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @return Представление ticketFail
     */
    @GetMapping("/ticketFail")
    public String fail(Model model, HttpSession session) {
        model.addAttribute("message", "Билет с данными посадочными местами уже занят");
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        return "error/ticketFail";
    }

    /**
     * Метод удаляет из сессии данные о ряде, месте и id сеанса и
     * @param session Объект типа HttpSession
     * @return Переадресация по url /allSessions
     */
    @GetMapping("/cancelAnOrder")
    public String cancelAnOrder(HttpSession session) {
        session.removeAttribute("sessionId");
        session.removeAttribute("row");
        session.removeAttribute("place");
        return "redirect:/allSessions";
    }
}
