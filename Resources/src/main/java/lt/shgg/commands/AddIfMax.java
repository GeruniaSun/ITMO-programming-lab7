package lt.shgg.commands;

import lt.shgg.app.Receiver;
import lt.shgg.data.Ticket;
import lt.shgg.data.User;
import lt.shgg.database.DatabaseManager;
import lt.shgg.network.Response;

import java.io.Serial;
import java.io.Serializable;

/**
 * <h1>Команда add_if_max</h1>
 * класс инкапсулирующий объект команды add_if_max
 */
public class AddIfMax implements Command, Serializable {
    /**
     * Номер версии сериализации нужен, чтоб JVM понимала, что это один и тот же класс на клиенте и на сервере
     */
    @Serial
    private static final long serialVersionUID = 902L;
    /**
     * Переопределенные методы из интерфейса {@link Command}
     * логика описана в самом интерфейсе
     */
    @Override
    public Response execute(Object args, Ticket ticket, Receiver receiver, User user) {
        if (args != null) throw new IllegalArgumentException("Команда add_if_max не принимает никаких аргументов, " +
                "значение element нужно вводить с новой строки");
        if (ticket == null) throw new IllegalArgumentException("Команда add_if_max не работает без билета");
        var dataBaseManager = new DatabaseManager();
        long id = dataBaseManager.addIfMax(ticket);
        if (id == -1) {
            return new Response("Не удалось выполнить команду");
        } else if (id == -2) {
            return new Response("Элемент не добавлен в коллекцию он не такой уж и большой ");
        } else {
            ticket.setId(id);
            return receiver.addIfMax(ticket);
        }
    }

    @Override
    public String description() {
        return "add_if_max {element} - добавляет элемент в коллекцию, если он превышает максимальный";
    }


    @Override
    public String getName(){
        return "add_if_max";
    }
}