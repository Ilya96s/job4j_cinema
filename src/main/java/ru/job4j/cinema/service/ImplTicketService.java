package ru.job4j.cinema.service;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Optional;

/**
 * ImplTicketService - класс, описывающий бизнес логику приложения
 *
 * @author Ilya Kaltygin
 */
@Service
@ThreadSafe
public class ImplTicketService implements TicketService {

    @GuardedBy("this")
    private final TicketRepository ticketRepository;

    public ImplTicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    /**
     * Добавление пользователя в базу данных
     * @param ticket Объект типа Ticket, который необходимо добавить в базу данных
     * @return Объект типа User, если такого объекта в базе данных нет
     */
    @Override
    public Optional<Ticket> add(Ticket ticket) {
        return ticketRepository.add(ticket);
    }
}
