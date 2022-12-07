package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

/**
 * Тесты на класс PostgresTicketRepository
 *
 * @author Ilya Kaltygin
 */
class JdbcTicketRepositoryTest {

    /**
     * Загрузка настроек
     * @return Файл с настройками для подключения к базе данных
     */
    public static Properties loadDbProperties() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        JdbcTicketRepositoryTest.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return cfg;
    }

    /**
     * Создание объекта типа BasicDataSource
     * Внутри объекта создаются коннекты к базе данных, которые находятся в многопоточной очереди
     */
    public static BasicDataSource loadPool() {
        Properties cfg = loadDbProperties();
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
        return pool;
    }

    /**
     * Очищение таблиц tickets,sessions, users после внесенных изменений
     * Данный метод обозначен аннотацией @AfterEach - метод выполняется один раз после каждого теста
     */
    @AfterEach
    public void clearTable() throws SQLException {
        try (Connection connection = loadPool().getConnection();
             PreparedStatement preparedStatement1 = connection.prepareStatement("DELETE FROM tickets");
             PreparedStatement preparedStatement2 = connection.prepareStatement("DELETE FROM sessions");
             PreparedStatement preparedStatement3 = connection.prepareStatement("DELETE FROM users")) {
            preparedStatement1.execute();
            preparedStatement2.execute();
            preparedStatement3.execute();
        }
    }

    /**
     * Добавление билета в базу данных
     */
    @Test
    public void whenAddTicket() {
        JdbcSessionRepository sessionRepository = new JdbcSessionRepository(loadPool());
        Session session = new Session();
        session.setTitle("Film №1");
        session.setDesc("Desc №1");
        sessionRepository.add(session);

        JdbcUserRepository userRepository = new JdbcUserRepository(loadPool());

        User user = new User();
        user.setName("First user name");
        user.setPassword("First user password");
        user.setEmail("First user email");
        user.setPhone("First user phone");
        userRepository.add(user);

        JdbcTicketRepository ticketRepository = new JdbcTicketRepository(loadPool());
        Ticket ticket = new Ticket();
        ticket.setRow(1);
        ticket.setPlace(1);
        int userId = userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword()).get().getId();
        ticket.setUserId(userId);
        int sessionId = sessionRepository.findById(session.getId()).getId();
        ticket.setSessionId(sessionId);
        assertThat(ticketRepository.add(ticket).isPresent()).isTrue();
    }

    /**
     * Добавление в базу данных двух одинаковых билетов
     */
    @Test
    public void whenAdded1TicketAndNotAdded2Ticket() {
        JdbcSessionRepository sessionRepository = new JdbcSessionRepository(loadPool());
        Session session = new Session();
        session.setTitle("Film №1");
        session.setDesc("Desc №1");
        sessionRepository.add(session);

        JdbcUserRepository userRepository = new JdbcUserRepository(loadPool());

        User user = new User();
        user.setName("First user name");
        user.setPassword("First user password");
        user.setEmail("First user email");
        user.setPhone("First user phone");
        userRepository.add(user);

        JdbcTicketRepository ticketRepository = new JdbcTicketRepository(loadPool());
        Ticket ticket1 = new Ticket();
        ticket1.setRow(1);
        ticket1.setPlace(1);
        int userId = userRepository.findUserByEmailAndPassword(user.getEmail(), user.getPassword()).get().getId();
        ticket1.setUserId(userId);
        int sessionId = sessionRepository.findById(session.getId()).getId();
        ticket1.setSessionId(sessionId);

        Ticket ticket2 = new Ticket();
        ticket2.setRow(1);
        ticket2.setPlace(1);
        ticket2.setUserId(userId);
        ticket2.setSessionId(sessionId);

        assertThat(ticketRepository.add(ticket1).isPresent()).isTrue();
        assertThat(ticketRepository.add(ticket2).isPresent()).isFalse();
    }

}