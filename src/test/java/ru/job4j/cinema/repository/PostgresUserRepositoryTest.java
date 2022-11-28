package ru.job4j.cinema.repository;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.model.User;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

/**
 * Тесты на класс PostgresUserRepository
 *
 * @author Ilya Kaltygin
 */
class PostgresUserRepositoryTest {

    /**
     * Загрузка настроек
     * @return Файл с настройками для подключения к базе данных
     */
    public static Properties loadDbProperties() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        PostgresUserRepositoryTest.class.getClassLoader()
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
     * Очищение таблицы users после внесенных изменений
     * Данный метод обозначен аннотацией @AfterEach - метод выполняется один раз после каждого теста
     */
    @AfterEach
    public void clearTable() throws SQLException {
        try (Connection connection = loadPool().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users")) {
            preparedStatement.execute();
        }
    }

    /**
     * Тест на добавление пользователя в базу данных
     */
    @Test
    void whenAddUser() {
        PostgresUserRepository repository = new PostgresUserRepository(loadPool());

        User user = new User();
        user.setName("name");
        user.setPassword("pass");
        user.setEmail("email");
        user.setPhone("phone");
        Optional<User> userInDb = repository.add(user);

        assertThat(userInDb.isPresent()).isTrue();
        assertThat(userInDb.get().getPhone()).isEqualTo(user.getPhone());
        assertThat(userInDb.get().getName()).isEqualTo(user.getName());
        assertThat(userInDb.get().getPassword()).isEqualTo(user.getPassword());
        assertThat(userInDb.get().getPhone()).isEqualTo(user.getPhone());
    }

    /**
     * Тест на поиск пользователя в базе данных по почте и номеру телеофна
     */
    @Test
    void findUserByEmailAndPassword() {
        PostgresUserRepository repository = new PostgresUserRepository(loadPool());

        User user1 = new User();
        user1.setName("First user name");
        user1.setPassword("First user password");
        user1.setEmail("First user email");
        user1.setPhone("First user phone");

        User user2 = new User();
        user2.setName("Second user name");
        user2.setPassword("Second user password");
        user2.setEmail("Second user email");
        user2.setPhone("Second user phone");

        User notAddedUser = new User();
        notAddedUser.setName("Third user name");
        notAddedUser.setPassword("Third user password");
        notAddedUser.setEmail("Third user email");
        notAddedUser.setPhone("Third user phone");

        repository.add(user1);
        repository.add(user2);

        Optional<User> user1InDb = repository.findUserByEmailAndPassword(user1.getEmail(), user1.getPassword());
        Optional<User> user2InDb = repository.findUserByEmailAndPassword(user2.getEmail(), user2.getPassword());
        Optional<User> wrong = repository.findUserByEmailAndPassword(notAddedUser.getEmail(), notAddedUser.getPassword());

        assertThat(user1InDb.isPresent()).isTrue();
        assertThat(user2InDb.isPresent()).isTrue();
        assertThat(wrong.isEmpty()).isTrue();

        User user1FromOptional = user1InDb.get();
        User user2FromOptional = user2InDb.get();

        assertThat(user1FromOptional.getName()).isEqualTo(user1.getName());
        assertThat(user1FromOptional.getPassword()).isEqualTo(user1.getPassword());
        assertThat(user1FromOptional.getEmail()).isEqualTo(user1.getEmail());
        assertThat(user1FromOptional.getPhone()).isEqualTo(user1.getPhone());

        assertThat(user2FromOptional.getName()).isEqualTo(user2.getName());
        assertThat(user2FromOptional.getPassword()).isEqualTo(user2.getPassword());
        assertThat(user2FromOptional.getEmail()).isEqualTo(user2.getEmail());
        assertThat(user2FromOptional.getPhone()).isEqualTo(user2.getPhone());
    }
}