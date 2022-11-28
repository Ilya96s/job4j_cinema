package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.Session;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

/**
 * Тесты на класс PostgresSessionRepository
 *
 * @author Ilya Kaltygin
 */
class PostgresSessionRepositoryTest {

    /**
     * Загрузка настроек
     * @return Файл с настройками для подключения к базе данных
     */
    public static Properties loadDbProperties() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        PostgresSessionRepositoryTest.class.getClassLoader()
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
     * Очищение таблицы sessions после внесенных изменений
     * Данный метод обозначен аннотацией @AfterEach - метод выполняется один раз после каждого теста
     */
    @AfterEach
    public void clearTable() throws SQLException {
        try (Connection connection = loadPool().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM sessions")) {
            preparedStatement.execute();
        }
    }

    /**
     * Добавление и поиск всех сеансов в базе данных
     */
    @Test
    void whenAddAndThenFindAll() {
        PostgresSessionRepository repository = new PostgresSessionRepository(loadPool());

        Session session1 = new Session();
        session1.setTitle("Film №1");
        session1.setDesc("Desc №1");

        Session session2 = new Session();
        session2.setTitle("Film №2");
        session2.setDesc("Desc №2");

        Session session3 = new Session();
        session3.setTitle("Film №3");
        session3.setDesc("Desc №3");

        Session session4 = new Session();
        session4.setTitle("Film №4");
        session4.setDesc("Desc №4");

        repository.add(session1);
        repository.add(session2);
        repository.add(session3);
        repository.add(session4);

        Collection<Session> sessionList = repository.findAll();
        assertThat(sessionList.size()).isEqualTo(4);
    }

    /**
     * Добавление и поиск сеанса по id в базе данных
     */
    @Test
    void whenAddThenFindSession() {
        PostgresSessionRepository repository = new PostgresSessionRepository(loadPool());

        Session session = new Session();
        session.setTitle("Film №1");
        session.setDesc("Desc №1");
        repository.add(session);

        Session sessionInDb = repository.findById(session.getId());
        assertThat(sessionInDb).isEqualTo(session);
    }

    /**
     * Обновление сеанса в базе данных
     */
    @Test
    void whenUpdateSession() {
        PostgresSessionRepository repository = new PostgresSessionRepository(loadPool());

        Session session1 = new Session();

        session1.setTitle("Film №1");
        session1.setDesc("Desc №1");
        repository.add(session1);

        Session session2 = new Session();
        session2.setId(session1.getId());
        session2.setTitle("Updated Film №3");
        session2.setDesc("Updated Desc №3");
        repository.update(session2);

        Session sessionInDb = repository.findById(session1.getId());
        assertThat(sessionInDb.getTitle()).isEqualTo(session2.getTitle());
        assertThat(sessionInDb.getDesc()).isEqualTo(session2.getDesc());
    }
}