package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.data.User;
import lt.shgg.database.DatabaseManager;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда clear</h1>
 * класс инкапсулирующий объект команды clear
 */
public class Clear implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 903L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver, User user) {
        if (args != null) throw new IllegalArgumentException("Команда clear не принимает никаких аргументов");
        var databaseManager = new DatabaseManager();
        databaseManager.clear(user.getLogin());
        return receiver.clear(user.getLogin());
    }

    @Override
    public String description() {
        return "clear - удаляет все элементы из коллекции";
    }

    @Override
    public String getName(){
        return "clear";
    }
}
