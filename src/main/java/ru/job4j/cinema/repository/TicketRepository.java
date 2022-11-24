package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;
import java.util.Optional;

/**
 * TicketRepository - интерфейс, описывающий поведения для работы с базой данных
 *
 * @author Ilya Kaltygin
 */
public interface TicketRepository {

    /**
     * Добавление билета в базе данных
     * @param ticket Объект типа Ticket
     * @return Объект типа Optional<Ticket>
     */
    Optional<Ticket> add(Ticket ticket);
}
