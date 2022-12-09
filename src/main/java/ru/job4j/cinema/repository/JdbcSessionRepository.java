package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.sql.DataSource;

/**
 * PostgresSessionRepository - логика работы с базой данных
 *
 * @author Ilya Kaltygin
 */
@Repository
@ThreadSafe
public class JdbcSessionRepository implements SessionRepository {

    private final DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(JdbcSessionRepository.class.getName());

    private static final String FIND_ALL = """
            SELECT * FROM sessions
            """;

    private static final String ADD_SESSION = """
            INSERT INTO sessions(title, description, photo)
            VALUES(?, ?, ?)
            """;

    private static final String FIND_SESSION_BY_ID = """
            SELECT * FROM sessions
            WHERE id = ?
            """;

    private static final String UPDATE_SESSION = """
            UPDATE sessions
            SET title = ?, description = ?
            WHERE id = ?
            """;

    public JdbcSessionRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Поиск всех сеансов в базе данных
     * @return Список объектов типа Session
     */
    @Override
    public Collection<Session> findAll() {
        List<Session> sessions = new ArrayList<>();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(createSession(rs));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in method .findAll()", e);
        }
        return sessions;
    }

    /**
     * Добавление сеанса в базе данных
     * @param session Объект типа Session
     */
    @Override
    public void add(Session session) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_SESSION, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getTitle());
            ps.setString(2, session.getDesc());
            ps.setBytes(3, session.getPhoto());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in method .add(Session session", e);
        }
    }

    /**
     * Поиск сеанса в базе данных по id
     * @param id id по которому будет производиться поиск
     * @return Найденный объект типа Session
     */
    @Override
    public Session findById(int id) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_SESSION_BY_ID)
        ) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return createSession(rs);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in method .findById(int id)", e);
        }
        return null;
    }

    /**
     * Обновление сеанса в базе данных
     * @param session Обновленный объект типа Session
     */
    @Override
    public void update(Session session) {
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE_SESSION)
        ) {
            ps.setString(1, session.getTitle());
            ps.setString(2, session.getDesc());
            ps.setInt(3, session.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception in method .update(Session session)", e);
        }
    }

    /**
     * Создание объекта типа Session
     * @param rs Объект типа ResultSet из которого получаем данные
     * @return Объект типа Session
     * @throws SQLException
     */
    private Session createSession(ResultSet rs) throws SQLException {
        return new Session(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBytes("photo")
        );
    }
}
