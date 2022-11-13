package ru.job4j.cinema.model;

import java.util.Objects;

/**
 * Ticket - модель, описывающая билет
 *
 * @author Ilya Kaltygin
 */
public class Ticket {

    /**
     * id билета
     */
    private int id;

    /**
     * id сеанса
     */
    private int sessionId;

    /**
     * Ряд
     */
    private int row;

    /**
     * Место
     */
    private int place;

    /**
     * id пользователя
     */
    private int userId;

    public Ticket() {

    }

    public Ticket(int id, int sessionId, int row, int place, int userId) {
        this.id = id;
        this.sessionId = sessionId;
        this.row = row;
        this.place = place;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
