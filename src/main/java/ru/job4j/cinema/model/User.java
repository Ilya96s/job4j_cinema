package ru.job4j.cinema.model;

import java.util.Objects;

/**
 * User - модель, описывающая пользователя
 *
 * @author Ilya Kaltygin
 */
public class User {
    /**
     * id пользователя
     */
    private int id;

    /**
     * Имя пользователя
     */
    private String name;

    /**
     * Пароль пользователя
     */
    private String password;

    /**
     * Email пользователя
     */
    private String email;

    /**
     * Номер телефона пользователя
     */
    private String phone;

    public User() {

    }

    public User(int id, String name, String password, String email, String phone) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
