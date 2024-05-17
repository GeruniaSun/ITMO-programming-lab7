package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.data.User;
import lt.shgg.database.DatabaseManager;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда add</h1>
 * класс инкапсулирующий объект команды add
 */
public class Add implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 901L;
    /**
     * Переопределенные методы из интерфейса {@link lt.shgg.commands.Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver, User user) {
        if (args != null)
            throw new IllegalArgumentException("Команда add не принимает никаких аргументов, кроме билета");
        if (ticket == null) throw new IllegalArgumentException("Команда add не работает без билета");
        var databaseManager = new DatabaseManager();
        long id = databaseManager.addObject(ticket);
        if (id == -1) {
            return new Response("Не получилось добавить элемент в базу данных");
        } else {
            ticket.setId(id);
            return receiver.add(ticket);
        }

    }

    @Override
    public String description() {
        return "add - добавляет элемент в коллекцию";
    }

    @Override
    public String getName(){
        return "add";
    }
}
