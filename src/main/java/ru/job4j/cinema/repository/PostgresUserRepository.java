package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * PostgresUserRepository - логика работы с базой данных
 *
 * @author Ilya Kaltygin
 */
@Repository
public class PostgresUserRepository implements UserRepository {
    private final DataSource pool;

    private static final Logger LOG = LoggerFactory.getLogger(PostgresUserRepository.class.getName());

    private static final String ADD_USER = """
            INSERT INTO users(username, password, email, phone)
            VALUES(?, ?, ?, ?)
            """;

    private static final String FIND_USER_BY_EMAIL_AND_PASSWORD = """
            SELECT * FROM users
            WHERE email = ?
            AND password = ?
            """;

    public PostgresUserRepository(DataSource pool) {
        this.pool = pool;
    }


    /**
     * Добавление пользователя в базе данных
     * @param user Объект типа User
     * @return Объект типа Optional<User>
     */
    @Override
    public Optional<User> add(User user) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(ADD_USER, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
                result = Optional.of(user);
            }
        }  catch (Exception e) {
            LOG.error("Exception in method .add(User user)", e);
        }
        return result;
    }

    /**
     * Поиск пользователя в базе данных по email и номеру телефона
     * @param email Email пользователя
     * @param password Номер телефона пользователя
     * @return найденный Объект типа Optional<User>
     */
    @Override
    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
            PreparedStatement ps = cn.prepareStatement(FIND_USER_BY_EMAIL_AND_PASSWORD)
        ) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result = Optional.of(createUser(rs));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in method .findUserByEmailAndPassword(String email, String password", e);
        }
        return result;
    }

    /**
     * Создание объекта типа User
     * @param rs Объект типа ResultSet из которого получаем данные
     * @return Объект типа User
     * @throws SQLException
     */
    private User createUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
