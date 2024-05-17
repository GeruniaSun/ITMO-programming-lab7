package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.data.User;
import lt.shgg.database.DatabaseManager;
import lt.shgg.database.DatabaseParser;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда remove_by_id</h1>
 * класс инкапсулирующий объект команды remove_by_id
 */
public class RemoveById implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 911L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver, User user) {
        if (args == null)
            throw new NullPointerException("Команда remove_by_id не работает без аргумента id");
        long id;
        try {
            id = Long.parseLong((String) args);
        } catch (Exception e) {
            throw new IllegalArgumentException("аргумент id должен быть числом");
        }
        var databaseManager = new DatabaseManager();
        if (databaseManager.removeById(id, user.getLogin())) return receiver.removeById(id);
        else return new Response("То ли объект не ваш, то ли id такого нет, но ужалить не получается");
    }

    @Override
    public String description() {
        return "remove_by_id id - удаляет из коллекции элемент с айди id";
    }

    @Override
    public String getName(){
        return "remove_by_id";
    }
}
