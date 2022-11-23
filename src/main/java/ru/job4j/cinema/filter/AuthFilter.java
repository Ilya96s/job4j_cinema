package ru.job4j.cinema.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * AuthFilter - фильтр с помощью которого осуществляется доступ к ресурсам перед запросом
 *
 * @author Ilya Kaltygin
 */
@Component
public class AuthFilter implements Filter {

    private static final Set<String> URLS = Set.of("posterSession", "allSessions", "login", "loginPage", "fail", "formAddUser", "registration", "success");

    /**
     * Через этот метод будут проходить запросы к сервлетам.
     * Если запрос идет к адресам allSessions, login, allSessions, fail, registration, success то мы их пропускаем сразу.
     * Если запросы идут к другим адресам, то проверяем наличие пользователя в HttpSession
     * Если его нет, то мы переходим на страницу авторизации.
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (checkURL(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean checkURL(String uri) {
        return AuthFilter.URLS.stream()
                .anyMatch(uri::contains);
    }
}

