package lt.shgg.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * <h1>Класс пользователя</h1>
 * Этот класс является абстракцией над пользователем, здесь хранятся его логин и пароль
 */
public class User implements Serializable {
    /**
     * Логин - уникальный строковый идентификатор пользователя в системе
     */
    private String login;
    /**
     * Пароль - ключ доступа пользователя к системе. Может быть неуникален
     */
    private String password;
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 13L;

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public User(){}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        if (login.isEmpty()) throw new IllegalArgumentException("Логин не может быть пустым!");
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if (password.isEmpty()) throw new IllegalArgumentException("Пароль не может быть пустым!");
        if (password.length() < 4) throw new IllegalArgumentException("Слишком короткий пароль!");
        this.password = password;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || this.getClass() != other.getClass()) return false;
        User otherUser = (User) other;
        return this.login.equals(otherUser.getLogin());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.login) * 23;
    }
}
