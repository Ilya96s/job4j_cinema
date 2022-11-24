package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import java.util.Optional;

/**
 * TicketService - интерфейс, описывающий поведения для бизнес логики приложения
 *
 * @author Ilya Kaltygin
 */
public interface TicketService {

    /**
     * Добавление пользователя в базу данных
     * @param ticket Объект типа Ticket, который необходимо добавить в базу данных
     * @return Объект типа User, если такого объекта в базе данных нет
     */
    public Optional<Ticket> add(Ticket ticket);
}
