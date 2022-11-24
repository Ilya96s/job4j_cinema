package ru.job4j.cinema.model;

import java.util.Objects;

/**
 * Session - модель, описывающая сеанс
 *
 * @author Ilya Kaltygin
 */
public class Session {
    /**
     * id сеанса
     */
    private int id;

    /**
     * Название Фильма
     */
    private String title;

    /**
     * Постер для фильма
     */
    private byte[] photo;

    /**
     * Описание
     */
    private String desc;

    public Session() {

    }

    public Session(int id, String title, String desc, byte[] photo) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.photo = photo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id == session.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
