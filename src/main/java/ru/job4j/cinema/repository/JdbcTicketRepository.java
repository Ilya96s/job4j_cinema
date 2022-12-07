package ru.job4j.cinema.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Ticket;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;

/**
 * PostgresTicketRepository - логика работы с базой данных
 *
 * @author Ilya Kaltygin
 */
@Repository
public class JdbcTicketRepository implements TicketRepository {

    private final DataSource dataSource;

    private static final Logger LOG = LoggerFactory.getLogger(JdbcTicketRepository.class.getName());

    private static final String ADD_TICKET = """
            INSERT INTO tickets(session_id, pos_row, cell, user_id)
            VALUES(?, ?, ?, ?)
            """;

    public JdbcTicketRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Добавление билета в базе данных
     * @param ticket Объект типа Ticket
     * @return Объект типа Optional<Ticket>
     */
    @Override
    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> result = Optional.empty();
        try (Connection cn = dataSource.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_TICKET, PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getRow());
            ps.setInt(3, ticket.getPlace());
            ps.setInt(4, ticket.getUserId());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    ticket.setId(rs.getInt(1));
                }
                result = Optional.of(ticket);
            }
        } catch (Exception e) {
            LOG.error("Exception in method .add(Ticket ticket)", e);
        }
        return result;
    }
}
