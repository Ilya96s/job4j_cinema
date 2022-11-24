package ru.job4j.cinema.controller;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.utility.HttpSessionUtility;
import javax.servlet.http.HttpSession;


/**
 * SessionController - контроллер, обрабатывающий запросы от клиента и возвращающий результаты
 */
@Controller
@ThreadSafe
public class SessionController {

    @GuardedBy("this")
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Метод возвращет представление со списком всех сеансов из базы данных
     * @param model Модель с данными
     * @param session объект типа HttpSession
     * @return представление allFilms
     */
    @GetMapping("/allSessions")
    public String allFilms(Model model, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        model.addAttribute("sessions", sessionService.findAll());
        return "session/sessions";
    }

    /**
     * Метод возвращает представление с возможностью для редактированием всех сеансов
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @return представление editAllSessions
     */
    @GetMapping("/editAllSessions")
    public String editAllSessions(Model model, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        model.addAttribute("sessions", sessionService.findAll());
        return "session/editAllSessions";
    }

    /**
     * Метод возвращает представление с формой добавления нового сеанса
     * @param model Модель данных
     * @return представление addSession
     */
    @GetMapping("/formAddSession")
    public String addSession(Model model, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        return "session/addSession";
    }

    /**
     * Метод добавляет сеанс в базу данных
     * @param session Объект типа Session из формы по добавление сеанса
     * @return перенаправляет на URL /allSessions
     */
    @PostMapping("/createSession")
    public String createSession(@ModelAttribute Session session,
                                @RequestParam("file") MultipartFile file) throws Exception {
        session.setPhoto(file.getBytes());
        sessionService.add(session);
        return "redirect:/allSessions";
    }

    /**
     * Метод возвращает представление с формой для редактирования сеанса
     * @param model Модель данных
     * @param id id редактируемого сеанса
     * @return представление с формой ввода новых данных для сеанса
     */
    @GetMapping("/formUpdateSession/{sessionId}")
    public String updateSession(Model model, @PathVariable("sessionId") int id, HttpSession session) {
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        model.addAttribute("ses", sessionService.findById(id));
        return "session/updateSession";
    }

    /**
     * Метод обновляет сеанс в базе данных
     * @param session Объект типа Session полученный из формы ввода
     * @return перенаправляет на URL /allSessions
     */
    @PostMapping("/updateSession")
    public String updateSession(@ModelAttribute Session session) {
        sessionService.update(session);
        return "redirect:/allSessions";
    }

    /**
     * Метод преобразует массив байт в строку в кодировке BASE64. В свою очередь браузер преобразует ее в изображение
     * @param sessionId id сеанса
     */
    @GetMapping("/posterSession/{sessionId}")
    public ResponseEntity<Resource> download(@PathVariable("sessionId") Integer sessionId) {
        Session session = sessionService.findById(sessionId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(session.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(session.getPhoto()));
    }

    /**
     * Метод возвращает представление с информацией о фильме и формой для выбора ряда
     * @param model Модель с данными
     * @param id id сеанса
     * @return представление selectRow в котором пользователь должен выбрать ряд
     */
    @GetMapping("/selectRow/{sessionId}")
    public String selectRow(Model model, @PathVariable("sessionId") int id, HttpSession session) {
        model.addAttribute("ses", sessionService.findById(id));
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        session.setAttribute("ses", sessionService.findById(id));
        return "session/selectRow";
    }

    /**
     * Метод возвращает представление с информацией о сеансе и формой выбора места
     * @param model Модель с данными
     * @param id id сеанса
     * @param session Объект типа HttpSession
     * @param row номер ряда, полученный из метода selectRow
     * @return представление selectPlace в котором пользователь должен выбрать место
     */
    @GetMapping("/selectPlace/{sessionId}")
    public String selectPlace(Model model, @PathVariable("sessionId") int id, HttpSession session, @RequestParam("row") int row) {
        model.addAttribute("ses", sessionService.findById(id));
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        session.setAttribute("row", row);
        return "session/selectPlace";
    }

    /* Перенести метод в контроллер билета */
    /**
     * Метод возвращает представление с информацией о сеансе, номере ряда и номере места
     * @param model Модель с данными
     * @param session Объект типа HttpSession
     * @param place номер места, полученный из метода selectPlace
     * @return представление aboutSession в котором представлена информация о сеансе
     */
    @GetMapping("/aboutSession")
    public String aboutSession(Model model, HttpSession session, @RequestParam("place") int place) {
        model.addAttribute("ses", session.getAttribute("ses"));
        model.addAttribute("user", HttpSessionUtility.checkSession(session));
        model.addAttribute("place", place);
        model.addAttribute("row", session.getAttribute("row"));
        session.setAttribute("place", place);
        return "session/aboutSession";
    }
}
